<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ page import="java.util.Collection" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!-------注意放置在tags:mainlayout的上面 ------->
<c:set var="pageCss">
	<tags:plugins_dataTables_css/>
	<tags:plugins_ztree_css/>
	<tags:plugins_dialog_css/>
	<tags:plugins_searchHighlight_css/>
	<tags:plugins_searchable_css/>
</c:set>
<c:set var="pageJavascript">
	 <tags:plugins_dataTables_js/>
	 <tags:plugins_ztree_js/>
	 <tags:plugins_dialog_js/>
	 <tags:plugins_searchHighlight_js/>
	 <tags:plugins_searchable_js/>
	 <script>
		var table;
	 	$(document).ready(function() {
	 		table = $('#example').DataTable( {
		        "language": {
		            "url": "${root}/statics/plugins/DataTables-1.10.12/Chinese.json"
		        },
		        "processing": true,
		        "serverSide": true,
		        "paging": true,
		        "pageLength": 25,
		        "searching": true,
		        "searchHighlight": true,
		        "responsive": true,
		        "lengthChange": false,
		        "dom": 'lBfrtip',
		        "order": [[6, 'desc' ]],
	            "buttons": [
	               {
	                   "text": '新增用户',
	                   "action": function ( e, dt, node, config ) {
	                	   addUser("");
	                   }
	               }
	            ],
		        "ajax": {
		            "url": "${root}/user/usersJson",
		            "type": "POST"
		        },
		        "columns": [
					{ "data": "user_cn_name" },
					{ "data": "username" },
		            { "data": "tel_no" },
		            { "data": "sex"},
		            { "data": "role_name"},
		            { "data": "is_locked"},
		            { "data": "create_time"},
		            { "data": "user_id"}
		        ],
		        "columnDefs": [{
		            "targets": 3,
		            "render": function ( data, type, full, meta ) {
		                if(data==1){
		                	return "男";
		                }else{
		                	return "女";
		                }
		            }
		        },{
		            "targets": [5],
		            "render": function ( data, type, full, meta ) {
		                if(data==1){
		                	return "是";
		                }else{
		                	return "否";
		                }
		            }
		        },{
		            "targets": -1,
		            "width":"80px",
		            "data": null,
		            "orderable": false,
		            "render": function ( data, type, full, meta ) {
		            	var btnClass = "";
		            	var btnTitle="";
		            	if(full.is_locked==0){
		            		btnClass = "fa-lock";
		            		btnTitle="锁定";
		            	}else{
		            		btnClass = "fa-unlock";
		            		btnTitle="解锁";
		            	}
		            	var html = '<button onclick="addUser(\''+full.user_id+'\')" type="button" data-toggle="tooltip" data-original-title="编辑" class="btn  btn-primary btn-xs"><i class="fa fa-edit"></i></button>&nbsp;';
		                html += '<button title="'+btnTitle+'" onclick="lock(\''+full.user_id+'\','+full.is_locked+')" type="button" class="btn  btn btn-warning btn-xs"><i class="fa '+btnClass+'"></i></button>&nbsp;';
		                if(full.allow_del==1){
			                html += '<button title="删除用户" onclick="del(\''+full.user_id+'\')" type="button" class="btn  btn btn-danger btn-xs"><i class="fa fa-trash"></i></button>&nbsp;';
		                } else {
			                html += '<button disabled="disabled" title="该用户禁止删除"  type="button" class="btn  btn btn-danger btn-xs"><i class="fa fa-trash"></i></button>&nbsp;';
		                }
		                return html;
		            }
		        }]
		    } );
		} );
	 	function lock(user_id,is_locked){
	 		var msg="";
	 		if(is_locked == 1){
	 			msg = "确认要解锁该用户吗？";
	 		}else{
	 			msg = "确认要锁定该用户吗？";
	 		}
	 		layer.confirm(msg, {icon: 3, title:'提示'}, function(index){
	 			$.ajax({
	 	            url: "${root }/user/lock",
	 	            type: "POST",
	 	            data:{user_id:user_id,is_locked:is_locked},
	 	            success: function(data){
	 	            	table.ajax.reload();
	 	            	layer.msg(data.msg,{icon: 6}); 
	 	            }
	 	        });
			  	layer.close(index);
			});
	 	}
	 	
	 	function del(user_id){
	 		layer.confirm('确认要删除该用户吗？', {icon: 3, title:'提示'}, function(index){
	 			$.ajax({
	 	            url: "${root }/user/del",
	 	            type: "POST",
	 	            data:{user_id:user_id},
	 	            success: function(data){
	 	            	table.ajax.reload();
	 	            	layer.msg(data.msg,{icon: 6}); 
	 	            }
	 	        });
			  	layer.close(index);
			});
	 	}
	 	
	 	var add_dialog;
	 	function addUser(user_id){
	 		add_dialog = BootstrapDialog.show({
	 			title: user_id == ''?'添加用户':'编辑用户',
	            message: $('<div></div>').load('${root}/user/addUser?user_id='+user_id),
	            closeByBackdrop: false,
	            closeByKeyboard: false,
	            buttons: [{
	                label: '保存',
	                cssClass: 'btn-primary',
	                icon: 'glyphicon glyphicon-ok',
	                action: function(dialog) {
	                    $('#user_edit_form').submit();
	                }
	            },{
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
			<table id="example" class="table table-hover table-striped table-bordered nowrap" cellspacing="0" width="100%">
				<thead>
		            <tr>
		                <th>姓名</th>
		                <th>用户名(账号)</th>
		                <th>电话</th>
		                <th>性别</th>
		                <th>角色名称</th>
		                <th>是否锁定</th>
		                <th>创建时间</th>
		                <th>相关操作</th>
		            </tr>
		        </thead>
			</table>
		</section>
	</div>
</tags:adminLayout>
