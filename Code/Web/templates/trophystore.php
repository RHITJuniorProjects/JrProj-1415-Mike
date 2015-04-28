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
							new Tab("Back to My Projects", ["onclick" => "backFromStore()"],"#ProjectMilestones")
						)
					);
				?>
			</div>
		<div class="small-9 columns main-content">
			<div class="tabs-content">
				<div class= "content active">
					<table style="width:100%">
							<thead>
								<td>Name</td>
								<td>Description</td>
								<td>Cost</td> 
								<td>Image</td>
								<td>&nbsp;</td>
							</thead>
							<tbody id="trophy-store-rows">
						 </tbody>
					</table>
				</div>
			</div>
		  </div>
		</div>
	</body>
</html>