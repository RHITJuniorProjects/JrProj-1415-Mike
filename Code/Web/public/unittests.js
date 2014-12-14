function testLogin() {
    var user = "test6@test.com", pass = "test", id = "simplelogin:48";
    testLoginUser(user, pass);
    setTimeout(function(){
        if(userData != null && userData.uid === id  && userData.password.email === user &&  $("#currentUser").text() === "Currently logged in as " + user){
           console.log("Pass");
        } else {
            console.log("Fail");
        }
    }, 2000);
}

function testLoginUser(user, pass){
    $("#loginUser").val(user);
    $("#loginPass").val(pass);
    $("#loginSubmit").click();
}

function testLoginBad(){
    $("#myLoginModal").foundation('reveal', 'open');
    $("#loginUser").val("test3@test.com");
    $("#loginPass").val("test");
    $("#loginSubmit").click();
    setTimeout(function(){
        if(userData == null && $("#loginError").is(":visible")){
            console.log("Pass");
            $("#myLoginModal").foundation('reveal','close');
        } else {
            console.log("Fail");
        }
    }, 3000);
}

function testLogout() {
    $("#logoutButton")[0].click();
    setTimeout(function(){
        if(userData == null){
            console.log("Pass");
        } else {
            console.log("Fail");
        }
    }, 2000);
}

function testRegistration() {
    var user = "test8@test.com", pass = "test";
    $("#registerUser").val(user);
    $("#registerPass").val(pass);
    $("#registerPassCheck").val(pass);
    $("#githubuser").val("BobTheGhost");
    $("#name").val("Bob the magically testing fairy");
    $("#registerSubmit").click();
    setTimeout(function () {
        if(userData != null && userData.password.email === user &&  $("#currentUser").text() === "Currently logged in as " + user){
            console.log("Pass");
        } else {
            console.log("Fail");
        }
    }, 2000);
}

function testRegistrationBad() {
    $("#myRegisterModal").foundation('reveal', 'open');
    var user = "test8@test.com", pass = "test";
    $("#registerUser").val(user);
    $("#registerPass").val(pass);
    $("#registerPassCheck").val(pass);
    $("#githubuser").val("");
    $("#name").val("Bob the magically testing fairy");
    $("#registerSubmit").click();
    setTimeout(function () {
        if(userData == null && $("#registerError").is(":visible") && !$("#passwordError").is(":visible") && !$("#emailError").is(":visible")){
            console.log("Pass");
            $("#myRegisterModal").foundation('reveal','close');
        } else {
            console.log("Fail");
        }
    }, 3000);
}

function testRegistrationBad2() {
    $("#myRegisterModal").foundation('reveal', 'open');
    var user = "test8@test.com", pass = "test";
    $("#registerUser").val(user);
    $("#registerPass").val(pass);
    $("#registerPassCheck").val("tests");
    $("#githubuser").val("gitUserNameBlah");
    $("#name").val("Bob the magically testing fairy");
    $("#registerSubmit").click();
    setTimeout(function () {
        if(userData == null && !$("#registerError").is(":visible") && $("#passwordError").is(":visible") && !$("#emailError").is(":visible")){
            console.log("Pass");
            $("#myRegisterModal").foundation('reveal','close');
        } else {
            console.log("Fail");
        }
    }, 3000);
}

function testRegistrationBad3() {
    $("#myRegisterModal").foundation('reveal', 'open');
    var user = "test6@test.com", pass = "test";
    $("#registerUser").val(user);
    $("#registerPass").val(pass);
    $("#registerPassCheck").val(pass);
    $("#githubuser").val("gitUserNameBlah");
    $("#name").val("Bob the magically testing fairy");
    $("#registerSubmit").click();
    setTimeout(function () {
        if(userData == null && !$("#registerError").is(":visible") && !$("#passwordError").is(":visible") && $("#emailError").is(":visible")){
            console.log("Pass");
            $("#myRegisterModal").foundation('reveal','close');
        } else {
            console.log("Fail");
        }
    }, 3000);
}