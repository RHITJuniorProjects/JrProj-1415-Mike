/**
 * Metric collection script for CSSE371 Henry project
 * Authors: Sean Carter, Abby Mann, Andrew Davidson, Matt Rocco, Jonathan Jenkins, Adam Michael
 */


//TODO: add in burndown data, functionality for removed tasks?, aggregate commit data on startup

var Firebase = require('firebase');

var production = 'https://henry-production.firebaseio.com';
var staging = 'https://henry-staging.firebaseio.com';
var test = 'https://henry-test.firebaseio.com';

// used to test changes to the script so we don't damage the real DB. 
var metricsTest = 'https://henry-metrics-test.firebaseio.com/';

var metricsTestToken = 'swPpsOgpNn7isrfxeoWNNV7yxLy85j94JO7p9lDf';
var testTestToken = 'FDrYDBNvRCgq0kGonjmsPl0gUwXvxcqUdgaCQ1FI';
// TODO: set this to the appropriate database, potentially with commandline override
// commandline version could also have a flag for which DB to use?
var firebaseUrl = metricsTest;

var FirebaseTokenGenerator = require('firebase-token-generator');
//this token is for metricsTest
var tokenGenerator = new FirebaseTokenGenerator(metricsTestToken);
var token = tokenGenerator.createToken({
    uid: 'nodeServer'
});

var fbRef = new Firebase(firebaseUrl);
var commitsRef = new Firebase(firebaseUrl + '/commits');
var usersRef = new Firebase(firebaseUrl + '/users');
var projectsRef = new Firebase(firebaseUrl + '/projects');
var testRef = new Firebase(firebaseUrl + '/test'); // TODO: what is this?

fbRef.authWithCustomToken(token, function(error, authData) {
    if (error) {
        console.log('Login Failed!', error);
    }
    else {
        console.log('Login Succeeded!', authData);
    }
});

var addedLines = 'added_lines_of_code';
var removedLines = 'removed_lines_of_code';
var totalLines = 'total_lines_of_code';
var totalHours = 'total_hours';
var updatedHourEst = 'updated_hour_estimate';


// on 'child added' returns each child one at a time the first run, then only the one child that is added
projectsRef.on('child_added', function(project) {
    // console.log('Messing with project ' + project.key() + ' at time ' + new Date().toLocaleTimeString());
    console.log('Added listener for project ' + project.key());
    listenToProject(project);
});


function listenToProject(project) {
    var projectID = project.key();
    var projectMembers = project.child('members');

    //adds up tasks in the project backlog in order to properly calculate correct task complete percent
    //TODO: comment out
    var totalProjectBacklogTask = 0;
    var backlog = project.child('backlog');
    backlog.forEach(function(item) {
        totalProjectBacklogTask++;
    });

    projectMembers.ref().on('child_added', function(newMember) {
        // console.log('adding new member ' + newMember.key() + ' at time ' + new Date().toLocaleTimeString());
        var userName = newMember.key();
        usersRef.child(userName + '/projects/' + projectID).ref().update({
            'role': newMember.val()
        });
    });

    projectMembers.ref().on('child_removed', function(deletedMember) {
        // console.log('deleting member ' + deletedMember.key() + ' at time ' + new Date().toLocaleTimeString());
        var userName = deletedMember.key();
        usersRef.child(userName + '/projects/' + projectID).ref().update({
            'role': 'removed'
        });
    });

    // The Task Listener: listens to changes in the task.  
    // When activated, checks status of task and updates if needed, then recalculates and updates the total hours, 
    // percent hours, percent task, percent milestone

    // project.child('milestones').ref().on('value', function(milestones) {
    //     // TODO: this listener is running 2 or 3 or 4 times. Always. Hence, quadruplicate data for the burndown.
    //     console.log('Milestone event for project ' + project.key());
    //     listenToMilestones(milestones, project, totalProjectBacklogTask);
    // });


    project.child('milestones').ref().on('child_added', function(milestone) {
        addMilestoneListeners(milestone.ref());
    });
}

