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
