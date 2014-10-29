/**
 * Metric collection script for CSSE371 Henry project
 * @author Sean Carter, Abby Mann, Andrew Davidson
 */

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
    console.log('inside once');
    projects.forEach(function(project) {
        var projectID= project.name()
        project.child('milestones').ref().on('value', function(milestones) { 
            console.log('inside forEach');
            milestones.forEach(function(milestone) {
                var milestoneID = milestone.name()
                var milestoneHours = 0;
                milestone.child('tasks').ref().on('value', function(tasks) {
                    tasks.forEach(function(task) {
                        var taskID = task.name();
                        var updated_estimate = task.child('updated_hour_estimate').val();
                        console.log("inside listener");
                        var assignee = task.child("assignedTo").val();
                        if(assignee){
                        var taskNode = usersRef.child(assignee + "/projects/" + projectID + "/milestones/" + milestoneID + "/tasks/" + taskID ).ref().update(
                            ({"total_hours": task.child('total_hours').val(),
                            "total_lines_of_code": task.child('total_lines_of_code').val()}));
                            }// update user with task info
                        //TODO: add up estimated hours on milestone.  If changed, update, and then update project.  Simple.
                        var milestoneHoursSum = 0;
                        var milestoneTotalHours = 0
                        var currentMilestoneHours = milestone.child('estimated_hours').val();
                        milestone.child('tasks').forEach(function(taskToSum) {
                            milestoneHoursSum += taskToSum.child('updated_hour_estimate').val();
                            milestoneTotalHours += taskToSum.child('total_hours').val();
                        });
                        console.log("First???");
                        if (milestoneHoursSum != currentMilestoneHours) {
                            var hoursChanged = milestoneHoursSum - currentMilestoneHours;
                            var currentProjectHours = project.child('total_estimated_hours').val();
                            var updatedProjectHours = currentProjectHours + hoursChanged;
                            milestone.ref().update({
                                'estimated_hours': milestoneHoursSum
                            });
                            project.ref().update({
                                'total_estimated_hours': updatedProjectHours
                            });
                            //TODO discuss creating new methods to share with cmmit update ro do again here???
                        }

                        var isDone = task.child('is_completed').val();
                        var taskStatus = task.child('status').val();
                        console.log("Status??? " + taskStatus);
                        var isStatusDone = (taskStatus == 'Closed');
                        //console.log("isStatusDone??? " + isStatusDone);
                        console.log("isDone??? " + isDone);
                        //if the status is closed, but it isn't marked as complete, this means that it was a new update, 
                        //and 'is_completed' needs to be updated as well
                        if (isStatusDone && !isDone) {

                            var projectTasksCompleted = project.child('tasks_completed').val();
                            projectTasksCompleted += 1;
                            var milestoneTasksCompleted = milestone.child('tasks_completed').val();
                            milestoneTasksCompleted += 1;
                            var milestoneTotalTasks = project.child('total_tasks').val();
                            var milestoneTaskPercent = (milestoneTasksCompleted * 100) / milestone.child('total_tasks').val();
                            var projectTaskPercent = (projectTasksCompleted * 100) / milestoneTotalTasks;


                            milestone.ref().update({
                                'tasks_completed': milestoneTasksCompleted,
                                'task_percent': milestoneTaskPercent,
                                'total_hours': milestoneTotalHours
                            });
                            var milestonesCompleted = project.child('milestones_completed').val();
                            var projectMilestonePercent = 0;
                            if (milestoneTasksCompleted == milestoneTotalTasks) {
                                milestonesCompleted += 1;
                                projectMilestonePercent = (milestonesCompleted * 100) / project.child('total_milestones').val();
                            }
                            console.log("let's update project stuff");
                            //var percentMilestoneComplete (Going to be the percent calculation)
                            project.ref().update({
                                'tasks_completed': projectTasksCompleted,
                                'task_percent': projectTaskPercent,
                                'milestones_completed': milestonesCompleted,
                                'milestone_percent': projectMilestonePercent
                            });

                            console.log("let's update is_completed!");
                            task.ref().update({
                                'is_completed': true
                            });


                            //TODO: create new method to update a project and milestone with updated task and mielstone completeness percents

                        }
                    });
                });
            });
        });
    });
});

