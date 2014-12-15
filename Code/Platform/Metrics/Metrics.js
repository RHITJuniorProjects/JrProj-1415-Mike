/**
 * Metric collection script for CSSE371 Henry project
 * Authors: Sean Carter, Abby Mann, Andrew Davidson, Matt Rocco, Jonathan Jenkins, Adam Michael
 */


var Firebase = require('firebase');

var production = 'https://henry-production.firebaseio.com';
var staging = 'https://henry-staging.firebaseio.com';
var test = 'https://henry-test.firebaseio.com';

// used to test changes to the script so we don't damage the real DB. 
var metricsTest = 'https://henry-metrics-test.firebaseio.com/';

var metricsTestToken = "swPpsOgpNn7isrfxeoWNNV7yxLy85j94JO7p9lDf"
var testTestToken = "FDrYDBNvRCgq0kGonjmsPl0gUwXvxcqUdgaCQ1FI"
    // TODO: set this to the appropriate database, potentially with commandline override
    // commandline version could also have a flag for which DB to use?
var firebaseUrl = test;

var FirebaseTokenGenerator = require("firebase-token-generator");
//this token is for metricsTest
var tokenGenerator = new FirebaseTokenGenerator(testTestToken);
var token = tokenGenerator.createToken({
    uid: "nodeServer"
});

var fbRef = new Firebase(firebaseUrl);
var commitsRef = new Firebase(firebaseUrl + '/commits');
var usersRef = new Firebase(firebaseUrl + '/users');
var projectsRef = new Firebase(firebaseUrl + '/projects');
var testRef = new Firebase(firebaseUrl + "/test");

fbRef.authWithCustomToken(token, function(error, authData) {
    if (error) {
        console.log("Login Failed!", error);
    }
    else {
        console.log("Login Succeeded!", authData);
    }
});

// TODO: Winter term: extract child getters/updates to string vars
var addedLines = 'added_lines_of_code';
var removedLines = 'removed_lines_of_code';
var totalLines = 'total_lines_of_code';
var totalHours = 'total_hours';
var updatedHourEst = 'updated_hour_estimate';


// on 'child added' returns each child one at a time the first run, then only the one child that is added
projectsRef.on('child_added', function(project) {
    // console.log('Messing with project ' + project.name() + ' at time ' + new Date().toLocaleTimeString());
    console.log('Added listener for project ' + project.name());
    listenToProject(project);
});


function listenToProject(project) {
    var projectID = project.name();
    var projectMembers = project.child('members');

    //adds up tasks in the project backlog in order to properly calculate correct task complete percent
    var totalProjectBacklogTask = 0;
    var backlog = project.child('backlog');
    backlog.forEach(function(item) {
        totalProjectBacklogTask++;
    });

    projectMembers.ref().on('child_added', function(newMember) {
        // console.log('adding new member ' + newMember.name() + ' at time ' + new Date().toLocaleTimeString());
        var userName = newMember.name();
        usersRef.child(userName + '/projects/' + projectID).ref().update({
            'role': newMember.val()
        });
    });

    projectMembers.ref().on('child_removed', function(deletedMember) {
        // console.log('deleting member ' + deletedMember.name() + ' at time ' + new Date().toLocaleTimeString());
        var userName = deletedMember.name();
        usersRef.child(userName + '/projects/' + projectID).ref().update({
            'role': 'removed'
        });
    });
    
    // The Task Listener: listens to changes in the task.  
    // When activated, checks status of task and updates if needed, then recalculates and updates the total hours, 
    // percent hours, percent task, percent milestone 
    project.child('milestones').ref().on('value', function(milestones) {
        // TODO: this listener is running 4 times. Always. Hence, quadruplicate data for the burndown.
        console.log("Caught Milestone event for project "+ project.name())
        listenToMilestones(milestones, project, totalProjectBacklogTask);
    });
}

