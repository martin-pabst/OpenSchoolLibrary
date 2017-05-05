// JS file for library module


(function (app) {


    var userData = []; // Information about users and their roles

    if (!app.actions['startUserAdministration']) {
        app.actions['startUserAdministration'] = [];
    }

    app.actions['startUserAdministration'].push({

        open: function (parameters) {

            var used = $("body").height() + 50;
            $("#adminRolesPermissionsList").height($(window).height() - used - 20);
            $("#adminRolesRolesList").height($(window).height()
                - used - 35 - $("#adminRolesRolesList").next().height());

            initializeTables();


            initializeDOM();

            $('#userAdministrationRoesTab').trigger('shown.bs.tab');


        },
        close: function () {
            w2ui['adminRolesRolesList'].destroy();
            w2ui['adminRolesPermissionsList'].destroy();
            userData = null;
        }
    });


    function fetchData() {
        $.post('/admin/roleAdministration/getLists', JSON.stringify({school_id: global_school_id}),
            function (data) {

                var permissionNavigator = w2ui['adminRolesPermissionsList'];
                var roleNavigator = w2ui['adminRolesRolesList'];

                permissionNavigator.clear();
                roleNavigator.clear();

                permissionNavigator.add(data.permissions);
                roleNavigator.add(data.roles);

                userData = data.users;

            }, "json");

    }

    function initializeDOM() {


        $('#userAdministrationRolesTab').on('shown.bs.tab', function (e) {


            fetchData();

            w2ui['adminRolesPermissionsList'].resize();
            w2ui['adminRolesRolesList'].resize();

        });


        var searchAll = $('#adminRolesRolesList').find('.w2ui-search-all');

        searchAll.keyup(function (event) {
            this.onchange();
        });

        searchAll = $('#adminRolesPermissionsList').find('.w2ui-search-all');

        searchAll.keyup(function (event) {
            this.onchange();
        });
    }


    function initializeTables() {

        /**
         * Table left top which shows Roles
         */

        $('#adminRolesRolesList').w2grid({
            name: 'adminRolesRolesList',
            header: 'Rollen',
            //url		: 'library/inventoryBooks',
            buffered: 2000,
            recid: 'id',
            postData: {},
            show: {
                header: true,
                toolbar: true,
                selectColumn: false,
                multiSelect: false,
                toolbarAdd: true,
                toolbarEdit: true,
                toolbarDelete: true,
                toolbarSave: false,
                footer: true
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
            onAdd: function (event) {

                openAddEditRoleDialog(null);

            },

            onEdit: function (event) {

                if (this.getSelection(false).length === 1) {

                    var role = this.get(this.getSelection(false)[0]);

                    openAddEditRoleDialog(role);

                }

            },

            onDelete: function (event) {

                event.preventDefault();

                var roleGrid = w2ui['adminRolesRolesList'];

                var message = "Wollen Sie die Datensätze wirklich löschen?";

                w2confirm({
                    title: "Vorsicht:",
                    msg: message,
                    btn_yes: {"class": 'btn-red'},
                    callBack: function (result) {
                        if (result === 'Yes') {


                            var selectedIds = roleGrid.getSelection(false);

                            showUpdateMessage(roleGrid);

                            $.post('/admin/roleAdministration/removeRoles',
                                JSON.stringify({
                                    school_id: global_school_id,
                                    user_ids: selectedIds
                                }),
                                function (data) {

                                    if (data.status === 'success') {

                                        roleGrid.remove(selectedIds);

                                    } else {

                                        w2alert(data.message, "Fehler beim Löschen:");

                                    }

                                    hideUpdateMessage(roleGrid);

                                }, "json");


                        }
                    }
                });

            },

            onSelect: function (event) {

                event.onComplete = onSelectUnselectRoles;

            },

            onUnselect: function (event) {

                event.onComplete = onSelectUnselectRoles;

            }

        });

        /**
         * Table on the right which shows Permissions
         */

        $('#adminRolesPermissionsList').w2grid({
            name: 'adminRolesPermissionsList',
            header: 'Berechtigungen der selektierten Rolle',
            //url		: 'library/inventoryBooks',
            buffered: 2000,
            recid: 'name',
            postData: {},
            show: {
                header: true,
                toolbar: false,
                selectColumn: true,
                multiSelect: true,
                toolbarAdd: false,
                toolbarEdit: false,
                toolbarDelete: false,
                toolbarSave: false,
                footer: false
            },
            columns: [
                {field: 'name', caption: 'name', size: '10px', hidden: true, sortable: true},
                {field: 'module', caption: 'Modul', size: '30%', sortable: true, resizable: true},
                {field: 'remark', caption: 'Bemerkung', size: '70%', sortable: true, resizable: true}
            ],
            searches: [
                {field: 'module', caption: 'Modul', type: 'text'},
                {field: 'remark', caption: 'Beschreibung', type: 'text'}
            ],
            sortData: [{field: 'module', direction: 'asc'}],
            onDblClick: function (event) {
                event.preventDefault();
            },
            onClick: function (event) {

                event.preventDefault();

                var recid = event.recid;
                var selection = this.getSelection(false);

                var isSelected = false;

                for (var i = 0; i < selection.length; i++) {
                    if (selection[i] === recid) {
                        isSelected = true;
                        break;
                    }
                }

                var roleGrid = w2ui['adminRolesRolesList'];
                var selectedRoleList = roleGrid.getSelection(false); // id of user

                if (selectedRoleList.length === 1) {

                    var role_id = selectedRoleList[0];
                    var role = roleGrid.get(role_id);
                    var grid = this;

                    $.post('/admin/roleAdministration/addRemovePermission',
                        JSON.stringify({
                            school_id: global_school_id, role_id: role.id,
                            permission_name: recid, addRemove: isSelected ? 'remove' : 'add'
                        }),
                        function (data) {

                            if (data.status === 'success') {

                                if (isSelected) {
                                    grid.unselect(recid);

                                    var index = -1;
                                    for(var i = 0; i < role.permissionIdentifierList.length; i++){
                                        if(role.permissionIdentifierList[i] === recid){
                                            index = i;
                                        }
                                    }

                                    if(index >= 0){
                                        role.permissionIdentifierList.splice(index, 1);
                                    }
                                } else {
                                    grid.select(recid);
                                    role.permissionIdentifierList.push(recid);
                                }

                            } else {
                                w2alert(data.message, "Fehler beim Speichern:");

                            }

                        }, "json");


                }


            }


        });


    }

    /*
     * TODO: Hier geht's mit der Arbeit weiter...
     */

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



    function onSelectUnselectRoles() {

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
    function openAddEditRoleDialog(record) {

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
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Ist Admin:</label>' +
                '        <div>' +
                '           <input name="is_admin" type="checkbox"/>' +
                '        </div>' +
                '    </div>' +
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
                    {field: 'is_admin', type: 'checkbox', required: true},
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
                        rec.is_admin = record.is_admin;
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