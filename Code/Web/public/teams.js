var milestonePage;
var projectPage;
var taskPage;

var projects = new Table(function(fb){ return new Project(fb);},firebase.child('projects'));

var milestones = {};

function selectProject(projectID){
    milestonePage.show();
    projectPage.hide();
    taskPage.hide();
	var milestones = projects.get(projectID).getMilestones();
	var panel = $('#milestones-panel');
	milestones.onItemAdded(function(milestone){
		milestone.getButtonHtml(function(html){
			panel.append(html);
		});
	});


}

function selectMilestone(milestone){
    milestonePage.hide();
    projectPage.hide();
    taskPage.show();
    currentMilestone = milestone;
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

		user.popSelect(function(html){
            $select.append(html);
        });
		var name = $('#username-' + user.uid);
		
		user.getName(function(nameStr){
			name.html(nameStr);
		});
		
	
		$title = $('#username-' + user.uid);
		user.getName(function(name){
            $title.html(name);
        });
	
	});

}

$(function(){
	var projectIDs = ['-JYcg488tAYS5rJJT4Kh'];
	var $panel = $('#panel1');
	for(var i = 0; i < 1; i++){
		//var $tString = '#project-name' + i;
		//var $dString = '#project-description' + i;
		
		$title = $('#milestone-name' + i);
		//console.log($title);
		$description = $('#milestone-description' + i);
		//var projectId = $.cookie('project');
		var projectId = projectIDs[i];
		var project = projects.get(projectId);
		
		var milestoneTable = project.getMilestones();
		
		var milestone = milestoneTable.get('-JYc_9ZGEPFM8cjChyKl');
		milestone.getButtonHtml(function(html){
			$panel.append(html);

		});
		milestone.getName(function(name){
			$title.html(name);
		});
		milestone.getDescription(function(description){
			$description.html(description);
		});
	}
});


var currentProject = null;
var currentTask = null;

$(function(){
    milestonePage = $('#milestones-page');
    projectPage = $('#projects-page');
    taskPage = $('#tasks-page');
	allProjects();
	getAllUsers();
});
