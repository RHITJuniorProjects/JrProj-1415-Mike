/**
 * makes a new commit to test the metrics script.
 */
var Firebase = require("firebase");
var commitsRef = new Firebase("https://henry-test.firebaseio.com/commits");

var newCommit = commitsRef.push();
var randomTime = Math.floor(Math.random() * 500);
console.log(randomTime);
newCommit.set({
	'user' : '-JYcV535NCVDLJ-WN1QJ',
	'project' : '-JYcg488tAYS5rJJT4Kh',
	'time_spent' : randomTime
}); // in minutes

