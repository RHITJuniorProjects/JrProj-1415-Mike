window.onload = function(){
var fb = new Firebase('https://henry371.firebaseio.com');

document.getElementById("setValue").addEventListener('click',function(event){
var userName = document.getElementById("fireBaseUserName").value

fbUserName = fb.child("user/" + userName +"/teams");
var teams;
fbUserName.once("value", function(data) {
  var received = data.val() ? data : "";
  received.forEach( function(team){
  		alert(team.name() + " " + team.val());
  		teams = teams + " " + team.val();
  	}
  	alert(teams)	
  )})

}
);
}
//LISTEN FOR REALTIME CHANGES


 