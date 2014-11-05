
<div id="milestones-page" hidden>
	<div class="small-2 columns small-offset-1 tabbar">
		<?php
			$tabs = array("Project Milestones","Team Members","Milestone Statistics");
			require 'tabbar.php';
		?>
	</div>
		<div class="small-9 columns main-content">
			<div class="tabs-content">
				<div class="content active" id="ProjectMilestones">
					<div class="small-10 columns small-offset-1">
						<div class="row collapse text-center outlined">
							<h1><a id="project-name" onclick="allProjects()"></a></h1>
							<button data-reveal-id="myModal">Add Member</button>
							<div id="myModal" class="reveal-modal" data-reveal>
								Users
								<select id="member-select"></select>
								<div class="row"><button id="member-submit"<button class="close-reveal-modal" onclick="addNewMember();">Add</button></div>
							</div>
							<button data-reveal-id="myMilestoneModal">Add Milestone</button>
							<div id="myMilestoneModal" class="reveal-modal" data-reveal>
								<h2>New Milestone</h2><br>
								Milestone Name: <input type="text" id="milestoneName"></br>
								Description: <input type="text" id="milestoneDescription"></br>
								Due Date: <input type="text" id="milestoneDueDate"></br>
								Estimated Hours: <input type="text" id="milestoneEstimatedHours"></br>
								<div class="row"><button id="milestone-submit" <button class="close-reveal-modal" onclick="addNewMilestone()">Submit</button></div>
							</div>
						</div>
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
					<div class="small-10 columns small-offset-1">
						<div class="row text-center">
							<h1>Team Members</h1>
						</div>
						<div class="row" id="member-container">
						</div>
					</div>
				</div>
				<div class="content" id="MilestoneStatistics">
					<div class="row">
						<div class="small-12 column">
							<div id="mileContainer"></div>
						</div>
						<div class="small-12 column">
							<div id="linesOfCode"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>	
</div>
