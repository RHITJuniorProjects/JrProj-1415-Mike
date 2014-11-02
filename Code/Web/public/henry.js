

/* this file contains classes and utility functions that are used everywhere on the website */
var firebase = new Firebase("https://henry-test.firebaseIO.com");
var userData;
var currentProject;

// table object manages a table of values in the database, use get to get objects from the database
// by uid

function Table(factory,firebase){
	this.__factory = factory;
	this.__firebase = firebase;
};

Table.prototype = {
	get:function(uid){
		return this.__factory(this.__firebase.child(uid));
	},
	onItemAdded:function(callback){
		//console.log(this.__onChildAdded);
		var table = this;
		this.__firebase.on('child_added',function(snap){
			var val = table.__factory(snap.ref());
			callback(val);
		});
	},
	onItemRemoved:function(callback){
		var table = this;
		this.__firebase.on('child_removed',function(snap){
			var val = table.__factory(snap.ref());
			callback(val);
		});
	},
	off:function(){
		this.__firebase.off();
	},
	add:function(id){
		var ref;
		if(id){
			ref = this.__firebase.child(id);
		} else {
			ref = this.__firebase.push();
		}
		return this.__factory(ref);
	},
	getSelect:function(onselect){
	var select = $('<select>');
		if(onselect){
			var table = this;
			select.change(function(){
				onselect(table.get(select.val()));
			});
		}
		this.onItemAdded(function(item){
			select.append(item.getOption());
		});
		return select;
	},
	sort:function(comparator){

	}
};

function ReferenceTable(referencedTable,firebase){
	this.__factory = function(ref){
		return referencedTable.get(ref.name());
	};
	this.__firebase = firebase;
}

ReferenceTable.prototype = Table.prototype;

function User(firebase){
	this.__firebase = firebase;
	this.uid = firebase.name();
	this.__projects = firebase.child('projects');
	this.__name = firebase.child('name');
	this.__email = firebase.child('email');
}

User.prototype = {
	getName:function(callback){
		this.__name.on('value',function(dat){
			callback(dat.val());
		});
	},
	getOption:function(){
		var option = $('<option value="'+this.uid+'"></option>');
		this.getName(function(name){
			option.text(name);
		});
		return option;
	},
	getProjects:function(){
		return new ReferenceTable(projects,this.__projects);
	},
	getProjectStats:function(){
		var user = this;
		return new Table(
			function(ref){ return new ProjectStats(ref);});
	},
	getRole:function(project,callback){
		return project.getRole(callback);
	},
	getMilestoneTasks:function(milestone){

	},
	onProjectTaskAdded:function(project,callback){
		var projectData = this.__projects.child(project.uid),
			milestones = project.getMilestones(),
			milestoneData = projectData.child('milestones');

		console.log('check 1');
		milestoneData.on('child_added',function(snap){
			var milestoneData = snap.ref(),
				milestone = milestones.get(snap.name()),
				taskData = milestoneData.child('tasks'),
				tasks = milestone.getTasks();
			console.log('check 2');
			taskData.on('child_added',function(snap){
				taskid = snap.name();
				callback(tasks.get(taskid));
			});
		});
	},
	getAllTasks:function(){

	},
	getMemberTile:function(project){
		var tile = $(
				'<dl class="row collapse accordian outlined" data-accordion>'
			),
			memberDD = $(
				'<dd class="accordion-navigation">'
			),
			memberA = $(
				'<a href="#member-'+this.uid+'">'
			),
			nameH4 = $('<h4>'),
			roleSpan = $('<span>'),
			milestonePanel = $(
				'<div id="member-'+this.uid+'" class="content panel row">'
			);

		this.getName(function(name){
			nameH4.text(name);
		});

		project.getRole(this,function(role){
			roleSpan.text(role);
		});

		this.milestones.onItemAdded(function(milestone){
			
		});

		this.onProjectTaskAdded(project,function(task){
			taskPanel.append(task.getDescriptionDiv());
		});
		memberA.append(nameH4,roleSpan);
		memberDD.append(memberA,taskPanel);
		tile.append(memberDD);
		return tile;
	},
	off:function(){
		this.__firebase.off();
	}
};


// A function that is used to add memebers
function addNewMember(){
	var projectID =  currentProject.uid;
	var selected = $("#member-select").val();

	var id = $("#member-select").children(":selected").attr("id").substring(9);//+ ": " + "developer";
	firebase.child('projects/'+projectID).child("members").push(id);
	
}


