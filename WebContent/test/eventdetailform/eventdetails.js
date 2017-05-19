/**
 * Created by Martin on 14.05.2017.
 */


$(function(){

    $('#launchButton').click(function(){
        $('#myModal').modal();
    });

    $('#myModal').on('shown.bs.modal', function (e) {

        $('#eventName').focus();

        $('#myForm').validator();

        $('#eventDateTimeFrom').datetimepicker({
            locale: 'de',
            calendarWeeks: true
        });

        $('#eventDateTimeTo').datetimepicker({
            locale: 'de',
            calendarWeeks: true,
            useCurrent: false
        });

        $("#eventDateTimeFrom").on("dp.change", function (e) {
            $('#eventDateTimeTo').data("DateTimePicker").minDate(e.date);
        });
        
        $("#eventDateTimeTo").on("dp.change", function (e) {
            $('#eventDateTimeFrom').data("DateTimePicker").maxDate(e.date);
        });

        loadValues();

    });

    $('#eventAbsenceTab').on('shown.bs.tab', function () {

        // Alle Klassen anwesend
        // $('#eventAbsence').find('label').removeClass('active');

        $('#eventAbsence').find('label').on('focusin', (function(e){

            e.stopImmediatePropagation();

        }));

        $('#eventAbsence').find('button').click(function(e){

            var labels = $(this).siblings().find('label');

            var isSelected = $(labels[0]).hasClass('active');

            if(isSelected){
                labels.removeClass('active');
            } else {
                labels.addClass('active');
            }


        });

        $('#eventAbsencesWholeSchool').change(function(){
            if($(this).is(':checked')){
                $('#eventAbsencesMatrix').hide();
            } else {
                $('#eventAbsencesMatrix').show();
            }
        });

    });

        $('#calendarDetailsForm').validator().on('submit', function(e){
        if(!e.isDefaultPrevented()){
            alert('hier!');
        }

    });



});

function loadValues(){

    $('#eventName').val('Name des Events');
    $('#eventnameshort').val('Shortname');
    $('#eventDateTimeFrom').data('DateTimePicker').date('17.05.2017');
    $('#eventDateTimeTo').data('DateTimePicker').date('18.05.2017 10:02');
    $('#eventWholeDay').prop('checked', false);
    $('#eventLocation').val('Ingolstadt');
    $('#eventDescription').val('Beschreibung...\nNoch eine Zeile');

    $('#eventRestriction').find('option[value="3"]').prop('selected', true);

    $('#eventAbsencesWholeSchool').prop('checked', false);
    $('#eventAbsencesMatrix').find('label[text="5a"]').addClass('active');
    $('#eventAbsencesNoBigTests').prop('checked', false);
    $('#eventAbsencesNoBigTests').prop('checked', true);

}
