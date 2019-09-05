<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="pageJavascript"%>
<!-- Bootstrap 3.3.6 -->
<script src="${root}/statics/bootstrap/js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="${root}/statics/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<script src="${root}/statics/AdminLTE/js/adminlte.min.js"></script>
<script src="${root}/statics/AdminLTE/js/demo.js"></script>
<!-- FastClick -->
<%-- <script src="${root}/statics/plugins/fastclick/fastclick.js"></script> --%>
<!-- AdminLTE App -->
<%-- <script src="${root}/statics/AdminLTE/js/app.min.js"></script> --%>
<!-- nice validator -->
<script type="text/javascript" src="${root }/statics/nice-validator-1.0.0/dist/jquery.validator.js"></script>
<script type="text/javascript" src="${root }/statics/nice-validator-1.0.0/dist/local/zh-CN.js"></script>
<!-- http://layer.layui.com/ -->
<script src="${root }/statics/layer-v2.4/layer/layer.js"></script>
<script src="${root }/statics/plugins/scroll2Top-master/jquery.scroll2Top.min.js"></script>
<script type="text/javascript">
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
	//初始化tooltip
	$("[data-toggle='tooltip']").tooltip();
	$("#to_the_top").scroll2Top();
});

var chars = ['0','1','2','3','4','5','6','7','8','9'];
function getRandomNum(n) {
     var res = "";
     for(var i = 0; i < n ; i ++) {
         var id = Math.ceil(Math.random()*9);
         res += chars[id];
     }
     return Date.parse(new Date())/1000+res;
}

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
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "H+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


function wrapKit(txt,len){
	if(undefined != txt && ""!=txt){
		var rowNum = parseInt(txt.length / len);
		var isInteger = txt.length % len;
		rowNum = isInteger==0? rowNum:rowNum+1;
		var r="";
		for(var i=0; i<rowNum; i++){
			if(i==(rowNum-1)){
				r += txt.substring(i*len,txt.length);
			} else {
				r += txt.substring(i*len,(i+1)*len) + "<br>";
			}
		}
		return r;
	}
	return "";
}

function toDecimal(x) {
	if(null == x || '' == x || 'null' == x){
		return "——";
	}
	var f = parseFloat(x); 
    if (isNaN(f)) {
      return "——"; 
    } 
    f = x.toFixed(2); 
    return f; 
}

function  DateDiff(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
	if(null == sDate1 || null == sDate2){
		return "——";
	}
    var  aDate,  oDate1,  oDate2,  iDays  
    aDate  =  sDate1.split("-")  
    oDate1  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0])    //转换为12-18-2006格式  
    aDate  =  sDate2.split("-")  
    oDate2  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0])  
    iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24)    //把相差的毫秒数转换为天数  
    return  iDays  
}

function openImg(url){
	layer.open({
	  type: 1,
	  title: false,
	  closeBtn: 1,
	  skin: 'layui-layer-nobg', //没有背景色
	  shadeClose: false,
      offset: ['100px','350px'],
	  content: "<img src='${root}"+url+"' width='100%'/>"
	});
}

function delFile(obj){
	$(obj).parent().parent().remove();
}

function delImg(obj, attachId){
	$.ajax({
		url: "${root}/uploadFile/deleteImg",
		data: {attachId: attachId},
		type: "POST",
		success: function(data){
			if(data.status=='y'){
				layer.msg(data.msg,{icon:1});
			}else{
				layer.alert('删除失败！', {icon:2});
			}
		}
 	});
	$(obj).parent().parent().remove();
}
</script>
${pageJavascript}