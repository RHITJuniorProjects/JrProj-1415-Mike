var firebase = new Firebase("https://henry-test.firebaseio.com");
var users = new Table(function (fb) {
	return new User(fb);}
	, firebase.child('users'));

(function testMilestone() {
	var milestoneFirebase = new Firebase("https://henry-test.firebaseio.com/projects/-JdEoWCJIL99xpmSBE1i/milestones/-JdEp9QPkTZNeByz1rPi");
	var milestone = new Milestone(milestoneFirebase);
	console.log("Testing");
	
	milestone.getName(function(val){
		if (val === "test7")
			console.log("Name test passes.");
		else
			console.log("Name test fails.");	
	});
	
	milestone.getDueDate(function(val){
		if (val === "No Due Date")
			console.log("Due Date test passes.");
		else
			console.log("Due Date test fails.");
	});
	
	milestone.getDescription(function(val){
		if (val === "test")
			console.log("Description test passes.");
		else
			console.log("Description test fails.");
	});
	
	milestone.getTasks(function(val){
		var table = new Table(function(fb){
			return new Milestone(fb);
		}, milestoneFirebase.child('tasks'));
		
		if (val === table)
			console.log("Tasks test passes.");
		else
			console.log("Tasks test fails.");
	});
	
});