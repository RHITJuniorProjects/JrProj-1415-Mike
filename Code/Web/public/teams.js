var milestonePage;
var projectPage;
var taskPage;
var selectedProject;
var selectedMilestone;

function selectProject(project){
    milestonePage.show();
    projectPage.hide();
    taskPage.hide();
	selectedProject = project;
	var milestones = selectedProject.getMilestones();
	var $panel = $('#milestones-panel');
	milestones.onItemAdded(function(milestone){
		$panel.append(milestone.getButtonDiv());
	});
}

function selectMilestone(milestone){
    milestonePage.hide();
    projectPage.hide();
    taskPage.show();
    currentMilestone = milestone;
	var tasks = currentMilestone.getTasks();
	var $panel = $('#tasks-table');
	tasks.onItemAdded(function(task){
		$panel.append(task.getTableRow());
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
        $panel.append(project.getButtonDiv())
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

$(function(){
    milestonePage = $('#milestones-page');
    projectPage = $('#projects-page');
    taskPage = $('#tasks-page');
	allProjects();
	getAllUsers();
});
