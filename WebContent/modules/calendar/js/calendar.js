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
    var detailsDialogFullcalendarEvent = null;

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

        detailsDialogFullcalendarEvent = event;

        $.post('/calendar/fetchEventDetails',
            JSON.stringify({school_id: global_school_id,
                            school_term_id: global_school_term_id,
                            event_id: event.id}),
            function (data) {
                
                loadDetailValues(data);

                $('#eventDetailsDialog').modal();

                return true;

            }, "json");



    }



    function initializeEventDetailsDialog(){


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



        $('#eventDetailsDialog').on('shown.bs.modal', function (e) {

            $('#eventName').focus();

            $('#eventDetailsForm').validator();

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
                saveDetailValues();
            }

        });

    }


    function buildAbsenceMatrix(absenceValues) {

        var html = '';

        for(var i = 0; i < absenceValues.length; i++){
            var form = absenceValues[i];
            html += '<div style="margin-top: 0.5em">\n';
            html += '<button type="button" class="btn btn-primary btn-xs">' + form.form_name + '</button>\n';
            html += '<div class="btn-group" data-toggle="buttons"\n>';

                for(var j = 0; j < form.classEntries.length; j++){

                    var classEntry = form.classEntries[j];

                    var active = classEntry.is_absent ? ' active' : '';
                    html += '<label class="btn btn-xs btn-default class-button' + active + '"><input type="checkbox">' + classEntry.class_name + '</label>';

                }


                html += '</div></div>';
        }


        $('#eventAbsencesMatrix').html(html);

    }

    function loadDetailValues(eventData) {

        var event = eventData.event;

        $('#eventAbsencesWholeSchool').prop('checked', eventData.absenceWholeSchool);

        $('#eventName').val(event.title);
        $('#eventNameShort').val(event.short_title);

        var from = moment(event.start, "DD.MM.YYYY");
        var to = moment(event.end, "DD.MM.YYYY");

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

        var html = '<option value="-1" style="color: green; font-weight: bold">Alle (öffentlich)</option>';
        var selectedValues = [];

        for(var i = 0; i < eventData.roleRestrictions.length; i++){

            var restriction = eventData.roleRestrictions[i];

            html += '<option value="' + restriction.id + '">' + restriction.name + '</option>';

            if(restriction.isRestricted){
                selectedValues.push[restriction.id];
            }

        }

        var eventRestrictionElement = $('#eventRestriction');
        eventRestrictionElement.html(html);

        eventRestrictionElement.selectpicker('val', selectedValues);
        eventRestrictionElement.selectpicker('refresh');

        buildAbsenceMatrix(eventData.formEntries);

    }

    function saveDetailValues(){

        var eventData = {
            id: null
        }

        if(detailsDialogFullcalendarEvent !== null){
            eventData.id = detailsDialogFullcalendarEvent.id;
        }
        
        var fcEventStart;
        var fcEventEnd;

        eventData.title = $('#eventName').val();
        eventData.short_title = $('#eventNameShort').val();

        eventData.allDay = $('#eventWholeDay').prop('checked');

        eventData.start = $('#eventDateFrom').find('input').val();
        fcEventStart = $.fullCalendar.moment.parse(eventData.start, "DD.MM.YYYY");
        fcEventStart.stripTime();

        eventData.end = $('#eventDateTo').find('input').val();
        fcEventEnd = $.fullCalendar.moment.parse(eventData.end, "DD.MM.YYYY");
        fcEventEnd.stripTime();

        if(!eventData.allDay){
            eventData.start += " " + $('#eventTimeFrom').val();
            fcEventStart = $.fullCalendar.moment.parse(eventData.start, "DD.MM.YYYY hh:mm");
            eventData.end += " " + $('#eventTimeTo').val();
            fcEventEnd = $.fullCalendar.moment.parse(eventData.end, "DD.MM.YYYY hh:mm");
        }


        eventData.location = $('#eventLocation').val();
        eventData.description = $('#eventDescription').val();

        eventData['restrictionIndices'] = $('#eventRestriction').selectpicker('val');

        eventData['absenceWholeSchool'] = $('#eventAbsencesWholeSchool').prop('checked');
        eventData['absenceNoBigTests'] = $('#eventAbsencesNoBigTests').prop('checked');
        eventData['absenceNoSmallTests'] = $('#eventAbsencesNoSmallTests').prop('checked');

        var activeLabels = $('#eventAbsence').find('label.active.class-button');

        eventData['absencesSelectedClasses'] = [];

        for(var i = 0; i < activeLabels.length; i++){
            eventData['absencesSelectedClasses'].push($(activeLabels[i]).text());
        }

        $.post('/calendar/setEventDetails',
            JSON.stringify({school_id: global_school_id,
                school_term_id: global_school_term_id,
                event_data: eventData}),
            function (data) {

                if(data.status === "success"){

                    if(data.isCreateEvent) {

                        // Todo

                    } else {

                        detailsDialogFullcalendarEvent.title = eventData.title;
                        detailsDialogFullcalendarEvent.start = eventData.start;
                        detailsDialogFullcalendarEvent.end = eventData.end;
                        $('#fullCalendar').fullCalendar('updateEvent', detailsDialogFullcalendarEvent);

                    }
                } else {
                    w2alert(data.message);
                }

                return true;

            }, "json");


        // console.log(eventData);

    }




}(App));