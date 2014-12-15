/* this file contains classes and utility functions that are used everywhere on the website */
var firebase = new Firebase("https://henry-test.firebaseIO.com");
var user;
var currentProject;
var milestonePage;
var projectPage;
var taskPage;
var selectedProject;
var selectedMilestone;


// table object manages a table of values in the database, use get to get objects from the database
// by uid
function selectProject(project){
    milestonePage.show();
    projectPage.hide();
    taskPage.hide();
	if(selectedProject){
		selectedProject.off();
	}
	selectedProject = project;
	var milestones = selectedProject.getMilestones();
	
	drawStuff(selectedProject.uid);
	
	var $panel = $('#milestones-panel');
	$panel.children().remove();
	milestones.onItemAdded(function(milestone){
		$panel.append(milestone.getButtonDiv());
	});
	var nameA = $('#project-name');
	selectedProject.getName(function(name){
		nameA.text(name);
	});

/*	var memberCont = $('#member-container');
	selectedProject.getMembers().onItemAdded(function(user){
		memberCont.append(user.getMemberTile(selectedProject));
	});*/
}

function selectMilestone(milestone){
    milestonePage.hide();
    projectPage.hide();
    taskPage.show();
	if(selectedMilestone){
		currentMilestone.off();
	}
    selectedMilestone = milestone;
	var tasks = selectedMilestone.getTasks();
	var $panel = $('#task-rows');
	$panel.children().remove();
	tasks.onItemAdded(function(task){
		$panel.prepend(task.getTableRow());
	});
}

function showProjects(){
    milestonePage.hide();
    projectPage.show();
    taskPage.hide();
}

function getAllUsers(){
	users.onItemAdded(function (user) {
		var $select = $('#member-select');
		user.getName(function(nameStr){
			$select.append('<option value="' + user.uid + '">' +
				nameStr + '</option>');	
		});
	});
}

function selectUser(selectedUser){
	user = selectedUser;
	var userProjects = user.getProjects();
    var $panel = $('#projects-panel');
    userProjects.onItemAdded(function(project) {
        $panel.append(project.getButtonDiv())
    });
}



function Table(factory, firebase) {
    this.__factory = factory;
    this.__firebase = firebase;
};

Table.prototype = {
    get: function (uid) {
        return this.__factory(this.__firebase.child(uid));
    },
    onItemAdded: function (callback) {
        var table = this;
        this.__firebase.orderByChild("name").on('child_added', function (snap) {
            var val = table.__factory(snap.ref());
            callback(val);
        });
    },
    onItemRemoved: function (callback) {
        var table = this;
        this.__firebase.on('child_removed', function (snap) {
            var val = table.__factory(snap.ref());
            callback(val);
        });
    },
    off: function () {
        this.__firebase.off();
    },
    add: function (id) {
        var ref;
        if (id) {
            ref = this.__firebase.child(id);
        } else {
            ref = this.__firebase.push();
        }
        return this.__factory(ref);
    },
    getSelect: function (onselect, defaultId) {
        var select = $('<select>');
        if (onselect) {
            var table = this;
            select.change(function () {
                onselect(table.get(select.val()));
            });
        }
        this.onItemAdded(function (item) {
            select.prepend(item.getOption());
            select.val(defaultId);
        });
        return select;
    },
    sort: function (comparator) {

    }
};

function ReferenceTable(referencedTable, firebase) {
    this.__factory = function (ref) {
        return referencedTable.get(ref.name());
    };
    this.__firebase = firebase;
}

ReferenceTable.prototype = Table.prototype;

function User(firebase) {
    this.__firebase = firebase;
    this.uid = firebase.key();
    this.__projects = firebase.child('projects');
    this.__name = firebase.child('name');
    this.__email = firebase.child('email');
}

