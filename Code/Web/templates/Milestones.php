<html>
	<head>
		<title>Henry - Milestones</title>
		<?php require 'header.php';?>
		<script type="text/javascript" src="/milestones.js"></script>
	</head>
	<body>
		<?php require 'topbar.php';?>
		<div class="row text-center">
		<h1 id="project-name"></h1>
		</div>
		<div class="small-10 columns small-offset-1">
			<dl class="row collapse accordion" data-accordion>
				<dd class="accordion-navigation">
					<a href="#panel1" class="text-center">
						<h3>Production</h3>
					</a>
					<div id="panel1" class="content active row panel">
						<div class="small-4 columns small-offset-1">
							<div class="row text-center">
								<h3 id="milestone-name"></h3>
							</div>
								<div class="row">
								<ul>
									<li><a href="tasks" class="button expand">Tasks</a></li>
								</ul>
							</div>
						
						</div>
						<div class="small-4 columns small-offset-2 left">
							<div class="row text-center">
								<h3>Milestone 5</h3>
							</div>
							<div class="row">
								<ul>
									<li><a href="tasks" class="button expand">Tasks</a></li>
								</ul>
							</div>
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
							<div class="row text-center">
								<h3>Milestone 3</h3>
							</div>
							<div class="row">
								<ul>
									<li><a href="tasks" class="button expand">Tasks</a></li>
									
								</ul>
							</div>
						</div>
					</div>
				</dd>
			</dl>
			<dl class="row collapse accordion" data-accordion>
				<dd class="accordion-navigation">
					<a href="#panel3" class="text-center">
						<h3>Completed</h3>
					</a>
					<div id="panel3" class="content active row panel">
						<div class="small-4 columns small-offset-1">
							<div class="row text-center">
								<h3>Milestone 1</h3>
							</div>
							<div class="row">
								<ul>
									<li><a href="tasks" class="button expand">Tasks</a></li>
									
								</ul>
							</div>
						</div>
						<div class="small-4 columns small-offset-2 left">
							<div class="row text-center">
								<h3>Milestone 2</h3>
							</div>
							<div class="row">
								<ul>
									<li><a href="tasks" class="button expand">Tasks</a></li>
									
								</ul>
							</div>
						</div>
					</div>
				</dd>
			</dl>
		</div>
	</body>
</html>
