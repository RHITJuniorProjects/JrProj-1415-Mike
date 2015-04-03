<div id='tasks-page' hidden>
	<div class="small-2 columns small-offset-1 tabbar">
		<?php
			require_once 'tabbar.php';
			make_tabbar(
				array(
					new Tab("Back To Milestone",
					        ["onclick" => "selectProject(selectedProject)"],
							"#ViewTasks"),
					new Tab("View Tasks",["id" => "TaskTab", "class" => "active"]),
					new Tab("Add New Task",["onclick" => "Milestone.prototype.newTask() || setTimeout(function(){ $('#TaskTab a').click() });"],"#ViewTasks"),
					new Tab("Task Statistics",["onclick" => "Task.prototype.getCharts()"],"#ViewTasks"),
					new Tab("Burndown Chart")
				)
			);
		?>
	</div>
	<div class="small-9 columns main-content">
		<div class="tabs-content">
			<div class="content active" id="ViewTasks">
				<table id="tasks-table" style="width:100%">
					<thead>
						<td>Task Name</td>
						<td>Description</td>
						<td>Assigned User</td> 
						<td>Category</td>
						<td>Status</td>
						<td>Due Date</td>
						<td>Estimated Hours</td>
						<td class="gamification">Bounty Points</td>
					</thead>
					<tbody id="task-rows">
					</tbody>
				</table>
			</div>
		<!--
		<div class="button task-row" data-reveal-id="task-modal" onclick="newTask()">
			Add New Task
		</div>
		<div class="button task-row" data-reveal-id="taskContainer" onclick="taskStatics()">
			Task Statistics
		</div>
		-->
			<div class="content" id="BurndownChart">
				<br />
				<div id="milestone-burn-down-chart">
				</div>
			</div>
		</div>
	</div>
</div>
<div id="task-modal" class="reveal-modal" data-reveal>
		<!-- <div class="small-12 columns wide" id="tasks-table">
			<div class="row wide collapse" id="tasks-table-header">
				<div class="small-1 columns" 
			</div>
		</div>-->
</div>
<div id="taskContainer" class="reveal-modal" data-reveal>
</div>
