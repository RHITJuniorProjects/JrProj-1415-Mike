

function userLeaderboard(){
	var users = null;
	var arr = null;

	console.log("4");
	console.log("userLeaderboard called");
	var pointsArray = [];
	arr = [];

	// console.log("here");
	firebase.child("users").on('value', function(snapshot) {
		console.log("1");

		users = snapshot.val();
		console.log("2");

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
	console.log("3");

	arr.sort(function(a,b) {return b[1] - a[1]});
	// console.log(arr);

	// $('#' + 0).html('Current User');
	// user.getName(function(name){
	// 	//console.log(name);
	// 	$('#name' + 0).html(name);
	// });

	// user.getPoints(function(points){
	// 	$('#pointValue' + 0).html(points);
	// });

	// $('#pointValue' + 0).html(0);
	console.log("Test0");

	createLeaderboard(10);

	alert("Test1");

	// var i =1;
	// for(var element in arr){
	// 	// console.log(element);
	// 	$('#' + i).html(i);
	// 	$('#name' + i).html(arr[i-1][0]);
	// 	$('#pointValue' + i).html(arr[i-1][1]);
	// 	i++;
	// }

};

function createLeaderboard(num_users_to_display) {
	alert("Test2");
	// Find a <table> element with id="leaderboard-table":
	var table = document.getElementById("leaderboard-table");
	table.innerHTML = "";

	var current_user_row = table.insertRow(0);
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

	int i = 1;
	//for(int i = 1; i < (num_users_to_display + 1); i++) {
		// Create an empty <tr> element and add it to the 1st position of the table:
		var row = table.insertRow(i);

		// Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
		var place = row.insertCell(0);
		var name = row.insertCell(1);
		var points = row.insertCell(2);

		// Add some text to the new cells:
		place.innerHTML = i;
		name.innerHTML = arr[i][0];
		points.innerHTML = arr[i][1];
	//}
};