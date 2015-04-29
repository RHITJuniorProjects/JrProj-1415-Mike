function login(user, pass) { // Authenticates with Firebase, giving a callback that will
	// cause the window to go to projects if it was successful,
	// else go back to login and show the invalid input message.
	//console.log(user);
	firebase.authWithPassword({
		email: user,
		password: pass
	}, function (error, authData) {
		if (error === null) {
			var userData = authData;
			console.log("login");
			$("#myLoginModal").foundation('reveal', 'close');
			window.location.replace('/projects');
		} else {
			$("#loginError").show();
		}
	});
};

function getLoginData() { // Takes the login data from the form and places it into variables
	var user = $("#loginUser").val();
	var pass = $("#loginPass").val();
	$("loginPass").val("");
	login(user, pass);
}

function register() { 		// Registers a new user with Firebase, and also adds that user
	// to our local database (making a new developer/manager)
	var email = $("#registerUser").val();
	var pass = $("#registerPass").val();
	var passCheck = $("#registerPassCheck").val();
	var name = $("#name").val();

	if (pass !== passCheck) {
		$("#passwordError").show();
		$("#registerError").hide();
		$("#emailError").hide();
		return;
	}
	// Validate all fields are filled in
	if (!email || !pass || !passCheck || !name) {
		$("#passwordError").hide();
		$("#registerError").show();
		$("#emailError").hide();
		return;
	}
	firebase.createUser(
		{
			email: email,
			password: pass
		},
		function (error, userData) {
			if (error === null) { // if an error is throw

				login(email, pass, true);
				firebase.authWithPassword({
					email: user,
					password: pass
				}, function (error, authData) {
					if (error === null) {
					var userData = authData;
					firebase.child('users/' + userData.uid).update(
					{
						email: userData.password.email,
						name: $("#name").val()
					}, function (error) {
						if (error) {
							$("#loginError").show();
						} else {
							$("#loginError").hide();
							$("#myLoginModal").foundation('reveal', 'close');
							window.location.replace('/projects');
						}
					}
				);
					
				} else {
					$("#loginError").show();
				}
			});
			} else {
				$("#registerError").hide();
				$("#emailError").show();
			}
		}
	);
}

function showLoginModal() {
	$("#myLoginModal").foundation('reveal', 'open');
}

function logout() { // get rid all user data
	firebase.unauth();
	window.location.replace("/");
}