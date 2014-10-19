<html class="wide">
	<head>
		<title>Henry - Teams</title>
		<?php require 'header.php';?>
		<script type="text/javascript" src="/teams.js"></script>
	</head>
	<body class="wide">
		<?php require 'topbar.php';?>
		<div id="projects-page" class="wide row">
			<div class="small-2 columns small-offset-1 tabbar">
				<?php
					$tabs = array("My Projects","My Tasks","My Statistics");
					require 'tabbar.php';
				?>
			</div>
			<div class="small-9 columns">
				<div class="tabs-content">
					<div class="content active" id="MyProjects">
						<div class="row text-center">
							<h1>My Teams</h1>
						</div>
						<div class="small-10 columns small-offset-1 ">
							<dl class="row collapse accordion" data-accordion>
								<dd class="accordion-navigation">
									<a href="#projects-panel" class="text-center">
										<h3>Production</h3>
									</a>
									<div id="projects-panel" class="content active panel row">
										
									</div>
								</dd>
							</dl>
							<dl class="row collapse accordion" data-accordion>
								<dd class="accordion-navigation">
									<a href="#finished-projects-panel" class="text-center">
										<h3>Closed</h3>
									</a>
									<div id="finished-projects-panel" class="content active row panel">
										<!-- pojects added by projects.js -->
									</div>
								</dd>
							</dl>
						</div>
					</div>
				</div>
			</div>
		</div>
		<?php require 'Milestones.php'?>
		<?php require 'tasks.php'?>
	</body>
</html>

