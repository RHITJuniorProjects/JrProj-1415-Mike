<html>
	<head>
		<title>Henry - Projects</title>
		<?php require 'header.php';?>
	</head>
	<body class="wide">
		<?php require 'topbar.php';?>
		<div id="projects-page" class="wide row">
			<div class="small-2 columns small-offset-1 tabbar">
				<?php
					require_once 'tabbar.php';
					make_tabbar(
						array(
							new Tab("My Projects",["id" => "MyProjectsTab","class" => "active"]),
							new Tab("My Tasks"),
							new Tab("My Statistics"),
							new Tab("Project Statistics"),
							new Tab("Add Project",["onclick" => "$('#myProjectModal').foundation('reveal','open') && setTimeout(function(){ $('#MyProjectsTab a').click() })"],"#MyProjects"),
							new Tab("LeaderBoard", ["class" => "gamification", "onclick" => "userLeaderboard()"])
							)
					);
				?>
			</div>
			<div class="small-9 columns main-content">
				<div class="tabs-content">
					<div class="content" id="MyTasks">
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
							<tbody id="my-tasks-rows">
							</tbody>
						</table>
					</div>
					<div class="content active" id="MyProjects">
						<div class="small-10 columns small-offset-1 ">
							<div class="row collapse text-center outlined">
								<h1>My Projects</h1>
								<!--<button data-reveal-id="myProjectModal">Add Project</button>-->
								<div id="myProjectModal" class="reveal-modal small-4" data-reveal>
									<h2>New Project</h2><br />
									<form action="" onsubmit="Project.prototype.addNewProject(); return false;">
										<label for="projectName">Project Name:</label> <input type="text" id="projectName">
										<label for="projectDescription">Description:</label> <input type="text"id="projectDescription" />
										<label for="projectDueDate">Due Date:</label> <input type="text" placeholder="yyyy-mm-dd" id="projectDueDate" onclick="showDatePicker('#projectDueDate')" />
										<label for="projectEstimatedHours">Total Estimated Hours:</label> <input type="text" id="projectEstimatedHours">
										<input type="submit" class="button" id="project-submit" value="Add Project" />
										<div id="project-error" class="my-error" hidden>All fields must be specified</div>
									</form>
								</div>
							</div>
							<dl class="row collapse accordion outlined" data-accordion>
								<dd class="accordion-navigation">
									<a href="#projects-panel" class="text-center outlined">
										<h3>Production</h3>
									</a>
									<div id="projects-panel" class="content active panel row">
										<!-- projects added by projects.js -->
									</div>
								</dd>
							</dl>
							<dl class="row collapse accordion" data-accordion>
								<dd class="accordion-navigation">
									<a href="#finished-projects-panel" class="text-center outlined">
										<h3>Closed</h3>
									</a>
									<div id="finished-projects-panel" class="content active row panel">
										<!-- pojects added by projects.js -->
									</div>
								</dd>
							</dl>
						</div>
						<div id="member-modal" class="reveal-modal" data-reveal>
							<div class="row">
								<div class="small-12 columns">
									<h1 id="member-modal-name">
										<!-- name set by projects.js -->
									</h1>
								</div>
							</div>
							<div class="row">
								<div id="member-modal-tiles" class="small-12-columns">
									<!-- content added by projects.js on member button click -->
								</div>
							</div>
						</div>
					</div>
					<div class="content" id="ProjectStatistics">
						<div class="row">
							<div class="small-12 columns">
								<div id="projContainer1"></div>
							</div>
							<div class="small-12 columns">
								<div id="projContainer2"></div>
							</div>
						</div>
					</div>
						<div class="content" id="MyStatistics">
						<div class="row">
							<div class="small-12 columns">
								<div id="UserStatistics1"></div>
							</div>
							<div class="small-12 columns">
								<div id="UserStatistics2"></div>
							</div>
						</div>
					</div>
					<div class="content" id="LeaderBoard">
						<div class="row">
							<div class="small-12 columns">
								<div>
								<table>
									<tr>
										<td> Place</td>
										<td> Name</td>
										<td> Points</td>
									</tr>
									<tr id = "currentUser">
										<td id = "0"></td>
										<td id = "name0"></td>
										<td id = "pointValue0"></td>
									</tr>
									<?php
										for($i = 1; $i < 26; $i++) {
									?>
											<tr id="place<?php echo $i; ?>">
												<td id="<?php echo $i; ?>"></td>
												<td id="name<?php echo $i; ?>"></td>
												<td id="pointValue<?php echo $i; ?>"></td>
											</tr>
									<?php		
										}
									?>
								</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<?php
			require 'Milestones.php';
			require 'tasks.php';
			require 'mytasks.php';
			require 'profile.php';
			require 'myStatistics.php'
		?>
		
	</body>
</html>
