<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>  
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-------注意放置在tags:adminlayout的上面 ------->
<c:set var="pageCss">
<tags:plugins_dataTables_css/>
<tags:plugins_dialog_css/>
</c:set>
<c:set var="pageJavascript">
<tags:plugins_dataTables_js/>
<tags:plugins_timeago_js/>
<tags:plugins_dialog_js/>
 <tags:plugins_ueditor_js/>
<tags:plugins_ajaxfileupload_js/>
<script type="text/javascript">
var table;
function loadLogDataTable(){
		table = $('#example').DataTable({
		      "paging": true,
		      "lengthChange": false,
		      "searching": false,
		      "ordering": true,
		     "pageLength": 25,
		    "responsive": true,
		    "autoWidth": false,
		   "bAutoWidth": false,
		      "info": true,
		      "language": {
		    	  "url": '${root}/statics/plugins/DataTables-1.10.12/Chinese.json'
		      },	
		      "serverSide": true,
		      "ajax": {
		          "url": '${root}/userInfo/myLogJson',
		          "type": 'POST'
		      },
		      "deferRender": true,
		      "columns": [
		            { "data": "log_create_time"},
	                { "data": "log_content" },
	            { "data": "log_ip" },
	            { "data": "log_request_method" }
	           
	           ],
	          "order": [[ 0, 'desc' ]],
 	       "columnDefs": [{
 	            "targets": 0,
 	            "render": function ( data, type, full, meta ) {
 	                return $.timeago(data);
 	            }
 	        },{
 	            "targets": '_all',
 	            "orderable":false
 	        }]
		   });
 }
	 $(function () {
         $(".upload_li").click(function () {
        	 $("#upload_file").click();
         });
         $("#upload_file").bind('change',function () {
        	 if ($("#upload_file").val().length > 0) {
                 ajaxFileUpload();
             }
         });
         $(".myLog").click(function(){
    		 myLog();
    	 });
         //切换通知和提醒
        /*  $(".warn-title").click(function(){
        	 $(".warn-list").show();
        	 $(".notice-list").hide();
         });
         $(".notice-title").click(function(){
        	 $(".warn-list").hide();
        	 $(".notice-list").show();
         }); */
         //跳转显示
         $("#warn-skip").click(function(){
        	 $("#warn-skip-content").show();
        	 $("#warn-skip-page").focus();
         });
         /* $("#warn-skip-page").blur(function(){
        	 $("#warn-skip-content").hide();
         }); */
         
         $("#notice-skip").click(function(){
        	 $("#notice-skip-content").show();
        	 $("#notice-skip-page").focus();
         });
        /*  $("#notice-skip-page").blur(function(){
        	 $("#notice-skip-content").hide();
         }); */
         
         var warn="warn";
         var notice="notice";
         var warnPage=new Number($("#warn-page").text());
         var noticePage=new Number($("#notice-page").text());
         //点击上一页 下一页
         $("#warn-last").click(function(){
        	 loadPage(warn,warnPage-1);
         });
         $("#notice-last").click(function(){
        	 loadPage(notice,noticePage-1);
         });
         $("#warn-next").click(function(){
        	 loadPage(warn,warnPage+1);
         });
         $("#notice-next").click(function(){
        	 loadPage(notice,noticePage+1);
         });
         //页面跳转
         $("#warn-sure").click(function(){
        	 var page=$("#warn-skip-page").val();
        	 var pageCount=$("#warn-count").text();
        	 if(page>pageCount){
        		 layer.msg("最大页数为"+pageCount+",请重新输入");
        	 }else{
        		 if(page<1){
            		 layer.msg("页码最小为1,请重新输入");
            	 }else{
            		 loadPage(warn,page);
            	 } 
        	 }
         });
         
         $("#notice-sure").click(function(){
        	 var page=$("#notice-skip-page").val();
        	 var pageCount=$("#notice-count").text();
        	 if(page>=pageCount){
        		 layer.msg("最大页数为"+pageCount+",请重新输入");
        	 }else{
        		 if(page<1){
            		 layer.msg("页码最小为1,请重新输入");
            	 }else{
            		 loadPage(notice,page);
            	 } 
        	 }
         });
         
     });
     function ajaxFileUpload() {
         $.ajaxFileUpload({
                 url: '${root}/uploadFile', //用于文件上传的服务器端请求地址
                 secureuri: false, //是否需要安全协议，一般设置为false
                 fileElementId: 'upload_file', //文件上传域的ID
                 dataType: 'json', //返回值类型 一般设置为json
                 success: function (data, status)  //服务器成功响应处理函数
                 {
                	 $.post("${root}/userInfo/updateHeadImg", {head_img:data.name}, function(data2) {
                		 $(".userHeadImg").attr('src',"${root}/upload/"+data.name);
                		 layer.msg("头像更新成功");
                		 location.reload();
                 	});
                 },
                 error: function (data, status, e)//服务器响应失败处理函数
                 {
                     layer.msg(e);
                 }
         });
         return false;
     }
	
	 function myLog(){
		 BootstrapDialog.show({
 			title: '我的日志',
 			size: BootstrapDialog.SIZE_WIDE,
            message: $('<div></div>').load("${root }/userInfo/myLog"),
            closeByBackdrop: true,
            closeByKeyboard: true,
            buttons: [{
                label: '关闭',
                cssClass: 'btn-warning',
                icon: 'glyphicon glyphicon-remove',
                action: function(dialog) {
                	dialog.close();
                }
            }],
            onshown: function(dialog) {
            	loadLogDataTable();
            }
        });
 	}
	 //分页处理
	 function loadPage(type,page){
		 window.location.href="${root}/userInfo?type="+type+"&page="+page;
	 }
	 //链接跳转
	 function link_url(url){
		 window.location.href="${root}"+url;
	 }
	 //已读处理
	 function warnRead(id){
		 $.post("${root}/info/warnRead",{id:id},function(data){
			 if(data.status==0){
				 layer.msg('操作成功！', {
					  icon: 1,
					  time: 1500 //1.5秒关闭（如果不配置，默认是3秒）
					}, function(){
						 loadPage('warn',1);
				});  
			 }
		 });
	 }
	 
	 function noticeRead(id){
		 $.post("${root}/info/noticeRead",{id:id},function(data){
			 if(data.status==0){
				 layer.msg('操作成功！', {
					  icon: 1,
					  time: 1500 //1.5秒关闭（如果不配置，默认是3秒）
					}, function(){
						 loadPage('notice',1);
				});  
			 }
		 });
	 }
	 //查看通知详情
	 function noticeInfo(notice_id){
		 BootstrapDialog.show({
	 			title: '通知详情',
	            message: $('<div></div>').load("${root }/info/myNoticeInfo?notice_id=" + notice_id),
	            closeByBackdrop: true,//esc键关闭
	            closeByKeyboard: true,//点击弹框以外关闭
	            buttons: [{
	                label: '关闭',
	                cssClass: 'btn-warning',
	                icon: 'glyphicon glyphicon-remove',
	                action: function(dialog) {
	                	dialog.close();
	                }
	            }],
	            onhide: function(dialog) {
	            	window.location.reload();
	            }
	        });
	 	}
	//查看提醒详情
	 function warnInfo(warn_id){
		 BootstrapDialog.show({
	 			title: '提醒详情',
	            message: $('<div></div>').load("${root }/info/myWarnInfo?warn_id=" + warn_id),
	            closeByBackdrop: true,//esc键关闭
	            closeByKeyboard: true,//点击弹框以外关闭
	            buttons: [{
	                label: '关闭',
	                cssClass: 'btn-warning',
	                icon: 'glyphicon glyphicon-remove',
	                action: function(dialog) {
	                	dialog.close();
	                }
	            }],
	            onhide: function(dialog) {
	            	window.location.reload();
	            }
	        });
	 	}
