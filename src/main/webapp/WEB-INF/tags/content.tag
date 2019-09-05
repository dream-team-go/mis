<%@ tag pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name='funcName'%>
<%@ attribute name='backUrl'%>
<div class="wrapper">
    <tags:header/>
    <tags:leftSide/>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper" name="navbar_top_oo" id="navbar_top_oo">
    <!-- Content Header (Page header) -->
    <section class="content-header" style="background-color: #fff;padding-bottom: 10px;border-bottom:2px solid #eeeeee;color: #333">
        <h1>
           <c:if test="${empty funcName }">首页</c:if>
           <c:if test="${!empty funcName }">${funcName }</c:if>
           <c:if test="${!empty backUrl }">
           		<a href="${backUrl}" class="btn btn-info btn-sm" style="float: right">返回</a>
           </c:if>
        </h1>
        <!-- <hr style="margin-top:15px;margin-bottom: 10px;border: 0.5px solid  #8d8a8a"/> -->
    </section>
    <!-- Main content -->
    <section class="content">
        <jsp:doBody ></jsp:doBody>
    </section>
    <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <tags:footer/>
    
    <!--滚到顶部全局插件 css在css.tag中，js在js.tag中 -->
    <div id="to_the_top"></div>
</div>

<!-- ./wrapper -->