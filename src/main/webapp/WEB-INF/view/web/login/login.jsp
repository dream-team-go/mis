<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>系统登录界面</title>
<link rel="stylesheet" href="${root}/statics/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${root}/statics/AdminLTE/css/font-awesome.min.css">
<link rel="stylesheet" href="${root}/statics/AdminLTE/css/ionicons.min.css">
<link rel="stylesheet" href="${root}/statics/AdminLTE/css/AdminLTE.min.css">
<script src="${root }/statics/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script src="${root }/statics/layer-v2.4/layer/layer.js"></script>
<style type="text/css">
	.login-input{
		height:42px;font-size:16px;
	}
	.error-border{
		border: 1px rgba(204, 63, 68, 0.5) solid!important;
	}
	.error{
		color:red;
		font-size:14px;
		font-weight: 500;
		margin-bottom: 0px;
	}
	.e{color: red;}
</style>
<script type="text/javascript">
//域名切换测试
/* window.onload=jstiao();  //自动加载
function jstiao(){
    var widths = window.screen.availWidth;
    if(widths > 1000){  //屏幕尺寸宽度小于1000，则执行域名替换，跳转
        var domain = window.location.host; //获取当前域名
        alert(domain);
        var url = document.location.toString();  //获取当前url
        alert(url);
        var result = url.replace(domain, "W3School");
        window.location.href = result;
    }
} */
	$(function(){
		//切换验证码
		$("#randCodeImage").click(function(){
	  	    var date = new Date();
		    var img = document.getElementById("randCodeImage");
		    img.src='${root}/login/img?a=' + date.getTime();
    	});
		//忘记密码
		$("#forgetPW").click(function(){
			layer.msg("请联系管理员进行密码重置！",{offset: 'auto',anim: 6});
		});
		//监听回车
		$(document).keyup(function(event){
		  	if(event.keyCode ==13){
		    	$("#submit").trigger("click");
		 	  }
		});
		//用户输入用户名 或 密码 清除提示
		$(".login-input").focus(function(){
			$(".login-input").removeClass("error-border");
			$(".error").hide();
		});
		//提交表单
		$("#submit").click(function(){
			var req=true;
			var username=$("form").find('#username').val().trim();
			var password=$("form").find('#passwd').val().trim();
			var code=$("form").find("#code").val().trim();
			// 验证是否为空
			if(username==""){
				req=false;
				$("#username").addClass("error-border");
				$("#username-error").show();
			}else if(password==""){
				req=false;
				$("#passwd").addClass("error-border");
				$("#passwd-error").show();
			}else if(code==""){
				req=false;
				$("#code").addClass("error-border");
				$("#code-error").show();
			}
			if(req){
				//正在加载
				layer.load();
		        $.ajax({
		            url: "${root }/login/doLogin",
		            data: {username:username,password:password,checkCode:code},
		            type: "POST",
		            success: function(data){
		            	//关闭加载
		            	layer.closeAll('loading');
		                if(data.status=='y'){
		                	window.location.href="${root}/userInfo";
		                } else {
		                	layer.msg(data.msg);
		                }
		            }
		        });	
			}
		});  
	});
</script>
</head>
<body  class="hold-transition login-page">
<div class="login-box">
  <div class="login-logo">
    <a href="${root }/userInfo"><b>${fn:substring(systemInfos.systemTitle, 0, 2)}  </b>${fn:substring(systemInfos.systemTitle, 2, fn:length(systemInfos.systemTitle))} </a>
  </div>
  <!-- /.login-logo -->
  <div class="login-box-body">

    <form method="post">
      <div class="form-group has-feedback">
        <input id="username" type="text" class="form-control login-input" placeholder="用户名" >
        <span class="glyphicon glyphicon-user form-control-feedback"></span>
        <label style="display:none" id="username-error" class="error" for="username">请输入用户名</label>
      </div>
      <div class="form-group has-feedback">
        <input id="passwd" type="password" class="form-control login-input" placeholder="密码" >
        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
        <label style="display:none" id="passwd-error" class="error" for="passwd">请输入密码</label>
      </div>
      <div class="row">
      	<div class="col-md-8">
      		<div class="form-group has-feedback">
      			<input type="text" class="form-control login-input" id="code" name="checkCode"  placeholder="验证码">
      		</div>
      	</div>
      	<div class="col-md-4">
      		<div class="form-group has-feedback">
      			<img style="height: 100%;width: 100%;cursor:pointer;" id="randCodeImage" title="点击刷新验证码" src="${root }/login/img"/>
      		</div>
      	</div>
      </div>
      <div class="form-group error" style="display: none;position: relative;top:-15px;" id="code-error">
		<span >请输入验证码</span>
	  </div>
      <div class="row">
        <div class="col-xs-12">
          <a id="submit" class="btn btn-primary btn-block btn-flat" style="height:40px;font-size:16px;">登录</a>
        </div>
      </div>
    </form>
	<div style="margin-top:10px;">
		<a id="forgetPW" href="#" style="font-size:14px;">忘记密码</a><br>
	</div>

  </div>
</div>
</body>
</html>