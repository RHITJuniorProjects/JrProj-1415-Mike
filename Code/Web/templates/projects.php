<html>
	<head>
		<title>Henry - Projects</title>
		<?php require 'header.php';?>
		<script type="text/javascript" src="/teams.js"></script>
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
			<div id="content" class="small-9 columns small-offset-3">
				<div class="tabs-content">
					<div class="content active" id="MyProjects">
						<div class="small-10 columns small-offset-1 ">
							<div class="row collapse text-center outlined">
								<h1>My Projects</h1>
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
						<div class-"row">
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
			<?php require 'Milestones.php'?>
			<?php require 'tasks.php'?>
		</div>
	</body>
</html>

