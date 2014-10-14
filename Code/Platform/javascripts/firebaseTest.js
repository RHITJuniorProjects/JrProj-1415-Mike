window.onload = function() {
    var fb = new Firebase('https://henry-test.firebaseio.com');

    document.getElementById("submit").addEventListener("click", function(event) {

            var userEmail = document.getElementById("email").value
            var pass = document.getElementById("password").value
        var fName = document.getElementById("firstName").value
       	 var githubName = document.getElementById("gitHub").value
            fb.createUser({
                    email  : userEmail,
                    password : pass
                }, function(error) {
                    if (error == null) {
                        console.log("user created");
             fb.authWithPassword({
                "email": userEmail,
                "password": pass
            }, function(error, authData) {
                if (error == null) {
                	 console.log("Success");
                    document.getElementById("projectField").innerHTML = authData.uid;
                    fb.child('users').child(authData.uid).set({email : userEmail, firstName : fName, github : githubName, projects : false, tasks : false});

                } else {
                    console.log("login Failed", error);
                   

                }
            }
        )
                    } else {
                        console.log("error", error);
                       document.getElementById("projectField").innerHTML = error.code;

                    }
                });

    })

document.getElementById("login").addEventListener("click", function(event) {

        var userEmail = document.getElementById("emailLogin").value
        var pass = document.getElementById("passwordLogin").value

            fb.authWithPassword({
                "email": userEmail,
                "password": pass
            }, function(error, authData) {
                if (error == null) {
                	 console.log("Success");
                    var uid = authData.uid;
                    fb.child('users').child(authData.uid).once("value", function(data){
   					document.getElementById("projectField").innerHTML = data.email + " " + data.firstName + " " + data.github;
	
                    })

                } else {
                    console.log("login Failed", error);
                   

                }
            }
        )
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
