package com.dream.mis.core.web.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dream.mis.core.utils.CommonUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@TableBind(tableName="td_role",pkName="role_id")
public class Role extends Model<Role>{
	private static final long serialVersionUID = -8066564850233284003L;
	
	private static String msg = "";
	private static String status = "y";
	public static Role dao = new Role();
	public List<Function> getResources(String role_id){
		return Function.dao.find("select * from td_function where function_id in(select function_id from tr_role_function where role_id = ?)",role_id);
	}
	public Page<Record> page(int pageNumber,int pageSize,Map<String, String[]> dataMap){
		String condition = "";
		return Db.paginate(pageNumber, pageSize, "select * ", " from td_role where 1=1 " + condition + " ");
	}
	/**
	 * 查询全部角色
	 * @return
	 */
	public List<Record> findAll(String searchKey){
		StringBuffer sb = new StringBuffer(" where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(StrKit.notBlank(searchKey)){
			sb.append(" and role_name like ? ");
			params.add("%"+searchKey+"%");
			sb.append(" or role_desc like ? ");
			params.add("%"+searchKey+"%");
		}
		return Db.find("select * from td_role "+sb.toString(),params.toArray());
	}
	public Record saveRole(Role role,String function_ids) throws Exception{
		if(StrKit.notBlank(role.getStr("role_id"))){//update 
			role.update();
			Db.update("delete from tr_role_function where role_id=?",role.getStr("role_id"));
			RoleFunction.dao.saveFunc(function_ids,role.getStr("role_id"));
			status = "y";
			msg = "更新成功！";
		} else { // new save
			role.set("role_id", CommonUtils.getUUID());
			role.save();
			RoleFunction.dao.saveFunc(function_ids,role.getStr("role_id"));
			status = "y";
			msg = "保存成功！";
		}
		return new Record().set("status", status).set("msg", msg); 
	}
	/**
	 * 删除角色功能关联表
	 * @param role_id
	 * @return 
	 */
	public int delRFunction(String role_id){
		return Db.update("delete from tr_role_function where role_id=?",role_id);
	}
	public Record findFirstRoleById(String role_id){
		return Db.findFirst("select * from td_role where role_id=?", role_id);
	}
	
}