User.ProjectData = function(user,ref){
	this.user = user;
	this.__firebase = ref;
	this.uid = ref.name();
	this.__added_lines_of_code = ref.child('added_lines_of_code');
	this.__removed_lines_of_code = ref.child('removed_lines_of_code');
	this.__total_lines_of_code = ref.child('total_lines_of_code');
	this.__milestones = ref.child('milestones');
};

User.ProjectData.prototype = {
	getProject:function(){
		return projects.get(this.uid);
	},
	getMilestoneData:function(){
		return new Table(
			function(ref){return new User.MilestoneData(this.user,ref)},
			this.__milestones
		);
	},
	getMilestones:function(){
		return new ReferenceTable(
			this.getProject().getMilestones,
			this.__milestones
		);
	}
};

User.MilestoneData = function(user,ref){
	this.user = user;
	this.__firebase = ref;
	this.uid = ref.name();
	this.__added_lines_of_code = ref.child('added_lines_of_code');
	this.__removed_lines_of_code = ref.child('removed_lines_of_code');
	this.__total_lines_of_code = ref.child('total_lines_of_code');
	this.__tasks = ref.child('tasks');
};

User.MilestoneData.prototype = {
	getMilestone:function(){

	}
};

function makeProgressBar(divClass,text,percentRef){
	var div = $('<div>');
	var label = $('<h4>'+text+'</h4>');
	var progress = $('<div class="progress '+divClass+'">');
	var span = $('<span class="meter">');
	div.append(label,progress);
	progress.append(span);
	percentRef.on('value',function(snap){
		span.width(String(snap.val())+"%");
	});
	return div;
}

function Project(firebase){
	this.__firebase = firebase;
	this.uid = firebase.name();
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
	getName:function(callback){
		this.__name.on('value',function(dat){
			callback(dat.val());
		});
	},
	getDescription:function(callback){
		this.__description.on('value',function(dat){
			callback(dat.val());
		});
	},
	setName:function(name){
		this.__name.set(name);
	},
	getDueDate:function(callback){
		this.__dueDate.on('value',function(dat){
			callback(dat.val());
		});
	},
	setDueDate:function(due){
		this.__dueDate.set(due);
	},
	setDescription:function(desc){
		this.__description.set(description);
	},
	getButtonDiv:function(callback){
		var project = $('<div class="row project">'),
			leftColumn = $('<div class="small-4 columns small-offset-1">'),
			rightColumn = $('<div class="small-4 columns small-offset-2 left">'),
			button = $('<div>'),
			a = $('<a class="button expand text-center">'),
			nameH3 = $('<h3>'),
			descDiv = $('<div>'),
			dueDiv = $('<div>');

		a.append(nameH3);
		button.append(a);
		leftColumn.append(button,descDiv,dueDiv);
		rightColumn.append(
			this.getMilestoneProgressBar(),
			this.getTaskProgressBar(),
			this.getHoursProgressBar()
		);
		project.append(leftColumn,rightColumn);
		var p = this;
		a.click(function(){
			selectProject(p);
			currentProject = p;
		});
		this.getName(function(nameStr){
			nameH3.text(nameStr);
		});

		this.getDescription(function(descriptionStr){
			descDiv.html(descriptionStr);
		});
		this.getDueDate(function(dateStr){
			if(dateStr){
				dueDiv.html('Due:' + dateStr);
			}
		});
		return project;
	},
	getMilestones:function() {
		return new Table(function(fb){ return new Milestone(fb);},this.__firebase.child('milestones'));
	},
	// adds the user specified by either a user object or the uid of the user in the database,
	// the role by default is "Developer", this function can also be used to change a role of a user
	addMember:function(user,role){
		if(!role){
			role = "Developer";
		}
		if(typeof user == "object"){
			user = user.uid;
		}
		this.__members.set(user,role);
	},
	getOption:function(){
		var option = $('<option value="'+this.uid+'"></option>');
		this.getName(function(name){
			option.text(name);
		});
		return option;
	},
	getTaskProgressBar:function(){
		return makeProgressBar('small-12','Tasks Completed',this.__taskPercent);
	},
	getHoursProgressBar:function(){
		return makeProgressBar('small-12','Hours Completed',this.__hoursPercent);
	},
	getMilestoneProgressBar:function(){
		return makeProgressBar('small-12','Milestones Completed',this.__milestonePercent);
	},
	off:function(){
		this.__firebase.off();
	},
	getRole:function(member,callback){
		this.__members.child(member.uid).on('value',function(snap){
			callback(snap.val());
		});
	},
	getMembers:function(){
		return new ReferenceTable(users,this.__members);
	}
};
// creates new projects and are added in to the firebase database
function addNewProject(){
	// if(userData != null) {
		var docName = $("#projectName").val();
		var docDescription = $("#projectDescription").val();
		var docDueDate = $("#projectDueDate").val();
		var docEstimatedHours = $("#projectEstimatedHours").val();
		var currentUser = userData.uid; //+ ": " + "lead";
		var project = firebase.child('projects').push(
			{ 'name': docName, 'description': docDescription, 
			'due_date': docDueDate, 'total_estimated_hours': docEstimatedHours, 'members': currentUser});
		$('#myProjectModal').trigger('reveal:close');
	//}	

}

