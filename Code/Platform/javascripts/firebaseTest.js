window.onload = function(){
var fb = new Firebase('https://henry371.firebaseio.com');

fb.on("value", function(data){
	var received = data.val() ? data : "";
	var textToDisplay = ""; 
	var users = received.child('user')
	var sprintName = ""
	var milestoneName =""
	var taskName = ""

	users.forEach(function (user){
		textToDisplay = textToDisplay + user.name() + " is on the the following teams: <br/>"
		var teams = user.child('teams')
		teams.forEach(function (projects){
			textToDisplay = textToDisplay + "&nbsp&nbsp&nbspProject: " + projects.name() + "<br>&nbsp&nbsp&nbspTeam: " + projects.val() + "<br><br>"


			// sprints = received.child('projects/' + project.name() + "/" + project.val())
			// sprints.forEach(function (sprint){
			// 	if(sprint.name() != "members"){
			// 		sprintName = sprint.name()
			// 		milestones = received.child()
			// 	}

			})
		})
	document.getElementById("projectField").innerHTML += document.getElementById("projectField").innerHTML + textToDisplay;
	})
}


// fbUsers = fb.child("user/")
// document.getElementById("setValue").addEventListener('click',function(event){
// var userName = document.getElementById("fireBaseUserName").value

// fbUserName = fb.child("user/" + userName +"/teams");
// var teams;
// fbUserName.once("value", function(data) {
//   var received = data.val() ? data : "";
//   received.forEach( function(team){
//   		alert(team.name() + " " + team.val());
//   		teams = teams + " " + team.val();
//   	}
//   	alert(teams)	
//   )})

// }
// );
// }
//LISTEN FOR REALTIME CHANGES


 