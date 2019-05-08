$(function(){
    funcList('.plat_list li');
    funcList('.role_list li');
    funcList('.role_center li');
	placeHolder();
	addScroll();
    makeline();
})

function makeline() {
    for(var j=0;j<$('.use_percent').length;j++){
        line('.use_percent','.use_condition',j);
    }
    for(var n=0;n<$('.data_condition_num').length;n++){
        line('.data_condition_num','.use_condition',n);
    }
}

// tab切换
function funcList(className1){
	$(className1).click(function(){
		$(className1).removeClass('active');
		$(this).addClass('active');
	})
}

//输入框
function placeHolder() {
    if($("input").length>0){
        $('input').placeholder();
    }
    if($("textarea").length>0){
        $('textarea').placeholder();
    }
}

//增加滚动条
function addScroll(){
    if($(".addScroll").length > 0){
        $(".addScroll").mCustomScrollbar();
    }
}
// 圆弧
function circle(index1,index2,class1,r){
    // 分数的百分比
    var score = parseFloat($(class1).eq(index2).html())/100;       
    // console.log($(class1).length);
    var i = 1;
    var perimeter = Math.PI * 2 * r;
    var circle = document.querySelectorAll("circle")[index1];
    // 每一次转动的弧度
    var step = score*perimeter/100;
    var tc = setInterval(function(){
        circle.setAttribute('stroke-dasharray', step*i + " " + perimeter);
        i++;
        // console.log(i);
        if(i>100){
            clearInterval(tc);
        }
    },20)
}
   
// 直线进度条
function line(className1,className2,count){
    var width = parseFloat($(className1).eq(count).html()),
        step = width/100,
        i = 1;

    if(width>80){
        $(className1).eq(count).parent().addClass('use_serious');
    }

    var tc = setInterval(function(){
        $(className1).eq(count).prev().children(className2).css('width',step*i+'%');
        $(className1).eq(count).prev().children().children(className2).css('width',step*i+'%');
        i++;
        if(i>100){
            clearInterval(tc);
        }
    },1);
}

    