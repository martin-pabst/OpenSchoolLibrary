// JS file for library module


(function (app) {

    if (!app.actions['startCalendar']) {
        app.actions['startCalendar'] = [];
    }

    app.actions['startCalendar'].push({

        open: function (parameters) {

            var used = $("body").height() + 50;
            var calendarHeight = $(window).height() - used - 100;

            initializeCalendar(calendarHeight);

            initializeEventDetailsDialog();



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
            },
            eventClick: function(event){
                openEventDetailsDialog(event);
            }
        });
    }

    function toggle(button){
        $(button).parent().find('button').removeClass('fc-state-active');
        $(button).addClass('fc-state-active');
        $('#fullCalendar').fullCalendar('refetchEvents');
    }

    function openEventDetailsDialog(event){

        $.post('/calendar/fetchEventDetails',
            JSON.stringify({school_id: global_school_id,
                            school_term_id: global_school_term_id,
                            eventId: event.id}),
            function (data) {
                
                loadDetailValues(data);

                $('#eventDetailsDialog').modal();

                return true;

            }, "json");



    }



    function initializeEventDetailsDialog(){

        $('#eventDetailsDialog').on('shown.bs.modal', function (e) {

            $('#eventName').focus();

            $('#eventDetailsForm').validator();


            $('#eventDateFrom').datetimepicker({
                locale: 'de',
                calendarWeeks: true,
                format: 'DD.MM.YYYY'
            });

            $('#eventTimeFrom').timepicker({
                minuteStep: 5,
                showSeconds: false,
                showMeridian: false,
                defaultTime: false,
                explicitMode: true
            });

            $('#eventDateTo').datetimepicker({
                locale: 'de',
                calendarWeeks: true,
                format: 'DD.MM.YYYY'
            });

            $('#eventTimeTo').timepicker({
                minuteStep: 5,
                showSeconds: false,
                showMeridian: false,
                defaultTime: false,
                explicitMode: true
            });

            $("#eventDateFrom").on("dp.change", function (e) {
                $('#eventDateTo').data("DateTimePicker").minDate(e.date);
            });

            $("#eventDateTo").on("dp.change", function (e) {
                $('#eventDateFrom').data("DateTimePicker").maxDate(e.date);
            });

        });


        $('#eventAbsenceTab').on('shown.bs.tab', function () {


            $('#eventAbsence').find('label').on('focusin', (function (e) {

                e.stopImmediatePropagation();

            }));

            $('#eventAbsence').find('button').click(function (e) {

                var labels = $(this).siblings().find('label');

                var isSelected = $(labels[0]).hasClass('active');

                if (isSelected) {
                    labels.removeClass('active');
                } else {
                    labels.addClass('active');
                }

                e.stopImmediatePropagation();

            });

            $('#eventAbsencesWholeSchool').change(function () {
                if ($(this).is(':checked')) {
                    $('#eventAbsencesMatrix').hide();
                } else {
                    $('#eventAbsencesMatrix').show();
                }
            });


        });

        $('#calendarDetailsForm').validator().on('submit', function (e) {
            if (!e.isDefaultPrevented()) {
                saveEventDetailValues();
            }

        });

    }


    function buildAbsenceMatrix(absenceValues) {

        var html = '';

        for(var jgst in absenceValues){
            if(absenceValues.hasOwnProperty(jgst)){
                html += '<div style="margin-top: 0.5em">\n';
                html += '<button type="button" class="btn btn-primary btn-xs">' + jgst + '</button>\n';
                html += '<div class="btn-group" data-toggle="buttons"\n>';

                for(var className in absenceValues[jgst]){
                    if(absenceValues[jgst].hasOwnProperty(className)){

                        var active = absenceValues[jgst][className] ? ' active' : '';
                        html += '<label class="btn btn-xs btn-default class-button' + active + '"><input type="checkbox">' + className + '</label>';

                    }
                }


                html += '</div></div>';
            }
        }

        $('#eventAbsencesMatrix').html(html);

    }

    function loadDetailValues(eventData) {

        var event = eventData.event;

        $('#eventName').val(event.title);
        $('#eventNameShort').val(event.short_title);

        var from = moment(event.start);
        var to = moment(event.end);

        $('#eventDateFrom').data('DateTimePicker').date(from.format('DD.MM.Y'));
        $('#eventDateTo').data('DateTimePicker').date(to.format('DD.MM.Y'));

        var fromTime = '';
        var toTime = '';

        if(!event.allDay){
            fromTime = from.format('HH:mm');
            toTime = to.format('HH.mm');
        }

        $('#eventTimeFrom').timepicker('setTime', fromTime);
        $('#eventTimeTo').timepicker('setTime', toTime);

        $('#eventWholeDay').prop('checked', event.allDay);
        $('#eventLocation').val(event.location);
        $('#eventDescription').val(event.description);

        //todo: set options according to data.roleRestrictions == GetEventDetailsResponse.roleRestrictions
        $('#eventRestriction').find('option[value="3"]').prop('selected', true);

        $('#eventAbsencesWholeSchool').prop('checked', false);
        $('#eventAbsencesNoBigTests').prop('checked', false);
        $('#eventAbsencesNoSmallTests').prop('checked', true);

        var absenceValues = {

            '5': {
                '5a': true,
                '5b': false,
                '5c': true
            },
            '6': {
                '6a': false,
                '6b': true,
                '6c': false
            }

        };

        buildAbsenceMatrix(absenceValues);

    }

    function saveDetailValues(){

        var eventData = {};

        eventData['eventName'] = $('#eventName').val();
        eventData['shortName'] = $('#eventNameShort').val();
        eventData['dateFrom'] = $('#eventDateFrom').find('input').val();
        eventData['dateTo'] = $('#eventDateTo').find('input').val();

        eventData['timeFrom'] = $('#eventTimeFrom').val();
        eventData['timeTo'] = $('#eventTimeTo').val();

        eventData['wholeDay'] = $('#eventWholeDay').prop('checked');
        eventData['location'] = $('#eventLocation').val();
        eventData['description'] = $('#eventDescription').val();

        eventData['restriction'] = $('#eventRestriction').val();

        eventData['absenceWholeSchool'] = $('#eventAbsencesWholeSchool').prop('checked');
        eventData['absenceNoBigTests'] = $('#eventAbsencesNoBigTests').prop('checked');
        eventData['absenceNoSmallTests'] = $('#eventAbsencesNoSmallTests').prop('checked');

        var activeLabels = $('#eventAbsence').find('label.active.class-button');
        var inactiveLabels = $('#eventAbsence').find('label.class-button').not('.active');

        eventData['absencesSelectedClasses'] = [];
        eventData['absencesUnselectedClasses'] = [];

        for(var i = 0; i < activeLabels.length; i++){
            eventData['absencesSelectedClasses'].push($(activeLabels[i]).text());
        }

        for(var i = 0; i < inactiveLabels.length; i++){
            eventData['absencesUnselectedClasses'].push($(inactiveLabels[i]).text());
        }

        console.log(eventData);
    }




}(App));