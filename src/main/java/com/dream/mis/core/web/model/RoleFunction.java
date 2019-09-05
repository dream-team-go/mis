package com.dream.mis.core.web.model;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

@TableBind(tableName = "tr_role_function")
public class RoleFunction extends Model<RoleFunction> {

	private static final long serialVersionUID = -3625780054778190122L;
	private static String msg = "";
	private static String status = "y";
	public static RoleFunction dao = new RoleFunction();
	
	public void saveFunc(String function_ids,String role_id){
		if(StrKit.notBlank(function_ids)){
			String function_id[]=function_ids.split(",");
			for (String string : function_id) {
				this.set("role_id", role_id);
				this.set("function_id", string);
				this.save();
			}
		}
	}
	
	public List<Record> findRoleById(String role_id){
		List<Record> list =  Db.find("select b.* from tr_role_function a left join td_function b on a.function_id=b.function_id where a.role_id=? and is_parent=1 order by function_order desc",role_id);
		if (!list.isEmpty()) {
			List<Record> functionsList = new ArrayList<Record>();
			for (Record l : list) {
				Record r = new Record();
				r.set("parent", l);
				List<Record> childList = Db.find("select b.* from tr_role_function a left join td_function b on a.function_id=b.function_id where a.role_id=? and p_id=? order by function_order desc",role_id,l.getStr("function_id"));
				r.set("childList", childList);
				functionsList.add(r);
			}
			return functionsList;
		}
		return null;
	}
	
	public List<Record> findByRoleId(String webRoot,String role_id){
		return Db.find("select b.function_id,b.p_id,b.`name`,'"+webRoot+"/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/fun_url.png' as icon from tr_role_function a left join td_function b on a.function_id=b.function_id where a.role_id=? and is_menu=1 order by function_order desc",role_id);
	}
	
	public List<Record> findUserFunctionByRoleId(String role_id){
		return Db.find("select b.function_id,b.p_id,b.name  from tr_role_function a left join td_function b on a.function_id=b.function_id where a.role_id=? ",role_id);
	}
	
	/**
	 * 如果菜单被删除，既要删除关联数据,以免存在垃圾数据导致系统隐患
	 */
	public void delRoleFunction(String function_id){
		Db.update("delete from tr_role_function where function_id=?",function_id);
	}
}
