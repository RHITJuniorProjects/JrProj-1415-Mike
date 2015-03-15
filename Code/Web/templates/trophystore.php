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
							new Tab("Back to My Projects", ["onclick" => "showProjects()"],"#ProjectMilestones")
							
						)
					);
				?>
			</div>
		</div>
	</body>
</html>

