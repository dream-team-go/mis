package com.dream.mis.core.web.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.dream.mis.core.enums.WarnType;
import com.dream.mis.core.shiro.SimpleUser;
import com.dream.mis.core.utils.CommonUtils;
import com.dream.mis.core.web.controller.WebSocketController;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 消息提醒model
 * @author by
 *
 */
@TableBind(tableName="td_info_warn",pkName="warn_id")
public class Warn extends Model<Warn>{
	private static final long serialVersionUID = -3625780054778190122L;
	private static String msg = "成功";
	private static String status = "y";
	public static Warn dao = new Warn();
	
	/**
	 * 生成重要日志提醒
	 */
	public void makeLogWarn(String user_id,String log_content,String actionKey) {
		//根据存的日志 生成提醒消息
		User user=User.dao.findById(user_id);
		String userName=user.getStr("username");
		String title="用户："+userName+" 有一条操作记录";
		String content="操作内容："+log_content+"；<br/>";
		content+="操作用户："+userName+"；<br/>";
		content+="访问方法："+actionKey;
		Warn.dao.saveWarn(title, content,WarnType.LOG.getIndex());
	}
	
	/**
	 *保存提醒消息 
	 */
	public void saveWarn(String title,String content,int warn_type_id) {
		//查询提醒类型对象
		Record warnType=Db.findFirst("select * from td_warn_type where type_id=?",warn_type_id);
		//消息保存
		Warn warn=new Warn();
		String warn_id=CommonUtils.getUUID();
		warn.set("warn_id", warn_id);
		warn.set("warn_title",title);
		warn.set("content", content);
		UserV user = ((SimpleUser)SecurityUtils.getSubject().getPrincipal()).getUser();
		warn.set("create_user_id",user.getStr("user_id"));
		warn.set("type_id", warn_type_id);
		warn.save();
		//消息与用户对应关系保存
		List<Record> users=new ArrayList<Record>();
		if(warnType.getInt("receive_type")==2) {
			users=Db.find("select user_id from tr_warn_type_user where type_id=?",warn_type_id);
		}else {
			//查出提醒类型对应内容列表有权限的用户
			String url=warnType.getStr("link_url");
			String allUser_sql="select user_id from td_user " + 
					"where is_del=0 ";
					if(url!=null&&!"".equals(url)) {
					allUser_sql+="and user_role_id in ( " + 
					"select DISTINCT c.role_id from tr_role_function c,td_function d " + 
					"where d.function_id=c.function_id " + 
					"and d.function_url='"+url+"' )";
					}
					users=Db.find(allUser_sql);
		}	
		for(Record u:users) {
			u.set("warn_id", warn_id);
			boolean i=Db.save("tr_warn_user", u);
			if(i) {//保存成功提醒客户端有新消息
				WebSocketController.sendMessage(u.getStr("user_id"));
			}
		}
	}
	/**
	 * 获取提醒类型
	 * @return
	 */
	public List<Record> getWarnType() {
		String sql="select * from td_warn_type";
		List<Record> list=Db.find(sql);
		return list;
	}
	/**
	 * 根据type_id获取对应的接收用户
	 * @return
	 */
	public List<Record> getUsersByType(String type_id){
		String sql="select * from tr_warn_type_user where type_id=?";
		List<Record> users=Db.find(sql,type_id);
		return users;
	}
	
	/**
	 * 保存消息提醒设置
	 */
	public void saveSet(String type_id,String[] users,String receive_type) {
		//查询提醒类型对应的列表url
		String url=Db.queryStr("select link_url from td_warn_type where type_id=?",type_id);
		String sql="update td_warn_type set receive_type=? where type_id=?";
		//查出提醒类型对应内容列表有权限的用户
		String allUser_sql="select user_id from td_user " + 
				"where is_del=0 ";
				if(url!=null&&!"".equals(url)) {
				allUser_sql+="and user_role_id in ( " + 
				"select DISTINCT c.role_id from tr_role_function c,td_function d " + 
				"where d.function_id=c.function_id " + 
				"and d.function_url='"+url+"' )";
				}
		List<Record> all_user=Db.find(allUser_sql);
			List<Record> listUser=Db.find("select * from tr_warn_type_user where type_id=?",type_id);
			Db.tx(() ->{
				Db.update(sql,receive_type,type_id);
				if(listUser!=null) {
					for(int i=0;i<listUser.size();i++) {
						Db.delete("tr_warn_type_user", listUser.get(i));
					}
				}
				if("2".equals(receive_type)) {
					for(int i=0;i<users.length;i++) {
						boolean is_contains=false;
						for(int j=0;j<all_user.size();j++) {
							if(all_user.get(j).getStr("user_id").equals(users[i])) {
								is_contains=true;
							}
						}
						if(is_contains) {
							Record record=new Record();
							record.set("type_id", type_id);
							record.set("user_id", users[i]);
							Db.save("tr_warn_type_user", record);
						}
					}
				}
				return true;
			});
	}
	
	/**
	 * 分页获取warn
	 * @return
	 */
	public Page<Record> pageByUserId(int page,int pagesize,String user_id){
		String sql="select warn_id,warn_title,type_name,link_url,url_des,"
				+ "date_format(create_time,'%Y-%m-%d  %H:%i:%s') create_time,is_read ";
		String exceptSelect= "from v_warn "
				+ "where user_id=? "
				+ "order by is_read, create_time desc";
		Page<Record> warns=Db.paginate(page, pagesize, sql, exceptSelect,user_id);
		return warns;
	}
	/**
	 * 获取未读数量
	 * @return
	 */
	public long getNoReadCount(String user_id) {
		Long count=Db.queryLong("select count(*) from v_warn where is_read=0 and user_id=?", user_id);
		return count;
	}
	
	/**
	 * 已读设置
	 * @return
	 */
	public Record read(String id,String user_id) {
		Record record =new Record();
		Date date=new Date(System.currentTimeMillis());
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now=format.format(date);
		List<String> params=new ArrayList<>();
		String sql="update tr_warn_user " + 
				"set is_read=1 ,read_time=?" + 
				"where user_id=? ";
		params.add(now);
		params.add(user_id);
		if(id!=null&&!"".equals(id)) {
			sql+=" and warn_id =?";
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
	/**
	 * 得到提醒的视图对象
	 * @return
	 */
	public Record findVNotice(String warn_id) {
		String sql="select * from v_warn where warn_id=?";
		Record record=Db.findFirst(sql,warn_id);
		return record;
	}
}
