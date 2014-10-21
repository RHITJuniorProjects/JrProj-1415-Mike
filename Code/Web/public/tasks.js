
$(function(){
	var projectIDs = ['-JYcg488tAYS5rJJT4Kh'];
	var $panel = $('#task-table');
	for(var i = 0; i < 1; i++){
		//var $tString = '#project-name' + i;
		//var $dString = '#project-description' + i;
		
		$title = $('#task-name' + i);
		$description = $('#task-description' + i);
		$user = $('#task-user' + i);
		$category = $('#task-category' + i);
		$originalTime = $('#task-originalTime' + i);
		$updatedTime = $('#task-updatedTime' + i);
		//var projectId = $.cookie('project');
		var projectId = projectIDs[i];
		var project = projects.get(projectId);
		var milestone = project.getMilestones();
		var task = milestone.getTasks();
		
		task.get('-JYc_fRjZ_IV3WgHIszD').getTableHtml(function(html){
			$panel.append(html);
		});
		task.getName(function(name){
			$title.html(name);
		});
		task.getDescription(function(description){
			$description.html(description);
		});
		task.getAssignedUser(function(user){
			$user.html(user);
		});
		task.getCategory(function(category){
			$category.html(category);
		});
		task.getOriginalTime(function(originalTime){
			$originalTime.html(originalTime);
		});
		task.getUpdatedTime(function(updatedTime){
			$updatedTime.html(updatedTime);
		});
	}
});
