/* this file contains classes and utility functions that are used everywhere on the website */
var firebase = new Firebase("https://henry-test.firebaseIO.com");
var user;
var milestonePage;
var projectPage;
var taskPage;
var profilePage;
var selectedProject;
var selectedMilestone;
var myTasks;
var myStatisticsPage;
var myTrophyStorePage;
var defaultCategories = [];
var userTrophies = [];
// var selectedUserTrophies = [];


function backFromStore(){
	console.log("going back");
	window.location = "https://henry-test.firebaseapp.com/projects"
}

function User(firebase2) {
	this.__firebase = firebase2;
	this.uid = firebase2.key();
	this.__projects = firebase2.child('projects');
	this.__name = firebase2.child('name');
	this.__email = firebase2.child('email');
	this.__trophies = firebase2.child('trophies');
	this.__all_projects = firebase.child('projects');
	this.__total_points = firebase2.child('total_points');
	this.__avail_points = firebase2.child('available_points');
}

User.prototype = {
	getName: function (callback) {
		this.__name.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getBountyPoints: function(callback){
		this.__avail_points.once('value', function (snap) {
			callback(snap.val());
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
				milestone = milestones.get(snap.key()),
				taskData = milestoneData.child('tasks'),
				tasks = milestone.getTasks();
			taskData.orderByChild("name").on('child_added', function (snap) {
				taskid = snap.key();
				callback(tasks.get(taskid));
			});
		});
	},
	getTasks: function (panel) {
		var projects = this.getProjects();
		var table = new Table
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
	getPoints: function (callback) {
		this.__total_points.on('value', function (snap) {
			callback(snap.val());
		});
	},
	setPoints: function (pts) {
		this.__avail_points.set(pts);
	},
	getTrophies: function() {
		var $panel = $('#profile-trophies');
		$panel.children().remove();

		this.__trophies.orderByChild("name").on('child_added', function (snap) {
			var val = new Trophy(snap.ref());
			userTrophies[userTrophies.length] = val;
		});
		trophies.onItemAdded(function(trophy){
			var arrayLength = userTrophies.length;
			var needs = true;
			for (var i = 0; i < arrayLength; i++) {
				if(userTrophies[i].uid == trophy.uid) {

					needs = false;
					break;
				}
			
			}
			if (Boolean(needs)) {
				$panel.append(trophy.getTableRow());
			}              
		});
	},
	off: function (arg1, arg2) {
		this.__firebase.off(arg1, arg2);
	}
};

// table object manages a table of values in the database, use get to get objects from the database
// by uid
function selectProject(project){
	milestonePage.show();
	projectPage.hide();
	taskPage.hide();
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
	myTrophyStorePage.hide();
	taskPage.show();
	profilePage.hide();
	if(selectedMilestone){
		selectedMilestone.off();
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
	var trophyPanel = $('#profile-trophies');
	trophyPanel.children().remove();
	user.getTrophies();
}

function showProjects(){
	drawUserStatistics(firebase,user.uid);
	drawProjectStuff(firebase);
	milestonePage.hide();
	taskPage.hide();
	// myTrophyStorePage.hide();
	profilePage.hide();
	projectPage.show();
	myStatisticsPage.hide();
	selectMyTasks();
	getTrophies();
}

// function showMyTrophyStore() {
//     milestonePage.hide();
//     projectPage.hide();
//     taskPage.hide();
//     myStatisticsPage.hide();
//     myTrophyStorePage.show();
// }

function selectMyTasks(){
	var $panel = $('#my-tasks-rows');
	$panel.children().remove();
	user.getTasks($panel);
}

function showMyStatsticsPage(){
	milestonePage.hide();
	projectPage.hide();
	taskPage.hide();
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

firebase.child('default_categories').on('child_added', function(category){
	defaultCategories.push(category.key());
});

var projects = new Table(function (fb) {
	return new Project(fb);
}, firebase.child('projects'));

var users = new Table(function (fb) {
	return new User(fb);
}, firebase.child('users'));


var trophies = new Table(function (fb) {
	// console.log(user);
	return new Trophy(fb,user);
}, firebase.child('trophies'));


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
				profilePage = $('#profile-page');
				myTrophyStorePage = $('#store-page');
				myStatisticsPage =  $('#my-statistics-page');
				getAllUsers();
				showProjects();
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
