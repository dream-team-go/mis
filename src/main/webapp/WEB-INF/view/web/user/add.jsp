<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<style>
	.form-group{
		margin-bottom: 25px;
	}
</style>
<!-- form start -->
 <form id="user_edit_form" class="form-horizontal" method="post" data-validator-option="{stopOnError:true,focusCleanup:true,theme: 'yellow_top'}">
   <div class="box-body">
	     <div class="form-group">
	       <label for="systemTitleSmall" class="col-sm-3 control-label">姓名</label>
	       <div class="col-sm-9">
	         <input type="text" class="form-control" name="user.user_cn_name"   placeholder="请输入用户真实姓名" value="${user.user_cn_name }" data-rule="required;">
	       </div>
	     </div>
	     <c:if test="${!empty user }">
	     	<div class="form-group">
		       <label for="allRightReserved" class="col-sm-3 control-label">用户名</label>
		       <div class="col-sm-9">
			         <input type="text" class="form-control" name="user.username"   placeholder="请输入用户名"
			         	value="${user.username }"
			         	data-rule="required;username;remote[${root }/login/checkUserName3?old=${user.username }]">
		       </div>
		     </div>
	     </c:if>
	     <c:if test="${empty user }">
	     	<div class="form-group">
		       <label for="allRightReserved" class="col-sm-3 control-label">用户名</label>
		       <div class="col-sm-9">
			         <input type="text" class="form-control" name="user.username"   placeholder="请输入用户名"
			         	value="${user.username }"
			         	data-rule="required;username;remote[${root }/login/checkUserName2]">
		       </div>
		     </div>
	     </c:if>
	     <div class="form-group">
	       <label for="systemTitleSmall" class="col-sm-3 control-label">联系电话</label>
	       <div class="col-sm-9">
	         <input type="text" class="form-control" name="user.tel_no"   placeholder="请输入用户手机号" value="${user.tel_no }" data-rule="required;mobile;">
	       </div>
	     </div>
	     <div class="form-group">
	       <label for="defaultPassword" class="col-sm-3 control-label">性别</label>
				<div class="radio col-sm-8">
					<label> <input type="radio" ${("1" eq user.sex||user.sex==null)?"checked":""} name="user.sex" value="1">男&nbsp;&nbsp;
					</label> 
					<label> <input type="radio" ${("2" eq user.sex)?"checked":""} name="user.sex" value="2">女&nbsp;&nbsp;
					</label>
				</div>
	     </div>
	     <div class="form-group">
	       <label for="systemTitle" class="col-sm-3 control-label">角色</label>
	       <div class="col-sm-6">
				<select id="role_id" name="user.user_role_id">
					<option value="">不选</option>
					<c:forEach var="item" items="${roles}" varStatus="status">
						<c:if test="${item.role_id==user.user_role_id }">
							<option value="${item.role_id }" selected="selected">${item.role_name }</option>
						</c:if>
						<c:if test="${item.role_id!=user.user_role_id}">
							<option value="${item.role_id }">${item.role_name }</option>
						</c:if>
					</c:forEach>
				</select> 
	       </div>
	     </div>
 	     <div class="form-group">
	       <label for="user.allow_del" class="col-sm-3 control-label">是否允许删除</label>
			<div class="radio col-sm-9">
				<label> <input  ${("0" eq user.allow_del)?"disabled='disabled'":""} type="radio" ${(user.allow_dell==null||"1" eq user.allow_del)?"checked":""} name="user.allow_del" value="1">允许删除&nbsp;&nbsp;
				</label> 
				<label> <input  ${("0" eq user.allow_del)?"disabled='disabled'":""} type="radio" ${("0" eq user.allow_del)?"checked":""} name="user.allow_del" value="0">禁止删除&nbsp;&nbsp;
				</label>
			</div>
	     </div>
     </div>
   	<input name="user.user_id" value="${user.user_id }" type="hidden">
 </form>
<script type="text/javascript">
$(function(){
	$("#role_id").searchableSelect(); //初始化searchable
});

$("#user_edit_form").validator({
	    valid: function(form){
	        var me = this;
	        // 提交表单之前，hold住表单，并且在以后每次hold住时执行回调
	        me.holdSubmit(function(){
	            layer.msg("正在处理中...");
	        });
	        $.ajax({
	            url: "${root }/user/saveOrUpdateUser",
	            data: $(form).serialize(),
	            type: "POST",
	            success: function(data){
	                // 提交表单成功后，释放hold，就可以再次提交
	                me.holdSubmit(false);
	                if(data.status=='y'){
	                	layer.msg(data.msg,{icon: 1}); 
	                	add_dialog.close();
	                	table.ajax.reload();
	                }else{
	                	layer.alert(data.msg, {icon:2});
	                }
	            }
	        });
	    }
	});
</script>