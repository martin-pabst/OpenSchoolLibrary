/**
 * Created by Martin on 17.04.2017.
 */

// JS file for library report module


(function (app) {

    if (!app.actions['startLibrary']) {
        app.actions['startLibrary'] = [];
    }

    app.actions['startLibrary'].push({

        open: function (parameters) {

            var used = $("body").height() + 50 + $("#entitychooser").height();
            $("#libraryReportsDataNavigator").height($(window).height() - used - 100);
            $("#reportsList").height($(window).height() - used - 100 - 60);


            initializeDOM();

            fetchData();

            //$('#libraryReportsTab').trigger('shown.bs.tab');


        },
        close: function () {
            w2ui['libraryReportsDataNavigator'].destroy();

            // TODO: set data variables = undefined

        }
    });

    var DATATYPES = {
        schueler: 1,
        lehrer: 2,
        klassen: 3,
        buecher: 4,
        other: 5
    };

    var CONTENTTYPETOICON = {
        pdf: {icon: 'pdf', color: '#800000'},
        html: {icon: 'text', color: 'black'},
        docx: {icon: 'word', color: '#000080'},
        xlsx: {icon: 'excel', color: '#008000'}
    };

    var dataType = DATATYPES.schueler;
    var reportData = {
        reports: {}
    };

    function collectSelectedData() {
        var gridObj = w2ui['libraryReportsDataNavigator'];
        //$('#reportsList .active')[0] ist das <a>-Element mit Eigenschaft data-id (z.B. "0")
        //$('#reportsList .active')[1] ist das <label>-Element mit Eigenschaft data-contenttype (z.B. "pdf")
        // $($('#reportsList .active')[0]).data() = {id: 0}
        // $($('#reportsList .active')[1]).data() = {contenttype: "pdf"}

        var selectedRows = gridObj.getSelection(false);

        if(selectedRows.length === 0){
            selectedRows = gridObj.selectAll();
            selectedRows = gridObj.getSelection(false);
        }

        var reportElement = $('#reportsList').find('a.active:first');
        var reportId = reportElement.data().id;
        var contentType = reportElement.find('label.active:first').data().contenttype;

        var requestData = {
            selectedRows: selectedRows,
            reportId: reportId,
            contentType: contentType,
            dataType: dataType,
            school_id: global_school_id,
            school_term_id: global_school_term_id
        };

        var text = JSON.stringify(requestData);
        $('#reportParameterTextarea').val(text);
        // alert(text);
    }

    function getHtmlForReport(report) {
        /*
         e.g. report = {dataType: 1, name: "Liste der \"entliehenen Bücher",
         description: "Gibt je Schüler/in die Liste der entliehenen Bücher aus", contentTypes: [pdf]}
         */

        var html = '<a href="#" class="list-group-item active" data-id="' + report.id + '">';
        html += '<span style="display: inline-block">';
        html += '<div style="font-weight: bold; color: black">' + report.name + "</div>";
        html += '<div>' + report.description + "</div>";
        html += '</span>';
        html += '<span style="display: inline-block; float: right">';
        html += '<div class="btn-group" data-toggle="buttons" >'

        for (var i = 0; i < report.contentTypes.length; i++) {
            var contentType = report.contentTypes[i];
            html += '<label class="btn btn-default' + (i === 0 ? ' active' : '') + '" data-contenttype="' + contentType + '">';
            html += '<input type="radio" autocomplete="off"' + (i === 0 ? ' checked' : '') + '">';
            html += '<i class="fa fa-file-' + CONTENTTYPETOICON[contentType].icon + '-o" style="font-size:24px; color:'
                + CONTENTTYPETOICON[contentType].color + '"></i>';
            html += '</label>'
        }

        html += '</div></span>';
        html += '</a>';

        return html;
    }

    function initReportsList() {

//    e.g. reports = {1: [{dataType: 1, name: "Liste der \"entliehenen Bücher", description: "Gibt je Schüler/in die Liste der entliehenen Bücher aus", contentTypes: [pdf]}], 2: [], 3: [], 4: [], 5: []}

        var reports = reportData.reports;

        for (var dt in reports) { // dt = 1, 2, 3, 4, 5
            if (reports.hasOwnProperty(dt)) {

                var reportList = reports[dt];

                for (var i = 0; i < reportList.length; i++) {
                    var report = reportList[i];
                    report.html = getHtmlForReport(report);
                }

            }
        }

    }

    function getReportsList() {

//    e.g. reports = {1: [{dataType: 1, name: "Liste der \"entliehenen Bücher", description: "Gibt je Schüler/in die Liste der entliehenen Bücher aus", contentTypes: [pdf]}], 2: [], 3: [], 4: [], 5: []}

        var html = '';
        var reports = reportData.reports;

        var reportList = reports[dataType];

        for (var i = 0; i < reportList.length; i++) {
            var report = reportList[i];
            html += report.html;
        }

        return html;

    }


    /*
     {
     schueler: [
     // Jeweils alle Attribute von BorrowserRecord, aber nur die Schüler
     ],
     lehrer: {
     ],
     klassen: [
     // jeweils name, Anzahl der Schüler/innen
     ],
     buecher: [
     // Jeweils alle Attribute von LibraryInventoryRecord
     ],
     reports: [

     ]
     }
     */

    // TODO

    function fetchData() {
        $.post('/library/reports/navigatordata',
            JSON.stringify({school_id: global_school_id, school_term_id: global_school_term_id}),
            function (data) {

                reportData = data;

                initReportsList();

                initDataTable();

                selectDataType(dataType);

                return true;

            }, "json");

    }

    function initDataTable() {

        var gridObj = w2ui['libraryReportsDataNavigator'];

        if (gridObj !== undefined) {
            gridObj.destroy();
        }

        if (dataType === DATATYPES.other) {

            $("libraryReportsDataNavigator").hide();

        } else {

            $("libraryReportsDataNavigator").show();

            initializeDataNavigator();

            gridObj = w2ui['libraryReportsDataNavigator'];

            var objects = [];

            switch (dataType) {
                case DATATYPES.schueler:
                    objects = reportData.schueler;
                    break;
                case DATATYPES.lehrer:
                    objects = reportData.lehrer;
                    break;
                case DATATYPES.klassen:
                    objects = reportData.klassen;
                    break;
                case DATATYPES.buecher:
                    objects = reportData.buecher;
                    break;
            }

            gridObj.add(objects);

        }

    }


    function initializeDOM() {

        $('#reportsSchuelerSelected').parent().click(function () {
            selectDataType(DATATYPES.schueler)
        });
        $('#reportsLehrerSelected').parent().click(function () {
            selectDataType(DATATYPES.lehrer)
        });
        $('#reportsKlassenSelected').parent().click(function () {
            selectDataType(DATATYPES.klassen)
        });
        $('#reportsBuecherSelected').parent().click(function () {
            selectDataType(DATATYPES.buecher)
        });
        $('#reportsAndereSelected').parent().click(function () {
            selectDataType(DATATYPES.other)
        });

        $('#reportsStart').click(function () {
            collectSelectedData()
        });

        $('#libraryReportsTab').on('shown.bs.tab', function (e) {

            // Show w2ui-tables for the first time only if they are yet visible.
            // Otherwise they behave strange!

            //var gridObj = w2ui['libraryBorrowerList'];

            //if (typeof gridObj == "undefined") {

            //    initializeBorrowTables();

            //} else {
            w2ui['libraryReportsDataNavigator'].resize();

            //}

        });


    }

    function selectDataType(dt) {
        dataType = dt;
        initDataTable();

        $('#reportsList').html('<div class="list-group">\n' + getReportsList() + '\n</div>');

    }


    function initializeDataNavigator() {

        var navigator = $('#libraryReportsDataNavigator');

        switch (dataType) {

            case DATATYPES.schueler:

                navigator.w2grid({
                    name: 'libraryReportsDataNavigator',
                    header: 'Schüler/innen',
                    buffered: 2000,
                    recid: 'student_id',
                    show: {
                        header: false,
                        toolbar: true,
                        selectColumn: true,
                        multiSelect: true,
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
                    }, {field: 'name', direction: 'asc'}]

                });

                break;

            case DATATYPES.lehrer:

                navigator.w2grid({
                    name: 'libraryReportsDataNavigator',
                    header: 'Lehrkräfte',
                    buffered: 2000,
                    recid: 'teacher_id',
                    show: {
                        header: false,
                        toolbar: true,
                        selectColumn: true,
                        multiSelect: true,
                        toolbarAdd: false,
                        toolbarDelete: false,
                        toolbarSave: false,
                        footer: true
                    },
                    columns: [
                        {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true},
                        {field: 'name', caption: 'Name', size: '180px', sortable: true, resizable: true},
                    ],
                    searches: [
                        {field: 'name', caption: 'Name', type: 'text'}
                    ],
                    sortData: [{field: 'name', direction: 'asc'}]

                });


                break;

            case DATATYPES.klassen:

                navigator.w2grid({
                    name: 'libraryReportsDataNavigator',
                    header: 'Klassen',
                    buffered: 2000,
                    recid: 'id',
                    show: {
                        header: false,
                        toolbar: true,
                        selectColumn: true,
                        multiSelect: true,
                        toolbarAdd: false,
                        toolbarDelete: false,
                        toolbarSave: false,
                        footer: true
                    },
                    columns: [
                        {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true},
                        {field: 'name', caption: 'Name', size: '180px', sortable: true, resizable: true},
                        {field: 'size', caption: 'Anzahl', size: '30px', sortable: true, resizable: true}
                    ],
                    searches: [
                        {field: 'name', caption: 'Name', type: 'text'}
                    ],
                    sortData: [{field: 'name', direction: 'asc'}]

                });


                break;

            case DATATYPES.buecher:

                navigator.w2grid({
                    name: 'libraryReportsDataNavigator',
                    header: 'Bücher',
                    //url		: 'library/inventoryBooks',
                    buffered: 1000,
                    recid: 'id',
                    postData: {school_id: global_school_id},
                    show: {
                        header: false,
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
                        {
                            field: 'publisher',
                            caption: 'Verlag',
                            size: '110px',
                            resizable: true,
                            editable: {type: 'text'}
                        },
                        {
                            field: 'subject',
                            caption: 'Fach',
                            size: '40px',
                            resizable: true,
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
                    }]

                });

                break;

        }
    }


}(App));