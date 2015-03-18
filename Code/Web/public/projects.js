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

