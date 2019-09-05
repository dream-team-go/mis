package com.dream.mis.core.web.model;

import java.util.ArrayList;
import java.util.List;

import com.dream.mis.core.utils.CommonUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="td_attach",pkName="attach_id")
public class Attach extends Model<Attach>{
	private static final long serialVersionUID = 1L;
	private static String msg = "";
	private static String status = "y";
	public static Attach dao = new Attach();
	/**
	 * 添加一条
	 * @param original_file_name
	 * @param new_file_name
	 * @param file_path
	 * @return
	 */
	public Attach addOne(String original_file_name,String new_file_name,String file_path){
		Attach attach = new Attach();
		attach.set("attach_id", CommonUtils.getUUID());
		attach.set("original_file_name", original_file_name);
		attach.set("new_file_name", new_file_name);
		attach.set("file_path", file_path);
		attach.set("suffix", original_file_name.substring(original_file_name.lastIndexOf(".")));
		attach.save();
		return attach;
	}
	
	public static String getImagePath(String contextPath, String attachId){
		Attach a = Attach.dao.findById(attachId);
		String imagePath = contextPath + a.getStr("file_path")+a.getStr("new_file_name");
		return imagePath;
	}
}
