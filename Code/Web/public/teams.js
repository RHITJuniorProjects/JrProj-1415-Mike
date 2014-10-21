var milestonePage;
var projectPage;
var taskPage;
var selectedProject;
var selectedMilestone;

function selectProject(project){
    milestonePage.show();
    projectPage.hide();
    taskPage.hide();
	if(selectedProject){
		selectedProject.off();
	}
	selectedProject = project;
	var milestones = selectedProject.getMilestones();
	var $panel = $('#milestones-panel');
	$panel.children().remove();
	milestones.onItemAdded(function(milestone){
		$panel.append(milestone.getButtonDiv());
	});
	var nameA = $('#project-name');
	selectedProject.getName(function(name){
		nameA.text(name);
	});
}

function selectMilestone(milestone){
    milestonePage.hide();
    projectPage.hide();
    taskPage.show();
	if(currentMilestone){
		currentMilestone.off();
	}
    currentMilestone = milestone;
	var tasks = currentMilestone.getTasks();
	var $panel = $('#tasks-table');
	$panel.children().remove();
	tasks.onItemAdded(function(task){
		$panel.append(task.getTableRow());
	});
}

function allProjects(){
    milestonePage.hide();
    projectPage.show();
    taskPage.hide();
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
    var $panel = $('#projects-panel');
    projects.onItemAdded(function(project) {
        $title = $('#project-name' + project.uid);
        $description = $('#project-description' + project.uid);
        $panel.append(project.getButtonDiv())
    });
});
