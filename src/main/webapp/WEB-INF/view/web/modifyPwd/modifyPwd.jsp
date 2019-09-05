<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="pageCss">
	<style>
		.form-group{
			margin-bottom: 30px;
		}
	</style>
</c:set>
<c:set var="pageJavascript">
	<script type="text/javascript">
	$("#modifyPwd_from").validator({
	    valid: function(form){
	        var me = this;
	        // 提交表单之前，hold住表单，并且在以后每次hold住时执行回调
	        me.holdSubmit(function(){
	            layer.msg("正在处理中...");
	        });
	        $.ajax({
	            url: "${root }/modifyPwd/doModify",
	            data: $(form).serialize(),
	            type: "POST",
	            success: function(data){
	                // 提交表单成功后，释放hold，就可以再次提交
	                me.holdSubmit(false);
	                if(data.status=='y'){
	                	layer.msg(data.msg, {time: 1200,icon: 6},function(){
		                	location.href="${root }/userInfo"
		                });
	                }else{
	                	layer.msg(data.error);
	                }   
	            }
	        });
	    }
	});
	</script>
</c:set>
<tags:adminLayout pageCss="${pageCss }" pageJavascript="${pageJavascript }" funcName="修改密码" backUrl="${root }/userInfo">
	<div class="callout callout-info">
      <h4>温馨提示!</h4>
      <p>密码修改后，下次登录生效；如果不记得原密码将无法修改密码。</p>
    </div>
    <div class="box box-info">
		<div class="row col-md-offset-2">
			<div class="col-md-8 ">
				<form id="modifyPwd_from" class="form-horizontal" method="post" data-validator-option="{theme: 'yellow_top_effect'}">
			        <div class="box-body">
			        <div class="form-group">
			            <label for="systemTitle" class="col-sm-2 control-label">原密码</label>
			            <div class="col-sm-10">
			              <input type="password" class="form-control" name="password" data-rule="原密码:required;<%-- remote[${root }/modifyPwd/checkOldPwd?username=<shiro:principal property='username'/>] --%>"  id="password" placeholder="请输入原密码">
			              <!-- <span class="help-block">请输入原密码</span> -->
			              <input type="hidden" name="username" value="<shiro:principal property='username'/>">
			            </div>
			          </div>
			          <div class="form-group">
			            <label for="systemTitle" class="col-sm-2 control-label">新密码</label>
			            <div class="col-sm-10">
			              <input type="password" class="form-control" name="newPassword" data-rule="新密码:required;password" id="newPassword" placeholder="请填写6-16位字符，不能包含空格">
			              <!-- <span class="help-block">请填写6-16位字符，不能包含空格</span> -->
			            </div>
			          </div>
			          <div class="form-group">
			            <label for="systemTitle" class="col-sm-2 control-label">确认密码</label>
			            <div class="col-sm-10">
			              <input type="password" class="form-control" name="cPassword" data-rule="确认密码:required;password;match(newPassword);" id="cPassword" placeholder="请再次输入新密码，确保两次输入一致">
			              <!-- <span class="help-block">请再次输入新密码，确保两次输入一致</span> -->
			            </div>
			          </div>
			          </div>
			        <!-- /.box-body -->
			        <div class="box-footer">
			          <button type="submit" class="btn btn-success pull-right">修&nbsp;改&nbsp;密&nbsp;码</button>
			        </div>
			        <!-- /.box-footer -->
			      </form>
			
			</div>
		</div>
    </div>
</tags:adminLayout>
