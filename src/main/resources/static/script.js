// Функция отображения поля для ввода номера телефона
function addpolevvoda(count, tel) {
    if (tel === undefined) {
        tel = "Нет в базе";
    }
    document.getElementById('polevvoda' + count).innerHTML = tel + '<input type="text" id="work_tel' + count + '" name="work_tel" value="" >  <input type="button" onclick="update(' + count + ')" value="Добавить номер"/>';
}

// Функция выводы результата по запросу
function viewresult(result, employee) {
    var itog = '<ul>';
    var count = 1;
    for (var n in result) {
        if (result[n].sex === "Ж") {
            var avatar = "../images/women.png";
        } else {
            var avatar = "../images/men.png";
        }
        if (employee !== "former") {
            if (result[n].workPhoneNumber == null || result[n].workPhoneNumber == '') {
                var numtel = '<font id="polevvoda' + count + '">Нет в базе   <input type="button" onclick="addpolevvoda(' + count + ')" value=" Добавить номер "/></font>';
            } else {
                var numtel = '<font id="polevvoda' + count + '">' + result[n].workPhoneNumber + '   <input type="button" onclick="addpolevvoda(' + count + ',\'' + result[n].workPhoneNumber + '\')" value=" Изменить номер "/></font>';
            }
        } else {
            if (result[n].workPhoneNumber == '' || result[n].workPhoneNumber == null) {
                var numtel = '<font id="polevvoda' + count + '">Нет в базе <input type="button" onclick="addpolevvoda(' + count + ')" value=" Добавить номер "/></font>';

            } else {
                var numtel = '<font id="polevvoda' + count + '">' + result[n].workPhoneNumber + ' <input type="button" onclick="addpolevvoda(' + count + ',\'' + result[n].workPhoneNumber + '\')" value=" Изменить номер "/></font>';
            }
        }
        // При выборе именинника открываем полную информацию
        if (result[n].todaybd == 1) {
            var openresult = "";
        } else {
            var openresult = "display:none;";
        }
        itog = itog + '<input type="hidden" name="id" id="iduser' + count + '" value="' + result[n].id + '"><div><a href="#open1" style="text-decoration:none" onclick="show(\'hidden_' + count + '\',250,100)">' + result[n].firstName + ' ' + result[n].name + ' ' + result[n].middleName + '</a></div><div id="hidden_' + count + '" style="' + openresult + 'width:auto;background-color:#f0f0f0"><img align="left" style="border:3px solid black;"  src="' + avatar + '" ><table style="position:relative; padding-left: 25px"><tr><td style="width:150px">День рождения:</td><td>' + result[n].birthday + '</td></tr><tr><td style="width:150px">Предприятие:</td><td>' + result[n].companyName + '</td></tr><tr><td style="width:150px">Подразделение:</td><td>' + result[n].divisionName + '</td></tr><tr><td style="width:150px">Должность:</td><td>' + result[n].postName + '</td></tr><tr><td style="width:150px">Табельный номер:</td><td>' + result[n].tabNum + '</td></tr><tr><td style="width:150px">Номер телефона:</td><td><font size="4" color="red" >' + numtel + '</font>  </td></tr></table> <br><div><label id="oldplacework' + count + '"></label></div><label id="buttonoldplacework' + count + '"><input style="position: relative; left : 50px" type="button" value="Предыдущее место  работы"  onClick="oldplacework(' + count + ');"></label></div>';
        count = count + 1;
    }
    document.getElementById('SlideMenu1').innerHTML = itog + '</ul>';
    document.location.replace('#top');
}

