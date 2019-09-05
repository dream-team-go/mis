<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="pageJavascript">
	<script type="text/javascript">
	$(function(){
		$("#myInfo_form").validator({
			valid:function(form){
				var me = this;
				var username=$("#username").val();
				//var oldname=$("#username").attr("data-old")
				var tel_no=$("#tel_no").val();
				var sex=$("input[name='sex']:checked").val();
				// 提交表单之前，hold住表单，并且在以后每次hold住时执行回调
		        me.holdSubmit(function(){
		            layer.msg("正在处理中...");
		        });
		        $.ajax({
		            url: "${root }/userInfo/myInfoSave",
		            data: {username:username,tel_no:tel_no,sex:sex},
		            type: "POST",
		            success: function(data){
		                // 提交表单成功后，释放hold，就可以再次提交
		                me.holdSubmit(false);
		                layer.msg("保存成功", {time: 1200,icon: 6},function(){
		                	location.reload();
		                });         
		            },
		            error:function(){
		            	// 提交表单成功后，释放hold，就可以再次提交
		                me.holdSubmit(false);
		            	layer.msg("保存失败", {time: 1200,icon: 5});
		            }
		        });
			}
		});
	});
	</script>
</c:set>
<tags:adminLayout pageCss="${pageCss }"
	pageJavascript="${pageJavascript }" funcName="编辑个人资料" backUrl="${root}/userInfo">
	<div class="callout callout-info">
		<h4>温馨提示!</h4>
		<p>后台系统用户为实名制，‘姓名’不可修改；‘角色’是管理员设置，用户无权限修改！</p>
	</div>
	<div class="box box-info">
		<div class="row col-md-offset-2">
			<div class="col-md-8 ">
				<form id="myInfo_form" class="form-horizontal" method="post"
					data-validator-option="{stopOnError:true,focusCleanup:true,theme: 'yellow_right_effect'}">
					<div class="box-body">
						<div class="form-group">
							<label for="systemTitleSmall" class="col-sm-2 control-label">用户名：</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="username" name="user.username"
									value="${user.username }" data-rule="required;username;remote[${root }/login/checkUserName3?old=${user.username }]">
							</div>
						</div>
						<div class="form-group">
							<label for="systemTitleSmall" class="col-sm-2 control-label">姓名：</label>
							<div class="col-sm-9">
								<input readonly="readonly" type="text" class="form-control"
									value="${user.user_cn_name }">
							</div>
						</div>
						<div class="form-group">
							<label for="systemTitleSmall" class="col-sm-2 control-label">手机号：</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="tel_no"
									value="${user.tel_no }" data-rule="required;mobile;">
							</div>
						</div>
						<div class="form-group">
							<label for="systemTitleSmall" class="col-sm-2 control-label">性别：</label>
							<div class="radio col-sm-8">
								<label> <input type="radio" ${("1" eq user.sex)?"checked":""} name="sex" value="1" >男&nbsp;&nbsp;
								</label>
								<label> <input type="radio" ${("2" eq user.sex)?"checked":""} name="sex" value="2" >女&nbsp;&nbsp;
								</label>
							</div>
						</div>
						<div class="form-group">
							<label for="systemTitleSmall" class="col-sm-2 control-label">角色：</label>
							<div class="col-sm-9">
								<input readonly="readonly" type="text" class="form-control" id="role_name"
									value="${user.role_name }">
							</div>
						</div>
						<input type="hidden" id="user_id" value="${user.user_id }">
					</div>
					<div class="box-footer">
						<button type="submit" class="btn btn-success pull-right">保&nbsp;存&nbsp;</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</tags:adminLayout>