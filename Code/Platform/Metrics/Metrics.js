/**
 * Metric collection script for CSSE371 Henry project
 * @author Sean Carter, Abby Mann, Andrew Davidson, Matt Rocco, Jonathan Jenkins, Adam Michael
 */

// TODO: Winter term: extract child getters/updates to string vars

var Firebase = require("firebase");

var production = "https://henry-production.firebaseio.com";
var staging = "https://henry-staging.firebaseio.com";
var test = "https://henry-test.firebaseio.com";

// used to test changes to the script so we don't damage the real DB. 
var metricsTest = "https://henry-metrics-test.firebaseio.com/";

// TODO: set this to the appropriate database, potentially with commandline override
// commandline version could also have a flag for which DB to use?
var firebaseUrl = metricsTest;

var commitsRef = new Firebase(firebaseUrl + '/commits');
var usersRef = new Firebase(firebaseUrl + '/users');
var projectsRef = new Firebase(firebaseUrl + '/projects');


//on() versus once()? This this will theoretically be constantly running, I think we want .on() so it can detect new projects and such.
projectsRef.on('value', function(projects) {

    projects.forEach(function(project) {
        var projectID = project.name();
        var projectMembers = project.child("members");

        //adds up tasks in the project backlog in order to properly calculate correct task complete percent
        var totalProjectBacklogTask = 0;
        var backlog = project.child("backlog");
        backlog.forEach(function(item) {
            totalProjectBacklogTask++;
        });


        projectMembers.ref().on("child_added", function(newMember) {
            var userName = newMember.name();
            usersRef.child(userName + "/projects/" + projectID).ref().update({
                "role": newMember.val()
            });
        });
        projectMembers.ref().on("child_removed", function(deletedMember) {
            var userName = deletedMember.name();
            usersRef.child(userName + "/projects/" + projectID).ref().update({
                "role": "removed"
            });
        });

        // The Task Listener: listens to changes in the task.  
        // When activated, checks status of task and updates if needed, then recalculates and updates the total hours, 
        // percent hours, percent task, percent milestone 
        project.child('milestones').ref().on('value', function(milestones) {
            //sets up vars for task listener
            
            var milestoneCount = 0,
                milestonesCompleted = 0,
                totalProjectTasks = 0,
                totalProjectTasksComplete = 0,
                projectHours = 0,
                totalProjectEstimatedHours = 0,
                projectAddedLOC = 0,
                projectTotalLOC = 0,
                projectRemovedLOC = 0;

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
                milestone.child('tasks').ref().on('value', function(tasks) {
                    
                    tasks.forEach(function(task) {

                        taskCount++;
                        if (task.child('is_completed').val()) {
                            completedTasks++;
                        }
                        milestoneHours += task.child('total_hours').val();
                        milestoneEstimatedHours += task.child('updated_hour_estimate').val();
                        var taskID = task.name();
                        milestoneRemovedLOC += task.child('removed_lines_of_code').val();
                        milestoneAddedLOC += task.child('added_lines_of_code').val();
                        milestoneTotalLOC += task.child('total_lines_of_code').val();

                        // update user with task info
                        var assignee = task.child("assignedTo").val();
                        if (assignee) {
                            var taskNode = usersRef.child(assignee + "/projects/" + projectID + "/milestones/" + milestoneID + "/tasks/" + taskID).ref();
                            taskNode.update({
                                "total_hours": task.child('total_hours').val(),
                                "added_lines_of_code": task.child('added_lines_of_code').val(),
                                "removed_lines_of_code": task.child('removed_lines_of_code').val(),
                                "total_lines_of_code": task.child('total_lines_of_code').val()
                            });
                        }

                        //adds up estimated hours on milestone.  If changed, update, and then update project.
                        var milestoneEstHoursSum = 0;
                        var milestoneTotalHours = 0;

                        var currentMilestoneEstHours = milestone.child('estimated_hours').val();
                        var currentMilestoneHours = milestone.child('total_hours').val();

                        milestone.child('tasks').forEach(function(taskToSum) {
                            milestoneEstHoursSum += taskToSum.child('updated_hour_estimate').val();
                            milestoneTotalHours += taskToSum.child('total_hours').val();
                        });

                        if (milestoneEstHoursSum != currentMilestoneEstHours) {
                            var hoursChanged = milestoneEstHoursSum - currentMilestoneEstHours;
                            var currentProjectEstHours = project.child('total_estimated_hours').val();
                            var updatedProjectHours = currentProjectEstHours + hoursChanged;
                            milestone.ref().update({
                                'estimated_hours': milestoneEstHoursSum
                            });
                            project.ref().update({
                                'total_estimated_hours': updatedProjectHours
                            });
                        }
                        var isDone = task.child('is_completed').val();
                        var taskStatus = task.child('status').val();
                        var isStatusDone = (taskStatus == 'Closed');
                        var updatedHourEstimate = task.child('updated_hour_estimate').val();
                        var totalHours = task.child('total_hours').val();
                        var totalLinesOfCode = task.child('total_lines_of_code').val();
                        var addedLOC = task.child('added_lines_of_code').val();
                        var removedLOC =task.child('removed_lines_of_code').val();

                        if(isDone === null){
                            isDone = false;
                        }
                        if(taskStatus === null){
                            taskStatus = "new";
                        }
                        if(isStatusDone === null){
                            isStatusDone = false;
                        }
                        if(updatedHourEstimate === null){
                            updatedHourEstimate = task.child('original_hour_estimate').val();
                        }
                        if(totalHours === null){
                          totalHours = 0;  
                        }
                        if(totalLinesOfCode === null){
                          totalLinesOfCode = 0;  
                        }
                        if(addedLOC === null){
                          addedLOC = 0;  
                        }
                        if(removedLOC === null){
                          removedLOC = 0;  
                        }
                        //Calculates percent hours completed
                        var taskHrPercent = calculatePercentage(totalHours, updatedHourEstimate);
                        //TODO: workaround
                        //check calculatePercentage. should error check for all percetange calculations
                        task.ref().update({
                            'percent_complete': taskHrPercent,
                            'is_completed': isDone,
                            'status': taskStatus,
                            'updated_hour_estimate': updatedHourEstimate,
                            'total_hours': totalHours,
                            'total_lines_of_code': totalLinesOfCode,
                            'added_lines_of_code': addedLOC,
                            'removed_lines_of_code': removedLOC
                        });

                        //if the status is closed, but it isn't marked as complete, this means that it was a new update, 
                        //and 'is_completed' needs to be updated as well
                        if (isStatusDone && !isDone) {

                            var projectTasksCompleted = project.child('tasks_completed').val();
                            projectTasksCompleted += 1;
                            var milestoneTasksCompleted = milestone.child('tasks_completed').val();
                            milestoneTasksCompleted += 1;
                            var milestoneTotalTasks = project.child('total_tasks').val();
                            var milestoneTaskPercent = calculatePercentage(milestoneTasksCompleted, milestone.child('total_tasks').val());
                            var projectTaskPercent = calculatePercentage(projectTasksCompleted, milestoneTotalTasks);

                            //moved outside of task listener
                            // milestone.ref().update({
                            //     'tasks_completed': milestoneTasksCompleted,
                            //     'task_percent': milestoneTaskPercent,
                            //    'total_hours': milestoneTotalHours
                            //});

                            var milestonesCompleted = project.child('milestones_completed').val();
                            if (milestoneTotalTasks !== 0 && milestoneTasksCompleted == milestoneTotalTasks) {
                                milestonesCompleted += 1;
                            }
                            //project.ref().update({
                            //    'tasks_completed': projectTasksCompleted,
                            //    'task_percent': projectTaskPercent,
                            //    'milestones_completed': milestonesCompleted,
                            //    'milestone_percent': projectMilestonePercent
                            //});

                            task.ref().update({
                                'is_completed': true
                            });

                            //TODO: create new method to update a project and milestone with updated task and mielstone completeness percents

                        }
                    });
                });
                var taskPercentage = calculatePercentage(completedTasks, taskCount);
                var hoursPercent = calculatePercentage(milestoneHours, milestoneEstimatedHours);
                milestone.ref().update({
                    "task_percent": taskPercentage,
                    "total_tasks": taskCount,
                    "tasks_completed": completedTasks,
                    "total_hours": milestoneHours,
                    "total_estimated_hours": milestoneEstimatedHours,
                    "removed_lines_of_code": milestoneRemovedLOC,
                    "added_lines_of_code": milestoneAddedLOC,
                    "total_lines_of_code": milestoneTotalLOC,
                    "hours_percent": hoursPercent

                });
                if (taskCount == completedTasks) {
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
                "total_milestones": milestoneCount,
                "tasks_completed": totalProjectTasksComplete,
                "total_tasks": totalProjectTasks,
                "milestones_completed": milestonesCompleted,
                "total_hours": projectHours,
                "task_percent": projectTaskPercent,
                "total_estimated_hours": totalProjectEstimatedHours,
                "removed_lines_of_code": projectRemovedLOC,
                "added_lines_of_code": projectAddedLOC,
                "total_lines_of_code" : projectTotalLOC,
                'milestone_percent': projectMilestonePercent,
                'hours_percent': projectHoursPercent
            });

        });
    });
});

