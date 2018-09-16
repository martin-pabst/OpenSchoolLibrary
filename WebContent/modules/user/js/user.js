// JS file for library module


(function (app) {

    if (!app.actions['userSettings']) {
        app.actions['userSettings'] = [];
    }

    app.actions['userSettings'].push({

        open: function (parameters) {

            initDOM();

        },
        close: function () {

        }
    });

    function initDOM(){
        var changePasswordDiv = $('#ls_changePassword');

        $('#ls_changePassword button').click(function(){

            var oldPassword = $('#usersettings_old_password').val();
            var newPassword1 = $('#usersettings_new_password1').val();
            var newPassword2 = $('#usersettings_new_password2').val();


            var animatedGif = changePasswordDiv.find('.animated_gif');
            var alertDiv = changePasswordDiv.find('.alert-success');
            var button = changePasswordDiv.find('button');

            animatedGif.show();
            button.prop('disabled', true);

            $.post("/library/settings/changePassword", JSON.stringify(
                {
                    school_id: global_school_id,
                    oldPassword: oldPassword,
                    newPassword1: newPassword1,
                    newPassword2: newPassword2
                }),
                function (data) {

                    animatedGif.hide();
                    button.prop('disabled', false);

                    if (data.status === "success") {

                        showMessage(alertDiv, 'success', data.message);
                        $('#usersettings_old_password').val('');
                        $('#usersettings_new_password1').val('');
                        $('#usersettings_new_password2').val('');

                    } else {

                        showMessage(alertDiv, 'danger', data.message);

                    }

                }, "json"
            );




        });
    }


}(App));