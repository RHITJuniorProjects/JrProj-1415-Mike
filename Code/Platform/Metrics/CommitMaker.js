/**
 * makes a new commit to test the metrics script.
 */
var Firebase = require("firebase");
var commitsRef = new Firebase("https://henry-test.firebaseio.com/commits");

var newCommit = commitsRef.push();
var randomTime = Math.floor(Math.random() * 24);
var randomLines = Math.floor(Math.random() * 300);
console.log(randomTime);
newCommit.set({
	'hours' : randomTime, // in hours
	'lines_of_code' : randomLines, // get from git
	'timestamp' : Firebase.ServerValue.TIMESTAMP,
	'task' : 'bar', // update to a task in the base project
	'milestone' : 'foo', // same as above
	'user' : '-JYcV535NCVDLJ-WN1QJ',
	'project' : '-JYcg488tAYS5rJJT4Kh'
});