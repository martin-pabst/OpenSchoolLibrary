/**
 * Created by Martin on 14.05.2017.
 */


$(function () {

    $('#launchButton').click(function () {
        $('#myModal').modal();
    });

    $('#myModal').on('shown.bs.modal', function (e) {

        $('#eventName').focus();

        $('#myForm').validator();


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

        loadDetailValues();

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

    function loadDetailValues() {

        $('#eventName').val('Name des Events');
        $('#eventNameShort').val('Shortname');
        $('#eventDateFrom').data('DateTimePicker').date('17.05.2017');
        $('#eventDateTo').data('DateTimePicker').date('18.05.2017');

        $('#eventTimeFrom').timepicker('setTime', '10:00');
        $('#eventTimeTo').timepicker('setTime', '12:00');

        $('#eventWholeDay').prop('checked', false);
        $('#eventLocation').val('Ingolstadt');
        $('#eventDescription').val('Beschreibung...\nNoch eine Zeile');

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


});

