<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!-- form start -->
 <form id="func_edit_form" class="form-horizontal" method="post" data-validator-option="{stopOnError:true,focusCleanup:true,theme: 'yellow_top'}">
   <div class="box-body">
	     <%-- <div class="form-group">
	       <label for="systemTitle" class="col-sm-2 control-label">父菜单</label>
	       <div class="col-sm-10">
	         <select class="form-control"  name="func.p_id">
	         		<option value="0" <c:if test="${func.p_id=='0' }">selected="selected"</c:if>>作为父菜单</option>
                    <c:forEach items="${funcList }"  var="f">
                   		<option value="${f.function_id }" <c:if test="${func.p_id==f.function_id }">selected="selected"</c:if>>${f.name }</option>
                    </c:forEach>
              </select>
	          <span class="help-block">请选择父菜单或者作为父菜单添加</span>
	       </div>
	     </div> --%>
	     <div class="form-group">
	       <label for="systemTitleSmall" class="col-sm-2 control-label">菜单名称</label>
	       <div class="col-sm-10">
	         <input type="text" class="form-control" name="func.name"   placeholder="请输入菜单的名称"
	         	value="${func.name }"
	         	data-rule="required;">
	         <span class="help-block">请输入菜单的名称</span>
	       </div>
	     </div>
	     <div class="form-group">
	       <label for="systemTitleSmall" class="col-sm-2 control-label">菜单图标</label>
	       <div class="col-sm-10">
	         <input type="text" class="form-control" name="func.icon"  placeholder="请输入图标class"
	         <c:if test="${empty func.icon }">value="fa-circle-o"</c:if> <c:if test="${!empty func.icon }">value="${func.icon }"</c:if>
	           data-rule="required;">
	         <span class="help-block">请输入图标class</span>
	       </div>
	     </div>
	     <div class="form-group">
	       <label for="defaultPassword" class="col-sm-2 control-label">是否菜单</label>
	       <div class="col-sm-10">
                <div class="checkbox">
                     <label>
                       <input type="radio" name="func.is_menu" value="1" <c:if test="${empty func }">checked="checked"</c:if> <c:if test="${func.is_menu==1 }">checked="checked"</c:if> > 是
                     </label>
                     <label>
                       <input type="radio" name="func.is_menu" value="0" <c:if test="${func.is_menu==0 }">checked="checked"</c:if> > 否
                     </label>
                 </div>
		         <span class="help-block">菜单会在左侧显示；功能只以按钮或者链接的形式显示</span>
	         </div>
	     </div>
	     <div class="form-group">
	       <label for="allRightReserved" class="col-sm-2 control-label">链接</label>
	       <div class="col-sm-10">
		         <input type="text" class="form-control" name="func.function_url"   placeholder="请输入菜单链接"
		         	value="${func.function_url }"
		         	data-rule="required;">
		         <span class="help-block">菜单访问链接；父菜单与子菜单链接不能相同；所有链接中不能重复；</span>
	       </div>
	     </div>
	     <div class="form-group">
	       <label for="allRightReserved" class="col-sm-2 control-label">权限</label>
	       <div class="col-sm-10">
		         <input type="text" class="form-control" name="func.resource_name" placeholder="请输入访问权限"
		         	value="${func.resource_name }" data-tip="类与方法用冒号(:)隔开，如：user:edit;json;"
		         	data-rule="required;">
		         <span class="help-block">权限class:method1;method2;（例user:edit;json;或user:*或user）</span>
	       </div>
	     </div>
	     <div class="form-group">
	       <label for="allRightReserved" class="col-sm-2 control-label">排序</label>
	       <div class="col-sm-10">
		         <input type="text" data-slider-value="${func.function_order }" name="func.function_order" class="slider form-control"  data-slider-orientation="horizontal" 
		         	data-slider-selection="before" data-slider-tooltip="show" data-slider-id="blue">
		         <span class="help-block">菜单排序，从小到大排序</span>
	       </div>
	     </div>
	     <div class="form-group">
	       <label for="systemTitle" class="col-sm-2 control-label">类型</label>
	       <div class="col-sm-10">
	       		<div class="checkbox">
                  <label>
                  	<input type="radio" name="isParentRadio" id="isParentRadio1" value="1" 
                  		<c:choose>
					     	<c:when test="${empty func }">
					     		checked="checked"
					     	</c:when>
					     	<c:when test="${!empty func && func.p_id=='0' }">
					     		checked="checked"
					     	</c:when>
				     	</c:choose>
                  	  >作为父菜单
                  </label>
                  <label>
                  	<input type="radio" name="isParentRadio" value="0" id="isParentRadio0" <c:if test="${!empty func && func.p_id!='' && func.p_id!='0' }">checked="checked"</c:if> >作为子菜单/功能
                  </label>
                 </div>
	       </div>
	     </div>
	     
	     <div class="form-group" id="div_func_add_tree"
	     	<c:choose>
		     	<c:when test="${empty func }">
		     		style="display: none;"
		     	</c:when>
		     	<c:when test="${!empty func && func.p_id=='0' }">
		     		style="display: none;"
		     	</c:when>
		     	<c:otherwise>
		     		style="display: true;"
		     	 </c:otherwise>   
	     	</c:choose> >
	       <label for="systemTitle" class="col-sm-2 control-label">上级菜单</label>
	       <div class="col-sm-10">
	       		<ul id="func_add_tree" class="ztree" style="margin-top:0; width:200px;"></ul>
	       		<input type="hidden" name="func.p_id" value="${func.p_id }" id="p_id" />
	          	<span class="help-block">请选择上级菜单</span>
	       </div>
	     </div>
     </div>
     <c:if test="${!empty func }">
     	<input name="func.function_id" value="${func.function_id }" type="hidden">
     </c:if>
 </form>
