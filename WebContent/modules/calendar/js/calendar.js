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

            fetchEmptyEvent();

            initializePlusButton();

            initializeDeleteButton();
        },
        close: function () {
            // w2ui['librarySettingsMergeStudents'].destroy();
        }
    });

    function initializePlusButton() {
        $('#calendarPlusButton').click(
            function () {
                openEventDetailsDialog(null, null);
            }
        )
    }

    function initializeDeleteButton() {

        $('#eventDeleteButton').confirmation({
            rootSelector: '#eventDeleteButton',
            title: 'Sind Sie sicher?',
            content: 'Sie sind dabei, einen Termin unwiderruflich zu <span style="font-weight: bold">löschen</span>.',
            html: true,
            singleton: true,
            popout: true,
            btnOkLabel: 'Ja',
            btnOkClass: 'btn-success',
            btnCancelLabel: 'Abbrechen',
            btnCancelClass: 'btn-danger',
            onConfirm: function () {
                $('#eventDetailsDialog').modal('hide');

                $.post('/calendar/removeEvent',
                    JSON.stringify({
                        school_id: global_school_id,
                        event_id: detailsDialogFullcalendarEvent.id
                    }),
                    function (data) {
                        if (data.status === 'success') {
                            $('#fullCalendar').fullCalendar('removeEvents', detailsDialogFullcalendarEvent.id);
                        } else {
                            w2alert('Der Termin konnte nicht gelöscht werden:\n' + data.message);
                        }

                        return true;

                    }, "json");
            }

        });

    }


    function fetchEmptyEvent() {
        $.post('/calendar/fetchEventDetails',
            JSON.stringify({
                school_id: global_school_id,
                school_term_id: global_school_term_id,
                event_id: null
            }),
            function (data) {

                emptyEvent = data;

                return true;

            }, "json");

    }


    var calendarEntriesType = "schedule";
    var detailsDialogFullcalendarEvent = null;

    var emptyEvent = null;

    function initializeCalendar(calendarHeight) {
        $('#fullCalendar').fullCalendar({
            height: calendarHeight,
            nowIndicator: true,
            weekNumbers: true,
            weekNumbersWithinDays: true,
            minTime: "05:00:00",
            maxTime: "22:00:00",
            weekFormat: "KW w",
            eventLimit: true,
            views: {
                month: {
                    timeFormat: 'H:mm',
                    eventLimit: 6
                }
            },
            customButtons: {
                schedule: {
                    text: 'Termine',
                    click: function () {
                        calendarEntriesType = "schedule";
                        toggle(this);
                    }
                },
                tests: {
                    text: 'Prüfungen',
                    click: function () {
                        calendarEntriesType = "tests";
                        toggle(this);
                    }
                }

            },
            header: {
                center: 'title',
                left: 'prevYear,nextYear',
                right: 'schedule,tests,absences today month,agendaWeek,agendaDay prev,next listWeek,listMonth,listYear'
            },
            events: {
                url: '/calendar/fetchEntries',
                type: 'POST',
                data: function () {
                    return {
                        school_id: global_school_id,
                        type: calendarEntriesType
                    }
                },
                error: function () {
                    alert('there was an error while fetching events!');
                }
            },
            eventDataTransform: function (eventData) {
                // Event object in Database is slightly different from fullcalendars event object format
                // so we have to transform ist after fetching from the server:
                if (eventData.backgroundRendering) {
                    eventData.rendering = 'background';
                }
                return eventData;
            },
            eventClick: function (event) {
                openEventDetailsDialog(event, null);
            },
            dayClick: function (date, jsEvent, view) {

                openEventDetailsDialog(null, date);

                /*
                 alert('Clicked on: ' + date.format());

                 alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);

                 alert('Current view: ' + view.name);

                 // change the day's background color just for fun
                 $(this).css('background-color', 'red');
                 */
            },
            eventDrop: function (event, delta, revertFunc) {

                moveEvent(event, revertFunc);

            },
            eventResize: function (event, delta, revertFunc) {

                moveEvent(event, revertFunc);

            },
            eventRender: function (event, element, view) {
                // console.log(element.html());

                var iconHtml = '<i class="fa fa-globe fullcalendar-icon" style="color: #0000a0"></i>';

                if (typeof event.restrictions === 'object' && event.restrictions.length > 0) {
                    iconHtml = '';
                }

                if (typeof event.absences === 'object' && event.absences.length > 0) {
                    var iconAbsence = '<i class="fa fa-external-link fullcalendar-icon" ></i>';

                    for (var i = 0; i < event.absences.length; i++) {
                        if (typeof event.absences[i].school_id !== 'undefined') {
                            iconAbsence = '<i class="fa fa-suitcase fullcalendar-icon" ></i>';
                            break;
                        }
                    }

                    iconHtml += iconAbsence;

                }

                if (event.preliminary) {
                    iconHtml += '<i class="fa fa-warning fullcalendar-icon" ></i>'
                }

                if (event.rendering == 'background') {

                    var html = element[0].outerHTML;

                    var backgroundColor = event.backgroundColor;

                    if (typeof backgroundColor === 'string') {

                        var brighterBackgroundColor = makeWhiter(event.backgroundColor, 0.2);
                        html = html.replace(backgroundColor, brighterBackgroundColor);
                    }

                    html = html.replace('fc-bgevent', '');


                    var html1 = $('<div style="color: #a9a9a9; position: absolute; bottom: 0; font-size: 130%; cursor: pointer; padding: 3px">'
                        + event.title + '<span style="margin-left: 1em; opacity: .3">' + iconHtml + '</span></div>');

                    html1.on('click', function () {
                        openEventDetailsDialog(event, null);
                    });

                    return $(html).append(html1);
                }


                if (iconHtml.length > 0) {
                    element.find('.fc-content').css('background-color', 'inherit');

                    $('<span class="fullcalendar-iconspan">' + iconHtml + '</span>').insertAfter(element.find('.fc-title'));
                }

                return element;
            }
        });
    }

    function makeWhiter(hex, percent) {
        // strip the leading # if it's there
        hex = hex.replace(/^\s*#|\s*$/g, '');

        // convert 3 char codes --> 6, e.g. `E0F` --> `EE00FF`
        if (hex.length == 3) {
            hex = hex.replace(/(.)/g, '$1$1');
        }

        var r = parseInt(hex.substr(0, 2), 16),
            g = parseInt(hex.substr(2, 2), 16),
            b = parseInt(hex.substr(4, 2), 16);

        r = increasePercent(r, percent);
        g = increasePercent(g, percent);
        b = increasePercent(b, percent);

        return '#' + r + g + b;
    }

    function increasePercent(c, percent) {
        c = Math.round(c * percent + 255 * (1 - percent));
        if (c > 255) {
            c = 255
        }
        c = c.toString(16);
        if (c.length < 2) {
            c = '0' + c;
        }
        return c;
    }


    function moveEvent(event, revertFunc) {

        var end = null;

        if (event.end !== null) {
            end = event.end.format('DD.MM.YYYY HH:mm');
        } else {
            if (!event.allDay) {
                var endMoment = $.fullCalendar.moment(event.start);
                endMoment.add(2, 'hours');
                event.end = endMoment;
                end = event.end.format('DD.MM.YYYY HH:mm');
            }
        }

        $.post('/calendar/moveEvent',
            JSON.stringify({
                start: event.start.format('DD.MM.YYYY HH:mm'),
                end: end,
                allDay: event.allDay,
                id: event.id,
                school_id: global_school_id
            }),
            function (data) {

                if (data.status !== "success") {

                    revertFunc();
                    w2alert(data.message);

                }

                return true;

            }, "json");

    }


    function toggle(button) {
        $(button).parent().find('button').removeClass('fc-state-active');
        $(button).addClass('fc-state-active');
        $('#fullCalendar').fullCalendar('refetchEvents');
    }

    /**
     * Open dialog to edit event details
     *  - click on existing event => event != null && date == null
     *  - click on empty timeslot => event == null && date != null
     * @param event
     * @param date
     */
    function openEventDetailsDialog(event, from) {

        detailsDialogFullcalendarEvent = event;

        if (event !== null) {

            $('#eventDetailsDialogLabel').html('Termin ändern');
            $('#eventDeleteButton').show();

            $.post('/calendar/fetchEventDetails',
                JSON.stringify({
                    school_id: global_school_id,
                    school_term_id: global_school_term_id,
                    event_id: event.id
                }),
                function (data) {

                    loadDetailValues(data);

                    $('#eventDetailsDialog').modal();

                    return true;

                }, "json");

        } else {

            $('#eventDetailsDialogLabel').html('Neuer Termin');
            $('#eventDeleteButton').hide();

            clearEventDetailDialogValues();
            detailsDialogFullcalendarEvent = null;

            if (from !== null) {

                /*
                 In code above we set to >= from and from <= to. As both have unknown values we have
                 to empty them before setting new values
                 */
                $('#eventDateFrom').data('DateTimePicker').clear();
                $('#eventDateTo').data('DateTimePicker').clear();

                $('#eventDateFrom').data('DateTimePicker').date(from.format('DD.MM.YYYY'));
                $('#eventDateTo').data('DateTimePicker').date(from.format('DD.MM.YYYY'));

                var fromTime = '';
                var toTime = '';

                if (from.hasTime()) {
                    fromTime = from.format('HH:mm');

                    var to = $.fullCalendar.moment(from);
                    to.add(30, 'minutes');

                    toTime = to.format('HH:mm');

                    $('#eventTimeFrom').timepicker('setTime', fromTime);
                    $('#eventTimeTo').timepicker('setTime', toTime);

                }

                $('#eventWholeDay').prop('checked', !from.hasTime());
                $('#eventWholeDay').trigger('change');

            }

            // Force validation
            $('#eventName').trigger('input');
            $('#eventDateFrom').trigger('input');
            $('#eventDateTo').trigger('input');

            $('#eventDetailsDialog').modal();

        }

    }


    function initializeEventDetailsDialog() {


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
            // $('#eventDateTo').data("DateTimePicker").minDate(e.date);
            var dateTo = $('#eventDateTo').data("DateTimePicker").date();

            if (e.date > dateTo) {
                $('#eventDateTo').data("DateTimePicker").date(e.date);
            }

        });

        $("#eventDateTo").on("dp.change", function (e) {
            // $('#eventDateFrom').data("DateTimePicker").maxDate(e.date);
            var dateFrom = $('#eventDateFrom').data("DateTimePicker").date();

            if (e.date < dateFrom) {
                $('#eventDateFrom').data("DateTimePicker").date(e.date);
            }
        });

        $('#eventcolor').simplecolorpicker({theme: 'fontawesome'});


        $('#eventDetailsDialog').on('shown.bs.modal', function (e) {

            $('#eventName').focus();

            $('#eventDetailsForm').validator();

            $('#eventRestriction').on('changed.bs.select', function (event, clickedIndex, newValue, oldValue) {

                var el = $('#eventRestriction');

                var values = el.selectpicker('val');

                if (clickedIndex === 0 && newValue === true) {
                    if (values.length > 1) {
                        el.selectpicker('val', [-1]);
                    }

                    return;

                }

                if (values.length === 0) {
                    el.selectpicker('val', [-1]);
                    return;
                }

                if (clickedIndex > 0 && newValue === true) {
                    if (values[0] === "-1") {
                        values.splice(0, 1);
                        el.selectpicker('val', values);
                    }

                }
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

        $('#eventDetailsForm').validator().on('submit', function (e) {
            if (!e.isDefaultPrevented()) {
                saveDetailValues();
                $('#eventDetailsDialog').modal('hide');
                e.preventDefault();
            }

        });

        $('#eventWholeDay').change(function () {

            if ($('#eventWholeDay').prop('checked')) {
                $('#eventTimeFrom').parent().hide();
                $('#eventTimeTo').parent().hide();
            } else {
                $('#eventTimeFrom').parent().show();
                $('#eventTimeTo').parent().show();
                $('#eventBackgroundRendering').prop('checked', false);
            }

        });

        $('#eventBackgroundRendering').change(function () {
            $('#eventWholeDay').prop('checked', true);
            $('#eventWholeDay').trigger('change');
        });


    }


    function buildAbsenceMatrix(absenceValues) {

        var html = '';

        for (var i = 0; i < absenceValues.length; i++) {
            var form = absenceValues[i];
            html += '<div style="margin-top: 0.5em">\n';
            html += '<button type="button" class="btn btn-primary btn-xs">' + form.form_name + '</button>\n';
            html += '<div class="btn-group" data-toggle="buttons"\n>';

            for (var j = 0; j < form.classEntries.length; j++) {

                var classEntry = form.classEntries[j];

                var active = classEntry.is_absent ? ' active' : '';
                html += '<label class="btn btn-xs btn-default class-button' + active + '" data-class-id="' + classEntry.class_id + '"><input type="checkbox">' + classEntry.class_name + '</label>';

            }


            html += '</div></div>';
        }


        $('#eventAbsencesMatrix').html(html);

    }

    function loadDetailValues(eventData) {

        var event = eventData.event;

        $('#eventAbsencesWholeSchool').prop('checked', eventData.absenceWholeSchool);

        $('#eventName').val(event.title).trigger('input');
        $('#eventNameShort').val(event.short_title);

        var from = moment(event.start, "DD.MM.YYYY HH:mm");
        var to = moment(event.end, "DD.MM.YYYY HH:mm");

        var fromTime = '';
        var toTime = '';

        if (!event.allDay) {
            fromTime = from.format('HH:mm');
            toTime = to.format('HH.mm');
        } else {
            to.subtract(1, 'days');
        }

        $('#eventDateFrom').data('DateTimePicker').date(from.format('DD.MM.YYYY'));
        $('#eventDateTo').data('DateTimePicker').date(to.format('DD.MM.YYYY'));


        $('#eventTimeFrom').timepicker('setTime', fromTime);
        $('#eventTimeTo').timepicker('setTime', toTime);

        $('#eventDateFrom').trigger('input');
        $('#eventDateTo').trigger('input');

        $('#eventWholeDay').prop('checked', event.allDay);
        $('#eventWholeDay').trigger('change');

        $('#eventLocation').val(event.location);
        $('#eventDescription').val(event.description);

        var html = '<option value="-1" data-content = "<span style=\'color: green; font-weight: bold\'>Alle (öffentlich)<i class=\'fa fa-globe fullcalendar-icon\' style=\'color: #0000a0\'></i></span>">Alle (öffentlich)</option>';
        html += '<option data-divider="true"></option>';

        var selectedValues = [];

        for (var i = 0; i < eventData.roleRestrictions.length; i++) {

            var restriction = eventData.roleRestrictions[i];

            html += '<option value="' + restriction.id + '">' + restriction.name + '</option>';

            if (restriction.isRestricted) {
                selectedValues.push(restriction.id);
            }

        }

        if (selectedValues.length === 0) {
            selectedValues.push(-1);
        }

        var eventRestrictionElement = $('#eventRestriction');
        eventRestrictionElement.html(html);

        eventRestrictionElement.selectpicker('val', selectedValues);
        eventRestrictionElement.selectpicker('refresh');

        buildAbsenceMatrix(eventData.formEntries);

        var color = eventData.event.backgroundColor;
        if (typeof color === "undefined") {
            color = "#4986e7";
        }

        $('#eventcolor').simplecolorpicker('selectColor', color);
        $('#eventBackgroundRendering').prop('checked', eventData.event.backgroundRendering);
        $('#eventPreliminary').prop('checked', eventData.event.preliminary);

    }

    function clearEventDetailDialogValues() {

        $('#eventAbsencesWholeSchool').prop('checked', false);

        $('#eventName').val('');
        $('#eventNameShort').val('');

        $('#eventDateFrom').data('DateTimePicker').clear();
        $('#eventDateTo').data('DateTimePicker').clear();

        $('#eventTimeFrom').timepicker('setTime', '');
        $('#eventTimeTo').timepicker('setTime', '');

        $('#eventWholeDay').prop('checked', false);
        $('#eventLocation').val('');
        $('#eventDescription').val('');

        var html = '<option value="-1" data-content = "<span style=\'color: green; font-weight: bold\'>Alle (öffentlich)<i class=\'fa fa-globe\' style=\'color: #0000a0; margin: 0 3px\'></i></span>">Alle (öffentlich)</option>';
        html += '<option data-divider="true"></option>';

        var selectedValues = [-1];

        for (var i = 0; i < emptyEvent.roleRestrictions.length; i++) {

            var restriction = emptyEvent.roleRestrictions[i];

            html += '<option value="' + restriction.id + '">' + restriction.name + '</option>';

        }

        var eventRestrictionElement = $('#eventRestriction');
        eventRestrictionElement.html(html);

        eventRestrictionElement.selectpicker('val', selectedValues);
        eventRestrictionElement.selectpicker('refresh');

        buildAbsenceMatrix(emptyEvent.formEntries);

        $('#eventBackgroundRendering').prop('checked', false);
        $('#eventPreliminary').prop('checked', false);

    }

    function saveDetailValues() {

        var eventData = {
            school_id: global_school_id,
            school_term_id: global_school_term_id,
            id: null
        };

        if (detailsDialogFullcalendarEvent !== null) {
            eventData.id = detailsDialogFullcalendarEvent.id;
        }

        var fcEventStart;
        var fcEventEnd;

        eventData.title = $('#eventName').val();
        eventData.short_title = $('#eventNameShort').val();

        eventData.allDay = $('#eventWholeDay').prop('checked');

        eventData.start = $('#eventDateFrom').find('input').val();
        fcEventStart = $.fullCalendar.moment(eventData.start, "DD.MM.YYYY");
        fcEventStart.stripTime();

        eventData.end = $('#eventDateTo').find('input').val();

        if (typeof eventData.end !== 'string' || eventData.end === '') {
            eventData.end = eventData.start;
        }

        fcEventEnd = $.fullCalendar.moment(eventData.end, "DD.MM.YYYY");
        fcEventEnd.stripTime();

        if (!eventData.allDay) {
            eventData.start += " " + $('#eventTimeFrom').val();
            fcEventStart = $.fullCalendar.moment(eventData.start, "DD.MM.YYYY HH:mm");

            var timeTo = $('#eventTimeTo').val();
            if (typeof timeTo !== 'string' || timeTo === '') {
                timeTo = $('#eventTimeFrom').val();
            }

            eventData.end += " " + timeTo;
            fcEventEnd = $.fullCalendar.moment(eventData.end, "DD.MM.YYYY HH:mm");
        } else {
            eventData.start += " 00:00";
            fcEventEnd.add(1, 'days');
            eventData.end = fcEventEnd.format('DD.MM.YYYY HH:mm');
        }

        eventData.start.trim();
        if (typeof eventData.end === 'string') {
            eventData.end.trim();
        }

        eventData.location = $('#eventLocation').val();
        eventData.description = $('#eventDescription').val();

        eventData['restrictionIndices'] = $('#eventRestriction').selectpicker('val');

        eventData['absenceWholeSchool'] = $('#eventAbsencesWholeSchool').prop('checked');
        eventData['absenceNoBigTests'] = $('#eventAbsencesNoBigTests').prop('checked');
        eventData['absenceNoSmallTests'] = $('#eventAbsencesNoSmallTests').prop('checked');

        var activeLabels = $('#eventAbsence').find('label.active.class-button');

        eventData['absencesSelectedClasses'] = [];

        for (var i = 0; i < activeLabels.length; i++) {
            eventData['absencesSelectedClasses'].push($(activeLabels[i]).data('class-id'));
        }

        eventData['backgroundColor'] = $('#eventcolor').val();
        eventData['borderColor'] = $('#eventcolor').data('bordercolor');
        eventData['textColor'] = $('#eventcolor').data('textcolor');

        eventData['backgroundRendering'] = $('#eventBackgroundRendering').prop('checked');
        eventData['preliminary'] = $('#eventPreliminary').prop('checked');

        $.post('/calendar/setEventDetails',
            JSON.stringify(eventData),
            function (data) {

                if (data.status === "success") {

                    // new Event?
                    if (detailsDialogFullcalendarEvent === null) {

                        if (data.event.backgroundRendering) {
                            data.event.rendering = 'background'
                        }

                        $('#fullCalendar').fullCalendar('renderEvent', data.event);

                    } else {

                        detailsDialogFullcalendarEvent.title = eventData.title;
                        detailsDialogFullcalendarEvent.start = fcEventStart;
                        detailsDialogFullcalendarEvent.end = fcEventEnd;
                        detailsDialogFullcalendarEvent.allDay = data.event.allDay;
                        detailsDialogFullcalendarEvent.backgroundColor = eventData['backgroundColor'];
                        detailsDialogFullcalendarEvent.borderColor = eventData['borderColor'];
                        detailsDialogFullcalendarEvent.textColor = eventData['textColor'];
                        detailsDialogFullcalendarEvent.absences = data.event.absences;
                        detailsDialogFullcalendarEvent.restrictions = data.event.restrictions;
                        detailsDialogFullcalendarEvent.rendering = data.event.backgroundRendering ? 'background' : undefined;
                        detailsDialogFullcalendarEvent.preliminary = data.event.preliminary;
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