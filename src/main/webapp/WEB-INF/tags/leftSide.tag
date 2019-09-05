<%@ tag pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">

  <!-- sidebar: style can be found in sidebar.less -->
  <section class="sidebar">

    <!-- Sidebar user panel (optional) -->
    <%-- <div class="user-panel">
      <div class="pull-left image">
      <c:if test="${empty currentUser.head_img }">
         <img class="img-circle" src="${root}/statics/AdminLTE/img/user2-160x160.jpg">
      </c:if>
      <c:if test="${!empty currentUser.head_img }">
         <img class="img-circle" src="${root}/upload/${currentUser.head_img}" onerror="javascript:this.src='${root}/statics/AdminLTE/img/user2-160x160.jpg'">
      </c:if>
      </div>
      <div class="pull-left info">
        <p>${currentUser.user_cn_name }</p>
        <a href="#"><i class="fa fa-circle text-success"></i>在线</a>
      </div>
    </div> --%>

    <!-- search form (Optional) -->
 <!-- <form action="#" method="get" class="sidebar-form">
      <div class="input-group">
        <input type="text" name="q" class="form-control" placeholder="Search...">
            <span class="input-group-btn">
              <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
              </button>
            </span>
      </div>
    </form> -->
    <!-- /.search form -->

    <!-- Sidebar Menu -->
    <ul class="sidebar-menu tree" data-widget="tree">
    <li style="color:white;padding-left: 10px;font-size:13px;" class="header">主导航</li>
      <!-- Optionally, you can add icons to the links -->
      <c:forEach items="${menus }" var="m">
      	<li class="treeview <c:if test="${parF.function_url==m.function_url }">active menu-open</c:if>">
	      	<c:if test="${fn:length(m.getSubMenu())==0}">
	      		<a href="${root}${m.function_url}">
	      	</c:if>
	      	<c:if test="${fn:length(m.getSubMenu())>0}">
	      		<a href="#">
	      	</c:if>
	      		<i class="fa <c:if test="${empty m.icon}">fa-folder</c:if><c:if test="${!empty m.icon}">${m.icon}</c:if> "></i> <span>${m.name }</span>
	      		<span class="pull-right-container">
	            	<i class="fa fa-angle-left pull-right"></i>
	          	</span>
	      	</a>
	      	<ul class="treeview-menu">
		      	<c:forEach items="${m.getSubMenu() }" var="sub">
		      		<li <c:if test="${subF.function_url==sub.function_url }">class="active"</c:if>>
		      			<a href="${root }${sub.function_url}">
		      				<c:if test="${subF.function_url!=sub.function_url }"><i class="fa ${sub.icon }"></i></c:if>
		      				<c:if test="${subF.function_url==sub.function_url }"><i class="fa ${sub.icon } active"></i></c:if>
		      			${sub.name }</a>
		      		</li>
		      	</c:forEach>
	      	</ul>
      	</li>
      </c:forEach>
    </ul>
  </section>
</aside>
