


(function TestProjectGets(){
	
	//pick up a trophy here
	var projectfirebase = new Firebase("https://henry-test.firebaseio.com/projects/-JiJOfP0zkiqoCLQeSeQ");
	var project = new Project(trophyfirebase);
	
	project.getName(function(val){
		if (val === "My New Project")
			console.log("name test passed");
		else
			console.log("name test failed");
	});
	
	project.getDescription(function(val){
		if (val === "A new project")
			console.log("description test passed");
		else
			console.log("description test failed");
	});
	
	project.getDueDate(function(val){
		if (val === "2015-05-05_)
			console.log("due date test passed");
		else
			console.log("due date test failed");
	});
	
}());