
<div id="milestones-page" hidden>
	<div class="small-2 columns small-offset-1 tabbar">
		<?php
			$tabs = array("Project Milestones","Team Members","Project Statistics");
			require 'tabbar.php';
		?>
	</div>
		<div class="small-9 columns">
			<div class="tabs-content">
				<div class="content active" id="ProjectMilestones">
					<div class="row text-center">
						<h1 id="project-name">
							<button data-reveal-id="myModal">Add Member</button>
							<div id="myModal" class="reveal-modal" data-reveal>
								Users
								<select id="member-select"></select>
								<button onclick="addNewMember();">Add</button>
							</div>
						</h1>
					</div>
					<div class="small-10 columns small-offset-1">
						<dl class="row collapse accordion" data-accordion>
							<dd class="accordion-navigation">
								<a href="#milestones-panel" class="text-center">
									<h3>Production</h3>
								</a>
								<div id="milestones-panel" class="content active row panel">
									<!-- milestones inserted by javascript -->
								</div>
							</dd>
						</dl>
						<dl class="row collapse accordion" data-accordion>
							<dd class="accordion-navigation">
								<a href="panel3" class="text-center">
									<h3>Completed</h3>
								</a>
							</dd>
						</dl>
					</div>
				</div>
				<div class="content" id="TeamMembers">
				</div>
				<div class="content" id="ProjectStatistics">
					<div class="row">
						<div class="small-12 column">
							<div id="mileContainer"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>	
</div>
