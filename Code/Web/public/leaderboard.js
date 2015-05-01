function userLeaderboard(){
	console.log("userLeaderboard called");
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
	var i = 1;
	for(var element in arr){
		// console.log(element);
		$('#' + i).html(i);
		$('#name' + i).html(arr[i - 1][0]);
		$('#pointValue' + i).html(arr[i - 1][1]);
		i++;
		if(i > $('input[name=boardLimit]:checked', '#limitForm').val()) {
			console.log("break at: " + i);
			break;
		}
		else console.log("keep going: " + i);
	}

};