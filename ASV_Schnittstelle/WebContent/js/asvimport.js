/**
 * Created by martin on 14.08.2016.
 */

(function (app) {

    if (!app.actions['startAdminASVImport']) {
        app.actions['startAdminASVImport'] = [];
    }

    app.actions['startAdminASVImport'].push({

        open: function (parameters) {

            showAdminASVImportForm();

        },
        close: function () {

            w2ui['asvImportForm'].destroy();

        }
    });

    function showAdminASVImportForm() {
        $('#asvImportForm').w2form({
            name: 'asvImportForm',
            header: 'ASV-Import',
            url: '/asvimport',
            fields: [{
                name: 'file',
                type: 'file',
                required: true
            }, {
                name: 'zippassword',
                type: 'text',
                required: true
            }],
            postData:{
            	school_id: global_school_id
            },
            actions: {
                upload: function () {
                    var obj = this;
                    this.save({}, function (data) {
                        // this function is called when upload is finished
                        if (data.status == 'error') {
                            console.log('ERROR: ' + data.message);
                            return;
                        }
                        obj.clear();

                        // server starts processing data, so client shows progress bar:
                        startProgress(data.progressCode, function (response) {
                            // this function is called when server finished processing data.

                            $("#protocoll").html(response);
                            $("#responseView").show();

                        });
                    });
                }
            }
        });

    }

}(App));