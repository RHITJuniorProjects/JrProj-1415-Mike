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
		<div class="small-10 columns small-offset-1">
			<dl class="row collapse accordion" data-accordion>
				<dd class="accordion-navigation">
					<a href="#panel1" class="text-center">
						<h3>Production</h3>
					</a>
					<div id="panel1" class="content active panel row">
						<div class="row">
							<div class="small-4 columns small-offset-1">
								<div class="button expand text-center">
									<a href="Milestones"><h2 id="project-name"></h2></a>
								</div>
							</div>
							<div class="small-4 columns small-offset-2 left">
								<div class="button expand text-center">
									<h3>Project 2</h3>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="small-4 columns small-offset-1">
								<div class="button expand text-center">
									<h3>Project 6</h3>
								</div>
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
	</body>
</html>
