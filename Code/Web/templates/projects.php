<html>
	<head>
		<title>Henry - Teams</title>
		<?php require 'header.php';?>
		<script type="text/javascript" src="/teams.js"></script>
	</head>
	<body>
		<?php require 'topbar.php';?>
		<div id="projects-page">
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
							<button data-reveal-id="myModal">Add Member</button>
							<div id="myModal" class="reveal-modal" data-reveal>
								 Users
								 <select>
									<option>mike</option>
									<option>jordan</option>
									<option>bulls</option>
									<option>NBA</option>
								</select>
								<button>Select</button>
							</div>
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
		<?php require 'Milestones.php'?>
		<?php require 'tasks.php'?>
	</body>
</html>

