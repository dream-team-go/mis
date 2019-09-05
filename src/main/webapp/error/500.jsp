<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>${systemInfos.systemTitle }  | 程序出错了！</title>
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <tags:css />
</head>
<body class="hold-transition lockscreen">
<div class="lockscreen-wrapper">
  <div class="lockscreen-logo">
    ${systemInfos.systemTitle } 
  </div>
  <div class="lockscreen-name"><h2 class="headline text-yellow"> 500</h2></div>
  <div class="help-block text-center">
    <h3><i class="fa fa-warning text-yellow"></i>请联系管理员处理......</h3>
  </div>
  <div class="text-center">
    <a href="javascript:void(0);" onclick="window.history.back();">点击返回</a>
  </div>
  <div class="lockscreen-footer text-center">
  ${systemInfos.allRightReserved } 
  </div>
</div>
</body>
</html>
