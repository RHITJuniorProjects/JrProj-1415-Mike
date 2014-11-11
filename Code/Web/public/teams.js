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
	
	drawStuff(selectedProject.uid);
	
	var $panel = $('#milestones-panel');
	$panel.children().remove();
	milestones.onItemAdded(function(milestone){
		$panel.append(milestone.getButtonDiv());
	});
	var nameA = $('#project-name');
	selectedProject.getName(function(name){
		nameA.text(name);
	});

/*	var memberCont = $('#member-container');
	selectedProject.getMembers().onItemAdded(function(user){
		memberCont.append(user.getMemberTile(selectedProject));
	});*/
}

function selectMilestone(milestone){
    milestonePage.hide();
    projectPage.hide();
    taskPage.show();
	if(selectedMilestone){
		currentMilestone.off();
	}
    currentMilestone = milestone;
	var tasks = currentMilestone.getTasks();
	var $panel = $('#tasks-table');
	$('.task-row').remove();
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
			$select.append('<option value="' + user.uid + '">' +
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
