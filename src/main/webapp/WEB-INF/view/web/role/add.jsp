<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!-- form start -->
 <form id="role_edit_form" class="form-horizontal" method="post" data-validator-option="{stopOnError:true,focusCleanup:true,theme: 'yellow_top'}">
   <div class="box-body">
	     <div class="form-group">
	       <label for="systemTitleSmall" class="col-sm-2 control-label">角色名称</label>
	       <div class="col-sm-10">
	         <input type="text" class="form-control" name="role.role_name"   placeholder="请输入角色名称"
	         	value="${role.role_name }"
	         	data-rule="required;">
	       </div>
	     </div>
	     <div class="form-group">
	       <label for="allRightReserved" class="col-sm-2 control-label">角色描述</label>
	       <div class="col-sm-10">
		         <input type="text" class="form-control" name="role.role_desc"   placeholder="请输入角色描述"
		         	value="${role.role_desc }"
		         	data-rule="required;">
	       </div>
	     </div>
	     <div class="form-group">
	       <label for="allRightReserved" class="col-sm-2 control-label">权限</label>
	       <div class="col-sm-10">
		        <ul id="func_tree" class="ztree" style="margin-top:0; width:200px;"></ul>
				<input type="hidden" value="${roleFuncs }" name="function_ids" id="function_ids" />
		        <span class="help-block">请选择该角色拥有的功能权限</span>
	       </div>
	     </div>
     </div>
     <input name="role.role_id" value="${role.role_id }" id="role_id" type="hidden">
 </form>
