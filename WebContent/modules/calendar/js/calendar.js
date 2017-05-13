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

    var calendarEntriesType = "schedule";

    function initializeCalendar(calendarHeight) {
        $('#fullCalendar').fullCalendar({
            height: calendarHeight,
            nowIndicator: true,
            customButtons: {
                schedule: {
                    text: 'Termine',
                    click: function(){
                        console.log(this);
                        calendarEntriesType = "schedule";
                        toggle(this);
                    }
                },
                tests: {
                    text: 'Prüfungen',
                    click: function(){
                        calendarEntriesType = "tests";
                        toggle(this);
                    }
                },
                absences: {
                    text: 'Abwesende Klassen',
                    click: function(){
                        calendarEntriesType = "absences";
                        toggle(this);
                    }
                }
            },
            header:{
                right: 'schedule,tests,absences today month,agendaDay,agendaWeek prev,next listWeek,listMonth,listYear'
            },
            events: {
                url: '/calendar/fetchEntries',
                type: 'POST',
                data: function(){
                    return {
                        school_id: global_school_id,
                        type: calendarEntriesType
                    }
                },
                error: function() {
                    alert('there was an error while fetching events!');
                },
                color: 'yellow',   // a non-ajax option
                textColor: 'black' // a non-ajax option
            }
        });
    }

    function toggle(button){
        $(button).parent().find('button').removeClass('fc-state-active');
        $(button).addClass('fc-state-active');
        $('#fullCalendar').fullCalendar('refetchEvents');
    }

}(App));