<html>
	<head>
		<title>Henry - Teams</title>
		<?php require 'header.php';?>
		<script type="text/javascript" src="/teams.js"></script>
	</head>
	<body>
		<?php require 'topbar.php';?>
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
					<a href="#panel2" class="text-center">
						<h3>Maintenance</h3>
					</a>
					<div id="panel2" class="content active row panel">
						<div class="small-4 columns small-offset-1">
							<div class="text-center button expand">
								<h3>Project 3</h3>
							</div>
						</div>
					</dd>
				</dl>
				<dl class="row collapse accordion" data-accordion>
					<dd class="accordion-navigation">
						<a href="#panel2" class="text-center">
							<h3>Maintenance</h3>
						</a>
						<div id="panel2" class="content active row panel">
							<div class="small-4 columns small-offset-1">
								<div class="text-center button expand">
									<h3>Project 3</h3>
								</div>
							</div>
						</div>
					</dd>
				</dl>
				<dl class="row collapse accordion" data-accordion>
					<dd class="accordion-navigation">
						<a href="#panel3" class="text-center">
							<h3>Closed</h3>
						</a>
						<div id="panel3" class="content active row panel">
							<div class="small-4 columns small-offset-1">
								<div class="text-center button expand">
									<h3>Project 4</h3>
								</div>
							</div>
							<div class="small-4 columns small-offset-2 left">
								<div class="button expand text-center">
									<h3>Project 5</h3>
								</div>
							</div>
						</div>
					</dd>
				</dl>
			</div>
		</div>
		<?php require 'Milestones.php'?>
		<?php require 'tasks.php'?>
	</body>
</html>

