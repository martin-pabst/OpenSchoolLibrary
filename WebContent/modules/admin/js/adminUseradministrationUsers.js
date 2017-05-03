// JS file for library module


(function (app) {


    if (!app.actions['startUserAdministration']) {
        app.actions['startUserAdministration'] = [];
    }

    app.actions['startUserAdministration'].push({

        open: function (parameters) {

            var used = $("body").height() + 50;
            $("#adminUsersUserList").height($(window).height() - used - 20);
            $("#adminUsersRoleList").height($(window).height() - used - 20);

            initializeTables();


            initializeDOM();

            $('#userAdministrationUsersTab').trigger('shown.bs.tab');


        },
        close: function () {
            w2ui['adminUsersUserList'].destroy();
            w2ui['adminUsersRoleList'].destroy();
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
            this.onchange();
        });

        searchAll = $('#adminUsersRoleList').find('.w2ui-search-all');

        searchAll.keyup(function (event) {
            this.onchange();
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
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true},
                {field: 'username', caption: 'Benutzername', size: '50px', sortable: true, resizable: true},
                {field: 'name', caption: 'Echter Name', size: '180px', sortable: true, resizable: true},
                {field: 'is_admin', caption: 'Admin', size: '50px', sortable: true, resizable: true, render: 'toggle'},
            ],
            searches: [
                {field: 'username', caption: 'Benutzername', type: 'text'},
                {field: 'name', caption: 'Echter Name', type: 'text'}
            ],
            sortData: [{field: 'username', direction: 'asc'},
                { field: 'name', direction: 'asc'}],

            onAdd: function (event) {

                openAddEditUserDialog(null);

            },

            onEdit: function (event) {

                if (this.getSelection(false).length === 1) {

                    var borrower = this.get(this.getSelection(false)[0])

                    openAddEditUserDialog(borrower);

                }

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
            header: 'Rollen',
            //url		: 'library/inventoryBooks',
            buffered: 2000,
            recid: 'id',
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
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true},
                {field: 'name', caption: 'Name', size: '50px', sortable: true, resizable: true},
                {field: 'remark', caption: 'Bemerkung', size: '180px', sortable: true, resizable: true}
            ],
            searches: [
                {field: 'name', caption: 'Name', type: 'text'},
                {field: 'remark', caption: 'Bemerkung', type: 'text'}
            ],
            sortData: [{field: 'name', direction: 'asc'}],

            onSelect: function (event) {

                // event.onComplete = onSelectUnselectUsers;

            },

            onUnselect: function (event) {

                // event.onComplete = onSelectUnselectUsers;

            }

        });



    }


    function onSelectUnselectUsers() {

        var userGrid = w2ui['adminUsersUserList'];
        var roleGrid = w2ui['adminUsersRoleList'];

        var selectedUserList = userGrid.getSelection(false); // id of user

        if (selectedUserList.length == 1) {

            var user_id = selectedUserList[0];

            var user = userGrid.get(user_id);
            var name = user.name;

        }

    }




    /**
     * Dialog for adding new users
     */
    function openAddEditUserDialog(record) {

        var user_id = null;

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
                '<div style="padding: 3px; font-weight: bold; color: #777;">Grunddaten</div>' +
                '<div class="w2ui-group" style="height: 285px;">' +

                '    <div class="w2ui-field w2ui-span8">' +
                '        <label>Rufname:</label>' +
                '        <div>' +
                '              <input name="firstname" type="text" maxlength="100" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Familienname:</label>' +
                '        <div>' +
                '           <input name="surname" type="text" maxlength="300" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Namensbest. vorang.:</label>' +
                '        <div>' +
                '           <input name="before_surname" type="text" maxlength="100" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Namensbest. nachg.:</label>' +
                '        <div>' +
                '           <input name="after_surname" type="text" maxlength="100" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Geschlecht:</label>' +
                '        <div>' +
                '           <input name="sex" type="text" maxlength="2" style="width: 100px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Geburtsdatum:</label>' +
                '        <div>' +
                '           <input name="date_of_birth" type="text" maxlength="10" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Klasse:</label>' +
                '        <div>' +
                '            <input name="classname" type="text" maxlength="20" style="width: 120px"/>' +
                '        </div>' +
                '    </div>' +

                '    </div></div>' +
                '<div style="width: 300px; float: right; margin-left: 0px;">' +
                '<div style="padding: 3px; font-weight: bold; color: #777;">Schullaufbahn:</div>' +
                '<div class="w2ui-group" style="height: 285px;">' +

                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Besuchter Religionsunterricht:</label>' +
                '        <div>' +
                '            <input name="religion" type="text" maxlength="20" style="width: 80px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Ausbildungsrichtung:</label>' +
                '        <div>' +
                '            <input name="curriculum" type="text" maxlength="20" style="width: 80px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>1. Femdsprache:</label>' +
                '        <div>' +
                '           <input name="language_1" type="text" maxlength="10" style="width: 50px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Von Jahrgangsstufe:</label>' +
                '        <div>' +
                '           <input name="from_form_1" type="text" maxlength="10" style="width: 50px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>2. Femdsprache:</label>' +
                '        <div>' +
                '           <input name="language_2" type="text" maxlength="10" style="width: 50px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Von Jahrgangsstufe:</label>' +
                '        <div>' +
                '           <input name="from_form_2" type="text" maxlength="10" style="width: 50px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>3. Femdsprache:</label>' +
                '        <div>' +
                '           <input name="language_3" type="text" maxlength="10" style="width: 50px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field  w2ui-span8">' +
                '        <label>Von Jahrgangsstufe:</label>' +
                '        <div>' +
                '           <input name="from_form_3" type="text" maxlength="10" style="width: 50px"/>' +
                '        </div>' +
                '    </div>' +

                '</div></div>' +

                '</div>' +
                '<div class="w2ui-buttons">' +
                '    <button class="btn" name="reset">Zurücksetzen</button>' +
                '    <button class="btn" name="save">Speichern</button>' +
                '</div>',
                fields: [
                    {field: 'firstname', type: 'text', required: true},
                    {field: 'surname', type: 'text', required: true},
                    {field: 'before_surname', type: 'text', required: false},
                    {field: 'after_surname', type: 'text', required: false},
                    {
                        field: 'sex', type: 'list',
                        options: {items: app.globalDefinitions().sexList},
                        showNone: false, required: true
                    },
                    {field: 'date_of_birth', type: 'date', format: 'dd.MM.yyyy', required: true},
                    {
                        field: 'classname', type: 'list',
                        options: {items: app.globalDefinitions().classList},
                        showNone: false, required: true
                    },
                    {
                        field: 'religion', type: 'list',
                        options: {items: app.globalDefinitions().religionList},
                        showNone: true, required: true
                    },
                    {
                        field: 'curriculum', type: 'list',
                        options: {items: app.globalDefinitions().curriculumList},
                        showNone: true, required: true
                    },
                    {
                        field: 'language_1', type: 'list',
                        options: {items: app.globalDefinitions().languageList},
                        showNone: true, required: false
                    },
                    {
                        field: 'from_form_1',
                        type: 'int',
                        required: false
                    },
                    {
                        field: 'language_2', type: 'list',
                        options: {items: app.globalDefinitions().languageList},
                        showNone: true, required: false
                    },
                    {
                        field: 'from_form_2',
                        type: 'int',
                        required: false
                    },
                    {
                        field: 'language_3', type: 'list',
                        options: {items: app.globalDefinitions().languageList},
                        showNone: true, required: false
                    },
                    {
                        field: 'from_form_3',
                        type: 'int',
                        required: false
                    }
                ],
                actions: {
                    "save": function () {

                        this.submit({}, function (response) {

                            w2popup.close();

                            if (response.status === "success") {

                                // var newRecord = response.borrowerRecord;
                                //
                                // w2ui['libraryBorrowerList'].add(newRecord);

                                fetchData();

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

        w2ui.libraryAddStudentDialog.postData = {
            school_id: global_school_id, school_term_id: global_school_term_id,
            student_id: student_id, student_school_term_id: student_school_term_id
        };


        $().w2popup('open', {
            title: 'Schüler/in hinzufügen',
            body: '<div id="form" style="width: 100%; height: 100%;"></div>',
            style: 'padding: 15px 0px 0px 0px',
            width: 800,
            height: 500,
            showMax: true,
            onToggle: function (event) {
                $(w2ui.foo.box).hide();
                event.onComplete = function () {
                    $(w2ui.libraryAddStudentDialog.box).show();
                    w2ui.libraryAddStudentDialog.resize();
                }
            },
            onOpen: function (event) {
                event.onComplete = function () {

                    if (record !== null) {
                        var rec = w2ui.libraryAddStudentDialog.record;

                        rec.surname = record.surname;
                        rec.firstname = record.firstname;
                        rec.before_surname = record.before_surname;
                        rec.after_surname = record.after_surname;
                        rec.curriculum = {id: record.curriculum_id};
                        rec.sex = {id: record.sex_key};
                        rec.classname = {id: record.class_id};
                        rec.date_of_birth = record.dateofbirth;
                        rec.religion = {id: record.religion_id};

                        for (var i = 1; i <= 3; i++) {
                            if (record.languageskills.length >= i) {
                                rec["language_" + i] = {id: record.languageskills[i - 1].subject_id};
                                rec["from_form_" + i] = record.languageskills[i - 1].from_year;
                            } else {
                                rec["language_" + i] = {};
                                rec["from_form_" + i] = null;
                            }

                        }
                        w2ui.libraryAddStudentDialog.refresh();

                    } else {

                        w2ui.libraryAddStudentDialog.clear();

                    }

                    // specifying an onOpen handler instead is equivalent to specifying an onBeforeOpen handler, which would make this code execute too early and hence not deliver.
                    $('#w2ui-popup #form').w2render('libraryAddStudentDialog');
                }
            }
        });


    }


}(App));