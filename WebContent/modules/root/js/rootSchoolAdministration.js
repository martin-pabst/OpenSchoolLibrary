// JS file for library module


(function (app) {
    
    if (!app.actions['startSchoolAdministration']) {
        app.actions['startSchoolAdministration'] = [];
    }

    app.actions['startSchoolAdministration'].push({

        open: function (parameters) {

            initializeTables();


            initializeDOM();

        },
        close: function () {
            w2ui['rootSchoolsSchoollist'].destroy();
            w2ui['rootSchoolsAdminList'].destroy();
        }
    });


    function fetchData() {
        $.post('/root/schoolAdministration/getLists', JSON.stringify({}),
            function (data) {

                var schoolNavigator = w2ui['rootSchoolsSchoollist'];

                schoolNavigator.clear();

                schoolNavigator.add(data.schools);
                
            }, "json");

    }

    function initializeDOM() {
        
            var used = $("body").height() + 55;
            $("#rootSchoolsSchoollist").height($(window).height() - used - 20);
            $("#rootSchoolsAdminList").height($(window).height()
                - used - 35 - $("#rootSchoolsSchoolDetails").parent().height());

            fetchData();

        w2ui['rootSchoolsSchoollist'].resize();
        w2ui['rootSchoolsAdminList'].resize();

        var searchAll = $('#rootSchoolsSchoollist').find('.w2ui-search-all');

        searchAll.keyup(function (event) {
            searchAll.blur();
            this.onchange();
            searchAll.focus();
        });

        searchAll = $('#rootSchoolsAdminList').find('.w2ui-search-all');

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

        $('#rootSchoolsSchoollist').w2grid({
            name: 'rootSchoolsSchoollist',
            header: 'Schulen',
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
                {field: 'number', caption: 'Nummer', size: '4em', sortable: true, resizable: true},
                {field: 'name', caption: 'Name', size: '70%', sortable: true, resizable: true},
                {field: 'abbreviation', caption: 'Abkürzung', size: '30%', sortable: true, resizable: true}
            ],
            searches: [
                {field: 'number', caption: 'Nummer', type: 'text'},
                {field: 'name', caption: 'Name', type: 'text'},
                {field: 'abbreviation', caption: 'Abkürzung', type: 'text'}
            ],
            sortData: [{field: 'name', direction: 'asc'}],
            onAdd: function (event) {

                openAddEditSchoolDialog(null);

            },

            onEdit: function (event) {

                if (this.getSelection(false).length === 1) {

                    var school = this.get(this.getSelection(false)[0]);

                    openAddEditSchoolDialog(school);

                }

            },

            onDelete: function (event) {

                event.preventDefault();

                var schoolGrid = w2ui['adminSchoolsSchoolList'];

                var message = "Wollen Sie die Schulen wirklich löschen?";

                w2confirm({
                    title: "Vorsicht:",
                    msg: message,
                    btn_yes: {"class": 'btn-red'},
                    callBack: function (result) {
                        if (result === 'Yes') {


                            var selectedIds = schoolGrid.getSelection(false);

                            showUpdateMessage(schoolGrid);

                            $.post('/root/schoolAdministration/removeSchool',
                                JSON.stringify({
                                    school_ids: selectedIds
                                }),
                                function (data) {

                                    if (data.status === 'success') {

                                        schoolGrid.remove(selectedIds);

                                    } else {

                                        w2alert(data.message, "Fehler beim Löschen:");

                                    }

                                    hideUpdateMessage(schoolGrid);

                                }, "json");


                        }
                    }
                });

            },

            onSelect: function (event) {

                event.onComplete = onSelectUnselectSchool;

            },

            onUnselect: function (event) {

                event.onComplete = onSelectUnselectSchool;

            }

        });

        /**
         * Table on the right which shows Permissions
         */

        $('#rootSchoolsAdminList').w2grid({
            name: 'rootSchoolsAdminList',
            header: 'Administratoren der selektierten Schule',
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
                footer: false
            },
            columns: [
                {field: 'id', caption: 'id', size: '10px', hidden: true, sortable: true},
                {field: 'username', caption: 'Benutzername', size: '30%', sortable: true, resizable: true},
                {field: 'name', caption: 'Name', size: '70%', sortable: true, resizable: true}
            ],
            searches: [
                {field: 'username', caption: 'Benutzername', type: 'text'},
                {field: 'name', caption: 'Name', type: 'text'}
            ],
            sortData: [{field: 'username', direction: 'asc'}],
            onAdd: function (event) {

                openAddEditAdminDialog(null);

            },

            onEdit: function (event) {

                if (this.getSelection(false).length === 1) {

                    var user = this.get(this.getSelection(false)[0]);

                    openAddEditAdminDialog(user);

                }

            },

            onDelete: function (event) {

                event.preventDefault();

                var adminGrid = w2ui['rootSchoolsAdminList'];

                var message = "Wollen Sie die Administratoren wirklich löschen?";

                w2confirm({
                    title: "Vorsicht:",
                    msg: message,
                    btn_yes: {"class": 'btn-red'},
                    callBack: function (result) {
                        if (result === 'Yes') {


                            var selectedIds = adminGrid.getSelection(false);

                            showUpdateMessage(adminGrid);

                            $.post('/root/schoolAdministration/removeAdmins',
                                JSON.stringify({
                                    school_ids: selectedIds
                                }),
                                function (data) {

                                    if (data.status === 'success') {

                                        adminGrid.remove(selectedIds);

                                    } else {

                                        w2alert(data.message, "Fehler beim Löschen:");

                                    }

                                    hideUpdateMessage(adminGrid);

                                }, "json");


                        }
                    }
                });

            }

        });


    }

    function renderSchoolDetails(){

        var schoolDetailsDiv = $('#rootSchoolsSchoolDetails');

        var school = getSelectedSchool();

        var html = 'Selektiert ist die Schule mit der id: ' + school.id;

        // TODO

        schoolDetailsDiv.html(html);
    }

    function getSelectedSchool(){

        var schoolGrid = w2ui['rootSchoolsSchoollist'];

        var selectedSchoolsList = schoolGrid.getSelection(false); // ids of roles

        if (selectedSchoolsList.length === 1) {

            var school_id = selectedSchoolsList[0];

            return schoolGrid.get(school_id);

        }

        return null;

    }

    function onSelectUnselectSchool() {

        var adminGrid = w2ui['rootSchoolsAdminList'];
        var schoolGrid = w2ui['rootSchoolsSchoollist'];

        var school = getSelectedSchool();

        adminGrid.selectNone();

        if (school !== null) {

            adminGrid.clear();

            for(var i = 0; i < school.admins.length; i++){
                adminGrid.add(school.admins[i]);
            }

            renderSchoolDetails();



            // TODO: add admins to grid
        }


    }


    var old_record = null;

    /**
     * Dialog for adding new schools
     */
    function openAddEditSchoolDialog(record) {

        var role_id = null;

        old_record = record;

        if (record !== null) {
            role_id = record.id;
        }

        if (!w2ui.addRoleDialog) {

            $().w2form({
                name: 'addSchoolDialog',
                style: 'border: 0px; background-color: transparent;',
                url: '/root/schoolAdministration/saveSchool',
                formHTML: '<div class="w2ui-page page-0">' +

                '<div style="width: 440px; float: left; margin-right: 0px;">' +

                '    <div class="w2ui-field w2ui-span8">' +
                '        <label>Schulnummer:</label>' +
                '        <div>' +
                '              <input name="number" type="text" maxlength="10" style="width: 100px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field w2ui-span8">' +
                '        <label>Name der Schule:</label>' +
                '        <div>' +
                '              <input name="name" type="text" maxlength="200" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Schulkürzel:</label>' +
                '        <div>' +
                '           <input name="abbreviation" type="text" maxlength="20" style="width: 100px"/>' +
                '        </div>' +
                '    </div>' +
                '</div>' +
                '<div class="w2ui-buttons">' +
                '    <button class="btn" name="reset">Zurücksetzen</button>' +
                '    <button class="btn" name="save">Speichern</button>' +
                '</div>',
                fields: [
                    {field: 'number', type: 'text', required: true},
                    {field: 'name', type: 'text', required: true},
                    {field: 'abbreviation', type: 'text', required: true}
                ],
                actions: {
                    "save": function () {

                        this.submit({}, function (response) {

                            w2popup.close();

                            if (response.status === "success") {

                                var newRecord = response.record;

                                if (old_record !== null) {
                                    w2ui['rootSchoolsSchoollist'].remove(old_record.id);
                                    // TODO
                                    // newRecord.permissions = old_record.permissions;
                                    // newRecord.permissionList = old_record.permissionList;
                                    old_record = null; // for garbage collection
                                } 
                                w2ui['rootSchoolsSchoollist'].add(newRecord);

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
            title: 'Schuldaten bearbeiten',
            body: '<div id="form" style="width: 100%; height: 100%;"></div>',
            style: 'padding: 15px 0px 0px 0px',
            width: 800,
            height: 500,
            showMax: true,
            onToggle: function (event) {
                $(w2ui.foo.box).hide();
                event.onComplete = function () {
                    $(w2ui.addSchoolDialog.box).show();
                    w2ui.addSchoolDialog.resize();
                }
            },
            onOpen: function (event) {
                event.onComplete = function () {

                    if (record !== null) {
                        var rec = w2ui.addSchoolDialog.record;

                        rec.number = record.number;
                        rec.name = record.name;
                        rec.abbreviation = record.abbreviation;
                        rec.id = record.id;

                        w2ui.addSchoolDialog.refresh();

                    } else {

                        w2ui.addSchoolDialog.clear();

                    }

                    // specifying an onOpen handler instead is equivalent to specifying an onBeforeOpen handler, which would make this code execute too early and hence not deliver.
                    $('#w2ui-popup #form').w2render('addSchoolDialog');
                }
            }
        });


    }


    /**
     * Dialog for adding new users
     */
    function openAddEditAdminDialog(record) {

        var school = getSelectedSchool();

        if(school === null){
            return;
        }

        var user_id = null;

        old_record = record;

        if (record !== null) {

            user_id = record.user_id;
        }

        if (!w2ui.addAdminDialog) {

            $().w2form({
                name: 'addAdminDialog',
                style: 'border: 0px; background-color: transparent;',
                url: '/root/schoolAdministration/saveAdmin',
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
                    {field: 'password', type: 'password', required: false}
                ],
                actions: {
                    "save": function () {

                        this.submit({}, function (response) {

                            w2popup.close();

                            if (response.status === "success") {

                                var newRecord = response.record;

                                if (old_record !== null) {
                                    w2ui['rootSchoolsAdminList'].remove(old_record.id);
                                    newRecord.role_ids = old_record.role_ids;
                                    old_record = null; // for garbage collection
                                }

                                newRecord.role_ids = [];
                                w2ui['rootSchoolsAdminList'].add(newRecord);

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

        w2ui.addAdminDialog.postData = {
            school_id: school.id
        };


        $().w2popup('open', {
            title: 'Daten des Admins bearbeiten',
            body: '<div id="form" style="width: 100%; height: 100%;"></div>',
            style: 'padding: 15px 0px 0px 0px',
            width: 800,
            height: 500,
            showMax: true,
            onToggle: function (event) {
                $(w2ui.foo.box).hide();
                event.onComplete = function () {
                    $(w2ui.addAdminDialog.box).show();
                    w2ui.addAdminDialog.resize();
                }
            },
            onOpen: function (event) {
                event.onComplete = function () {

                    if (record !== null) {
                        var rec = w2ui.addAdminDialog.record;

                        rec.username = record.username;
                        rec.name = record.name;
                        // rec.is_admin = record.is_admin;
                        rec.password = "";
                        rec.id = record.id;

                        w2ui.addAdminDialog.refresh();

                    } else {

                        w2ui.addAdminDialog.clear();

                    }

                    // specifying an onOpen handler instead is equivalent to specifying an onBeforeOpen handler, which would make this code execute too early and hence not deliver.
                    $('#w2ui-popup #form').w2render('addAdminDialog');
                }
            }
        });


    }


}(App));