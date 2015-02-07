/**
 * Metric collection script for CSSE371/CSSE374/CSSE375 Henry project
 * Authors: Sean Carter, Abby Mann, Andrew Davidson, Matt Rocco, Jonathan Jenkins, Adam Michael
 * Last Modified: 24 Jan 2015, 12:21AM
 */


//TODO: functionality for removed tasks?

var Firebase = require('firebase');

var production = 'https://henry-production.firebaseio.com';
var staging = 'https://henry-staging.firebaseio.com';
var test = 'https://henry-test.firebaseio.com';

// used to test changes to the script so we don't damage the real DB. 
var metricsTest = 'https://henry-metrics-test.firebaseio.com/';

var metricsTestToken = 'swPpsOgpNn7isrfxeoWNNV7yxLy85j94JO7p9lDf';
var testToken = 'FDrYDBNvRCgq0kGonjmsPl0gUwXvxcqUdgaCQ1FI';
// TODO: set this to the appropriate database, potentially with commandline override
// commandline version could also have a flag for which DB to use?
var firebaseUrl = test;

var FirebaseTokenGenerator = require('firebase-token-generator');
//this token is for metricsTest
var tokenVal = (firebaseUrl === test) ? testToken : metricsTestToken;
var tokenGenerator = new FirebaseTokenGenerator(tokenVal);
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

// TODO: update burndown on commit, push commit for changed updated_hour_estimate (?)


// add a listener to commits for new projects
commitsRef.on('child_added', function(project) {

    projectsRef.child(project.key()).child('milestones').once('value', function(milestones) {
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
            // milestone.ref().update({
            //     'burndown_data': {
            //         'placeholder': 'placeholder'
            //     }
            // });
        });
    });

    // if we have at least one commit, remove the placeholder.
    if (project.numChildren() > 1) {
        project.ref().update({
            'description': null,
            'placeholder': null
        });
    }
    // add a listener to each project for new commits
    project.ref().on('child_added', function(commit) {
        if (commit.val() === 'placeholder') return;
        calculateMetrics(projectsRef.child(project.key()), commit);
    });
    console.log('added commit listener for project ' + project.key());
});

// on 'child added' returns each child one at a time the first run, then only the one child that is added
projectsRef.on('child_added', function(project) {
    // console.log('Messing with project ' + project.key() + ' at time ' + new Date().toLocaleTimeString());
    listenToProject(project);
    console.log('Added listener for project ' + project.key());
});

// usersRef.on('child_changed', function(user) {
//     user.child('projects').forEach(function(project) {
//         aggregateUserProjectData(project);
//     });
// });

usersRef.on('child_added', function(user) {
    user.ref().child('projects').on('child_added', function(userProject) {
        userProject.child('milestones').ref().on('child_added', function(milestone) {
            milestone.child('tasks').ref().on('child_added', function(task) {
                task.ref().on('value', function(taskVal) {
                    aggregateUserProjectData(userProject);
                    var project = projectsRef.child(userProject.key());
                    updateLocAndHoursContribs(project);

                });
            });
        });
    });
});

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

function addMilestoneListeners(milestoneRef) {
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

    var isStartup = true;
    milestoneRef.child('tasks').on('child_added', function(newTask) {

        // do new task stuff
        console.log('a child is born'); // blame Abby
        //set defaults
        setDefaults(newTask);

        //updated_hour_estimate listener
        newTask.child('updated_hour_estimate').ref().on('value', function(updatedHrEst) {
            if (isStartup) return;
            updatedHourEstChange(updatedHrEst, milestoneRef, newTask, isStartup);
        });

    });

    milestoneRef.child('tasks').on('child_changed', function(changedTask) {
        // do changed task stuff
        console.log('changing task');
        // TODO: this is called twice on change to updated_hour_estimate
        taskChanged(milestoneRef, changedTask.ref());
    });
    isStartup = false;


}

function updatedHourEstChange(updatedHrEst, milestoneRef, task, isStartup) {
    var projectID = milestoneRef.parent().parent().key();

    // commit maker for change to updated_hour_estimate
    var newCommit = {
        'added_lines_of_code': 0,
        'hours': 0,
        'message': 'Change to updated_hour_estimate from Metrics Script',
        'milestone': milestoneRef.key(),
        'project': projectID,
        'removed_lines_of_code': 0,
        'status': task.child('status').val(),
        'task': task.key(),
        'timestamp': Firebase.ServerValue.TIMESTAMP,
        'user': task.child('assignedTo').val(),
        'updated_hour_estimate': updatedHrEst.val()
    };
    var childExists = false;
    commitsRef.child(projectID).once('value', function(commits) {
        commits.forEach(function(commit) {
            if (commit.child('message') === newCommit['message'] && commit.child('task') === newCommit['task'] && commit.child('updated_hour_estimate') !== null && commit.child('updated_hour_estimate') === newCommit['updated_hour_estimate']) {
                childExists = true;
            }
        });
    });

    // if there's a commit already there with the same estimate, don't push the same commit again.
    // TODO: test this. Also, it'll definitely break if we do a += 1, -= 1 thing.
    // TODO: put current LoC in the commit, and if commit has updated_hour_estimate,
    //          then ignore those fields and only use them for comparison here?
    if (!childExists) {
        commitsRef.child(projectID).push(newCommit);
    }

}

