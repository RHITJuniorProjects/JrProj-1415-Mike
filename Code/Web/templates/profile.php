
<div id="profile-page" class="main-content" hidden>
	<div class="small-2 columns small-offset-1 tabbar">
		<?php
			require_once 'tabbar.php';
			make_tabbar(
				array(
					new Tab("Back to My Projects", ["onclick" => "showProjects()"]),
				)
			);
		?>
	</div>
	<div class="small-10 columns small-offset-1">
			<div class="content" id="Profile">
				<div class="small-10 columns small-offset-1 panel">
					<div class="row collapse text-center">
						<h1><span class="profile-name"></span>'s Profile</h1>
					</div>
					<div class="row text-center">
						About
					</div>
					<div class="row">
						<div class="small-8 columns small-offset-2 text-center">
							<span id="biography"></span>
						</div>
					</div>
					<div class="row text-center">
						Contact Information
					<div>
					<div id="contact-row" class="row">
						<!-- email added by henry.js -->
					</div>
					<div class="row text-center">
						<div class="small-12 columns">
							<h2><span class="profile-name"></span>'s Projects</h2>
							<h1>&nbsp;</h1>
						</div>
					</div>
					<div class="row text-center">
						<h2>Bounty Points: <span id="profile-points"></span></h2>
					</div>
					<div class="row">
						<div id="profile-projects" class="small-12 columns">
							<!-- populated by henry.js -->
						</div>
					<div class= "content active">
						<table style="width:100%">
							<thead>
								<td>Name</td>
								<td>Description</td>
								<td>Image</td>
							</thead>
							<tbody id="profile-trophies-rows">
						 </tbody>
					</table>
				</div>
			</div>
		  </div>
				</div>
			</div>
		</div>
	</div>
</div>
