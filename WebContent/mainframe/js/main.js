/**
 * Created by martin on 13.08.2016.
 */

// Stop Chrome from opening download-page when scanning barcodes
document.addEventListener('keydown', function(event) {
    if( event.keyCode === 17 || event.keyCode === 74 )
        event.preventDefault();
});

/**
 * Every action (e.g. menu-item click) is implemented as <a href="#exampleAction">. This triggers window.onHashchange
 * which in turn looks into the following Object, which holds an object
 *
 * 'exampleAction': {
 *      function show(parameters){
 *          ...
 *      },
 *      function hide(){
 *          ...
 *      }
 * }
 *
 * parameters are given after the hash as JSON-String, e.g. #library.showBooks/{id = 10, name= "test"}
 *
 */

var App = (function () {

    var actions = {}; // 'exampleAction': { open: function(parameter){ ... }, close: function(){ ... },  }

    var fragmentCache = {}; // 'exampleAction': "<div>...</div>"

    var lastAction;

    var globalDefinitions = {
        formList: [],
        curriculumList: [],
        subjectList: [],
        schoolList: [],
        username: "dummy",
        currentSchool: undefined,
        currentSchoolTerm: undefined
    };

    $(function () {

        $.post("/definitions", JSON.stringify({
            school_id: global_school_id,
            school_term_id: global_school_term_id
        }), function (data) {

            globalDefinitions = data;

            initMainMenu();

            navigate(); // Evaluate initial URL

            /**
             * To enable Navigation with browsers back button every click on a menuitem alters the portion of the URL after the hash (#).
             * This triggers window.onHashchange:
             */
            $(window).on('hashchange', navigate);
        });


    });

    function getContent(fragmentId, callback) {

        // If the page has been fetched before,
        if (fragmentCache[fragmentId]) {

            // pass the previously fetched content to the callback.
            callback(fragmentCache[fragmentId]);

        } else {
            // If the page has not been fetched before, fetch it.
            $.post("/fragments", JSON.stringify({
                fragmentId: fragmentId,
                school_id: global_school_id
            }), function (content) {

                // Store the fetched content in the cache.
                fragmentCache[fragmentId] = content;

                // Pass the newly fetched content to the callback.
                callback(content);
            });
        }

    }


    function navigate() {
        var url = decodeURI(window.location.hash); // e.g. #library.showBooks/{id = 10, name= "test"}
        var actionName = url.split('/')[0].substr(1); // remove leading # and trailing /{id=...

        var parameters = {};
        var parameterJSON = url.substr(actionName.length + 2);
        if (parameterJSON.length > 0) {
            parameters = JSON.parse(url.split('/')[1]);
        }

        //alert("url: " + url + ", actionName: " + actionName + ", parameters: " + parameters);

        if (actionName.length > 0) {
            var action = actions[actionName];

            if (action) {

                if (lastAction) {

                    lastAction.forEach(function (a) {
                        a.close();
                    });
                }

                $("#mainContent").html(''); // remove fragment of last action from DOM

                getContent(actionName, function (content) {
                    $("#mainContent").html(content);
                    action.forEach(function (a) {
                        a.open(parameters);
                    });
                    lastAction = action;
                });

            } else {
                alert("System error: action " + actionName + " is not defined.")
            }
        }
    }

    function initMainMenu() {

        $($("#user").find(".menuitemtext")[0]).text(globalDefinitions.username);

        var schoolSubmenu = "";
        var termSubmenu = "";

        var indent = "                                ";

        // find currently selected school
        globalDefinitions.schoolList.forEach(function (school) {

            var icon = "";

            if (school.id == global_school_id) {
                globalDefinitions.currentSchool = school;
                icon = '<i class="fa fa-check fa-fw"></i>';
            }

            schoolSubmenu += indent + '<li>\n' +
                indent + '   <a href = "#" onclick="App.chooseSchool(' + school.id + '); return true;">\n' +
                indent + '   ' + icon + '<span class="menuitemtext">' + school.abbreviation + '</span></a>\n' +
                indent + '</li>\n';

        });

        var school = globalDefinitions.currentSchool;

        $('#schooltermMenuSchoolSubmenu').html(schoolSubmenu);

        // find currently selected schoolterm
        school.schoolTerms.forEach(function (st) {

            var icon = "";

            if (st.id == global_school_term_id) {
                globalDefinitions.currentSchoolTerm = st;
                icon = '<i class="fa fa-check fa-fw"></i>';
            }

            termSubmenu += indent + '<li>\n' +
                indent + '   <a href = "#" onclick="App.chooseSchoolTerm(' + st.id + '); return true;">\n' +
                indent + '   ' + icon + '<span class="menuitemtext">' + st.name + '</span></a>\n' +
                indent + '</li>\n';


        });

        var termname = globalDefinitions.currentSchoolTerm.name;
        if (termname.indexOf("20") == 0 && termname.length > 5) {
            termname = termname.substr(2);
        }

        var menuCaption = school.abbreviation + ' ' + termname;

        $('#schooltermMenuCaption').text(menuCaption);
        $('#schooltermMenuTermSubmenu').html(termSubmenu);

    }

    function chooseSchool(school_id) {

        var optimal_school_term = undefined;

        var optimal_term_name = undefined;

        // find school_term_id with given school_id and same term as global_school_term_id
        if (globalDefinitions.currentSchoolTerm) {

            optimal_term_name = globalDefinitions.currentSchoolTerm.name;
        }

        var latest_school_term = undefined;

        globalDefinitions.schoolList.forEach(function (school) {

            if (school.id === school_id) {
                school.schoolTerms.forEach(function (st) {

                    if (latest_school_term) {
                        if (st.begindate > latest_school_term.begindate) {
                            latest_school_term = st;
                        }
                    } else {
                        latest_school_term = st;
                    }

                    if(optimal_term_name){
                        if (st.name === optimal_term_name) {
                            optimal_school_term = st;
                        }
                    }

                });
            }

        });

        if (!optimal_school_term) {
            optimal_school_term = latest_school_term;
        }

        if (optimal_school_term) {

            var hash = decodeURI(window.location.hash);

            $("#invisibleForm").html('<form method="get" action="/main/st' + optimal_school_term.id + hash + '">\n' +
                '<input type="submit" />' +
                '</form>');

            $("#invisibleForm form").submit();

        } else {
            w2alert("Zur gewählten Schule ist noch kein Schuljahr angelegt.", "Fehler");
        }
    }

    function chooseSchoolTerm(school_term_id) {
//        global_school_term_id = school_term_id;
//        initMainMenu();
//        navigate();

        var hash = decodeURI(window.location.hash);

        $("#invisibleForm").html('<form method="get" action="/main/st' + school_term_id + hash + '">\n' +
            '<input type="submit" />' +
            '</form>');

        $("#invisibleForm form").submit();
    }

    return {

        actions: actions,
        globalDefinitions: function () {
            return globalDefinitions;
        },
        chooseSchool: chooseSchool,
        chooseSchoolTerm: chooseSchoolTerm,

        getContent: getContent

    };


}());
