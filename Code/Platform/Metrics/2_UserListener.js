/**
 * User Listener script for CSSE371/CSSE374/CSSE375 Henry project.
 * Handles read/write operations directly relating to Users.
 *
 * Authors: Sean Carter, Abby Mann, Andrew Davidson, Matt Rocco, Jonathan Jenkins, Adam Michael
 * Last Modified: 23 April 2015, 10:00am.
 */
var Firebase = require('firebase');

// various firebase DBs in use
var production = 'https://henry-production.firebaseio.com';
var staging = 'https://henry-staging.firebaseio.com';
var test = 'https://henry-test.firebaseio.com';
var metricsTest = 'https://henry-metrics-test.firebaseio.com';
var qa = 'https://henry-qa.firebaseio.com';

var firebaseUrl = metricsTest;

// optional commandline arg: name of the DB to connect to. Defaults to henry-metrics-test otherwise.
if (process.argv.length === 3) {
    firebaseUrl = 'https://' + process.argv[2] + '.firebaseio.com';
    console.log("Connecting to " + firebaseUrl);
}
else if (process.argv.length === 4) {
    // TODO: override auth token?
}

// Firebase references for root and top-level trees
var fbRef = new Firebase(firebaseUrl);
var commitsRef = new Firebase(firebaseUrl + '/commits');
var usersRef = new Firebase(firebaseUrl + '/users');
var projectsRef = new Firebase(firebaseUrl + '/projects');
var trophiesRef = new Firebase(firebaseUrl + '/trophies');

// authorization stuff
var FirebaseTokenGenerator = require('firebase-token-generator');

// TODO: add more as needed, or do commandline override for auth token
var config = require('./config.json');
var metricsTestToken = config.metricsTestToken;
var testToken = config.testToken;

var tokenVal = null;

if (firebaseUrl === test) {
    tokenVal = testToken;
}
else if (firebaseUrl === metricsTest) {
    tokenVal = metricsTestToken;
}

// authorize the script to read/write data to the firebase
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

// Various strings that are used in several places
// TODO: chack for adding more?
var addedLines = 'added_lines_of_code';
var removedLines = 'removed_lines_of_code';
var totalLines = 'total_lines_of_code';
var totalHours = 'total_hours';
var updatedHourEst = 'updated_hour_estimate';


/**
 * -------------------------USER LISTENER STUFF-------------------------
 */

usersRef.on('child_added', function(user) {
    console.log('New user ' + user.key() + ' added');
    // reset available points prior to aggregation
    user.ref().update({
        'available_points': user.child('total_points').val()
    });
    // Deal with new trophies
    if (!user.hasChild('trophies')) {
        user.ref().update({
            'trophies': {
                'placeholder': 'placeholder'
            }
        });
    }
    var userTrophies = user.child('trophies');
    if (userTrophies.hasChild('placeholder') && userTrophies.numChildren() >= 2) {
        userTrophies.ref().update({
            'placeholder': null
        });
    }
    // TODO: test the thing
    userTrophies.ref().on('child_added', function(trophy) {
        if (trophy.key() === 'placeholder') return;
        console.log('new trophy added. User: ' + user.key() + ', trophy: ' + trophy.key());
        calculateAvailablePoints(user.ref(), trophy.key());
    });

    user.ref().on('child_added', function(projects) {

        // are there projects? drill down into them
        if (projects.key() !== 'projects') return;
        projects.ref().on('child_added', function(userProject) {
            //console.log('New project ' + userProject.key() + ' added for user ' + user.key());
            userProject.ref().on('child_added', function(milestones) {

                // are there milestones? drill down into them
                if (milestones.key() !== 'milestones') return;
                milestones.ref().on('child_added', function(milestone) {
                    //console.log('New milestone ' + milestone.key() + ' added to project ' + userProject.key() + ' for user ' + user.key());
                    milestone.ref().on('child_added', function(tasks) {

                        // are there tasks? drill down into them
                        if (tasks.key() !== 'tasks') return;
                        tasks.ref().on('child_added', function(task) {
                            //console.log('New task ' + task.key() + ' added to milestone ' + milestone.key() + ' and project ' + userProject.key() + ' for user ' + user.key());
                            task.ref().on('value', function(taskVal) {
                                // Propagate up from the task level every time anything happens to the task
                                aggregateUserProjectData(userProject);
                                var project = projectsRef.child(userProject.key());
                                // calculate percentage contribution for loc and hours
                                updateLocAndHoursContribs(project);
                            });
                        });
                    });
                });
            });
        });
    });
});

/* 
 * Aggragates data upward from the task level to the top level for one user
 */
