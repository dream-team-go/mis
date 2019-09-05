<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
 <table id="example" class="table table-hover table-striped table-bordered nowrap">
	<thead>
           <tr>
               <th>创建时间</th>
               <th>描述</th>
               <th>IP</th>
               <th>请求路径</th>
           </tr>
       </thead>
       <tfoot>
           <tr>
           	<th>创建时间</th>
               <th>描述</th>
               <th>IP</th>
               <th>请求路径</th>
           </tr>
       </tfoot>
</table>
