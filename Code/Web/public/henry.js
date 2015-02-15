/* this file contains classes and utility functions that are used everywhere on the website */
var firebase = new Firebase("https://henry-test.firebaseIO.com");
var user;
var milestonePage;
var projectPage;
var taskPage;
var myTasksPage;
var profilePage;
var selectedProject;
var selectedMilestone;
var myTasks;
var myStatisticsPage;
var defaultCategories = [];


function Chart(id,title){
	this.chart = {
		renderTo:id,
		ignoreHiddenSeries:true
	};
	this.title = {
		text:title
	};
	this.series = [];
}

function makeAxis(name,units){
	return {
		labels:{
			formatter:function(){
				if(units){
					return this.value+' '+units;
				}
				return this.value;
			}
		},
		title:{
			text:name
		}
	};
}

function dateFormatter(){
	return new Date(this.value).toLocaleDateString();
}

function dateAxis(name){
	return {
		labels:{
			formatter:dateFormatter
		},
		title:{
			text:'Date'
		}
	}
}

Chart.prototype = {
	setXAxis:function(name,units){
		this.xAxis = makeAxis(name,units);
	},
	setYAxis:function(name,units){
		this.yAxis = makeAxis(name,units);
	},
	render:function(){
		if(this._renderedChart){
			this._renderedChart.redraw();
		} else {
			this._renderedChart = new Highcharts.Chart(this);
		}
	}
};

function LineChart(id, title){
	Chart.call(this,id,title);
	this.chart.defaultSeriesType = 'line';
}

LineChart.prototype = new Chart(null,null);

LineChart.prototype.addSeries = function(points,name){
	if(name === undefined){
		name = points._id;
	}
	//console.log(name);
	points._charts.push(this);
	//console.log(points);
	var series = {
		data:points._points,
		id:points._id,
		name:name
	};
	this.series.push(series);
};

function BurndownChart(id,title,burndownData){
	LineChart.call(this,id,title);
	this.xAxis = dateAxis();
	this.tooltip = {
		formatter:function(){
			return new Date(this.x).toLocaleString()+'<br>'+this.y+' hours';
		}
	};
	this.setYAxis('Hours','hrs');
	this.addSeries(burndownData.estimatedHours());
	this.addSeries(burndownData.hoursCompleted());
}

BurndownChart.prototype = new LineChart(null,'Burn Down Chart');

// table object manages a table of values in the database, use get to get objects from the database
// by uid
function selectProject(project){
    milestonePage.show();
    projectPage.hide();
    taskPage.hide();
    myTasksPage.hide();
	profilePage.hide();
	if(selectedProject){
		selectedProject.off();
	}
	selectedProject = project;
	var milestones = selectedProject.getMilestones();
	
	drawMilestoneStuff(selectedProject.uid, firebase);
	
	var $panel = $('#milestones-panel');
	$panel.children().remove();
	milestones.onItemAdded(function(milestone){
		$panel.append(milestone.getButtonDiv());
	});
	var nameA = $('#project-name');
	selectedProject.getName(function(name){
		nameA.text(name);
	});

	var memberCont = $('#member-container');
	memberCont.append(makeAddMemberTile(project));
	project.getMemberTiles(function(tile){
		memberCont.prepend(tile);
	});
	project.renderBurndownChart('burndownchart');
}

function selectMilestone(milestone){
    milestonePage.hide();
    projectPage.hide();
    taskPage.show();
    myTasksPage.hide();
	profilePage.hide();
	if(selectedMilestone){
		seletedMilestone.off();
	}
    selectedMilestone = milestone;
	var tasks = selectedMilestone.getTasks();
	var $panel = $('#task-rows');
	$panel.children().remove();
	tasks.onItemAdded(function(task){
		$panel.prepend(task.getTableRow());
	});
	milestone.renderBurndownChart('milestone-burn-down-chart');
}

function viewProfile(user){
	milestonePage.hide();
    projectPage.hide();
    taskPage.hide();
    myTasksPage.hide();
	profilePage.show();
	user.getName(function(name){
		$('span.profile-name').text(name);
	});
	user.getEmailLink(function(email){
		$('#contact-row').append(email);
	});
	$('#biography').text('no profile');
	$('#profile-phone').text('no number');
	var projPanel = $('#profile-projects');
	projPanel.children().remove();
	user.getProjects().onItemAdded(function(project){
		projPanel.prepend(project.getButtonDiv());
	});
}

