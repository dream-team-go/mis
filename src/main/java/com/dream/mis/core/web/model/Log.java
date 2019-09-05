package com.dream.mis.core.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dream.mis.core.enums.WarnType;
import com.dream.mis.core.utils.CommonUtils;
import com.dream.mis.core.utils.IPUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@TableBind(tableName = "td_log", pkName = "log_id")
public class Log extends Model<Log> {

	private static final long serialVersionUID = -3625780054778190122L;
	private static String msg = "";
	private static String status = "y";
	public static Log dao = new Log();
	
	
	public Page<Record> page(int pageNumber,int pageSize,Map<String, String[]> dataMap,UserV userV){
		String exceptSelect = " from v_log a where 1=1 ";
		
		List<Object> params = new ArrayList<>();
		if(StrKit.notBlank(dataMap.get("search[value]"))){
			exceptSelect += " and log_content LIKE ? ";
			params.add("%"+dataMap.get("search[value]")[0] + "%");
		}
		if(StrKit.notBlank(dataMap.get("order[0][column]"))){
			exceptSelect += " order by " + getOrderByColumn(dataMap) + " " + getDesc_Asc(dataMap);
		}
		Page<Record> page = Db.paginate(pageNumber, pageSize, "select a.*", exceptSelect,params.toArray());
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
	
	/**
	 * 日志全量分页数据
	 * @param pageNumber
	 * @param pageSize
	 * @param kw
	 * @return
	 */
	public Page<Log> page(int pageNumber,int pageSize,String kw){
		String condition = "";
		return this.paginate(pageNumber, pageSize, "select * ", " from td_log where 1=1 " + condition + " ");
	}
	
	/**
	 * 日志视图v_log全量数据分页
	 * @param pageNumber
	 * @param pageSize
	 * @param start_date
	 * @param end_date
	 * @param user_cn_name
	 * @return
	 */
	public Page<Record> pageAll(int pageNumber,int pageSize,String start_date,String end_date,String user_cn_name){
		return Db.paginate(pageNumber, pageSize, "select * ", " from v_log order by log_create_time desc");
	}
	
	/**
	 * 根据用户user_id获取用户的所有相关日志
	 * @param pageNumber
	 * @param pageSize
	 * @param user_id
	 * @return
	 */
	public Page<Record> pageMine(int pageNumber,int pageSize,String user_id){
		return Db.paginate(pageNumber, pageSize, "select * ", " from v_log where  user_id=?  order by log_create_time desc ",user_id);
	}
	
	/**
	 * 保存日志方法
	 * @param user_id 操作人用户id
	 * @param log_content 日志内容，记录详细的日志描述
	 * @param log_type 日志类型，用来区分不同操作的日志
	 * @param log_type_desc 当前日志的简要描述，如：用户操作
	 * @param request 当前请求对象
	 */
	public void saveLog(String user_id,String log_content,HttpServletRequest request,String actionKey,String request_params){
		this.set("log_id", CommonUtils.getUUID());
		this.set("user_id", user_id);
		this.set("log_content", log_content);
		this.set("log_ip", IPUtils.getIpAddr(request));
		this.set("log_request_method", actionKey);
		this.set("request_params", request_params);
		this.save();
	}
	
	/**
	 * 查询某人的上一次的登录日志
	 * @param user_id
	 * @return
	 */
	public Log findUserLatestLoginLog(String user_id){
		return this.findFirst("select * from td_log where user_id=? order by log_create_time desc",user_id);
	}
	
	/**
	 * 获取指定用户的登录日志，如下所示：<br>
		本月登录总数：263<br>
		本次登录IP：192.168.5.39<br>
		本次登录时间：2012-09-03 17:21<br>
		上次登录IP：192.168.0.72<br>
		上次登录时间：2012-09-01 10:28<br>
	 * @param user_id
	 * @return
	 */
	public Record logInfo(String user_id){
		String loginTimeOfMonth = "SELECT * FROM td_log WHERE datediff(month,[log_create_time],getdate())=0 and log_type=0 and user_id=? order by log_create_time desc ";
		List<Record> list = Db.find(loginTimeOfMonth,user_id);
		Record info = new Record();
		if (list != null && list.size() != 0) {
			info.set("loginTimeOfMonth", list.size());
			info.set("thisLoginIp", list.get(0).getStr("log_ip"));
			info.set("thisLoginTime", list.get(0).getDate("log_create_time"));
			if(list.size() == 1){
				info.set("thisLoginTime", list.get(0).getDate("log_create_time"));
				info.set("beforeLoginIp", "");
				info.set("beforeLoginTime", "");
			} else {
				info.set("beforeLoginIp",  list.get(1).getStr("log_ip"));
				info.set("beforeLoginTime", list.get(1).getDate("log_create_time"));
			}
		}
		return info;
	}
	
	/**
	 * 获取指定用户所有的日志总数
	 * @param user_id
	 * @return
	 */
	public int loginTimes(String user_id){
		return Db.queryInt("select count(*) from td_log where user_id=?",user_id);
	}
	
	public Record findVlog(String log_id){
		return Db.findFirst("select * from v_log where log_id=?",log_id);
	}
	
	/**
	 * 保存定时任务日志，只有操作描述，其他内容都为空
	 * @param log_content
	 */
	public void saveJobLog(String log_content){
		new Log().set("log_id", CommonUtils.getUUID()).set("log_content", log_content).save();
	}
	
}
