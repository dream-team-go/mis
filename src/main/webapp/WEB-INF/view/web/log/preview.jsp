<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<p><b>操作人</b>：${log.user_cn_name}</p>
<p><b>操作描述</b>：${log.log_content}</p>
<p><b>请求参数</b>：${log.log_request_method}</p>
<p><b>IP地址</b>：${log.log_ip}</p>
<p><b>操作时间</b>：${log.log_create_time}</p>
<p><b>请求参数</b>：${log.request_params}</p>
