<%@ tag pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!-- Main Header -->
<header class="main-header" >

  <!-- Logo -->
  <a href="${root}/userInfo/index" class="logo">
    <span class="logo-mini"><b>${systemInfos.systemTitleSmall }</b></span>
    <!-- logo for regular state and mobile devices -->
    <span class="logo-lg"><i class="fa fa-fw fa-home"></i><b>${systemInfos.systemTitle }</b></span>
  </a>

  <!-- Header Navbar -->
  <nav class="navbar navbar-static-top" role="navigation">
    <!-- Sidebar toggle button-->
    <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
      <span class="sr-only">Toggle navigation</span>
    </a>
    
    <!-- Navbar Right Menu -->

    <div class="navbar-custom-menu">
      <ul class="nav navbar-nav">
        <li class="dropdown messages-menu">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" id="noReadcount_link">
            <i class="fa fa-envelope-o"></i>
            <span id="noReadcount" class="label label-success"></span>
          </a>
        </li>         
      
        <!-- User Account Menu -->
        <li class="dropdown user user-menu">
          <!-- Menu Toggle Button -->
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            <!-- The user image in the navbar-->
              <c:if test="${empty currentUser.head_img }">
               <img class="user-image" src="${root}/statics/AdminLTE/img/user2-160x160.jpg" alt="User Avatar">
              </c:if>
              <c:if test="${!empty currentUser.head_img }">
               <img class="user-image" src="${root}/upload/${currentUser.head_img}" onerror="javascript:this.src='${root}/statics/AdminLTE/img/user2-160x160.jpg'">
              </c:if>
            <!-- hidden-xs hides the username on small devices so only the image appears. -->
            <span class="hidden-xs">${currentUser.user_cn_name }</span>
          </a>
          <ul class="dropdown-menu">
            <!-- The user image in the menu -->
            <li class="user-header">
               <c:if test="${empty currentUser.head_img }">
               <img onclick="location.href='${root }/userInfo'" class="img-circle" src="${root}/statics/AdminLTE/img/user2-160x160.jpg" alt="User Avatar">
               </c:if>
               <c:if test="${!empty currentUser.head_img }">
                <img onclick="location.href='${root }/userInfo'" class="img-circle" src="${root}${currentUser.head_img}" onerror="javascript:this.src='${root}/statics/AdminLTE/img/user2-160x160.jpg'">
               </c:if>
              <p>
                ${currentUser.user_cn_name }
              </p>
            </li>
            <!-- Menu Footer-->
            <li class="user-footer">
              <div class="pull-left">
                <a href="${root }/userInfo" class="btn btn-default btn-flat">个人中心</a>
              </div>
              <div class="pull-right">
                <a href="${root }/login/logout" class="btn btn-default btn-flat">退出</a>
              </div>
            </li>
          </ul>
        </li>
        <!-- Control Sidebar Toggle Button -->
        <li>
          <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
        </li>
      </ul>
    </div>
  </nav>
</header>
<script type="text/javascript">
        var websocket = null;
        //判断当前浏览器是否支持WebSocket
        if ('WebSocket' in window) {
            websocket = new WebSocket("ws://localhost:8080/mis/websocket");
        }
        else {
        	console.log('当前浏览器 Not support websocket')
        }
     	 //连接发生错误的回调方法
        websocket.onerror = function () {
            console.log("wesocket连接失败！");
        };
    
        //连接成功建立的回调方法
        websocket.onopen = function () {
            console.log("WebSocket连接成功!");
        }
      
        //接收到消息的回调方法
        websocket.onmessage = function (event) {
            var data=event.data;
            if(data>0){
            	$("#noReadcount").text(data);	
            }
            
        }
    
        //连接关闭的回调方法
        websocket.onclose = function () {
        	console.log("WebSocket连接关闭");
        }
    
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            closeWebSocket();
        }
    
        //关闭WebSocket连接
        function closeWebSocket() {
            websocket.close();
        }
        $(function(){
        	$("#noReadcount_link").click(function(){
        		window.location.href="${root}/userInfo";
        	});
        });
  </script>