$(function(){
    $("input[type='text']").focus(function(){
        $(this).val("");
    });

    $(".comment .textarea").on("click", function(){
    	if($(this).text() !== "请输入评论内容……"){
    		$(this).text("");
    	}
    });

    $("textarea").focus(function(){
        $(this).val("");
    });

    $(window).scroll(function(){
        var h=$(this).scrollTop();
        if(h>180){
            $(".return-top-ico").fadeIn();
        }else{
            $(".return-top-ico").fadeOut();
        }
    });

    $(".type-list-ico").on("click", function(){
        $(".notice-filter").is(":visible") ? $(".notice-filter").slideUp("slow") : $(".notice-filter").slideDown("slow");
    });

    $(".solid-comment").on("click", function(){
        $(this).hasClass("solid-comment-ico") ? $(this).removeClass("solid-comment-ico").addClass("solid-commented-ico") : $(this).removeClass("solid-commented-ico").addClass("solid-comment-ico")
    });

    $(".solid-comment1").on("click", function(){
        $(this).hasClass("solid-comment1-ico") ? $(this).removeClass("solid-comment1-ico").addClass("solid-commented1-ico") : $(this).removeClass("solid-commented1-ico").addClass("solid-comment1-ico")
    });

    $(".study-type-ico").on("click", function(){
        $(".study-type").is(":visible") ? $(".study-type").slideUp("slow") : $(".study-type").slideDown("slow");
    });
    var imgShow = function(){
    	if($(".loop-img img").length > 0){
    		$(".loop-img img").on("click", function(){
    			$(".marking").show().css("height",  "850px");
    			$('html,body').animate({scrollTop: '0px'}, 200);
    			$(".marking img").attr("src", $(this).attr("src"));
    		});
    	}else{
    		$("img[alt]").on("click", function(){
    			$(".marking").show().css("height",  "850px");;
    			$('html,body').animate({scrollTop: '0px'}, 200);
    			$(".marking img").attr("src", $(this).attr("src"));
    		});
    	}
    	
    	$(".close").on("click", function(){
    		$(".marking").hide();
    	});
    };
    
    imgShow();
});