// add a listener to commits for new projects
commitsRef.on('child_added', function(project) {
    var isFirstRunThrough = true;

    // add a listener to each project for new commits
    project.ref().on('child_added', function(commit) {
        if (isFirstRunThrough) return;
        calculateMetrics(project.name(), commit);
    });
    isFirstRunThrough = false;
    console.log("added listener for project " + project.name());
});


//user table listeners
usersRef.on("value", function(users) {
    users.forEach(function(user) {
        user.child("projects").forEach(function(project) {
            // console.log("test")
            var totalProjectHours = 0;
            var totalProjectAddedLOC = 0;
            var totalProjectRemovedLOC = 0;
            var totalProjectLOC = 0;

            project.child("milestones").forEach(function(milestone) {
                //  console.log(milestone.name())
                var totalMilestoneHours = 0;
                var totalAddedMilestoneLOC = 0;
                var totalRemovedMilestoneLOC = 0;
                var totalMilestoneLOC = 0;

                milestone.child("tasks").forEach(function(task) {
                    //       console.log(task + "milestoneloop")
                    totalMilestoneHours += task.child("total_hours").val();
                    totalAddedMilestoneLOC += task.child("added_lines_of_code").val();
                    totalRemovedMilestoneLOC += task.child("removed_lines_of_code").val();
                    totalMilestoneLOC += task.child("total_lines_of_code").val();
                });

                milestone.ref().update({
                    "total_hours": totalMilestoneHours,
                    "added_lines_of_code": totalAddedMilestoneLOC,
                    "removed_lines_of_code": totalRemovedMilestoneLOC,
                    "total_lines_of_code": totalMilestoneLOC
                });

                totalProjectHours += totalMilestoneHours;
                totalProjectAddedLOC += totalAddedMilestoneLOC;
                totalProjectRemovedLOC += totalRemovedMilestoneLOC;
                totalProjectLOC += totalMilestoneLOC;

            });
            project.ref().update({
                "total_hours": totalProjectHours,
                "added_lines_of_code": totalProjectAddedLOC,
                "removed_lines_of_code": totalProjectRemovedLOC,
                "total_lines_of_code" : totalProjectLOC
            });
        });
    });
});

