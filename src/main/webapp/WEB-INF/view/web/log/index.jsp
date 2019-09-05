<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-------注意放置在tags:mainlayout的上面 ------->
<c:set var="pageCss">
	<tags:plugins_dataTables_css/>
	<tags:plugins_dialog_css/>
</c:set>
<c:set var="pageJavascript">
	 <tags:plugins_dataTables_js/>
	 <tags:plugins_timeago_js/>
	 <tags:plugins_dialog_js/>
	 <script>
		var table;
	 	$(document).ready(function() {
	 		table = $('#example').DataTable({
	 		      "paging": true,
	 		      "lengthChange": true,
	 		      "searching": true,
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
	 		          "url": '${root}/log/logJson',
	 		          "type": 'POST'
	 		      },
	 		      "deferRender": true,
	 		      "columns": [
	 		            { "data": "log_create_time"},
	 	                { "data": "log_content" },
			            { "data": "user_cn_name" },
			            { "data": "log_ip" },
			            { "data": "log_request_method" },
			            { "data": "log_id" }
			           
	 	           ],
	 	          "order": [[ 0, 'desc' ]],
		 	       "columnDefs": [{
		 	            "targets": 0,
		 	            "render": function ( data, type, full, meta ) {
		 	                return $.timeago(data);
		 	            }
		 	        },{
		 	           "targets": -1,
		 	           "orderable": false,
		 	           "render": function ( data, type, full, meta ) {
			                return '<button onclick="preview(\''+data+'\')" type="button" title="预览请求参数" class="btn  btn btn-danger btn-xs"><i class="glyphicon glyphicon-eye-open"></i></button>';
			            }
		 	        }]
	 		   });
		} );
	 	
	 	function preview(log_id){
			 BootstrapDialog.show({
		 			title: '预览',
		            message: $('<div></div>').load("${root }/log/preview?log_id=" + log_id),
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
		            }
		        });
		 	}
	 </script>      
</c:set>
<tags:adminLayout pageCss="${pageCss }" pageJavascript="${pageJavascript }">
	<!-- <div class="callout callout-info">
      <h4>温馨提示!</h4>
      <p>您可以在此页面配置系统的基本参数，保存后需重新登录系统方可生效。</p>
    </div> -->
	<div class="box box-info">
		<!-- Main content -->
    	<section class="content">
			<table id="example" class="table table-hover table-striped table-bordered nowrap">
				<thead>
		            <tr>
		                <th style="width: 10%">创建时间</th>
		                <th>描述</th>
		                <th>操作人</th>
		                <th>IP</th>
		                <th>请求路径</th>
		                <th>操作</th>
		            </tr>
		        </thead>
		        <tfoot>
		            <tr>
		            	<th>创建时间</th>
		                <th>描述</th>
		                <th>操作人</th>
		                <th>IP</th>
		                <th>请求路径</th>
		                <th>操作</th>
		            </tr>
		        </tfoot>
			</table>
		</section>
	</div>
</tags:adminLayout>
