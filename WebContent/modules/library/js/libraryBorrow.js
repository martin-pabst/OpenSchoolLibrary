// JS file for library module


(function (app) {

    if (!app.actions['startLibrary']) {
        app.actions['startLibrary'] = [];
    }

    app.actions['startLibrary'].push({

        open: function (parameters) {

            var used = $("body").height() + 50;
            $("#libraryBorrowerList").height($(window).height() - used - 20);

            $("#libraryBorrowedBooksList").height($(window).height() - used - 385);
            $("#libraryNeededBooksList").height($(window).height() - used - 385);


            initializeBorrowTables();


            initializeDOM();

            fetchData();
            $('#libraryBorrowTab').trigger('shown.bs.tab');


        },
        close: function () {
            w2ui['libraryBorrowerList'].destroy();
            w2ui['libraryBorrowedBooksList'].destroy();
            w2ui['libraryNeededBooksList'].destroy();
            bookCopyDetailsData = undefined;
        }
    });


    var bookFormStore = [];
    var bookCopyDetailsData;
    var bookCopyDetailsRecord;
    var bookCopyDetailsName;

    function fetchData() {
        $.post('/library/bookFormStore', JSON.stringify({school_id: global_school_id}), function (data) {

            bookFormStore = data;

            var gridObj = w2ui['libraryBorrowerList'];

            gridObj.load('/library/borrowerList/get', function () {

                if (typeof gridObj.records == "object" && gridObj.records.length > 0) {
                    gridObj.select(gridObj.records[0]);
                }

                return true;
            });

        }, "json");

    }

    function initializeDOM() {


        $('#bookDetailDate').w2field('date',  { format: 'dd.mm.yyyy' });


        $('#bookDetailButton').click(function(event){
            addBookDetail();
        });


        $("#bookDetailMarkUL li a").click(function(){
            $(this).parents(".dropup").find('.btn').html($(this).text() + ' <span class="caret"></span>');
            $(this).parents(".dropup").find('.btn').val($(this).data('value'));
        });

        $('#libraryBorrowTab').on('shown.bs.tab', function (e) {

            // Show w2ui-tables for the first time only if they are yet visible.
            // Otherwise they behave strange!

            //var gridObj = w2ui['libraryBorrowerList'];

            //if (typeof gridObj == "undefined") {

            //    initializeBorrowTables();

            //} else {
            w2ui['libraryBorrowerList'].resize();
            w2ui['libraryBorrowedBooksList'].resize();
            w2ui['libraryNeededBooksList'].resize();

            libraryBorrowsOnSelectUnselect();

            //bookCopyDetailsData = undefined;
            showBookCopyDetails();
            //}

        });


        var searchAll = $('#libraryBorrowerList').find('.w2ui-search-all');

        searchAll.keyup(function (event) {
            this.onchange();
        });


        $('#libraryBorrowBarcodeField').keypress(function (event) {

            if (event.which == 13) {

                var barcode = $('#libraryBorrowBarcodeField').val();

                var borrowerGrid = w2ui['libraryBorrowerList'];
                var borrowedGrid = w2ui['libraryBorrowedBooksList'];

                var selectedBorrowerList = borrowerGrid.getSelection(false); // id of borrower

                if (selectedBorrowerList.length == 1) {

                    var borrower = borrowerGrid.get(selectedBorrowerList[0]);

                    showUpdateMessage(borrowedGrid);

                    $.post("/library/borrowedBooks/save", JSON.stringify(
                        {
                            barcode: barcode,
                            unbookFromPreviousBorrower: false,
                            student_id: borrower.student_id,
                            teacher_id: borrower.teacher_id,
                            school_id: global_school_id
                        }),
                        function (data) {

                            hideUpdateMessage(borrowedGrid);

                            if (data.status == "success") {
                                borrowedGrid.add(
                                    {
                                        id: data.borrows_id,
                                        title: data.title,
                                        author: data.author,
                                        book_id: data.book_id,
                                        subject: data.subject,
                                        barcode: barcode
                                    }
                                );

                                libraryBorrowUpdateTables();
                                borrowedGrid.selectNone();
                                borrowedGrid.select(data.borrows_id);

                            } else if (data.status == "error") {
                                w2alert("Fehler beim Speichern der Daten: " + data.message);
                            } else {
                                bookAlreadyBorrowed(data, borrowedGrid, barcode, borrower.student_id, borrower.teacher_id);
                            }
                        },
                        "json"
                    );

                } else {
                    w2alert("Bitte wählen Sie genau eine/n Entleiher/in aus.")
                }


                $('#libraryBorrowBarcodeField').val('');

                event.stopPropagation();

                return false;
            }
        });

        function bookAlreadyBorrowed(data, borrowedGrid, barcode, student_id, teacher_id) {

            var firstname = data.student_firstname;
            var surname = data.student_surname;

            var followingPerson = "diese/n Schüler/in";

            if (data.status == "teacherHasBook") {
                firstname = data.teacher_firstname;
                surname = data.teacher_surname;
                followingPerson = "diese Lehrkraft";
            }

            if ((data.student_id && data.student_id == student_id) ||
                (data.teacher_id && data.teacher_id == teacher_id)) {
                w2alert("Das Buch mit dem Barcode " + barcode + " ist bereits auf " + followingPerson +
                    " registriert: " + surname + ", " + firstname);
                return;
            }


            var message = "Das Buch mit dem Barcode " + barcode + " ist noch auf " + followingPerson + " gebucht: <br />";
            message += surname + ", " + firstname;

            message += "<br /> Soll es auf den/die ausgewählte/n Schüler/in/Lehrkraft umgebucht werden?";

            w2confirm(message)
                .yes(function () {

                    $.post("/library/borrowedBooks/save", JSON.stringify(
                        {
                            barcode: barcode,
                            unbookFromPreviousBorrower: true,
                            student_id: student_id,
                            teacher_id: null,
                            school_id: global_school_id
                        }),
                        function (data) {

                            hideUpdateMessage(borrowedGrid);

                            if (data.status == "success") {
                                borrowedGrid.add(
                                    {
                                        id: data.borrows_id,
                                        title: data.title,
                                        author: data.author,
                                        book_id: data.book_id,
                                        subject: data.subject,
                                        barcode: barcode,
                                        school_id: global_school_id
                                    }
                                );

                                libraryBorrowUpdateTables();
                                borrowedGrid.selectNone();
                                borrowedGrid.select(data.borrows_id);

                            } else if (data.status == "error") {
                                w2alert("Fehler beim Speichern der Daten: " + data.message);
                            }
                        },
                        "json"
                    );


                })
                .no(function () {
                    libraryBorrowUpdateTables();
                });

        }


        $('#libraryBorrowBarcodeField').focusin(function (event) {

            $('body').css('backgroundColor', '#eeffee');

        });

        $('#libraryBorrowBarcodeField').focusout(function (event) {

            $('body').css('backgroundColor', '#ffffff');

        });


    }


    function initializeBorrowTables() {

        /**
         * Table left top which shows Books
         */

        $('#libraryBorrowerList').w2grid({
            name: 'libraryBorrowerList',
            header: 'Ausleiher/innen',
            //url		: 'library/inventoryBooks',
            buffered: 2000,
            recid: 'id',
            postData: {school_id: global_school_id, school_term_id: global_school_term_id},
            show: {
                header: false,
                toolbar: true,
                selectColumn: false,
                multiSelect: false,
                toolbarAdd: false,
                toolbarDelete: false,
                toolbarSave: false,
                footer: true
            },
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true},
                {field: 'class_name', caption: 'Klasse', size: '50px', sortable: true, resizable: true},
                {field: 'name', caption: 'Name', size: '180px', sortable: true, resizable: true},
                {field: 'languages', caption: 'Sprachen', size: '70px', sortable: true, resizable: true},
                {field: 'curriculum_name', caption: 'AR', size: '70px', sortable: true, resizable: true}
            ],
            searches: [
                {field: 'class_name', caption: 'Klasse', type: 'text'},
                {field: 'name', caption: 'Name', type: 'text'},
                {field: 'curriculum_name', caption: 'Ausbildungsrichtung', type: 'text'}
            ],
            sortData: [{field: 'class_name', direction: 'asc'}, {
                field: 'curriculum_name',
                direction: 'asc'
            }, {field: 'name', direction: 'asc'}],

            onSelect: function (event) {

                event.onComplete = libraryBorrowsOnSelectUnselect;

            },

            onUnselect: function (event) {

                event.onComplete = libraryBorrowsOnSelectUnselect;

            }

        });



        $('#libraryBorrowedBooksList').w2grid({
            name: 'libraryBorrowedBooksList',
            header: 'Geliehene Bücher',
            //url		: 'library/inventoryBooks',
            buffered: 200,
            recid: 'id',
            postData: {school_id: global_school_id},
            show: {
                header: true,
                toolbar: true,
                selectColumn: true,
                toolbarAdd: false,
                toolbarDelete: false,
                toolbarSave: false,
                toolbarReload: false,
                toolbarColumns: false,
                footer: false
            },
            multiSelect: true,
            toolbar: {
                items: [
                    {type: 'break'},
                    {
                        type: 'button',
                        id: 'returnBooksButton',
                        caption: 'Rückgabe',
                        img: 'fa fa-reply-all fa-fw',
                        disabled: true
                    }
                ],
                onClick: function (target, data) {
                    if (target == "returnBooksButton") {

                        var selectedItems = w2ui['libraryBorrowedBooksList'].getSelection();
                        if (selectedItems.length > 0) {
                            returnBooks(selectedItems);
                        }
                    }
                }
            },
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true, render: 'int'},
                {field: 'title', caption: 'Titel', size: '200px', sortable: true, resizable: true},
                {field: 'author', caption: 'Autor', size: '50px', hidden: true, sortable: true, resizable: true},
                {field: 'subject', caption: 'F', size: '30px', sortable: true, resizable: true},
                {field: 'barcode', caption: 'Barcode', size: '70px', sortable: true, resizable: true}
            ],
            searches: [
                {field: 'title', caption: 'Titel', type: 'text'},
                {field: 'barcode', caption: 'Barcode', type: 'text'}
            ],
            sortData: [{field: 'subject', direction: 'asc'}, {
                field: 'title',
                direction: 'asc'
            }],

            onClick: function (event) {

                var record = this.get(event.recid);

                //getBookCopyRemarks(record);

            },
            onSelect: function (event) {
                event.onComplete = function () {

                    var selection = this.getSelection(false);

                    if (selection.length == 0) {
                        this.toolbar.disable('returnBooksButton');
                    } else {
                        this.toolbar.enable('returnBooksButton');
                    }

                    if(selection.length == 1){
                        var record = this.get(selection[0]);
                        getBookCopyRemarks(record);
                    } else {
                        bookCopyDetailsRecord = undefined;
                        bookCopyDetailsData = undefined;
                        bookCopyDetailsName = undefined;
                        showBookCopyDetails();
                    }
                }
            },
            onUnselect: function (event) {
                this.onSelect(event);
            }

        });


        $('#libraryNeededBooksList').w2grid({
            name: 'libraryNeededBooksList',
            header: 'Benötigte Bücher',
            //url		: 'library/inventoryBooks',
            buffered: 2000,
            recid: 'id',
            show: {
                header: true,
                toolbar: false,
                selectColumn: false,
                toolbarAdd: false,
                toolbarDelete: false,
                toolbarSave: false,
                footer: false
            },
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true, render: 'int'},
                {field: 'title', caption: 'Titel', size: '190px', sortable: true, resizable: true},
                {field: 'author', caption: 'Autor', size: '50px', hidden: true, sortable: true, resizable: true},
                {field: 'subject', caption: 'F', size: '30px', sortable: true, resizable: true},
                {field: 'status', caption: 'Status', size: '60px', sortable: true, resizable: true},

            ],
            sortData: [{field: 'subject', direction: 'asc'}, {
                field: 'title',
                direction: 'asc'
            }],

            onClick: function (event) {

                var record = this.get(event.recid);

            }

        });

    }

    function libraryBorrowsOnSelectUnselect() {

        var borrowerGrid = w2ui['libraryBorrowerList'];
        var borrowedGrid = w2ui['libraryBorrowedBooksList'];
        var neededGrid = w2ui['libraryNeededBooksList'];

        var selectedBorrowerList = borrowerGrid.getSelection(false); // id of borrower

        if (selectedBorrowerList.length == 1) {

            var borrower_id = selectedBorrowerList[0];

            var borrower = borrowerGrid.get(borrower_id);
            var name = borrower.name;
            if (borrower.isStudent) {
                name += " (Klasse " + borrower.class_name + ")";
            } else {
                name += " (Lehrkraft)";
            }

            bookCopyDetailsName = name;

            $('#libraryBorrowBorrower').html("Bücher von " + name + ":");

            borrowedGrid.postData = {
                reference_id: borrower.isStudent ? borrower.student_id : borrower.teacher_id,
                reference_type: borrower.isStudent ? "student" : "teacher",
                school_id: global_school_id
            };

            borrowedGrid.load("/library/borrowedBooks/get", function () {

                libraryBorrowUpdateTables();
                var records = borrowedGrid.records;
                if(records.length > 0){
                    borrowedGrid.select(records[0].id);
                } else {
                    bookCopyDetailsData = undefined;
                    showBookCopyDetails();
                }

            });

        } else {
            borrowedGrid.clear();
            neededGrid.clear();
            $('#libraryBorrowBorrower').text('');
        }
    }

    function libraryBorrowUpdateTables() {

        var borrowerGrid = w2ui['libraryBorrowerList'];
        var borrowedGrid = w2ui['libraryBorrowedBooksList'];
        var neededGrid = w2ui['libraryNeededBooksList'];

        var selectedBorrowerList = borrowerGrid.getSelection(false); // id of borrower

        if (selectedBorrowerList.length == 1) {
            var selectedBorrowerId = selectedBorrowerList[0];
            var selectedBorrower = borrowerGrid.get(selectedBorrowerId);

            neededGrid.clear();

            if (selectedBorrower.isStudent) {
                bookFormStore.forEach(function (bfsr) {
                    if (bookNeeded(selectedBorrower, bfsr)) {

                        var status = "fehlt";

                        borrowedGrid.records.forEach(function (borrowedBook) {
                            if (borrowedBook.book_id == bfsr.book_id) {
                                status = "OK";
                            }
                        });

                        var record = {
                            id: bfsr.book_id,
                            title: bfsr.title,
                            author: bfsr.author,
                            subject: bfsr.subject,
                            status: status
                        };

                        neededGrid.add(record);

                    }
                });
            }


        }

    }

    function bookNeeded(selectedBorrower, bookFormStoreRecord) {

        for (var i = 0; i < bookFormStoreRecord.bookFormEntries.length; i++) {

            var entry = bookFormStoreRecord.bookFormEntries[i];

            var bookNeeded = true;

            if (entry.form_id) {
                if (selectedBorrower.form_id !== entry.form_id) {
                    bookNeeded = false;
                }
            }

            if (entry.curriculum_id && bookNeeded) {
                if (selectedBorrower.curriculum_id !== entry.curriculum_id) {
                    bookNeeded = false;
                }
            }

            if (entry.languageyear && bookNeeded) {

                if (bookFormStoreRecord.subject_id) {
                    var languageSkillFound = false;
                    selectedBorrower.languageskills.forEach(function (ls) {
                        if (ls.subject_id == bookFormStoreRecord.subject_id) {

                            if (typeof ls.from_year == "undefined" || selectedBorrower.year_of_school - ls.from_year + 1 == entry.languageyear) {
                                if (typeof ls.to_year == "undefined" || selectedBorrower.year_of_school <= ls.to_year) {
                                    languageSkillFound = true;
                                }
                            }

                        }

                    });

                    if (!languageSkillFound) {
                        bookNeeded = false;
                    }

                }

            }

            if (bookNeeded) {
                return true;
            }

        }

        return false;
    }


    function returnBooks(selectedItems) {

        var borrows_ids = [];
        selectedItems.forEach(function (si) {
            var record = w2ui['libraryBorrowedBooksList'].get(si);
            borrows_ids.push(record.borrows_id);
        });

        var message = "Soll die Rückgabe der " + selectedItems.length + " Bücher gebucht werden?";

        commitW2GridDelete(w2ui['libraryBorrowedBooksList'],
            {
                cmd: "delete",
                selected: borrows_ids,
                school_id: global_school_id
            }
            , "/library/borrowedBooks/delete", undefined, message);

    }

    function getBookCopyRemarks(record){

        $.post('/library/bookCopyStatus/get', JSON.stringify({barcode: record.barcode, school_id: global_school_id}), function (data) {

            bookCopyDetailsData = data;
            bookCopyDetailsRecord = record;

            showBookCopyDetails();

        }, "json");

    }

    function showBookCopyDetails(){

        var out = '<ul class="list-group" style="margin-bottom: 0">\n';

        if(bookCopyDetailsData){

            var lastMark = "Zustand als Note";

            if(bookCopyDetailsData.length == 0){
            out += '<li class="list-group-item">Keine Einträge</li>';
            }

            bookCopyDetailsData.statusList.forEach(function (record){
               out += '<li class="list-group-item"><span style="color: blue">' + record.statusdate + ':</span> ';

               if(record.event == "borrow"){
                   out += "<b>Entliehen</b> an <b style='color: forestgreen'>" + record.borrowername + "</b> durch " + record.username + "</div>";
               }

                if(record.event == "return"){
                    out += "<b>Rückgabe</b>; Buch angenommen von " + record.username + "</div>";
                }

                if(record.event == "examine"){
                    var badgeHtml = '';
                    if(record.mark && record.mark.length > 0){
                        badgeHtml = '<span class="badge">' + record.mark.substr(0, 1) + '</span>';
                    }
                    out += "<b>Begutachtet</b> durch " + record.username + badgeHtml + '</div>';
                    if(record.evidence) {
                        out += "<div style='white-space: pre'>" + Handlebars.Utils.escapeExpression(record.evidence) + "</div>";
                    }
                }

                if(record.mark){
                    lastMark = record.mark;
                }

                out += "</li>";
            });

            out += "</ul>\n"

            $('#bookDetailButton').prop("disabled",false);
            $('#bookDetailDate').prop("disabled",false);
            $('#bookDetailText').prop("disabled",false);
            $('#bookDetailMark').removeClass("disabled");

            $('#bookDetailMark').val(lastMark);
            $('#bookDetailMark').text(lastMark);
            $('#bookTitle').text(' "' + bookCopyDetailsRecord.title + '"');

        } else {
            $('#bookDetailButton').prop("disabled",true);
            $('#bookDetailDate').prop("disabled",true);
            $('#bookDetailText').prop("disabled",true);
            $('#bookDetailMark').addClass("disabled");
            $('#bookDetailText').val("");
            $('#bookDetailMark').val("");
            $('#bookDetailMark').text("Zustand als Note");
            $('#bookTitle').text('');
        }


        var today = w2utils.formatDate((new Date()), 'dd.MM.yyyy');
        $('#bookDetailDate').val(today);

        $('#bookDetailTextAll').html(out);
    }

    function addBookDetail(){

        if(bookCopyDetailsRecord) {

            var mark = $('#bookDetailMark').val();

            if (mark.indexOf("Note") > 0) {
                mark = null;
            }

            var statusdate = $('#bookDetailDate').val();

//            var dateParts = $('#bookDetailDate').val().split(".");
//            var statusdate = new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
            
            var bookDetail = {
                evidence: $('#bookDetailText').val(),
                event: "examine",
                mark: mark,
                borrowername: bookCopyDetailsName,
                username: app.globalDefinitions().username,
                book_copy_id: bookCopyDetailsRecord.id,
                statusdate: statusdate,
                school_id: global_school_id
            };


            $.post('/library/bookCopyStatus/save', JSON.stringify(bookDetail), function (data) {

                if (data.status == "success") {

                    bookCopyDetailsData.statusList.push(bookDetail);

                    showBookCopyDetails();

                } else {
                    w2alert(data.message, "Fehler beim Speichern der Bewertung");
                }

            }, "json");

        }
    }


}(App));