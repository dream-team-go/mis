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
	<tags:plugins_bootstrapSlider_css/>
	<tags:plugins_dialog_css/>
	<tags:plugins_ztree_css/>
	<tags:plugins_searchHighlight_css/>
	<style>
<!--
.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
-->
</style>
</c:set>
<c:set var="pageJavascript">
	<tags:plugins_dataTables_js/>
	<tags:plugins_bootstrapSlider_js/>
	<tags:plugins_dialog_js/>
	<tags:plugins_ztree_js/>
	<tags:plugins_searchHighlight_js/>
 <script>
 var table;
 var setting = {
	   view: {
			addHoverDom: addHoverDom,
			removeHoverDom: removeHoverDom,
			selectedMulti: false
		},
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
		edit: {
			enable: true
		},
		callback : {
			onClick: zTreeOnClick,
			beforeEditName: zTreeBeforeEditName,
			beforeRemove: zTreeBeforeRemove,
			onDrop: zTreeOnDrop,
			beforeDrop: zTreeBeforeDrop
		}
	};
 
	var setting_add = {
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
			chkStyle: "checkbox"
		},
		callback : {
			onClick: zTreeAddOnClick,
			onCheck: zTreeAddOnCheck
		}
	};
	
	function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {
	    //alert(treeNodes.length + "," + (targetNode ? (targetNode.tId + ", " + targetNode.name) : "isRoot" ));
	};
	function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) {
		var tip = "确定进行拖拽移动吗？";
		var function_id = treeNodes[0].function_id;
		var target_function_id = targetNode.function_id;
		console.log(moveType+",node:" + treeNodes[0].name+",target:" + targetNode.name);
		if(confirm(tip)){
			$.ajax({
 	            url: "${root }/func/moveTo",
 	            type: "POST",
 	            async: false,
 	            data:{function_id:function_id,target_function_id:target_function_id,moveType:moveType},
 	            success: function(data){
 	            	table.ajax.reload();
 	            	layer.msg(data.msg,{icon: 6}); 
 	            }
 	        });
			return true;
		}
		return false;
	};

 	$(document).ready(function() {
 		table = $('#example').DataTable( {
	        "language": {
	            "url": "${root}/statics/plugins/DataTables-1.10.12/Chinese.json"
	        },
	        "processing": true,
	        "serverSide": true,
	        "paging": false,
	        "searching": true,
	        "searchHighlight": true,
	        "lengthChange": true,
	        "responsive": true,
	        "dom": 'Bfrtip',
            "buttons": [
               {
                   "text": '新增菜单/功能',
                   "action": function ( e, dt, node, config ) {
                	   addFunc('');
                   }
               },
               {
                   "text": '重置搜索',
                   "action": function ( e, dt, node, config ) {
                	   clearSearch();
                   }
               }
            ],
	        "ajax": {
	            "url": "${root}/func/funcJson",
	            "type": "POST"
	        },
	        "columns": [
	            { "data": "name" },
				{ "data": "p_name" },
	            { "data": "is_menu"},
	            { "data": "function_url"},
	            { "data": "resource_name"},
	            { "data": "function_id"}
	        ],
	        "columnDefs": [{
	        	"targets": '_all',
	        	"orderable": false
	        },{
	            "targets": 0,
	            "render": function ( data, type, full, meta ) {
                	return '<i class="fa '+full.icon+'"></i>'+data;
	            }
	        },{
	            "targets": 2,
	            "render": function ( data, type, full, meta ) {
	                if(data==1){
	                	return "<img src=\"${root }/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/fun_url.png\">菜单";
	                }else{
	                	return "<img src=\"${root }/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/function.png\">功能";
	                }
	            }
	        },{
	            "targets": -1,
	            "width":"100px",
	            "render": function ( data, type, full, meta ) {
	                return '<button onclick="addFunc(\''+data+'\')" type="button" class="btn  btn-primary btn-xs"><i class="fa fa-edit"></i></button>&nbsp;'
	                      +'<button onclick="del(\''+data+'\')" type="button" class="btn  btn btn-danger btn-xs"><i class="fa fa-trash"></i></button>';
	            }
	        }]
	    } );
 		
 		initTree();
	} );
 	
 	function clearSearch(){
		table.search('').draw();
	}
 	
 	var treeObj;
 	function initTree(){
 		$.post("${root}/func/func4TreeWithIconJson", {}, function(data) {
    		$.fn.zTree.init($("#func_tree"), setting, data);//初始化树
    		treeObj = $.fn.zTree.getZTreeObj("func_tree");
    		treeObj.expandAll(true);//折叠全部
    	});
 	}
 	
 	function initFuncTree(){
		$.post("${root}/func/func4TreeWithIconJson", {}, function(data) {
			$.fn.zTree.init($("#func_add_tree"), setting_add, data);
			var treeObjAdd = $.fn.zTree.getZTreeObj("func_add_tree");
			 if($("#p_id").val()!='' && $("#p_id").val()!='0'){
				var node = treeObjAdd.getNodeByParam("function_id", $("#p_id").val(), null);
	  			treeObjAdd.checkNode(node, true, false);
	  			treeObjAdd.expandNode(node, true, false, true);
			 }
		});
	}
 	
 	function initFuncTree2(p_id){
 		$("#isParentRadio0").click();
		$.post("${root}/func/func4TreeWithIconJson", {}, function(data) {
			$.fn.zTree.init($("#func_add_tree"), setting_add, data);
			var treeObjAdd = $.fn.zTree.getZTreeObj("func_add_tree");
         	var node = treeObjAdd.getNodeByParam("function_id", p_id, null);
         	treeObjAdd.checkNode(node, true, false);
         	treeObjAdd.expandNode(node, true, false, true);
		});
	}
 	
 	function zTreeOnClick(event, treeId, treeNode) {
 		table.search(treeNode.name).draw();
	};
	
 	function zTreeAddOnClick(event, treeId, treeNode) {
 		var treeObj = $.fn.zTree.getZTreeObj("func_add_tree");
 		treeObj.checkAllNodes(false);
 		treeObj.checkNode(treeNode, true, false);
	};
	
 	function zTreeAddOnCheck(event, treeId, treeNode) {
 		var treeObj = $.fn.zTree.getZTreeObj("func_add_tree");
 		treeObj.checkAllNodes(false);
 		treeObj.checkNode(treeNode, true, false);
	};
	
	function zTreeAddBeforeCheck(treeId, treeNode) {
		var treeObj = $.fn.zTree.getZTreeObj("func_add_tree");
 		//treeObj.checkAllNodes(false);
	};
	
	var newCount = 1;
	function addHoverDom(treeId, treeNode) {
		var sObj = $("#" + treeNode.tId + "_span");
		if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
		var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='添加功能权限' onfocus='this.blur();'></span>";
		sObj.after(addStr);
		var btn = $("#addBtn_"+treeNode.tId);
		if (btn) btn.bind("click", function(){
			addFunc2('',treeNode.function_id);
			return false;
		});
	};
	
	function removeHoverDom(treeId, treeNode) {
		$("#addBtn_"+treeNode.tId).unbind().remove();
	};
	
	 function zTreeBeforeEditName(treeId, treeNode) {
		 addFunc(treeNode.function_id);
	 	return false;
	 }
	 
	 function zTreeBeforeRemove(treeId, treeNode) {
		del(treeNode.function_id);
		return false;
	 }
 	
 	function del(function_id){
 		layer.confirm('确认要删除该菜单/功能吗？', {icon: 3, title:'提示'}, function(index){
 			$.ajax({
 	            url: "${root }/func/del",
 	            type: "POST",
 	            data:{function_id:function_id},
 	            success: function(data){
 	            	table.ajax.reload();
 	            	layer.msg(data.msg,{icon: 6}); 
 	            	initTree();
 	            }
 	        });
		  	layer.close(index);
		});
 		
 	}
 	
 	function addFunc(function_id){
 		BootstrapDialog.show({
 			title: function_id==undefined?'添加菜单/功能':'编辑菜单/功能',
            message: $('<div></div>').load('${root }/func/addFunc?function_id='+function_id),
            closeByBackdrop: false,
            closeByKeyboard: false,
            buttons: [{
                label: '保存',
                cssClass: 'btn-primary',
                icon: 'glyphicon glyphicon-ok',
                action: function(dialog) {
                    $('#func_edit_form').submit();
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
            	$.post("${root}/func/func4TreeWithIconJson", {}, function(data) {
        			$.fn.zTree.init($("#func_add_tree"), setting_add, data);
        			var treeObjAdd = $.fn.zTree.getZTreeObj("func_add_tree");
        			 if($("#p_id").val()!='' && $("#p_id").val()!='0'){
        				var node = treeObjAdd.getNodeByParam("function_id", $("#p_id").val(), null);
        	  			treeObjAdd.checkNode(node, true, false);
        	  			treeObjAdd.expandNode(node, true, false, true);
        			 }
        			 
        			 $('.slider').slider({
                 	    range: false,
                 	    min: 0,
                 	    max: 200
                 	});
        			 
        			 $("input[name='isParentRadio']").on('click',function(){//1 父菜单 0子菜单
                 		if($(this).val()==1){
                 			$("#div_func_add_tree").hide();
                 			var treeObj = $.fn.zTree.getZTreeObj("func_add_tree");
                 			treeObj.checkAllNodes(false);
                 			$("#p_id").val("0");
                 		}
                 		if($(this).val()==0){
                 			$("#div_func_add_tree").show();
                 		}
                 	});
                 	
                 	$("#func_edit_form").validator({
              		    valid: function(form){
              		        var me = this;
              		        // 提交表单之前，hold住表单，并且在以后每次hold住时执行回调
              		        me.holdSubmit(function(){
              		            layer.msg("正在处理中...");
              		        });
              		        
              		        var treeObj = $.fn.zTree.getZTreeObj("func_add_tree");
              		        var nodes = treeObj.getCheckedNodes(true);
              		        if(nodes.length>0){
     	         		        $("#p_id").val(nodes[0].function_id);
              		        } else {
              		        	$("#p_id").val("0");
              		        }
              		        $.ajax({
              		            url: "${root }/func/saveOrUpdateFunc",
              		            data: $(form).serialize(),
              		            type: "POST",
              		            success: function(data){
              		                // 提交表单成功后，释放hold，就可以再次提交
              		                me.holdSubmit(false);
              		                if(data.status=='y'){
              		                	dialog.close();
              		                	table.ajax.reload();
              		                	layer.msg(data.msg,{icon: 6}); 
              		                	initTree();
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
 	
 	function addFunc2(function_id,p_id){
 		BootstrapDialog.show({
 			title: function_id==undefined?'添加菜单/功能':'编辑菜单/功能',
            message: $('<div></div>').load('${root }/func/addFunc'),
            closeByBackdrop: false,
            closeByKeyboard: false,
            buttons: [{
                label: '保存',
                cssClass: 'btn-primary',
                icon: 'glyphicon glyphicon-ok',
                action: function(dialog) {
                    $('#func_edit_form').submit();
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
            	$('.slider').slider({
            	    range: false,
            	    min: 0,
            	    max: 200
            	});
            	
            	$("input[name='isParentRadio']").on('click',function(){//1 父菜单 0子菜单
            		if($(this).val()==1){
            			$("#div_func_add_tree").hide();
            			var treeObj = $.fn.zTree.getZTreeObj("func_add_tree");
            			treeObj.checkAllNodes(false);
            			$("#p_id").val("0");
            		}
            		if($(this).val()==0){
            			$("#div_func_add_tree").show();
            		}
            	});
            	
            	initFuncTree2(p_id);
            	
            	$("#func_edit_form").validator({
         		    valid: function(form){
         		        var me = this;
         		        // 提交表单之前，hold住表单，并且在以后每次hold住时执行回调
         		        me.holdSubmit(function(){
         		            layer.msg("正在处理中...");
         		        });
         		        
         		        var treeObj = $.fn.zTree.getZTreeObj("func_add_tree");
         		        var nodes = treeObj.getCheckedNodes(true);
         		        if(nodes.length>0){
	         		        $("#p_id").val(nodes[0].function_id);
         		        } else {
         		        	$("#p_id").val("0");
         		        }
         		        $.ajax({
         		            url: "${root }/func/saveOrUpdateFunc",
         		            data: $(form).serialize(),
         		            type: "POST",
         		            success: function(data){
         		                // 提交表单成功后，释放hold，就可以再次提交
         		                me.holdSubmit(false);
         		                if(data.status=='y'){
         		                	dialog.close();
         		                	table.ajax.reload();
         		                	layer.msg(data.msg,{icon: 6}); 
         		                	initTree();
         		                }else{
         		                	layer.open({
         		                		  content: data.msg
       		                		});
         		                }
         		            }
         		        });
         		    }
         		});
            }
        });
 	}
 </script>      
</c:set>
<tags:adminLayout pageCss="${pageCss }" pageJavascript="${pageJavascript }">
	<div class="callout callout-info">
      <h4>温馨提示!</h4>
      <p><img src="${root }/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/fun_url.png">菜单是可以在左边功能菜单中显示出来的，<img src="${root }/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/function.png">功能是不会显示在菜单中，会在页面中以链接、按钮的形式显示。您可以在功能菜单树中使用拖拽的方式进行移动和排序。</p>
    </div>
    <div class="row">
		<div class="col-md-2">
			<div class="box box-info">
				<div class="box-header">
					<h3 class="box-title"><img src="${root }/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/fun_url.png">菜单-<img src="${root }/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/function.png">功能</h3>
				</div>
				<div class="box-body">
					<ul id="func_tree" class="ztree" style="margin-top:0; width:200px;"></ul>
				</div>
			</div>
		</div>
		<div class="col-md-10">
		
			<div class="box box-info">
				<!-- Main content -->
		    	<section class="content">
					<table id="example" class="table table-hover table-striped table-bordered nowrap" cellspacing="0" width="100%">
						<thead>
				            <tr>
				                <th>菜单/功能名称</th>
				                <th>父菜单名称</th>
				                <th>菜单/功能</th>
				                <th>链接</th>
				                <th>资源</th>
				                <th>相关操作</th>
				            </tr>
				        </thead>
					</table>
				</section>
			</div>
		</div>
		</div>
</tags:adminLayout>
