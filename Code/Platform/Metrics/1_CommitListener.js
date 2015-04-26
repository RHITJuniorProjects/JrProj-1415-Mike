/*
 * Commit Listener script for CSSE371/CSSE374/CSSE375 Henry project.
 * Handles read/write operations directly relating to commits.
 *
 * Authors: Sean Carter, Abby Mann, Andrew Davidson, Matt Rocco, Jonathan Jenkins, Adam Michael
 * Last Modified: 2 April 2015, 6:36pm
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
}
else if (process.argv.length === 4) {
    tokenVal = process.argv[3];
    firebaseUrl = 'https://' + process.argv[2] + '.firebaseio.com';
}


console.log("Connecting to " + firebaseUrl);

// Firebase references for root and top-level trees
var fbRef = new Firebase(firebaseUrl);
var commitsRef = new Firebase(firebaseUrl + '/commits');
var usersRef = new Firebase(firebaseUrl + '/users');
var projectsRef = new Firebase(firebaseUrl + '/projects');

// authorization stuff
var FirebaseTokenGenerator = require('firebase-token-generator');

// TODO: add more as needed, or do commandline override for auth token
var metricsTestToken = 'swPpsOgpNn7isrfxeoWNNV7yxLy85j94JO7p9lDf';
var testToken = 'FDrYDBNvRCgq0kGonjmsPl0gUwXvxcqUdgaCQ1FI';

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


/* -------------------------COMMIT LISTENER STUFF------------------------- */

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

// zero out project data in preparation for startup aggregation
projectsRef.once('value', function(projects) {
    projects.forEach(function(project) {
        project.child('milestones').ref().once('value', function(milestones) {
            milestones.forEach(function(milestone) {
                milestone.child('tasks').forEach(function(task) {
                    task.ref().update({
                        'added_lines_of_code': 0,
                        'removed_lines_of_code': 0,
                        'total_hours': 0,
                        'total_lines_of_code': 0,
                        'percent_complete': 0
                    });
                });
            });
        });
    });
});

// zero out user data in preparation for startup aggregation
usersRef.once('value', function(users) {
    users.forEach(function(user) {
        user.child('projects').forEach(function(project) {
            project.child('milestones').forEach(function(milestone) {
                milestone.child('tasks').forEach(function(task) {
                    task.ref().update({
                        'added_lines_of_code': 0,
                        'removed_lines_of_code': 0,
                        'total_hours': 0,
                        'total_lines_of_code': 0,
                    });
                });
            });
        });
    });
});

// add a listener to commits for new projects
commitsRef.on('child_added', function(project) {
    // add a listener to each project for new commits
    project.ref().on('child_added', function(commit) {
        // if we have at least one commit, remove the placeholder.
        if (project.numChildren() > 1) {
            console.log("removing commit placeholder");
            project.ref().update({
                'description': null,
                'placeholder': null
            });
        }
        if (commit.val() === 'placeholder') return;
        calculateMetrics(projectsRef.child(project.key()), commit);
    });
    // console.log('added commit listener for project ' + project.key());
});

/* 
 * calculates metrics that need to be updated on a commit
 */
