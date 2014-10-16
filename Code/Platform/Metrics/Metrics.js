/**
* Metric collection script for CSSE371 Henry project
*/

var Firebase = require("firebase");
var commitsRef = new Firebase("https://henry-test.firebaseio.com/commits");
var usersRef = new Firebase("https://henry-test.firebaseio.com/users");
var projectsRef = new Firebase("https://henry-test.firebaseio.com/projects");

var projects = null;

// commitsRef.on('value', function(commitsData) {
//     projects = commitsData;
//     projects.forEach(function(project) {
//         if (project.name() == 'description') return;
//         addCommitListener(project);
//         console.log("added commit listener for project " + project.name());
//     });
// });

// commitsRef.off('value');

commitsRef.on('child_added', function(project) {
        if (project.name() == 'description') return;
        project.ref().on('child_added', function(commit) {
            console.log("inside commit listener");
            if (commit.name() == 'description') {
                console.log("hit description");
                return;
            }
            var hours = commit.child('hours').val();
            var lines_of_code = commit.child('lines_of_code').val();
            var milestone = commit.child('milestone').val();
            var user = commit.child('user').val();
            var task = commit.child('task').val();

            usersRef.child(user)
                    .child('projects')
                    .child(project.name())
                    .once('value', function(userProject) {
                console.log("inside user listener");
                // metrics data for users
                var userMilestone = userProject.child('milestones').child(milestone);
                var userMilestoneHours = userMilestone.child('total_hours').val();
                var userMilestoneLOC = userMilestone.child('total_lines_of_code').val();
                
                userMilestoneHours += hours;
                userMilestoneLOC += lines_of_code;
                
                userMilestone.ref().update({
                    'total_hours' : userMilestoneHours,
                    'total_lines_of_code' : userMilestoneLOC
                });
                
                var userProjectHours = userProject.child('total_hours').val();
                var userProjectLOC = userProject.child('total_lines_of_code').val();
                
                userProjectHours += hours;
                userProjectLOC += lines_of_code;
                
                userProject.ref().update({
                    'total_hours' : userProjectHours,
                    'total_lines_of_code' : userProjectLOC
                });
                
            });
            
            // usersRef.child(user)
            //         .child('projects')
            //         .child(project.name())
            //         .off('value');
            
            projectsRef.child(project.name()).once('value', function(projectBranch) {
                console.log("inside project branch listener");
                // metrics data for projects and milestones
                var projectLOC = projectBranch.child('total_lines_of_code').val();
                var projectHours = projectBranch.child('total_hours').val();
                
                projectLOC += lines_of_code;
                projectHours += hours;
                
                projectBranch.ref().update({
                    'total_hours' : projectHours,
                    'total_lines_of_code' : projectLOC
                });
                
                // need to update completeness percentages
                
                var milestoneBranch = projectBranch.child('milestones').child(milestone);
                
                var milestoneLOC = milestoneBranch.child('total_lines_of_code').val();
                var milestoneHours = milestoneBranch.child('total_hours').val();
                
                milestoneLOC += lines_of_code;
                milestoneHours += hours;
                
                milestoneBranch.ref().update({
                    'total_hours' : milestoneHours,
                    'total_lines_of_code' : milestoneLOC
                });
            
         });
        // projectsRef.child(project.name()).off('value');        
                
    });
        console.log("added listener for project " + project.name());
});
