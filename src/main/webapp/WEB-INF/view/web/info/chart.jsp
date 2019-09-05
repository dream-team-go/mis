<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<h4 style="text-align: center;">${notice.notice_title }</h4>
<h6 style="text-align: center;color: gray;">类型：${notice.type }&nbsp;&nbsp;发布人：${notice.user_cn_name }&nbsp;&nbsp;发布时间：${notice.create_time }</h6>
<p>
已读：<img alt="" src="${root}/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/read_user.png" />
未读：<img alt="" src="${root}/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/user.png" />
<ul id="org_tree" class="ztree" style="margin-top:0; max-height: 300px;overflow-y: scroll;"></ul>
<input type="hidden" value="${user_ids }" name="user_ids" id="user_ids" />
<input type="hidden" value="${read_user_ids }" id="read_user_ids" />
</p>
<p><b><img alt="" src="${root}/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/read_user.png" />已读人</b>：<c:if test="${!empty read_user_names }">${read_user_names }</c:if><c:if test="${empty read_user_names }">暂无人阅读</c:if></p>
<p><b><img alt="" src="${root}/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/user.png" />未读人</b>：<c:if test="${!empty not_read_user_names }">${not_read_user_names }</c:if><c:if test="${empty not_read_user_names }">空</c:if></p>
<div id="echart_notice_read" style="width: 100%;height:800px;"></div>
