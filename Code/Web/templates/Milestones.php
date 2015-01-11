
<div id="milestones-page" hidden>
	<div class="small-2 columns small-offset-1 tabbar">
		<?php
			require_once 'tabbar.php';
			make_tabbar(
				array(
					new Tab("Project Milestones",["class" => "active"]),
					new Tab("Team Members"),
					new Tab("Milestone Statistics")
				)
			);
		?>
	</div>
		<div class="small-9 columns main-content">
			<div class="tabs-content">
				<div class="content active" id="ProjectMilestones">
					<div class="small-10 columns small-offset-1">
						<div class="row collapse text-center outlined">
							<h1 id="project-name"></h1>
							<?php	// in case we want the add modal instead for the members page
							/*<button data-reveal-id="myModal">Add Member</button>
							<div id="myModal" class="reveal-modal small-4" data-reveal>
								Users <br />
                                <form action="" onsubmit="addNewMember(); return false;">
                                    <select id="member-select"></select>
                                    <input type="submit" id="member-submit" class="button" value="Add" />
                                    <div id="member-error" class="my-error" hidden>No member selected</div>
                                </form>
							</div>*/?>
							<button data-reveal-id="myMilestoneModal">Add Milestone</button>
							<div id="myMilestoneModal" class="reveal-modal small-4" data-reveal>
								<h2>New Milestone</h2><br />
                                <form action="" onsubmit="addNewMilestone(); return false;">
                                    <label for="milestoneName">Milestone Name:</label> <input type="text" id="milestoneName" />
                                    <label for="milestoneDescription">Description:</label> <input type="text" id="milestoneDescription" />
                                    <label for="milestoneDueDate">Due Date:</label> <input type="text" placeholder="yyyy-mm-dd" id="milestoneDueDate" onclick="showDatePicker('#milestoneDueDate')"/>
                                    <label for="milestoneEstimatedHours">Estimated Hours:</label> <input type="text" id="milestoneEstimatedHours" />
                                    <input type="submit" id="milestone-submit" class="button" value="Add Milestone" />
                                    <div id="milestone-error" class="my-error" hidden>All fields must be specified</div>
							    </form>
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
					<div class="row" id="burndownchart">
						<!-- burn down chart inserted by script -->
					</div>
				</div>
			</div>
		</div>
	</div>	
</div>
