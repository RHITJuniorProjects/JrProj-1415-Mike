/**
 * Metric collection script for CSSE371 Henry project
 * @author Sean Carter, Abby Mann
 */

var Firebase = require("firebase");
var commitsRef = new Firebase("https://henry-test.firebaseio.com/commits");
var usersRef = new Firebase("https://henry-test.firebaseio.com/users");
var projectsRef = new Firebase("https://henry-test.firebaseio.com/projects");

// add a listener to commits for new projects
commitsRef.on('child_added', function(project) {
    // ignore description
    if (project.name() == 'description') return;
    
    // add a listener to each project for new commits
    project.ref().on('child_added', function(commit) {
        // ignore description
        if (commit.name() == 'description') return;
        
        var hours = commit.child('hours').val();
        var lines_of_code = commit.child('lines_of_code').val();
        var milestone = commit.child('milestone').val();
        var user = commit.child('user').val();
        var task = commit.child('task').val();

        // metrics data for users
        usersRef.child(user)
                .child('projects')
                .child(project.name())
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
        projectsRef.child(project.name())
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
    });
    console.log("added listener for project " + project.name());
});