</script>
</c:set>

<tags:adminLayout pageCss="${pageCss }" pageJavascript="${pageJavascript }" funcName="个人中心">
        <div class="row">
			  <div class="col-md-4">
			  	<form id="modifyPwd_from" class="form-horizontal" method="post" data-validator-option="{theme: 'yellow_top_effect'}">
		          <div class="box box-primary widget-user-2">
		            <div class="widget-user-header" style="padding: 20px;padding-bottom: 21px;background-color: #eee;color: #333;border: 0.5px solid #cbc7c7a6;"> 
		              <div class="widget-user-image">
		                <c:if test="${empty currentUser.head_img }">
			                <img class="img-circle userHeadImg" src="${root}/statics/AdminLTE/img/user2-160x160.jpg" >
		                </c:if>
		                <c:if test="${!empty currentUser.head_img }">
			                <img class="img-circle userHeadImg" src="${root}/upload/${currentUser.head_img}" onerror="javascript:this.src='${root}/statics/AdminLTE/img/user2-160x160.jpg'">
		                </c:if>
		              </div>
		              <h3 class="widget-user-username" style="margin-left:100px;">
		              		<shiro:principal property="userCnName"/>
			               <img src="${root}/statics/images/is_org_admin.png">
		              </h3>
		             <h5 class="widget-user-desc" style="margin:10px 0px 0px 100px;">
		            	 <i class="fa fa-fw fa-home"></i>通用后台管理&nbsp;&nbsp;&nbsp;&nbsp;
		             </h5>
		             <h5 class="widget-user-desc" style="margin:10px 0px 0px 100px;">
		             	<i class="fa fa-fw fa-user"></i>
		             	<shiro:principal property="roleName"/>
		             </h5>
		            </div>
		           <div class="box-footer no-padding">	           			
		              <ul class="nav nav-stacked">
		              	<input type="file" id="upload_file" name="upload_file" style="display: none;" />
		              	<li><a class="" href="${root }/userInfo/myInfo"><i class="fa fa-fw fa-eye"></i>&nbsp;编辑个人资料<span class="pull-right badge bg-light-blue"><i class="fa fa-fw fa-edit"></i></span></a></li>
		                <li><a class="" href="${root }/modifyPwd"><i class="fa fa-fw fa-unlock-alt"></i>&nbsp;修改密码<span class="pull-right badge bg-light-blue"><i class="fa fa-fw fa-edit"></i></span></a></li>
		                <li class="upload_li"><a href="#"><i class="fa fa-fw fa-photo"></i>&nbsp;更换头像<span class="pull-right badge bg-light-blue"><i class="fa fa-fw fa-edit"></i></span></a></li>
		                <li class="myLog"><a href="#"><i class="fa fa-fw fa-list"></i>&nbsp;操作日志<span class="pull-right badge bg-light-blue"><i class="glyphicon glyphicon-zoom-in"></i></span></a></li>
		              </ul>
		            </div>
		          </div>
		         </form>
			  </div>
			  <div class="col-md-8" >
			  <div class="nav-tabs-custom">
				<ul class="nav nav-tabs">
					<c:if test="${type=='warn' }">
						<li class="active warn-title">	
					</c:if>
					<c:if test="${type!='warn' }">
						<li class="warn-title">	
					</c:if>
						<a href="#" onclick="javascipt:loadPage('warn',1)" style="position: relative;cursor:pointer;" data-toggle="tab">系统提醒
							<c:if test="${warnNoReadCount>0 }">
							<span id="warn-no-count" style="position: absolute;top:0px;right:2px;" class="label label-success">
							${warnNoReadCount }</span>
							</c:if>
						</a>
					</li>
					<c:if test="${type=='notice' }">
						<li class="active warn-title">	
					</c:if>
					<c:if test="${type!='notice' }">
						<li class="warn-title">	
					</c:if>
						<a href="#" onclick="javascipt:loadPage('notice',1)" style="position: relative;cursor:pointer;" data-toggle="tab">系统通知
							<c:if test="${noticeNoReadCount>0}">
							<span id="notice-no-count" style="position: absolute;top:0px;right:2px;" class="label label-success">
							${noticeNoReadCount }</span>
							</c:if>
						</a>
					</li>
				</ul>
				 <div class="box-body no-padding" style="position: relative;">
				 <c:if test="${type=='warn' }">
				 <div class="warn-list">
				 <div id="warn-skip-content" style="z-index: 1121; position: absolute;font-size:12px; right: 2px; top: -43px; width: 140px;border: 1px solid #ccc;background: #fff;border-radius:5px;display: none;">
	 				<div style="overflow-y:auto;overflow-x:hidden;width:183px;height:auto;">
	 					<div style="height:45px;line-height:45px;">
	 						<div style="position:absolute;width:160px;margin-left:2px;">
	 							<div style="float:left;">跳转到第 
	 								<input type="text" id="warn-skip-page" style="width:30px;height: 20px;"> 
	 							页</div>
	 							<a href="#" id="warn-sure" class="btn btn-primary" style="width:40px;padding:0;font-size:12px;margin-left:3px; display:inline;">&nbsp;确定&nbsp;</a>
	 						</div>
	 					</div>
	 				</div>
				 </div> 
              <div class="mailbox-controls">
                <div class="btn-group">
                  <button onclick="javacript:warnRead()" id="warn-all-read" type="button" class="btn btn-default btn-sm">全部忽略</button>
                </div>
                <!-- /.btn-group -->
                <button onclick="javascipt:loadPage('warn',1)" type="button" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i></button>
                <div class="pull-right">
                  <span id="warn-page">${page }</span>/<span id="warn-count">${warnCount }</span>
                  <div class="btn-group">
                  <c:if test="${page!=1 }">
                  		<button id="warn-last" type="button" class="btn btn-default btn-sm">
                  		<i class="fa fa-chevron-left"></i>
                  		</button>
                  </c:if>
                  <c:if test="${page<warnCount }">
                  		<button id="warn-next" type="button" class="btn btn-default btn-sm">
                  		<i class="fa fa-chevron-right"></i>
                  		</button>
                  </c:if>
                    <button id="warn-skip" style="margin-left: 10px;" type="button" class="btn btn-default btn-sm">跳转</button>
                  </div>
                  <!-- /.btn-group -->
                </div>
                <!-- /.pull-right -->
              </div>
              <div class="table-responsive mailbox-messages">
                <table class="table table-hover table-striped">
                  <tbody>
                  <c:forEach var="item" items="${warns }">
                  	<tr>
                    <td class="mailbox-name"><b>${item.type_name }</b></td>
                    <td class="mailbox-subject">${item.warn_title }</td>
                    <td>
                    	<c:if test="${item.is_read==0 }">
                    		<span class="label label-danger">
                    			未读
                    		</span>
                    	</c:if>
                    	<c:if test="${item.is_read==1 }">
                    		<span class="label label-success">
                    			已读
                    		</span>
                    	</c:if>
                    </td>
                    <td class="mailbox-date">${item.create_time }</td>
                    <td>
                    	<button onclick="javacricpt:warnInfo('${item.warn_id}')" class="btn btn-xs btn-primary" data-toggle="tooltip" title="查看"><i class="fa fa-eye"></i></button>
                    	<button onclick="javacript:warnRead('${item.warn_id }')" class="btn btn-xs btn-warning" data-toggle="tooltip" title="忽略"><i class="fa fa-minus-square"></i></button>
                    	<button onclick="javascript:link_url('${item.link_url }')" class="btn btn-xs btn-success" data-toggle="tooltip" title="${item.url_des }"><i class="fa fa-share"></i></button>
                    </td>
                  </tr>
                  </c:forEach>
                  </tbody>
                </table>
                <!-- /.table -->
              </div>
              <!-- /.mail-box-messages -->
            </div>
				 </c:if>
            
            <c:if test="${type=='notice' }">
            <div class="notice-list">
				 <div id="notice-skip-content" style="z-index: 1121; position: absolute;font-size:12px; right: 2px; top: -43px; width: 140px;border: 1px solid #ccc;background: #fff;border-radius:5px;display: none;">
	 				<div style="overflow-y:auto;overflow-x:hidden;width:183px;height:auto;">
	 					<div style="height:45px;line-height:45px;">
	 						<div style="position:absolute;width:160px;margin-left:2px;">
	 							<div style="float:left;">跳转到第 
	 								<input type="text" id="notice-skip-page" style="width:30px;height: 20px;"> 
	 							页</div>
	 							<a id="notice-sure" href="javascript:;" class="btn btn-primary" style="width:40px;padding:0;font-size:12px;margin-left:3px; display:inline;">&nbsp;确定&nbsp;</a>
	 						</div>
	 					</div>
	 				</div>
				 </div> 
              <div class="mailbox-controls">
                <div class="btn-group">
                  <button onclick="javacript:noticeRead('${item.notice_id }')" id="notice-all-read" type="button" class="btn btn-default btn-sm">全部标记为已读</button>
                </div>
                <!-- /.btn-group -->
                <button onclick="javascipt:loadPage('notice',1)" type="button" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i></button>
                <div class="pull-right">
                  <span id="notice-page">${page }</span>/<span id="notice-count">${noticeCount }</span>
                  <div class="btn-group">
                  <c:if test="${page!=1 }">
                  		<button id="notice-last" type="button" class="btn btn-default btn-sm"><i class="fa fa-chevron-left"></i></button>
                  </c:if>
                  <c:if test="${page<noticeCount }">
                  		<button id="warn-next" type="button" class="btn btn-default btn-sm"><i class="fa fa-chevron-right"></i></button>
                  </c:if>
                    <button id="notice-skip" style="margin-left: 10px;" type="button" class="btn btn-default btn-sm">跳转</button>
                  </div>
                  <!-- /.btn-group -->
                </div>
                <!-- /.pull-right -->
              </div>
              <div class="table-responsive mailbox-messages">
                <table class="table table-hover table-striped">
                  <tbody>
                  <c:forEach var="item" items="${notices }">
                  	<tr>
                  	<c:if test="${item.receive_type==1 }">
                  		<td class="mailbox-subject"><b>全站通知</b></td>
                  	</c:if>
                  	<c:if test="${item.receive_type==2 }">
                  		<td class="mailbox-subject"><b>部分通知</b></td>
                  	</c:if>
                    <td class="mailbox-name">${item.notice_title }</td>
                    <td>
                    	<c:if test="${item.is_read==0 }">
                    		<span class="label label-danger">
                    			未读
                    		</span>
                    	</c:if>
                    	<c:if test="${item.is_read==1 }">
                    		<span class="label label-success">
                    			已读
                    		</span>
                    	</c:if>
                    </td>
                    <td class="mailbox-date">${item.create_time }</td>
                    <td>
                    	<button onclick="javacript:noticeInfo('${item.notice_id}')" class="btn btn-xs btn-primary" data-toggle="tooltip" title="查看"><i class="fa fa-eye"></i></button>
                    	<button onclick="javacript:noticeRead('${item.notice_id }')" class="btn btn-xs btn-success" data-toggle="tooltip" title="标记为已读"><i class="fa fa-check-circle"></i></button>
                    </td>
                  </tr>
                  </c:forEach>
                  </tbody>
                </table>
                <!-- /.table -->
              </div>
              <!-- /.mail-box-messages -->
            </div>
            </c:if>
            </div>
			</div>
		</div>
	</div>
</tags:adminLayout>
