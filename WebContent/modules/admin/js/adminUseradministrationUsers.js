// JS file for library module


(function (app) {


    var permissionData = [];

    if (!app.actions['startUserAdministration']) {
        app.actions['startUserAdministration'] = [];
    }

    app.actions['startUserAdministration'].push({

        open: function (parameters) {

            var used = $("body").height() + 55;
            $("#adminUsersUserList").height($(window).height() - used - 20);
            $("#adminUsersRoleList").height($(window).height()
                - used - 35 - $("#adminUsersRoleList").next().height());

            initializeTables();


            initializeDOM();

            $('#userAdministrationUsersTab').trigger('shown.bs.tab');


        },
        close: function () {
            w2ui['adminUsersUserList'].destroy();
            w2ui['adminUsersRoleList'].destroy();
            permissionData = null;
        }
    });


    // var bookFormStore = [];


    function fetchData() {
        $.post('/admin/userAdministration/getLists', JSON.stringify({school_id: global_school_id}),
            function (data) {

                var userNavigator = w2ui['adminUsersUserList'];
                var roleNavigator = w2ui['adminUsersRoleList'];

                userNavigator.clear();
                roleNavigator.clear();

                userNavigator.add(data.users);
                roleNavigator.add(data.roles);

                permissionData = data.permissions;

            }, "json");

    }

    function initializeDOM() {


        $('#userAdministrationUsersTab').on('shown.bs.tab', function (e) {


            fetchData();

            w2ui['adminUsersUserList'].resize();
            w2ui['adminUsersRoleList'].resize();

        });


        var searchAll = $('#adminUsersUserList').find('.w2ui-search-all');

        searchAll.keyup(function (event) {
            searchAll.blur();
            this.onchange();
            searchAll.focus();
        });

        searchAll = $('#adminUsersRoleList').find('.w2ui-search-all');

        searchAll.keyup(function (event) {
            searchAll.blur();
            this.onchange();
            searchAll.focus();
        });
    }


    function initializeTables() {

        /**
         * Table left top which shows Users
         */

        $('#adminUsersUserList').w2grid({
            name: 'adminUsersUserList',
            header: 'Benutzer',
            //url		: 'library/inventoryBooks',
            buffered: 2000,
            recid: 'id',
            postData: {},
            multiSelect: false,
            show: {
                header: true,
                toolbar: true,
                selectColumn: false,
                toolbarAdd: true,
                toolbarEdit: true,
                toolbarDelete: true,
                toolbarSave: false,
                footer: true
            },
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true},
                {field: 'username', caption: 'Benutzername', size: '20%', sortable: true, resizable: true},
                {field: 'name', caption: 'Echter Name', size: '80%', sortable: true, resizable: true},
                {
                    field: 'is_admin', caption: 'Admin', size: '50px', sortable: true, resizable: true,
                    render: function (record) {
                        return '<div>' + (record.is_admin ?
                                '<img src="/public/img/green_check_mark.png" style="height: 1em; display: block; margin: auto" />' : '') + '</div>';
                    }
                }
            ],
            searches: [
                {field: 'username', caption: 'Benutzername', type: 'text'},
                {field: 'name', caption: 'Echter Name', type: 'text'}
            ],
            sortData: [{field: 'username', direction: 'asc'},
                {field: 'name', direction: 'asc'}],

            onAdd: function (event) {

                openAddEditUserDialog(null);

            },

            onEdit: function (event) {

                if (this.getSelection(false).length === 1) {

                    var user = this.get(this.getSelection(false)[0]);

                    openAddEditUserDialog(user);

                }

            },

            onDelete: function (event) {

                event.preventDefault();

                var userGrid = w2ui['adminUsersUserList'];

                var message = "Wollen Sie die Datensätze wirklich löschen?";

                w2confirm({
                    title: "Vorsicht:",
                    msg: message,
                    btn_yes: {"class": 'btn-red'},
                    callBack: function (result) {
                        if (result === 'Yes') {


                            var selectedIds = userGrid.getSelection(false);

                            showUpdateMessage(userGrid);

                            $.post('/admin/userAdministration/removeUsers',
                                JSON.stringify({
                                    school_id: global_school_id,
                                    user_ids: selectedIds
                                }),
                                function (data) {

                                    if (data.status === 'success') {

                                        userGrid.remove(selectedIds);

                                    } else {

                                        w2alert(data.message, "Fehler beim Löschen:");

                                    }

                                    hideUpdateMessage(userGrid);

                                }, "json");


                        }
                    }
                });

            },

            onSelect: function (event) {

                event.onComplete = onSelectUnselectUsers;

            },

            onUnselect: function (event) {

                event.onComplete = onSelectUnselectUsers;

            }

        });

        /**
         * Table on the right which shows Roles
         */

        $('#adminUsersRoleList').w2grid({
            name: 'adminUsersRoleList',
            header: 'Rollen des selektierten Benutzers',
            //url		: 'library/inventoryBooks',
            buffered: 2000,
            recid: 'id',
            postData: {},
            multiSelect: true,
            show: {
                header: true,
                toolbar: false,
                selectColumn: false,
                toolbarAdd: false,
                toolbarEdit: false,
                toolbarDelete: false,
                toolbarSave: false,
                footer: false
            },
            columns: [
                {field: 'id', caption: 'ID', size: '10px', hidden: true, sortable: true},
                {field: 'translated_name', caption: 'Name', size: '30%', sortable: true, resizable: true},
                {field: 'remark', caption: 'Bemerkung', size: '70%', sortable: true, resizable: true}
            ],
            searches: [
                {field: 'translated_name', caption: 'Name', type: 'text'},
                {field: 'remark', caption: 'Bemerkung', type: 'text'}
            ],
            sortData: [{field: 'name', direction: 'asc'}],
            onDblClick: function (event) {
                event.preventDefault();
            },
            onClick: function (event) {

                // event.originalEvent.ctrlKey = true;
                event.preventDefault();

                var recid = event.recid;
                var selection = this.getSelection(false);

                var isSelected = false;

                for (var i = 0; i < selection.length; i++) {
                    if (selection[i] === Number(recid)) {
                        isSelected = true;
                        break;
                    }
                }

                var userGrid = w2ui['adminUsersUserList'];
                var selectedUserList = userGrid.getSelection(false); // id of user

                if (selectedUserList.length === 1) {

                    var user_id = selectedUserList[0];
                    var user = userGrid.get(user_id);
                    var grid = this;

                    $.post('/admin/userAdministration/addRemoveRole',
                        JSON.stringify({
                            school_id: global_school_id, user_id: user.id,
                            role_id: recid, addRemove: isSelected ? 'remove' : 'add'
                        }),
                        function (data) {

                            if (data.status === 'success') {

                                if (isSelected) {
                                    grid.unselect(recid);

                                    var index = -1;
                                    for(var i = 0; i < user.role_ids.length; i++){
                                        if(user.role_ids[i] === Number(recid)){
                                            index = i;
                                        }
                                    }

                                    if(index >= 0){
                                        user.role_ids.splice(index, 1);
                                    }
                                } else {
                                    grid.select(recid);
                                    user.role_ids.push(Number(recid));
                                }

                                renderRoleDetails();

                            } else {
                                w2alert(data.message, "Fehler beim Speichern:");

                            }

                        }, "json");


                }


            }

        });


    }

    function renderRoleDetails(){

        var roleGrid = w2ui['adminUsersRoleList'];
        var roleIds = roleGrid.getSelection(false);

        var html = '<ul class="list-group" style="margin-bottom: 0">\n';

        for(var i = 0; i < roleIds.length; i++){

            var role = roleGrid.get(roleIds[i]);
            html += '<li class="list-group-item"><div><span style="color: blue; font-weight: bold">';
            html += 'Rolle ' + role.translated_name + ':</span></div>';
            html += '<div>';

            for(var j = 0; j < role.permissionIdentifierList.length; j++){

                var permissionId = role.permissionIdentifierList[j];

                for(var k = 0; k < permissionData.length; k++){
                    var pd = permissionData[k];
                    if(pd.name === permissionId){
                        html += '<div><span style="font-weight: bold">Modul ' + pd.module + ': </span>' + pd.remark + '</div>';
                    }
                }

            }

            html += '</div></li>';

        }

        html += '</ul>';

        $('#UserAdministrationRoleDetails').html(html);
    }



    function onSelectUnselectUsers() {

        var userGrid = w2ui['adminUsersUserList'];
        var roleGrid = w2ui['adminUsersRoleList'];

        var selectedUserList = userGrid.getSelection(false); // id of user

        if (selectedUserList.length == 1) {

            var user_id = selectedUserList[0];

            var user = userGrid.get(user_id);
            var role_ids = user.role_ids;

            roleGrid.selectNone();
            for (var i = 0; i < role_ids.length; i++) {
                roleGrid.select(role_ids[i]);
            }

        } else {
            roleGrid.selectNone();
        }

        renderRoleDetails();

    }


    var old_record = null;

    /**
     * Dialog for adding new users
     */
    function openAddEditUserDialog(record) {

        var user_id = null;

        old_record = record;

        if (record !== null) {

            user_id = record.user_id;
        }

        if (!w2ui.addUserDialog) {

            $().w2form({
                name: 'addUserDialog',
                style: 'border: 0px; background-color: transparent;',
                url: '/admin/userAdministration/saveUser',
                formHTML: '<div class="w2ui-page page-0">' +

                '<div style="width: 440px; float: left; margin-right: 0px;">' +

                '    <div class="w2ui-field w2ui-span8">' +
                '        <label>Benutzername:</label>' +
                '        <div>' +
                '              <input name="username" type="text" maxlength="30" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Echter Name:</label>' +
                '        <div>' +
                '           <input name="name" type="text" maxlength="200" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
/*
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Ist Admin:</label>' +
                '        <div>' +
                '           <input name="is_admin" type="checkbox"/>' +
                '        </div>' +
                '    </div>' +
*/
                '    <div class="w2ui-field w2ui-span8">' +
                '        <label>Passwort:</label>' +
                '        <div>' +
                '              <input name="password" type="password" maxlength="30" style="width: 250px" autocomplete="new-password"/>' +
                '        </div>' +
                '    </div>' +
                '</div>' +
                '<div class="w2ui-buttons">' +
                '    <button class="btn" name="reset">Zurücksetzen</button>' +
                '    <button class="btn" name="save">Speichern</button>' +
                '</div>',
                fields: [
                    {field: 'username', type: 'text', required: true},
                    {field: 'name', type: 'text', required: true},
                    // {field: 'is_admin', type: 'checkbox', required: true},
                    {field: 'password', type: 'password', required: false}
                ],
                actions: {
                    "save": function () {

                        this.submit({}, function (response) {

                            w2popup.close();

                            if (response.status === "success") {

                                var newRecord = response.record;

                                if (old_record !== null) {
                                    w2ui['adminUsersUserList'].remove(old_record.id);
                                    newRecord.role_ids = old_record.role_ids;
                                    old_record = null; // for garbage collection
                                }

                                newRecord.role_ids = [];
                                w2ui['adminUsersUserList'].add(newRecord);

                            } else {
                                w2alert("Fehler beim Speichern", response.message);
                            }
                        });
                    },
                    "reset": function () {
                        this.clear();
                    }
                }
            });
        }

        w2ui.addUserDialog.postData = {
            school_id: global_school_id
        };


        $().w2popup('open', {
            title: 'Benutzer hinzufügen',
            body: '<div id="form" style="width: 100%; height: 100%;"></div>',
            style: 'padding: 15px 0px 0px 0px',
            width: 800,
            height: 500,
            showMax: true,
            onToggle: function (event) {
                $(w2ui.foo.box).hide();
                event.onComplete = function () {
                    $(w2ui.addUserDialog.box).show();
                    w2ui.addUserDialog.resize();
                }
            },
            onOpen: function (event) {
                event.onComplete = function () {

                    if (record !== null) {
                        var rec = w2ui.addUserDialog.record;

                        rec.username = record.username;
                        rec.name = record.name;
                        // rec.is_admin = record.is_admin;
                        rec.password = "";
                        rec.id = record.id;

                        w2ui.addUserDialog.refresh();

                    } else {

                        w2ui.addUserDialog.clear();

                    }

                    // specifying an onOpen handler instead is equivalent to specifying an onBeforeOpen handler, which would make this code execute too early and hence not deliver.
                    $('#w2ui-popup #form').w2render('addUserDialog');
                }
            }
        });


    }


}(App));