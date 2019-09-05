<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!-------注意放置在tags:adminlayout的上面 ------->
<c:set var="pageCss">
	<tags:plugins_dataTables_css/>
	<tags:plugins_dialog_css/>
	<tags:plugins_ztree_css/>
</c:set>
<c:set var="pageJavascript">
	<tags:plugins_dataTables_js/>
	<tags:plugins_dialog_js/>
	<tags:plugins_ztree_js/>
	<tags:plugins_echarts_js/> 
 <script>
 var table;
 var param = {};
 var setting = {//ztree设置
			data : {
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "pid",
					rootPId : 0
				},
				key : {
					name : "text"
				}
			},
			edit: {
				enable: false
			},
			check: {
				enable: true
			}
		 };
 
 
 	$(document).ready(function() {
 		table = $('#example').DataTable( {
	        "language": {
	            "url": "${root}/statics/plugins/DataTables-1.10.12/Chinese.json"
	        },
	        "processing": true,
	        "serverSide": true,
	        "paging": true,
	        "pageLength": 20,
	        "searching": true,
	        "responsive": true,
	        "lengthChange": false,
	        "dom": 'lBfrtip',
	        "order": [[ 4, 'desc' ]],
            "buttons": [
               {
                   "text": '新增通知',
                   "action": function ( e, dt, node, config ) {
                	   addNotice("");
                   }
               }
            ],
	        "ajax": {
	            "url": "${root}/info/noticeJson",
	            "type": "POST"
	        },
	        "columns": [
				{ "data": "notice_title" },
	            { "data": "receive_type" },
	            { "data": "user_cn_name"},
	            { "data": "create_time"},
	            { "data": "notice_id"}
	        ],
	        "columnDefs": [{
	            "targets": 1,
	            "width":"100px",
	            "orderable": false,
	            "render": function ( data, type, full, meta ) {
	            	if(data==1){
	            		return "全站接收";	
	            	}else{
	            		return "全站接收";	
	            	}
	            }
	        },{
	            "targets": -1,
	            "width":"80px",
	            "orderable": false,
	            "render": function ( data, type, full, meta ) {
	                return '<button onclick="preview(\''+data+'\')" type="button" data-toggle="tooltip" data-original-title="查看详情" class="btn  btn btn-primary btn-xs"><i class="glyphicon glyphicon-eye-open"></i></button>&nbsp;&nbsp;'
	                      +'<button onclick="del(\''+data+'\')" type="button" data-toggle="tooltip" data-original-title="删除" class="btn  btn btn-danger btn-xs"><i class="fa fa-trash"></i></button>&nbsp;&nbsp;';
	            }
	        }]
	    } );
	} );
 	function del(notice_id){
 		layer.confirm('确认要删除该通知吗？', {icon: 3, title:'提示'}, function(index){
 			$.ajax({
 	            url: "${root }/info/noticeDel",
 	            type: "POST",
 	            data:{notice_id:notice_id},
 	            success: function(data){
 	            	table.ajax.reload();
 	            	layer.msg(data.msg,{icon: 6}); 
 	            }
 	        });
		  	layer.close(index);
		});
 		
 	}
 	function addNotice(notice_id){
 		location.href="${root}/info/noticeAdd";
 	}
 	
 	function preview(notice_id){
		 BootstrapDialog.show({
	 			title: '预览',
	            message: $('<div></div>').load("${root }/info/preview?notice_id=" + notice_id),
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
	            	$.post("${root}/info/roleUsreJson" ,{}, function(data) {
	    				$.fn.zTree.init($("#org_tree"), setting, data);
	    				var treeObj = $.fn.zTree.getZTreeObj("org_tree");
	    				treeObj.expandAll(true);
	    				
	    				if($("#user_ids").val()==''){
	    					treeObj.checkAllNodes(true);
	    				} else {
	    					$.each($("#user_ids").val().split(","),function(i,val){
	    						var node = treeObj.getNodeByParam("id", val, null);
	            				treeObj.checkNode(node, true, true);
	    					});
	    					if($("#read_user_ids").val()!=''){
		    					$.each($("#read_user_ids").val().split(","),function(i,val){
		    						var node = treeObj.getNodeByParam("id", val, null);
		            				node.text = node.text+"(已读)";
		            				node.icon = "${root}/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/read_user.png";
		            				treeObj.updateNode(node);
		    					});
	    					}
	    				}
	    			});
	            }
	        });
	 }
 	
	 function search(){
 	    table.ajax.reload();
	 }
 </script>      
</c:set>
<tags:adminLayout pageCss="${pageCss }" pageJavascript="${pageJavascript }">
	<div class="box box-info">
    	<section class="content">
			<table id="example" class="table table-hover table-striped table-bordered nowrap" cellspacing="0" width="100%">
				<thead>
		            <tr>
		                <th>标题</th>
		                <th>类型</th>
		                <th>创建人</th>
		                <th>创建时间</th>
		                <th>相关操作</th>
		            </tr>
		        </thead>
			</table>
		</section>
	</div>
</tags:adminLayout>
