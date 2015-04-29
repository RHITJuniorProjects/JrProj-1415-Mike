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
							new Tab("Back to My Projects", ["onclick" => "backFromStore()"],"#ProjectMilestones"),
							new Tab("Trophies", ["id" => "TrophyTab", "class" => "active"]),
							new Tab("Add New Trophy",["onclick" => "$('#addTrophyModal').foundation('reveal','open') && setTimeout(function(){ $('#TrophyTab a').click() });"],"#ProjectMilestones"),
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
					<div id="addTrophyModal" class="reveal-modal" data-reveal>
						<h2>New Trophy</h2><br />
						<form action="" onsubmit="Trophy.prototype.addTrophy(); return false;">
							<label for="trophyName">Trophy Name:</label> <input type="text" id="trophyName">
							<label for="trophyDescription">Description:</label> <input type="text"id="trophyDescription" />
							<label for="trophyCost">Cost:</label> <input type="text" id="trophyCost" />
							<label for="trophyImage">Image Url:</label> <input type="text" id="trophyImage">
							<input type="submit" class="button" id="trophy-submit" value="Add Trophy" />
							<div id="trophy-error" class="my-error" hidden>All fields must be specified</div>
						</form>
					</div>
				</div>
				</div>
			</div>
		  </div>
		</div>
	</body>
</html>