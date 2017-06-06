// JS file for library module


(function (app) {

    var errorBeep = new Audio("data:audio/wav;base64,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qJrx4KOQjahZxWbcZgztj2c49nKmkId44S71j0c8eV9yDK6uPRzx5X18eDvjvQ6yKo9ZSS6l//8elePK/Lf//IInrOF/FvDoADYAGBMGb7FtErm5MXMlmPAJQVgWta7Zx2go+8xJ0UiCb8LHHdftWyLJE0QIAIsI+UbXu67dZMjmgDGCGl1H+vpF4NSDckSIkk7Vd+sxEhBQMRU8j/12UIRhzSaUdQ+rQU5kGeFxm+hb1oh6pWWmv3uvmReDl0UnvtapVaIzo1jZbf/pD6ElLqSX+rUmOQNpJFa/r+sa4e/pBlAABoAAAAA3CUgShLdGIxsY7AUABPRrgCABdDuQ5GC7DqPQCgbbJUAoRSUj+NIEig0YfyWUho1VBBBA//uQZB4ABZx5zfMakeAAAAmwAAAAF5F3P0w9GtAAACfAAAAAwLhMDmAYWMgVEG1U0FIGCBgXBXAtfMH10000EEEEEECUBYln03TTTdNBDZopopYvrTTdNa325mImNg3TTPV9q3pmY0xoO6bv3r00y+IDGid/9aaaZTGMuj9mpu9Mpio1dXrr5HERTZSmqU36A3CumzN/9Robv/Xx4v9ijkSRSNLQhAWumap82WRSBUqXStV/YcS+XVLnSS+WLDroqArFkMEsAS+eWmrUzrO0oEmE40RlMZ5+ODIkAyKAGUwZ3mVKmcamcJnMW26MRPgUw6j+LkhyHGVGYjSUUKNpuJUQoOIAyDvEyG8S5yfK6dhZc0Tx1KI/gviKL6qvvFs1+bWtaz58uUNnryq6kt5RzOCkPWlVqVX2a/EEBUdU1KrXLf40GoiiFXK///qpoiDXrOgqDR38JB0bw7SoL+ZB9o1RCkQjQ2CBYZKd/+VJxZRRZlqSkKiws0WFxUyCwsKiMy7hUVFhIaCrNQsKkTIsLivwKKigsj8XYlwt/WKi2N4d//uQRCSAAjURNIHpMZBGYiaQPSYyAAABLAAAAAAAACWAAAAApUF/Mg+0aohSIRobBAsMlO//Kk4soosy1JSFRYWaLC4qZBYWFRGZdwqKiwkNBVmoWFSJkWFxX4FFRQWR+LsS4W/rFRb/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////VEFHAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAU291bmRib3kuZGUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMjAwNGh0dHA6Ly93d3cuc291bmRib3kuZGUAAAAAAAAAACU=");


    if (!app.actions['startLibrary']) {
        app.actions['startLibrary'] = [];
    }

    app.actions['startLibrary'].push({

        open: function (parameters) {

            var used = $("body").height() + 50;
            $("#libraryBorrowerList").height($(window).height() - used - 20);

            $("#libraryBorrowedBooksList").height($(window).height() - used - 410);
            $("#libraryNeededBooksList").height($(window).height() - used - 410);


            initializeBorrowTables();


            initializeDOM();

            // fetchData();
            $('#libraryBorrowTab').trigger('shown.bs.tab');


        },
        close: function () {
            w2ui['libraryBorrowerList'].destroy();
            w2ui['libraryBorrowedBooksList'].destroy();
            w2ui['libraryNeededBooksList'].destroy();
            bookCopyDetailsData = undefined;

            $('html').unbind('click',backgroundClickHandler);
        }
    });

    function backgroundClickHandler(e){
        // alert(e.target);
        // alert(e.target.tagName);

        var classes = $(e.target).attr('class');
        var isMargin = false;
        if (!(typeof classes === "undefined")) {
            isMargin = classes.indexOf("col-md-") >= 0 || classes.indexOf("form-inline") >= 0 || classes.indexOf("nav-tabs") >= 0 || classes.indexOf("navbar-collapse") >= 0;
        }
        if (e.target.tagName === 'HTML' || isMargin) {
            $('#libraryBorrowBarcodeField').focus();
        }

    }


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

                libraryBorrowsOnSelectUnselect();

                showBookCopyDetails();

                return true;
            });

        }, "json");

    }

    function initializeDOM() {

        $('#libraryBorrowHoliday').change(function () {
            $('#libraryBorrowBarcodeField').focus();
        });

        $('#libraryBorrowNextSchoolyear').change(function () {
            $('#libraryBorrowBarcodeField').focus();
        });

        $('#bookDetailDate').w2field('date', {format: 'dd.mm.yyyy'});


        $('#bookDetailButton').click(function (event) {
            addBookDetail();
            $('#libraryBorrowBarcodeField').focus();
        });


        $("#bookDetailMarkUL li a").click(function () {
            $(this).parents(".dropup").find('.btn').html($(this).text() + ' <span class="caret"></span>');
            $(this).parents(".dropup").find('.btn').val($(this).data('value'));
        });

        $('#libraryBorrowNextSchoolyear').click(function () {
            libraryBorrowUpdateTables();
        });

        $('#libraryBorrowTab').on('shown.bs.tab', function (e) {

            if (w2ui['libraryBorrowerList'].records.length === 0) {

                fetchData();
                $("#libraryBorrowErrorList").hide();

                w2ui['libraryBorrowerList'].resize();
                w2ui['libraryBorrowedBooksList'].resize();
                w2ui['libraryNeededBooksList'].resize();

                libraryBorrowsOnSelectUnselect();

                //bookCopyDetailsData = undefined;
                showBookCopyDetails();

            }

            $('html').bind('click',backgroundClickHandler);
            $('#libraryBorrowBarcodeField').focus();


        });

        $('#libraryBorrowTab').on('hide.bs.tab', function (e) {

            $('html').unbind('click',backgroundClickHandler);

        });



        var searchAll = $('#libraryBorrowerList').find('.w2ui-search-all');

        searchAll.keyup(function (event) {

            searchAll.blur();
            this.onchange();
            searchAll.focus();

        });


        $('#libraryBorrowBarcodeField').keypress(function (event) {

            if (event.which == 13) {

                var barcode = $('#libraryBorrowBarcodeField').val();

                while (barcode.length < 13) {
                    barcode = "0" + barcode;
                }

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
                            school_id: global_school_id,
                            over_holidays: $('#libraryBorrowHoliday').is(':checked')
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
                                        over_holidays: $('#libraryBorrowHoliday').is(':checked')
                                    }
                                );

                                libraryBorrowUpdateTables();
                                borrowedGrid.selectNone();
                                borrowedGrid.select(data.borrows_id);

                            } else if (data.status == "error") {
                                // w2alert("Fehler beim Speichern der Daten: " + data.message);
                                var dt = new Date();
                                var time = dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();

                                var errorlist = $("#libraryBorrowErrorList");
                                errorlist.show();
                                var text = errorlist.html();
                                text = time + ": " + data.message + "<br />" + text;
                                errorlist.html(text);
                                errorBeep.play();

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
            multiSelect: false,
            postData: {school_id: global_school_id, school_term_id: global_school_term_id},
            show: {
                header: false,
                toolbar: true,
                selectColumn: false,
                toolbarAdd: true,
                toolbarEdit: true,
                toolbarDelete: false,
                toolbarSave: false,
                footer: true
            },
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true},
                {field: 'class_name', caption: 'Klasse', size: '50px', sortable: true, resizable: true},
                {field: 'name', caption: 'Name', size: '180px', sortable: true, resizable: true},
                {field: 'religion', caption: 'Rel.-U.', size: '70px', sortable: true, resizable: true},
                {field: 'languages', caption: 'Sprachen', size: '70px', sortable: true, resizable: true},
                {field: 'curriculum_name', caption: 'AR', size: '70px', sortable: true, resizable: true}
            ],
            searches: [
                {field: 'class_name', caption: 'Klasse', type: 'text'},
                {field: 'name', caption: 'Name', type: 'text'},
                {field: 'languages', caption: 'Sprachenfolge', type: 'text'},
                {field: 'curriculum_name', caption: 'Ausbildungsrichtung', type: 'text'}
            ],
            sortData: [{field: 'class_name', direction: 'asc'}, {
                field: 'curriculum_name',
                direction: 'asc'
            }, {field: 'name', direction: 'asc'}],

            onAdd: function (event) {

                openAddEditStudentDialog(null);

            },

            onEdit: function (event) {

                if (this.getSelection(false).length === 1) {

                    var borrower = this.get(this.getSelection(false)[0])

                    openAddEditStudentDialog(borrower);

                }

            },

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
                toolbar: false,
                selectColumn: false,
                toolbarAdd: false,
                toolbarDelete: false,
                toolbarSave: false,
                toolbarReload: false,
                toolbarColumns: false,
                footer: false
            },
            multiSelect: false,
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
                    if (target === "returnBooksButton") {

                        var selectedItems = w2ui['libraryBorrowedBooksList'].getSelection();
                        if (selectedItems.length > 0) {
                            returnBooks(selectedItems);
                        }
                    }
                }
            },
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true, render: 'int'},
                {
                    field: 'title', caption: 'Titel', size: '200px', sortable: true, resizable: true,
                    render: function (record) {
                        var neededBookRecords = w2ui['libraryNeededBooksList'].records;
                        var needed = false;

                        for (var i = 0; i < neededBookRecords.length; i++) {
                            var nbr = neededBookRecords[i];
                            if (nbr.id === record.book_id) {
                                needed = true;
                                break;
                            }
                        }

                        if (needed) {
                            return '<div>' + record.title + '</div>';
                        } else {
                            return '<div style="color:#ff2020;font-weight: bold">' + record.title + '</div>';
                        }


                    }
                },
                {field: 'author', caption: 'Autor', size: '50px', hidden: true, sortable: true, resizable: true},
                {field: 'subject', caption: 'F', size: '30px', sortable: true, resizable: true},
                {field: 'barcode', caption: 'Barcode', size: '120px', sortable: true, resizable: true},
                {
                    field: 'over_holidays', caption: 'FA', size: '20px', sortable: true, resizable: true,
                    editable: {type: 'check'}
                }

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

                    if (selection.length == 1) {
                        var record = this.get(selection[0]);
                        getBookCopyRemarks(record);
                    } else {
                        bookCopyDetailsRecord = undefined;
                        bookCopyDetailsData = undefined;
                        bookCopyDetailsName = undefined;
                        showBookCopyDetails();
                    }
                }

                $('#libraryBorrowBarcodeField').focus();

            },
            onUnselect: function (event) {
                this.onSelect(event);
            },
            onChange: function (event) {

                var change = {
                    recid: event.recid
                };

                var id = w2ui[event.target].records[event.index].borrows_id;

                change[this.columns[event.column].field] = event.value_new;

                var updateRequest = {
                    cmd: "update",
                    school_id: global_school_id,
                    id: id,
                    field: this.columns[event.column].field,
                    value_new: event.value_new,
                    changes: [change]
                };

                commitW2GridChanges(this, updateRequest, "/library/borrowedBooks/updateHoliday");

                $('#libraryBorrowBarcodeField').focus();

            }
        });


        $('#libraryNeededBooksList').w2grid({
            name: 'libraryNeededBooksList',
            header: 'Benötigte Bücher',
            //url		: 'library/inventoryBooks',
            buffered: 2000,
            recid: 'id',
            multiSelect: false,
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
                {
                    field: 'status', caption: 'Status', size: '60px', sortable: true, resizable: true,
                    render: function (record) {
                        if (record.status === 'OK') {
                            return '<img src="/public/img/green_check_mark.png" style="height: 1em"/>';
                        } else {
                            return '<div style="color:#ff2020; font-weight:bold">Fehlt</div>';
                        }
                    }
                }

            ],
            sortData: [{field: 'subject', direction: 'asc'}, {
                field: 'title',
                direction: 'asc'
            }],

            onClick: function (event) {

                $('#libraryBorrowBarcodeField').focus();

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
                if (records.length > 0) {
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

        // Switch focus to barcode field
        $('#libraryBorrowBarcodeField').focus();

    }

    function libraryBorrowUpdateTables() {

        var borrowerGrid = w2ui['libraryBorrowerList'];
        var borrowedBooksGrid = w2ui['libraryBorrowedBooksList'];
        var neededGrid = w2ui['libraryNeededBooksList'];

        var selectedBorrowerList = borrowerGrid.getSelection(false); // id of borrower

        var benoetigtUndGeliehen = 0;

        if (selectedBorrowerList.length === 1) {
            var selectedBorrowerId = selectedBorrowerList[0];
            var selectedBorrower = borrowerGrid.get(selectedBorrowerId);

            neededGrid.clear();

            if (selectedBorrower.isStudent) {
                bookFormStore.forEach(function (bfsr) {
                    if (bookNeeded(selectedBorrower, bfsr)) {

                        var status = "fehlt";

                        borrowedBooksGrid.records.forEach(function (borrowedBook) {
                            if (borrowedBook.book_id == bfsr.book_id) {
                                status = "OK";
                                benoetigtUndGeliehen++;
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

        var geliehen = borrowedBooksGrid.records.length;
        var benoetigt = neededGrid.records.length;
        
        borrowedBooksGrid.header = '' + geliehen + ' geliehene Bücher, davon <span style="color:green; font-weight:bold">' +
            benoetigtUndGeliehen + '</span> benötgt, <span style="color:#ff2020; font-weight:bold">' +
            (geliehen - benoetigtUndGeliehen) + '</span> zusätzlich';

        var color = (benoetigt - benoetigtUndGeliehen) === 0 ? "green" : "#ff2020";
        
        neededGrid.header = '' + benoetigt + ' benötigt, davon <span style="color:' + color + '; font-weight:bold">' +
            (benoetigt - benoetigtUndGeliehen) + '</span> noch nicht entliehen.';
        
        /**
         * Borrowed books which are not needed are rendered in red color. This is only
         * possible if neededGrid is rendered beforehand.
         */
        borrowedBooksGrid.refresh();
        neededGrid.refresh();

    }

    function bookNeeded(selectedBorrower, bookFormStoreRecord) {

        var bookNeededForNextTerm = $('#libraryBorrowNextSchoolyear').is(':checked');

        var religionList = app.globalDefinitions().religionList;

        var form_id, curriculum_id, languageskills, religion_id, year_of_school;

        if (bookNeededForNextTerm) {
            form_id = selectedBorrower.nst_form_id;
            curriculum_id = selectedBorrower.nst_curriculum_id;
            languageskills = selectedBorrower.nst_languageskills;
            religion_id = selectedBorrower.nst_religion_id;
            year_of_school = selectedBorrower.nst_year_of_school;
        } else {
            form_id = selectedBorrower.form_id;
            curriculum_id = selectedBorrower.curriculum_id;
            languageskills = selectedBorrower.languageskills;
            religion_id = selectedBorrower.religion_id;
            year_of_school = selectedBorrower.year_of_school;
        }


        for (var i = 0; i < bookFormStoreRecord.bookFormEntries.length; i++) {

            var entry = bookFormStoreRecord.bookFormEntries[i];

            var bookNeeded = true;

            if (entry.form_id) {
                if (form_id !== entry.form_id) {
                    bookNeeded = false;
                }
            }

            if (entry.curriculum_id && bookNeeded) {
                if (curriculum_id !== entry.curriculum_id) {
                    bookNeeded = false;
                }
            }

            if (entry.languageyear && bookNeeded) {

                if (bookFormStoreRecord.subject_id) {
                    var languageSkillFound = false;
                    languageskills.forEach(function (ls) {
                        if (ls.subject_id === bookFormStoreRecord.subject_id) {

                            if (typeof ls.from_year === "undefined" || year_of_school - ls.from_year + 1 === entry.languageyear) {
                                if (typeof ls.to_year === "undefined" || year_of_school <= ls.to_year) {
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
                var isReligionBook = false;
                // Religion?
                for (var r = 0; r < religionList.length; r++) {
                    if (religionList[r].id === bookFormStoreRecord.subject_id) {
                        isReligionBook = true;
                        break;
                    }
                }

                if (isReligionBook) {
                    if (religion_id !== bookFormStoreRecord.subject_id) {
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
            , "/library/borrowedBooks/delete",
            function () {
                libraryBorrowUpdateTables();
            }
            , message);

    }

    function getBookCopyRemarks(record) {

        $.post('/library/bookCopyStatus/get', JSON.stringify({
            barcode: record.barcode,
            school_id: global_school_id
        }), function (data) {

            bookCopyDetailsData = data;
            bookCopyDetailsRecord = record;

            showBookCopyDetails();

        }, "json");

    }

    function showBookCopyDetails() {

        var out = '<ul class="list-group" style="margin-bottom: 0">\n';

        if (bookCopyDetailsData) {

            var lastMark = "Zustand als Note";

            if (bookCopyDetailsData.length == 0) {
                out += '<li class="list-group-item">Keine Einträge</li>';
            }

            bookCopyDetailsData.statusList.forEach(function (record) {
                out += '<li class="list-group-item"><span style="color: blue">' + record.statusdate + ':</span> ';

                if (record.event == "borrow") {
                    out += "<b>Entliehen</b> an <b style='color: forestgreen'>" + record.borrowername + "</b> durch " + record.username + "</div>";
                }

                if (record.event == "return") {
                    out += "<b>Rückgabe</b>; Buch angenommen von " + record.username + "</div>";
                }

                if (record.event == "examine") {
                    var badgeHtml = '';
                    if (record.mark && record.mark.length > 0) {
                        badgeHtml = '<span class="badge">' + record.mark.substr(0, 1) + '</span>';
                    }
                    out += "<b>Begutachtet</b> durch " + record.username + badgeHtml + '</div>';
                    if (record.evidence) {
                        out += "<div style='white-space: pre'>" + Handlebars.Utils.escapeExpression(record.evidence) + "</div>";
                    }
                }

                if (record.mark) {
                    lastMark = record.mark;
                }

                out += "</li>";
            });

            out += "</ul>\n"

            $('#bookDetailButton').prop("disabled", false);
            $('#bookDetailDate').prop("disabled", false);
            $('#bookDetailText').prop("disabled", false);
            $('#bookDetailMark').removeClass("disabled");

            $('#bookDetailMark').val(lastMark);
            $('#bookDetailMark').text(lastMark);
            $('#bookTitle').text(' "' + bookCopyDetailsRecord.title + '"');

        } else {
            $('#bookDetailButton').prop("disabled", true);
            $('#bookDetailDate').prop("disabled", true);
            $('#bookDetailText').prop("disabled", true);
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

    function addBookDetail() {

        if (bookCopyDetailsRecord) {

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


    /**
     * Dialog for adding new Students
     */
    function openAddEditStudentDialog(record) {

        var student_id = null;
        var student_school_term_id = null;

        if (record !== null) {

            student_id = record.student_id;
            student_school_term_id = record.student_school_term_id;
        }

        if (!w2ui.libraryAddStudentDialog) {
            $().w2form({
                name: 'libraryAddStudentDialog',
                style: 'border: 0px; background-color: transparent;',
                url: '/library/students/save',
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