function listenToMilestones(milestones, project, totalProjectBacklogTask) {
    var projectID = project.name();
    //sets up vars for task listener
    // console.log('Messing with milestones for project ' + project.name() + ' at time ' + new Date().toLocaleTimeString());
    var milestoneCount = 0,
        milestonesCompleted = 0,
        totalProjectTasks = 0,
        totalProjectTasksComplete = 0,
        projectHours = 0,
        totalProjectEstimatedHours = 0,
        projectAddedLOC = 0,
        projectTotalLOC = 0,
        projectRemovedLOC = 0;
    // TODO: we *could* extract the accumulator vars to a JSO, and edit its properties from another function
    //       thus, we could flatten the listener structure further.
    milestones.forEach(function(milestone) {
        milestoneCount++;
        var milestoneEstimatedHours = 0,
            milestoneID = milestone.name(),
            milestoneHours = 0,
            milestoneAddedLOC = 0,
            milestoneRemovedLOC = 0,
            milestoneTotalLOC = 0,
            taskCount = 0,
            completedTasks = 0;
        milestone.child('tasks').ref().once('value', function(tasks) {
            tasks.forEach(function(task) {
                // console.log('Messing with task ' + task.name() + ' for milestone ' + milestone.name() + ' in project ' + project.name() + ' at time ' + new Date().toLocaleTimeString());
                taskCount++;
                var isCompleted = task.child('is_completed').val();
                if (isCompleted) {
                    completedTasks++;
                }
                milestoneHours += task.child(totalHours).val();
                milestoneEstimatedHours += task.child(updatedHourEst).val();
                var taskID = task.name();
                milestoneRemovedLOC += task.child(removedLines).val();
                milestoneAddedLOC += task.child(addedLines).val();
                milestoneTotalLOC += task.child(totalLines).val();

                // update user with task info
                var assignee = task.child('assignedTo').val();
                if (assignee !== null) {
                    var taskNode = usersRef.child(assignee + '/projects/' + projectID + '/milestones/' + milestoneID + '/tasks/' + taskID).ref();
                    taskNode.update({
                        'total_hours': task.child(totalHours).val(),
                        'added_lines_of_code': task.child(addedLines).val(),
                        'removed_lines_of_code': task.child(removedLines).val(),
                        'total_lines_of_code': task.child(totalLines).val()
                    });
                }

                //adds up estimated hours on milestone.  If changed, update, and then update project.
                var milestoneEstHoursSum = 0;
                var milestoneTotalHours = 0;

                var currentMilestoneEstHours = milestone.child('total_estimated_hours').val();
                var currentMilestoneHours = milestone.child(totalHours).val();

                milestone.child('tasks').forEach(function(taskToSum) {
                    milestoneEstHoursSum += taskToSum.child(updatedHourEst).val();
                    milestoneTotalHours += taskToSum.child(totalHours).val();
                });

                // TODO: pull this up another level?
                if (milestoneEstHoursSum !== currentMilestoneEstHours) {
                    var hoursChanged = milestoneEstHoursSum - currentMilestoneEstHours;
                    var currentProjectEstHours = project.child('total_estimated_hours').val();
                    var updatedProjectHours = currentProjectEstHours + hoursChanged;
                    
                    //CAUSES RECALCULATION (+1), both
                    milestone.ref().update({
                        'total_estimated_hours': milestoneEstHoursSum
                    });
                    
                    project.ref().update({
                        'total_estimated_hours': updatedProjectHours
                    });



                }
                var taskStatus = task.child('status').val();
                var isClosed = (taskStatus === 'Closed');
                var updatedHourEstimate = task.child(updatedHourEst).val();
                var taskTotalHours = task.child(totalHours).val();
                var taskTotalLinesOfCode = task.child(totalLines).val();
                var addedLOC = task.child(addedLines).val();
                var removedLOC = task.child(removedLines).val();

                //Default values for tasks, if no value default to....

                if (isCompleted === null) {
                    isCompleted = false;
                }
                if (taskStatus === null) {
                    taskStatus = 'New';
                }
                if (isClosed === null) {
                    isClosed = false;
                }
                if (updatedHourEstimate === null) {
                    updatedHourEstimate = task.child('original_hour_estimate').val();
                }
                if (taskTotalHours === null) {
                    taskTotalHours = 0;
                }
                if (taskTotalLinesOfCode === null) {
                    taskTotalLinesOfCode = 0;
                }
                if (addedLOC === null) {
                    addedLOC = 0;
                }
                if (removedLOC === null) {
                    removedLOC = 0;
                }
                //Calculates percent hours completed
                var taskHrPercent = calculatePercentage(taskTotalHours, updatedHourEstimate);

                //Update task


                //CAUSES RECALCULATION FOR ONLY FIRST MILESTONE (+1)
                task.ref().update({
                    'percent_complete': taskHrPercent,
                    'is_completed': isCompleted,
                    'status': taskStatus,
                    'updated_hour_estimate': updatedHourEstimate,
                    'total_hours': taskTotalHours,
                    'total_lines_of_code': taskTotalLinesOfCode,
                    'added_lines_of_code': addedLOC,
                    'removed_lines_of_code': removedLOC
                });

                //If status is no longer closed, change is_completed
                if (!isClosed && isCompleted) {
                    task.ref().update({
                        'is_completed': false
                    });
                }

                //if the status is closed, but it isn't marked as complete, this means that it was a new update, 
                //and 'is_completed' needs to be updated as well
                if (isClosed && !isCompleted) {

                    var projectTasksCompleted = project.child('tasks_completed').val();
                    projectTasksCompleted += 1;
                    var milestoneTasksCompleted = milestone.child('tasks_completed').val();
                    milestoneTasksCompleted += 1;
                    var milestoneTotalTasks = project.child('total_tasks').val();
                    var milestoneTaskPercent = calculatePercentage(milestoneTasksCompleted, milestone.child('total_tasks').val());
                    var projectTaskPercent = calculatePercentage(projectTasksCompleted, milestoneTotalTasks);

                    var milestonesCompleted = project.child('milestones_completed').val();
                    if (milestoneTotalTasks !== 0 && milestoneTasksCompleted === milestoneTotalTasks) {
                        milestonesCompleted += 1;
                    }

                    task.ref().update({
                        'is_completed': true
                    });

                    //TODO: create new method to update a project and milestone with updated task and milestone completeness percents

                }
            });
        });
        var taskPercentage = calculatePercentage(completedTasks, taskCount);
        var hoursPercent = calculatePercentage(milestoneHours, milestoneEstimatedHours);


        // THIS CAUSES A RECALCULATION FOR THE MILESTONE (+1), both
        milestone.ref().update({
            'task_percent': taskPercentage,
            'total_tasks': taskCount,
            'tasks_completed': completedTasks,
            'total_hours': milestoneHours,
            'total_estimated_hours': milestoneEstimatedHours,
            'removed_lines_of_code': milestoneRemovedLOC,
            'added_lines_of_code': milestoneAddedLOC,
            'total_lines_of_code': milestoneTotalLOC,
            'hours_percent': hoursPercent
        });

        if (taskCount === completedTasks) {
            milestonesCompleted++;
        }

        totalProjectTasks += taskCount;
        totalProjectTasksComplete += completedTasks;
        projectHours += milestoneHours;
        totalProjectEstimatedHours += milestoneEstimatedHours;
        projectAddedLOC += milestoneAddedLOC;
        projectRemovedLOC += milestoneRemovedLOC;
        projectTotalLOC += milestoneTotalLOC;
    });

    totalProjectTasks += totalProjectBacklogTask;

    var projectTaskPercent = calculatePercentage(totalProjectTasksComplete, totalProjectTasks);
    var projectMilestonePercent = calculatePercentage(milestonesCompleted, milestoneCount);
    var projectHoursPercent = calculatePercentage(projectHours, totalProjectEstimatedHours);

    project.ref().update({
        'total_milestones': milestoneCount,
        'tasks_completed': totalProjectTasksComplete,
        'total_tasks': totalProjectTasks,
        'milestones_completed': milestonesCompleted,
        'total_hours': projectHours,
        'task_percent': projectTaskPercent,
        'total_estimated_hours': totalProjectEstimatedHours,
        'removed_lines_of_code': projectRemovedLOC,
        'added_lines_of_code': projectAddedLOC,
        'total_lines_of_code': projectTotalLOC,
        'milestone_percent': projectMilestonePercent,
        'hours_percent': projectHoursPercent
    });

    //burndownChartRefThing.update/push/idk - needs to have timestamp
    var dataPoint = {
        'timestamp': Firebase.ServerValue.TIMESTAMP,
        'estimated_hours_remaining' : totalProjectEstimatedHours - projectHours,
        'hours_completed': projectHours,
        'tasks_completed': totalProjectTasksComplete,
        'tasks_remaining': totalProjectTasks - totalProjectTasksComplete
    }

    if (!project.hasChild('burndown_data')){
        project.ref().update({
            'burndown_data': {'placeholder' : 'placeholder'}
        });
    }
    else {
       var latestData = null;
        project.child('burndown_data').forEach(function(data) {
            // TODO: remove placeholder if we have real data for the initialization
            if (data.name() !== 'placeholder' && (latestData === null || latestData.child('timestamp') < data.child('timestamp'))) {
                latestData = data;
            }
        });
        // console.log("latestData: " + latestData.child('estimated_hours_remaining').val() + ", " 
        //             + latestData.child('hours_completed').val() + ", "
        //             + latestData.child('tasks_completed').val() + ", "
        //             + latestData.child('tasks_remaining').val());
        // console.log("dataPoint: " + dataPoint['estimated_hours_remaining'] + ", "
        //             + dataPoint['hours_completed'] + ", "
        //             + dataPoint['tasks_completed'] + ", "
        //             + dataPoint['tasks_remaining']);
                    
        if (latestData === null 
            || (latestData.child('estimated_hours_remaining').val() !== dataPoint['estimated_hours_remaining'] 
                || latestData.child('hours_completed').val() !== dataPoint['hours_completed'] 
                || latestData.child('tasks_completed').val() !== dataPoint['tasks_completed'] 
                || latestData.child('tasks_remaining').val() !== dataPoint['tasks_remaining'])) {
            // if anything is different, push the new data point
            project.ref().child('burndown_data').push(dataPoint);
        }
    }
}
    // Add any nonexisting commit branches for projects
