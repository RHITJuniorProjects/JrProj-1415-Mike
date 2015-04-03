function MilestoneDB() {
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
}

MilestoneDB.prototype.pushNewTask = function(nameInput,descriptionInput,userSelect,categoryName,statusSelect,estHours,dueInput) {
	selectedMilestone.__tasks.push({
		name: nameInput,
		description: descriptionInput,
		assignedTo: userSelect,
		category: categoryName,
		status: statusSelect,
		original_hour_estimate: estHours,
		due_date: dueInput 
	});
};
MilestoneDB.prototype.getMilestoneNames = function() {

	
};
