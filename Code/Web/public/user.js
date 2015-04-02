function userLeaderboard(){
	var pointsArray = [];
	var arr = [];
	// console.log("here");
	firebase.child("users").on('value', function(snapshot) {
		var users = snapshot.val();
		for(var id in users){
			var name = users[id].name;
			var points = users[id].total_points; 
			pointsArray.push(name);
			if(points != null){
			pointsArray.push(points);
			} else {
				pointsArray.push(0);
			}
			arr.push(pointsArray);
			pointsArray = [];
		} 
		
	});
	arr.sort(function(a,b) {return b[1] - a[1]});
	// console.log(arr);
	$('#' + 0).html('Current User');
	user.getName(function(name){
		//console.log(name);
		$('#name' + 0).html(name);
	});

	user.getPoints(function(points){
		$('#pointValue' + 0).html(points);
	});

	$('#pointValue' + 0).html(0);
	var i =1;
	for(var element in arr){
		// console.log(element);
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
	console.log("User.ProjectData called");
	this.user = user;
	this.__firebase = ref;
	this.uid = ref.key();
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
	console.log("User.MilestoneData called");
	this.user = user;
	this.__firebase = ref;
	this.uid = ref.key();
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