projectsRef.once('value', function(projects) {
    projects.forEach(function(project) {
        var projectName = project.name();
        // TODO: extract to method so we can run it for new projects?
        commitsRef.once('value', function(commitProjects) {
            if (!commitProjects.hasChild(projectName)) {
                var newBranch = {};
                newBranch[projectName] = {
                    'description': 'placeholder'
                };
                commitsRef.update(newBranch);
                console.log("Added commit branch for project " + projectName);
            }
        });
    });
});

// add a listener to commits for new projects
commitsRef.on('child_added', function(project) {
    var isFirstRunThrough = true;
    // TODO: run calculateMetrics( once('value') forEach commit ) the first time
    // add a listener to each project for new commits
    project.ref().on('child_added', function(commit) {
        if (isFirstRunThrough) {
            // TODO: recalculate metrics from commit tree
            // We should "only" need to zero out these fields at the task level:
            //     added_lines_of_code
            //     removed_lines_of_code
            //     total_hours
            //     total_lines_of_code
            // then run calculateMetrics() forEach commit
            return;
        }
        calculateMetrics(project.name(), commit);
    });
    isFirstRunThrough = false;
    console.log('added commit listener for project ' + project.name());
});


usersRef.on('child_changed', function(user) {
    // console.log('Messing with user at time ' + new Date().toLocaleTimeString());
    user.child('projects').forEach(function(project) {
        listenToUserProject(project);
    });
});

