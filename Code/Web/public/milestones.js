

$(function(){
	$title = $('#project-name');
	//var projectId = $.cookie('project');
	var projectId = '-JYcg488tAYS5rJJT4Kh';
	var project = projects.get(projectId);
	project.getName(function(name){
		$title.html(name);
	});
});

$(function(){
	$milestoneTitle = $('#milestone-name');
	//var projectId = $.cookie('project');
	var milestoneId = '-JYc_9ZGEPFM8cjChyKl';
	var milestone = milestones.get(milestoneId);
	milestone.getName(function(name){
		$milestoneTitle.html(name);
	});
});