//user table listeners
usersRef.on("value", function (users){
    users.forEach(function(user){
        user.child("projects").forEach(function (project){
            // console.log("test")
             var totalProjectHours = 0
             var totalProjectLOC = 0
             project.child("milestones").forEach(function(milestone){
              //  console.log(milestone.name())
                var totalMilestoneHours = 0
                var totalMilestoneLOC = 0
                milestone.child("tasks").forEach(function(task){
                  //       console.log(task + "milestoneloop")
                    totalMilestoneHours += task.child("total_hours").val()  
                    totalMilestoneLOC += task.child("total_lines_of_code").val()
                     })
                    milestone.ref().update({
                     "total_hours":totalMilestoneHours,
                     "total_lines_of_code":totalMilestoneLOC
                     })
              totalProjectHours += totalMilestoneHours;
              totalProjectLOC += totalMilestoneLOC;
            })
            project.ref().update({
                 "total_hours":totalProjectHours,
                 "total_lines_of_code":totalProjectLOC
            })
            })
        })
})


function calculateMetrics(projectName, commit) {
    console.log("Calculating metrics for: " + projectName + ", " + commit.name());

    // TODO: add null checks later in code for these
    var hours = commit.child('hours').val();
    var lines_of_code = commit.child('lines_of_code').val();
    var milestone = commit.child('milestone').val();
    var user = commit.child('user').val();
    var task = commit.child('task').val();

    // calculates all metrics data for the user
    usersRef.child(user)
        .child('projects')
        .child(projectName)
        .once('value', function(userProject) {

            //retrieves the user's current milestone metrics
            var userMilestone = userProject.child('milestones').child(milestone);
            var userMilestoneHours = userMilestone.child('total_hours').val();
            var userMilestoneLOC = userMilestone.child('total_lines_of_code').val();

            //calculates new values
            userMilestoneHours += hours;
            userMilestoneLOC += lines_of_code;

            // updates the user's milestone data
            userMilestone.ref().update({
                'total_hours': userMilestoneHours,
                'total_lines_of_code': userMilestoneLOC
            });

            //retrieves the user's current milestone metrics
            var userProjectHours = userProject.child('total_hours').val();
            var userProjectLOC = userProject.child('total_lines_of_code').val();

            //calculates new values
            userProjectHours += hours;
            userProjectLOC += lines_of_code;

            // updates the user's project data
            userProject.ref().update({
                'total_hours': userProjectHours,
                'total_lines_of_code': userProjectLOC
            });

            //retrieves the user's current metrics for the task
            var userTask = userMilestone.child(task);
            var userTaskHours = userTask.child('total_hours').val();
            var userTaskLOC = userTask.child('total_lines_of_code').val();

            //calculates new values
            userTaskHours += hours;
            userTaskLOC += lines_of_code;

            // updates the user's task data
            userTask.ref().update({
                'total_hours': userTaskHours,
                'total_lines_of_code': userTaskLOC
            });

        });

    // // metrics data for projects, milestones, and tasks
    // projectsRef.child(projectName)
    //     .once('value', function(projectBranch) {

    // var projectHours = projectBranch.child('total_hours').val();
    // var projectLOC = projectBranch.child('total_lines_of_code').val();

    // var projectHoursProjected = projectBranch.child('total_estimated_hours').val();

    // projectHours += hours;
    // projectLOC += lines_of_code;
    // var projectHoursPercent = (projectHours * 100) / projectHoursProjected;


    // //update project
    // projectBranch.ref().update({
    //     'total_hours': projectHours,
    //     'total_lines_of_code': projectLOC,
    //     'hours_percent': projectHoursPercent
    // });



    // var milestoneBranch = projectBranch.child('milestones').child(milestone);

    // var milestoneHours = milestoneBranch.child('total_hours').val();
    // var milestoneLOC = milestoneBranch.child('total_lines_of_code').val();
    // var milestoneHoursProjected = milestoneBranch.child('estimated_hours').val();


    // milestoneHours += hours;
    // milestoneLOC += lines_of_code;
    // var milestoneHoursPercent = (milestoneHours * 100) / milestoneHoursProjected;

    // // update milestone
    // milestoneBranch.ref().update({
    //     'total_hours': milestoneHours,
    //     'total_lines_of_code': milestoneLOC,
    //     'hours_percent': milestoneHoursPercent
    // });

    // var taskBranch = milestoneBranch.child('tasks').child(task);
    // var taskHours = taskBranch.child('total_hours').val();
    // var taskLOC = taskBranch.child('total_lines_of_code').val();
    // var taskHoursProjected = taskBranch.child('updated_time_estimate').val();

    // taskHours += hours;
    // taskLOC += lines_of_code;
    // var taskHoursPercent = (taskHours * 100) / taskHoursProjected;

    // //update task
    // taskBranch.ref().update({
    //     'total_hours': taskHours,
    //     'total_lines_of_code': taskLOC,
    //     'hours_percent': taskHoursPercent
    // });
    // });
}