User.prototype = {
    getName: function (callback) {
        this.__name.on('value', function (dat) {
            callback(dat.val());
        });
    },
	getEmailLink: function(){
		var a = $('<a>');
		this.getEmail(function(email){
			a.attr('href',email);
			a.text(email);
		});
		return a;
	},
	getEmail: function(callback){
		this.__email.on('value',function(value){
			callback(value.val());
		});
	},
    getOption: function () {
        var option = $('<option value="' + this.uid + '"></option>');
        this.getName(function (name) {
            option.text(name);
        });
        return option;
    },
    getProjects: function () {
        return new ReferenceTable(projects, this.__projects);
    },
    getProjectStats: function () {
        var user = this;
        return new Table(
            function (ref) {
                return new ProjectStats(ref);
            });
    },
    getRole: function (project, callback) {
        return project.getRole(callback);
    },
    getMilestoneTasks: function (milestone) {

    },
    onProjectTaskAdded: function (project, callback) {
        var projectData = this.__projects.child(project.uid),
            milestones = project.getMilestones(),
            milestoneData = projectData.child('milestones');
        milestoneData.on('child_added', function (snap) {
            var milestoneData = snap.ref(),
                milestone = milestones.get(snap.name()),
                taskData = milestoneData.child('tasks'),
                tasks = milestone.getTasks();
            taskData.orderByChild("name").on('child_added', function (snap) {
                taskid = snap.name();
                callback(tasks.get(taskid));
            });
        });
    },
    getAllTasks: function () {

    },
    getMemberTile:function(project){
    	var tile = $(
    		'<div class="panel outlined">'
    	),
		nameH4 = $('<h4>'),
		roleSpan = $('<span>'),
		email = this.getEmailLink();

		this.getName(function(name){
			nameH4.text(name);
		});

		project.getRole(this,function(role){
			roleSpan.text(role);
		});

		tile.append(nameH4,email,roleSpan);
		return tile;
	},
    off: function (arg1, arg2) {
        this.__firebase.off(arg1, arg2);
    }
};


// Adds the currently member selected in the "Add member" modal to the project
function addNewMember() {
    var projectID = currentProject.uid;
    var selected = $("#member-select").val();

    if (!projectID || !selected) {
        $("#member-error").show();
        return;
    } else {
        $("#member-error").hide();
    }

    currentProject.addMember(selected, 'Developer');
    $("#member-submit").foundation('reveal', 'close');
}


User.ProjectData = function (user, ref) {
    this.user = user;
    this.__firebase = ref;
    this.uid = ref.name();
    this.__added_lines_of_code = ref.child('added_lines_of_code');
    this.__removed_lines_of_code = ref.child('removed_lines_of_code');
    this.__total_lines_of_code = ref.child('total_lines_of_code');
    this.__milestones = ref.child('milestones');
};

User.ProjectData.prototype = {
    getProject: function () {
        return projects.get(this.uid);
    },
    getMilestoneData: function () {
        return new Table(
            function (ref) {
                return new User.MilestoneData(this.user, ref)
            },
            this.__milestones
        );
    },
    getMilestones: function () {
        return new ReferenceTable(
            this.getProject().getMilestones,
            this.__milestones
        );
    },
    getLinesOfCode: function (callback) {
        this.__total_lines_of_code.on('value', function (snap) {
            callback(snap.val());
        });
    }
};

User.MilestoneData = function (user, ref) {
    this.user = user;
    this.__firebase = ref;
    this.uid = ref.name();
    this.__added_lines_of_code = ref.child('added_lines_of_code');
    this.__removed_lines_of_code = ref.child('removed_lines_of_code');
    this.__total_lines_of_code = ref.child('total_lines_of_code');
    this.__tasks = ref.child('tasks');
};

User.MilestoneData.prototype = {
    getMilestone: function () {

    },
    getLinesOfCode: function (callback) {
        this.__total_lines_of_code.on('value', function (snap) {
            callback(snap.val());
        });
    }
};

