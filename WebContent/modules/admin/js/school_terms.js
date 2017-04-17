/**
 * Created by martin on 23.09.2016.
 */


// JS file for library module

(function (app) {

    if (!app.actions['startSchoolTerms']) {
        app.actions['startSchoolTerms'] = [];
    }

    app.actions['startSchoolTerms'].push({

        open: function (parameters) {

            initializeInventoryTables();

            w2ui['schoolList'].load('/admin/school_terms/schools/get');

            initializeDOM();

        },
        close: function () {
            w2ui['schoolList'].destroy();
            w2ui['termList'].destroy();
        }
    });

    function initializeDOM() {



    }


    function initializeInventoryTables() {

        /**
         * Table left top which shows schools
         */

        $('#schoolList').w2grid({
            name: 'schoolList',
            header: 'Schulen',
            //url		: 'library/inventoryBooks',
            buffered: 1000,
            recid: 'id',
            postData: {},
            show: {
                header: true,
                toolbar: true,
                selectColumn: true,
                multiSelect: true,
                toolbarAdd: true,
                toolbarDelete: true,
                toolbarSave: false,
                footer: true
            },
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true, render: 'int'},
                {
                    field: 'number',
                    caption: 'Nummer',
                    size: '100px',
                    sortable: true,
                    resizable: true,
                    editable: {type: 'text'}
                },
                {
                    field: 'name',
                    caption: 'Name',
                    size: '250px',
                    sortable: true,
                    resizable: true,
                    editable: {type: 'text'}
                },
                {field: 'abbreviation', caption: 'Abkürzung', size: '100px', resizable: true, editable: {type: 'text'}}
            ],
            searches: [
                {field: 'number', caption: 'Nummer', type: 'text'},
                {field: 'name', caption: 'Name', type: 'text'},
                {field: 'abbreviation', caption: 'Abkürzung', type: 'text'}
            ],
            sortData: [{field: 'number', direction: 'asc'}],

            onChange: function (event) {

                var change = {
                    recid: event.recid
                };

                change[this.columns[event.column].field] = event.value_new;

                var updateRequest = {
                    cmd: "update",
                    changes: [change]
                };

                commitW2GridChanges(this, updateRequest, "/admin/school_terms/schools/update");

            },

            onClick: function (event) {

                w2ui['termList'].clear();
                var record = this.get(event.recid);

                w2ui['libraryInventoryCopies'].postData['school_id'] = record.id;
                w2ui['libraryInventoryCopies'].load('/admin/school_terms/terms/get');

            },
            onSelect: function (event) {
                if (this.getSelection(false).length > 1) {
                    w2ui['termList'].clear();
                }
            },
            onAdd: function (event) {
                openAddSchoolDialog();
            },
            onDelete: function (event) {

                event.preventDefault();

                var json = {
                    cmd: "delete-records",
                    selected: this.getSelection(false)
                };

                commitW2GridDelete(this, json, "/admin/school_terms/schools/delete",
                    function () {

                        w2ui["termList"].clear();

                    });


            }

        });

        /**
         * Table bottom left which shows info about forms/curriculums for which selected book is used
         */

        $('#termList').w2grid({
            name: 'termList',
            header: 'Schuljahre',
            buffered: 1000,
            recid: 'id',
            show: {
                header: true,
                toolbar: true,
                selectColumn: true,
                multiSelect: true,
                toolbarAdd: true,
                toolbarDelete: true,
                toolbarSave: false,
                footer: true
            },
            postData: {school_id: null},
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true, render: 'int'},
                {
                    field: 'name',
                    caption: 'Bezeichnung',
                    size: '150px',
                    sortable: true,
                    resizable: true,
                    editable: {type: 'text'}
                },
                {
                    field: 'begin',
                    caption: 'Von',
                    size: '50px',
                    sortable: true,
                    resizable: true,
                    render: 'date:dd.MM.yyyy',
                    editable: {type: 'date'}
                },
                {
                    field: 'end',
                    caption: 'Bis',
                    size: '50px',
                    sortable: true,
                    resizable: true,
                    render: 'date:dd.MM.yyyy',
                    editable: {type: 'date'}
                }
            ],
            searches: [
                {field: 'name', caption: 'Bezeichnung', type: 'text'}
            ],
            sortData: [{field: 'begin', direction: 'asc'}, {
                field: 'end',
                direction: 'asc'
            }],

            onChange: function (event) {
                console.log(event);

                var change = {
                    recid: event.recid
                };

                change[this.columns[event.column].field] = event.value_new;

                var updateRequest = {
                    cmd: "update",
                    changes: [change]
                };

                commitW2GridChanges(this, updateRequest, "/admin/school_terms/terms/update");

            },
            onAdd: function (event) {

                openAddTermDialog();

            },
            onDelete: function (event) {

                event.preventDefault();

                var selectedIds = this.getSelection(false);

                var json = {
                    cmd: "delete-records",
                    selected: selectedIds
                };


                var grid = this;

                commitW2GridDelete(this, json, "/admin/school_terms/terms/delete",
                    function () {

                    });

            }

        });
    }

    /**
     * Dialog for adding new school
     */
    function openAddSchoolDialog() {

        if (!w2ui.adminAddSchoolDialog) {
            $().w2form({
                name: 'adminAddBookDialog',
                style: 'border: 0px; background-color: transparent;',
                url: '/admin/school_terms/schools/save',
                postData: {},
                formHTML: '<div class="w2ui-page page-0">' +
                '    <div class="w2ui-field">' +
                '        <label>Title:</label>' +
                '        <div>' +
                '           <input name="title" type="text" maxlength="300" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field">' +
                '        <label>Autoren:</label>' +
                '        <div>' +
                '            <input name="author" type="text" maxlength="300" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field">' +
                '        <label>Verlag:</label>' +
                '        <div>' +
                '            <input name="publisher" type="text" maxlength="200" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field">' +
                '        <label>Fach:</label>' +
                '        <div>' +
                '            <input name="subject" type="text" maxlength="10" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field">' +
                '        <label>ISBN:</label>' +
                '        <div>' +
                '            <input name="isbn" type="text" maxlength="13" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field">' +
                '        <label>Preis:</label>' +
                '        <div>' +
                '            <input name="price" type="text" maxlength="20" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '</div>' +
                '<div class="w2ui-buttons">' +
                '    <button class="btn" name="reset">Zurücksetzen</button>' +
                '    <button class="btn" name="save">Speichern</button>' +
                '</div>',
                fields: [
                    {field: 'title', type: 'text', required: true},
                    {field: 'author', type: 'text', required: true},
                    {field: 'publisher', type: 'text', required: false},
                    {field: 'subject', type: 'list', options: {items: app.globalDefinitions().subjectList}, showNone: true, required: false},
                    {field: 'isbn', type: 'text', required: false},
                    {field: 'price', type: 'money', required: false}
                ],
                actions: {
                    "save": function () {

                        var record = this.record; // this context changes inside following closure...

                        this.submit({}, function (response) {

                            w2popup.close();

                            if (response.status == "success") {

                                var newRecord = {};
                                for (key in record) {
                                    newRecord[key] = record[key];
                                }
                                newRecord.id = response.id;
                                newRecord.bookFormList = [];

                                w2ui['libraryInventoryBooks'].add(newRecord);

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

        $().w2popup('open', {
            title: 'Schule hinzufügen',
            body: '<div id="form" style="width: 100%; height: 100%;"></div>',
            style: 'padding: 15px 0px 0px 0px',
            width: 500,
            height: 400,
            showMax: true,
            onToggle: function (event) {
                $(w2ui.foo.box).hide();
                event.onComplete = function () {
                    $(w2ui.libraryAddSchoolDialog.box).show();
                    w2ui.libraryAddSchoolDialog.resize();
                }
            },
            onOpen: function (event) {
                event.onComplete = function () {
                    // specifying an onOpen handler instead is equivalent to specifying an onBeforeOpen handler, which would make this code execute too early and hence not deliver.
                    $('#w2ui-popup #form').w2render('adminAddSchoolDialog');
                }
            }
        });


    }




}(App));