function addMilestoneListeners(milestoneRef) {
    console.log('inside milestone listener');
    // TODO: separate task update and data aggregation?
    // Alternatively, only aggregate the first time, then overwrite values as necessary, same as our plan for commits
    //     This would allow us to do on('child_changed'), instead of on('value'), and thus not cause a listener trigger
    //     Potential downside: new task may cause strangeness.

    //initialize everything in the milestone to 0 so everything aggregates correctly in following listeners
    milestoneRef.update({
        'task_percent': 0,
        'total_tasks': 0,
        'tasks_completed': 0,
        'total_hours': 0,
        'total_estimated_hours': 0,
        'removed_lines_of_code': 0,
        'added_lines_of_code': 0,
        'total_lines_of_code': 0,
        'hours_percent': 0
    });


    milestoneRef.child('tasks').on('child_added', function(newTask) {
        // do new task stuff
        console.log('a child is born'); // blame Abby
        //set defaults    
        setDefaults(newTask);
        taskChanged(milestoneRef);
        // following may be automatically called since task is changed when setting defaults
        // //aggregate task data    
        // aggregateTaskData(milestone, newTask);
        // aggregateMilestoneData(project, milestone);

    });

    milestoneRef.child('tasks').on('child_changed', function(changedTask) {
        // do changed task stuff
        taskChanged(milestoneRef);

    });
}

function taskChanged(milestoneRef) {

    //note: do not set defaults

    //re-initialize milestone and aggregate task data for all tasks
    milestoneRef.update({
        'task_percent': 0,
        'total_tasks': 0,
        'tasks_completed': 0,
        'total_hours': 0,
        'total_estimated_hours': 0,
        'removed_lines_of_code': 0,
        'added_lines_of_code': 0,
        'total_lines_of_code': 0,
        'hours_percent': 0
    });

    milestoneRef.child('tasks').once('value', function(tasks) {
        tasks.forEach(function(task) {
            aggregateTaskData(milestoneRef, task);
        });
    });
    // update the project (by re-initializing and aggregating data over all milestones)
    var projectRef = milestoneRef.parent().parent();

    milestoneRef.once('value', function(milestone) {
        projectRef.once('value', function(project) {
            //initialize num tasks to backlog tasks
            var backlogTasks = 0;
            project.child('backlog').forEach(function(item) {
                backlogTasks++;
            });

            project.ref().update({
                'milestone_percent': 0,
                'total_milestones': 0,
                'milestones_completed': 0,
                'total_hours': 0,
                'total_estimated_hours': 0,
                'removed_lines_of_code': 0,
                'added_lines_of_code': 0,
                'total_lines_of_code': 0,
                'hours_percent': 0,
                'total_tasks': backlogTasks,
                'task_percent': 0
            });

        });
        // aggregate milestone data
        aggregateMilestoneData(projectRef, milestone);
    });

}

// function listenToMilestones(milestones, project, totalProjectBacklogTask) {
//     var projectID = project.key();
//     if (projectID === 'defaults') return;
//     //sets up vars for task listener
//     // console.log('Messing with milestones for project ' + project.key() + ' at time ' + new Date().toLocaleTimeString());
//     var milestoneCount = 0,
//         milestonesCompleted = 0,
//         totalProjectTasks = 0,
//         totalProjectTasksComplete = 0,
//         projectHours = 0,
//         totalProjectEstimatedHours = 0,
//         projectAddedLOC = 0,
//         projectTotalLOC = 0,
//         projectRemovedLOC = 0;
//     // TODO: we *could* extract the accumulator vars to a JSON Blob, and edit its properties from another function.
//     //       thus, we could flatten the listener structure further.
//     milestones.forEach(function(milestone) {
//         milestoneCount++;
//         var milestoneEstimatedHours = 0,
//             milestoneID = milestone.key(),
//             milestoneHours = 0,
//             milestoneAddedLOC = 0,
//             milestoneRemovedLOC = 0,
//             milestoneTotalLOC = 0,
//             taskCount = 0,
//             completedTasks = 0;
//         // TODO: extract this to the new milestone listener function
//         milestone.child('tasks').ref().once('value', function(tasks) {
//             tasks.forEach(function(task) {
//                 // console.log('Messing with task ' + task.key() + ' for milestone ' + milestone.key() + ' in project ' + project.key() + ' at time ' + new Date().toLocaleTimeString());
//                 taskCount++;
//                 var isCompleted = task.child('is_completed').val();
//                 if (isCompleted) {
//                     completedTasks++;
//                 }
//                 milestoneHours += task.child(totalHours).val();
//                 milestoneEstimatedHours += task.child(updatedHourEst).val();
//                 var taskID = task.key();
//                 milestoneRemovedLOC += task.child(removedLines).val();
//                 milestoneAddedLOC += task.child(addedLines).val();
//                 milestoneTotalLOC += task.child(totalLines).val();

