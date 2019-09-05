<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
	<script>
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
			edit : {
				enable : false
			},
			check : {
				enable : true
			},
			callback : {
				onCheck : zTreeOnCheck
			}
		};
		function zTreeOnCheck(event, treeId, treeNode) {
			//alert(treeNode.isuser + ", " + treeNode.name + "," + treeNode.checked);
		};
		
		/*查询提醒类型对应用户  并重新加载树*/
		function findUser(type_id){
			$.post("${root}/info/getUsersByType",{id:type_id},function(data){
				var treeObj = $.fn.zTree.getZTreeObj("role_tree");
				if(treeObj && data.length>0){
					for(var i=0;i<data.length;i++){
						var node = treeObj.getNodeByParam("id", data[i].user_id, null);
						if(node){
							treeObj.checkNode(node, true, true);
							treeObj.expandNode(node.getParentNode(),true);
						}	
					}
					$("input[name='receive_type']").removeAttr("checked");
					$("input[name='receive_type'][value='2']").prop("checked",true);
					$("#user_box").show();
				}else{
					$("input[name='receive_type']").removeAttr("checked");
					$("input[name='receive_type'][value='2']").prop("checked",true);
					$("#user_box").show();
				}
			});
		}
		
		/*重置树*/
		function setTree(){
			//重置树
			var treeObj = $.fn.zTree.getZTreeObj("role_tree");//根据 treeId 获取 zTree 对象 
			treeObj.expandAll(true);//折叠全部节点,参数为true时表示展开全部节点
			treeObj.checkAllNodes(false);//实现不选中任何节点
		}
		
		$(function(){
			/* ztree初始化 */
		 	$.post("${root}/info/roleUsreJson", {}, function(data) {
				$.fn.zTree.init($("#role_tree"), setting, data);
				var treeObj = $.fn.zTree.getZTreeObj("role_tree");
				treeObj.expandAll(true);
				/*默认设置第一个types为选中状态  初始化完成之后在执行 此处为异步请求*/
				$(".types:first").trigger("click");
			}); 
			/*点击提醒类型按钮*/
			$(".types").click(function(){
				$(".types").removeClass("active");
				$(this).addClass("active");
				var id=$(this).attr("data-id");
				var receive_type=$(this).attr("data-receive");
				if(receive_type==2){
					setTree();
					findUser(id);
				}else{
					setTree();
					$("input[name='receive_type']").removeAttr("checked");
					$("input[name='receive_type'][value='1']").prop("checked",true);
					$("#user_box").hide();
				}
			});
			//切换提醒接收类型
			$("input[name='receive_type']").change(function(){
				var receive_type=$("input[name='receive_type']:checked").val();
				if(receive_type==2){
					$("#user_box").show();
				}else{
					$("#user_box").hide();
				}
			});
			//提交设置
			$("#save").click(function(){
				var receive_type=$("input[name='receive_type']:checked").val();
				var type_id=$(".types.active").attr("data-id");
				var user_ids =[];//接收人
				var check=true;
				if(receive_type==2){
					var treeObj = $.fn.zTree.getZTreeObj("role_tree");
					var nodes = treeObj.getCheckedNodes(true);
					if(nodes.length == 0){
						layer.msg('请选择通知接收人！', function(){});
						check=false;	
					} else {
						$.each(nodes,function(i,val){
							if(val.isuser==1){//是否选中
								user_ids.push(val.id);
							}
						});
						if(user_ids==''){
							layer.msg('请勾选群组下面的接收人！');
							check=false;	
						}
					}
				}
				if(check){
				$.ajax({
					url:"${root}/info/saveWarnSet",
					dataType:"json",
					type:"post",
					data:{users:user_ids,receive_type:receive_type,type_id:type_id},
					success:function(data){
						layer.alert(data.msg,{
							   /*  skin: 'layui-layer-lan'
							    , */closeBtn: 0
							    ,anim: 4 //动画类型
						},function(){
							if(data.status==0){
								window.location.reload();	
							}
						});
					}
				});
			}
			});
			     
		});
	</script>
</c:set>
 <tags:adminLayout pageCss="${pageCss }" pageJavascript="${pageJavascript }">
	<div class="callout callout-info">
      <h4>温馨提示!</h4>
      <p>各个消息提醒默认是全站提醒，若想只对一部分后台用户提醒，请在此页面进行设置</p>
      <p>在设置提醒时，若用户没有此提醒对应内容列表的权限，则接收不到提醒</p>
    </div>
  	<div class="box">
    	<div class="box-header with-border">
    	<ul class="nav nav-pills">
    	 	 <c:forEach var="item" items="${types }" varStatus="status">
    			<li class="types" role="presentation" data-id="${item.type_id }" data-receive="${item.receive_type }">
    				<a href="#">${item.type_name }</a>
    			</li>
    		</c:forEach>
  			<!-- <li role="presentation" class="active"><a href="#">操作日志提醒</a></li>
  			<li role="presentation"><a href="#">用户报名兼职提醒</a></li> -->
		</ul>
		<hr style="margin-top:10px;margin-bottom: 10px;"/>
		<div class="box-body">
        	<div class="box box-primary">
	            <div class="box-body" id="notice_type">
	              <div class="form-group">
	                <label for="recipient_user_id">设置提醒接收类型</label>
	                <div class="radio">
						<label><input type="radio" checked="checked" name="receive_type" value="1">全站接收&nbsp;&nbsp;</label> 
						<label><input type="radio"  name="receive_type" value="2">部分接收&nbsp;&nbsp;</label>
					</div>
	              </div>
	            </div>
			</div>
			<div id="user_box" class="box box-primary" style="display:none;">
	            <div class="box-body" id="receive_user">
	              <div class="form-group" style="max-height: 650px;overflow-y:scroll;">
	                <label for="recipient_user_id">设置提醒接收人</label>
	                <ul id="role_tree" class="ztree" style="margin-top:0; width:300px;"></ul>
	                <input type="hidden" value="" name="user_ids" id="user_ids" />
	              </div>
	            </div>
	        </div>
	        <div style="text-align: center;">
	        	<button class="btn btn-primary" id="save"><i class="fa fa-save"> 设置并保存</i></button>
	        </div>        
    	</div>
    </div> 
</tags:adminLayout>