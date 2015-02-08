<html>
	<head>
		<title>Henry - Projects</title>
		<?php require 'header.php';?>
	</head>
	<body class="wide">
		<?php require 'topbar.php';?>
		<div id="projects-page" class="wide row">
			<div class="small-2 columns small-offset-1 tabbar">
				<?php
					require_once 'tabbar.php';
					make_tabbar(
						array(
							new Tab("My Projects",["class" => "active"]),
							new Tab("My Tasks", ["onclick" => "showMyTasksPage()"]),
							new Tab("My Statistics"),
							new Tab("Project Statistics"),
							new Tab("LeaderBoard", ["onclick" => "userLeaderboard()"])
						)
					);
				?>
			</div>
			<div class="small-9 columns main-content">
				<div class="tabs-content">
					<div class="content active" id="MyProjects">
						<div class="small-10 columns small-offset-1 ">
							<div class="row collapse text-center outlined">
								<h1>My Projects</h1>
								<button data-reveal-id="myProjectModal">Add Project</button>
								<div id="myProjectModal" class="reveal-modal small-4" data-reveal>
                                    <h2>New Project</h2><br />
                                    <form action="" onsubmit="addNewProject(); return false;">
                                        <label for="projectName">Project Name:</label> <input type="text" id="projectName">
                                        <label for="projectDescription">Description:</label> <input type="text"id="projectDescription" />
                                        <label for="projectDueDate">Due Date:</label> <input type="text" placeholder="yyyy-mm-dd" id="projectDueDate" onclick="showDatePicker('#projectDueDate')" />
                                        <label for="projectEstimatedHours">Total Estimated Hours:</label> <input type="text" id="projectEstimatedHours">
                                        <input type="submit" class="button" id="project-submit" value="Add Project" />
                                        <div id="project-error" class="my-error" hidden>All fields must be specified</div>
                                    </form>
							    </div>
							</div>
							<dl class="row collapse accordion outlined" data-accordion>
								<dd class="accordion-navigation">
									<a href="#projects-panel" class="text-center outlined">
										<h3>Production</h3>
									</a>
									<div id="projects-panel" class="content active panel row">
										<!-- projects added by projects.js -->
									</div>
								</dd>
							</dl>
							<dl class="row collapse accordion" data-accordion>
								<dd class="accordion-navigation">
									<a href="#finished-projects-panel" class="text-center outlined">
										<h3>Closed</h3>
									</a>
									<div id="finished-projects-panel" class="content active row panel">
										<!-- pojects added by projects.js -->
									</div>
								</dd>
							</dl>
						</div>
						<div id="member-modal" class="reveal-modal" data-reveal>
							<div class="row">
								<div class="small-12 columns">
									<h1 id="member-modal-name">
										<!-- name set by projects.js -->
									</h1>
								</div>
							</div>
							<div class="row">
								<div id="member-modal-tiles" class="small-12-columns">
									<!-- content added by projects.js on member button click -->
								</div>
							</div>
						</div>
					</div>
					<div class="content" id="ProjectStatistics">
						<div class="row">
							<div class="small-12 columns">
								<div id="projContainer1"></div>
							</div>
							<div class="small-12 columns">
								<div id="projContainer2"></div>
							</div>
						</div>
					</div>
						<div class="content" id="MyStatistics">
						<div class="row">
							<div class="small-12 columns">
								<div id="UserStatistics1"></div>
							</div>
							<div class="small-12 columns">
								<div id="UserStatistics2"></div>
							</div>
						</div>
					</div>
					<div class="content" id="LeaderBoard">
						<div class="row">
							<div class="small-12 columns">
								<div>
								<table>
									<tr>
										<td> Place</td>
										<td> Name</td>
										<td> Points</td>
									</tr>
									<tr id = "firstPlace">
										<td id = "1"></td>
										<td id = "name1"></td>
										<td id = "pointValue1"></td>
									</tr>
									<tr id = "secondPlace">
										<td id = "2"></td>
										<td id = "name2"></td>
										<td id = "pointValue2"></td>
									</tr>
									<tr id = "thirdPlace">
										<td id = "3"></td>
										<td id = "name3"></td>
										<td id = "pointValue3"></td>
									</tr>
									<tr id = "fourthPlace">
										<td id = "4"></td>
										<td id = "name4"></td>
										<td id = "pointValue4"></td>
									</tr>
									<tr id = "fifthPlace">
										<td id = "5"></td>
										<td id = "name5"></td>
										<td id = "pointValue5"></td>
									</tr>
									<tr id = "sixthPlace">
										<td id = "6"></td>
										<td id = "name6"></td>
										<td id = "pointValue6"></td>
									</tr>	
									<tr id = "seventhPlace">
										<td id = "7"></td>
										<td id = "name7"></td>
										<td id = "pointValue7"></td>
									</tr>	
									<tr id = "eighthPlace">	
										<td id = "8"></td>
										<td id = "name8"></td>
										<td id = "pointValue8"></td>
									</tr>
									<tr id = "ninthPlace">	
										<td id = "9"></td>
										<td id = "name9"></td>
										<td id = "pointValue9"></td>
									</tr>
									<tr id = "tenthPlace">	
										<td id = "10"></td>
										<td id = "name10"></td>
										<td id = "pointValue10"></td>
									</tr>
									<tr id = "eleventhPlace">	
										<td id = "11"></td>
										<td id = "name11"></td>
										<td id = "pointValue11"></td>
									</tr>
									<tr id = "twelfthPlace">
										<td id = "12"></td>
										<td id = "name12"></td>
										<td id = "pointValue12"></td>
									</tr>	
									<tr id = "thirteenthPlace">	
										<td id = "13"></td>
										<td id = "name13"></td>
										<td id = "pointValue13"></td>
									</tr>
									<tr id = "fourthteenthPlace">	
										<td id = "14"></td>
										<td id = "name14"></td>
										<td id = "pointValue14"></td>
									</tr>
									<tr id = "fifthteenthPlace">
										<td id = "15"></td>
										<td id = "name15"></td>
										<td id = "pointValue15"></td>
									</tr>	
									<tr id = "sixthteenthPlace">	
										<td id = "16"></td>
										<td id = "name16"></td>
										<td id = "pointValue16"></td>
									</tr>
									<tr id = "seventeenthPlace">	
										<td id = "17"></td>
										<td id = "name17"></td>
										<td id = "pointValue17"></td>
									</tr>
									<tr id = "eighteenthPlace">
										<td id = "18"></td>
										<td id = "name18"></td>
										<td id = "pointValue18"></td>
									</tr>
									<tr id = "nineteenthPlace">
										<td id = "19"></td>
										<td id = "name19"></td>
										<td id = "pointValue19"></td>
									</tr>	
									<tr id = "twentiethPlace">
										<td id = "20"></td>
										<td id = "name20"></td>
										<td id = "pointValue20"></td>
									</tr>	
									<tr id = "twentyfirstPlace">
										<td id = "21"></td>
										<td id = "name21"></td>
										<td id = "pointValue21"></td>
									</tr>	
									<tr id = "twentysecondPlace">
										<td id = "22"></td>
										<td id = "name22"></td>
										<td id = "pointValue22"></td>
									</tr>	
									<tr id = "twentythridPlace">
										<td id = "23"></td>
										<td id = "name23"></td>
										<td id = "pointValue23"></td>
									</tr>	
									<tr id = "twentyfourPlace">
										<td id = "24"></td>
										<td id = "name24"></td>
										<td id = "pointValue24"></td>
									</tr>
									<tr id = "twentyfivePlace">
										<td id = "25"></td>
										<td id = "name25"></td>
										<td id = "pointValue25"></td>
									</tr>
								</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<?php require 'Milestones.php'?>
		<?php require 'tasks.php'?>
        <?php require 'mytasks.php'?>
		<?php require 'profile.php'?>
        <?php require 'myStatistics.php'?>
	</body>
</html>

