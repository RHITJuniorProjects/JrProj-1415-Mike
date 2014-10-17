
$(function(){
	var projectIDs = ['-JYcg488tAYS5rJJT4Kh'];
	var $panel = $('#panel2');
	for(var i = 0; i < 1; i++){
		//var $tString = '#project-name' + i;
		//var $dString = '#project-description' + i;
		
		$title = $('#milestone-name' + i);
		//console.log($title);
		$description = $('#milestone-description' + i);
		//var projectId = $.cookie('project');
		var projectId = projectIDs[i];
		var project = projects.get(projectId);
		var milestone = project.getMilestones();
		
		milestone.get('-JYc_9ZGEPFM8cjChyKl').getButtonHtml(function(html){
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