function listenToUserProject(project) {
    var totalProjectHours = 0;
    var totalProjectAddedLOC = 0;
    var totalProjectRemovedLOC = 0;
    var totalProjectLOC = 0;

    project.child('milestones').forEach(function(milestone) {
        var totalMilestoneHours = 0;
        var totalAddedMilestoneLOC = 0;
        var totalRemovedMilestoneLOC = 0;
        var totalMilestoneLOC = 0;

        // console.log('Messing with tasks for milestone ' + milestone.name() + ' and project ' + project.name() + ' at time ' + new Date().toLocaleTimeString());
        milestone.child('tasks').forEach(function(task) {
            totalMilestoneHours += task.child(totalHours).val();
            totalAddedMilestoneLOC += task.child(addedLines).val();
            totalRemovedMilestoneLOC += task.child(removedLines).val();
            totalMilestoneLOC += task.child(totalLines).val();
        });

        milestone.ref().update({
            'total_hours': totalMilestoneHours,
            'added_lines_of_code': totalAddedMilestoneLOC,
            'removed_lines_of_code': totalRemovedMilestoneLOC,
            'total_lines_of_code': totalMilestoneLOC
        });

        totalProjectHours += totalMilestoneHours;
        totalProjectAddedLOC += totalAddedMilestoneLOC;
        totalProjectRemovedLOC += totalRemovedMilestoneLOC;
        totalProjectLOC += totalMilestoneLOC;

    });
    project.ref().update({
        'total_hours': totalProjectHours,
        'added_lines_of_code': totalProjectAddedLOC,
        'removed_lines_of_code': totalProjectRemovedLOC,
        'total_lines_of_code': totalProjectLOC
    });
}

