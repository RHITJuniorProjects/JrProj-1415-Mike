/**
 * Metric collection script for CSSE371 Henry project
 * @author Sean Carter, Abby Mann
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

var commitsRef = new Firebase(firebaseUrl+'/commits');
var usersRef = new Firebase(firebaseUrl+'/users');
var projectsRef = new Firebase(firebaseUrl+'/projects');


// add a listener to commits for new projects
commitsRef.on('child_added', function(project) {
    var isFirstRunThrough = true;
    // ignore description
    if (project.name() == 'description') return;
    
    
    // add a listener to each project for new commits
    project.ref().on('child_added', function(commit) {
        // ignore description
        if (commit.name() == 'description') return;
        
        if (isFirstRunThrough) return;
        
        calculateMetrics(project.name(), commit);
    });
    isFirstRunThrough = false;
    console.log("added listener for project " + project.name());
});


function calculateMetrics(projectName, commit) {
    console.log("Calculating metrics for: " + projectName + ", " + commit.name());
    var hours = commit.child('hours').val();
    var lines_of_code = commit.child('lines_of_code').val();
    var milestone = commit.child('milestone').val();
    var user = commit.child('user').val();
    var task = commit.child('task').val();

    // metrics data for users
    usersRef.child(user)
            .child('projects')
            .child(projectName)
            .once('value', function(userProject) {

        var userMilestone = userProject.child('milestones').child(milestone);
        var userMilestoneHours = userMilestone.child('total_hours').val();
        var userMilestoneLOC = userMilestone.child('total_lines_of_code').val();
        
        userMilestoneHours += hours;
        userMilestoneLOC += lines_of_code;
        
        // update milestone data
        userMilestone.ref().update({
            'total_hours' : userMilestoneHours,
            'total_lines_of_code' : userMilestoneLOC
        });
        
        var userProjectHours = userProject.child('total_hours').val();
        var userProjectLOC = userProject.child('total_lines_of_code').val();
        
        userProjectHours += hours;
        userProjectLOC += lines_of_code;
        
        // update project data
        userProject.ref().update({
            'total_hours' : userProjectHours,
            'total_lines_of_code' : userProjectLOC
        });
        
    });
    
    // metrics data for projects and milestones
    projectsRef.child(projectName)
            .once('value', function(projectBranch) {

        var projectLOC = projectBranch.child('total_lines_of_code').val();
        var projectHours = projectBranch.child('total_hours').val();
        
        projectLOC += lines_of_code;
        projectHours += hours;
        
        // update project
        projectBranch.ref().update({
            'total_hours' : projectHours,
            'total_lines_of_code' : projectLOC
        });
        
        // TODO: need to update completeness percentages
        
        var milestoneBranch = projectBranch.child('milestones').child(milestone);
        
        var milestoneLOC = milestoneBranch.child('total_lines_of_code').val();
        var milestoneHours = milestoneBranch.child('total_hours').val();
        
        milestoneLOC += lines_of_code;
        milestoneHours += hours;
        
        // update milestone
        milestoneBranch.ref().update({
            'total_hours' : milestoneHours,
            'total_lines_of_code' : milestoneLOC
        });
    });
}