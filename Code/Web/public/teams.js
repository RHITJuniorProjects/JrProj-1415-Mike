
$(function(){
	var $panel = $('#panel1');
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
	
});
