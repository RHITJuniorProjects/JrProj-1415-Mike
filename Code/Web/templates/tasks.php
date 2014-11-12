<div id='tasks-page' hidden>
	<div class="main-content">
		<table id="tasks-table" style="width:100%">
			<thead>
				<td>Task Name</td>
				<td>Description</td>
				<td>Assigned User</td> 
				<td>Category</td>
				<td>Status</td>
                <td>Due Date</td>
				<td>Estimated Hours</td>
			</thead>
			<tbody id="task-rows">
			</tbody>
			<tbody>
				<tr class="task-row" data-reveal-id="task-modal" onclick="newTask()">
					<td>Add New Task</td>
				</tr>
			</tbody>
		</table>
		<div id="task-modal" class="reveal-modal" data-reveal>
		</div>
		<!-- <div class="small-12 columns wide" id="tasks-table">
			<div class="row wide collapse" id="tasks-table-header">
				<div class="small-1 columns" 
			</div>
		</div>-->
	</div>
</div>
