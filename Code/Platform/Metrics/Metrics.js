/**
 * Metric collection script for CSSE371 Henry project
 */

var Firebase = require("firebase");
var commitsRef = new Firebase("https://henry-test.firebaseio.com/commits");
var metrics = new Firebase("https://henry-test.firebaseio.com/metrics");

var projects = null;
var commits = null;

metrics.on('value', function(metric) {
	projects = metric;
});

commitsRef.on('value', function(commitsData) {
	commits = commitsData;
});

commitsRef.on('child_added', function(commit) {
	if (projects !== null && commits !== null) {
		
		var project = projects.child(commit.child("project").val());

		var averageTime = project.child("average_time").val();
		var numCommits = project.child("number_of_commits").val();
		var commitTime = commit.child("time_spent").val();

		averageTime = (averageTime * (numCommits - 1) + commitTime) / numCommits;

		numCommits++;

		project.ref().update({
			average_time : averageTime,
			number_of_commits : numCommits
		});
	}
});
/*
commitsRef.on('child_removed', function(commit) {
	if (projects !== null && commits !== null) {
		var project = projects.child(commit.child("project").val());
		
		var numCommits = project.child("number_of_commits").val() - 1;
		
		project.ref().update({
			number_of_commits : numCommits
		});
	}
});
*/
