package com.dream.mis.core.web.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dream.mis.core.utils.CommonUtils;
import com.dream.mis.core.web.controller.WebSocketController;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@TableBind(tableName="td_info_notice",pkName="notice_id")
public class Notice extends Model<Notice>{
	private static final long serialVersionUID = 8574125570715711373L;
	public static final Notice dao = new Notice();
	private static String msg = "";
	private static String status = "y";

	public Page<Record> page(int pageNumber,int pageSize,Map<String, String[]> dataMap){
		String exceptSelect = " from td_info_notice a,td_user b where a.create_user_id=b.user_id ";
		List<Object> params = new ArrayList<>();
		if(StrKit.notBlank(dataMap.get("search[value]"))){
			exceptSelect += " and notice_title LIKE ? ";
			params.add("%"+dataMap.get("search[value]")[0] + "%");
		}
		if(StrKit.notBlank(dataMap.get("order[0][column]"))){
			exceptSelect += " order by " + getOrderByColumn(dataMap) + " " + getDesc_Asc(dataMap);
		}
		Page<Record> page = Db.paginate(pageNumber, pageSize, "select a.notice_id,a.notice_title,a.receive_type,b.user_cn_name,a.create_time ", exceptSelect,params.toArray());
		return page;
	}
	
	/**
	 * 得到当前排序的字段
	 * @param dataMap
	 * @return
	 */
	public Object getOrderByColumn(Map<String, String[]> dataMap){
		return dataMap.get("columns["+dataMap.get("order[0][column]")[0]+"][data]")[0];
	}
	
	/**
	 * 得到当前字段的排序方式
	 * @param dataMap
	 * @return desc asc
	 */
	public Object getDesc_Asc(Map<String, String[]> dataMap){
		return dataMap.get("order[0][dir]")[0];
	}
	
	public Record saveNotice(Notice notice,String create_user_id,String user_ids) throws Exception {
		notice.set("notice_id", CommonUtils.getUUID());
		notice.set("create_user_id", create_user_id);
		notice.save();
		if("1".equals(notice.getStr("receive_type"))){
			List<Record> users=Db.find("select user_id from td_user where is_del=0 ");
			if(users!=null){
				for (Record user : users) {
					user.set("notice_id", notice.getStr("notice_id"));
					boolean b=Db.save("tr_notice_user", user);
					if(b) {//消息保存成功，提醒客户端有新消息
						WebSocketController.sendMessage(user.getStr("user_id"));
					}
				}
			}
		}else {
			saveRNoticeUser(notice,user_ids);
		}
		status = "y";
		msg = "发布成功！";
		return new Record().set("status", status).set("msg", msg).set("notice",notice); 
	}
	
	
	private void saveRNoticeUser(Notice notice, String user_ids) throws Exception {       
		if(StrKit.notBlank(user_ids)){
			String[] user_ids_arr = user_ids.split(",");
			for (String user_id : user_ids_arr) {
				Record record = new Record();
				record.set("notice_id", notice.getStr("notice_id"));
				record.set("user_id", user_id);
				boolean b=Db.save("tr_notice_user", record);
				if(b) {//消息保存成功，提醒客户端有新消息
					WebSocketController.sendMessage(user_id);
				}
			}
		}
	}
	

	/**
	 * 批量删除
	 */
	public Record batchDelete(String notice_id){
		if(StrKit.notBlank(notice_id)){
			String notice_ids[] = notice_id.split(",");
			for (String id : notice_ids) {
				this.deleteById(id);
				this.delRNoticeUser(id);
			}
		}
		return new Record().set("status", status).set("msg", "删除成功！"); 
	}
	
	
	/**
	 * 删除通知用户的关联表
	 */
	public void delRNoticeUser(String notice_id){
		Db.update("delete from tr_notice_user where notice_id=?",notice_id);
	}
	
	/**
	 * @param notice_id
	 * @return 通知接收人user_ids
	 */
	public String findNoticeUserIds(String notice_id){
		return Db.queryStr("select GROUP_CONCAT(user_id) from tr_notice_user where notice_id=?",notice_id);
	}
	
