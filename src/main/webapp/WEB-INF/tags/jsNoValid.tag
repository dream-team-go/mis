<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="pageJavascript"%>
<!-- jQuery 2.2.3 -->
<script src="${root}/statics/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="${root}/statics/bootstrap/js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="${root}/statics/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="${root}/statics/plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="${root}/statics/AdminLTE/js/app.min.js"></script>
<!-- http://layer.layui.com/ -->
<script src="${root }/statics/layer-v2.4/layer/layer.js"></script>
<script src="${root }/statics/plugins/scroll2Top-master/jquery.scroll2Top.min.js"></script>
<script type="text/javascript">
<!--
$(document).ajaxStart(onStart).ajaxComplete(onComplete).ajaxSuccess(onSuccess).ajaxError(ajaxError).ajaxStop(onStop);
function onStart(event) {
	layer.load(2);
}
function onComplete(event, xhr, settings) {
	layer.closeAll('loading');
}
function onSuccess(event, xhr, settings) {
	layer.closeAll('loading');
}
function onStop(event, xhr, settings) {
	layer.closeAll('loading');
}
function ajaxError(event, xhr, settings) {
	layer.closeAll('loading');
}
$('.sidebar-toggle').click(function(){
	$.ajax({
         url: "${root }/collapseStatus",
         data: {collapse:$("body").hasClass("sidebar-collapse")},
         type: "POST"
     });
});

$(function(){
	$("#to_the_top").scroll2Top();
});


function markAsRead(flag){
	$.ajax({
        url: "${root }/message/markAsRead2/"+flag,
        type: "POST",
        success: function(data){
        	layer.msg(data.msg,{icon: 6,time: 500},function(){
	        	location.reload();
        	}); 
        }
    });
}
//-->
</script>
${pageJavascript}