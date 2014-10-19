var milestonePage;
var projectPage;
var taskPage;

var projects = new Table(function(fb){ return new Project(fb);},firebase.child('projects'));

var milestones = {};

function selectProject(projectID){
    milestonePage.show();
    projectPage.hide();
    taskPage.hide();
	var project = projects.get(projectID);
	var milestones = project.getMilestones();
	var panel = $('#milestones-panel');
	var title = $('#project-name');
	project.getName(function(name){
		title.text(name);
	});

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

var newProject = {
	name:'',
	description:'',
	members:[]
}

function createProject(){
	var proj = Projects.add()
	proj.setName(name);
	proj.setDescription(desc);
}

$(function(){
    milestonePage = $('#milestones-page');
    projectPage = $('#projects-page');
    taskPage = $('#tasks-page');
	allProjects();
}); 

