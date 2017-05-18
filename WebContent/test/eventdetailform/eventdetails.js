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


    });

    $('#calendarDetailsForm').validator().on('submit', function(e){
        if(!e.isDefaultPrevented()){
            alert('hier!');
        }

    });

});
