/**
 * User Listener script for CSSE371/CSSE374/CSSE375 Henry project.
 * Handles read/write operations directly relating to Users.
 *
 * Authors: Sean Carter, Abby Mann, Andrew Davidson, Matt Rocco, Jonathan Jenkins, Adam Michael
 * Last Modified: 6 Feb 2015, 1:16pm
 */

var Firebase = require('firebase');

var production = 'https://henry-production.firebaseio.com';
var staging = 'https://henry-staging.firebaseio.com';
var test = 'https://henry-test.firebaseio.com';
var metricsTest = 'https://henry-metrics-test.firebaseio.com';
var qa = 'https://henry-qa.firebaseio.com';

var firebaseUrl = metricsTest;

// optional commandline arg: name of the DB to connect to. Defaults to test or metrics-test otherwise.
if (process.argv.length === 3) {
    firebaseUrl = 'https://' + process.argv[2] + '.firebaseio.com';
    console.log("Connecting to " + firebaseUrl);
}

var fbRef = new Firebase(firebaseUrl);
var commitsRef = new Firebase(firebaseUrl + '/commits');
var usersRef = new Firebase(firebaseUrl + '/users');
var projectsRef = new Firebase(firebaseUrl + '/projects');

var FirebaseTokenGenerator = require('firebase-token-generator');
var metricsTestToken = 'swPpsOgpNn7isrfxeoWNNV7yxLy85j94JO7p9lDf';
var testToken = 'FDrYDBNvRCgq0kGonjmsPl0gUwXvxcqUdgaCQ1FI';

var tokenVal = null;

// TODO: add more
if (firebaseUrl === test) {
    tokenVal = testToken;
}
else if (firebaseUrl === metricsTest) {
    tokenVal = metricsTestToken;
}

if (tokenVal !== null) {
    var tokenGenerator = new FirebaseTokenGenerator(tokenVal);
    var token = tokenGenerator.createToken({
        uid: 'nodeServer'
    });

    fbRef.authWithCustomToken(token, function(error, authData) {
        if (error) {
            console.log('Login Failed!', error);
        }
        else {
            console.log('Login Succeeded!', authData);
        }
    });
}
var addedLines = 'added_lines_of_code';
var removedLines = 'removed_lines_of_code';
var totalLines = 'total_lines_of_code';
var totalHours = 'total_hours';
var updatedHourEst = 'updated_hour_estimate';


/**
 * -------------------------USER LISTENER STUFF-------------------------
 */

usersRef.on('child_added', function(user) {
    user.ref().on('child_added', function(projects) {
        if (projects.key() == 'projects') {
            projects.ref().on('child_added', function(userProject) {
                userProject.ref().on('child_added', function(milestones) {
                    if (milestones.key() == 'milestones') {
                        milestones.ref().on('child_added', function(milestone) {
                            milestone.ref().on('child_added', function(tasks) {
                                if (tasks.key() == 'tasks') {
                                    tasks.ref().on('child_added', function(task) {
                                        task.ref().on('value', function(taskVal) {
                                            aggregateUserProjectData(userProject);
                                            var project = projectsRef.child(userProject.key());
                                            // console.log(userProject.key());
                                            updateLocAndHoursContribs(project);
                                        });
                                    });
                                }
                            });
                        });
                    }
                });
            });
        }
    });
});



function aggregateUserProjectData(project) {
    console.log('aggregating user project data');

    var totalProjectHours = 0;
    var totalProjectAddedLOC = 0;
    var totalProjectRemovedLOC = 0;
    var totalProjectLOC = 0;
    var totalProjectPoints = 0;

    project.child('milestones').forEach(function(milestone) {
        var totalMilestoneHours = 0;
        var totalAddedMilestoneLOC = 0;
        var totalRemovedMilestoneLOC = 0;
        var totalMilestoneLOC = 0;
        var totalMilestonePoints = 0;

        // console.log('Messing with tasks for milestone ' + milestone.key() + ' and project ' + project.key() + ' at time ' + new Date().toLocaleTimeString());
        milestone.ref().once('value', function(milestoneSnapshot) {
            milestoneSnapshot.child('tasks').forEach(function(task) {
                totalMilestoneHours += task.child(totalHours).val();
                totalAddedMilestoneLOC += task.child(addedLines).val();
                totalRemovedMilestoneLOC += task.child(removedLines).val();
                totalMilestoneLOC += task.child(totalLines).val();
                if (task.child('points') !== null) {
                    totalMilestonePoints += task.child('points').val();
                    //console.log('adding points to milestone: ' + totalMilestonePoints);
                }
            });
        });

        milestone.ref().update({
            'total_hours': totalMilestoneHours,
            'added_lines_of_code': totalAddedMilestoneLOC,
            'removed_lines_of_code': totalRemovedMilestoneLOC,
            'total_lines_of_code': totalMilestoneLOC,
            'total_points': totalMilestonePoints
        });

        totalProjectHours += totalMilestoneHours;
        totalProjectAddedLOC += totalAddedMilestoneLOC;
        totalProjectRemovedLOC += totalRemovedMilestoneLOC;
        totalProjectLOC += totalMilestoneLOC;
        totalProjectPoints += totalMilestonePoints;

    });
    project.ref().update({
        'total_hours': totalProjectHours,
        'added_lines_of_code': totalProjectAddedLOC,
        'removed_lines_of_code': totalProjectRemovedLOC,
        'total_lines_of_code': totalProjectLOC,
        'total_points': totalProjectPoints
    });

    //TODO: add total_points for user
    // TRIGGERS AN ON() LISTENER
    var userRef = project.ref().parent().parent().ref();
    var userPoints = 0;
    userRef.once('value', function(user) {
        // if (user.hasChild('total_points')) {
        //     userPoints += user.child('total_points').val();
        //     // console.log(userPoints);
        // }
        user.child('projects').forEach(function(project) {
            if (project.hasChild('total_points')) {
                // console.log('adding user points');
                userPoints += project.child('total_points').val();
            }
        });
    });
    console.log('changing user');
    userRef.update({
        'total_points': userPoints
    });

}




