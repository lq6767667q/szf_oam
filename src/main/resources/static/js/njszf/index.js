var positions = ['top_IO', 'left_IO1', 'left_IO2', 'left_IO3', 'left_IO4', 'left_IO5', 'bottom_IO', 'right_IO5', 'right_IO4', 'right_IO3', 'right_IO2', 'right_IO1'];

$(function () {
    getTime();

    $('.exit_icon').click(function () {
        window.location.href = "/logout"
    })

    getDataTopRight();
    getDataTopLeft();
    getDataBottomLeft();

    setInterval("getTime();", 1000);
    setInterval("getDataTopRight();", 1000 * 60);
    setInterval("getDataTopLeft();", 1000 * 60);
    setInterval("getDataBottomLeft();", 1000 * 60);
})


function getDataTopRight() {
    $.ajax({
        url: '/getDataTopRight',
        type: 'get',
        success: function (data) {
            var servicesStatus = data.servicesStatus;
            if(servicesStatus){
                var small_circle = "";
                var big_circle = "";
                for(var i =0 ; i<servicesStatus.length; i++){
                    var smallnoneclass = "";
                    var bignoneclass = "";
                    if(servicesStatus[i].res && servicesStatus[i].res != 'yes'){
                        smallnoneclass = "none";
                    }
                    else if(servicesStatus[i].res && servicesStatus[i].res == 'yes'){
                        bignoneclass = "none";
                    }
                    small_circle += "<div class=\""+positions[i]+" "+smallnoneclass+"\">"+servicesStatus[i].name+"</div>";
                    big_circle += "<div class=\""+positions[i]+" "+bignoneclass+"\">"+
                        servicesStatus[i].name+"<i class=\"IO_icon\"></i>"+
                        "</div>";
                }
                if(small_circle && big_circle){
                    $('.small_circle').html(small_circle);
                    $('.big_circle').html(big_circle);
                }
            }
        }
    })
}

function getDataTopLeft() {
    $.ajax({
        url: '/getDataTopLeft',
        type: 'get',
        success: function (data) {
            var pcStatus = data.pcStatus;
            if(pcStatus){
                var serve_main = "";
                for(var i =0 ; i<pcStatus.length; i++){
                    var cpuUsage = pcStatus[i].cpuUsage;
                    var cpucolor = "#1774ef";
                    var cpuclass = "";
                    if(cpuUsage && cpuUsage.length >=3 && Number(cpuUsage.substr(0,1)) >=9 ){
                        cpucolor = "#fe3678";
                        cpuclass = "use_serious_circle";
                    }
                    var diskUsage = pcStatus[i].diskUsage;
                    var diskcolor = "#1774ef";
                    var diskclass = "";
                    if(diskUsage && diskUsage.length >=3 && Number(diskUsage.substr(0,1)) >=9 ){
                        diskcolor = "#fe3678";
                        diskclass = "use_serious_circle";
                    }
                    var memoryUsage = pcStatus[i].memoryUsage;
                    var memorycolor = "#1774ef";
                    var memoryclass = "";
                    if(memoryUsage && memoryUsage.length >=3 && Number(memoryUsage.substr(0,1)) >=9 ){
                        memorycolor = "#fe3678";
                        memoryclass = "use_serious_circle";
                    }
                    var name = pcStatus[i].name;
                    serve_main += "<div class=\"address_title\">"+name+"</div>\n" +
                        "                    <ul class=\"each_serve_condition clearFix\">\n" +
                        "                        <li class=\"\"+memoryclass+\"\">\n" +
                        "                            <svg width=\"84\" height=\"84\">\n" +
                        "                                <circle cx=\"42\" cy=\"42\" r=\"40\" stroke-width=\"4\" stroke=\"#f5f5f5\" fill=\"none\"></circle>\n" +
                        "                                <circle cx=\"42\" cy=\"42\" r=\"40\" stroke-width=\"4\" stroke=\""+memorycolor+"\" fill=\"none\" transform=\"matrix(0,-1,1,0,0,84)\" stroke-dasharray=\"0 251\"></circle>\n" +
                        "                            </svg>\n" +
                        "                            <div class=\"serve_condition_infor\">\n" +
                        "                                <div class=\"serve_perc\">"+memoryUsage+"</div>\n" +
                        "                                <div>内存</div>\n" +
                        "                            </div>\n" +
                        "                        </li>\n" +
                        "                        <li class=\"\"+cpuclass+\"\">\n" +
                        "                            <svg width=\"84\" height=\"84\">\n" +
                        "                                <circle cx=\"42\" cy=\"42\" r=\"40\" stroke-width=\"4\" stroke=\"#f5f5f5\" fill=\"none\"></circle>\n" +
                        "                                <circle cx=\"42\" cy=\"42\" r=\"40\" stroke-width=\"4\" stroke=\""+cpucolor+"\" fill=\"none\" transform=\"matrix(0,-1,1,0,0,84)\" stroke-dasharray=\"0 251\"></circle>\n" +
                        "                            </svg>\n" +
                        "                            <div class=\"serve_condition_infor\">\n" +
                        "                                <div class=\"serve_perc\">"+cpuUsage+"</div>\n" +
                        "                                <div>CPU</div>\n" +
                        "                            </div>\n" +
                        "                        </li>\n" +
                        "                        <li class=\"\"+diskclass+\"\">\n" +
                        "                            <svg width=\"84\" height=\"84\">\n" +
                        "                                <circle cx=\"42\" cy=\"42\" r=\"40\" stroke-width=\"4\" stroke=\"#f5f5f5\" fill=\"none\"></circle>\n" +
                        "                                <circle cx=\"42\" cy=\"42\" r=\"40\" stroke-width=\"4\" stroke=\""+diskcolor+"\" fill=\"none\" transform=\"matrix(0,-1,1,0,0,84)\" stroke-dasharray=\"0 251\"></circle>\n" +
                        "                            </svg>\n" +
                        "                            <div class=\"serve_condition_infor\">\n" +
                        "                                <div class=\"serve_perc\">"+diskUsage+"</div>\n" +
                        "                                <div>磁盘</div>\n" +
                        "                            </div>\n" +
                        "                        </li>\n" +
                        "                    </ul>";
                }
                if(serve_main){
                    $('#pcstatus').html(serve_main);
                    for(var k=0;k<$('svg').length;k++){
                        circle(2*k+1,k,'.serve_perc',40);
                    }
                }
            }
        }
    })
}


function getDataBottomLeft() {
    $.ajax({
        url: '/getDataBottomLeft',
        type: 'get',
        success: function (data) {
            var db = data.dbStatus;
            if(db){
                var dbStatus = "";
                for(var i =0 ; i<db.length; i++){
                    dbStatus += " <li>\n" +
                        "                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                        "                                <tr>\n" +
                        "                                    <th colspan=\"3\">"+db[i].name+"</th>\n" +
                        "                                </tr>\n" +
                        "                                <tr>\n" +
                        "                                    <td width=\"34%\">表空间</td>\n" +
                        "                                    <td width=\"53%\" class=\"font0\">\n" +
                        "                                        <div class=\"total_num\">1800000\n" +
                        "                                            <div class=\"use_condition\"></div>\n" +
                        "                                        </div>\n" +
                        "                                    </td>\n" +
                        "                                    <td class=\"data_condition_num\">"+db[i].used+"</td>\n" +
                        "                                </tr>\n" +
                        "                            </table>\n" +
                        "                        </li>";
                }
                if(dbStatus){
                    $('#dbStatus').html(dbStatus);
                    makeline();
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