//                 // update user with task info
//                 var assignee = task.child('assignedTo').val();
//                 if (assignee !== null) {
//                     var taskNode = usersRef.child(assignee + '/projects/' + projectID + '/milestones/' + milestoneID + '/tasks/' + taskID).ref();
//                     taskNode.update({
//                         'total_hours': task.child(totalHours).val(),
//                         'added_lines_of_code': task.child(addedLines).val(),
//                         'removed_lines_of_code': task.child(removedLines).val(),
//                         'total_lines_of_code': task.child(totalLines).val()
//                     });
//                 }

//                 var taskStatus = task.child('status').val();
//                 var isClosed = (taskStatus === 'Closed');

//                 setDefaults(task);


//                 //If status is no longer closed, change is_completed
//                 if (!isClosed && isCompleted) {
//                     task.ref().update({
//                         'is_completed': false
//                     });
//                 }

//                 //if the status is closed, but it isn't marked as complete, this means that it was a new update, 
//                 //and 'is_completed' needs to be updated as well
//                 if (isClosed && !isCompleted) {

//                     var projectTasksCompleted = project.child('tasks_completed').val();
//                     projectTasksCompleted++;
//                     var milestoneTasksCompleted = milestone.child('tasks_completed').val();
//                     milestoneTasksCompleted++;
//                     var milestoneTotalTasks = project.child('total_tasks').val();
//                     var milestoneTaskPercent = calculatePercentage(milestoneTasksCompleted, milestone.child('total_tasks').val());
//                     var projectTaskPercent = calculatePercentage(projectTasksCompleted, milestoneTotalTasks);

//                     var milestonesCompleted = project.child('milestones_completed').val();
//                     if (milestoneTotalTasks !== 0 && milestoneTasksCompleted === milestoneTotalTasks) {
//                         milestonesCompleted++;
//                     }

//                     task.ref().update({
//                         'is_completed': true
//                     });

//                     //TODO: create new method to update a project and milestone with updated task and milestone completeness percents

//                 }
//             });
//         });
//         var taskPercentage = calculatePercentage(completedTasks, taskCount);
//         var hoursPercent = calculatePercentage(milestoneHours, milestoneEstimatedHours);


//         /* 
//         THIS CAUSES A RECALCULATION FOR THE MILESTONE (+1), both
//         The only easy way Sean sees to fix this is to listen to each milestone individually.
//             The overarching listener would then be set to only react to new milestones and add their listeners.
//         TODO: do an isFirstRunThrough thing as with projects above, and ONLY aggregate existing task data.
//         */
//         milestone.ref().update({
//             'task_percent': taskPercentage,
//             'total_tasks': taskCount,
//             'tasks_completed': completedTasks,
//             'total_hours': milestoneHours,
//             'total_estimated_hours': milestoneEstimatedHours,
//             'removed_lines_of_code': milestoneRemovedLOC,
//             'added_lines_of_code': milestoneAddedLOC,
//             'total_lines_of_code': milestoneTotalLOC,
//             'hours_percent': hoursPercent
//         });

//         if (taskCount === completedTasks) {
//             milestonesCompleted++;
//         }

//         totalProjectTasks += taskCount;
//         totalProjectTasksComplete += completedTasks;
//         projectHours += milestoneHours;
//         totalProjectEstimatedHours += milestoneEstimatedHours;
//         projectAddedLOC += milestoneAddedLOC;
//         projectRemovedLOC += milestoneRemovedLOC;
//         projectTotalLOC += milestoneTotalLOC;
//     });

//     totalProjectTasks += totalProjectBacklogTask;

//     var projectTaskPercent = calculatePercentage(totalProjectTasksComplete, totalProjectTasks);
//     var projectMilestonePercent = calculatePercentage(milestonesCompleted, milestoneCount);
//     var projectHoursPercent = calculatePercentage(projectHours, totalProjectEstimatedHours);

//     project.ref().update({
//         'total_milestones': milestoneCount,
//         'tasks_completed': totalProjectTasksComplete,
//         'total_tasks': totalProjectTasks,
//         'milestones_completed': milestonesCompleted,
//         'total_hours': projectHours,
//         'task_percent': projectTaskPercent,
//         'total_estimated_hours': totalProjectEstimatedHours,
//         'removed_lines_of_code': projectRemovedLOC,
//         'added_lines_of_code': projectAddedLOC,
//         'total_lines_of_code': projectTotalLOC,
//         'milestone_percent': projectMilestonePercent,
//         'hours_percent': projectHoursPercent
//     });

