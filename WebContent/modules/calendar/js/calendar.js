// JS file for library module


(function (app) {

    var selectedStudents = []; // students to merge

    if (!app.actions['startCalendar']) {
        app.actions['startCalendar'] = [];
    }

    app.actions['startCalendar'].push({

        open: function (parameters) {

            var used = $("body").height() + 50;
            var calendarHeight = $(window).height() - used - 100;

            initializeCalendar(calendarHeight);



        },
        close: function () {
            // w2ui['librarySettingsMergeStudents'].destroy();
        }
    });

    function initializeCalendar(calendarHeight) {
        $('#fullCalendar').fullCalendar({
            height: calendarHeight,
            header:{
                right: 'today month,agendaDay,agendaWeek prev,next listMonth'
            }
        });
    }


}(App));