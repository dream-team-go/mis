<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-------注意放置在tags:adminlayout的上面 ------->
<c:set var="pageCss">
	<tags:plugins_ztree_css/>
</c:set>
<c:set var="pageJavascript">
	<tags:plugins_ueditor_js/>
	<tags:plugins_ztree_js/>
	
	<script type="text/javascript">
	//var userIds = "${user_ids}";
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
		},
		callback: {
			onCheck: zTreeOnCheck
		}
	 };
	function zTreeOnCheck(event, treeId, treeNode) {
	    //alert(treeNode.isuser + ", " + treeNode.name + "," + treeNode.checked);
	};
		
		$(function() {
			//初始化编辑器
			var ue = UE.getEditor('notice_content', {
				toolbars : [ [ 'source', 'undo', 'redo', 'pasteplain', 'print',
						'removeformat', 'simpleupload', 'insertimage', 'link',
						'map', 'insertvideo', 'insertorderedlist',
						'insertunorderedlist', 'attachment', 'wordimage',
						'template', 'charts', 'inserttable', 'forecolor', 'bold','justifyleft',
				        'justifyright', 
				        'justifycenter', 
				        'justifyjustify',
						'fontfamily', 'fontsize', 'paragraph' ,'emotion', 'spechars','autotypeset','formatmatch'] ]
			});
			
			/* ztree初始化 */
			$.post("${root}/info/roleUsreJson", {}, function(data) {
				$.fn.zTree.init($("#role_tree"), setting, data);
				var treeObj = $.fn.zTree.getZTreeObj("role_tree");
				treeObj.expandAll(true);
			});
			//通知类型未部分通知时 显示选择 接受人
			$("input[name='notice.receive_type']").on("change",function(){
				var type=$("input[name='notice.receive_type']:checked").val();
				if(type==2){
					$("#user_box").show();
				}else{
					//重置树
					var treeObj = $.fn.zTree.getZTreeObj("role_tree");//根据 treeId 获取 zTree 对象 
					treeObj.expandAll(true);//折叠全部节点,参数为true时表示展开全部节点
					treeObj.checkAllNodes(false);//实现不选中任何节点
					//清空选中 接受人数据
					$("#user_ids").val("");
					$("#user_box").hide();
				}
			});
			
			$("#notice_from").validator({
			    valid: function(form){
			        var me = this;
			        var type=$("input[name='notice.receive_type']:checked").val();
			        //对其他必填进行校验
			        if($('#notice_title').val()==""){
						layer.msg('请填写标题！', function(){});
						$("#notice_title").focus();
						return false;	
					} 
					
					if(!ue.hasContents()){
						layer.msg('内容不能为空！', function(){});
						return false;
					}
					if(type==2){
						//站内信需要选择接收人
						var treeObj = $.fn.zTree.getZTreeObj("role_tree");
						var nodes = treeObj.getCheckedNodes(true);
						if(nodes.length == 0){
							layer.msg('请选择通知接收人！', function(){});
							return false;	
						} else {
							var user_ids =[];
							$.each(nodes,function(i,val){
								if(val.isuser==1){
									user_ids.push(val.id);
								}
							});
							if(user_ids==''){
								layer.msg('请勾选群组下面的接收人！');
								return false;	
							}else {
								$("#user_ids").val(user_ids.join(","));
							}
						}	
					}
			        // 提交表单之前，hold住表单，并且在以后每次hold住时执行回调
			        me.holdSubmit(function(){
			            layer.msg("正在处理中...");
			        });
			        $.ajax({
			            url: "${root }/info/noticeSaveOrUpdate",
			            data: $(form).serialize(),
			            type: "POST",
			            success: function(data){
			                // 提交表单成功后，释放hold，就可以再次提交
			                me.holdSubmit(false);
			                if(data.status == 'y'){
				        		layer.confirm(data.msg, {icon: 1, title:'提示', btn:['消息列表','继续发布通知']}, function(index){
				        			location.href="${root}/info/noticeList";
				        		},function(){
								    location.href="${root}/info/noticeAdd";
				        		});
			                }
			            }
			        });
			    }
			});
		});
		
	</script>
</c:set>
<tags:adminLayout pageCss="${pageCss }" pageJavascript="${pageJavascript }">
    <form id="notice_from" method="post"  data-validator-option="{theme: 'bottom'}">
    <div class="row">
        <!-- left column -->
        <div class="col-md-8">
        	<div class="box box-primary">
	            <div class="box-body">
	              <div class="form-group">
	                <label for="exampleInputEmail1">通知标题</label>
	                <input type="text" name="notice.notice_title"  value="" class="form-control" id="notice_title" placeholder="请输入通知标题">
	              </div>
	              <div class="form-group">
	                <label for="notice_content">内容</label>
	                <textarea rows="" style="width: 100%;height: 230px;" name="notice.content" id="notice_content" cols=""></textarea>
	              </div>
	            </div>
		    </div>
        </div>
        <div class="col-md-4">
        	<div class="box box-primary">
	            <div class="box-body" id="notice_type">
	              <div class="form-group">
	                <label for="recipient_user_id">选择通知接收类型</label>
	                <div class="radio">
						<label><input type="radio" checked="checked" name="notice.receive_type" value="1">全站接收&nbsp;&nbsp;</label> 
						<label><input type="radio" name="notice.receive_type" value="2">部分接收&nbsp;&nbsp;</label>
					</div>
	              </div>
	            </div>
	        </div>
        	<div id="user_box" class="box box-primary" style="display:none;">
	            <div class="box-body" id="receive_user">
	              <div class="form-group" style="max-height: 750px;overflow-y:scroll;">
	                <label for="recipient_user_id">选择通知接收人</label>
	                <ul id="role_tree" class="ztree" style="margin-top:0; width:300px;"></ul>
	                <input type="hidden" value="" name="user_ids" id="user_ids" />
	              </div>
	            </div>
	        </div>
	       	<button type="submit" class="btn btn-primary"><i class="fa fa-save"> 保存并发布</i></button>
        </div>
     </div>
     </form>
</tags:adminLayout>
