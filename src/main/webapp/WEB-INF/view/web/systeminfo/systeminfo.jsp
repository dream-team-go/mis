<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-------注意放置在tags:adminlayout的上面 ------->
<c:set var="pageJavascript">
	<script type="text/javascript">
	$(function(){
		$("#systeminfo_from").validator({
		    valid: function(form){
		        var me = this;
		        // 提交表单之前，hold住表单，并且在以后每次hold住时执行回调
		        me.holdSubmit(function(){
		            layer.msg("正在处理中...");
		        });
		        $.ajax({
		            url: "${root }/systeminfo/save",
		            data: $(form).serialize(),
		            type: "POST",
		            success: function(data){
		                // 提交表单成功后，释放hold，就可以再次提交
		                me.holdSubmit(false);
		                layer.msg(data.msg);
		            }
		        });
		    }
		});
	});
	</script>
</c:set>
<tags:adminLayout pageCss="${pageCss }" pageJavascript="${pageJavascript }" funcName="系统设置">
	<div class="callout callout-info">
      <h4>温馨提示!</h4>
      <p>您可以在此页面配置系统的基本参数，保存后需重新登录系统方可生效。</p>
    </div>
    <!-- Horizontal Form -->
    <div class="box box-info">
      <div class="box-header with-border">
        <h3 class="box-title">参数配置</h3>
      </div>
      <!-- /.box-header -->
      <!-- form start -->
      <form id="systeminfo_from" class="form-horizontal" method="post" data-validator-option="{stopOnError:true,focusCleanup:true,theme: 'yellow_top'}">
        <div class="box-body">
        <div class="form-group">
            <label for="systemTitle" class="col-sm-2 control-label">系统名称</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="systemTitle" value="${systemTitle }" id="systemTitle" placeholder="系统名称" data-rule="required;">
              <span class="help-block">作为系统的全局名称，会在登录和后台首页显示</span>
            </div>
          </div>
          <div class="form-group">
            <label for="systemTitleSmall" class="col-sm-2 control-label">系统名称简称</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="systemTitleSmall" value="${systemTitleSmall }" id="systemTitleSmall" placeholder="系统名称简称" data-rule="required;">
              <span class="help-block">作为后台菜单折叠后的缩略简称</span>
            </div>
          </div>
          <div class="form-group">
            <label for="defaultPassword" class="col-sm-2 control-label">登录默认密码</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="defaultPassword" value="${defaultPassword }" id="defaultPassword" placeholder="默认密码" data-rule="required;password">
              <span class="help-block">用作新增用户默认密码；重置用户登录密码</span>
            </div>
          </div>
          <div class="form-group">
            <label for="allRightReserved" class="col-sm-2 control-label">版权所有</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="allRightReserved" value="${allRightReserved }" id="allRightReserved" placeholder="系统版权信息">
            </div>
          </div>
          <div class="form-group">
            <label for="techSupport" class="col-sm-2 control-label">技术支持</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="techSupport" value="${techSupport }" id="techSupport" placeholder="技术支持">
            </div>
          </div>
          <%-- <div class="form-group">
            <label for="techSupportLink" class="col-sm-2 control-label">技术支持链接</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="techSupportLink" value="${techSupportLink }" id="techSupportLink" placeholder="技术支持链接">
            </div>
          </div> --%>
          <div class="form-group">
            <label for="contactPhone" class="col-sm-2 control-label">联系电话</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="contactPhone" value="${contactPhone }" id="contactPhone" placeholder="联系电话" data-rule="required;mobile;">
            </div>
          </div>
          <div class="form-group">
            <label for="systemVersion" class="col-sm-2 control-label">系统版本号</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="systemVersion" value="${systemVersion }" id="systemVersion" placeholder="系统版本号">
            </div>
          </div>
          </div>
        <!-- /.box-body -->
        <div class="box-footer" style="text-align:center">
          <button type="submit" class="btn btn-info">保&nbsp;存&nbsp;配&nbsp;置</button>
        </div>
        <!-- /.box-footer -->
      </form>
    </div>
</tags:adminLayout>
