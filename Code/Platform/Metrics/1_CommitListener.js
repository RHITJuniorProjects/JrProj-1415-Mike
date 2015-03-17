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

var commitsRef = fbRef.child("commits");
var projectsRef = fbRef.child("projects");
var usersRef = fbRef.child('users');

// remove tasks that are broken
projectsRef.once('value', function(projects) {
    projects.forEach(function(project) {
        project.child('milestones').forEach(function(milestone) {
            // milestone.child('burndown_data').forEach(function(point) {
            //     var hasCommit = false;
            //     commitsRef.child(project.key()).once('value', function(commits) {
            //         commits.forEach(function(commit) {
            //             if (commit.child('timestamp').val() === point.key()) {
            //                 hasCommit = true;
            //             }
            //         });
            //     });
            //     console.log('iterating over burndown point');
            //     if (!hasCommit) {
            //         console.log('Removing bad data point');
            //         point.ref().remove();
            //     }
            // });
            milestone.child('tasks').forEach(function(task) {
                if (!task.hasChild('name')) {
                    task.ref().remove();
                }
                else if (task.hasChild('bounties')) {
                    if (task.child('bounties').hasChild('points')) {
                        task.child('bounties').ref().update({
                            'points': null
                        })
                    }
                }
            });
        });
    });
});

// remove commits with no task
commitsRef.once('value', function(projects) {
    projects.forEach(function(project) {
        project.forEach(function(commit) {
            if (commit.key() === 'description') return;
            // remove junk commits from Metrics Script
            console.log('iterating over commit @ project ' + project.key());
            if (commit.child('message').val() === 'Change to updated_hour_estimate from Metrics Script') {
                console.log('removing commit');
                commit.ref().remove();
                return;
            }
            // projectsRef.on('value', function(projects) {
            //     if (!projects.hasChild(commit.child('project').val() + '/milestones/' + commit.child('milestone').val() + '/tasks/' + commit.child('task').val())) {
            //         commit.ref().remove();
            //     }
            // });
        });
    });
});



// remove user tasks that no longer exist
usersRef.on('value', function(users) {
    users.forEach(function(user) {
        user.child('projects').forEach(function(project) {
            project.child('milestones').forEach(function(milestone) {
                milestone.child('tasks').forEach(function(task) {
                    projectsRef.on('value', function(projects) {
                        if (!projects.hasChild(project.key() + '/milestones/' + milestone.key() + '/tasks/' + task.key())) {
                            task.ref().remove();
                            console.log('removing task');
                        }
                    });
                });
            });
        });
    });
});