	/**
	 * 得到通知的视图对象
	 * @param notice_id
	 * @return
	 */
	public Record findVNotice(String notice_id){
		return Db.findFirst("select * from v_notice where notice_id=?",notice_id);
	}
	
	/**
	 * 通过user_id得到通知分页数据
	 * @param pageNumber
	 * @param pageSize
	 * @param dataMap
	 * @return 
	 */
	public Page<Record> pageByUserId(int page,int pageSize,String user_id){
		String sql="select notice_id,notice_title,receive_type,date_format(create_time,'%Y-%m-%d  %H:%i:%s') create_time,is_read ";
		String exceptSelect ="from v_notice "
				+ "where user_id=? "
				+ "order by is_read, create_time desc ";
		Page<Record> notices=Db.paginate(page, pageSize, sql, exceptSelect,user_id);
		return notices;
	}
	
	/**
	 * 已经阅读通知的人员
	 */
	public String hasReadUser(String notice_id){
		return Db.queryStr("select GROUP_CONCAT(user_id) from tr_notice_user where notice_id=? and is_read=1",notice_id);
	}
	
	/**
	 * 已经阅读通知的人员
	 */
	public String findUserByIsRead(String notice_id,int is_read){
		return Db.queryStr("select GROUP_CONCAT(b.user_cn_name) from tr_notice_user a LEFT JOIN td_user b on a.user_id=b.user_id where notice_id=? and is_read=?",notice_id,is_read);
	}
	
	public List<Record> findMyNotice(String user_id, String type, int is_read) {
		if(0 == is_read){
			return Db.find("select  a.notice_id,a.notice_title as notice_title,a.notice_abstract,a.content,a.create_time,b.is_read from td_notice a left join r_notice_recipient b on a.notice_id = b.notice_id where a.type_id = ? and  b.is_read = 0 and b.user_id = ? order by a.create_time desc ", type, user_id);
		}else{
			return Db.find("select  a.notice_id,a.notice_title as notice_title,a.notice_abstract,a.content,a.create_time,b.is_read from td_notice a left join r_notice_recipient b on a.notice_id = b.notice_id where a.type_id = ? and b.user_id = ? order by a.create_time desc ", type, user_id);
		}
	}
	
	
	public List<Record> findMyNotice(String user_id){
		return Db.find("select  a.notice_id,a.notice_title as notice_title,a.notice_abstract,a.content,a.create_time,b.is_read from td_notice a left join r_notice_recipient b on a.notice_id = b.notice_id where  b.is_read = 0 and b.user_id = ? order by a.create_time desc ", user_id);
	}
	
	/**
	 * @param webRoot
	 * @return
	 */
	public List<Record> roleUserJson(String webRoot){
		String sql="select * from " + 
				"(SELECT a.user_id as id,a.user_cn_name as text,a.user_role_id as pid, "+ 
				"'"+webRoot+"/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/user.png' as icon , " +
				"1 as isuser  from v_user a where a.is_del=0  " + 
				"UNION  all " + 
				"(SELECT b.role_id as id,b.role_name as text,'-1' as pid, "+
				"'"+webRoot+"/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/group.png' as icon," + 
				"0 as isuser from td_role b order by b.org_order)) c";
		return Db.find(sql);
	}

	/**
	 * 获取未读数量
	 * @return
	 */
	public long getNoReadCount(String user_id) {
		Long count=Db.queryLong("select count(*) from v_notice where is_read=0 and user_id=?", user_id);
		return count;
	}
	
	/**
	 * 设置已读
	 * @return
	 */
	public Record read(String id,String user_id) {
		Record record =new Record();
		Date date=new Date(System.currentTimeMillis());
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now=format.format(date);
		List<String> params=new ArrayList<>();
		String sql="update tr_notice_user " + 
				"set is_read=1 ,read_time=?" + 
				"where user_id=? ";
		params.add(now);
		params.add(user_id);
		if(id!=null&&!"".equals(id)) {
			sql+=" and notice_id =?";
			params.add(id);
		}
		int i=Db.update(sql,params.toArray());
		if(i>=1) {
			record.set("status", 0);
			record.set("msg", "更新成功！");
		}else {
			record.set("status", 1);
			record.set("msg", "更新失败！");
		}
		return record;
	}
}