function calculateMetrics(projectRef, commit) {
    console.log('Calculating metrics for: ' + projectRef.key() + ', ' + commit.key() + ', at commit ' + commit.key());
    var milestone = commit.child('milestone').val();
    var task = commit.child('task').val();

    try {
        if (projectRef.child(task) === null) {
            console.log('task ' + task + ' not found for project ' + projectRef.key());
            return;
        }
    }
    catch (error) {
        console.log('Commit ' + commit.key() + ' for project ' + projectRef.key() + ' has an empty task field.');
        return;
    }

    try {
        if (projectRef.child(milestone) === null) {
            console.log('milestone ' + milestone + ' not found for project ' + projectRef.key());
            return;
        }
    }
    catch (error) {
        console.log('Commit ' + commit.key() + ' for project ' + projectRef.key() + ' has an empty milestone field.');
        projectRef.child('milestones').once('value', function(milestones) {
            milestones.forEach(function(milestoneBranch) {
                if (milestoneBranch.child('tasks').hasChild(task)) {
                    milestone = milestoneBranch.key();
                    console.log('Found milestone ' + milestone + ' for task ' + task + ' in project ' + projectRef.key());
                }
            });
        });
    }

    if (milestone === null) {
        console.log('Unable to find milestone for commit ' + commit.key());
        return;

    }
    var addedLOC = commit.child(addedLines).val();
    var removedLOC = commit.child(removedLines).val();

    var taskRef = projectRef.child('milestones/' + milestone + '/tasks/' + task);

    taskRef.once('value', function(taskBranch) {
        var currentHours = taskBranch.child(totalHours).val();
        var newHours = currentHours + commit.child('hours').val();

        var totalLOC = taskBranch.child(totalLines).val();


        var newTotalLOC = totalLOC + addedLOC - removedLOC;
        var totalAddedLOC = taskBranch.child(addedLines).val() + addedLOC;
        var totalRemovedLOC = taskBranch.child(removedLines).val() + removedLOC;

        var updatedHourEstimate = taskBranch.child('updated_hour_estimate').val();
        if (commit.hasChild('updated_hour_estimate')) {
            updatedHourEstimate = commit.child('updated_hour_estimate').val();
        }

        var taskHrPercent = calculatePercentage(newHours, updatedHourEstimate);
        var status = commit.child('status').val();

        if (currentHours !== newHours ||
            addedLOC !== totalAddedLOC ||
            removedLOC !== totalRemovedLOC ||
            status !== taskBranch.child('status').val() ||
            updatedHourEstimate !== taskBranch.child('updated_hour_estimate').val()) {

            taskBranch.ref().update({
                'total_hours': newHours,
                'total_lines_of_code': newTotalLOC,
                'added_lines_of_code': totalAddedLOC,
                'removed_lines_of_code': totalRemovedLOC,
                'percent_complete': taskHrPercent,
                'status': status,
                'updated_hour_estimate': updatedHourEstimate
            });
        }
    });
    // update user with task info
    var projectID = commit.child('project').val();
    var milestoneID = commit.child('milestone').val();
    var taskID = taskRef.key();
    var assignee = commit.child('user').val();
    var partner = commit.child('pair_programmed').val();

    var commitHours = commit.child('hours').val();

    // TODO: log update?
    if (assignee !== null) {
        var taskNode = usersRef.child(assignee + '/projects/' + projectID + '/milestones/' + milestoneID + '/tasks/' + taskID);
        taskNode.once('value', function(taskSnap) {

            if (taskSnap.val() === null) {
                console.log('task does not exist for user');
                taskNode.update({
                    'total_hours': commitHours,
                    'added_lines_of_code': addedLOC,
                    'removed_lines_of_code': removedLOC,
                    'total_lines_of_code': addedLOC - removedLOC
                });
            }
            else {

                var userHours = taskSnap.child('hours').val() + commitHours;
                var userTotalLOC;

                if (partner === null || !partner) {
                    userTotalLOC = taskSnap.child(totalLines).val() + addedLOC - removedLOC;
                    taskNode.update({
                        'total_hours': userHours,
                        'added_lines_of_code': taskSnap.child(addedLines).val() + addedLOC,
                        'removed_lines_of_code': taskSnap.child(removedLines).val() + removedLOC,
                        'total_lines_of_code': userTotalLOC
                    });
                }
                // TODO: here and below: ensure partner is on project? (CLI may handle)
                else if (partner !== null) {
                    console.log("Pair programmed commit: User: " + assignee + ", partner: " + partner);
                    console.log("project, milestone, task: " + projectID + ", " + milestoneID + ", " + taskID)
                    userTotalLOC = taskSnap.child(totalLines).val() + Math.ceil(addedLOC / 2) - Math.ceil(removedLOC / 2);
                    taskNode.update({
                        'total_hours': userHours,
                        'added_lines_of_code': Math.ceil((taskSnap.child(addedLines).val() + addedLOC) / 2),
                        'removed_lines_of_code': Math.ceil((taskSnap.child(removedLines).val() + removedLOC) / 2),
                        'total_lines_of_code': userTotalLOC
                    });
                }
            }
        });

        if (partner !== null && partner !== false) {
            var partnerTaskNode = usersRef.child(partner + '/projects/' + projectID + '/milestones/' + milestoneID + '/tasks/' + taskID);
            partnerTaskNode.once('value', function(partnerTaskSnap) {
                if (partnerTaskSnap.val() === null) {
                    console.log('task does not exist for partner')
                    partnerTaskNode.update({
                        'total_hours': commitHours,
                        'added_lines_of_code': addedLOC,
                        'removed_lines_of_code': removedLOC,
                        'total_lines_of_code': addedLOC - removedLOC
                    });
                }
                else {
                    var partnerTotalLOC = partnerTaskSnap.child(totalLines).val() + Math.floor(addedLOC / 2) - Math.floor(removedLOC / 2);
                    var partnerHours = partnerTaskSnap.child(totalHours).val() + commit.child('hours').val();

                    partnerTaskNode.update({
                        'total_hours': partnerHours,
                        'added_lines_of_code': Math.ceil(partnerTaskSnap.child(addedLines).val() + addedLOC / 2),
                        'removed_lines_of_code': Math.ceil(partnerTaskSnap.child(removedLines).val() + removedLOC / 2),
                        'total_lines_of_code': partnerTotalLOC
                    });
                }
            });
        }
    }

    var milestoneRef = taskRef.parent().parent();

    // add burndown data point with new data
    milestoneRef.once('value', function(milestoneSnap) {

        var milestoneEstimatedHours = milestoneSnap.child('total_estimated_hours').val();
        var milestoneHours = milestoneSnap.child('total_hours').val();
        var milestoneTasksComplete = milestoneSnap.child('tasks_completed').val();
        var milestoneTasks = milestoneSnap.child('total_tasks').val();

        var dataPoint = {};
        var timestamp = commit.child('timestamp').val();
        dataPoint[timestamp] = {
            'estimated_hours_remaining': milestoneEstimatedHours - milestoneHours,
            'hours_completed': milestoneHours,
            'tasks_completed': milestoneTasksComplete,
            'tasks_remaining': milestoneTasks - milestoneTasksComplete
        };

        if (!milestoneSnap.hasChild('burndown_data')) {
            milestoneRef.update({
                'burndown_data': {
                    'placeholder': 'placeholder'
                }
            });
        }
        else if (milestoneSnap.child('burndown_data').numChildren() > 1) {
            milestoneRef.child('burndown_data').update({
                'placeholder': null
            });
        }
        var dataPointExists = false;

        milestoneRef.child('burndown_data').once('value', function(burndownPoints) {
            burndownPoints.forEach(function(burndownPoint) {
                if (parseInt(burndownPoint.key(), 10) === timestamp) {
                    dataPointExists = true;
                }
            });
        });
        if (!dataPointExists) {
            milestoneRef.child('burndown_data').update(dataPoint);
        }
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




/* -------------------------PROJECT LISTENER STUFF------------------------- */

// on 'child added' returns each child one at a time the first run, then only the one child that is added
projectsRef.on('child_added', function(project) {
    // console.log('Messing with project ' + project.key() + ' at time ' + new Date().toLocaleTimeString());
    listenToProject(project);
    console.log('Added listener for project ' + project.key());
});

/*
 * Listens for new / deleted members, and adds listeners for milestones
 */
function listenToProject(project) {
    var projectID = project.key();
    var projectMembers = project.child('members');

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
    project.child('milestones').ref().on('child_added', function(milestone) {
        addMilestoneListeners(milestone.ref());
    });
}

/* 
 * Adds various listeners at the milestone level:
 *     sets defaults for new tasks
 *     listens for changes to existing tasks
 */
function addMilestoneListeners(milestoneRef) {
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

    var isStartup = true;
    milestoneRef.child('tasks').on('child_added', function(newTask) {
        // do new task stuff
        setDefaults(newTask);
    });

    milestoneRef.child('tasks').on('child_changed', function(changedTask) {
        // do changed task stuff
        taskChanged(milestoneRef, changedTask.ref());
    });
    isStartup = false;
}

/* 
 * makes appropriate updates whenever a task is changed
 */
function taskChanged(milestoneRef, taskRef) {
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
    // update the project (by re-initializing and aggregating data over all milestones)
    var projectRef = milestoneRef.parent().parent();

    milestoneRef.child('tasks').once('value', function(tasks) {
        tasks.forEach(function(task) {
            aggregateTaskData(milestoneRef, task);
        });
    });


    projectRef.once('value', function(project) {
        //initialize num tasks to backlog tasks
        var backlogTasks = project.child('backlog').numChildren();

        projectRef.update({
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
            'task_percent': 0,
            'tasks_completed': 0
        });

    });

    // bounty stuff
    taskRef.once('value', function(taskBranch) {
        // update user with task info
        var assignee = taskBranch.child('assignedTo').val();
        if (assignee !== null) {
            var taskNode = usersRef.child(assignee + '/projects/' + projectRef.key() + '/milestones/' + milestoneRef.key() + '/tasks/' + taskRef.key()).ref();
            var bountyPoints = 0;
            // check for null 'bounties' field in the task
            if (taskBranch.hasChild('bounties')) {
                taskBranch.child('bounties').forEach(function(bounty) {
                    if ((bounty.child('hour_limit').val() === 'None') || (bounty.child('hour_limit').val() >= taskBranch.child(totalHours).val())) {
                        if ((bounty.child('line_limit').val() === 'None') || (bounty.child('line_limit').val() >= taskBranch.child(totalHours).val()))
                            var date = bounty.child('due_date').val();
                        if (date === 'No Due Date' || +Date.parse(date) >= +(new Date().getTime())) {
                            bountyPoints += bounty.child('points').val();
                        }
                    }
                });

                if (taskBranch.child('status').val() === 'Closed') {
                    taskNode.update({
                        // 'total_hours': taskBranch.child(totalHours).val(),
                        // 'added_lines_of_code': taskBranch.child(addedLines).val(),
                        // 'removed_lines_of_code': taskBranch.child(removedLines).val(),
                        // 'total_lines_of_code': taskBranch.child(totalLines).val(),
                        'points': bountyPoints
                    });
                }
                else {
                    taskNode.update({
                        // 'total_hours': taskBranch.child(totalHours).val(),
                        // 'added_lines_of_code': taskBranch.child(addedLines).val(),
                        // 'removed_lines_of_code': taskBranch.child(removedLines).val(),
                        // 'total_lines_of_code': taskBranch.child(totalLines).val(),
                        'points': 0
                    });
                }
            }
            else {
                taskNode.update({
                    // 'total_hours': taskBranch.child(totalHours).val(),
                    // 'added_lines_of_code': taskBranch.child(addedLines).val(),
                    // 'removed_lines_of_code': taskBranch.child(removedLines).val(),
                    // 'total_lines_of_code': taskBranch.child(totalLines).val(),
                    'points': 0
                });
            }
        }
    });

    // aggregate milestone data
    aggregateMilestoneData(projectRef);
    // //TODO: add for each
}

/*
 * sets defaults for a new task
 */
function setDefaults(task) {
    // var isCompleted = task.child('is_completed').val();
    var taskStatus = task.child('status').val();
    var isClosed = (taskStatus === 'Closed');
    var updatedHourEstimate = task.child(updatedHourEst).val();
    var taskTotalHours = task.child(totalHours).val();
    var totalLOC = task.child(totalLines).val();
    var addedLOC = task.child(addedLines).val();
    var removedLOC = task.child(removedLines).val();

    // if (isCompleted === null) {
    //     isCompleted = false;
    // }
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
    if (totalLOC === null) {
        totalLOC = 0;
    }
    if (addedLOC === null) {
        addedLOC = 0;
    }
    if (removedLOC === null) {
        removedLOC = 0;
    }

    task.ref().update({
        'status': taskStatus,
        'updated_hour_estimate': updatedHourEstimate,
        'total_hours': taskTotalHours,
        'total_lines_of_code': totalLOC,
        'added_lines_of_code': addedLOC,
        'removed_lines_of_code': removedLOC
    })

}

/* 
 * updates a milestone's data given a task (this should only be called in a loop over all its tasks)
 */
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

        // TODO: try to update user inside commit
        // update user with task info
        // var projectID = milestoneRef.parent().parent().key();
        // var milestoneID = milestone.key();
        // var taskID = task.key();
        // var assignee = task.child('assignedTo').val();

        // if (assignee !== null) {
        //     var taskNode = usersRef.child(assignee + '/projects/' + projectID + '/milestones/' + milestoneID + '/tasks/' + taskID).ref();
        //     taskNode.update({
        //         'total_hours': task.child(totalHours).val(),
        //         'added_lines_of_code': task.child(addedLines).val(),
        //         'removed_lines_of_code': task.child(removedLines).val(),
        //         'total_lines_of_code': task.child(totalLines).val()
        //     });
        // }
    });
}

/*
 * updates a project's data by looping over all its milestones
 */
function aggregateMilestoneData(projectRef) {
    projectRef.child('milestones').once('value', function(milestones) {
        milestones.forEach(function(milestone) {
            projectRef.once('value', function(project) {
                // console.log('inside milestone aggregator');
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
                    'task_percent': taskPercent,
                    'tasks_completed': tasksCompleted
                });
            });
        });
    });
    // updateLocAndHoursContribs(projectRef);
}