// makes appropriate updates whenever a task is changed
function taskChanged(milestoneRef, taskRef) {
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

    taskRef.once('value', function(taskBranch) {
        // update user with task info
        var assignee = taskBranch.child('assignedTo').val();
        if (assignee !== null) {
            var taskNode = usersRef.child(assignee + '/projects/' + projectRef.key() + '/milestones/' + milestoneRef.key() + '/tasks/' + taskRef.key()).ref();
            var bountyPoints = 0;
            taskBranch.child('bounties').forEach(function(bounty) {
                if ((bounty.child('hour_limit').val() === 'None') || (bounty.child('hour_limit').val() >= taskBranch.child(totalHours).val())) {
                    if ((bounty.child('line_limit').val() === 'None') || (bounty.child('line_limit').val() >= taskBranch.child(totalHours).val()))
                        // due_date_stamp = blah
                        // commit_stamp = blah
                    var date = bounty.child('due_date').val();
                    if (date === 'No Due Date' || Date.parse(date) >= new Date().getTime()) {
                        bountyPoints += bounty.child('points').val();
                    }
                }
            });

            if (taskBranch.child('status').val() === 'Closed') {
                taskNode.update({
                    'total_hours': taskBranch.child(totalHours).val(),
                    'added_lines_of_code': taskBranch.child(addedLines).val(),
                    'removed_lines_of_code': taskBranch.child(removedLines).val(),
                    'total_lines_of_code': taskBranch.child(totalLines).val(),
                    'points': bountyPoints
                });
            }
            else {
                taskNode.update({
                    'total_hours': taskBranch.child(totalHours).val(),
                    'added_lines_of_code': taskBranch.child(addedLines).val(),
                    'removed_lines_of_code': taskBranch.child(removedLines).val(),
                    'total_lines_of_code': taskBranch.child(totalLines).val(),
                    'points': 0
                });
            }
        }
    });

    // aggregate milestone data
    aggregateMilestoneData(projectRef);
    // //TODO: add for each
}

// aggregates all the data for the project and milestones over the tasks in the USER'S branch
function aggregateUserProjectData(project) {

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
    userRef.update({
        'total_points': userPoints
    });

}

// calculates metrics which need to be updated on a commit
function calculateMetrics(projectRef, commit) {
    // console.log('Calculating metrics for: ' + projectRef.key() + ', ' + commit.key());
    // console.log('inside calculateMetrics');
    // TODO: add null checks later in code for these
    var milestone = commit.child('milestone').val();
    var task = commit.child('task').val();

    // console.log('milestone: ' + milestone + ', task: ' + task);
    try {
        if (projectRef.child(task) === null) {
            console.log('task ' + task + ' not found for project ' + projectRef.key());
            return;
        }
    }
    catch (error) {
        console.log('Commit ' + commit.key() + ' for project ' + projectRef.key() + ' has an empty task field.')
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

    var taskRef = projectRef.child('milestones/' + milestone + '/tasks/' + task);

    taskRef.once('value', function(taskBranch) {
        var currentHours = taskBranch.child(totalHours).val();
        var newHours = currentHours + commit.child('hours').val();

        var totalLOC = taskBranch.child(totalLines).val();
        var addedLOC = commit.child(addedLines).val();
        var removedLOC = commit.child(removedLines).val();

        var newTotalLOC = totalLOC + addedLOC - removedLOC;
        var totalAddedLOC = taskBranch.child(addedLines).val() + addedLOC;
        var totalRemovedLOC = taskBranch.child(removedLines).val() + removedLOC;

        var taskTotalHours = taskBranch.child('total_hours').val();
        var updatedHourEstimate = taskBranch.child('updated_hour_estimate').val();
        var taskHrPercent = calculatePercentage(taskTotalHours, updatedHourEstimate);
        var status = commit.child('status').val();

        if (currentHours !== newHours || addedLOC !== totalAddedLOC || removedLOC !== totalRemovedLOC) {

            taskBranch.ref().update({
                'total_hours': newHours,
                'total_lines_of_code': newTotalLOC,
                'added_lines_of_code': totalAddedLOC,
                'removed_lines_of_code': totalRemovedLOC,
                'percent_complete': taskHrPercent,
                'status': status
            });
        }
    });

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

// calculates percentage current/total
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

//once milestone data is aggregated, updates the percentage of each user's percentage of contributed LOC and hours on the project and milestone levels
function updateLocAndHoursContribs(projectRef) {
    projectRef.once('value', function(project) {
        // console.log(project.val());
        var projAddedLOC = project.child(addedLines).val();
        var projRemovedLOC = project.child(removedLines).val();
        var projTotalLOC = project.child(totalLines).val();
        var projHours = project.child(totalHours).val();

        project.child('milestones').forEach(function(milestone) {
            var mileAddedLOC = milestone.child('added_lines_of_code').val();
            var mileRemovedLOC = milestone.child('removed_lines_of_code').val();
            var mileTotalLOC = milestone.child('total_lines_of_code').val();
            var mileHours = milestone.child('total_hours').val();

            project.child('members').forEach(function(memberSnapshot) {
                var memberRef = usersRef.child(memberSnapshot.key());
                memberRef.once('value', function(member) {
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

// sets defaults for a new task
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

// updates a miletone's data given a task (this should only be called in a loop over all its tasks)
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

// updates a project's data by looping over all its milestones
function aggregateMilestoneData(projectRef) {
    projectRef.child('milestones').once('value', function(milestones) {
        milestones.forEach(function(milestone) {
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
    updateLocAndHoursContribs(projectRef);
}
