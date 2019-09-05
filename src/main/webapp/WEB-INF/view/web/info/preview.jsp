<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<h3 style="text-align: center;font-weight: bold;">${notice.notice_title }</h3>
<h6 style="text-align: center;color: gray;">类型：${notice.type }&nbsp;&nbsp;发布人：${notice.user_cn_name }&nbsp;&nbsp;发布时间：${notice.create_time }</h6>
<p>
${notice.content }
</p>
<p style="font-weight: bold;">通知接收人：</p>
<ul id="org_tree" class="ztree" style="margin-top:0; width:200px;"></ul>
<input type="hidden" value="${user_ids }" name="user_ids" id="user_ids" />
<input type="hidden" value="${read_user_ids }" id="read_user_ids" />
<p><b>已读人</b>：<c:if test="${!empty read_user_names }">${read_user_names }</c:if><c:if test="${empty read_user_names }">暂无人阅读</c:if></p>
<p><b>未读人</b>：<c:if test="${!empty not_read_user_names }">${not_read_user_names }</c:if><c:if test="${empty not_read_user_names }">空</c:if></p>