function makeSelect(categories, def, onselect) {
    var select = $('<select>');
    categories.forEach(function (category) {
        select.append('<option value="' + category + '">' + category + '</option>');
    });
    select.val(def);
    if (onselect) {
        select.change(function () {
            onselect(select.val());
        });
    }
    return select;
}

function makeProgressBar(divClass, text, percentRef) {
    var div = $('<div>');
    var label = $('<h4>' + text + '</h4>');
    var progress = $('<div class="progress ' + divClass + '">');
    var span = $('<span class="meter">');
    div.append(label, progress);
    progress.append(span);
    percentRef.on('value', function (snap) {
        span.width(String(snap.val()) + "%");
    });
    return div;
}

function label(elem, label) {
    var l = $('<label>');
    l.append(label, elem);
    return l;
}

//Initializes the Project object
function Project(firebase) {
    this.__firebase = firebase;
    this.uid = firebase.key();
    this.__name = firebase.child('name');
    this.__description = firebase.child('description');
    this.__milestones = firebase.child('milestones');
    this.__dueDate = firebase.child('due_date');
    this.__members = firebase.child('members');
    this.__taskPercent = firebase.child('task_percent');
    this.__milestonePercent = firebase.child('milestone_percent');
    this.__hoursPercent = firebase.child('hours_percent');
};