function updateLocAndHoursContribs(projectRef) {
    projectRef.once('value', function(project) {
        // console.log('hi. i am calculating contributions of a user. have a good day.');
        // console.log(project.val());
        var projAddedLOC = project.child(addedLines).val();
        var projRemovedLOC = project.child(removedLines).val();
        var projTotalLOC = project.child(totalLines).val();
        var projHours = project.child(totalHours).val();

        project.child('milestones').forEach(function(milestone) {
            // console.log('hi. i am iterating over another milestone');
            var mileAddedLOC = milestone.child('added_lines_of_code').val();
            var mileRemovedLOC = milestone.child('removed_lines_of_code').val();
            var mileTotalLOC = milestone.child('total_lines_of_code').val();
            var mileHours = milestone.child('total_hours').val();

            project.child('members').forEach(function(memberSnapshot) {
                // the above forEach executes the correct number of times
                // console.log('i am calculating percentages for another member');
                var memberRef = usersRef.child(memberSnapshot.key());
                // console.log('in between the thing that calculates correctly and the thing that does not');
                memberRef.once('value', function(member) {
                    // the above once happens more than it should
                    // console.log('i just got a new snapshot of the member ' + member.key());
                    if (member.hasChild('projects/' + project.key() + '/milestones/' + milestone.key())) {
                        var userMilestone = member.child('projects/' + project.key() + '/milestones/' + milestone.key());
                        var mileContribAddedLOC = userMilestone.child(addedLines).val();
                        var mileContribRemovedLOC = userMilestone.child(removedLines).val();
                        var mileContribHours = userMilestone.child(totalHours).val();
                        var mileContribTotalLOC = userMilestone.child(totalLines).val();

                        // update milestone stuff 
                        var mileAddPercent = calculatePercentage(mileContribAddedLOC, mileAddedLOC);
                        var mileRemPercent = calculatePercentage(mileContribRemovedLOC, mileRemovedLOC);
                        var mileLOCPercent = calculatePercentage(mileContribTotalLOC, mileTotalLOC);
                        var mileHourPercent = calculatePercentage(mileContribHours, mileHours);

                        // console.log('calculate the percent!');
                        // console.log(milestone.key());
                        // console.log(member.key());
                        // console.log(mileAddedLOC);
                        // console.log(mileAddPercent);

                        userMilestone.ref().update({
                            'added_loc_percent': mileAddPercent,
                            'removed_loc_percent': mileRemPercent,
                            'loc_percent': mileLOCPercent,
                            'hours_percent': mileHourPercent
                        });
                    }
                });
            });
        });

        project.child('members').forEach(function(memberSnapshot) {
            var memberRef = usersRef.child(memberSnapshot.key());
            memberRef.once('value', function(member) {
                var projectID = project.key();
                var projContribAddedLOC = member.child('projects/' + projectID + '/added_lines_of_code').val();
                var projContribRemovedLOC = member.child('projects/' + projectID + '/removed_lines_of_code').val();
                var projContribTotalLOC = member.child('projects/' + projectID + '/total_lines_of_code').val();
                var projContribHours = member.child('projects/' + projectID + '/total_hours').val();

                // update project stuff
                var projAddedLOCPercent = calculatePercentage(projContribAddedLOC, projAddedLOC);
                var projRemovedLOCPercent = calculatePercentage(projContribRemovedLOC, projRemovedLOC);
                var projTotalLOCPercent = calculatePercentage(projContribTotalLOC, projTotalLOC);
                var projHoursPercent = calculatePercentage(projContribHours, projHours);

                // console.log("User = " + member.key());
                // console.log("Project Added: " + projAddedLOC + " " + projContribAddedLOC);
                // console.log("Project Rmved: " + projRemovedLOC + " " + projContribRemovedLOC);
                // console.log("Project total: " + projTotalLOC + " " + projContribTotalLOC);
                // console.log("Project hours: " + projHours + " " + projContribHours + "\n");

                member.child('projects/' + projectID).ref().update({
                    'hours_percent': projHoursPercent,
                    'removed_loc_percent': projRemovedLOCPercent,
                    'added_loc_percent': projAddedLOCPercent,
                    'loc_percent': projTotalLOCPercent

                });
            });
        });
    });
}



/* 
 * calculates percentage current/total
 */
function calculatePercentage(current, total) {
    var percentage = 0;
    if (total !== 0 && current !== 0) {
        percentage = Math.round(current / total * 100);
    }
    if (percentage !== Infinity && !isNaN(percentage)) {
        return percentage;
    }
    else {
        return 0;
    }
}