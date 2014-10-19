var milestonePage;
var projectPage;
var taskPage;
var selectedProject;
var selectedMilestone;
var projects = new Table(function(fb){ return new Project(fb);},firebase.child('projects'));

function selectProject(projectID){
    milestonePage.show();
    projectPage.hide();
    taskPage.hide();
	selectedProject = projects.get(projectID);
	var milestones = selectedProject.getMilestones();
	var $panel = $('#milestones-panel');
	milestones.onItemAdded(function(milestone){
		milestone.getButtonHtml(function(html){
			$panel.append(html);
		});
	});
}

function selectMilestone(milestoneId){
    milestonePage.hide();
    projectPage.hide();
    taskPage.show();
    currentMilestone = selectedProject.getMilestones().get(milestoneId);
	var tasks = currentMilestone.getTasks();
	var $panel = $('#tasks-table');
	tasks.onItemAdded(function(task){
		task.getTableHtml(function(html){
			$panel.append(html);
		});
	});
}

function allProjects(){
    milestonePage.hide();
    projectPage.show();
    taskPage.hide();
    var $panel = $('#projects-panel');
    projects.onItemAdded(function(project) {
        $title = $('#project-name' + project.uid);
        $description = $('#project-description' + project.uid);
        project.getButtonHtml(function(html){
            $panel.append(html);
        });
        project.getName(function(name){
            $title.html(name);
        });
        project.getDescription(function(description){
            $description.html(description);
        });
    });
}

function getAllUsers(){
	users.onItemAdded(function (user) {
		var $select = $('#member-select');

		user.getName(function(nameStr){
			$select.append('<option id="username-' + user.uid + '">' +
				nameStr + '</option>');	
		});
	
	});

}
// milestone
/*$(function(){
	var projectIDs = ['-JYcg488tAYS5rJJT4Kh'];

});
*/

var selectedProject = null;
var currentTask = null;

$(function(){
    milestonePage = $('#milestones-page');
    projectPage = $('#projects-page');
    taskPage = $('#tasks-page');
	allProjects();
	getAllUsers();
});