Project.prototype = {
    getName: function (callback) {
        this.__name.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getDescription: function (callback) {
        this.__description.on('value', function (dat) {
            callback(dat.val());
        });
    },
    setName: function (name) {
        this.__name.set(name);
    },
    getDueDate: function (callback) {
        this.__dueDate.on('value', function (dat) {
            callback(dat.val());
        });
    },
    setDueDate: function (due) {
        this.__dueDate.set(due);
    },
    setDescription: function (desc) {
        this.__description.set(description);
    },
    getButtonDiv: function (callback) {
        var project = $('<div class="row project">'),
            leftColumn = $('<div class="small-4 columns small-offset-1">'),
            rightColumn = $('<div class="small-4 columns small-offset-2 left">'),
            projectButton = $('<div>'),
            projectA = $('<a class="button expand text-center">'),
			memberButton = $('<div>'),
			memberA = $('<a class="button expand text-center">'),
            nameSpan = $('<span>'),
            descDiv = $('<div>'),
            dueDiv = $('<div>'),
			thisProject = this;

        projectA.append(nameSpan);
       	projectButton.append(projectA);
		memberButton.append(memberA);
        leftColumn.append(projectButton, descDiv, dueDiv, memberButton);
		memberA.attr('href','#');
		memberA.attr('data-reveal-id','member-modal');
		memberA.text('view members');
		var memberModalName = $('#member-modal-name');
		var memberModalTiles = $('#member-modal-tiles');
		memberA.click(function(){
			thisProject.getName(function(name){
				memberModalName.text("Members For "+name);
			});
			memberModalTiles.children().remove();
			thisProject.getMemberTiles(function(tile){
				memberModalTiles.append(tile);
			});
		});
        rightColumn.append(
            this.getMilestoneProgressBar(),
            this.getTaskProgressBar(),
            this.getHoursProgressBar()
        );
        project.append(leftColumn, rightColumn);
        var p = this;
        projectA.click(function () {
            selectProject(p);
            currentProject = p;
        });
        this.getName(function (nameStr) {
            nameSpan.text(nameStr);
        });
        this.getDescription(function (descriptionStr) {
            descDiv.html(descriptionStr);
        });
        this.getDueDate(function (dateStr) {
            if (dateStr) {
                dueDiv.html('Due:' + dateStr);
            }
        });
        return project;
    },
    getMilestones: function () {
        return new Table(function (fb) {
            return new Milestone(fb);
        }, this.__firebase.child('milestones'));
    },
    // adds the user specified by either a user object or the uid of the user in the database,
    // the role by default is "Developer", this function can also be used to change a role of a user
    addMember: function (user, role) {
        if (!role) {
            role = "Developer";
        }
        if (typeof user == "object") {
            user = user.uid;
        }
        var obj = {};
        obj[user] = role;
        //console.log(obj);
        this.__members.update(obj);
    },
    getOption: function () {
        var option = $('<option value="' + this.uid + '"></option>');
        this.getName(function (name) {
            option.text(name);
        });
        return option;
    },
    getTaskProgressBar: function () {
        return makeProgressBar('small-12', 'Tasks Completed', this.__taskPercent);
    },
    getHoursProgressBar: function () {
        return makeProgressBar('small-12', 'Hours Completed', this.__hoursPercent);
    },
    getMilestoneProgressBar: function () {
        return makeProgressBar('small-12', 'Milestones Completed', this.__milestonePercent);
    },
    off: function () {
        this.__firebase.off();
    },
    getRole: function (member, callback) {
        this.__members.child(member.uid).on('value', function (snap) {
            callback(snap.val());
        });
    },
    getMembers: function () {
        return new ReferenceTable(users, this.__members);
    },
	getMemberTiles: function(callback){
		var project = this;
		this.getMembers().onItemAdded(function(user){
			callback(user.getMemberTile(project));
		});
	},
};
// creates new projects and are added into firebase
function addNewProject() {
    var docName = $("#projectName").val();
    var docDescription = $("#projectDescription").val();
    var docDueDate = $("#projectDueDate").val();
    var docEstimatedHours = $("#projectEstimatedHours").val();
    var currentUser = user.uid;
    if (!docName || !docDescription || !docDueDate || !docEstimatedHours || !currentUser) {
        $("#project-error").show();
        return;
    } else {
        $("#project-error").hide();
    }

    var estHours = Number(docEstimatedHours);

    if(isNaN(estHours) || !isFinite(estHours) || estHours < 0){
        $("#project-error").show();
        return;
    }

    if(!docDueDate.match(/^(19|20)[0-9][0-9][-\\/. ](0[1-9]|1[012])[-\\/. ](0[1-9]|[12][0-9]|3[01])$/)){
        $("#project-error").show();
        return;
    }

    var members = {};
    members[currentUser] = 'Lead';
    var project = firebase.child('projects').push({
        'name': docName,
        'description': docDescription,
        'due_date': docDueDate,
        'total_estimated_hours': estHours,
		'categories': {"Bug Fix":true,"Enhancement":true,"Feature":true,"Business":true,"General":true,"Infrastructure":true,"QA":true,"No Category":true},
        'members': members
    });
    $('#project-submit').foundation('reveal', 'close');
}

//Initializes the Milestone object
function Milestone(firebase) {
    this.__firebase = firebase;
    this.uid = firebase.key();
    this.__name = firebase.child('name');
    this.__description = firebase.child('description');
    this.__dueDate = firebase.child('due_date');
    this.__tasks = firebase.child('tasks');
    this.__members = firebase.child('members');
    this.__hoursPercent = firebase.child('hours_percent');
    this.__taskPercent = firebase.child('task_percent');
    this.__milestonePercent = firebase.child('milestone_percent');
};


Milestone.prototype = {
    getName: function (callback) {
        this.__name.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getDueDate: function (callback) {
        this.__dueDate.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getDescription: function (callback) {
        this.__description.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getButtonDiv: function () {
        var milestone = $('<div class="row milestone">'),
            leftColumn = $('<div class="small-4 columns small-offset-1">'),
            rightColumn = $('<div class="small-4 columns small-offset-2 left">'),
            button = $('<div>'),
            a = $('<a class="button expand text-center">'),
            nameSpan = $('<span>'),
            descDiv = $('<div>'),
            dueDiv = $('<div>');

        a.append(nameSpan);
        button.append(a);
        leftColumn.append(button, descDiv, dueDiv);
        rightColumn.append(this.getTaskProgressBar(), this.getHoursProgressBar());
        milestone.append(leftColumn, rightColumn);
        var m = this;
        a.click(function () {
            selectMilestone(m);
        });
        this.getName(function (nameStr) {
            nameSpan.text(nameStr);
        });

        this.getDescription(function (descriptionStr) {
            descDiv.html(descriptionStr);
        });
        this.getDueDate(function (dueStr) {
            if (dueStr) {
                dueDiv.html('Due:' + dueStr);
            }
        });
        return milestone;
    },
    getTasks: function () {
        return new Table(function (fb) {
            return new Task(fb);
        }, this.__tasks);
    },
    getMembers: function () {
        return new ReferenceTable(users, this.__members);
    },
    getTaskProgressBar: function () {
        return makeProgressBar('small-12', 'Tasks Completed', this.__taskPercent);
    },
    getHoursProgressBar: function () {
        return makeProgressBar('small-12', 'Hours Completed', this.__hoursPercent);
    },
    off: function () {
        this.__firebase.off();
    }
};

//Adds a new milestone to firebase based on the values of the modal's textfield inputs
function addNewMilestone() {
    var docName = $("#milestoneName").val();
    var docDescription = $("#milestoneDescription").val();
    var docDueDate = $("#milestoneDueDate").val();
    var docEstimatedHours = $("#milestoneEstimatedHours").val();
    var projectid = currentProject.uid;

    // Validate fields
    if (!docName || !docDescription || !docDueDate || !docEstimatedHours || !projectid) {
        $("#milestone-error").show();
        return;
    } else {
        $("#milestone-error").hide();
    }

    var estHours = Number(docEstimatedHours);

    if(isNaN(estHours) || !isFinite(estHours) || estHours < 0){
        $("#milestone-error").show();
        return;
    }

    if(!docDueDate.match(/^(19|20)[0-9][0-9][-\\/. ](0[1-9]|1[012])[-\\/. ](0[1-9]|[12][0-9]|3[01])$/)){
        $("#milestone-error").show();
        return;
    }

    firebase.child('projects/' + projectid).child('milestones').push(
        { 'name': docName,
          'description': docDescription,
          'due_date': docDueDate,
          'total_estimated_hours': estHours
        }
    );

    $("#milestone-submit").foundation('reveal', 'close');
}

function Task(firebase) {
    this.__firebase = firebase;
    this.uid = firebase.key();
    this.__name = firebase.child('name');
    this.__description = firebase.child('description');
    this.__assigned_user = firebase.child('assignedTo');
    this.__category = firebase.child('category');
    this.__status = firebase.child('status');
    this.__lines_of_code = firebase.child('total_lines_of_code');
    this.__due_date = firebase.child('due_date');
    this.__is_completed = firebase.child('is_completed');
    this.__hour_estimate = firebase.child('updated_hour_estimate');
};

Task.Statuses = [
    'New',
    'Implementation',
    'Testing',
    'Verify',
    'Regression',
    'Closed'
];

Task.Categories = [
    'Bug Fix',
    'Enhancement',
    'Feature',
    'Business',
    'General',
    'Infrastructure',
    'QA',
    'No Category'
];

Task.Flags = [
    'true',
    'false'
];

Task.prototype = {
    getName: function (callback) {
        this.__name.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getDescription: function (callback) {
        this.__description.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getAssignedUser: function (callback) {
        this.__assigned_user.on('value', function (dat) {
            var user = users.get(dat.val());
            callback(user);
        });
    },
    getCategory: function (callback) {
        this.__category.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getStatus: function (callback) {
        this.__status.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getDueDate: function (callback) {
        this.__due_date.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getFlag: function (callback) {
        this.__is_completed.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getTotalLinesOfCode: function (callback) {
        this.__total_lines_of_code.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getTimeEstimate: function (callback) {
        this.__hour_estimate.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getDescriptionDiv: function () {
        var task = $('<div>'),
            nameH4 = $('<h4>'),
            descSpan = $('<span>'),
            catSpan = $('<span>');
        this.getName(function (name) {
            nameH4.text(name);
        });
        this.getDescription(function (description) {
            descSpan.text(description);
        });
        this.getCategory(function (cat) {
            catSpan.text(cat);
        });
        task.append(nameH4, descSpan, catSpan);
        return task;
    },
    getTableRow: function () {
        var row = $('<tr class="task-row" data-reveal-id="task-modal">');
        var name = $('<td>');
        var desc = $('<td>');
        var cat = $('<td>');
        var stat = $('<td>');
        var user = $('<td>');
        var due = $('<td>');
        var flag = $('<td>');
        var hoursEstimate = $('<td>');
        var task = this;
        var modal = $('#task-modal');

        row.append(name, desc, user, cat, stat, due, flag, hoursEstimate);
        this.getName(function (nameStr) {
            name.html(nameStr);
        });
        this.getDescription(function (descriptionStr) {
            desc.html(descriptionStr);
        });
        this.getAssignedUser(function (assignedUser) {
            assignedUser.getName(function (name) {
                user.html(name);
            });
        });
        this.getCategory(function (categoryStr) {
            cat.html(categoryStr);
        });
        this.getStatus(function (statStr) {
            stat.html(statStr);
        });
        this.getDueDate(function (dueDate) {
            due.html(dueDate);
        });
        this.getFlag(function (f) {
            if(f){
                flag.html('<img src="completed.png" />');
            } else {
                flag.html('<img src="notcompleted.png" />');
            }
        });
        this.getTimeEstimate(function (hour_estimateStr) {
            hoursEstimate.html(hour_estimateStr);
        });
        row.click(function () {
            modal.children().remove();
            task.__firebase.once('value', function (snap) {
                var vals = snap.val(),
                    nameInput = $('<input type="text" value="' + vals.name + '">'),
                    descriptionInput = $('<textarea>' + vals.description + '</textarea>'),
                    userSelect = users.getSelect(function (user) {
                        selectedUser = user;
                    }, vals.assignedTo),
                    categoriesSelect = makeSelect(Task.Categories, vals.category),
                    statusSelect = makeSelect(Task.Statuses, vals.status),
                    dueInput = $('<input type="text" placeholder="yyyy-mm-dd" value="' + vals.due_date + '">'),
                    flagInput = makeSelect(Task.Flags, String(vals.is_completed)),
                    estHoursInput = $('<input type="text" value="' + vals.updated_hour_estimate + '">'),
                    nameH = $('<h3>'),
                    submit = $('<input class="button" value="Edit Task" />'),
                    taskError = $('<div id="task-error" class="my-error" hidden>All fields must be specified</div>');

                task.getName(function (name) {
                    nameH.text('Edit Task: ' + name);
                });
                modal.append(
                    nameH,
                    label(nameInput, 'Name'),
                    label(descriptionInput, 'Description'),
                    label(userSelect, 'User'),
                    label(categoriesSelect, 'Category'),
                    label(statusSelect, 'Status'),
                    label(dueInput, 'Due Date'),
                    label(flagInput, 'Is Complete'),
                    label(estHoursInput, 'Estimated Hours'),
                    submit,
                    taskError
                );
                modal.keypress(function (e) {
                    if (e.which == 13) {
                        submit.click();
                    }
                });
                dueInput.click(function () {
                    dueInput.fdatepicker({format: 'yyyy-mm-dd'});
                    dueInput.fdatepicker('show');
                });
                submit.click(function () {

                    if(!nameInput.val() || !descriptionInput.val() || !userSelect.val() || !dueInput.val() || !categoriesSelect.val() || !statusSelect.val() || !estHoursInput.val()){
                        $("#task-error").show();
                        return;
                    } else {
                        $("#task-error").hide();
                    }

                    var estHours = Number(estHoursInput.val());
                    // console.log(estHours);
                    var flagVal = flagInput.val() == 'true' ? true : false;
                    // console.log(typeof(flagInput.val()));
                    // console.log(flagInput.val());
                    // console.log(typeof(flagVal));
                    // console.log(flagVal);

                    if(isNaN(estHours) || !isFinite(estHours) || estHours < 0){
                        $("#task-error").show();
                        return;
                    }

                    if(!dueInput.val().match(/^(19|20)[0-9][0-9][-\\/. ](0[1-9]|1[012])[-\\/. ](0[1-9]|[12][0-9]|3[01])$/)){
                        $("#task-error").show();
                        return;
                    }

                    task.__firebase.update({
                        name: nameInput.val(),
                        description: descriptionInput.val(),
                        assignedTo: userSelect.val(),
                        due_date: dueInput.val(),
                        category: categoriesSelect.val(),
                        status: statusSelect.val(),
                        is_completed: flagVal,
                        updated_hour_estimate: estHours
                    });

                    $("#task-modal").foundation('reveal', 'close');
                });
            });
        });
        this.getTimeEstimate(function(time) {
            console.log(time);
        });
        return row;
    },
    setUser: function (user) {
        var id = user;
        if (typeof id != 'string') {
            id = user.uid;
        }
        this.__assigned_user.set(id);
    },
    setCategory: function (cat) {
        this.__category.set(cat);
    },
    setDescription: function (desc) {
        this.__description.set(desc);
    },
    setEstimatedHours: function (hours) {
        this.__original_hour_estimate.set(hours);
    },
    setName: function (name) {
        this.__name.set(name);
    },
    setFlag: function (flag) {
        this.__is_completed.set(flag);
    },
    setDueDate: function (dueDate) {
        this.__due_date.set(dueDate);
    },
    off: function () {
        this.__firebase.off();
    }
};

function newTask() {
    var nameInput = $('<input type="text">'),
        descriptionInput = $('<textarea>'),
        userSelect = users.getSelect(function (user) {
            selectedUser = user;
        }, user.uid),
        categoriesSelect = makeSelect(Task.Categories, "Feature"),
        statusSelect = makeSelect(Task.Statuses, "New"),
        estHoursInput = $('<input type="text">'),
        nameH = '<h3>Add New Task</h3>',
        submit = $('<input class="button" value="Add Task" />'),
        modal = $('#task-modal'),
        //completed = makeSelect(Task.Flags, 'false');
        dueInput = $('<input type="text" placeholder="yyyy-mm-dd">'),
        taskError = $('<div id="task-error" class="my-error" hidden>All fields must be specified</div>');

    modal.children().remove();
    modal.append(
        nameH,
        label(nameInput, 'Name'),
        label(descriptionInput, 'Description'),
        label(userSelect, 'User'),
        label(categoriesSelect, 'Category'),
        label(statusSelect, 'Status'),
        label(estHoursInput, 'Estimated Hours'),
        //label(completed, 'Is Completed'),
        label(dueInput, "Due Date"),
        submit,
        taskError
    );
    dueInput.click(function () {
        dueInput.fdatepicker({format: 'yyyy-mm-dd'});
        dueInput.fdatepicker('show');
    });
    modal.keypress(function (e) {
        if (e.which == 13) {
            submit.click();
        }
    });
    submit.click(function () {

        if(!nameInput.val() || !descriptionInput.val() || !userSelect.val() || !dueInput.val() || !categoriesSelect.val() || !statusSelect.val() || !estHoursInput.val()){
            $("#task-error").show();
            return;
        } else {
            $("#task-error").hide();
        }

        var estHours = Number(estHoursInput.val());

        if(isNaN(estHours) || !isFinite(estHours) || estHours < 0){
            $("#task-error").show();
            return;
        }

        if(!dueInput.val().match(/^(19|20)[0-9][0-9][-\\/. ](0[1-9]|1[012])[-\\/. ](0[1-9]|[12][0-9]|3[01])$/)){
            $("#task-error").show();
            return;
        }

        selectedMilestone.__tasks.push({
            name: nameInput.val(),
            description: descriptionInput.val(),
            assignedTo: userSelect.val(),
            category: categoriesSelect.val(),
            status: statusSelect.val(),
            original_hour_estimate: estHours,
            is_completed: false,    //default task to uncompleted
            due_date: dueInput.val()
        });
        $("#task-modal").foundation('reveal', 'close');
    });
}

var projects = new Table(function (fb) {
    return new Project(fb);
}, firebase.child('projects'));

var users = new Table(function (fb) {
    return new User(fb);
}, firebase.child('users'));


function getLoginData() { // Takes the login data from the form and places it into variables
    var user = $("#loginUser").val();
    var pass = $("#loginPass").val();
    $("loginPass").val("");
    login(user, pass, false);
}

function login(user, pass, registering) { // Authenticates with Firebase, giving a callback that will
    // cause the window to go to projects if it was successful,
    // else go back to login and show the invalid input message.
    //console.log(user);
    firebase.authWithPassword({
        email: user,
        password: pass
    }, function (error, authData) {
        if (error === null) {
            var userData = authData;
            //console.log(authData);
            if (registering) { // is registering
                firebase.child('users/' + userData.uid).update(
                    {
                        email: userData.password.email,
                        name: $("#name").val()
                    }, function (error) {
                        if (error) {
                            $("#loginError").show();
                        } else {
                            $("#loginError").hide();
                            $("#myLoginModal").foundation('reveal', 'close');
							window.location.replace('/projects');
                        }
                    }
                );
            } else {
                $("#myLoginModal").foundation('reveal', 'close');
				window.location.replace('/projects');
            }
        } else {
            $("#loginError").show();
        }
    });
};

function register() { 		// Registers a new user with Firebase, and also adds that user
    // to our local database (making a new developer/manager)
    var email = $("#registerUser").val();
    var pass = $("#registerPass").val();
    var passCheck = $("#registerPassCheck").val();
    var name = $("#name").val();

    if (pass !== passCheck) {
        $("#passwordError").show();
        $("#registerError").hide();
        $("#emailError").hide();
        return;
    }
    // Validate all fields are filled in
    if (!email || !pass || !passCheck || !name) {
        $("#passwordError").hide();
        $("#registerError").show();
        $("#emailError").hide();
        return;
    }
    firebase.createUser(
        {
            email: email,
            password: pass
        },
        function (error, userData) {
            if (error === null) { // if an error is throw
                login(email, pass, true);
            } else {
                $("#registerError").hide();
                $("#emailError").show();
            }
        }
    );
}

function showLoginModal() {
    $("#myLoginModal").foundation('reveal', 'open');
}

function logout() { // get rid all user data
    firebase.unauth();
    userData = null;
    projects = null;
    users = null;
    window.location.replace("/");
}

firebase.onAuth( // called on page load to auth users
    function (authData) {
		if(null === authData){
			return;
		}
		var user = users.get(authData.uid);
        $(document).ready(
            function () {
                if (user == null) { // isn't authed
                    $(".notLoggedIn").show();
                    $(".loginRequired").hide();
                } else {
                    $("#currentUser").html("Currently logged in as " + authData.password.email);
                    $(".notLoggedIn").hide();
                    $(".loginRequired").show();
					selectUser(user);
                }
				milestonePage = $('#milestones-page');
			    projectPage = $('#projects-page');
			    taskPage = $('#tasks-page');
				showProjects();
				getAllUsers();
            }
        );
    }
);

function showDatePicker(id) {
    $(id).fdatepicker({format: 'yyyy-mm-dd'});
    $(id).fdatepicker('show');
}
