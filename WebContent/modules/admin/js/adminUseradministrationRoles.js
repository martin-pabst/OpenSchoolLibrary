// JS file for library module


(function (app) {


    var roleToUserlistMap = {}; // Information about users and their roles
    var permissionNameToIdMap = {};

    if (!app.actions['startUserAdministration']) {
        app.actions['startUserAdministration'] = [];
    }

    app.actions['startUserAdministration'].push({

        open: function (parameters) {

            initializeTables();


            initializeDOM();

            $('#userAdministrationRolesTab').trigger('shown.bs.tab');


        },
        close: function () {
            w2ui['adminRolesRolesList'].destroy();
            w2ui['adminRolesPermissionsList'].destroy();
            roleToUserlistMap = null;
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

                permissionNameToIdMap = {};

                for(var i = 0; i < data.permissions.length; i++){
                    var permission = data.permissions[i];
                    permissionNameToIdMap[permission.name] = permission.id;
                }

                roleToUserlistMap = data.roleToUserlistMap;

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
            searchAll.blur();
            this.onchange();
            searchAll.focus();
        });

        searchAll = $('#adminRolesPermissionsList').find('.w2ui-search-all');

        searchAll.keyup(function (event) {
            searchAll.blur();
            this.onchange();
            searchAll.focus();
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
                                    role_ids: selectedIds
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
                {field: 'id', caption: 'id', size: '10px', hidden: true, sortable: true},
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
                    if (selection[i] === Number(recid)) {
                        isSelected = true;
                        break;
                    }
                }

                var roleGrid = w2ui['adminRolesRolesList'];
                var selectedRoleList = roleGrid.getSelection(false); // id of user

                var permission = this.get(recid);

                if (selectedRoleList.length === 1) {

                    var role_id = selectedRoleList[0];
                    var role = roleGrid.get(role_id);
                    var grid = this;

                    $.post('/admin/roleAdministration/addRemovePermission',
                        JSON.stringify({
                            school_id: global_school_id, role_id: role.id,
                            permission_name: permission.name, addRemove: isSelected ? 'remove' : 'add'
                        }),
                        function (data) {

                            if (data.status === 'success') {

                                if (isSelected) {
                                    grid.unselect(recid);

                                    var index = -1;
                                    for(var i = 0; i < role.permissionIdentifierList.length; i++){
                                        if(role.permissionIdentifierList[i] === permission.name){
                                            index = i;
                                        }
                                    }

                                    if(index >= 0){
                                        role.permissionIdentifierList.splice(index, 1);
                                    }
                                } else {
                                    grid.select(recid);
                                    role.permissionIdentifierList.push(permission.name);
                                }

                            } else {
                                w2alert(data.message, "Fehler beim Speichern:");

                            }

                        }, "json");


                }


            }


        });


    }

    function renderRoleDetails(){

        var roleGrid = w2ui['adminRolesRolesList'];
        var roleIds = roleGrid.getSelection(false);
        var roleDetailsDiv = $('#UsersWithSelectedRole');


        if(roleIds.length !== 1){
            roleDetailsDiv.html("");
            return;
        }

        var roleId = roleIds[0];

        var userList = roleToUserlistMap[roleId];

        if(userList === undefined){
            userList = [];
            roleToUserlistMap[roleId] = userList;
        }


        var html = '<ul class="list-group" style="margin-bottom: 0">\n';

        for(var i = 0; i < userList.length; i++){

            var user = userList[i];

            html += '<li class="list-group-item"><span style="color: blue; font-weight: bold">';
            html += user.name + '</span><span style="color: black"> (' + user.username + ')</span>';
            html += '</li>';

        }

        html += '</ul>';

        roleDetailsDiv.html(html);
    }



    function onSelectUnselectRoles() {

        var permissionGrid = w2ui['adminRolesPermissionsList'];
        var roleGrid = w2ui['adminRolesRolesList'];

        var selectedRolesList = roleGrid.getSelection(false); // ids of roles

        permissionGrid.selectNone();

        if (selectedRolesList.length === 1) {

            var role_id = selectedRolesList[0];

            var role = roleGrid.get(role_id);
            var permission_identifiers = role.permissionIdentifierList;

            var idsToSelect = [];

            for(var i = 0; i < permission_identifiers.length; i++){
                var id = permissionNameToIdMap[permission_identifiers[i]];
                if(id !== undefined){
                    permissionGrid.select(id);
                }
            }

        }

        renderRoleDetails();

    }


    var old_record = null;

    /**
     * Dialog for adding new roles
     */
    function openAddEditRoleDialog(record) {

        var role_id = null;

        old_record = record;

        if (record !== null) {
            role_id = record.id;
        }

        if (!w2ui.addRoleDialog) {

            $().w2form({
                name: 'addRoleDialog',
                style: 'border: 0px; background-color: transparent;',
                url: '/admin/roleAdministration/saveRole',
                formHTML: '<div class="w2ui-page page-0">' +

                '<div style="width: 440px; float: left; margin-right: 0px;">' +

                '    <div class="w2ui-field w2ui-span8">' +
                '        <label>Name der Rolle:</label>' +
                '        <div>' +
                '              <input name="name" type="text" maxlength="30" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Beschreibung:</label>' +
                '        <div>' +
                '           <input name="remark" type="text" maxlength="200" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '</div>' +
                '<div class="w2ui-buttons">' +
                '    <button class="btn" name="reset">Zurücksetzen</button>' +
                '    <button class="btn" name="save">Speichern</button>' +
                '</div>',
                fields: [
                    {field: 'name', type: 'text', required: true},
                    {field: 'remark', type: 'text', required: true}
                ],
                actions: {
                    "save": function () {

                        this.submit({}, function (response) {

                            w2popup.close();

                            if (response.status === "success") {

                                var newRecord = response.record;

                                if (old_record !== null) {
                                    w2ui['adminRolesRolesList'].remove(old_record.id);
                                    newRecord.permissions = old_record.permissions;
                                    newRecord.permissionList = old_record.permissionList;
                                    old_record = null; // for garbage collection
                                } else {
                                    roleToUserlistMap[newRecord.id] = [];
                                }

                                w2ui['adminRolesRolesList'].add(newRecord);

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

        w2ui.addRoleDialog.postData = {
            school_id: global_school_id
        };


        $().w2popup('open', {
            title: 'Rolle hinzufügen',
            body: '<div id="form" style="width: 100%; height: 100%;"></div>',
            style: 'padding: 15px 0px 0px 0px',
            width: 800,
            height: 500,
            showMax: true,
            onToggle: function (event) {
                $(w2ui.foo.box).hide();
                event.onComplete = function () {
                    $(w2ui.addRoleDialog.box).show();
                    w2ui.addRoleDialog.resize();
                }
            },
            onOpen: function (event) {
                event.onComplete = function () {

                    if (record !== null) {
                        var rec = w2ui.addRoleDialog.record;

                        rec.name = record.name;
                        rec.remark = record.remark;
                        rec.id = record.id;

                        w2ui.addRoleDialog.refresh();

                    } else {

                        w2ui.addRoleDialog.clear();

                    }

                    // specifying an onOpen handler instead is equivalent to specifying an onBeforeOpen handler, which would make this code execute too early and hence not deliver.
                    $('#w2ui-popup #form').w2render('addRoleDialog');
                }
            }
        });


    }


}(App));