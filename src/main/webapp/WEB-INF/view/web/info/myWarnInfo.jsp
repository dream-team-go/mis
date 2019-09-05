<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<h4 style="text-align: center;">${warn.warn_title }</h4>
<div style="background: #F1F3F4;border: 1px gay solid;border-radius:5px;color: #666;margin-bottom: 20px;">
	<div style="font-size: 12px;padding: 5px;">
	<div class="row" style="margin:5px;">
		<div class="col-xs-12 col-md-6">
			类型：${warn.type_name }
		</div>
	</div>
	<div class="row" style="margin:5px;">
		<div class="col-xs-12 col-md-6">
			发布时间：<fmt:formatDate value="${warn.create_time }" pattern="yyyy-MM-dd HH:mm:ss"/>
		</div>
		<c:if test="${warn.is_read==1 }">
		<div class="col-xs-12 col-md-6">
			已读时间：<fmt:formatDate value="${warn.read_time }" pattern="yyyy-MM-dd HH:mm:ss"/>
		</div>
		</c:if>
	</div>
	</div>
</div>
<p>${warn.content }</p>
