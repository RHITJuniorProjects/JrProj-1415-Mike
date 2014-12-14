/**
 * makes a new commit to test the metrics script.
 */

var Firebase = require("firebase");

var realTestURL = "https://henry-test.firebaseio.com"
var metricsTestURL = "https://henry-metrics-test.firebaseio.com"

var commitsRef = new Firebase(realTestURL + "/commits/-JYcg488tAYS5rJJT4Kh");

var newCommit = commitsRef.push();

var randomTime = Math.floor(Math.random() * 23) + 1;
var randomLines = Math.floor(Math.random() * 300);
var randomLines2 = Math.floor(Math.random() * randomLines)
var timestamp =  Firebase.ServerValue.TIMESTAMP;

console.log("hours: " + randomTime);
console.log("lines: " + randomLines);

// this one goes under commits
newCommit.set({
	'hours' : randomTime, // in hours
	'added_lines_of_code' : randomLines,
	'removed_lines_of_code' : randomLines2,
	'timestamp' : timestamp,
	'task' : '-JYc_fRjZ_IV3WgHIszD',
	'milestone' : '-JYc_9ZGEPFM8cjChyKl',
	'user' : 'simplelogin:27',
	'message' : 'test commit',
	'status' : 'Testing'
});