// JS file for library module


(function (app) {

    if (!app.actions['startLibrary']) {
        app.actions['startLibrary'] = [];
    }

    app.actions['startLibrary'].push({

        open: function (parameters) {

            initializeReturnTables();
            initializeDOM();

        },
        close: function () {
            w2ui['libraryReturnerList'].destroy();
            w2ui['libraryReturnBooksList'].destroy();
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
            $('#libraryReturnBarcodeField').focus();
        }

    }


    var bookCopyDetailsData;
    var bookCopyDetailsRecord;
    var bookCopyDetailsName;
    var bookCopyDetailsReturnerRecord;
    var feeData = [];
    var currentFeeId;


    function fetchData() {

        var gridObj = w2ui['libraryReturnerList'];

        gridObj.load('/library/returnerList/get', function () {

            if (typeof gridObj.records === "object" && gridObj.records.length > 0) {
                gridObj.select(gridObj.records[0]);
            }

            return true;
        });

    }


    function initializeDOM() {

        /**
         * This function is called each time the user selects "Return"-Tab.
         */
        $('#libraryReturnTab').on('shown.bs.tab', function (e) {

            var returner = w2ui['libraryReturnerList'];
            returner.resize();
            w2ui['libraryReturnBooksList'].resize();

            if (returner.records.length === 0) {
                fetchData();
                showBookCopyDetails();
                showFeeDetails();
            }

            $('#libraryReturnBarcodeField').focus();

            $('html').bind('click',backgroundClickHandler);

            onSelectUnselectReturner();


        });

        $('#libraryReturnTab').on('hide.bs.tab', function (e) {
            $('html').unbind('click',backgroundClickHandler);
        });

            $('#bookReturnDetailDate').w2field('date', {format: 'dd.mm.yyyy'});


        $('#bookReturnDetailButton').click(function (event) {
            addBookDetail();
            $('#libraryReturnBarcodeField').focus();
        });

        $('#bookReturnUpdatePaymentButton').click(function (event) {
            addOrUpdatePayment();
            $('#libraryReturnBarcodeField').focus();
        });

        $('#bookReturnAmount').keypress(function (event) {

            if (event.which === 13) {
                addOrUpdatePayment();
                $('#libraryReturnBarcodeField').focus();
            }

        });

        $('#bookReturnDeletePaymentButton').click(function (event) {
            deletePayment();
            $('#libraryReturnBarcodeField').focus();
        });

        $('#bookReturnPaymentDoneButton').click(function (event) {
            paymentDone();
            $('#libraryReturnBarcodeField').focus();
        });


        $("#bookReturnDetailMarkUL li a").click(function () {
            $(this).parents(".dropdown").find('.btn').html($(this).text() + ' <span class="caret"></span>');
            $(this).parents(".dropdown").find('.btn').val($(this).data('value'));
        });


        var searchAll = $('#libraryReturnerList').find('.w2ui-search-all');

        searchAll.keyup(function (event) {
            searchAll.blur();
            this.onchange();
            searchAll.focus();
        });


        $('#libraryReturnBarcodeField').keypress(function (event) {

            if (event.which == 13) {

                w2ui['libraryReturnerList'].searchReset();

                var barcode = $('#libraryReturnBarcodeField').val();

                var returnerGrid = w2ui['libraryReturnerList'];
                var booksGrid = w2ui['libraryReturnBooksList'];

                showUpdateMessage(booksGrid);

                $.post("/library/returnBook", JSON.stringify(
                    {
                        barcode: barcode,
                        performReturn: true,
                        school_id: global_school_id
                    }),
                    function (data) {

                        hideUpdateMessage(booksGrid);

                        if (data.status == "success") {
                            beep_ok();
                            returnerGrid.selectNone();
                            printLog(data, barcode);

                            bookCopyDetailsRecord = data; // Java-Type: BookReturnResponse
                            bookCopyDetailsRecord['id'] = data.book_copy_id;
                            bookCopyDetailsRecord.barcode = barcode;

                            for (var i = 0; i < returnerGrid.records.length; i++) {
                                var returner = returnerGrid.records[i];
                                if ((returner.isStudent && returner.student_id === data.student_id)
                                    || (returner.isTeacher && returner.teacher_id === data.teacher_id)
                                ) {
                                    bookCopyDetailsReturnerRecord = returner;
                                    returner.numberOfBorrowedBooks--;
                                    bookCopyDetailsName = returner.surname + ", " + returner.firstName;
                                    var onSelect = returnerGrid.onSelect;
                                    returnerGrid.onSelect = undefined;
                                    returnerGrid.select(returner.id);
                                    returnerGrid.scrollIntoView();
                                    returnerGrid.onSelect = onSelect;
                                    $('#bookReturnPaymentsHeader').html("Ausstehene Zahlungen von " + returner.name + ":");
                                    break;
                                }
                            }

                            returnerGrid.refresh();
                            bookCopyDetailsData = data.statusList;
                            feeData = data.feeList;

                            booksGrid.clear();

                            booksGrid.add(data.borrowedBooksList);

                            booksGrid.refresh();

                            showBookCopyDetails();
                            showFeeDetails();

                        } else if (data.status == "error") {
                            beep_error();
                            w2alert("Fehler beim Speichern der Daten: " + data.message);
                        }
                    },
                    "json"
                );

                $('#libraryReturnBarcodeField').val('');

                event.stopPropagation();

                return false;
            }
        });


        $('#libraryReturnBarcodeField').focusin(function (event) {

            $('body').css('backgroundColor', '#eeffee');

        });

        $('#libraryReturnBarcodeField').focusout(function (event) {

            $('body').css('backgroundColor', '#ffffff');

        });


    }


    function printLog(data, barcode){
        //data.title
        //data.student_firstname
        //data.student_surname
        //barcode
        //data.subject ('D')
        var html = '<div><span style="font-weight: bold">'
            + data.student_firstname + " " + data.student_surname + ": </span>";
        html += '<span style="color: blue">' + barcode + '</span>';
        html += '<div style="margin-bottom: 5px">(<span style="font-weight: bold">' + data.subject + '</span>: ' + data.title + ')' + '</div>';
        html += '</div>';

        $('#libraryReturnLog').append(html)
            .animate({scrollTop: $('#libraryReturnLog').prop("scrollHeight")}, 500);
    }

    function initializeReturnTables() {

        $('#libraryReturnerList').w2grid({
            name: 'libraryReturnerList',
            header: 'Ausleiher/innen',
            //url		: 'library/inventoryBooks',
            buffered: 2000,
            recid: 'id',
            postData: {school_id: global_school_id, school_term_id: global_school_term_id},
            multiselect: false,
            show: {
                header: false,
                toolbar: true,
                selectColumn: false,
                toolbarAdd: false,
                toolbarDelete: false,
                toolbarSave: false,
                footer: true
            },
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true},
                {field: 'class_name', caption: 'Klasse', size: '50px', sortable: true, resizable: true},
                {field: 'name', caption: 'Name', size: '180px', sortable: true, resizable: true},
                {
                    field: 'numberOfBorrowedBooks',
                    caption: 'offen',
                    size: '70px',
                    sortable: true,
                    resizable: true,
                    render: 'int'
                }
            ],
            searches: [
                {field: 'class_name', caption: 'Klasse', type: 'text'},
                {field: 'name', caption: 'Name', type: 'text'}
            ],
            sortData: [{field: 'class_name', direction: 'asc'}, {field: 'name', direction: 'asc'}],

            onSelect: function (event) {

                event.onComplete = onSelectUnselectReturner;

                $('#libraryReturnBarcodeField').focus();
            },

            onUnselect: function (event) {

                event.onComplete = onSelectUnselectReturner;

            }

        });


        $('#libraryReturnBooksList').w2grid({
            name: 'libraryReturnBooksList',
            header: 'Geliehene Bücher',
            //url		: 'library/inventoryBooks',
            buffered: 200,
            recid: 'id',
            postData: {
                school_id: global_school_id,
                reference_id: 999,
                reference_type: "student"
            }, // postData is set in onSelectUnselectBorrower
            show: {
                header: true,
                toolbar: true,
                selectColumn: true,
                toolbarAdd: false,
                toolbarDelete: false,
                toolbarSave: false,
                toolbarColumns: false,
                toolbarSearch: false,
                toolbarReload: false,
                footer: true
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

                        var selectedItems = w2ui['libraryReturnBooksList'].getSelection();
                        if (selectedItems.length > 0) {
                            returnBooks(selectedItems);
                        }
                    }
                }
            },
            columns: [
                {field: 'book_copy_id', caption: 'ID', size: '30px', hidden: true, sortable: true, render: 'int'},
                {field: 'title', caption: 'Titel', size: '200px', sortable: true, resizable: true},
                {field: 'author', caption: 'Autor', size: '50px', hidden: true, sortable: true, resizable: true},
                {field: 'subject', caption: 'F', size: '30px', sortable: true, resizable: true},
                {field: 'barcode', caption: 'Barcode', size: '120px', sortable: true, resizable: true},
                {
                    field: 'begindate',
                    caption: 'Ausleihdatum',
                    size: '100px',
                    sortable: true,
                    resizable: true
//                    render: 'date:dd.MM.yyyy'
                },
                {
                    field: 'return_date',
                    caption: 'Rückgabe',
                    size: '80px',
                    sortable: true,
                    resizable: true
//                    render: 'date:dd.MM.yyyy'
                }

            ],
            sortData: [{field: 'subject', direction: 'asc'},
                {field: 'title', direction: 'asc'}],

            onClick: function (event) {

                //var record = this.get(event.recid);

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
                        bookCopyDetailsRecord = record;
                        getAndShowBookDetails(record);
                    } else {
                        bookCopyDetailsRecord = undefined;
                        bookCopyDetailsData = undefined;
                        bookCopyDetailsName = undefined;
                        showBookCopyDetails();
                        showFeeDetails();
                    }

                    $('#libraryReturnBarcodeField').focus();

                }
            },
            onUnselect: function (event) {
                this.onSelect(event);
            }

        });


    }

    function onSelectUnselectReturner() {

        var returnerGrid = w2ui['libraryReturnerList'];
        var booksGrid = w2ui['libraryReturnBooksList'];

        var selectedReturnerList = returnerGrid.getSelection(false); // id of returner

        if (selectedReturnerList.length == 1) {

            var returner_id = selectedReturnerList[0];

            var returner = returnerGrid.get(returner_id);
            var name = returner.name;
            if (returner.isStudent) {
                name += " (Klasse " + returner.class_name + ")";
            } else {
                name += " (Lehrkraft)";
            }

            bookCopyDetailsName = name;
            bookCopyDetailsReturnerRecord = returner;

            $('#bookReturnPaymentsHeader').html("Zahlungen von " + name + ":");

            booksGrid.postData = {
                reference_id: returner.isStudent ? returner.student_id : returner.teacher_id,
                reference_type: returner.isStudent ? "student" : "teacher",
                school_id: global_school_id
            };

            booksGrid.load("/library/borrowedBooks/get", function () {

                var records = booksGrid.records;
                if (records.length > 0) {
                    booksGrid.select(records[0].id);
                } else {
                    bookCopyDetailsData = undefined;
                    showBookCopyDetails();
                }

                returner.numberOfBorrowedBooks = records.length;
                returnerGrid.refresh();

            });

            getAndShowFeeDetails(returner.student_id, returner.teacher_id);

        } else {
            booksGrid.clear();
            $('#bookReturnPaymentsHeader').text("Zahlungen:");
            bookCopyDetailsData = undefined;
            bookCopyDetailsReturnerRecord = undefined;
            feeData = [];
            showBookCopyDetails();
            showFeeDetails();
        }
    }


    function returnBooks(selectedItems) {

        var borrows_ids = [];
        var logData = [];
        var returner = getSelectedReturner();

        selectedItems.forEach(function (si) {
            var record = w2ui['libraryReturnBooksList'].get(si);
            borrows_ids.push(record.borrows_id);

            logData.push({
                barcode: record.barcode,
                title: record.title,
                student_firstname:returner.name,
                student_surname: '',
                subject:record.subject
            });

        });

        var message = "Soll die Rückgabe der " + selectedItems.length + " Bücher gebucht werden?";

        commitW2GridDelete(w2ui['libraryReturnBooksList'],
            {
                cmd: "delete",
                selected: borrows_ids,
                school_id: global_school_id
            }
            , "/library/borrowedBooks/delete", function () {

                var returner = getSelectedReturner();

                if (returner) {
                    returner.numberOfBorrowedBooks--;
                    w2ui['libraryReturnerList'].refresh();
                }

                logData.forEach(function(ld){
                   printLog(ld, ld.barcode);
                });

            }, message);

    }

    function getSelectedReturner() {
        var returnerGrid = w2ui['libraryReturnerList'];

        var selectedReturnerList = returnerGrid.getSelection(false); // id of returner

        if (selectedReturnerList.length == 1) {

            var returner_id = selectedReturnerList[0];

            return returnerGrid.get(returner_id);
        }

        return undefined;
    }

    function getAndShowFeeDetails(student_id, teacher_id) {
        $.post('/library/fee/get', JSON.stringify({
            student_id: student_id,
            teacher_id: teacher_id,
            school_id: global_school_id,
            onlyUnpaidFees: true
        }), function (data) {

            feeData = data;

            showFeeDetails();

        }, "json");
    }

    function showFeeDetails() {
        var out = '<ul class="list-group" style="margin-bottom: 0">\n';

        var sum = 0;

        currentFeeId = undefined;
        $('#bookReturnAmount').val('');

        if (feeData) { // 16.04.17: && bookCopyDetailsData entfernt

            var i = 1;

            feeData.forEach(function (record) {
                out += '<li class="list-group-item"><a href = "#" id="feeDetail' + i++ + '"><span style="color: blue">' + record.title + ' (Barcode: ' + record.barcode + ')</span> ';

                var amount = parseFloat(("" + record.amount).replace(",", "."));
                amount = amount.toFixed(2).replace(".", ",");

                out += '<span style="font-weight: bold; color: black">' + amount + " €</span></a>";

                if (record.remarks) {
                    out += "<div style='white-space: pre'>" + Handlebars.Utils.escapeExpression(record.remarks) + "</div>";
                }

                out += "</li>";

                if (bookCopyDetailsRecord) {
                    if (record.book_copy_id == bookCopyDetailsRecord.id) {
                        $('#bookReturnAmount').val(amount + " €");
                        currentFeeId = record.fee_id;
                    }
                }

                sum += record.amount;
            });

            out += "</ul>\n";

            $('#bookReturnPaymentsList').html(out);

            i = 1;

            feeData.forEach(function (record) {

                $('#feeDetail' + i++).click(function () {
                    getAndShowBookDetailsFromBarcode(record.barcode, record.borrows_id);
                });

            });


            $('#bookReturnUpdatePaymentButton').prop("disabled", false);
            $('#bookReturnDeletePaymentButton').prop("disabled", false);
            $('#bookReturnPaymentDoneButton').prop("disabled", false);
            $('#bookReturnAmount').prop("disabled", false);

            $('#bookReturnSumAmount').val(sum.toFixed(2).replace(".", ",") + " €");

        } else {
            $('#bookReturnUpdatePaymentButton').prop("disabled", true);
            $('#bookReturnDeletePaymentButton').prop("disabled", true);
            $('#bookReturnPaymentDoneButton').prop("disabled", true);
            $('#bookReturnAmount').prop("disabled", true);

            $('#bookReturnSumAmount').val('');

            out += '<li class="list-group-item">Keine Einträge</li>';

            $('#bookReturnPaymentsList').html(out);

        }


        var today = w2utils.formatDate((new Date()), 'dd.MM.yyyy');
        $('#bookReturnDetailDate').val(today);

    }


    function getAndShowBookDetails(record) {
        getAndShowBookDetailsFromBarcode(record.barcode, null);
    }

    getAndShowBookDetailsFromBarcode = function (barcode, borrows_id) {

        $.post('/library/bookCopyStatus/get', JSON.stringify({
            barcode: barcode,
            school_id: global_school_id
        }), function (data) {

            bookCopyDetailsData = data.statusList;
            bookCopyDetailsRecord = data.returnBookResponse; // vor 16.04.17: = record;
            bookCopyDetailsRecord.barcode = barcode;
            bookCopyDetailsRecord.id = bookCopyDetailsRecord.book_copy_id;
            if (borrows_id != null) {
                bookCopyDetailsRecord.borrows_id = borrows_id;
            }

            showBookCopyDetails();
            showFeeDetails();


        }, "json");

    }


    function showBookCopyDetails() {

        var out = '<ul class="list-group" style="margin-bottom: 0">\n';

        if (bookCopyDetailsData) {

            var lastMark = "Zustand als Note";

            if (bookCopyDetailsData.length == 0) {
                out += '<li class="list-group-item">Keine Einträge</li>';
            }

            bookCopyDetailsData.forEach(function (record) {
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

            $('#bookReturnDetailButton').prop("disabled", false);
            $('#bookReturnDetailDate').prop("disabled", false);
            $('#bookReturnDetailText').prop("disabled", false);
            $('#bookReturnDetailMark').removeClass("disabled");

            $('#bookReturnDetailMark').val(lastMark);
            $('#bookReturnDetailMark').text(lastMark);
            $('#bookReturnTitle').html('Zustand des Buchs <span style="font-weight: bold">' + bookCopyDetailsRecord.title + " </span><span>(Barcode " + bookCopyDetailsRecord.barcode + ")" + "</span>");

        } else {
            $('#bookReturnDetailButton').prop("disabled", true);
            $('#bookReturnDetailDate').prop("disabled", true);
            $('#bookReturnDetailText').prop("disabled", true);
            $('#bookReturnDetailMark').addClass("disabled");
            $('#bookReturnDetailText').val("");
            $('#bookReturnDetailMark').val("");
            $('#bookReturnDetailMark').text("Zustand als Note");
            $('#bookReturnTitle').html('Zustand');
        }


        var today = w2utils.formatDate((new Date()), 'dd.MM.yyyy');
        $('#bookReturnDetailDate').val(today);

        $('#bookReturnDetailTextAll').html(out);
    }

    function addBookDetail() {

        if (bookCopyDetailsRecord) {

            var mark = $('#bookReturnDetailMark').val();

            if (mark.indexOf("Note") > 0) {
                mark = null;
            }

            var statusdate = $('#bookReturnDetailDate').val();

            var bookDetail = {
                evidence: $('#bookReturnDetailText').val(),
                event: "examine",
                mark: mark,
                borrowername: bookCopyDetailsName,
                username: App.globalDefinitions().username,
                book_copy_id: bookCopyDetailsRecord.id,
                statusdate: statusdate,
                school_id: global_school_id
            };


            $.post('/library/bookCopyStatus/save', JSON.stringify(bookDetail), function (data) {

                if (data.status == "success") {

                    bookCopyDetailsData.push(bookDetail);

                    showBookCopyDetails();

                } else {
                    w2alert(data.message, "Fehler beim Speichern der Bewertung");
                }

            }, "json");

        }
    }


    function deletePayment() {

        if (currentFeeId) {

            var request = {
                school_id: global_school_id,
                fee_id: currentFeeId
            };

            $.post('/library/fee/delete', JSON.stringify(request), function (data) {

                if (data.status == "success") {

                    for (var i = 0; i < feeData.length; i++) {
                        if (currentFeeId == feeData[i].fee_id) {
                            feeData.splice(i, 1);
                            break;
                        }
                    }

                    currentFeeId = undefined;
                    showFeeDetails();

                } else {
                    w2alert(data.message, "Fehler beim Speichern der Zahlung");
                }

            }, "json");
        }
    }

    function addOrUpdatePayment() {
        var amount = $('#bookReturnAmount').val();
        amount = amount.match(/^([0-9]+([\,|\.][0-9][0-9]?)?) ?\u20AC?$/)[1]; // strip optional " €" at the end
        amount = parseFloat(amount.replace(",", "."));

        var borrows_id = bookCopyDetailsRecord.borrows_id;

        var request = {
            amount: amount,
            borrows_id: borrows_id,
            paid_date: null,
            remarks: "",
            school_id: global_school_id
        };

        var command = "save";

        if (currentFeeId) {
            command = "update";
            request.fee_id = currentFeeId;
        }

        $.post('/library/fee/' + command, JSON.stringify(request), function (data) {

            if (data.status == "success") {

                if (currentFeeId) {
                    for (var i = 0; i < feeData.length; i++) {
                        if (currentFeeId == feeData[i].fee_id) {
                            feeData[i].amount = request.amount;
                            feeData[i].borrows_id = request.borrows_id;
                            feeData[i].paid_date = request.paid_date;
                            feeData[i].remarks = request.remarks;
                            break;
                        }
                    }
                } else {
                    request['fee_id'] = data.fee_id;
                    request['title'] = bookCopyDetailsRecord.title;
                    request['author'] = bookCopyDetailsRecord.author;
                    request['book_id'] = bookCopyDetailsRecord.book_id;
                    request['book_copy_id'] = bookCopyDetailsRecord.id;
                    request['barcode'] = bookCopyDetailsRecord.barcode;
                    feeData.push(request);
                    currentFeeId = request.fee_id;
                }

                showFeeDetails();

            } else {
                w2alert(data.message, "Fehler beim Speichern der Zahlung");
            }

        }, "json");


    }


    function paymentDone() {
        if (bookCopyDetailsReturnerRecord) {

            var request = {
                school_id: global_school_id,
                student_id: bookCopyDetailsReturnerRecord.student_id,
                teacher_id: bookCopyDetailsReturnerRecord.teacher_id
            };


            $.post('/library/fee/paymentsDone', JSON.stringify(request), function (data) {

                if (data.status == "success") {

                    feeData = [];

                    showFeeDetails();

                } else {
                    w2alert(data.message, "Fehler beim Speichern");
                }

            }, "json");

        } else {
            w2alert("Es ist in der rechten Tabelle kein Ausleiher selektiert", "Fehler");
        }
    }

}(App));