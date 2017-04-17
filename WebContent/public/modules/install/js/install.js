/**
 * Created by martin on 23.09.2016.
 */



$(function () {

    $('#continueButton').click(function(event){

        $.ajax
        ({
            type: "POST",
            //the url where you want to sent the userName and password to
            url: '/install',
            dataType: 'json',
            async: true,
            //json object to sent to the authentication url
            data: JSON.stringify({
                username: $('#username').val(),
                name: $('#name').val(),
                password: $('#password').val()
            }),
            success: function (data, status, jqXHR) {

                startProgress(data.progressCode, function(result){

                    if(result == "success"){
                        window.location.href = "/main";
                    } else {
                        $('#mainPanel').text(result);
                    }

                });

            }
        });


    });

});

/**
 *
 * Every server side action which takes too much time emits a progressCode with which the client can
 * poll progressStatus further on till the action is finished. Pass progressCode to this function and you're done ;-)
 *
 * It shows a progress bar in a modal window and polls progress from server all 3 seconds.
 *
 * @param progressCode
 * @param callWhenCompleted
 *          callback function which is called when server-side action is completed. Server result data
 *          is passed as parameter.
 */
function startProgress(progressCode, callWhenCompleted) {

    //initialize modal window with progress bar
    $("#modalProgressbar").attr({
        "aria-valuemin": 0,
        "aria-valuemax": 100,
        "aria-valuenow": 0
    }).text("0%").css("width", "100%");

    $("#modalProgressbarWindow").modal("show").find(".modal-footer")
        .text("Warte auf den Beginn der Verarbeitung...")
        .css("text-align", "left");

    //periodic polling of status
    var progressIntervalID = setInterval(function () {
        $.ajax
        ({
            type: "POST",
            //the url where you want to sent the userName and password to
            url: '/progress',
            dataType: 'json',
            async: true,
            //json object to sent to the authentication url
            data: JSON.stringify({"progressCode": progressCode}),
            success: function (data, status, jqXHR) {

                $("#modalProgressbar").attr({
                    "aria-valuemin": data.min,
                    "aria-valuemax": data.max,
                    "aria-valuenow": data.now
                });
                $("#modalProgressbarWindow").find(".modal-footer").text(data.text);

                if (data.max - data.min > 0) {
                    var percent = "" + Math.round((data.now - data.min) / (data.max - data.min) * 100) + "%";
                    $("#modalProgressbar").css("width", percent).text(percent);
                } else {
                    $("#modalProgressbar").css("width", "100%");
                }


                if (data.completed) {
                    clearInterval(progressIntervalID); // stop timer
                    $("#modalProgressbarWindow").modal("hide");
                    callWhenCompleted(data.result); //callback to inform module which started progressbar
                }

            }
        });


    }, 1000);


}