function Milestone(firebase){
	this.__firebase = firebase;
	this.uid = firebase.name();
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
	getName:function(callback){
		this.__name.on('value',function(dat){
			callback(dat.val());
		});
	},
	getDueDate:function(callback){
		this.__dueDate.on('value',function(dat){
			callback(dat.val());
		});
	},
	getDescription:function(callback){
		this.__description.on('value',function(dat){
			callback(dat.val());
		});
	},
	getButtonDiv:function(){
		var milestone = $('<div class="row milestone">'),
			leftColumn = $('<div class="small-4 columns small-offset-1">'),
			rightColumn = $('<div class="small-4 columns small-offset-2 left">'),
			button = $('<div>'),
			a = $('<a class="button expand text-center">'),
			nameH3 = $('<h3>'),
			descDiv = $('<div>'),
			dueDiv = $('<div>');

		a.append(nameH3);
		button.append(a);
		leftColumn.append(button,descDiv,dueDiv);
		rightColumn.append(this.getTaskProgressBar(),this.getHoursProgressBar());
		milestone.append(leftColumn,rightColumn);
		var m = this;
		a.click(function(){
			selectMilestone(m);
		});
		this.getName(function(nameStr){
			nameH3.text(nameStr);
		});

		this.getDescription(function(descriptionStr){
			descDiv.html(descriptionStr);
		});
		this.getDueDate(function(dueStr){
			if(dueStr){
				dueDiv.html('Due:' + dueStr);
			}
		});
		return milestone;
	}, 
	getTasks:function() {
		return new Table(function(fb){ return new Task(fb);},this.__tasks);
	},
	getMembers:function(){
		return new ReferenceTable(users,this.__members);
	},
	getTaskProgressBar:function(){
		return makeProgressBar('small-12','Tasks Completed',this.__taskPercent);
	},
	getHoursProgressBar:function(){
		return makeProgressBar('small-12','Hours Completed',this.__hoursPercent);
	},
	off:function(){
		this.__firebase.off();
	}
};

function addNewMilestone(){
	var docName = $("#milestoneName").val();
	var docDescription = $("#milestoneDescription").val();
	var docDueDate = $("#milestoneDueDate").val();
	var docEstimatedHours = $("#milestoneEstimatedHours").val();
	var projectid = currentProject.uid;
	var milestone = firebase.child('projects/'+ projectid).child('milestones').push(
		{ 'name': docName, 'description': docDescription, 
		'due_date': docDueDate, 'estimated_hours': docEstimatedHours});
	

}

function Task(firebase){
	this.__firebase = firebase;
	this.uid = firebase.name();
	this.__name = firebase.child('name');
	this.__description = firebase.child('description');
	this.__assigned_user = firebase.child('assignedTo');
	this.__category = firebase.child('category');
	this.__originalTime = firebase.child('original_time_estimate');
	this.__updatedTime = firebase.child('updated_time_estimate');
};

Task.Categories = [
	'New',
	'Implementation',
	'Testing',
	'Verify',
	'Regression',
	'Closed'
];

