/* PCAS (Province City Area Selector 省、市、地区联动选择JS封装类)
	省市联动
		new PCAS("Province","City")
		new PCAS("Province","City","吉林省")
		new PCAS("Province","City","吉林省","吉林市")
	省市地区联动
		new PCAS("Province","City","Area")
		new PCAS("Province","City","Area","吉林省")
		new PCAS("Province","City","Area","吉林省","松原市")
		new PCAS("Province","City","Area","吉林省","松原市","宁江区")
	省、市、地区对象取得的值均为实际值。
	注：省、市、地区提示信息选项的值为""(空字符串)
***/
SPT = "--请选择省份--";
SCT = "--请选择城市--";
SAT = "--请选择地区--";
function PCAS() {
	this.SelP = document.getElementsByName(arguments[0])[0];
	this.SelC = document.getElementsByName(arguments[1])[0];
	this.SelA = document.getElementsByName(arguments[2])[0];
	this.DefPID="";
	this.DefP = this.SelA ? arguments[3] : arguments[2];
	this.DefCID="";
	this.DefC = this.SelA ? arguments[4] : arguments[3];
	this.DefAID="";
	this.DefA = this.SelA ? arguments[5] : arguments[4];
	this.SelP.PCA = this;
	this.SelC.PCA = this;
	that=this;
	this.SelP.onchange = function() {
		PCAS.SetC(this.PCA)
	};
	if (this.SelA) this.SelC.onchange = function() {
		PCAS.SetA(this.PCA)
	};
	$.ajax({
	 	url: webRoot+"pcas/getUserPcas",  
	 	type: 'POST',   
		data: {},    
	    success: function (data) {  
	    	if(data.province_id!=null && data.province_id!=""){
	    		that.DefPID=data.province_id;
	    	}
	    	if(data.city_id!=null && data.city_id!=""){
	    		that.DefCID=data.city_id;
	    	}
	    	if(data.area_id!=null && data.area_id!=""){
	    		that.DefAID=data.area_id;
	    	}
	    } 
	});	
	PCAS.SetP(this)
};

PCAS.SetP = function(PCA) {
	$.ajax({
	 	url: webRoot+"pcas/getPcas",  
	    type: 'POST',   
	    data: {
	    	key:PCA.DefP,
	    	level:1
	    },  
	    success: function (data) {  
	    	var firstP="";
	    	for(var i=0;i<data.length;i++){
	    		if(i==0) firstP=data[i].name;
	    		PCA.SelP.options.add(new Option(data[i].name, data[i].id));
	    		if(PCA.DefPID!=""){
	    			if(PCA.DefPID==data[i].id) PCA.SelP[i].selected = true;
	    		}else{
	    			if (PCA.DefP == data[i].name) PCA.SelP[i].selected = true;
	    		}  
	    	}	
	    	if(PCA.DefP==""){
	    		PCA.DefP=firstP;
	    	}
	    	PCAS.SetC(PCA);
	    		
	    },  
	    error: function (data) {  
	    	layer.msg(data.msg,{icon: 2}); 
	    }  
	});	
};

PCAS.SetC = function(PCA) {
	PI = PCA.SelP.selectedIndex;
	PID =PCA.SelP.options[PI].value; 
	PCA.SelC.length = 0;	
	$.ajax({
	 	url: webRoot+"pcas/getPcasChild",  
	    type: 'POST',   
	    data: {
	    	pid:PID,
	    	level:2
	    },  
	    success: function (data) {  
	    	var firstC="";
	    	for(var i=0;i<data.length;i++){
	    		if(i==0) firstC=data[i].name;
	    		PCA.SelC.options.add(new Option(data[i].name, data[i].id));
	    		if(PCA.DefCID!=""){
	    			if(PCA.DefCID==data[i].id) PCA.SelC[i].selected = true;
	    		}else{
	    			if (PCA.DefC == data[i].name) PCA.SelC[i].selected = true;
	    		}
	    	}	
	    	if(PCA.DefC==""){
	    		PCA.DefC=firstC;
	    	}
	    	PCAS.SetA(PCA);
	    },  
	    error: function (data) {  
	    	layer.msg(data.msg,{icon: 2}); 
	    }  
	});	
};

PCAS.SetA = function(PCA) {
	PI = PCA.SelP.selectedIndex;
	PID =PCA.SelP.options[PI].value; 
	CI = PCA.SelC.selectedIndex;
	CID =PCA.SelC.options[CI].value; 
	PCA.SelA.length = 0;
	
	$.ajax({
	 	url: webRoot+"pcas/getPcasChild",  
	    type: 'POST',   
	    data: {
	    	pid:CID,
	    	level:3
	    },  
	    success: function (data) {  
	    	for(var i=0;i<data.length;i++){
	    		PCA.SelA.options.add(new Option(data[i].name, data[i].id));
		    	if(PCA.DefAID!=""){
	    			if(PCA.DefAID==data[i].id) PCA.SelA[i].selected = true;
	    		}else{
	    			if (PCA.DefA == data[i].name) PCA.SelA[i].selected = true;
	    		}
	    	}	
	    },  
	    error: function (data) {  
	    	layer.msg(data.msg,{icon: 2}); 
	    }  
	});	
	
}