// calculates metrics which need to be updated on a commit
function calculateMetrics(projectID, commit) {
    console.log("Calculating metrics for: " + projectID + ", " + commit.name());

    // TODO: add null checks later in code for these
    var hours = commit.child('hours').val();
    var added_lines_of_code = commit.child('added_lines_of_code').val();
    var removed_lines_of_code = commit.child('removed_lines_of_code').val();
    var total_lines_of_code = commit.child('total_lines_of_code').val();
    var milestone = commit.child('milestone').val();
    var user = commit.child('user').val();
    var task = commit.child('task').val();

    projectsRef.child(projectID + "/milestones/" + milestone + "/tasks/" + task)
        .once('value', function(taskBranch) {


            var currentHours = taskBranch.child("total_hours").val();
            var newHours = currentHours + commit.child("hours").val();

            var totalLOC = taskBranch.child("total_lines_of_code").val();
            var addedLOC = commit.child("added_lines_of_code").val();
            var removedLOC = commit.child("removed_lines_of_code").val();
            
            totalLOC += addedLOC - removedLOC;
            var totalAddedLOC = taskBranch.child("added_lines_of_code").val() + addedLOC;
            var totalRemovedLOC = taskBranch.child("removed_lines_of_code").val() + removedLOC;
            

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
        if (percentage != Infinity && !isNaN(percentage)) {
            return percentage;
        }
        else {
            return 0;
        }
    }
    //on a commit, updates the percentage of each user's percentage of contributed LOC and hours on the project and milestone associated with the commit

function updateLocAndHoursContribs(projectID, milestoneID) {
    console.log("inside of updateHoursAndLocContribs!!!\n project: " + projectID + "\n milestone: " + milestoneID + "\n");

    projectsRef.child(projectID).once('value', function(project) {
        var projAddedLOC = project.child('added_lines_of_code').val();
        var projRemovedLOC = project.child('removed_lines_of_code').val();
        var projTotalLOC = project.child('total_lines_of_code').val();
        var projHours = project.child('total_hours').val();

        var mileAddedLOC = project.child('milestones/' + milestoneID + '/added_lines_of_code').val();
        var mileRemovedLOC = project.child('milestones/' + milestoneID + '/removed_lines_of_code').val();
        var mileTotalLOC = project.child('milestones/' + milestoneID + '/total_lines_of_code').val();
        var mileHours = project.child('milestones/' + milestoneID + '/total_hours').val();


        project.child('members').forEach(function(memberSnapshot) {
            var memberRef = usersRef.child(memberSnapshot.name());
            memberRef.once('value', function(member) {
                var projContribAddedLOC = member.child('projects/' + projectID + '/added_lines_of_code').val();

                var projContribRemovedLOC = member.child('projects/' + projectID + '/removed_lines_of_code').val();
                var projContribTotalLOC = member.child('projects/' + projectID + '/total_lines_of_code').val();
                var projContribHours = member.child('projects/' + projectID + '/total_hours').val();

                // update project stuff
                var projAddedLOCPercent = calculatePercentage(projContribAddedLOC, projAddedLOC);
                var projRemovedLOCPercent = calculatePercentage(projContribRemovedLOC, projRemovedLOC);
                var projTotalLOCPercent = calculatePercentage(projContribTotalLOC, projTotalLOC);
                var projHoursPercent = calculatePercentage(projContribHours, projHours);

                console.log("added loc percent : " + projAddedLOCPercent + "\n projContribAddedLOC: " + projContribAddedLOC + "\n projTotAddedLOC: " + projAddedLOC + "\n");

                member.child('projects/' + projectID).ref().update({
                    'hours_percent': projHoursPercent,
                    'removed_loc_percent': projRemovedLOCPercent,
                    'added_loc_percent': projAddedLOCPercent,
                    'loc_percent': projTotalLOCPercent

                });
                if (member.hasChild('projects/' + projectID + '/milestones/' + milestoneID)) {
                    var milestone = member.child('projects/' + projectID + '/milestones/' + milestoneID);
                    // if (milestone == milestoneID) {

                    var mileContribAddedLOC = milestone.child('added_lines_of_code').val();
                    var mileContribRemovedLOC = milestone.child('removed_lines_of_code').val();
                    var mileContribHours = milestone.child('total_hours').val();
                    var mileContribTotalLOC = milestone.child('total_lines_of_code').val();

                    // update milestone stuff 
                    var mileAddPercent = calculatePercentage(mileContribAddedLOC, mileAddedLOC);
                    var mileRemPercent = calculatePercentage(mileContribRemovedLOC, mileRemovedLOC);
                    var mileLOCPercent = calculatePercentage(mileContribTotalLOC, mileTotalLOC);
                    var mileHourPercent = calculatePercentage(mileContribHours, mileHours);

                    member.child('projects/' + projectID + '/milestones/' + milestoneID).ref().update({
                        "added_loc_percent": mileAddPercent,
                        "removed_loc_percent": mileRemPercent,
                        "loc_percent": mileLOCPercent,
                        "hours_percent": mileHourPercent
                    });
                }
            });
        });
    });
}
