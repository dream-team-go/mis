$(function(){

    var loopImg = function(){
    	if($(".loop-img").length !== 0){
	        Qfast(false, 'widgets', function () {
	            K.tabs({
	                id: 'fsD1',   //焦点图包裹id
	                conId: "D1pic1",  //** 大图域包裹id
	                tabId:"D1fBt",
	                tabTn:"a",
	                conCn: '.fcon', //** 大图域配置class
	                auto: 0,   //自动播放 1或0
	                effect: 'fade',   //效果配置
	                eType: 'click', //** 鼠标事件
	                pageBt:true,//是否有按钮切换页码
	                bns: ['.prev', '.next'],//** 前后按钮配置class
	                interval: 3000  //** 停顿时间
	            })
	        });
    	}
    };

    loopImg();
});