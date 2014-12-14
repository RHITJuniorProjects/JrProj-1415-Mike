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
    selectedMilestone = milestone;
	var tasks = selectedMilestone.getTasks();
	var $panel = $('#task-rows');
	$panel.children().remove();
	tasks.onItemAdded(function(task){
		$panel.prepend(task.getTableRow());
	});
}

function showProjects(){
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

function selectUser(user){
	userProjects = user.getProjects();
    userProjects.onItemAdded(function(project) {
        $title = $('#project-name' + project.uid);
        $description = $('#project-description' + project.uid);
        $panel.append(project.getButtonDiv())
    });
}

$(function(){
    milestonePage = $('#milestones-page');
    projectPage = $('#projects-page');
    taskPage = $('#tasks-page');
	showProjects();
	getAllUsers();
    var $panel = $('#projects-panel');
});
