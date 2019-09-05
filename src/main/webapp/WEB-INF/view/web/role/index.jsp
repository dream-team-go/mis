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
<tags:plugins_ztree_css/>
<tags:plugins_dialog_css/>
<tags:plugins_searchHighlight_css/>
<style>
</style>
</c:set>
<c:set var="pageJavascript">
	 <tags:plugins_dataTables_js/>
	 <tags:plugins_ztree_js/>
	 <tags:plugins_dialog_js/>
	 <tags:plugins_searchHighlight_js/>
	 <script>
		var table;
		var setting = {
			data : {
				simpleData : {
					enable : true,
					idKey : "function_id",
					pIdKey : "p_id",
					rootPId : 0
				},
				key : {
					name : "name"
				}
			},
			check: {
				enable: true,
				chkStyle: "checkbox",
				chkboxType: { "Y": "p", "N": "s" }
			},
			edit: {
				enable: false
			}
		};
	 	$(document).ready(function() {
	 		table = $('#example').DataTable( {
		        "language": {
		            "url": "${root}/statics/plugins/DataTables-1.10.12/Chinese.json"
		        },
		        "processing": true,
		        "serverSide": true,
		        "responsive": true,
		        "paging": false,
		        "searching": true,
		        "searchHighlight": true,
		        "lengthChange": true,
		        "dom": 'Bfrtip',
	            "buttons": [
	               {
	                   "text": '新增角色',
	                   "action": function ( e, dt, node, config ) {
	                	   addRole();
	                   } 
	               }
	            ], 
		        "ajax": {
		            "url": "${root}/role/roleJson",
		            "type": "POST"
		        },
		        "columns": [
					{ "data": "role_name" },
		            { "data": "role_desc" },
		            { "data": "role_id"}
		        ],
		        "columnDefs": [{
		        	"targets": '_all',
		        	"orderable": false
		        },{
		            "targets": -1,
		            "width":"100px",
		            "render": function ( data, type, full, meta ) {
		                var html = '<button onclick="addRole(\''+data+'\')" title="编辑查看" type="button" class="btn btn-primary btn-xs"><i class="fa fa-edit"></i></button>&nbsp;';
	                    if(full.allow_del==1){
			                html += '<button onclick="del(\''+data+'\')" title="删除" type="button" class="btn  btn btn-danger btn-xs"><i class="fa fa-trash"></i></button>&nbsp;';
		                } else {
			                html += '<button disabled="disabled" title="该角色禁止删除"  type="button" class="btn  btn btn-danger btn-xs"><i class="fa fa-trash"></i></button>&nbsp;';
		                }
		            	return html;
		            }
		        }]
		    } );
		} );
	 	
	 	function del(role_id){
	 		layer.confirm('确认要删除该角色吗？', {icon: 3, title:'提示'}, function(index){
	 			$.ajax({
	 	            url: "${root }/role/del",
	 	            type: "POST",
	 	            data:{role_id:role_id},
	 	            success: function(data){
	 	            	table.ajax.reload();
	 	            	layer.msg(data.msg,{icon: 6}); 
	 	            }
	 	        });
			  	layer.close(index);
			});
	 	}
	 	
	 	function addRole(role_id){
	 		BootstrapDialog.show({
	 			title: role_id==undefined?'添加角色':'编辑角色',
	            message: $('<div></div>').load('${root }/role/addRole?role_id='+role_id),
	            closeByBackdrop: false,
	            closeByKeyboard: false,
	            buttons: [{
	                label: '保存',
	                cssClass: 'btn-primary',
	                icon: 'glyphicon glyphicon-ok',
	                action: function(dialog) {
	                    $('#role_edit_form').submit();
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
	            	$.post("${root}/func/func4TreeJson", {}, function(data) {
	            		$.fn.zTree.init($("#func_tree"), setting, data);//初始化树
	            		var treeObj = $.fn.zTree.getZTreeObj("func_tree");
	            		treeObj.expandAll(true);
	            		if($('#function_ids').val()!=''){
	            			$.each($('#function_ids').val().split(','),function(i,val){
	            				var node = treeObj.getNodeByParam("function_id", val, null);
	            				if(node!=null){
	            					treeObj.checkNode(node, true, true);
	            				}
	            			});
	            		}
	            		
	            		$("#role_edit_form").validator({
		         		    valid: function(form){
		         		        var me = this;
		         		        // 提交表单之前，hold住表单，并且在以后每次hold住时执行回调
		         		        me.holdSubmit(function(){
		         		            layer.msg("正在处理中...");
		         		        });
		         		        var treeObj = $.fn.zTree.getZTreeObj("func_tree");
		         				var nodes = treeObj.getCheckedNodes(true);
		         				var func_ids=[];
		         				for (var i = 0; i < nodes.length; i++) {
		         					var node = nodes[i];
		         					func_ids.push(node.function_id);
		         				}
		         				$('#function_ids').val(func_ids.join(","));
		         		        $.ajax({
		         		            url: "${root }/role/saveOrUpdateRole",
		         		            data: $(form).serialize(),
		         		            type: "POST",
		         		            success: function(data){
		         		                // 提交表单成功后，释放hold，就可以再次提交
		         		                me.holdSubmit(false);
		         		                if(data.status=='y'){
		         		                	dialog.close();
		         		                	table.ajax.reload();
		         		                	layer.msg(data.msg,{icon: 6}); 
		         		                }else{
		         		                	layer.open({
		         		                		  content: data.msg
		       		                		});
		         		                }
		         		            }
		         		        });
		         		    }
		         		});
	            		
	            	});
	            	
	            }
	        });
	 	}
	 </script>      
</c:set>
<tags:adminLayout pageCss="${pageCss }" pageJavascript="${pageJavascript }">
	<div class="callout callout-info">
      <h4>温馨提示!</h4>
      <p>若为角色配置了权限，用户需重新登录系统方可生效。</p>
    </div>
	<div class="box box-info">
		<!-- Main content -->
    	<section class="content">
			<table id="example" class="table table-striped table-bordered nowrap" cellspacing="0" width="100%">
				<thead>
		            <tr>
		                <th>角色名称</th>
		                <th>角色描述</th>
		                <th>相关操作</th>
		            </tr>
		        </thead>
		        <!-- <tfoot>
		            <tr>
		                <th>角色名称</th>
		                <th>角色描述</th>
		                <th>相关操作</th>
		            </tr>
		        </tfoot> -->
			</table>
		</section>
	</div>
</tags:adminLayout>
