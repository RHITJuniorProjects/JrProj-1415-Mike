var firebase = new Firebase("https://henry-test.firebaseio.com");
var users = new Table(function (fb) {
	return new User(fb);}
	, firebase.child('users'));

(function testProject () {
	var projectFirebase = new Firebase("https://henry-test.firebaseio.com/projects/-JdEoWCJIL99xpmSBE1i");
	var project = new Project(projectFirebase);
	console.log("Testing");
	
	project.getName(function(val){
		if (val === "Android Test")
			console.log("Name test passes.");
		else
			console.log("Name test fails.");
	});
	
	project.getDescription(function(val){
		if (val === "Testing")
			console.log("Description test passes.");
		else
			console.log("Description test fails.");
	});
	
	project.getTotalPoints(function(val){
		if (val === null)
			console.log("Total Points test passes.")
		else
			console.log("Total Points test fails.")
	});
	
	project.getDueDate(function(val){
		if (val === "2014-12-15")
			console.log("Due Date test passes.");
		else
			console.log("Due Date test fails.");
	});
	
	project.getMilestones(function(val){
		var table = new Table(function(fb) {
				return new Milestone(fb);
		}, projectFirebase.child('milestones'));
		
		if (val === table)
			console.log("Milestones test passes.");
		else
			console.log("Milestones test fails.");
	});
})

