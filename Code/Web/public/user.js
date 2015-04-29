// function userLeaderboard(){
// 	console.log("userLeaderboard called");
// 	var pointsArray = [];
// 	var arr = [];
// 	// console.log("here");
// 	firebase.child("users").on('value', function(snapshot) {
// 		var users = snapshot.val();
// 		for(var id in users){
// 			var name = users[id].name;
// 			var points = users[id].total_points; 
// 			pointsArray.push(name);
// 			if(points != null){
// 			pointsArray.push(points);
// 			} else {
// 				pointsArray.push(0);
// 			}
// 			arr.push(pointsArray);
// 			pointsArray = [];
// 		} 
		
// 	});
// 	arr.sort(function(a,b) {return b[1] - a[1]});
// 	// console.log(arr);
// 	$('#' + 0).html('Current User');
// 	user.getName(function(name){
// 		//console.log(name);
// 		$('#name' + 0).html(name);
// 	});

// 	user.getPoints(function(points){
// 		$('#pointValue' + 0).html(points);
// 	});

// 	$('#pointValue' + 0).html(0);
// 	var i =1;
// 	for(var element in arr){
// 		// console.log(element);
// 		$('#' + i).html(i);
// 		$('#name' + i).html(arr[i-1][0]);
// 		$('#pointValue' + i).html(arr[i-1][1]);
// 		i++;
// 	}

// };

var users;
var arr;

function userLeaderboard(){

	var pointsArray = [];
	arr = [];

	// console.log("here");
	firebase.child("users").on('value', function(snapshot) {

		users = snapshot.val();

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

	createLeaderboard(25);

};

function createLeaderboard(n) {
	var num_users_to_display = n;
	// if(document.getElementById('10').checked) {
	// 	num_users_to_display = 10;
	// }else if(document.getElementById('25').checked) {
	// 	num_users_to_display = 25;
	// } else {
	// 	num_users_to_display = 50;
	// }

	// Find a <table> element with id="leaderboard-table":
	var table = document.getElementById("leaderboard-table");
	table.innerHTML = "";

	var header_row = table.insertRow(0);
	var place_label = header_row.insertCell(0);
	var name_label = header_row.insertCell(1);
	var points_label = header_row.insertCell(2);

	place_label.innerHTML = "Place";
	name_label.innerHTML = "Name";
	points_label.innerHTML = "Points";


	var current_user_row = table.insertRow(1);
	var current_user_place = current_user_row.insertCell(0);
	var current_user_name = current_user_row.insertCell(1);
	var current_user_points = current_user_row.insertCell(2);

	current_user_place.innerHTML = "Current User";
	user.getName(function(name){
		current_user_name.innerHTML = name;
	});

	user.getPoints(function(points){
		current_user_points.innerHTML = points;
	});

	for(var i = 2; i < (num_users_to_display + 2) && i < arr.length; i++) {
		// Create an empty <tr> element and add it to the 1st position of the table:
		var row = table.insertRow(i);

		// Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
		var place = row.insertCell(0);
		var name = row.insertCell(1);
		var points = row.insertCell(2);

		// Add some text to the new cells:
		place.innerHTML = i - 1;
		name.innerHTML = arr[i][0];
		points.innerHTML = arr[i][1];
	}
};

///////////////////////////////////////////////////////////////////


// Adds the currently member selected in the "Add member" modal to the project
function addNewMember() {
	console.log("user.addNewMember called");
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


// User.ProjectData = function (user, ref) {
// 	console.log("User.ProjectData called");
// 	this.user = user;
// 	this.__firebase = ref;
// 	this.uid = ref.key();
// 	this.__added_lines_of_code = ref.child('added_lines_of_code');
// 	this.__removed_lines_of_code = ref.child('removed_lines_of_code');
// 	this.__total_lines_of_code = ref.child('total_lines_of_code');
// 	this.__milestones = ref.child('milestones');
// };

// User.ProjectData.prototype = {
// 	getProject: function () {
// 		console.log("User.ProjectData.getProject called");
// 		return projects.get(this.uid);
// 	},
// 	getMilestoneData: function () {
// 		console.log("User.ProjectData.getMilestoneData called");
// 		return new Table(
// 			function (ref) {
// 				return new User.MilestoneData(this.user, ref)
// 			},
// 			this.__milestones
// 		);
// 	},
// 	getMilestones: function () {
// 		console.log("User.ProjectData.getMilestones called");
// 		return new ReferenceTable(
// 			this.getProject().getMilestones,
// 			this.__milestones
// 		);
// 	},
// 	getLinesOfCode: function (callback) {
// 		console.log("User.ProjectData.getLinesOfCode called");
// 		this.__total_lines_of_code.on('value', function (snap) {
// 			callback(snap.val());
// 		});
// 	}
// };

// User.MilestoneData = function (user, ref) {
// 	console.log("User.MilestoneData called");
// 	this.user = user;
// 	this.__firebase = ref;
// 	this.uid = ref.key();
// 	this.__added_lines_of_code = ref.child('added_lines_of_code');
// 	this.__removed_lines_of_code = ref.child('removed_lines_of_code');
// 	this.__total_lines_of_code = ref.child('total_lines_of_code');
// 	this.__tasks = ref.child('tasks');
// };

// User.MilestoneData.prototype = {
// 	getMilestone: function () {

// 	},
// 	getLinesOfCode: function (callback) {
// 		console.log("User.MilestoneData.getLinesOfCode called");
// 		this.__total_lines_of_code.on('value', function (snap) {
// 			callback(snap.val());
// 		});
// 	}
// };
