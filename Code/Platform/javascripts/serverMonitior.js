
/**
 * This is a script to monitior to see if the server is still working. 
 * Note a HUGE chunk of this is copied from Sean's CommitMaker.js
 */

var Firebase = require("firebase");

var testURL = "https://henry-test.firebaseio.com"

var testToken = 'FDrYDBNvRCgq0kGonjmsPl0gUwXvxcqUdgaCQ1FI';
var nodeMailer = require('nodemailer');

var FirebaseTokenGenerator = require('firebase-token-generator');
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
/*
Could test this really throughly, but honestly if the server isn't working, than any one of these will break... Especaily the later ones.
var projectRef = fbRef.child("projects/-JYcg488tAYS5rJJT4Kh");
var milestoneRef = fbRef.child("projects/-JYcg488tAYS5rJJT4Kh/milestones/-JYc_9ZGEPFM8cjChyKl");
var taskRef = fbRef.child("projects/-JYcg488tAYS5rJJT4Kh/milestones/-JYc_9ZGEPFM8cjChyKl/tasks/-JcG89BGv9maWTx214yi");
	var prevTaskTime = taskRef.val("total_hours");
	var prevMilestoneTime = milestoneRef.val("total_hours");
	var prevProjectTime = projectRef.val("total_hours");
	var prevTaskAdded = taskRef.val("added_lines_of_code");
	var prevMilestoneAdded = milestoneRef.val("added_lines_of_code");
	var prevProjectAdded = projectRef.val("added_lines_of_code");
	var prevTaskRemoved = taskRef.val("removed_lines_of_code");
	var prevMilestoneRemoved = milestoneRef.val("removed_lines_of_code");
	var prevProjectRemoved = projectRef.val("removed_lines_of_code");*/
	
	setInterval(function(){ //Will run every 2.5 minutes
    firstTask()
    setTimeout(checker(28), 5000);
	},250000);

	
	
	function firstTask(){
    var projectRef = fbRef.child("users/simplelogin:110/projects/-JYcg488tAYS5rJJT4Kh/total_hours");
    projectRef.once('value', function(project) {
    	//var prevTime = project.val();
	    var lines = 200;
var lines2 = 100;
var time = 4;
var timestamp = Firebase.ServerValue.TIMESTAMP;
var commitsRef = fbRef.child("commits/-JYcg488tAYS5rJJT4Kh");

        //try{
        var commit = commitsRef.push({
		'added_lines_of_code': lines,
		'hours': time, // in hours
		'message': 'test commit',
		'milestone': "-JYc_9ZGEPFM8cjChyKl",
		'project': '-JYcg488tAYS5rJJT4Kh',
		'removed_lines_of_code': lines2,
		'status': 'Testing',
		'task': "-JcG89BGv9maWTx214yi",
		'timestamp': timestamp,
		'user': 'simplelogin:110'//,
		//'updated_hour_estimate':60
	});
	        console.log("Commit: "+ commit.key());
        //}catch(Exception){
            //Failed to write
            //console.log("Something is wrong with rules/data");
           // console.log(Exception.message);
        //}
    });
	}
	
	
	function checker(prevTime){
	    var projectRef2 = fbRef.child("users/simplelogin:110/projects/-JYcg488tAYS5rJJT4Kh/total_hours");
	    projectRef2.once('value', function(project2) {
        var newTime = project2.val();
        //Do checking stuff here. Check to make sure that new = prev + constant
        if(newTime!=prevTime+4){
            //EMAIL THERE IS AN ERROR
            
        var transporter = nodeMailer.createTransport({
			  service: 'Gmail',
    	auth: {
        user: 'platformhenry@gmail.com',
        pass: 'Platform123'
    	}
});
var mailOptions = {
    from: 'platformhenry@gmail.com',
    to: 'jonathan.kl.jenkins@gmail.com', // list of receivers
    subject: 'The server is malfunctioning. Please investigate.', // Subject line
    text: 'Fix it', // plaintext body
    html: 'Now.' // html body
};
// send mail with defined transport object
		transporter.sendMail(mailOptions, function(error, info){
    	if(error){
        	console.log(error);
    	}else{
        	console.log('Message sent: ' + info.response);
    	}
	});
            console.log("Something is wrong with time");
    	    console.log(newTime);
    	    console.log(prevTime+4);
        }else{
            console.log("The commit has sucessfully updated.");
            console.log(newTime);
            console.log(prevTime+4);
        }
	    });
	}
