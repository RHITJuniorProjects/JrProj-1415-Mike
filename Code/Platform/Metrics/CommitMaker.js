/**
 * makes a new commit to test the metrics script.
 */

var Firebase = require("firebase");

var testURL = "https://henry-test.firebaseio.com"
var metricsTestURL = "https://henry-metrics-test.firebaseio.com"

var metricsTestToken = 'swPpsOgpNn7isrfxeoWNNV7yxLy85j94JO7p9lDf';
var testToken = 'FDrYDBNvRCgq0kGonjmsPl0gUwXvxcqUdgaCQ1FI';
// TODO: set this to the appropriate database, potentially with commandline override
// commandline version could also have a flag for which DB to use?

var FirebaseTokenGenerator = require('firebase-token-generator');
//this token is for metricsTest
var tokenGenerator = new FirebaseTokenGenerator(testToken);
var token = tokenGenerator.createToken({
	uid: 'nodeServer'
});

var fbRef = new Firebase(testURL);

fbRef.authWithCustomToken(token, function(error, authData) {
	if (error) {
		console.log('Login Failed!', error);
	}
	else {
		console.log('Login Succeeded!', authData);
	}
});

var commitsRef = fbRef.child("commits/-JYcg488tAYS5rJJT4Kh");

var milestone1 = '-JYc_9ZGEPFM8cjChyKl';
var milestone2 = '-JcR3LrJx3NUBrxnP_NG';

var m1Tasks = ['-JYc_fRjZ_IV3WgHIszD', '-JcG6RhiqRVPZhzn-X0R', '-JcG89BGv9maWTx214yi'];
var m2Tasks = ['-JcR4Z947gmL7oauyfXt', '-JcR5kxeVaw70fmYJyjl', '-JcR5l0QVjV0aM6iMyMB', '-JcR5lFXTTTRQsJ3VyF_',
	'-JcaLDPk41hc67a1_P7z', '-JcacU9nfEjWZQPtQeO4', '-Jd9WK7J52gae1BIoAF0'
];

var numM1Tasks = m1Tasks.length;
var numM2Tasks = m2Tasks.length;

// for (var i = 0; i < 10; i++) {
	// var milestone = (Math.random() < 0.5) ? milestone1 : milestone2;
	var milestone = milestone1;
	var tasks = (milestone === milestone1) ? m1Tasks : m2Tasks;
	var numTasks = (tasks === m1Tasks) ? numM1Tasks : numM2Tasks;

	var taskNum = Math.floor(Math.random() * numTasks);

	var task = tasks[taskNum];

	var randomTime = Math.floor(Math.random() * 23) + 1;
	var randomLines = Math.floor(Math.random() * 300);
	var randomLines2 = Math.floor(Math.random() * randomLines)
	var timestamp = Firebase.ServerValue.TIMESTAMP;

	console.log("hours: " + randomTime);
	console.log("lines: " + randomLines);
	console.log("Milestone: ", milestone);
	console.log("Task: ", task);
	// this one goes under commits
	commitsRef.push({
		'added_lines_of_code': randomLines,
		'hours': randomTime, // in hours
		'message': 'test commit',
		'milestone': milestone,
		'project': '-JYcg488tAYS5rJJT4Kh',
		'removed_lines_of_code': randomLines2,
		'status': 'Testing',
		'task': task,
		'timestamp': timestamp,
		'user': 'simplelogin:110'
	});
// }