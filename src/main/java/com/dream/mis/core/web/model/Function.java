package com.dream.mis.core.web.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dream.mis.core.utils.CommonUtils;
import com.jfinal.aop.Before;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.CacheName;

@TableBind(tableName="td_function",pkName="function_id")
public class Function extends Model<Function>{
	private static final long serialVersionUID = 9211624651403729790L;
	private static String msg = "";
	private static String status = "y";
	public static Function dao = new Function();
	private List<Function> subMenu;
	public Page<Record> page(int pageNumber,int pageSize,Map<String, String[]> dataMap){
		String condition = "";
		if(StrKit.notBlank(dataMap.get("param1"))){
			condition += " and a.name like '%" + dataMap.get("param1")[0] + "%'";
		}
		if(StrKit.notBlank(dataMap.get("type"))){
			int type = Integer.parseInt(dataMap.get("type")[0]);
			if(type!=-1){
				condition += " and a.is_parent=" + type;
			}
		}
		if(StrKit.notBlank(dataMap.get("p_id"))){
			if(!dataMap.get("p_id")[0].equals("all")){
				condition += " and a.p_id = '" + dataMap.get("p_id")[0] + "'";
			}
		}
		condition += " order by function_order asc";
		Page<Record> page = Db.paginate(pageNumber, pageSize, "select a.*,b.name as p_name ", "from td_function a left join td_function b on a.p_id=b.function_id where 1=1 " + condition);
		return page;
	}
	
	public Record saveFunc(Function func) throws Exception{
		if(StrKit.notBlank(func.getStr("function_id"))){//update 
			if("0".equals(func.getStr("p_id"))){
				func.set("is_parent", 1);
			}else{
				func.set("is_parent", 0);
			}
			func.update();
			status = "y";
			msg = "更新成功！";
		} else { // new save
			func.set("function_id", CommonUtils.getUUID());
			if("0".equals(func.getStr("p_id"))){
				func.set("is_parent", 1);
			}else{
				func.set("is_parent", 0);
			}
			func.save();
			status = "y";
			msg = "保存成功！";
		}
		return new Record().set("status", status).set("msg", msg); 
	}
	/**
	 * 查询父级菜单
	 * @param searchKey
	 * @return
	 */
	public List<Record> findParent(String searchKey){
		StringBuffer buffer = new StringBuffer("where 1=1 ");
		List<Object> params  = new ArrayList<Object>();
		if(StrKit.notBlank(searchKey)){
			buffer.append(" and name like ? or function_url like ? or resource_name like ?");
			params.add("%"+searchKey+"%");
			params.add("%"+searchKey+"%");
			params.add("%"+searchKey+"%");
		}
		List<Record> list = Db.find("select * from td_function "+buffer.toString()+" order by p_id,function_order asc",params.toArray());
		return list;
	}
	/**
	 * 根据父ID查询子权限
	 * @param p_id
	 * @return
	 */
	public List<Record> findSub(String p_id){
		return Db.find("select * from td_function where p_id=? order by function_order asc",p_id);
	}
	/**
	 * ztree json 
	 * @return
	 */
	public List<Record> find4Tree(String webRoot){
		return Db.find("select * from td_function order by function_order asc");
	}
	/**
	 * 获取菜单
	 * @return
	 */
	public List<Function> findMenu(String role_id){
		List<Function> menus = null;
		if(CacheKit.get("iss_auth", "menu_"+role_id)==null){
			menus = Function.dao.find("select * from td_function where is_parent = 1 and is_menu = 1 and function_id in (select function_id from tr_role_function where role_id = ?) order by function_order asc",role_id);
			for(Function f:menus){
				f.setSubMenu(Function.dao.find("select * from td_function where p_id = ? and is_menu = 1 and function_id in (select function_id from tr_role_function where role_id = ?) order by function_order asc",f.getStr("function_id"),role_id));
			}
			CacheKit.put("iss_auth", "menu_"+role_id,menus);
		}else{
			menus = CacheKit.get("iss_auth", "menu_"+role_id);
		}
		return menus;
	}
	/**
	 * 根据actionKey进行查询
	 * @return
	 */
	public Record findByActionKey(String actionKey){
		if(CacheKit.get("iss_auth", "Function_findByActionKey_"+actionKey)==null){
			CacheKit.put("iss_auth", "Function_findByActionKey_"+actionKey,Db.findFirst("select * from td_function where function_url = ?",actionKey));
		}
		return CacheKit.get("iss_auth", "Function_findByActionKey_"+actionKey);
	}
	/**
	 * 根据id进行查询
	 * @return
	 */
	public Function findByFID(String id){
		if(CacheKit.get("iss_auth", "Function_findByFID_"+id)==null){
			CacheKit.put("iss_auth", "Function_findByFID_"+id,Function.dao.findById(id));
		}
		return CacheKit.get("iss_auth", "Function_findByFID_"+id);
	}
	/**
	 * 查询角色拥有的字符串ID
	 * @return
	 */
	public String findRoleFuncs(String role_id){
		List<Record> list = Db.find("select function_id from tr_role_function where role_id=?",role_id);
		List<String> ids = new ArrayList<String>();
		for(Record r:list){
			ids.add(r.getStr("function_id"));
		}
		return StringUtils.join(ids,",");
	}
	public List<Function> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(List<Function> subMenu) {
		this.subMenu = subMenu;
	}
	
	public List<Record> find4TreeWithIcon(String webRoot){
		return Db.find("select *  from td_function order by function_order asc");
	}
	
	public List<Record> find4TreeMenuWithIcon(String webRoot){
		return Db.find("select *  from td_function where is_menu=1 order by function_order asc");
	}
	
	
}
