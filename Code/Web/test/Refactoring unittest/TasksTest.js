var firebase = new Firebase("https://henry-test.firebaseio.com");
var users = new Table(function (fb) {
	return new User(fb);
}, firebase.child('users'));


(function TestTaskConstructor(){
	
	//pick up a task here
	var taskfirebase = new Firebase("https://henry-test.firebaseio.com/projects/-JdEoWCJIL99xpmSBE1i/milestones/-JiJu_k62R83TjsgkvVs/tasks/-JiJudFTd3DrXY7gujD5");
	var task = new Task(taskfirebase);
	
	task.getName(function(val){
		if (val === "test")
			console.log("name test passed");
		else
			console.log("name test failed");
	});
	
	task.getDescription(function(val){
		if (val === "test")
			console.log("description test passed");
		else
			console.log("description test failed");
	});
	
	task.getAssignedUser(function(val){
		val.getName(function(name) {
			if (name === "LE Davey")
				console.log("assign user test passed");
			else
				console.log("assign user test failed");
		})
	});
	
	task.getCategory(function(val){
		if (val === "Enhancement")
			console.log("custom category test passed");
		else
			console.log("custom category test failed");
	});
	
	task.getStatus(function(val){
		if (val === "Closed")
			console.log("status test passed");
		else
			console.log("status test failed");
	});
	
	task.getDueDate(function(val){
		if (val === "No Due Date")
			console.log("due date test passed");
		else
			console.log("due date test failed");
	});
	
	task.getBountyPoints(function(val){
		if (val === 0){}
		else{
			if (val === 10)
				console.log("bounty points test passed");
			else
				console.log("bounty points test not finished or failed");
		}
	});
	
	task.getTotalLinesOfCode(function(val){
		if (val === 0)
			console.log("total lines of code test passed");
		else
			console.log("total lines of code test failed");
	});
	
	task.getTimeEstimate(function(val){
		if (val === 0)
			console.log("time estimate test passed");
		else
			console.log("time estimate test failed");
	});
	
}());