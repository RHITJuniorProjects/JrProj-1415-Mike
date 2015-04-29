function ProjectDB() {
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
}

ProjectDB.prototype.pushNewProject = function(docName, docDescription, docDueDate,estHours,members) {
	firebase.child('projects').push({
		'name': docName,
		'description': docDescription,
		'due_date': docDueDate,
		'total_estimated_hours': estHours,
		'custom_categories': {},
		'members': members
	});
};

ProjectDB.prototype.pushNewMilestone = function(projectID, docName,docDescription,docDueDate,estHours) {
	firebase.child('projects/' + projectID).child('milestones').push(
		{ 'name': docName,
		  'description': docDescription,
		  'due_date': docDueDate,
		  'total_estimated_hours': estHours
		}
	);
};

ProjectDB.prototype.getChartNameArray = function() {
	projectArray = [];
	// console.log(element);
	firebase.child("projects").on('value', function(snapshot) {
       	var project = snapshot.val();
        for(var id in snapshot.val()){
	        projectArray.push(project[id].name);
	    }
    });
    // console.log(projectArray);
    return projectArray;
};
ProjectDB.prototype.getChartHoursArray = function() {
	projectArray = [];
	// console.log(element);
	firebase.child("projects").on('value', function(snapshot) {
       	var project = snapshot.val();
        for(var id in snapshot.val()){
	        projectArray.push(project[id].hours_percent);
	    }
    });
    // console.log(projectArray);
    return projectArray;
};
ProjectDB.prototype.getChartTaskArray = function() {
	projectArray = [];
	// console.log(element);
	firebase.child("projects").on('value', function(snapshot) {
       	var project = snapshot.val();
        for(var id in snapshot.val()){
	        projectArray.push(project[id].task_percent);
	    }
    });
    // console.log(projectArray);
    return projectArray;
};