Task.prototype = {
	getName:function(callback){
		this.__name.on('value',function(dat){
			callback(dat.val());
		});
	},
	getDescription:function(callback){
		this.__description.on('value',function(dat){
			callback(dat.val());
		});
	},
	getAssignedUser:function(callback){
		this.__assigned_user.on('value',function(dat){
			var user = users.get(dat.val());
			callback(user);
		});
	},
	getCategory:function(callback){
		this.__category.on('value',function(dat){
			callback(dat.val());
		});
	},
	getCategorySelect:function(onselect){
		var select = $('<select>');
		Task.Categories.forEach(function(category){
			select.append('<option value="'+category+'">'+category+'</option>');
		});
		if(onselect){
			select.change(function(){
				onselect(select.val());
			});
		}
		return select;
	},
	getOriginalTime:function(callback){
		this.__originalTime.on('value',function(dat){
			callback(dat.val());
		});
	},
	getUpdatedTime:function(callback){
		this.__updatedTime.on('value',function(dat){
			callback(dat.val());
		});
	},
	getDescriptionDiv:function(){
		var task = $('<div>'),
			nameH4 = $('<h4>'),
			descSpan = $('<span>'),
			catSpan = $('<span>');
		this.getName(function(name){
			nameH4.text(name);
		});
		this.getDescription(function(description){
			descSpan.text(description);
		});
		this.getCategory(function(cat){
			catSpan.text(cat);
		});
		task.append(nameH4,descSpan,catSpan);
		return task;
	},
	getTableRow:function(){
		var row = $('<tr class="task-row">');
		var name = $('<td>');
		var desc = $('<td>');
		var cat = $('<td>');
		var user = $('<td>');
		var originalTime = $('<td>');
		var updatedTime = $('<td>');
		var task = this;
		row.append(name,desc,cat,user,originalTime,updatedTime);
		this.getName(function(nameStr){
			name.html(nameStr);
		});

		this.getDescription(function(descriptionStr){
			desc.html(descriptionStr);
		});

		var userName = $('<span>');
		var userSelect = users.getSelect(function(user){
			task.setUser(user);
			userSelect.blur();
			userSelect.hide();
			userName.show();
		});
		userSelect.hide();
		user.click(function(){
			userName.hide();
			userSelect.show();
		});
		user.append(userName,userSelect);

		this.getAssignedUser(function(assignedUser){
			assignedUser.getName(function(name){
				userName.text(name);
			});
		});

		var catLabel = $('<span>');
		var catSelect = this.getCategorySelect(function(category){
			task.setCategory(category);
			catSelect.blur();
			catSelect.hide();
			catLabel.show();
		});
		catSelect.hide();
		catLabel.click(function(){
			catLabel.hide();
			catSelect.show();
		});
		cat.append(catLabel,catSelect);
		this.getCategory(function(categoryStr){
			catLabel.text(categoryStr);
		});

		this.getOriginalTime(function(original_time_estimateStr){
			originalTime.html(original_time_estimateStr);
		});

		this.getUpdatedTime(function(updated_time_estimateStr){
			updatedTime.html(updated_time_estimateStr);
		});
		return row;
	},
	setUser:function(user){
		var id = user;
		if(typeof id != 'string'){
			id = user.uid;
		}
		this.__assigned_user.set(id);
	},
	setCategory:function(cat){
		this.__category.set(cat);
	},
	off:function(){
		this.__firebase.off();
	}
};


function getLoginData(){ // Takes the login data from the form and places it into variables
	var user = $("#user").val();
	var pass = $("#pass").val();
	document.getElementById("pass").value = "";
	login(user, pass, false);
}

function login(user, pass, registering){ // Authenticates with Firebase, giving a callback that will 
							// cause the window to go to projects if it was successful, 
							// else go back to login and show the invalid input message.
	firebase.authWithPassword({
		email: user,
		password: pass
	}, function(error, authData) {
		if (error === null) {
			userData = authData;
			if(registering){
				firebase.child('users/'+userData.uid).update(
					{
						email: userData.password.email,
						github: $("#githubuser").val(),
						name: $("#name").val()
					}
				);
			}
			window.location.replace("projects");
		} else {
			logout();
			$("#loginError").show();
		}
	});
};

function register(){ 		// Registers a new user with Firebase, and also adds that user
							// to our local database (making a new developer/manager)
	var user = $("#user").val();
	var pass = $("#pass").val();
	var githubName = $("#githubuser").val();
	var name = $("#name").val();
	if(!user || !pass || !githubName || !name){
		$("#registerError").show();
		return;
	} 
	firebase.createUser(
		{
			email: user,
			password: pass,
		}, 
		function(error) {
			if (error === null) {
				login(user, pass, true);
			} else {
				$("#emailError").show();
			}
		}
	);
}

function logout() { 		// Shows the login button and hides the current user and logout
	firebase.unauth();
	$("#currentUser").hide();
	$("#logoutButton").hide();
	$("#loginButton").show();
	userData = null;
	window.location.replace("/");
};

var projects = new Table(function(fb){ return new Project(fb);},firebase.child('projects'));
var users = new Table(function(fb){ return new User(fb);},firebase.child('users'));

firebase.onAuth(
	function(authData){
		userData = authData;
		$(document).ready(
			function(){
				if(userData == null){
					$("#currentUser").hide();
					$("#logoutButton").hide();
					$("#loginButton").show();
				} else {
					$("#currentUser").html("Currently logged in as " + userData.password.email);
					$("#currentUser").show();
					$("#logoutButton").show();
					$("#loginButton").hide();
				}
			}
		);
	}
);

