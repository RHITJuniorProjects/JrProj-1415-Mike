<html>
	<head>
		<title>Henry - Trophy Store</title>
		<?php require 'header.php';?>
	</head>
	<body class="wide">
		<?php require 'topbar.php';?>
		<div id="store-page" class="wide row">
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
</html>
