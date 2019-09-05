package com.dream.mis.core.web.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import com.dream.mis.core.baseController.BaseController;
import com.dream.mis.core.web.model.Attach;
import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

/**
 * 上传控制器
 */
@ControllerBind(controllerKey = "/uploadFile", viewPath = "")
@RequiresAuthentication
public class UploadController extends BaseController {
	
	@Clear
	public void index() {
		String newFileName ="";
		UploadFile uploadFile = getFile();
		File file = uploadFile.getFile();
		try {
			String name = file.getName();
			newFileName = generateNewFileName(name);
			System.out.println(newFileName);
			file.renameTo(new File(PathKit.getWebRootPath()+"/upload/"+newFileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Record json = new Record();
		Attach attach = Attach.dao.addOne(file.getName(), newFileName, "/upload/");
		json.set("name", newFileName).set("original_name", file.getName()).set("file_path", attach.getStr("file_path")).set("new_file_name", attach.getStr("new_file_name")).set("status", "y").set("attach_id", attach.getStr("attach_id"));
		renderText(json.toJson());
	}
	
	@Clear
	public void deleteImg(){
		Attach attach = Attach.dao.findById(getPara("attachId"));
		if(null != attach){
			File file = new File(PathKit.getWebRootPath()+"/upload/"+attach.getStr("new_file_name"));
			if(file.exists()){
				file.delete();
			}
			attach.dao.deleteById(getPara("attachId"));
			renderJson(successJson("删除成功！"));
		}
	}
	
	public void downloadFile(){
		String attach_id = getPara();
		Attach attach = Attach.dao.findById(attach_id);
		File file = new File(PathKit.getWebRootPath()+attach.getStr("file_path")+attach.getStr("original_file_name"));
		if(file.exists()){
			file.delete();
		}
		File newFile  = new File(PathKit.getWebRootPath()+attach.getStr("file_path")+attach.getStr("original_file_name"));
		new File(PathKit.getWebRootPath()+attach.getStr("file_path")+attach.getStr("new_file_name")).renameTo(newFile);
		renderFile(newFile);
	}
	
	/**
	 * Author:ly 
	 * @param oldName
	 * @return String
	 * @see 生成文件名
	 */
	@Clear
	public String generateNewFileName(String oldName) {
		String randomStr = new Random().nextInt(10) + UUID.randomUUID().toString().substring(0, 4);// 随机数+UUID的前4位
		String timeStr = new SimpleDateFormat("yyyyMMddHH24mmss").format(new Date());// 时间字符串
		String suffix = oldName.substring(oldName.lastIndexOf("."));
		System.out.println(suffix);
		return timeStr + randomStr + suffix;
	}
	/**
	 * 通过附件ID获取下载文件
	 * @param attach_id
	 * @return
	 */
	/**
	 * 修复重复下载文件报错的bug
	 * @author swb
	 * @version update at 2016年11月29日 上午11:24:20
	 * @param attach_id
	 * @return
	 */
	public static File getDownloadFile(String attach_id){
		Attach attach = Attach.dao.findById(attach_id);
		java.io.File file = new java.io.File(PathKit.getWebRootPath()+attach.getStr("file_path")+attach.getStr("original_file_name"));
		if(file.exists()){
			file.delete();
		}
		java.io.File newFile  = new java.io.File(PathKit.getWebRootPath()+attach.getStr("file_path")+attach.getStr("new_file_name"));
		file  = new java.io.File(PathKit.getWebRootPath()+attach.getStr("file_path")+attach.getStr("original_file_name"));
		try {
			FileUtils.copyFile(newFile, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
}
