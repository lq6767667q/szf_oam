var positions = ['top_IO', 'left_IO1', 'left_IO2', 'left_IO3', 'left_IO4', 'left_IO5', 'bottom_IO', 'right_IO5', 'right_IO4', 'right_IO3', 'right_IO2', 'right_IO1'];
var exip= '';

$(function () {
    getTime();

    $('.exit_icon').click(function () {
        window.location.href = "/logout"
    })

    getExDept();

    setInterval("getTime();", 1000);

    $.ajax({
        url: '/getExIp',
        type: 'get',
        success: function (data) {
            if(data){
                exip = data;
            }
        }
    })
})

function getExDept() {
    $.ajax({
        url: '/getExDept',
        type: 'get',
        data:{
            deptid :   1
        },
        success: function (data) {
            if(data){
                var depts = "";
                var firstdeptid = "";
                for(var i =0 ;i<data.length ; i++){
                    var dept_name = data[i].dept_name;
                    var pk = data[i].pk;
                    if(i == 0){
                        firstdeptid = pk;
                    }
                    depts += "<li class='deptli' onclick='clickonedept(this)' deptid='"+pk+"' py='"+Pinyin.GetJP(dept_name)+"'><div class=\"role_title\">"+dept_name+"</div></li>";
                }
                if(depts){
                    $('#depts').html(depts);
                    if(firstdeptid){
                        getExDeptLevel2(firstdeptid);
                        $('.deptli').eq(0).addClass('active');
                    }
                }
            }
        }
    })
}

function clickonedept(_this) {
    $('.deptli').removeClass('active');
    $(_this).addClass('active');
    getExDeptLevel2($(_this).attr('deptid'));
}

function getExDeptLevel2(deptid) {
    $.ajax({
        url: '/getExDept',
        type: 'get',
        data:{
            deptid :   deptid
        },
        success: function (data) {
            if(data){
                var deptsLevel2 = "";
                var firstdeptidLevel2 = "";
                for(var i =0 ;i<data.length ; i++){
                    var dept_name = data[i].dept_name;
                    var pk = data[i].pk;
                    if(i == 0){
                        firstdeptidLevel2 = pk;
                    }
                    deptsLevel2 += " <li class='deptliLevel2'   deptid='"+pk+"' py='"+Pinyin.GetJP(dept_name)+"' onclick='clicklevel2dept(this)'><div>"+dept_name+"</div></li>";
                }
                if(deptsLevel2){
                    $('#deptsLevel2').html(deptsLevel2);
                    if(firstdeptidLevel2){
                        getExUser(firstdeptidLevel2);
                        $('.deptliLevel2').eq(0).addClass('active');
                    }
                }
            }
        }
    })
}

function clicklevel2dept(_this) {
    $('.deptliLevel2').removeClass('active');
    $(_this).addClass('active');
    getExUser($(_this).attr('deptid'));
}

function getExUser(deptid) {
    $.ajax({
        url: '/getExUser',
        data: {
            deptid : deptid
        },
        type: 'get',
        success: function (data) {
            if(data){
                var users = "";
                for(var i =0 ;i<data.length ; i++){
                    var user_name = data[i].user_name;
                    var loginid = data[i].LOGIN_ID;
                    var sn = data[i].CASN;
                    var pk = data[i].pk;
                    users += "<li onclick='gossologin(this)' sn='"+sn+"'><div>"+user_name+"</div></li>";
                }
                if(users){
                    $('#users').html(users);
                }
            }
        }
    })
}

function gossologin(_this) {
    var sn = $(_this).attr('sn');
    window.open("http://"+exip+"/oamlogin.html?oamsn="+sn);
}



function search() {
    var val = $('#searchinput').val().trim();
    if(val){
        var $li = $('.deptli');
        for(var i =0 ; $li && i<$li.length; i++){
            var py = $li.eq(i).attr('py');
            if(!py || py.indexOf(val) <0 ){
                $li.eq(i).hide();
            }
            else{
                $li.eq(i).show();
            }
        }
    }
    else{
        $('.deptli').show();
    }
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