package com.dream.mis.core.baseController;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dream.mis.core.shiro.SimpleUser;
import com.dream.mis.core.utils.DataMap;
import com.dream.mis.core.utils.PropertyUtils;
import com.dream.mis.core.web.model.SystemInfo;
import com.dream.mis.core.web.model.UserV;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * Author : by <br>
 * 
 */
abstract public class BaseController extends Controller {
	
	protected int pageSize = 50;    //分页使用：每页显示条数
	protected int weixinPageSize = 10;    //分页使用：每页显示条数
	protected Page<Record> page ;   //分页
	protected DataMap dataMap;
	protected String client;        //客户端类型
	protected boolean mobile;      //是否移动设备
	
	public Record successJson(String msg){
		return new Record().set("status", "y").set("msg", msg);
	}
	public Record successJson(String msg,Object o){
		return new Record().set("status", "y").set("msg", msg).set("o", o);
	}
	public Record failJson(String msg){
		return new Record().set("status", "n").set("msg", msg);
	}
	
	/**
	 * 获取当前页
	 * @return
	 */ 
	public int getPageNumber() {
		return getParaToInt("start")==0?1:((getParaToInt("start")/getParaToInt("length",20))+1);
	}
	
	//获取当前IP，判断是否是合法IP
	public boolean allowIpAccess(String Ip){
		String ip_str = PropertyUtils.getPropertyValue("safeIp");
		if(ip_str.indexOf(Ip)>-1){
			return true;
		}
		return false;
	}
	
	/**
	 * @param request the client to set
	 */
	public void setClient(HttpServletRequest request) {
		this.client="PC";
		this.mobile=false;
		String user_agent=request.getHeader("user-agent");
		if (StringUtils.isNotEmpty(user_agent)) {
			user_agent=user_agent.toUpperCase();
			if (StringUtils.indexOf(user_agent, "IPHONE")>-1) {
				this.mobile=true;
				this.client="IPHONE";
			}
			if (StringUtils.indexOf(user_agent, "LINUX")>-1) {
				this.mobile=true;
				this.client="LINUX";
			}
		}
	}
	
	
	/**
	 * 手机端交互JSON
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public JSONObject getJson(){
		JSONObject json = null;
		String p = getPara("p");
		if(StringUtils.isNotEmpty(p)){
			json = JSON.parseObject(p);
		}
		return json;
	}
	
	/**
	 * 生成Least的UUID
	 * @return
	 */
	public String generateUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public Page<Record> getPage() {
		return page;
	}

	public void setPage(Page<Record> page) {
		this.page = page;
	}
	
	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}
	/**
	 * @return the mobile
	 */
	public boolean isMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}
	
	/**
	 * @desc 获取当前登录用户
	 * @return
	 * User
	 */
	public UserV getCurrentUser() {
		return ((SimpleUser)SecurityUtils.getSubject().getPrincipal()).getUser();
	}
	
	
	/**
	 * 获得系统信息
	 * @param code
	 * @return
	 */
	public String getSystemInfo(String code){
		Map<String, Object> map = getSessionAttr("systemInfos");
		return map.get(code).toString();
	}
}