$(function () {
    // Вывод сотрудников по дате рождения из календаря
    $("#datepicker").datepicker({
        changeYear: true, changeMonth: true, showOtherMonths: true, selectOtherMonths: true,
        onSelect: function (dateText, inst) {
            dateText = $.datepicker.formatDate(
                'mm-dd',
                new Date(inst.selectedYear, inst.selectedMonth, inst.selectedDay)
            );
            var vvod = document.getElementById("firstname").value;
            var pred = document.getElementById("pred").value;
            var divisionname = document.getElementById("division").value;
            var namepost = document.getElementById("post").value;
            $.ajax({
                type: 'POST',
                url: "../search.php",
                data: {
                    firstname: vvod,
                    datebd: dateText,
                    namepred: pred,
                    divisionname: divisionname,
                    namepost: namepost
                },
                success: function (data) {
                    viewresult(data);
                }
            });
        }
    });

// Отображает именнинников на странице
    $.ajax({
        url: "../birthday.php",
        success: function (data) {
            var stroka = data;
            stroka = stroka.replace("[", "");
            stroka = stroka.replace("]", "");
            var itog = '<ul>';
            var count = 1;
            while (stroka.indexOf('}') !== -1) {
                var position = stroka.indexOf('}');
                var result = stroka.substring(0, position + 1);
                stroka = stroka.substring(position + 2);
                var event = JSON.parse(result);
                if (event.sex === "Ж") {
                    var avatar = "../images/women.png";
                } else {
                    var avatar = "../images/men.png";
                }
                if (event.wt !== null) {
                    var numtel = event.wt;
                } else {
                    var numtel = "Нет в базе";
                }

                var np = event.np;		//Название предприятия

                if (np_old !== event.np) {	//Если название предприятия предыдущего сотрудника не совпадает с эти названием, то добавляем это название в список
                    itog = itog + '<div style="font-weight: bold;">' + np + '</div><div><input type="hidden" name="id" id="iduserbd' + count + '" value="' + event.id + '"><a id="' + count + '"   onClick="birthdayview(this)">' + event.f + ' ' + event.n + ' ' + event.mn + '</a></div>';

                } else { //Ели не совпадает, то не добавляем
                    itog = itog + '<div><input type="hidden" name="id" id="iduserbd' + count + '" value="' + event.id + '"><a id="' + count + '"   onClick="birthdayview(this)">' + event.f + ' ' + event.n + ' ' + event.mn + '</a></div>';
                }
                var np_old = np;
                count = count + 1;
            }
            document.getElementById('birthday').innerHTML = itog + '</ul>';

        }
    });
});
// код для добавления функции TRIM() для строки, чтобы работало в IE8
if (typeof String.prototype.trim !== 'function') {
    String.prototype.trim = function () {
        return this.replace(/^\s+|\s+$/g, '');
    };
}

// Функция поиска
function poisk(employee) {
    var vvod = document.getElementById("firstname").value;
    vvod = vvod.trim();
    var pred = document.getElementById("pred").value;
    var divisionname = document.getElementById("division").value;
    var namepost = document.getElementById("post").value;
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: "search",
        data: {firstname: vvod, namepred: pred, divisionname: divisionname, namepost: namepost, employee: employee},
        success: function (data) {
            if (employee === "former") {
                viewresult(data, "former");
            } else {
                viewresult(data);
            }
        }
    });
}

function filtrpred() {
    var pred = document.getElementById("pred").value;
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: "division",
        data: {namepred: pred},
        success: function (result) {
            $("#division").empty();
            $('#division').append('<option value="">Все подразделения</option>');
            $("#post").empty();
            $('#post').append('<option value="">Все должности</option>');
            for (var n in result) {
                $('#division').append('<option value=\'' + result[n].name + '\'>' + result[n].name + ' (' + result[n].numberOfEmployees + ')</option>');
            }
        }
    });
}

function filtrpost() {
    var pred = document.getElementById("pred").value;
    var division = document.getElementById("division").value;
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: "post",
        data: {namepred: pred, divisionname: division},
        success: function (result) {
            $("#post").empty();
            $('#post').append('<option value="">Все должности</option>');
            for (var n in result) {
                $('#post').append('<option value=\'' + result[n].name + '\'>' + result[n].name + ' (' + result[n].numberOfEmployees + ')</option>');
            }
        }
    });
}


$(document).ready(function () {
    $(".SlideMenu1_Folder div").click(function () {
        if ($(this).parent().find('ul').is(':hidden')) {
            $(this).parent().find('ul').fadeIn();
        } else {
            $(this).parent().find('ul').fadeOut();
        }
    });
});

/*<![CDATA[*/
var s = [], s_timer = [];

function show(id, h, spd) {
    s[id] = s[id] == spd ? -spd : spd;
    s_timer[id] = setTimeout(function () {
        var obj = document.getElementById(id);
        if (obj.offsetHeight + s[id] >= h) {
//							obj.style.height=h+"px";
            obj.style.height = "auto";
            obj.style.overflow = "auto";
        } else if (obj.offsetHeight + s[id] <= 0) {
            obj.style.height = 0 + "px";
            obj.style.display = "none";
        } else {
            obj.style.height = (obj.offsetHeight + s[id]) + "px";
            obj.style.overflow = "hidden";
            obj.style.display = "block";
            setTimeout(arguments.callee, 10);
        }
    }, 10);
} /*]]>*/


