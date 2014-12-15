
<div id="profile-page" hidden>
	<div class="small-2 columns small-offset-1 tabbar">
		<?php
			require_once 'tabbar.php';
			make_tabbar(
				array(
					new Tab('<span class="profile-name"></span> Profile',['class'=>'active'],'Profile'),
				)
			);
		?>
	</div>
	<div class="small-9 columns main-content">
		<div class="tabs-content">
			<div class="content" id="Profile">
				<div class="small-10 columns small-offset-1">
					<div class="row collapse text-center">
						<h1><span class="profile-name"></span> Profile</h1>
					</div>
				</div>
			</div>
			<div class="content active" id="ProfileProjects">
				<div class="small-10 columns small-offset-1 ">
					<div class="row collapse text-center outlined">
						<h1><span class="profile-name"></span>Projects</h1>
					</div>
					<dl class="row collapse accordion outlined" data-accordion>
						<dd class="accordion-navigation">
							<a href="#profile-projects-panel" class="text-center outlined">
								<h3>Production</h3>
							</a>
							<div id="profile-projects-panel" class="content active panel row">
								<!-- projects added by henry.js -->
							</div>
						</dd>
					</dl>
					<dl class="row collapse accordion" data-accordion>
						<dd class="accordion-navigation">
							<a href="#profile-finished-projects-panel" class="text-center outlined">
								<h3>Closed</h3>
							</a>
							<div id="profile-finished-projects-panel" class="content active row panel">
								<!-- pojects added by henry.js -->
							</div>
						</dd>
					</dl>
				</div>
			</div>
		</div>
	</div>
</div>
