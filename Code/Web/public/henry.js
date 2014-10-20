

/* this file contains classes and utility functions that are used everywhere on the website */
var firebase = new Firebase("https://henry-test.firebaseIO.com");
var userData;

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
		this.__selectCount = (this.__selectCount || 0) + 1;
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
	}
};

function addNewMember(){
	var projectID = '-JYcg488tAYS5rJJT4Kh';
	var selected = $("#member-select").val();
	var id = $("#member-select").children(":selected").attr("id").substring(9);
	//console.log(firebase.child('projects/'+projectID).child("members").name());
	/*firebase.child('projects/'+projectID).child("members").forEach(function(cs) {
		var name = cs.name();
		console.log(name);
		if(name != id)
		cs.push(id);
	});
	*/
	//firebase.child('projects/'+projectID).child("members").child(id).set(id);
	//if(!firebase.child('projects/'+projectID).child("members").val().hasChild(id)){
		firebase.child('projects/'+projectID).child("members").push(id);
	//}
	//console.log(id);
}
function makeProgressBar(divClass,percentRef){
	var progress = $('<div class="progress '+divClass+'">');
	var span = $('<span class="meter">');
	progress.append(span);
	percentRef.on('value',function(snap){
		span.width(String(snap.val())+"%");
	});
	return progress;
}

function Project(firebase){
	this.__firebase = firebase;
	this.uid = firebase.name();
	this.__name = firebase.child('name');
	this.__description = firebase.child('description');
	this.__milestones = firebase.child('milestones');
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
	setDescription:function(desc){
		this.__description.set(description);
	},
	getButtonDiv:function(callback){
		var project = $('<div class="row">'),
			leftColumn = $('<div class="small-4 columns small-offset-1">'),
			rightColumn = $('<div class="small-4 columns small-offset-2 left">'),
			button = $('<div class="button expand text-center">'),
			a = $('<a>'),
			nameH3 = $('<h3>'),
			descDiv = $('<div>');

		a.append(nameH3);
		button.append(a);
		leftColumn.append(button,descDiv);
		rightColumn.append(
			this.getMilestoneProgressBar(),
			this.getTaskProgressBar(),
			this.getHoursProgressBar()
		);
		project.append(leftColumn,rightColumn);
		var p = this;
		a.click(function(){
			selectProject(p);
		});
		this.getName(function(nameStr){
			nameH3.text(nameStr);
		});

		this.getDescription(function(descriptionStr){
			descDiv.html(descriptionStr);
		});
		return project;
	},
	getMilestones:function() {
		return new Table(function(fb){ return new Milestone(fb);},this.__firebase.child('milestones'));
	},
	addUser:function(id){
		this.__members.set(id,id);
	},
	getOption:function(){
		var option = $('<option value="'+this.uid+'"></option>');
		this.getName(function(name){
			option.text(name);
		});
		return option;
	},
	getTaskProgressBar:function(){
		return makeProgressBar('small-12',this.__taskPercent);
	},
	getHoursProgressBar:function(){
		return makeProgressBar('small-12',this.__hoursPercent);
	},
	getMilestoneProgressBar:function(){
		return makeProgressBar('small-12',this.__milestonePercent);
	}
};

function Milestone(firebase){
	this.__firebase = firebase;
	this.uid = firebase.name();
	this.__name = firebase.child('name');
	this.__description = firebase.child('description');
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
	getDescription:function(callback){
		this.__description.on('value',function(dat){
			callback(dat.val());
		});
	},
	getButtonDiv:function(){
		var milestone = $('<div class="row">'),
			leftColumn = $('<div class="small-4 columns small-offset-1">'),
			rightColumn = $('<div class="small-4 columns small-offset-2 left">'),
			button = $('<div class="button expand text-center">'),
			a = $('<a>'),
			nameH3 = $('<h3>'),
			descDiv = $('<div>');

		a.append(nameH3);
		button.append(a);
		leftColumn.append(button,descDiv);
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
		return milestone;
	}, 
	getTasks:function() {
		return new Table(function(fb){ return new Task(fb);},this.__tasks);
	},
	getMembers:function(){
		return new ReferenceTable(users,this.__members);
	},
	getTaskProgressBar:function(){
		return makeProgressBar('small-12',this.__taskPercent);
	},
	getHoursProgressBar:function(){
		return makeProgressBar('small-12',this.__hoursPercent);
	}
};

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
	getTableRow:function(){
		var row = $('<tr>');
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
			console.log('select');
			task.setUser(user);
			userSelect.hide();
			userName.show();
		});
		userSelect.hide();
		user.append(userSelect);
		user.append(userName);
		user.click(function(){
			userSelect.show();
			userName.hide();
		});

		this.getAssignedUser(function(assignedUser){
			assignedUser.getName(function(name){
				userName.text(name);
			});
		});

		this.getCategory(function(categoryStr){
			cat.html(categoryStr);
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
	}
};

function getLoginData(){
	var user = $("#user").val();
	var pass = $("#pass").val();
	document.getElementById("pass").value = "";
	login(user, pass);
}

function login(user, pass){
	firebase.authWithPassword({
		email: user,
		password: pass
	}, function(error, authData) {
		if (error === null) {
			userData = authData;
			window.location.replace("projects");
		} else {
			logout();
			$("#loginError").show();
		}
	});
};

function register(){
	var user = $("#user").val();
	var pass = $("#pass").val();
	firebase.createUser(
		{
			email: user,
			password: pass
		}, 
		function(error) {
			if (error === null) {
				login(user, pass);
			} else {
				$("#registerError").show();
			}
		}
	);
}

function logout() {
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
					firebase.child('users/'+userData.uid).child("email").set(userData.password.email);
					$("#currentUser").show();
					$("#logoutButton").show();
					$("#loginButton").hide();
				}
			}
		);
	}
);