// Функция просмора информации о именнинниках
function birthdayview(obj) {
    var count = obj.id;
    var id = document.getElementById("iduserbd" + count).value;

    $.ajax({
        type: 'POST',  ///тип запроса  GET либо POST
        url: "../search.php", //ваш контроллер или отдельный аякс контроллер
        data: {id: id, todaybd: 1},
        success: function (data) {
            viewresult(data);
        }
    });
}

// Функция добавления номера телефона в базу
function update(count) {
    var work_tel = document.getElementById("work_tel" + count).value.trim();


    if (work_tel.length > 0 && work_tel.indexOf('\'') === -1 && work_tel.indexOf('\"') === -1) {
        var id = document.getElementById("iduser" + count).value;

        $.ajax({
            type: 'POST',  ///тип запроса  GET либо POST
            url: "../teladdbase.php", //ваш контроллер или отдельный аякс контроллер
            data: {id: id, work_tel: work_tel},
            success: function (data) {   ///если ajax-запрос прошел удачно и сервер вернул код 200
                var stroka = data;
                alert("Добавлен номер телефона: " + data);
                document.getElementById('polevvoda' + count).innerHTML = data + '   <input type="button" onclick="addpolevvoda(' + count + ', \'' + data + '\')" value=" Изменить номер "/>';

            }
        });
    } else {
        alert("Некоректно введен номер телефона!");
    }
}

// Функция поиска предыдущих мест работы
function oldplacework(count) {
    var id = document.getElementById('iduser' + count).value;
    $.ajax({
        type: 'POST',
        url: "search_post.php",
        data: {id: id},
        success: function (data) {
            viewresultoldwork(data, count);
        }
    });
}

// Функция выводы предыдущих мест работы
function viewresultoldwork(data, count) {
    var stroka = data;
    stroka = stroka.replace("[", "");
    stroka = stroka.replace("]", "");
    var itog = '<table style="position:relative; padding-left: 25px; border-top: 1px solid #000; border-bottom: 1px solid #000; "><tr style="width:250px;"><td style="width:250px;">Предыдущее место работы:</td></tr><tr><td style="width:150px;border-bottom: 1px solid #000;">Предприятие:</td><td style="width:150px;border-bottom: 1px solid #000;">Подразделение:</td><td style="width:150px;border-bottom: 1px solid #000;">Должность:</td><td style="width:150px;border-bottom: 1px solid #000;">номер телефона:</td></tr>';
    while (stroka.indexOf('}') !== -1) {
        var position = stroka.indexOf('}');
        var result = stroka.substring(0, position + 1);
        stroka = stroka.substring(position + 2);
        var event = JSON.parse(result);
        namepred = event.namepred;
        divisionname = event.divisionname;
        namepost = event.namepost;
        work_tel = event.work_tel;
        /*
                    <table style="position:relative; padding-left: 25px"><tr><td style="width:150px">День рождения:</td><td>'+event.br+'</td></tr><tr><td style="width:150px">Предприятие:</td><td>'+event.np+'</td></tr><tr><td style="width:150px">Подразделение:</td><td>'+event.dn+'</td></tr><tr><td style="width:150px">Должность:</td><td>'+event.npost+'</td></tr><tr><td style="width:150px">Табельный номер:</td><td>'+event.tn+'</td></tr><tr><td style="width:150px">Номер телефона:</td><td><font size="4" color="red" >'+numtel+'</font>  </td></tr></table>
            */
        newline = '<tr><td style="width:150px">' + namepred + '</td><td style="width:150px">' + divisionname + '</td><td style="width:150px">' + namepost + '</td><td style="width:150px">' + work_tel + '</td></tr>';

        itog = itog + '' + newline;
        //itog = itog+' '+namepred+' | '+divisionname+' | '+namepost+' | номер телефона: '+work_tel+' <br>';
    }
    document.getElementById('oldplacework' + count).innerHTML = itog + '</table>';
    document.getElementById('buttonoldplacework' + count).innerHTML = '';
}