function showProjects(){
    drawUserStatistics(firebase,user.uid);
    drawProjectStuff(firebase);
    milestonePage.hide();
    taskPage.hide();
    myTasksPage.hide();
	profilePage.hide();
    projectPage.show();
    myStatisticsPage.hide();
}

function selectMyTasks(){
    var $panel = $('#my-tasks-rows');
    $panel.children().remove();
    user.getTasks($panel);
}

function showMyTasksPage(){
    selectMyTasks();
    milestonePage.hide();
    projectPage.hide();
    taskPage.hide();
	profilePage.hide();
    myStatisticsPage.hide();
    myTasksPage.show();

}
function showMyStatsticsPage(){
    milestonePage.hide();
    projectPage.hide();
    taskPage.hide();
    myTasksPage.hide();
    myStatisticsPage.show();
    // drawUserStatistics(firebase,user.uid);

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

function User(firebase2) {
    this.__firebase = firebase2;
    this.uid = firebase2.key();
    this.__projects = firebase2.child('projects');
    this.__name = firebase2.child('name');
    this.__email = firebase2.child('email');
    this.__all_projects = firebase.child('projects');
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
			a.attr('href','mailto:'+email);
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
    getTasks: function (panel) {
        this.__all_projects.on('child_added', function(project){
            project.child('milestones').forEach(function(milestone){
                milestone.child('tasks').forEach(function(task){
                    //console.log(user.uid);
                    if(task.child('assignedTo').val() === user.uid){
                        panel.append(new MyTasks(task.ref()).getTableRow());
                    }
                });
            });
        });
        /*return new Table(function (fb) {
            return new MyTasks(fb);
        }, this.__tasks);*/
    },
    getAllTasks: function () {
        // this.__projects has all of the project ids of the current user
        // should return a list of references to all the tasks
        var alltasks = firebase.child('projects/milestones/tasks');
        var currenttasks = {};
        alltasks.on("value", function (task) {
           // console.log(task.val());
            if (task.val().assignedTo === selecedUser) {
                currenttasks.add(task);
            }
        });
        return currenttasks;
    },
    getMemberTile:function(project){
    	var tile = $(
    		'<div class="column panel outlined">'
			),
			row = $('<div class="row">'),
			memberCol = $('<div class="small-8 columns">'),
			memberRow = $('<div class="row">'),
			nameCol = $('<div class="small-7 columns">'),
			roleCol = $('<div class="small-5 columns">'),
			nameH3 = $('<h3 class="text-right">'),
			roleSpan = $('<span class="text-left">'),
			emailRow = $('<div class="row">'),
			emailColumn = $('<div class="small-12 columns text-center">'),
			emailLink = this.getEmailLink(),
			buttonCol = $('<div class="small-4 columns">'),
			button = $('<div class="button text-center">View Profile</div>'),
			user = this;

		emailColumn.append(emailLink);
		emailRow.append(emailColumn);
		nameCol.append(nameH3);
		roleCol.append(roleSpan);
		memberRow.append(nameCol,roleCol);
		memberCol.append(memberRow,emailRow);
		tile.append(row);
		row.append(memberCol,buttonCol);
		buttonCol.append(button);

		button.click(function(){	
    		$("#member-modal").foundation('reveal', 'close');
			viewProfile(user);
		});

		this.getName(function(name){
			nameH3.text(name);
		});

		project.getRole(this,function(role){
			roleSpan.text(role);
		});
		return tile;
	},
    off: function (arg1, arg2) {
        this.__firebase.off(arg1, arg2);
    }
};
function userLeaderboard(){
    var pointsArray = [];
    var arr = [];
    console.log("here");
    firebase.child("users").on('value', function(snapshot) {
        var users = snapshot.val();
        for(var id in users){
            var name =users[id].name;
            var points = users[id].total_points; 
            pointsArray.push(name);
            if(points != null){
            pointsArray.push(points);
            }else{
                pointsArray.push(0);
            }
            arr.push(pointsArray);
            pointsArray = [];
        } 
        
    });
    arr.sort(function(a,b) {return b[1] - a[1]});
    console.log(arr);
    var i =1;
    for(var element in arr){
        console.log(element);
        $('#' + i).html(i);
        $('#name' + i).html(arr[i-1][0]);
        $('#pointValue' + i).html(arr[i-1][1]);
        i++;
    }

};


// Adds the currently member selected in the "Add member" modal to the project
function addNewMember() {
    var projectID = selectedProject.uid;
    var selected = $("#member-select").val();

    if (!projectID || !selected) {
        $("#member-error").show();
        return;
    } else {
        $("#member-error").hide();
    }

    selectedProject.addMember(selected, 'Developer');
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

function makeSelect(custom_categories, def, onselect) {
    var select = $('<select>');
    custom_categories.forEach(function (category) {
        if(category != null) {
            select.append('<option value="' + category + '">' + category + '</option>');
        }
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

function makeAddMemberTile(project){
	var selectedUser = null,
		selectButton = $('<div class="button expand text-center">Add Member</div>'),
		addMemberTile = $('<div class="column panel outlined">'),
		nameRow = '<div class="row">'
					+ '<div class="small-12 columns text-center">'
						+ '<h3>Add New Member</h3>'
					+ '</div>'
				+ '</div>',
		selectRow = $('<div class="row">'),
		selectColumn = $('<div class="small-7 columns">'),
		buttonColumn = $('<div class="small-5 columns">'),
		errorRow = $('<div class="row" hidden>'),
		errorColumn = $('<div class="small-12 columns">'),
		error = $('<div class="my-error">No Member Selected</div>'),
		userSelect = users.getSelect(function(user){
			selectedUser = user;
		});
	errorColumn.append(error);
	errorRow.append(errorColumn);
	buttonColumn.append(selectButton);
	selectColumn.append(userSelect);
	selectRow.append(selectColumn,buttonColumn);
	addMemberTile.append(nameRow,selectRow,errorRow);
	selectButton.click(function(){
		if(selectedUser){
			project.addMember(selectedUser);
		} else {
			errorRow.show();
		}
	});
	return addMemberTile;
}

function Series(id){
	this._points = [];
	this._id = id;
	this._charts = [];
}

Series.prototype.addPoint = function(point){
	this._points.push(point);
	var id = this._id;
	//console.log(id + ' add Point');
	var charts = this._charts;
	var chart;
	for(var i = charts.length;i--;){
		chart = charts[i];
		if(chart._renderedChart){
			chart._renderedChart.get(id).addPoint(point,true,true);
		}
	}
}

function CumulativeSeries(id){
	Series.call(this,id);
}

CumulativeSeries.prototype = new Series();
CumulativeSeries.prototype.addPoint = function(point){
	if(this._points.length > 0){
		var lastPoint = this._points[this._points.length-1];
		point = [point[0],point[1]+lastPoint[1]];
	}
	Series.prototype.addPoint.call(this,point);
};

function BurndownData(firebase){
	this._firebase = firebase;
}

function makeSeriesGetter(name){
	return function(){
		this._init();
		return this[name];
	}
}

BurndownData.prototype._init = function(){
	if(this._initialized){
		return;
	}
	this._estimHours = new Series('Estimated Hours');
	this._compHours = new CumulativeSeries('Completed Hours');
	this._remTasks = new Series('Remaining Tasks');
	this._compTasks = new Series('Completed Tasks');
	var bdd = this;
	this._firebase.on('child_added',function(snap){
		var obj = snap.val();
		if(typeof obj === "string"){
			return;
		}
		bdd._compHours.addPoint([obj.timestamp,obj.hours_completed]);
		bdd._estimHours.addPoint([obj.timestamp,obj.estimated_hours_remaining]);
		bdd._remTasks.addPoint([obj.timestamp,obj.tasks_remaining]);
		bdd._compTasks.addPoint([obj.timestamp,obj.tasks_completed]);
	});
	this._initialized = true;
};

BurndownData.prototype.estimatedHours = makeSeriesGetter('_estimHours');
BurndownData.prototype.hoursCompleted = makeSeriesGetter('_compHours');
BurndownData.prototype.tasksCompleted = makeSeriesGetter('_compTasks');
BurndownData.prototype.tasksRemaining = makeSeriesGetter('_remTasks');

//Initializes the Project object
function Project(firebase) {
    this.__firebase = firebase;
    this.uid = firebase.key();
    this.__name = firebase.child('name');
    this.__custom_categories = firebase.child('custom_categories');
    this.__description = firebase.child('description');
    this.__milestones = firebase.child('milestones');
    this.__dueDate = firebase.child('due_date');
    this.__members = firebase.child('members');
    this.__taskPercent = firebase.child('task_percent');
    this.__milestonePercent = firebase.child('milestone_percent');
    this.__hoursPercent = firebase.child('hours_percent');
	this.__burndownData = new BurndownData(firebase.child('burndown_data'));
    this.__totalPoints = firebase.root().child('users/' + user.uid + '/projects/' + this.uid + '/total_points');
};

Project.prototype = {
    getName: function (callback) {
        this.__name.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getCustomCategories: function (callback) {
        this.__custom_categories.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getDescription: function (callback) {
        this.__description.on('value', function (dat) {
            callback(dat.val());
        });
    },
    getTotalPoints: function (callback) {
        this.__totalPoints.on('value', function (dat) {
            callback(dat.val());
        });
    },
    setTotalPoints: function (points) {
        this.__totalPoints.set(points);
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
        this.__description.set(desc);
    },
    getButtonDiv: function () {
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
            pointsDiv = $('<div class="gamification">'),
			thisProject = this;
        projectA.append(nameSpan);
       	projectButton.append(projectA);
		memberButton.append(memberA);
        leftColumn.append(projectButton, descDiv, dueDiv, pointsDiv, memberButton);
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
			memberModalTiles.append(makeAddMemberTile(thisProject));
			thisProject.getMemberTiles(function(tile){
				memberModalTiles.prepend(tile);
			});
		});
        rightColumn.append(
            this.getMilestoneProgressBar(),
            this.getTaskProgressBar(),
            this.getHoursProgressBar()
        );
        project.append(leftColumn, rightColumn);
        projectA.click(function () {
            selectProject(thisProject);
        });
        this.getName(function (nameStr) {
            nameSpan.text(nameStr);
        });
        this.getDescription(function (descriptionStr) {
            descDiv.html(descriptionStr);
        });
        this.getTotalPoints(function (points) {
            pointsDiv.html("Project points: "  + points);
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
	renderBurndownChart:function(id){
		var proj = this;
		this.__name.once('value',function(name){
			var chart = new BurndownChart(id, name.val()+' Burn Down Chart',proj.__burndownData);
			chart.render();
		});
	}
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
		'custom_categories': {},
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
	this.__burndownData = new BurndownData(firebase.child('burndown_data'));
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
    },
	renderBurndownChart:function(id){
		var milestone = this;
		this.__name.once('value',function(name){
			var chart = new BurndownChart(id, name.val()+' Burn Down Chart',milestone.__burndownData);
			chart.render();
		});
	}
};

//Adds a new milestone to firebase based on the values of the modal's textfield inputs
function addNewMilestone() {
    var docName = $("#milestoneName").val();
    var docDescription = $("#milestoneDescription").val();
    var docDueDate = $("#milestoneDueDate").val();
    var docEstimatedHours = $("#milestoneEstimatedHours").val();
    var projectid = selectedProject.uid;

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

firebase.child('default_categories').on('child_added', function(category){
    defaultCategories.push(category.key());
});

function Bounty(firebase){
	this.uid = firebase.name();
	this.__claimed = firebase.child('claimed');
	this.__description = firebase.child('description');
	this.__due_date = firebase.child('due_date');
	this.__hour_limit = firebase.child('hour_limit');
	this.__line_limit = firebase.child('line_limit');
	this.__name = firebase.child('name');
	this.__points = firebase.child('points');
}

Bounty.Types = ["Lines","Hours"];

Bounty.Conditions = ["By Date","Whenever"];

Bounty.makeTypeSelect = function(onselect){
	return makeSelect(Bounty.Types,Bounty.Types[0],onselect);
};

Bounty.makeConditionSelect = function(onselect){
	return makeSelect(Bounty.Conditions,Bounty.Conditions[0],onselect);
};

Bounty.prototype = {
	getName:function(callback){
		this.__name.on('value',function(snap){
			callback(snap.val);
		});
	},
	getType:function(callback){
		this.getHourLimit(function(hours){
			if(hours > 0){
				callback('Hours');
			} else {
				callback('Lines');
			}
		});
	},
	getCondition:function(callback){
		this.getDueDate(function(date){
			if(date === 'No Due Date'){
				callback('Whenever');
			} else {
				callback('By Date');
			}
		});
	},
	getClaiment:function(callback){
		this.__claimed.on('value',function(snap){
			callback(snap.val());
		});
	},
	getHourLimit:function(callback){
		this.__hour_limit.on('value',function(snap){
			callback(snap.val());
		});
	},
	getLineLimit:function(callback){
		this.__line_limit.on('value',function(snap){
			callback(snap.val());
		});
	},
	getName:function(callback){
		this.__name.on('value',function(snap){
			callback(snap.val());
		});
	},
	getPoints:function(callback){
		this.__points.on('value',function(snap){
			callback(snap.val());
		});
	},
	getDueDate:function(callback){
		this.__due_date.on('value',function(snap){
			callback(snap.val());
		});
	},
	getRow:function(){
		var row = $('<div class="row">'),
			typeSpan = $('<span>'),
			tc = $('<div class="small-3 columns">'),
			condSpan = $('<span>'),
			coc = $('<div class="small-3 columns">'),
			claimSpan = $('<span>'),
			clc = $('<div class="small-3 columns">'),
			pointsSpan = $('<span>'),
			pc = $('<div class="small-3 columns">'),
			bounty = this;

		this.getType(function(type){
			if(type === 'Lines'){
				bounty.getLineLimit(function(lim){
					typeSpan.text(lim.toString()+' Lines');
				});
			} else {
				bounty.getHourLimit(function(lim){
					typeSpan.text(lim.toString()+' Hours');
				});
			}
		});

		this.getPoints(function(p){
			pointsSpan.html(p.toString()+' Points');
		});

		this.getCondition(function(cond){
			if(cond === "Whenever"){
				condSpan.text('');
			} else {
				bounty.getDueDate(function(date){
					condSpan.text("By "+date);
				});
			}
		});

		this.getClaiment(function(claiment){
			claimSpan.text('claimed by '+claiment);
		});
		row.append(
			tc.append(typeSpan),
			coc.append(condSpan),
			clc.append(claimSpan),
			pc.append(pointsSpan)
		);
		return row;
	}
}

function Task(firebase) {
    this.__firebase = firebase;
	this.__root = firebase.root();
    this.uid = firebase.key();
    this.__name = firebase.child('name');
    this.__description = firebase.child('description');
    this.__assigned_user = firebase.child('assignedTo');
    this.__custom_categories = firebase.parent().parent().parent().parent().child('custom_categories');
    this.__category = firebase.child('category');
    this.__status = firebase.child('status');
    this.__lines_of_code = firebase.child('total_lines_of_code');
    this.__due_date = firebase.child('due_date');
    this.__is_completed = firebase.child('is_completed');
    this.__hour_estimate = firebase.child('updated_hour_estimate');
    this.__bountiesPoints = firebase.child('bounties/points');
	this.__bounties = firebase.child('bounties');
};

Task.Statuses = [
    'New',
    'Implementation',
    'Testing',
    'Verify',
    'Regression',
    'Closed'
];

Task.Flags = [
    'true',
    'false'
];

Task.prototype = {
	getBounties: function(){
		return new Table(
			function(ref){
				return new Bounty(ref);
			},
			this.__bounties
		);
	},
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
    getCustomCategories: function (callback) {
        this.__custom_categories.on('value', function (dat) {
            callback(dat.val());
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
    getBountyPoints: function (callback) {
        this.__bountiesPoints.on('value', function (dat) {
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
        var cats = null;
        var stat = $('<td>');
        var user = $('<td>');
        var due = $('<td>');
        var flag = $('<td>');
        var hoursEstimate = $('<td>');
        var bountyPoints = $('<td class="gamification">');
        var task = this;
        var modal = $('#task-modal');

        row.append(name, desc, user, cat, stat, due, flag, hoursEstimate, bountyPoints);
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
        this.getCustomCategories(function (categories) {
            if(categories != null) {
                cats = Object.keys(categories);
            } else {
                cats = [];
            }
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
        this.getBountyPoints(function (bounty_point) {
            bountyPoints.html(bounty_point);
        });
        row.click(function () {
            modal.children().remove();
            task.__firebase.once('value', function (snap) {

				var nav = $('<nav class="breadcrumbs gamification">'),
					editA = $('<a class="current" href="#">Edit Task</a>'),
					bountyA = $('<a href="#">View Bounties</a>');

				var check = $('#gamification-switch');
				if(!check.is(':checked')){
					nav.hide();
				}

				nav.append(editA,bountyA);

				// make edit page
				var	taskEdit = $('<div id="task-edit">'),
					vals = snap.val(),
                	nameInput = $('<input type="text" value="' + vals.name + '">'),
                    descriptionInput = $('<textarea>' + vals.description + '</textarea>'),
                    userSelect = users.getSelect(function (user) {
                        selectedUser = user;
                    }, vals.assignedTo),
                    newCategory = $('<option id="newCategory" value="Add Category">Add Category</option>');
                    categoriesSelect = makeSelect(defaultCategories.concat(cats), vals.category).append(newCategory);
                    categoriesText = $('<input type="text">'),
                    statusSelect = makeSelect(Task.Statuses, vals.status),
                    dueInput = $('<input type="date" placeholder="yyyy-mm-dd" value="' + vals.due_date + '">'),
                    flagInput = makeSelect(Task.Flags, String(vals.is_completed)),
                    estHoursInput = $('<input type="text" value="' + vals.updated_hour_estimate + '">'),
                    eNameH = $('<h3>'),
					bNameH = $('<h3>'),
                    submit = $('<input class="button" value="Edit Task" />'),
                    taskError = $('<div id="task-error" class="my-error" hidden>All fields must be specified</div>');
				if(vals.bounties != undefined) {
					bountyPoints = $('<input type="text" value="' + vals.bounties.points + '">');
                } else {
					bountyPoints = $('<input type="text" value="' + 0 + '">');
				}
				task.getName(function (name) {
                    eNameH.text('Edit Task: ' + name);
					bNameH.text('Bounties for Task: ' + name);
                });
                $(categoriesText).hide();
                taskEdit.append(
                    eNameH,
                    label(nameInput, 'Name'),
                    label(descriptionInput, 'Description'),
                    label(userSelect, 'User'),
                    label(categoriesSelect, 'Category'),
                    categoriesText,
                    label(statusSelect, 'Status'),
                    label(dueInput, 'Due Date'),
                    label(flagInput, 'Is Complete'),
                    label(estHoursInput, 'Estimated Hours'),
                    //label(bountyPoints, 'Bounty Points')
                    submit,
                    taskError
                );

				// make bounty page
				var	taskBounties = $('<div id="task-bounties">'),
					list = $('<div>'),
					newBounty = $('<div id="row">'),
					numc = $('<div class="small-2 columns">'),
					num = $('<input type="number" placeholder="amount">'),
					addc = $('<div class="small-2 columns">'),
					add= $('<input type="button" class="button small" value="Add Bounty">'),
					datec = $('<div class="small-2 columns">'),
					date = $('<input type="date" placeholder="yyyy-mm-dd">'),
					typec = $('<div class="small-2 columns">'),
					typeS = Bounty.makeTypeSelect(),
					condc = $('<div class="small-2 columns">'),
					condS = Bounty.makeConditionSelect(function(){
						if(condS.val() === "Whenever"){
							datec.hide();
						} else {
							datec.show();
						}
					}),
					pointsc = $('<div class="small-2 columns">'),
					points = $('<input type="number" placeholder="points">');

					addc.append(add);
					numc.append(num);
					typec.append(typeS);
					datec.append(date);
					condc.append(condS);
					pointsc.append(points);
					newBounty.append(
						addc,
						numc,
						typec,
						condc,
						datec,
						pointsc
					);
					date.click(function(){
						date.fdatepicker({format: 'yyyy-mm-dd'});
						date.fdatepicker('show');
					});
				list.append(newBounty);
				task.getBounties().onItemAdded(function(bounty){
					list.prepend(bounty.getRow());
				});

				taskBounties.append(bNameH,list);
				taskBounties.hide();
				modal.append(nav,taskEdit,taskBounties);
				editA.click(function(){
					editA.attr('class','current');
					bountyA.attr('class','');
					taskEdit.show();
					taskBounties.hide();
				});

				bountyA.click(function(){
					bountyA.attr('class','current');
					editA.attr('class','');
					taskEdit.hide();
					taskBounties.show();
				});

				add.click(function(){
					msg = {
						claimed:"None",
						points:Number(points.val()),
						name:"Name",
						description:'Description'
					};
					if(typeS.val() === "Lines"){
						msg.line_limit = Number(num.val());
						msg.hour_limit = 'None';
					} else {
						msg.hour_limit = Number(num.val());
						msg.line_limit = 'None';
					}

					if(condS.val() === "Whenever"){
						msg.due_date = 'No Due Date';
					} else {
						msg.due_date = date.val();
					}
					task.__firebase.child('bounties').push(msg);
				});

                modal.keypress(function (e) {
                    if (e.which == 13) {
                        submit.click();
                    }
                });
                newCategory.click(function() {
                    $(categoriesSelect).hide();
                    $(categoriesText).show();
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

                    var categoryName = null;
                    if($(categoriesSelect).is(":visible")) {
                        categoryName = categoriesSelect.val();
                    } else {
                        categoryName = categoriesText.val();
                    }

                    task.__firebase.update({
                        name: nameInput.val(),
                        description: descriptionInput.val(),
                        assignedTo: userSelect.val(),
                        due_date: dueInput.val(),
                        category: categoryName,
                        //status: statusSelect.val(),
                        is_completed: flagVal,
                        //bounties: {points: bountyPoints.val()}
                        //updated_hour_estimate: estHours,
                    });
					var commit = {
						added_lines_of_code : 0,
						hours : 0,
						message : "direct change to task from UI",
						milestone : selectedMilestone.uid,
						project : selectedProject.uid,
						removed_lines_of_code : 0,
						status : statusSelect.val(),
						task : task.uid,
						timestamp : (new Date).getTime(),
						updated_hour_estimate : Number(estHoursInput.val()),
						user : userSelect.val()
					};
					console.log(commit);
					task.__root.child("commits/" + selectedProject.uid).push(commit);
					
                    var cate = {};
                    cate[categoryName] = true;
                    selectedProject.__custom_categories.update(cate);
                    $("#task-modal").foundation('reveal', 'close');
                });
            });
        });
		/*
        this.getTimeEstimate(function(time) {
            //console.log(time);
        });
		*/
        return row;
    },
    setUser: function (user) {
        var id = user;
        if (typeof id != 'string') {
            id = user.uid;
        }
        this.__assigned_user.set(id);
    },
    setCustomCategories: function (cat) {
        this.__custom_categories.update({cat : true});
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
    setBountyPoints: function (points) {
        this.__bountiesPoints.set(points);
    },
    off: function () {
        this.__firebase.off();
    }
};

function newTask() {
    var cats;
    var nameInput = $('<input type="text">'),
        descriptionInput = $('<textarea>'),
        userSelect = users.getSelect(function (user) {
            selectedUser = user;
        }, user.uid),
        newCategory = $('<option id="newCategory" value="Add Category">Add Category</option>'),
        categoriesSelect = makeSelect(defaultCategories.concat(cats), "Feature").append(newCategory),
        categoriesText = $('<input type="text" hidden=true>'),
        statusSelect = makeSelect(Task.Statuses, "New"),
        estHoursInput = $('<input type="text">'),
        bountyInput = $('<input type="text">'),
        nameH = '<h3>Add New Task</h3>',
        submit = $('<input class="button" value="Add Task" />'),
        modal = $('#task-modal'),
        //completed = makeSelect(Task.Flags, 'false');
        dueInput = $('<input type="text" placeholder="yyyy-mm-dd">'),
        taskError = $('<div id="task-error" class="my-error" hidden>All fields must be specified</div>');

	$('#task-modal').foundation('reveal','open');
    selectedProject.getCustomCategories(function(categories){
		if(categories){
       		cats = Object.keys(categories);
		} else {
			cats = [];
		}
		modal.children().remove();
		$(categoriesText).hide();
		modal.append(
			nameH,
			label(nameInput, 'Name'),
			label(descriptionInput, 'Description'),
			label(userSelect, 'User'),
			label(categoriesSelect, 'Category'),
			categoriesText,
			label(statusSelect, 'Status'),
			label(estHoursInput, 'Estimated Hours'),
			//label(completed, 'Is Completed'),
			label(dueInput, "Due Date"),
            label(bountyInput,"Bounty Points"),
			submit,
			taskError
		);
		newCategory.click(function() {
			$(categoriesSelect).hide();
			$(categoriesText).show();
		});
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
			var categoryName = null;
			if($(categoriesSelect).is(":visible")) {
				categoryName = categoriesSelect.val();
			} else {
				categoryName = categoriesText.val();
			}
			var cate = {};
			cate[categoryName] = true;
			selectedProject.__custom_categories.update(cate);
			selectedMilestone.__tasks.push({
				name: nameInput.val(),
				description: descriptionInput.val(),
				assignedTo: userSelect.val(),
				category: categoryName,
				status: statusSelect.val(),
				original_hour_estimate: estHours,
				is_completed: false,    //default task to uncompleted
				due_date: dueInput.val(),
                bounties: {points: bountyInput.val()} 
			});
			$("#task-modal").foundation('reveal', 'close');
		});

    });
}
function taskStatics(){
	$('#taskContainer').foundation('reveal','open');
    // console.log(selectedMilestone);
    drawTaskStuff(selectedProject.uid,selectedMilestone.uid,firebase);

}

function MyTasks(firebase) {
    this.__firebase = firebase;
    this.uid = firebase.key();
    this.__name = firebase.child('name');
    this.__description = firebase.child('description');
    this.__assigned_user = firebase.child('assignedTo');
    this.__custom_categories = firebase.parent().parent().parent().parent().child('custom_categories');
    this.__category = firebase.child('category');
    this.__status = firebase.child('status');
    this.__lines_of_code = firebase.child('total_lines_of_code');
    this.__due_date = firebase.child('due_date');
    this.__is_completed = firebase.child('is_completed');
    this.__hour_estimate = firebase.child('updated_hour_estimate');
    this.__bountiesPoints = firebase.child('bounties/points');
};

MyTasks.prototype = {
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
    getCustomCategories: function (callback) {
        this.__custom_categories.on('value', function (dat) {
            callback(dat.val());
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
    getBountyPoints: function (callback) {
        this.__bountiesPoints.on('value', function (dat) {
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
        var row = $('<tr class="task-row">');
        var name = $('<td>');
        var desc = $('<td>');
        var cat = $('<td>');
        var cats = null;
        var stat = $('<td>');
        var user = $('<td>');
        var due = $('<td>');
        var flag = $('<td>');
        var hoursEstimate = $('<td>');
        var bpts = $('<td>');
        var task = this;

        row.append(name, desc, user, cat, stat, due, flag, hoursEstimate,bpts);
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
        this.getCustomCategories(function (categories) {
            if(categories != null) {
                cats = Object.keys(categories);
            } else {
                cats = [];
            }
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
        this.getBountyPoints(function (points) {
            bpts.html(points);
        });
        return row;
    },
    off: function () {
        this.__firebase.off();
    }
};

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
                    firebase.child("users/" + user.uid + "/total_points").on("value", function(snap) {
                        $("#currentPoints").html("Points: " + snap.val());
                    });
                    // drawUserStatistics(firebase,user.uid);
                }
				milestonePage = $('#milestones-page');
			    projectPage = $('#projects-page');
			    taskPage = $('#tasks-page');
                myTasksPage = $('#my-tasks-page');
				profilePage = $('#profile-page');
                myStatisticsPage =  $('#my-statistics-page');
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

$(function(){
	var check = $('#gamification-switch');
	check.prop('checked',true)
	check.change(function(){
		var checked = check.is(':checked');
		if(checked){
			$('.gamification').show()
		} else {
			$('.gamification').hide();
		}
	});
});
