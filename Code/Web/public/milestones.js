

$(function(){
	$title = $('#project-name');
	//var projectId = $.cookie('project');
	var projectId = '-JYcg488tAYS5rJJT4Kh';
	var project = projects.get(projectId);
	var project.getName(function(name){
		console.log(name);
		$title.html(name);
	});
});
