// JS file for library module


(function (app) {

    var selectedStudents = []; // students to merge

    if (!app.actions['startLibrary']) {
        app.actions['startLibrary'] = [];
    }

    app.actions['startLibrary'].push({

        open: function (parameters) {

            initializeStudentTable();

            initializeDOM();

            $('#librarySettingsTab').trigger('shown.bs.tab');


        },
        close: function () {
            w2ui['librarySettingsMergeStudents'].destroy();
        }
    });


    function fetchData() {

        var gridObj = w2ui['librarySettingsMergeStudents'];

        gridObj.load('/library/settings/getStudentList', function () {

            if (typeof gridObj.records === "object" && gridObj.records.length > 0) {
                gridObj.select(gridObj.records[0]);
            }

        });

    }

    function initializeDOM() {

        // Delete old bookings
        var deleteOldBookingsDiv = $('#ls_deleteOldBookings');

        deleteOldBookingsDiv.find(".dateinput").w2field('date', {format: 'dd.mm.yyyy'});


        deleteOldBookingsDiv.find("button").click(function (event) {
            deleteOldBookings();
        });

        // Delete resigned Students
        var deleteResignedStudentsDiv = $('#ls_deleteResignedStudents');

        deleteResignedStudentsDiv.find(".dateinput").w2field('date', {format: 'dd.mm.yyyy'});


        deleteResignedStudentsDiv.find("button").click(function (event) {
            deleteResignedStudentsTeachers(deleteResignedStudentsDiv,
                '/library/settings/deleteResignedStudents', true);
        });

        // Delete resigned Teachers
        var deleteResignedTeachersDiv = $('#ls_deleteResignedTeachers');

        deleteResignedTeachersDiv.find("button").click(function (event) {
            deleteResignedStudentsTeachers(deleteResignedTeachersDiv,
                '/library/settings/deleteResignedTeachers', false);
        });

        // Merge Students
        var mergeStudentsDiv = $('#ls_mergeStudentRecords');

        var mergeStudentsButton = mergeStudentsDiv.find('button');
        mergeStudentsButton.prop('disabled', true);

        mergeStudentsButton.click(function (event) {
            mergeStudents();
        });

        $('#ls_deleteStudent1').click(function (event) {
            librarySettingsOnSelectUnselectStudent();
        });

        $('#ls_deleteStudent2').click(function (event) {
            librarySettingsOnSelectUnselectStudent();
        });

        //
        $('#mergeStudentRecordsTab').on('shown.bs.tab', function (e) {

            fetchData();

            w2ui['librarySettingsMergeStudents'].resize();


        });

        $('#sortOutTab').on('shown.bs.tab', function (e) {

            var sortOutDiv = $('#ls_sortOut');

            var dateinput = sortOutDiv.find(".dateinput");
            dateinput.w2field('date', {format: 'dd.mm.yyyy'});

            var today = w2utils.formatDate((new Date()), 'dd.MM.yyyy');
            dateinput.val(today);

            var barcodeField = $('#librarySortOutBarcodeField');

            barcodeField.keypress(doSortOut);

            barcodeField.focusin(function (event) {

                $('body').css('backgroundColor', '#eeffee');

            });

            barcodeField.focusout(function (event) {

                $('body').css('backgroundColor', '#ffffff');

            });

            barcodeField.focus();

        });

        // Change Barcode
        $('#changeBarcodeTab').on('shown.bs.tab', function (e) {

            var changeBarcodeDiv = $('#ls_changeBarcode');

            var button = changeBarcodeDiv.find('button');

            button.click(function () {

                var oldBarcodeInput = $('#libraryChangeBarcodeOld');
                var newBarcodeInput = $('#libraryChangeBarcodeNew');

                var animatedGif = changeBarcodeDiv.find('.animated_gif');
                var alertDiv = changeBarcodeDiv.find('.alert-success');

                animatedGif.show();
                button.prop('disabled', true);

                $.post("/library/settings/changeBarcode", JSON.stringify(
                    {
                        school_id: global_school_id,
                        oldBarcode: oldBarcodeInput.val(),
                        newBarcode: newBarcodeInput.val()
                    }),
                    function (data) {

                        animatedGif.hide();
                        button.prop('disabled', false);

                        if (data.status === "success") {

                            showMessage(alertDiv, 'success', data.message);
                            oldBarcodeInput.val('');
                            newBarcodeInput.val('');

                        } else {

                            showMessage(alertDiv, 'danger', data.message);

                        }

                    }, "json"
                );


            });

        });

        // Change Barcode
        $('#bookinfoTab').on('shown.bs.tab', function (e) {

            var bookinfoDiv = $('#ls_bookinfo');

            var button = bookinfoDiv.find('button');

            button.click(function () {

                var barcodeInput = $('#librarybookinfobarcodefield');

                var animatedGif = bookinfoDiv.find('.animated_gif');
                var alertDiv = bookinfoDiv.find('.alert-success');

                animatedGif.show();
                button.prop('disabled', true);

                $.post("/library/settings/changeBarcode", JSON.stringify(
                    {
                        school_id: global_school_id,
                        barcode: barcodeInput.val()
                    }),
                    function (data) {

                        animatedGif.hide();
                        button.prop('disabled', false);

                        if (data.status === "success") {

                            showMessage(alertDiv, 'success', data.message);
                            barcodeInput.val('');
                            $('#bookinfooutput').html(processBookInfoResponse(data));

                        } else {

                            showMessage(alertDiv, 'danger', data.message);

                        }

                    }, "json"
                );


            });

        });


        var searchAll1 = $('#librarySettingsMergeStudentsNavigator').find('.w2ui-search-all');

        searchAll1.keyup(function (event) {
            this.onchange();
        });

    }


    function processBookInfoResponse(bir){




        return "";

    }

    function doSortOut(event) {
        if (event.which === 13) {

            var sortOutDiv = $('#ls_sortOut');

            var dateinput = sortOutDiv.find(".dateinput");

            var sort_out_date = dateinput.val();

            var barcode = $('#librarySortOutBarcodeField').val();

            if (barcode !== "") {

                var image = sortOutDiv.find('.animated_gif');
                image.show();

                $('#librarySortOutBarcodeField').val('');


                $.post("/library/settings/sortOut", JSON.stringify(
                    {
                        barcode: barcode,
                        school_id: global_school_id,
                        sort_out_date: sort_out_date
                    }),
                    function (data) {

                        image.hide();

                        if (data.status === "success") {

                            var alert = sortOutDiv.find('.alert');
                            alert.show();
                            var html = alert.html();
                            html = insertSigns(data.message) + "<br/>" + html;
                            alert.html(html);

                        } else {
                            w2alert("Fehler beim Aussortieren eines Buches: " + data.message);
                        }
                    },
                    "json"
                );

                event.stopPropagation();

                return false;
            }
        }

    }


    function initializeStudentTable() {

        /**
         * Table left top which shows Books
         */

        $('#librarySettingsMergeStudentsNavigator').w2grid({
            name: 'librarySettingsMergeStudents',
            header: 'Schüler/innen',
            //url		: 'library/inventoryBooks',
            buffered: 2000,
            recid: 'id',
            postData: {school_id: global_school_id, school_term_id: global_school_term_id},
            show: {
                header: false,
                toolbar: true,
                selectColumn: true,
                multiSelect: true,
                toolbarAdd: false,
                toolbarEdit: false,
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


            onSelect: function (event) {

                event.onComplete = librarySettingsOnSelectUnselectStudent;

            },

            onUnselect: function (event) {

                event.onComplete = librarySettingsOnSelectUnselectStudent;

            }

        });

    }

    function librarySettingsOnSelectUnselectStudent() {
        var grid = w2ui['librarySettingsMergeStudents'];

        var selectedStudentIds = grid.getSelection(false); // ids of students

        var button = $('#ls_mergeStudentRecords').find('button');
        button.prop('disabled', true);

        selectedStudents = [];

        var mergeStudentsTds = [$('#ls_merge_student_1'), $('#ls_merge_student_2')];

        for (var i = 0; i < 2; i++) {

            var td = mergeStudentsTds[i];

            if (i < selectedStudentIds.length) {

                var student = grid.get(selectedStudentIds[i]);
                selectedStudents.push(grid.get(selectedStudentIds[i]));

                var html = "<b>Name:</b>" + student.name + "<br />";
                html += "<b>Klasse:</b>" + student.class_name + "<br />";
                html += "<b>Sprachenfolge:</b>" + student.languages + "<br />";
                html += "<b>Religion:</b>" + student.religion + "<br />";
                html += "<b>Ausbildungsrichtung:</b>" + student.curriculum_name + "<br />";
                html += "<b>Geburtsdatum:</b>" + student.dateofbirth + "<br />";

                td.html(html);


            } else {
                selectedStudents.push(null);
                td.html('Bitte selektieren Sie zwei Schüler/innen.');
            }

        }

        if (selectedStudentIds.length === 2) {
            var deleteStudent1 = $('#ls_deleteStudent1').is(':checked');
            var deleteStudent2 = $('#ls_deleteStudent2').is(':checked');

            if (deleteStudent1 !== deleteStudent2) {
                button.prop('disabled', false);
            }
        }

    }

    function mergeStudents() {

        var mergeStudentsDiv = $('#ls_mergeStudentRecords');
        var alertDiv = mergeStudentsDiv.find('.alert');
        var button = mergeStudentsDiv.find('button');
        var animatedGif = mergeStudentsDiv.find('.animated_gif');


        if (selectedStudents.length === 2) {

            var deleteStudent1 = $('#ls_deleteStudent1').is(':checked');
            var deleteStudent2 = $('#ls_deleteStudent2').is(':checked');

            var idOfStudentToDelete = deleteStudent1 ? selectedStudents[0].id : selectedStudents[1].id;

            if (deleteStudent1 !== deleteStudent2) {

                animatedGif.show();

                $.post('/library/settings/mergeStudents', JSON.stringify(
                    {
                        school_id: global_school_id,
                        student1_id: selectedStudents[0].student_id,
                        student2_id: selectedStudents[1].student_id,
                        deleteStudent12: deleteStudent1 ? 1 : 2
                    }),
                    function (data) {

                        animatedGif.hide();
                        button.prop('disabled', false);

                        if (data.status === "success") {

                            showMessage(alertDiv, 'success', data.message);
                            var grid = w2ui['librarySettingsMergeStudents'];
                            grid.remove(idOfStudentToDelete);

                        } else {

                            showMessage(alertDiv, 'danger', data.message);

                        }

                    }, "json"
                );


            } else {
                showMessage(alertDiv, 'danger', 'Bitte selektieren Sie oben, welcher der Schülerdatensätze gelöscht werden soll.');
            }

        } else {
            showMessage(alertDiv, 'danger', 'In der Tabelle oben sind ' + selectedStudents.length + ' Schüler/innen'
                + 'selektiert. Es müssen aber genau zwei Schüler/innen selektiert sein, damit die Datensätze zusammengeführt werden können.');
        }


    }


    /**
     *
     * @param alertDiv
     *      e.g. var alertDiv = $('#ls_deleteOldBookings').find('.alert');
     * @param type
     *      one of 'success', 'info', 'warning', 'danger'
     * @param message
     */
    function showMessage(alertDiv, type, message) {

        alertDiv.attr('class', 'alert alert-' + type);

        message = insertSigns(message);

        alertDiv.html(message);

        alertDiv.show();

    }

    function insertSigns(message){

        message = message.replace(/check_mark/g,
            '<img src="/public/img/green_check_mark.png" style="height: 1em; vertical-align: middle; margin-left: 0.2em">');

        message = message.replace(/warning_sign/g,
            '<img src="/public/img/warning_sign.png" style="height: 1em; vertical-align: middle; margin-left: 0.2em">');

        return message;
    }

    function deleteOldBookings() {

        var deleteOldBookingsDiv = $('#ls_deleteOldBookings');

        var animatedGif = deleteOldBookingsDiv.find('.animated_gif');
        animatedGif.show();

        var button = deleteOldBookingsDiv.find('button');
        button.prop('disabled', true);

        var date_from = deleteOldBookingsDiv.find('.dateinput').val();

        var alertDiv = deleteOldBookingsDiv.find('.alert');

        if (date_from !== '') {
            $.post('/library/settings/deleteOldBookings', JSON.stringify(
                {
                    school_id: global_school_id,
                    date_from: date_from
                }),
                function (data) {

                    animatedGif.hide();
                    button.prop('disabled', false);

                    if (data.status === "success") {

                        showMessage(alertDiv, 'success', data.message);

                    } else {

                        showMessage(alertDiv, 'danger', data.message);

                    }

                }, "json"
            );
        } else {
            showMessage(alertDiv, 'danger', 'Das Beginndatum ist nicht gesetzt.')
            animatedGif.hide();
            button.prop('disabled', false);
        }

    }

    function deleteResignedStudentsTeachers(div, url, isStudents) {

        var deleteResignedStudentsDiv = div;

        var animatedGif = deleteResignedStudentsDiv.find('.animated_gif');
        animatedGif.show();

        var button = deleteResignedStudentsDiv.find('button');
        button.prop('disabled', true);

        var date_from = "01.01.2200";

        if (isStudents) {
            date_from = deleteResignedStudentsDiv.find('.dateinput').val();
        }

        var alertDiv = deleteResignedStudentsDiv.find('.alert');

        if (date_from !== '') {
            $.post(url, JSON.stringify(
                {
                    school_id: global_school_id,
                    date_from: date_from
                }),
                function (data) {

                    animatedGif.hide();
                    button.prop('disabled', false);

                    if (data.status === "success") {

                        showMessage(alertDiv, 'success', data.message);

                    } else {

                        showMessage(alertDiv, 'danger', data.message);

                    }

                }, "json"
            );
        } else {
            showMessage(alertDiv, 'danger', 'Das Beginndatum ist nicht gesetzt.')
            animatedGif.hide();
            button.prop('disabled', false);
        }

    }

}(App));