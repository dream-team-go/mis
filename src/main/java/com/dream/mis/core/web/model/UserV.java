package com.dream.mis.core.web.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="v_user",pkName="user_id")
public class UserV extends Model<UserV>{
	private static final long serialVersionUID = 8574125570715711373L;
	public static final UserV dao = new UserV();
	public UserV findByUsernameExceptDel(String username) {
		return UserV.dao.findFirst("select * from v_user where is_del=0 and username = ?", username);
	}
	/**
	 * 查找同级别机构机构管理员用户（不包括自己）
	 *//*
	public List<UserV> findSameLevelUsers(String org_id,String user_id){
		return UserV.dao.find("select * from v_user where org_id = ? and is_org_admin = 1 and user_id <> ?",org_id,user_id);
	}
	*//**
	 * 查找上级机构管理员
	 *//*
	public List<UserV> findTopLevelUsers(String org_id){
		return UserV.dao.find("select * from v_user where org_id = ? and is_org_admin = 1",org_id);
	}*/
}
