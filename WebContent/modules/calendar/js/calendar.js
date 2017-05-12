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
            customButtons: {
                schedule: {
                    text: 'Termine',
                    click: function(){
                        console.log(this);
                        toggle(this);
                    }
                },
                tests: {
                    text: 'Prüfungen',
                    click: function(){
                        toggle(this);
                    }
                },
                absences: {
                    text: 'Abwesende Klassen',
                    click: function(){
                        toggle(this);
                    }
                }
            },
            header:{
                right: 'schedule,tests,absences today month,agendaDay,agendaWeek prev,next listWeek,listMonth,listYear'
            }
        });
    }

    function toggle(button){
        $(button).parent().find('button').removeClass('fc-state-active');
        $(button).addClass('fc-state-active');
    }

}(App));