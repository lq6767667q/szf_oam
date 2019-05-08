var positions = ['top_IO', 'left_IO1', 'left_IO2', 'left_IO3', 'left_IO4', 'left_IO5', 'bottom_IO', 'right_IO5', 'right_IO4', 'right_IO3', 'right_IO2', 'right_IO1'];

$(function () {
    getTime();

    $('.exit_icon').click(function () {
        window.location.href = "/logout"
    })

    getInterfaces();

    setInterval("getTime();", 1000);
    setInterval("getInterfaces();", 1000 * 60);
})


function getInterfaces() {
    $.ajax({
        url: '/getInterfaces',
        type: 'get',
        success: function (data) {
            var servicesStatus = data.servicesStatus;
            if(servicesStatus){
                var main = "";
                for(var i =0 ; i<servicesStatus.length; i++){
                    var name = servicesStatus[i].name;
                    var desc = servicesStatus[i].desc;
                    var tong = "";
                    var tongclass = "";
                    if(servicesStatus[i].res && servicesStatus[i].res != 'yes'){
                        tong = "异常，无法访问";
                        tongclass = "IO_error";
                    }
                    else if(servicesStatus[i].res && servicesStatus[i].res == 'yes'){
                        tong = "正常";
                    }
                    main += "<li>\n" +
                        "                <div class=\"IO_main "+tongclass+"\">\n" +
                        "                    <div class=\"IO_title\">"+name+"</div>\n" +
                        "                    <div class=\"IO_infor\">\n" +
                        "                        <p>接口状态：<span class=\"IO_status\">"+tong+"</span></p>\n" +
                        "                        <p>"+desc+"</p>\n" +
                        "                    </div>\n" +
                        "                </div>\n" +
                        "            </li>";
                }
                if(main){
                    $('#main').html(main);
                }
            }
        }
    })
}

//取得系统当前时间
function getTime() {
    var myDate = new Date();
    var year = myDate.getFullYear();
    var month = myDate.getMonth()+1;
    var day = myDate.getDate();
    var weekday=myDate.getDay();
    var weekdayCh=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
    var hours = myDate.getHours();
    var minutes = myDate.getMinutes();
    var seconds = myDate.getSeconds();
    if (day < 10) {
        day = "0" + day;
    }

    if (hours<10)
    {
        hours = "0" + hours;
    }

    if (minutes < 10) {
        minutes = "0" + minutes;
    }

    $("#nowdate").html("(" + year + "年" + month + "月" + day + "日" + " " + weekdayCh[weekday] + " " + hours + ":" + minutes + ":" + seconds + ")");
}



function goto(name) {
    window.location.href = "/"+name;
}