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
					$tabs = array("My Projects","My Tasks","My Statistics","Project Statistics");
					require 'tabbar.php';
				?>
			</div>
			<div class="small-9 columns main-content">
				<div class="tabs-content">
					<div class="content active" id="MyProjects">
						<div class="small-10 columns small-offset-1 ">
							<div class="row collapse text-center outlined">
								<h1>My Projects</h1>
								<button data-reveal-id="myProjectModal">Add Project</button>
								<div id="myProjectModal" class="reveal-modal small-4" data-reveal>
                                    <h2>New Project</h2><br />
                                    <form action="" onsubmit="addNewProject(); return false;">
                                        <label for="projectName">Project Name:</label> <input type="text" id="projectName">
                                        <label for="projectDescription">Description:</label> <input type="text"id="projectDescription" />
                                        <label for="projectDueDate">Due Date:</label> <input type="text" id="projectDueDate" onclick="showDatePicker('#projectDueDate')" />
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
					</div>
					<div class="content" id="ProjectStatistics">
						<div class="row">
							<div class="small-6 columns">
								<div id="projContainer1"></div>
							</div>
							<div class="small-6 columns">
								<div id="projContainer2"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<?php require 'Milestones.php'?>
		<?php require 'tasks.php'?>
	</body>
</html>

