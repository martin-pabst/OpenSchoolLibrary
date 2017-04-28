/**
 * Created by martin on 26.08.2016.
 */

// w2utils.settings['phrases'] = { ... } is too late, so we changed sourcecode of w2utils to do this:
w2utilsPhrases = {
    'Required field': 'Pflichtfeld',
    'Attach files by dragging and dropping or Click to Select': 'Dateien durch Drag\'n Drop einfügen oder Anklicken für Dateiauswahl...',
    'Reset': 'Zurücksetzen',
    'Search...': 'Suche...',
    'begins': 'beginnt mit',
    'ends': 'endet auf',
    'is': 'ist',
    'contains': 'enthält',
    'in': 'in',
    'not in': 'nicht in',
    'between': 'zwischen',
    'Search took': 'Die Suche dauerte',
    'sec': 's',
    'of': 'von',
    'Add new': 'Hinzufügen...',
    'Delete': 'Löschen',
    'Edit': 'Bearbeiten',
    'All Fields': 'Suchbegriff ...',
    'Add New': 'Hinzufügen',
    'Search': 'Suchen'
};

$(function () {

    /**
     * Settings for w2ui library
     */
    w2utils.settings['dataType'] = 'JSON';
    w2utils.settings['currencyPrefix'] = "€";
    w2utils.settings['locale'] = 'de-de';
    w2utils.settings.date_format = 'dd.mm.yyyy';


    /*
     * If e.g. current URL is http://localhost:8080/main#Library and user clicks on <a href = "#Library" />
     * then window.onHashchange is not triggered. This workaround does the job:
     */
    $(".navbar").find("a").on("click", function () {

        var hrefParts = this.href.split("#");

        if (hrefParts.length > 1) {
            location.hash == "#" + hrefParts[1] ? $(window).trigger("hashchange") : null;
        }
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


    }, 3000);


}

function commitW2GridChanges(gridObject, updateRequest, url) {

    var changes = updateRequest.changes;

    showUpdateMessage(gridObject);

    $.ajax
    ({
        type: "POST",
        //the url where you want to sent the userName and password to
        url: url,
        dataType: 'json',
        async: true,
        //json object to sent to the authentication url
        data: JSON.stringify(updateRequest),
        success: function (data, status, jqXHR) {

            hideUpdateMessage(gridObject);

            if (!(data.status == "success")) {
                w2alert(data.message, "Fehler beim Speichern:");
            }

            mergeOrDiscard2GridChanges(gridObject, changes, data.status == "success");
        },
        error: function (jqXhr, textStatus, errorThrown) {

            w2alert(errorThrown, "Verbindungsfehler:");
            mergeOrDiscard2GridChanges(gridObject, changes, false);

            hideUpdateMessage(gridObject);

        }
    });

}

function showUpdateMessage(gridObject) {

    if (typeof gridObject.numberOfOpenRequests == "undefined") {
        gridObject.numberOfOpenRequests = 1;
    } else {
        gridObject.numberOfOpenRequests++;
    }

    if (gridObject.numberOfOpenRequests == 1) {

        //$('.grid_toolbar_update_status').show();
        gridObject.lock();
    }

}

function hideUpdateMessage(gridObject) {
    gridObject.numberOfOpenRequests--;

    if (gridObject.numberOfOpenRequests == 0) {
        //$('.grid_toolbar_update_status').hide();
        gridObject.unlock();
    }
}

function mergeOrDiscard2GridChanges(gridObject, changes, isMerge) {

    for (var c in changes) {

        var record = gridObject.get(changes[c].recid);

        for (var s in changes[c]) {

            if (s == 'recid') continue; // do not allow to change recid

            if (isMerge) {
                try {
                    eval('record.' + s + ' = changes[c][s]');
                } catch (e) {
                }
            }
            try {
                eval('delete record.changes.' + s);
            } catch (e) {
            }

            if (Object.keys(record.changes).length == 0) {
                delete record.changes;
            }
        }

    }
    gridObject.refresh();
}

function commitW2GridDelete(gridObject, json, url, callBackIfSuccessful, confirmMessage) {

    var message = "Wollen Sie die Datensätze wirklich löschen?";
    if(confirmMessage){
        message = confirmMessage;
    }

    w2confirm({
        title: "Vorsicht:",
        msg: message,
        btn_yes: {"class": 'btn-red'},
        callBack: function (result) {
            if (result == 'Yes') {

                showUpdateMessage(gridObject);

                $.ajax
                ({
                    type: "POST",
                    //the url where you want to sent the userName and password to
                    url: url,
                    dataType: 'json',
                    async: true,
                    //json object to sent to the authentication url
                    data: JSON.stringify(json),
                    success: function (data, status, jqXHR) {

                        hideUpdateMessage(gridObject);

                        if (!(data.status == "success")) {
                            w2alert(data.message, "Fehler beim Löschen:");
                        } else {

                            gridObject.getSelection(false).forEach(function (recid) {
                                gridObject.remove(recid);
                            });

                            gridObject.selectNone();

                            if (typeof callBackIfSuccessful !== "undefined") {
                                callBackIfSuccessful();
                            }



                        }
                    },
                    error: function (jqXhr, textStatus, errorThrown) {

                        w2alert(errorThrown, "Verbindungsfehler:");

                        hideUpdateMessage(gridObject);

                    }
                });
            }
        }
    });

}