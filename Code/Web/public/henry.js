
/* this file contains classes and utility functions that are used everywhere on the website */
var firebase = new Firebase("https://henry-test.firebaseIO.com");
var userData;
// table object manages a table of values in the database, use get to get objects from the database
// by uid
function Table(factory,firebase){
	this.__onChildAdded = null;
	this.__onChildRemoved = null;
	this.__factory = factory;
	this.__firebase = firebase;
};

Table.prototype = {
	get:function(uid){
		return this.__factory(this.__firebase.child(uid));
	},
	onItemAdded:function(callback){
		//console.log(this.__onChildAdded);
		if(!this.__onChildAdded){
			this.__onChildAdded = [];
			var table = this;
			this.__firebase.on('child_added',function(snap){
				//console.log(snap.val());
				var val = table.__factory(snap.ref());
				table.__onChildAdded.forEach(function(callback){
					callback(val);
				});
			});
		}
		this.__onChildAdded.push(callback);
	},
	onItemRemoved:function(callback){
		if(!this.__onChildRemoved){
			this.__onChildRemoved = [];
			this.__firebase.on('child_removed',function(snap){
				var val = this.__factory(snap.val());
				this.__onChildRemoved.forEach(function(callback){
					callback(val);
				});
			});
		}
		this.__onChildRemoved.push(callback);
	}
};

var users = new Table(function(fb){return new User(fb);},firebase.child('users'));

function User(firebase){
	this.__firebase = firebase;
	this.uid = firebase.name();
	this.__name = firebase.child('name');
	this.__email = firebase.chile('email');
}

User.prototype = {
	getName:function(callback){
		this.__name
	},
	getProjects:function(){

	}
};

function Project(firebase){
	this.__firebase = firebase;
	this.uid = firebase.name();
	this.__name = firebase.child('name');
	this.__description = firebase.child('description');
	this.__milestones = firebase.child('milestones');
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
	getButtonHtml:function(callback){
		callback('<div class="row">'+
		'<div class="small-4 columns small-offset-1">'+
		'<div class="button expand text-center">'+
		'<a onclick="selectProject(\''+this.uid+'\')"><h3 id="project-name-'+this.uid+'"></h3></a>'+
		'</div>'+
		'<div id="project-description-'+this.uid+'"></div>'+
		'</div>'+
		'</div>');
		var name = $('#project-name-'+this.uid);
		this.getName(function(nameStr){
			name.html(nameStr);
		});
		var description = $('#project-description-'+this.uid);
		this.getDescription(function(descriptionStr){
			description.html(descriptionStr);
		});
	},
	getMilestones:function() {
		return new Table(function(fb){ return new Milestone(fb);},this.__firebase.child('milestones'));
	
	}
};

function Milestone(firebase){
	this.__firebase = firebase;
	this.uid = firebase.name();
	this.__name = firebase.child('name');
	this.__description = firebase.child('description');
	this.__task = firebase.child('tasks');
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
	getButtonHtml:function(callback){
		callback('<div class="row">'+
		'<div class="small-4 columns small-offset-1">'+
		'<div class="button expand text-center">'+
		'<a onclick="selectMilestone(\''+this.uid+'\'"><h3 id="milestone-name-'+this.uid+'"></h3></a>'+
		'</div>'+
		'<div id="milestone-description-'+this.uid+'"></div>'+
		'</div>'+
		'</div>');
		var name = $('#milestone-name-'+this.uid);
		this.getName(function(nameStr){
			name.html(nameStr);
		});
		var description = $('#milestone-description-'+this.uid);
		this.getDescription(function(descriptionStr){
			description.html(descriptionStr);
		});
	}, 
	getTasks:function() {
		return new Table(function(fb){ return new Task(fb);},this.__firebase.child('tasks'));
	
	}
};

function Task(firebase){
	this.__firebase = firebase;
	this.uid = firebase.name();
	this.__name = firebase.child('name');
	this.__description = firebase.child('description');
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
			callback(dat.val());
		});
	},
	getCategory:function(callback){
		this.__category.on('value',function(dat){
			callback(dat.val());
		});
	},
	
	getOriginalTime:function(callback){
		this.__original_time_estimate.on('value',function(dat){
			callback(dat.val());
		});
	},
	getUpdatedTime:function(callback){
		this.__updated_time_estimate.on('value',function(dat){
			callback(dat.val());
		});
	},
	
	getTableHtml:function(callback){
		callback('<tr>'+
			'<td id="#task-name-'+this.uid+'"></td>'+
			'<td id="#task-description-'+this.uid+'"></td>'+
			'<td id="#task-category-'+this.uid+'"></td>'+
			'<td id="#task-assignedTo-'+this.uid+'"></td>'+
			'<td id="#task-original_time_estimate-'+this.uid+'"></td>'+
			'<td id="#task-updated_time_estimate-'+this.uid+'"></td>'+
			'</tr>');
		
		var name = $('#task-name-'+this.uid);
		this.getName(function(nameStr){
			name.html(nameStr);
		});
		var description = $('#task-description-'+this.uid);
		this.getDescription(function(descriptionStr){
			description.html(descriptionStr);
		});
		var assignedUser = $('#task-assignedTo-'+this.uid);
		this.getAssignedUser(function(assignedUserStr){
			assignedUser.html(assignedUserStr);
		});
		var category= $('#task-category-'+this.uid);
		this.getCategory(function(categoryStr){
			category.html(categoryStr);
		});
		var originalTime = $('#task-original_time_estimate-'+this.uid);
		this.getOriginalTime(function(original_time_estimateStr){
			originalTime.html(original_time_estimateStr);
		});
		var updatedTime= $('#task-updated_time_estimate-'+this.uid);
		this.getUpdatedTime(function(updated_time_estimateStr){
			updatedTime.html(updated_time_estimateStr);
		});		
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
			console.log("Error authenticating user:", error);
			$("#loginerror").show();
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
				console.log("Error creating user:" + user + ", " + error);
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
					document.getElementById("currentUser").innerHTML = "Currently logged in as " + userData.password.email;
					firebase.child('users/'+userData.uid).child("email").set(userData.password.email);
					$("#currentUser").show();
					$("#logoutButton").show();
					$("#loginButton").hide();
				}
			}
		);
	}
);

var projects = new Table(function(fb){ return new Project(fb);},firebase.child('projects'));