// calculates metrics which need to be updated on a commit
function calculateMetrics(projectID, commit) {
    console.log('Calculating metrics for: ' + projectID + ', ' + commit.name());

    // TODO: add null checks later in code for these
    var hours = commit.child('hours').val();
    var added_lines_of_code = commit.child(addedLines).val();
    var removed_lines_of_code = commit.child(removedLines).val();
    var total_lines_of_code = commit.child(totalLines).val();
    var milestone = commit.child('milestone').val();
    var user = commit.child('user').val();
    var task = commit.child('task').val();

    projectsRef.child(projectID + '/milestones/' + milestone + '/tasks/' + task)
        .once('value', function(taskBranch) {
            // console.log('Updating task ' + taskBranch.name() + ' for milestone ' + milestone + ' and project ' + projectID + ' at time ' + new Date().toLocaleTimeString());
            var currentHours = taskBranch.child(totalHours).val();
            var newHours = currentHours + commit.child('hours').val();

            var totalLOC = taskBranch.child(totalLines).val();
            var addedLOC = commit.child(addedLines).val();
            var removedLOC = commit.child(removedLines).val();

            totalLOC += addedLOC - removedLOC;
            var totalAddedLOC = taskBranch.child(addedLines).val() + addedLOC;
            var totalRemovedLOC = taskBranch.child(removedLines).val() + removedLOC;

            taskBranch.ref().update({
                'total_hours': newHours,
                'total_lines_of_code': totalLOC,
                'added_lines_of_code': totalAddedLOC,
                'removed_lines_of_code': totalRemovedLOC,
            });
        });
    updateLocAndHoursContribs(projectID, milestone);
}

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
    //on a commit, updates the percentage of each user's percentage of contributed LOC and hours on the project and milestone associated with the commit

function updateLocAndHoursContribs(projectID, milestoneID) {
    // console.log('inside of updateHoursAndLocContribs!!!\n project: ' + projectID + '\n milestone: ' + milestoneID + '\n');

    projectsRef.child(projectID).once('value', function(project) {
        var projAddedLOC = project.child(addedLines).val();
        var projRemovedLOC = project.child(removedLines).val();
        var projTotalLOC = project.child(totalLines).val();
        var projHours = project.child(totalHours).val();

        var mileAddedLOC = project.child('milestones/' + milestoneID + '/added_lines_of_code').val();
        var mileRemovedLOC = project.child('milestones/' + milestoneID + '/removed_lines_of_code').val();
        var mileTotalLOC = project.child('milestones/' + milestoneID + '/total_lines_of_code').val();
        var mileHours = project.child('milestones/' + milestoneID + '/total_hours').val();


        project.child('members').forEach(function(memberSnapshot) {
            var memberRef = usersRef.child(memberSnapshot.name());
            memberRef.once('value', function(member) {
                // console.log('Updating member ' + member.name() + ' for project ' + project.name() + ' at time ' + new Date().toLocaleTimeString());
                var projContribAddedLOC = member.child('projects/' + projectID + '/added_lines_of_code').val();

                var projContribRemovedLOC = member.child('projects/' + projectID + '/removed_lines_of_code').val();
                var projContribTotalLOC = member.child('projects/' + projectID + '/total_lines_of_code').val();
                var projContribHours = member.child('projects/' + projectID + '/total_hours').val();

                // update project stuff
                var projAddedLOCPercent = calculatePercentage(projContribAddedLOC, projAddedLOC);
                var projRemovedLOCPercent = calculatePercentage(projContribRemovedLOC, projRemovedLOC);
                var projTotalLOCPercent = calculatePercentage(projContribTotalLOC, projTotalLOC);
                var projHoursPercent = calculatePercentage(projContribHours, projHours);

                // console.log('added loc percent : ' + projAddedLOCPercent + '\n projContribAddedLOC: ' + projContribAddedLOC + '\n projTotAddedLOC: ' + projAddedLOC + '\n');

                member.child('projects/' + projectID).ref().update({
                    'hours_percent': projHoursPercent,
                    'removed_loc_percent': projRemovedLOCPercent,
                    'added_loc_percent': projAddedLOCPercent,
                    'loc_percent': projTotalLOCPercent

                });
                if (member.hasChild('projects/' + projectID + '/milestones/' + milestoneID)) {
                    var milestone = member.child('projects/' + projectID + '/milestones/' + milestoneID);

                    var mileContribAddedLOC = milestone.child(addedLines).val();
                    var mileContribRemovedLOC = milestone.child(removedLines).val();
                    var mileContribHours = milestone.child(totalHours).val();
                    var mileContribTotalLOC = milestone.child(totalLines).val();

                    // update milestone stuff 
                    var mileAddPercent = calculatePercentage(mileContribAddedLOC, mileAddedLOC);
                    var mileRemPercent = calculatePercentage(mileContribRemovedLOC, mileRemovedLOC);
                    var mileLOCPercent = calculatePercentage(mileContribTotalLOC, mileTotalLOC);
                    var mileHourPercent = calculatePercentage(mileContribHours, mileHours);


                    member.child('projects/' + projectID + '/milestones/' + milestoneID).ref().update({
                        'added_loc_percent': mileAddPercent,
                        'removed_loc_percent': mileRemPercent,
                        'loc_percent': mileLOCPercent,
                        'hours_percent': mileHourPercent
                    });
                }
            });
        });
    });
}
