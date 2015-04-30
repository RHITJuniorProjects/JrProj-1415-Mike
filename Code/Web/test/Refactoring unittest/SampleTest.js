var firebase = new Firebase("https://henry-test.firebaseio.com");

(function Trophies(){
	
	var userRef = firebase.child('users/simplelogin:25/name');
	userRef.once('value', function(nameSnapshot) {
		var name = nameSnapshot.val();
		console.log("LOL");
		console.log(name);
	});
	
}());