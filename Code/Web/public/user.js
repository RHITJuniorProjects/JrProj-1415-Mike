function userLeaderboard(){
	console.log("userLeaderboard called");
	var pointsArray = [];
	var arr = [];

	firebase.child("users").on('value', function(snapshot) {
		var users = snapshot.val();
		for(var id in users){
			var name = users[id].name;
			var points = users[id].total_points;
			userTrophies = [];

			firebase.child("users/" + id + "/trophies").orderByChild("cost").on('child_added', function (snap) {
				var val = new Trophy(snap.ref());
				userTrophies[userTrophies.length] = val;
			});

			var trophy = userTrophies[userTrophies.length-1];
			var image = "";
			firebase.child("trophies/" + trophy.uid + "/image").on('value', function(snap) {
				image = snap.val();
			});
			pointsArray.push(name);
			if(points != null){
				pointsArray.push(points);
			}
			else {
				pointsArray.push(0);
			}
			if(image != null) {
				pointsArray.push(image);
			}
			else {
				pointsArray.push("");
			}
			arr.push(pointsArray);
			pointsArray = [];
			 
		}
		
	});

	arr.sort(function(a,b) {return b[1] - a[1]});
	$('#' + 0).html('Current User');
	user.getName(function(name){
		$('#name' + 0).html(name);
	});

	user.getPoints(function(points){
		$('#pointValue' + 0).html(points);
	});

	user.getTopTrophy(function (trophy) {
		var tempTrophy = userTrophies[userTrophies.length-1];
		if(tempTrophy != null) {
			var trophyUID = tempTrophy.uid;
			firebase.child("trophies/" + trophyUID + "/image").on('value', function(snap) {
				image = snap.val();
				if(image != null) {
					$('#topTrophy' + 0).html("<img src=" + image + " height=42 width=42>");
				}
			});
		}
	});
	
	var i = 0;
	$('#boardCurrentUser').nextAll().remove();	//clear board to repopulate
	for(var element in arr) {
		i++;
		if(i > $('input[name=boardLimit]:checked', '#limitForm').val()) break;

		var row = $('<tr>').attr('id', 'place' + i);
		var rank = $('<td>').attr('id', i).html(i);
		var name = $('<td>').attr('id', 'name' + i).html(arr[i - 1][0]);
		var pts = $('<td>').attr('id', 'pointValue' + i).html(arr[i - 1][1]);
		var trophy = $('<td>').attr('id', 'topTrophy' + i);

		// rank.html(i);
		// name.html(arr[i - 1][0]);
		// pts.html(arr[i - 1][1]);
		if(arr[i - 1][2] != "") {
			trophy.html("<img src=" + arr[i - 1][2] + " height=42 width=42>");
		}

		row.append(rank).append(name).append(pts).append(trophy);
		$('#leaderTable').append(row);
	}

};

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
