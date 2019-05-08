var positions = ['top_IO', 'left_IO1', 'left_IO2', 'left_IO3', 'left_IO4', 'left_IO5', 'bottom_IO', 'right_IO5', 'right_IO4', 'right_IO3', 'right_IO2', 'right_IO1'];

$(function () {
    getTime();

    $('.exit_icon').click(function () {
        window.location.href = "/logout"
    })

    getResourcesTop();
    getResourcesBottom();

    setInterval("getTime();", 1000);
    setInterval("getResourcesTop();", 1000 * 60);
    setInterval("getResourcesBottom();", 1000 * 60);
})

function getResourcesTop() {
    $.ajax({
        url: '/getResourcesTop',
        type: 'get',
        success: function (data) {
            var pcStatus = data.pcStatus;
            if (pcStatus) {
                var resourcestop = "";
                for (var i = 0; i < pcStatus.length; i++) {
                    var name = pcStatus[i].name;
                    var desc = pcStatus[i].desc;
                    var memoryUsage = pcStatus[i].memoryUsage;
                    var cpuUsage = pcStatus[i].cpuUsage;
                    var cpucolor = "#1774ef";
                    var cpuclass = "";
                    if(cpuUsage && cpuUsage.length >=3 && Number(cpuUsage.substr(0,1)) >=9 ){
                        cpucolor = "#fe3678";
                        cpuclass = "use_serious_circle";
                    }
                    var diskUsage = pcStatus[i].diskUsage;
                    var diskUsageDesc = pcStatus[i].diskUsageDesc;
                    var memoryUsageDesc = pcStatus[i].memoryUsageDesc;
                    resourcestop += "<tr>\n" +
                        "                        <td width=\"5.8%\" class=\"center\">"+(i+1)+"</td>\n" +
                        "                        <td width=\"8.6%\">"+name+"</td>\n" +
                        "                        <td width=\"25%\">"+desc+"</td>\n" +
                        "                        <td width=\"18.4%\" class=\"progress_bar\">\n" +
                        "                            <div class=\"total_num\">\n" +
                        "                                <div class=\"use_condition\"></div>\n" +
                        "                            </div>\n" +
                        "                            <span class=\"use_percent\">"+memoryUsage+"</span>\n" +
                        "                            <span class=\"use_num\">"+memoryUsageDesc+"</span>\n" +
                        "                        </td>\n" +
                        "                        <td class=\"center rotate_circle "+cpuclass+"\">\n" +
                        "                            <svg width=\"42\" height=\"42\">\n" +
                        "                                <circle cx=\"21\" cy=\"21\" r=\"18\" stroke-width=\"3\" stroke=\"#f5f5f5\" fill=\"none\"></circle>\n" +
                        "                                <circle cx=\"21\" cy=\"21\" r=\"18\" stroke-width=\"3\" stroke=\""+cpucolor+"\" fill=\"none\" transform=\"matrix(0,-1,1,0,0,42)\" stroke-dasharray=\"0 111\"></circle>\n" +
                        "                            </svg>\n" +
                        "                            <span>"+cpuUsage+"</span>\n" +
                        "                        </td>\n" +
                        "                        <td width=\"24.4%\" class=\"progress_bar\">\n" +
                        "                            <div class=\"total_num\">\n" +
                        "                                <div class=\"use_condition\"></div>\n" +
                        "                            </div>\n" +
                        "                            <span class=\"use_percent\">"+diskUsage+"</span>\n" +
                        "                            <span class=\"use_num\">"+diskUsageDesc+"</span>\n" +
                        "                        </td>\n" +
                        "                    </tr>";
                }
                if (resourcestop) {
                    $('#resourcestop').html(resourcestop);
                    makeline();
                    for(var k=0;k<$('svg').length;k++){
                        circle(2*k+1,k,'.rotate_circle span',18);
                    }
                }
            }
        }
    })
}


function getResourcesBottom() {
    $.ajax({
        url: '/getResourcesBottom',
        type: 'get',
        success: function (data) {
            var db = data.dbStatus;
            if(db){
                var dbStatus = "";
                for(var i =0 ; i<db.length; i++){
                    dbStatus += "<tr>\n" +
                        "                        <td width=\"11.3%\" class=\"center\">"+(i+1)+"</td>\n" +
                        "                        <td width=\"20.7%\">"+db[i].ip+"</td>\n" +
                        "                        <td>"+db[i].name+"</td>\n" +
                        "                        <td width=\"43.5%\" class=\"progress_bar\">\n" +
                        "                            <div class=\"total_num\">\n" +
                        "                                <div class=\"use_condition\"></div>\n" +
                        "                            </div>\n" +
                        "                            <span class=\"use_percent\">"+db[i].used+"</span>\n" +
                        "                            <span class=\"use_num\">"+db[i].usedsize+"/"+db[i].totalsize+"</span>\n" +
                        "                        </td>\n" +
                        "                    </tr>";
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