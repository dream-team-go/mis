<%@ tag pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name='k'%>
<c:forEach items="${systemInfos }" var="v"><c:if test="${k==v.code }">${v.val }</c:if></c:forEach>