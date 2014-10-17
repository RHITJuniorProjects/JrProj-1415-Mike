
$(function(){
	var projectIDs = ['-JYcg488tAYS5rJJT4Kh'];
	var $panel = $('#panel1');
	for(var i = 0; i < 1; i++){
		//var $tString = '#project-name' + i;
		//var $dString = '#project-description' + i;
		
		$title = $('#project-name' + i);
		//console.log($title);
		$description = $('#project-description' + i);
		//var projectId = $.cookie('project');
		var projectId = projectIDs[i];
		var project = projects.get(projectId);
		var milestone = project.getMilestones();
		
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



