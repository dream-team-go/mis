package com.dream.mis.core.web.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
/**
 * 系统配置表
 * @author zhaojing
 *
 */
@TableBind(tableName="td_system",pkName="id")
public class SystemInfo extends Model<SystemInfo>{
	private static final long serialVersionUID = 1L;
	public static SystemInfo dao = new SystemInfo();
	/**
	 * 查询所有
	 * @return
	 */
	public List<SystemInfo> findAll(){
		return SystemInfo.dao.find("select * from td_system");
	}
	/**
	 *根据code和val更新
	 * @param id
	 * @param val
	 * @return
	 */
	public int updateByCodeVal(String code,String val){
		return Db.update("update td_system set val = ? where code = ?",val,code);
	}
	/**
	 * 根据code查找
	 * @param id
	 * @return
	 */
	public String findValByCode(String code){
		SystemInfo s =SystemInfo.dao.findFirst("select * from td_system where code=?",code);
		if(s!=null){
			return s.getStr("val");
		}else{
			return "";
		}
	}
}