//     var dataPoint = {
//         'timestamp': Firebase.ServerValue.TIMESTAMP,
//         'estimated_hours_remaining': totalProjectEstimatedHours - projectHours,
//         'hours_completed': projectHours,
//         'tasks_completed': totalProjectTasksComplete,
//         'tasks_remaining': totalProjectTasks - totalProjectTasksComplete
//     };

//     if (!project.hasChild('burndown_data')) {
//         project.ref().update({
//             'burndown_data': {
//                 'placeholder': 'placeholder'
//             }
//         });
//     }
//     project.ref().child('burndown_data').push(dataPoint);
// }

// Add any nonexisting commit branches for projects
projectsRef.once('value', function(projects) {
    projects.forEach(function(project) {
        var projectName = project.key();
        // TODO: extract to method so we can run it for new projects?
        commitsRef.once('value', function(commitProjects) {
            if (!commitProjects.hasChild(projectName) && projectName !== 'defaults') {
                var newBranch = {};
                newBranch[projectName] = {
                    'description': 'placeholder'
                };
                commitsRef.update(newBranch);
                console.log('Added commit branch for project ' + projectName);
            }
        });
    });
});

// add a listener to commits for new projects
commitsRef.on('child_added', function(project) {
    var isFirstRunThrough = true;
    // add a listener to each project for new commits
    project.ref().on('child_added', function(commit) {
        if (isFirstRunThrough) {
            // TODO: recalculate metrics from commit tree
            // We should 'only' need to zero out these fields at the task level:
            //     added_lines_of_code
            //     removed_lines_of_code
            //     total_hours
            //     total_lines_of_code
            // then run calculateMetrics() forEach commit
            return;
        }
        calculateMetrics(project.key(), commit);
    });
    isFirstRunThrough = false;
    console.log('added commit listener for project ' + project.key());
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

        // console.log('Messing with tasks for milestone ' + milestone.key() + ' and project ' + project.key() + ' at time ' + new Date().toLocaleTimeString());
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
    console.log('Calculating metrics for: ' + projectID + ', ' + commit.key());

    // TODO: add null checks later in code for these
    // TODO: are the unused vars here part of the code Abby removed?
    var hours = commit.child('hours').val();
    var added_lines_of_code = commit.child(addedLines).val();
    var removed_lines_of_code = commit.child(removedLines).val();
    var total_lines_of_code = commit.child(totalLines).val();
    var milestone = commit.child('milestone').val();
    var user = commit.child('user').val();
    var task = commit.child('task').val();

    projectsRef.child(projectID + '/milestones/' + milestone + '/tasks/' + task)
        .once('value', function(taskBranch) {
            // console.log('Updating task ' + taskBranch.key() + ' for milestone ' + milestone + ' and project ' + projectID + ' at time ' + new Date().toLocaleTimeString());
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
            var memberRef = usersRef.child(memberSnapshot.key());
            memberRef.once('value', function(member) {
                // console.log('Updating member ' + member.key() + ' for project ' + project.key() + ' at time ' + new Date().toLocaleTimeString());
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

function setDefaults(task) {
    var isCompleted = task.child('is_completed').val();
    var taskStatus = task.child('status').val();
    var isClosed = (taskStatus === 'Closed');
    var updatedHourEstimate = task.child(updatedHourEst).val();
    var taskTotalHours = task.child(totalHours).val();
    var taskTotalLinesOfCode = task.child(totalLines).val();
    var addedLOC = task.child(addedLines).val();
    var removedLOC = task.child(removedLines).val();

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


    //CAUSES RECALCULATION FOR ONLY FIRST MILESTONE (+1)
    // Update task -> value trigger
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
}

function aggregateTaskData(milestoneRef, task) {
    milestoneRef.once('value', function(milestone) {
        //increase # tasks in milestone    
        var taskCount = milestone.child('total_tasks').val() + 1;

        //if task is completed, increment completed tasks
        var completedTasks = milestone.child('tasks_completed').val();
        if (task.child('status').val() === 'Closed') {
            completedTasks++;
        }

        //increment milestone hours
        var milestoneHours = milestone.child(totalHours).val();
        milestoneHours += task.child(totalHours).val();

        //increment milestone estimated hours
        var milestoneEstimatedHours = milestone.child('total_estimated_hours').val();
        milestoneEstimatedHours += task.child(updatedHourEst).val();

        //increment removedLOC
        var milestoneRemovedLOC = milestone.child(removedLines).val();
        milestoneRemovedLOC += task.child(removedLines).val();

        //increment addedLOC
        var milestoneAddedLOC = milestone.child(addedLines).val();
        milestoneAddedLOC += task.child(addedLines).val();

        //increment totalLOC
        var milestoneTotalLOC = milestone.child(totalLines).val();
        milestoneTotalLOC += task.child(totalLines).val();

        //update task and hours percentages
        var taskPercentage = calculatePercentage(completedTasks, taskCount);
        var hoursPercent = calculatePercentage(milestoneHours, milestoneEstimatedHours);

        //update milestone
        // console.log('updating milestone data');
        // console.log({
        //     'task_percent': taskPercentage,
        //     'total_tasks': taskCount,
        //     'tasks_completed': completedTasks,
        //     'total_hours': milestoneHours,
        //     'total_estimated_hours': milestoneEstimatedHours,
        //     'removed_lines_of_code': milestoneRemovedLOC,
        //     'added_lines_of_code': milestoneAddedLOC,
        //     'total_lines_of_code': milestoneTotalLOC,
        //     'hours_percent': hoursPercent
        // });
        milestoneRef.update({
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

        // update user with task info
        var projectID = milestoneRef.parent().parent().key();
        var milestoneID = milestone.key();
        var taskID = task.key();
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
    });
}

function aggregateMilestoneData(projectRef, milestone) {
    projectRef.once('value', function(project) {
        //increase # milestones in project    
        var milestoneCount = project.child('total_milestones').val() + 1;

        //if milestone is completed, increment completed milestones
        var completedMilestones = project.child('milestones_completed').val();
        if (milestone.child('total_tasks').val() === milestone.child('tasks_completed').val()) {
            completedMilestones++;
        }

        //increment task count
        var taskCount = project.child('total_tasks').val();
        taskCount += milestone.child('total_tasks').val();

        //increment tasks completed
        var tasksCompleted = project.child('tasks_completed').val();
        tasksCompleted += milestone.child('tasks_completed').val();

        //increment milestone hours
        var projectHours = project.child(totalHours).val();
        projectHours += milestone.child(totalHours).val();

        //increment milestone estimated hours
        var projectEstimatedHours = project.child('total_estimated_hours').val();
        projectEstimatedHours += milestone.child('total_estimated_hours').val();

        //increment removedLOC
        var projectRemovedLOC = project.child(removedLines).val();
        projectRemovedLOC += milestone.child(removedLines).val();

        //increment addedLOC
        var projectAddedLOC = project.child(addedLines).val();
        projectAddedLOC += milestone.child(addedLines).val();

        //increment totalLOC
        var projectTotalLOC = project.child(totalLines).val();
        projectTotalLOC += milestone.child(totalLines).val();

        //update milestone and hours percentages
        var milestonePercentage = calculatePercentage(completedMilestones, milestoneCount);
        var hoursPercent = calculatePercentage(projectHours, projectEstimatedHours);
        var taskPercent = calculatePercentage(tasksCompleted, taskCount);

        //update project
        // console.log('updating project');
        // console.log({
        //     'milestone_percent': milestonePercentage,
        //     'total_milestones': milestoneCount,
        //     'milestones_completed': completedMilestones,
        //     'total_hours': projectHours,
        //     'total_estimated_hours': projectEstimatedHours,
        //     'removed_lines_of_code': projectRemovedLOC,
        //     'added_lines_of_code': projectAddedLOC,
        //     'total_lines_of_code': projectTotalLOC,
        //     'hours_percent': hoursPercent,
        //     'total_tasks': taskCount,
        //     'task_percent': taskPercent
        // });
        projectRef.update({
            'milestone_percent': milestonePercentage,
            'total_milestones': milestoneCount,
            'milestones_completed': completedMilestones,
            'total_hours': projectHours,
            'total_estimated_hours': projectEstimatedHours,
            'removed_lines_of_code': projectRemovedLOC,
            'added_lines_of_code': projectAddedLOC,
            'total_lines_of_code': projectTotalLOC,
            'hours_percent': hoursPercent,
            'total_tasks': taskCount,
            'task_percent': taskPercent
        });
    });
}