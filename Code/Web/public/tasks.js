

$(function(){
	$title = $('#project-name');
	//var projectId = $.cookie('project');
	var projectId = '-JYcg488tAYS5rJJT4Kh';
	var project = projects.get(projectId);
	project.getName(function(name){
		$title.html(name);
	});
});

