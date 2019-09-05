<%@ tag pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name='pageCss'%>
<%@ attribute name='pageJavascript'%>
<%@ attribute name='bodyClass'%>
<%@ attribute name='funcName'%>
<%@ attribute name='backUrl'%>
<!DOCTYPE html>
<html>
    <head>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta http-equiv="Pragma" content="no-cache"> 
			<meta http-equiv="Expires" content="0">
            <title>${systemInfos.systemTitle } </title>
            <tags:css pageCss="${pageCss }" ></tags:css>
            <!-- jQuery 2.2.3 -->
			<script src="${root}/statics/plugins/jQuery/jquery-2.2.3.min.js"></script>
    </head>
    <body class="<c:if test="${!empty bodyClass}">${bodyClass}</c:if>
                 <c:if test="${empty bodyClass}"> skin-blue sidebar-mini wysihtml5-supported </c:if>">
        <tags:content funcName="${funcName }" backUrl="${backUrl }">
            <jsp:doBody ></jsp:doBody>
        </tags:content>
        <tags:js pageJavascript="${pageJavascript }"></tags:js>
    </body>
</html>