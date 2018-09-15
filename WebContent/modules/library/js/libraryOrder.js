// JS file for library module

(function (app) {

    if (!app.actions['startLibrary']) {
        app.actions['startLibrary'] = [];
    }

    app.actions['startLibrary'].push({

        open: function (parameters) {

            initializeInventoryTables();
            initializeDOM();
            w2ui['libraryOrderBooks'].load('/library/inventoryBooks/get');

        },
        close: function () {
            w2ui['libraryOrderBooks'].destroy();

        }
    });

    function initializeDOM() {
        $('#libraryOrderTab').on('shown.bs.tab', function (e) {

            w2ui['libraryOrderBooks'].resize();

        });

        $('#orderBooksStart').click(function(){
           collectData();
        });

        var ls = App.globalDefinitions().librarysettings;
        if(ls){
            if(ls.addressLeft){
                $('#bookdealeraddressleft').val(ls.addressLeft);
            }

            if(ls.addressRight){
                $('#bookdealeraddressright').val(ls.addressRight);
            }

            if(ls.orderId){
                $('#orderid').val(ls.orderId);
            }

            if(ls.customerId){
                $('#customerid').val(ls.customerId);
            }

            if(ls.rabatt){
                $('#rabatt').val(ls.rabatt);
            }

        }


    }


    function collectData(){

        var records = w2ui['libraryOrderBooks'].records;
        var selectedRows = [];

        for(var i = 0; i < records.length; i++){
            var record = records[i];
            if(record.number > 0){
                selectedRows.push({id: record.id, number: record.number});
            }
        }


        App.globalDefinitions().librarysettings = {
            addressLeft: $('#bookdealeraddressleft').val(),
            addressRight: $('#bookdealeraddressright').val(),
            orderId: $('#orderid').val(),
            customerId: $('#customerid').val(),
            rabatt: $('#rabatt').val(),
        };


        var requestData = {
            selectedRows: selectedRows,
            addressLeft: $('#bookdealeraddressleft').val(),
            addressRight: $('#bookdealeraddressright').val(),
            orderId: $('#orderid').val(),
            customerId: $('#customerid').val(),
            rabatt: $('#rabatt').val(),
            school_id: global_school_id,
            librarysettings: JSON.stringify(App.globalDefinitions().librarysettings)
        };


       var text = JSON.stringify(requestData);

        $('#orderBooksParameterTextarea').val(text);

    }

    function initializeInventoryTables() {

        /**
         * Table left top which shows Books
         */

        $('#libraryOrderLeft').w2grid({
            name: 'libraryOrderBooks',
            header: 'Bücher',
            //url		: 'library/inventoryBooks',
            buffered: 1000,
            multiSelect: false,
            recid: 'id',
            postData: {school_id: global_school_id},
            show: {
                header: true,
                toolbar: true,
                selectColumn: false,
                toolbarAdd: false,
                toolbarDelete: false,
                toolbarSave: false,
                footer: false
            },
            columns: [
                {field: 'id', caption: 'ID', size: '30px', hidden: true, sortable: true, render: 'int'},
                {
                    field: 'number',
                    caption: 'Anzahl',
                    size: '50px',
                    sortable: true,
                    resizable: true,
                    editable: {type: 'int'}
                },
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

                if (this.columns[event.column].field !== 'number') {
                    commitW2GridChanges(this, updateRequest, "/library/inventoryBooks/update");
                } else {
                    mergeOrDiscard2GridChanges(this, [change], true);
                }

            }


        });


    }

}(App));