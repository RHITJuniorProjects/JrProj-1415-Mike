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

MilestoneDB.prototype.getMilestoneName = function() {
	var array = [];
	firebase.child("projects/"+selectedProject.uid+"/milestones").on('value', function(snapshot) {
        var milestone = snapshot.val();
        for(var id in snapshot.val()){
            array.push(milestone[id].name);
        }
    });	
    return array;
};
MilestoneDB.prototype.getMilestoneHours = function() {
	var array = [];
	firebase.child("projects/"+selectedProject.uid+"/milestones").on('value', function(snapshot) {
        var milestone = snapshot.val();
        for(var id in snapshot.val()){
            array.push(milestone[id].hours_percent);
        }
    });	
    return array;
};
MilestoneDB.prototype.getMembers = function() {
	var array = [];
	firebase.child("projects/" + selectedProject.uid +"/members").on('value', function (snapshot) {
	    for(var member in snapshot.val()){
	        array.push(member);
	    }
    });
    return array;
};
MilestoneDB.prototype.getPieChartData = function(userID) {
	var userNameLOCArray = [];
	var userLOC = [];
	var userName = [];
	var i =0;
	firebase.child("users").on('value', function(snapshot){
		var user = snapshot.val();
		for(var id in snapshot.val()){
			if(id === userID[i] ){
				userLOC.push(user[id].projects[selectedProject.uid].total_lines_of_code);
				userName.push(user[id].name);
				userNameLOCArray.push(new Array(userName[i], userLOC[i]));
				i++
			}
		}
	});
    return userNameLOCArray;
};
