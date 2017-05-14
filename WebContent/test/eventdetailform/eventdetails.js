/**
 * Created by Martin on 14.05.2017.
 */


$(function(){

    $('#launchButton').click(function(){
        $('#myModal').modal();
    });

    $('#myModal').on('shown.bs.modal', function (e) {
        $('#myForm').validator()
    });

});