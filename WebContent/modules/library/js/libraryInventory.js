// JS file for library module

(function (app) {

    if (!app.actions['startLibrary']) {
        app.actions['startLibrary'] = [];
    }

    app.actions['startLibrary'].push({

        open: function (parameters) {

            initializeInventoryTables();
            initializeDOM();
            w2ui['libraryInventoryBooks'].load('/library/inventoryBooks/get');

        },
        close: function () {
            w2ui['libraryInventoryBooks'].destroy();
            w2ui['libraryInventoryCopies'].destroy();
            w2ui['libraryInventoryBookForm'].destroy();
        }
    });

    function initializeDOM() {
        $('#libraryInventoryTab').on('shown.bs.tab', function (e) {

            // Show w2ui-tables for the first time only if they are yet visible.
            // Otherwise they behave strange!
            //if (typeof w2ui['libraryInventoryBooks'] == "undefined") {
            //    initializeInventoryTables();
            //} else {
            w2ui['libraryInventoryBooks'].resize();
            w2ui['libraryInventoryCopies'].resize();
            w2ui['libraryInventoryBookForm'].resize();
            //}

            loadInventoryCopies();

            var searchAll = $('#libraryInventoryBooks').find('.w2ui-search-all');

            searchAll.keyup(function (event) {
                this.onchange();
            });

            var dateinput = $('#libraryInventoryBarcodeFields').find('.dateinput');
            dateinput.w2field('date', {format: 'dd.mm.yyyy'});
            var today = w2utils.formatDate((new Date()), 'dd.MM.yyyy');
            dateinput.val(today);

        });

        $('#libraryInventoryAddCopy').keypress(function (event) {

            if (event.which == 13) {

                var dateinput = $('#libraryInventoryBarcodeFields').find('.dateinput');
                var purchase_date = dateinput.val();

                var barcode = $('#libraryInventoryAddCopy').val();

                barcode = barcode.replace(/^0+/, ''); // remove leading "0"s

                var book_id = w2ui['libraryInventoryCopies'].postData['reference_id'];
                var edition = $('#libraryInventoryEdition').val();

                var selection = w2ui['libraryInventoryBooks'].getSelection(false);

                if(barcode.length >= 13){
                    w2alert("Sie haben eine ISBN-Nummer gescannt.\n Es wird keine Aktion ausgeführt.", "Fehler:", function() {
                        $('#libraryInventoryAddCopy').focus();
                    });
                } else if (selection.length < 1) {
                    w2alert("Bitte wählen Sie zuerst in der linken Tabelle ein Buch aus.", "Fehler: ", function() {
                        $('#libraryInventoryAddCopy').focus();
                    });
                    $('#libraryInventoryAddCopy').blur();
                } else if (barcode !== "") {

                    showUpdateMessage(w2ui['libraryInventoryCopies']);

                    $.post("/library/inventoryCopies/save", JSON.stringify(
                        {
                            cmd: "add",
                            record: {
                                barcode: barcode,
                                book_id: book_id,
                                edition: edition,
                                purchase_date: purchase_date
                            },
                            school_id: global_school_id
                        }),
                        function (data) {
                            hideUpdateMessage(w2ui['libraryInventoryCopies']);

                            if (data.status == "success") {
                                w2ui['libraryInventoryCopies'].add(
                                    {
                                        id: data.id,
                                        barcode: barcode,
                                        borrower: "Im Lager",
                                        class: "",
                                        edition: edition
                                    }
                                );
                            } else {
                                if(!$("#noErrorMessagesWhenAddCopy").is(':checked')){
                                    w2alert("Fehler beim Speichern der Daten: " + data.message, "Fehler: ", function(){
                                        $('#libraryInventoryAddCopy').focus();
                                    });
                                }
                            }
                        },
                        "json"
                    );


                    $('#libraryInventoryAddCopy').val('');

                    event.stopPropagation();

                    return false;
                }
            }
        });


        $('#libraryInventoryAddCopy').focusin(function (event) {

            $('#libraryInventoryRight').css('backgroundColor', '#eeffee');

        });

        $('#libraryInventoryAddCopy').focusout(function (event) {

            $('#libraryInventoryRight').css('backgroundColor', '#ffffff');

        });

        /**
         * Barcode input field to find book_copies
         */

        $('#libraryInventorySelect').keypress(function (event) {

            if (event.which == 13) {

                var barcode = $('#libraryInventorySelect').val();

                barcode = barcode.replace(/^0+/, ''); // remove leading "0"s

                $.post("/library/tools/findBookIdByBarcode", JSON.stringify({
                        school_id: global_school_id,
                        barcode: barcode
                    }),
                    function (data) {

                        if (data.status === "success") {

                            w2ui['libraryInventoryCopies'].barcodeToSelect = barcode;

                            var bookGrid = w2ui['libraryInventoryBooks'];
                            bookGrid.select(data.book_id);
                            bookGrid.scrollIntoView(bookGrid.get(data.book_id, true));

                        } else if (data.status === "error") {

                            w2alert(data.message);

                        }

                        $('#libraryInventorySelect').val('');

                    },
                    "json"
                );
                
                event.stopPropagation();

                return false;
            }
        });


        $('#libraryInventorySelect').focusin(function (event) {

            $('#libraryInventoryRight').css('backgroundColor', '#eeeeff');

        });

        $('#libraryInventorySelect').focusout(function (event) {

            $('#libraryInventoryRight').css('backgroundColor', '#ffffff');

        });


    }


    function initializeInventoryTables() {

        /**
         * Table left top which shows Books
         */

        $('#libraryInventoryBooks').w2grid({
            name: 'libraryInventoryBooks',
            header: 'Buchtitel',
            //url		: 'library/inventoryBooks',
            buffered: 1000,
            multiSelect: false,
            recid: 'id',
            postData: {school_id: global_school_id},
            show: {
                header: true,
                toolbar: true,
                selectColumn: true,
                toolbarAdd: true,
                toolbarDelete: true,
                toolbarSave: false,
                footer: false
            },
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true, render: 'int'},
                {
                    field: 'title',
                    caption: 'Titel',
                    size: '295px',
                    sortable: true,
                    resizable: true,
                    editable: {type: 'text'}
                },
                {
                    field: 'author',
                    caption: 'Autoren',
                    size: '140px',
                    sortable: true,
                    resizable: true,
                    editable: {type: 'text'}
                },
                {field: 'publisher', caption: 'Verlag', size: '110px', resizable: true, editable: {type: 'text'}},
                {
                    field: 'subject',
                    caption: 'Fach',
                    size: '40px',
                    resizable: true,
                    sortable: true,
                    editable: {type: 'list', items: app.globalDefinitions().subjectList, showNone: true},
                    render: function (record, index, col_index) {
                        var html = this.getCellValue(index, col_index).text;
                        return html || '';
                    }
                },
                {
                    field: 'isbn',
                    caption: 'ISBN',
                    size: '110px',
                    sortable: true,
                    resizable: true,
                    editable: {type: 'text'}
                },
                {
                    field: 'price',
                    caption: 'Preis',
                    size: '50px',
                    sortable: true,
                    resizable: true,
                    editable: {type: 'money'},
                    render: 'money'
                },
                {
                    field: 'approval_code',
                    caption: 'Zulassungs-Nr.',
                    size: '60',
                    sortable: true,
                    resizable: true,
                    editable: {type: 'text'}
                },
                {
                    field: 'edition',
                    caption: 'Auflage',
                    size: '40',
                    sortable: true,
                    resizable: true,
                    editable: {type: 'text'}
                }

                //{ field: 'remarks', caption: 'Bemerkungen', size: '120px', resizable: true },
            ],
            searches: [
                {field: 'title', caption: 'Titel', type: 'text'},
                {field: 'author', caption: 'Autoren', type: 'text'},
                {field: 'subject', caption: 'Fach', type: 'text'},
                {field: 'form', caption: 'Jgst', type: 'int'}
            ],
            sortData: [{field: 'grade', direction: 'asc'}, {field: 'subject', direction: 'asc'}, {
                field: 'title',
                direction: 'asc'
            }, {field: 'edition', direction: 'asc'}],

            onChange: function (event) {

                var change = {
                    recid: event.recid
                };

                change[this.columns[event.column].field] = event.value_new;

                var updateRequest = {
                    cmd: "update",
                    changes: [change],
                    school_id: global_school_id
                };

                commitW2GridChanges(this, updateRequest, "/library/inventoryBooks/update");

            },

            onSelect: function (event) {

                w2ui['libraryInventoryBookForm'].clear();
                var record = this.get(event.recid);

                if (typeof record.bookFormList !== "undefined") {
                    record.bookFormList.forEach(function (bf) {

                        if (typeof bf.bookFormRecord == "undefined") {

                            if (typeof bf.curriculum_id == "undefined") {
                                bf.curriculum_id = null;
                                bf.curriculum_name = "ALLE";
                            }

                            bf.bookFormRecord =
                                {
                                    id: bf.id,
                                    form: {
                                        id: bf.form_id, text: bf.form_name, valueOf: function () {
                                            return this.text
                                        }
                                    },
                                    curriculum: {
                                        id: bf.curriculum_id, text: bf.curriculum_name, valueOf: function () {
                                            return this.text
                                        }
                                    },
                                    languageyear: bf.languageyear

                                }
                        }

                        w2ui['libraryInventoryBookForm'].add([
                            bf.bookFormRecord
                        ]);


                    });
                }

                w2ui['libraryInventoryBookForm'].localSort();

                w2ui['libraryInventoryBookForm'].book = record;

                w2ui['libraryInventoryCopies'].postData['reference_id'] = record.id;

                loadInventoryCopies();

            },
            onAdd: function (event) {
                openAddBookDialog();
            },
            onDelete: function (event) {

                event.preventDefault();

                var json = {
                    cmd: "delete-records",
                    selected: this.getSelection(false),
                    school_id: global_school_id
                };

                commitW2GridDelete(this, json, "/library/inventoryBooks/delete",
                    function () {

                        w2ui["libraryInventoryBookForm"].clear();

                    });


            }

        });

        /**
         * Table bottom left which shows info about forms/curriculums for which selected book is used
         */

        $('#libraryInventoryBookForm').w2grid({
            name: 'libraryInventoryBookForm',
            header: 'Verwendung in den Jgst./Ausbildungsrichtungen',
            //url		: 'library/inventoryBooks',
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
            postData: {school_id: global_school_id},
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true, render: 'int'},
                {
                    field: 'form', caption: 'Jgst', size: '50px', sortable: true, resizable: true,
                    editable: {type: 'list', items: app.globalDefinitions().formList, showNone: false},
                    render: function (record, index, col_index) {
                        var html = this.getCellValue(index, col_index).text;
                        return html || '';
                    }
                },
                {
                    field: 'curriculum', caption: 'Ausbildungsrichtung', size: '140px', sortable: true, resizable: true,
                    editable: {type: 'list', items: app.globalDefinitions().curriculumList, showNone: false},
                    render: function (record, index, col_index) {
                        var html = this.getCellValue(index, col_index).text;
                        return html || '';
                    }
                },
                {
                    field: 'languageyear',
                    caption: 'Lernjahr (Fremdsprache)',
                    size: '120px',
                    resizable: true,
                    editable: {type: 'int'},
                    render: 'int'
                }
            ],
            searches: [
                {field: 'form', caption: 'Jahrgangsstufe', type: 'text'},
                {field: 'curriculum', caption: 'Ausbildungsrichtung', type: 'text'},
                {field: 'languageyear', caption: 'Lernjahr (Fremdspr.)', type: 'text'}
            ],
            sortData: [{field: 'form', direction: 'asc'}, {
                field: 'curriculum',
                direction: 'asc'
            }, {field: 'languageyear', direction: 'asc'}],

            onChange: function (event) {
                console.log(event);

                var change = {
                    recid: event.recid
                };

                change[this.columns[event.column].field] = event.value_new;

                var updateRequest = {
                    cmd: "update",
                    changes: [change],
                    school_id: global_school_id
                };

                commitW2GridChanges(this, updateRequest, "/library/inventoryBookForm/update");

            },
            onAdd: function (event) {

                openAddBookFormDialog(this.book);

            },
            onDelete: function (event) {

                event.preventDefault();

                var selectedIds = this.getSelection(false);

                var json = {
                    cmd: "delete-records",
                    selected: selectedIds,
                    school_id: global_school_id
                };


                var grid = this;

                commitW2GridDelete(this, json, "/library/inventoryBookForm/delete",
                    function () {

                        for (var i = 0; i < grid.book.bookFormList.length; i++) {

                            var bf = grid.book.bookFormList[i];

                            if (selectedIds.indexOf(bf.id) >= 0) {
                                grid.book.bookFormList.splice(i, 1);
                                i--;
                            }
                        }


                    });

            }

        });

        /**
         * Dialog on the right side which shows copies of books
         */

        $('#libraryInventoryCopies').w2grid({
            name: 'libraryInventoryCopies',
            barcodeToSelect: null,
            header: 'Exemplare',
            //url		: 'library/inventoryBooks',
            buffered: 1000,
            recid: 'id',
            show: {
                header: true,
                toolbar: true,
                selectColumn: true,
                multiSelect: true,
                toolbarAdd: false,
                toolbarDelete: true,
                toolbarSave: false,
                footer: true
            },
            postData: {school_id: global_school_id, school_term_id: global_school_term_id},
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true, render: 'int'},
                {
                    field: 'barcode',
                    caption: 'Barcode',
                    size: '120px',
                    sortable: true,
                    resizable: true,
                },
                {
                    field: 'class_name',
                    caption: 'Klasse',
                    size: '60px',
                    sortable: true,
                    resizable: true,
                },
                {
                    field: 'borrower',
                    caption: 'Entleiher/in (bzw. Lager)',
                    size: '170px',
                    sortable: true,
                    resizable: true,
                },
                {
                    field: 'edition',
                    caption: 'Auflage',
                    sortable: true,
                    size: '70px',
                    resizable: true,
                },
                {
                    field: 'purchase_date',
                    caption: 'Kaufdatum',
                    sortable: true,
                    size: '90px',
                    resizable: true,
                    hidden: true
                }

            ],
            searches: [
                {field: 'borrower', caption: 'Entleiher/in (bzw. Lager)', type: 'text'},
                {field: 'class_name', caption: 'Klasse', type: 'text'},
                {field: 'barcode', caption: 'Barcode', type: 'text'}
            ],
            sortData: [{field: 'class_name', direction: 'asc'}, {
                field: 'borrower',
                direction: 'asc'
            }, {field: 'edition', direction: 'asc'}, {field: 'barcode', direction: 'asc'}],

            onDelete: function (event) {

                event.preventDefault();

                var json = {
                    cmd: "delete-records",
                    selected: this.getSelection(false),
                    school_id: global_school_id
                };

                commitW2GridDelete(this, json, "/library/inventoryCopies/delete",
                    function () {

                    });

            }

        });

        w2ui['libraryInventoryCopies'].getFooterHTML = function(){
            return '<div class="w2ui-footer-center"></div>';
        }

    }

    /**
     * Dialog for adding new books
     */
    function openAddBookDialog() {

        if (!w2ui.libraryAddBookDialog) {
            $().w2form({
                name: 'libraryAddBookDialog',
                style: 'border: 0px; background-color: transparent;',
                url: '/library/inventoryBooks/save',
                postData: {school_id: global_school_id},
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
                '        <label>Zulassungs-Nr.:</label>' +
                '        <div>' +
                '            <input name="approval_code" type="text" maxlength="30" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field">' +
                '        <label>Auflage:</label>' +
                '        <div>' +
                '            <input name="edition" type="text" maxlength="30" style="width: 250px"/>' +
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
                    {
                        field: 'subject',
                        type: 'list',
                        options: {items: app.globalDefinitions().subjectList},
                        showNone: true,
                        required: false
                    },
                    {field: 'isbn', type: 'text', required: false},
                    {field: 'approval_code', type: 'text', required: false},
                    {field: 'edition', type: 'text', required: false},
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
            title: 'Buch hinzufügen',
            body: '<div id="form" style="width: 100%; height: 100%;"></div>',
            style: 'padding: 15px 0px 0px 0px',
            width: 500,
            height: 400,
            showMax: true,
            onToggle: function (event) {
                //$(w2ui.foo.box).hide();
                event.onComplete = function () {
                    $(w2ui.libraryAddBookDialog.box).show();
                    w2ui.libraryAddBookDialog.resize();
                }
            },
            onOpen: function (event) {
                event.onComplete = function () {
                    w2ui.libraryAddBookDialog.clear();
                    // specifying an onOpen handler instead is equivalent to specifying an onBeforeOpen handler, which would make this code execute too early and hence not deliver.
                    $('#w2ui-popup #form').w2render('libraryAddBookDialog');
                }
            }
        });


    }


    function loadInventoryCopies(){

        if(typeof w2ui['libraryInventoryCopies'].postData['reference_id'] !== 'undefined') {

            w2ui['libraryInventoryCopies'].load('/library/inventoryCopies/get',
                function () {

                    var imLager = 0;
                    var entliehen = 0;
                    var records = w2ui['libraryInventoryCopies'].records;

                    for (var i = 0; i < records.length; i++) {
                        if (records[i].borrower.indexOf("Lager") !== -1) {
                            imLager++;
                        } else {
                            entliehen++;
                        }
                    }

                    var text = 'Entliehen: <span style="color:red; font-weight: bold">' + entliehen + '</span>, im Lager: <span style="color:green; font-weight: bold">' + imLager;
                    $('#grid_' + 'libraryInventoryCopies' + '_footer').find('.w2ui-footer-center').html(text);

                    if (w2ui['libraryInventoryCopies'].barcodeToSelect !== null) {

                        var recordToSelect = null;

                        var barcode = w2ui['libraryInventoryCopies'].barcodeToSelect;
                        w2ui['libraryInventoryCopies'].barcodeToSelect = null;

                        w2ui['libraryInventoryCopies'].records.forEach(function (record) {
                            if (record.barcode === barcode) {
                                recordToSelect = record;
                            }
                        });

                        if (recordToSelect === null) {
                            w2alert("Das Buch mit dem Barcode " + barcode + " ist nicht in der Tabelle enthalten.", "Hinweis");
                        } else {

                            w2ui['libraryInventoryCopies'].selectNone();
                            w2ui['libraryInventoryCopies'].select(recordToSelect.id);
                            w2ui['libraryInventoryCopies'].scrollIntoView(w2ui['libraryInventoryCopies'].get(recordToSelect.id, true));
                        }

                    } else {
                        w2ui['libraryInventoryCopies'].selectNone();
                    }

                });

        }
    }

    /**
     * Dialog for adding new books
     */
    function openAddBookFormDialog(book) {

        if (!w2ui.libraryAddBookFormDialog) {
            $().w2form({
                name: 'libraryAddBookFormDialog',
                style: 'border: 0px; background-color: transparent;',
                url: '/library/inventoryBookForm/save',
                postData: {school_id: global_school_id},
                formHTML: '<div class="w2ui-page page-0">' +
                '    <div class="w2ui-field">' +
                '        <label>Jahrgangsstufe:</label>' +
                '        <div>' +
                '           <input name="form" type="text" maxlength="10" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field">' +
                '        <label>Ausbildungsrichtung:</label>' +
                '        <div>' +
                '            <input name="curriculum" type="text" maxlength="300" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '    <div class="w2ui-field">' +
                '        <label>Lernjahr (Fremdsprache):</label>' +
                '        <div>' +
                '            <input name="languageyear" type="int" style="width: 250px"/>' +
                '        </div>' +
                '    </div>' +
                '</div>' +
                '<div class="w2ui-buttons">' +
                '    <button class="btn" name="reset">Zurücksetzen</button>' +
                '    <button class="btn" name="save">Speichern</button>' +
                '</div>',
                fields: [
                    {
                        field: 'form',
                        type: 'list',
                        options: {items: app.globalDefinitions().formList},
                        showNone: false,
                        required: true
                    },
                    {
                        field: 'curriculum',
                        type: 'list',
                        options: {items: app.globalDefinitions().curriculumList},
                        showNone: true,
                        required: false
                    },
                    {field: 'languageyear', type: 'int', required: false}
                ],
                actions: {
                    "save": function () {

                        var record = this.record; // this context changes inside following closure...

                        this.record.book_id = w2ui['libraryInventoryBookForm'].book.id;

                        this.submit({}, function (response) {

                            w2popup.close();

                            if (response.status == "success") {

                                var newRecord = {
                                    form: {
                                        id: record.form.id,
                                        text: record.form.text,
                                        valueOf: function () {
                                            return this.text
                                        }
                                    },
                                    curriculum: {
                                        id: record.curriculum.id,
                                        text: record.curriculum.text,
                                        valueOf: function () {
                                            return this.text
                                        }
                                    },
                                    languageyear: record.languageyear
                                };

                                newRecord.id = response.id;

                                w2ui['libraryInventoryBookForm'].add(newRecord);
                                w2ui['libraryInventoryBookForm'].book.bookFormList.push(
                                    {
                                        id: response.id,
                                        bookFormRecord: newRecord
                                    }
                                );

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

        if (typeof book !== "undefined") {

            $().w2popup('open', {
                title: 'Buch hinzufügen',
                body: '<div id="form" style="width: 100%; height: 100%;"></div>',
                style: 'padding: 15px 0px 0px 0px',
                width: 500,
                height: 400,
                showMax: true,
                onToggle: function (event) {
                    $(w2ui.foo.box).hide();
                    event.onComplete = function () {
                        $(w2ui.libraryAddBookFormDialog.box).show();
                        w2ui.libraryAddBookFormDialog.resize();
                    }
                },
                onOpen: function (event) {
                    event.onComplete = function () {
                        w2ui.libraryAddBookFormDialog.clear();

                        // specifying an onOpen handler instead is equivalent to specifying an onBeforeOpen handler, which would make this code execute too early and hence not deliver.
                        $('#w2ui-popup #form').w2render('libraryAddBookFormDialog');
                    }
                }
            });

        } else {
            w2alert("Bitte selektieren Sie zuerst in der oberen Tabelle ein Buch.", "Einfügen (noch) nicht möglich");
        }


    }

}(App));