function aggregateUserProjectData(userProject) {
    // console.log('aggregating user project data for project ' + userProject.key());

    var totalProjectHours = 0;
    var totalProjectAddedLOC = 0;
    var totalProjectRemovedLOC = 0;
    var totalProjectLOC = 0;
    var totalProjectPoints = 0;

    var userRef = userProject.ref().parent().parent();
    userProject.child('milestones').forEach(function(userMilestone) {
        // aggregate milestone data
        var totalMilestoneHours = 0;
        var totalAddedMilestoneLOC = 0;
        var totalRemovedMilestoneLOC = 0;
        var totalMilestoneLOC = 0;
        var totalMilestonePoints = 0;

        // console.log('Messing with tasks for milestone ' + milestone.key() + ' and project ' + project.key() + ' at time ' + new Date().toLocaleTimeString());
        userMilestone.ref().once('value', function(milestoneSnapshot) {
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
        // console.log('Updating milestone ' + userMilestone.key() + ' in project ' + userProject.key() + ' for user ' + userRef.key());
        // console.log('total_hours: ' + totalMilestoneHours +
        //     '\nadded_lines_of_code: ' + totalAddedMilestoneLOC +
        //     '\nremoved_lines_of_code: ' + totalRemovedMilestoneLOC +
        //     '\ntotal_lines_of_code: ' + totalMilestoneLOC +
        //     '\ntotal_points: ' + totalMilestonePoints
        // );
        userMilestone.ref().update({
            'total_hours': totalMilestoneHours,
            'added_lines_of_code': totalAddedMilestoneLOC,
            'removed_lines_of_code': totalRemovedMilestoneLOC,
            'total_lines_of_code': totalMilestoneLOC,
            'total_points': totalMilestonePoints
        });

        // aggregate project data
        totalProjectHours += totalMilestoneHours;
        totalProjectAddedLOC += totalAddedMilestoneLOC;
        totalProjectRemovedLOC += totalRemovedMilestoneLOC;
        totalProjectLOC += totalMilestoneLOC;
        totalProjectPoints += totalMilestonePoints;

    });
    // console.log('Updating project ' + userProject.key() + ' for user ' + userRef.key());
    // console.log('total_hours: ' + totalProjectHours +
    //     '\nadded_lines_of_code: ' + totalProjectAddedLOC +
    //     '\nremoved_lines_of_code: ' + totalProjectRemovedLOC +
    //     '\ntotal_lines_of_code: ' + totalProjectLOC +
    //     '\ntotal_points: ' + totalProjectPoints
    // );
    userProject.ref().update({
        'total_hours': totalProjectHours,
        'added_lines_of_code': totalProjectAddedLOC,
        'removed_lines_of_code': totalProjectRemovedLOC,
        'total_lines_of_code': totalProjectLOC,
        'total_points': totalProjectPoints
    });

    var userPoints = 0;
    userRef.once('value', function(user) {
        user.child('projects').forEach(function(project) {
            if (project.hasChild('total_points')) {
                // console.log('adding user points');
                userPoints += project.child('total_points').val();
            }
        });
    });
    calculateAvailablePoints(userRef, userPoints);
    // console.log('changing user ' + userRef.key());
    userRef.update({
        'total_points': userPoints,
    });
}

/*
 * Calculates percentage contribution to lines of code and hours spent at the milestone and project levels.
 */
function updateLocAndHoursContribs(projectRef) {
    projectRef.once('value', function(project) {
        // add total project contributions here
        var projAddedLOC = project.child(addedLines).val();
        var projRemovedLOC = project.child(removedLines).val();
        var projTotalLOC = project.child(totalLines).val();
        var projHours = project.child(totalHours).val();

        project.child('milestones').forEach(function(milestone) {
            // add total milestone contributions here
            var mileAddedLOC = milestone.child('added_lines_of_code').val();
            var mileRemovedLOC = milestone.child('removed_lines_of_code').val();
            var mileTotalLOC = milestone.child('total_lines_of_code').val();
            var mileHours = milestone.child('total_hours').val();

            project.child('members').forEach(function(memberSnapshot) {
                var memberRef = usersRef.child(memberSnapshot.key());
                memberRef.once('value', function(member) {
                    if (member.hasChild('projects/' + project.key() + '/milestones/' + milestone.key())) {
                        var userMilestone = member.child('projects/' + project.key() + '/milestones/' + milestone.key());
                        // add user milestone contributions here
                        var mileContribAddedLOC = userMilestone.child(addedLines).val();
                        var mileContribRemovedLOC = userMilestone.child(removedLines).val();
                        var mileContribHours = userMilestone.child(totalHours).val();
                        var mileContribTotalLOC = userMilestone.child(totalLines).val();

                        // update milestone stuff 
                        // Add new percentage calculations here
                        var mileAddPercent = calculatePercentage(mileContribAddedLOC, mileAddedLOC);
                        var mileRemPercent = calculatePercentage(mileContribRemovedLOC, mileRemovedLOC);
                        var mileLOCPercent = calculatePercentage(mileContribTotalLOC, mileTotalLOC);
                        var mileHourPercent = calculatePercentage(mileContribHours, mileHours);

                        console.log("Updating percents for user " + memberSnapshot.key() + " At milestone " + userMilestone.key() + " and project " + projectRef.key());
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
                // add user project contributions here
                var projContribAddedLOC = member.child('projects/' + projectID + '/added_lines_of_code').val();
                var projContribRemovedLOC = member.child('projects/' + projectID + '/removed_lines_of_code').val();
                var projContribTotalLOC = member.child('projects/' + projectID + '/total_lines_of_code').val();
                var projContribHours = member.child('projects/' + projectID + '/total_hours').val();

                // update project stuff
                // add new percentage calculations here
                var projAddedLOCPercent = calculatePercentage(projContribAddedLOC, projAddedLOC);
                var projRemovedLOCPercent = calculatePercentage(projContribRemovedLOC, projRemovedLOC);
                var projTotalLOCPercent = calculatePercentage(projContribTotalLOC, projTotalLOC);
                var projHoursPercent = calculatePercentage(projContribHours, projHours);

                // console.log("Updating percents for user " + memberSnapshot.key() + " at project " + projectRef.key());
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
 * Calculates percentage current/total
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

/*
 * Calculates the number of available points after a trophy has been added
 */
function calculateAvailablePoints(userRef, trophyID) {
    console.log('calculating available points for user ' + userRef.key());
    trophiesRef.once('value', function(trophies) {
        if (trophies.child(trophyID) === null) return;
        userRef.once('value', function(user) {
            console.log('got here');
            var availablePoints = user.child('available_points').val();
            availablePoints -= trophies.child(trophyID + '/cost').val();
            userRef.update({
                'available_points': availablePoints
            });
        });
    });
}