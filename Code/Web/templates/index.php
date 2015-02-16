<html>
	<head>
		<title>Henry - Homepage</title>
		<?php require 'header.php';?>
	</head>
	<body class="wide">
        <?php require 'topbar.php'; ?>
		<div class="main-box">
			<br />
			<br />
			<br />
			<div>
			<h1>Henry</h1>
			<div class="centereddiv">
			Henry is a smart project-management tool through Git using Firebase.<br/>It is an integrated multi-platform application that is available on Android, iOS, and web browser.
			</div>
			</div>
			<div class="filler">
			</div>
			<div id="buttonWrapper">
				<div class="small-4 columns">
	                <button data-reveal-id="myRegisterModal" class="expand">Register</button>
	                <div id="myRegisterModal" class="reveal-modal" data-reveal>
	                    <h2>Register</h2> <br />
	                    <form action="" onsubmit="register(); return false;">
	                        <label for="registerUser">Email:</label> <input type="text" id="registerUser" />
	                        <label for="registerPass">Password:</label> <input type="password" id="registerPass" />
	                        <label for="registerPassCheck">Retype password:</label> <input type="password" id="registerPassCheck" />
	                        <label for="name">Name:</label> <input type="text" id="name"><br />
	                        <input id="registerSubmit" class="button" type="submit" value="Register" />
	                        <div id="passwordError" class="my-error" hidden>Please check that the passwords are the same.</div>
	                        <div id="registerError" class="my-error" hidden>Error registering, make sure all the fields are filled in.</div>
	                        <div id="emailError" class="my-error" hidden>Email already in use.</div>
	                    </form>
	                </div>
				</div>
				<div class="small-4 columns">
					<button data-reveal-id="myLoginModal" class="expand">Login</button>
	                <div id="myLoginModal" class="reveal-modal" data-reveal>
	                	<h2>Login</h2> <br />
	                    <form action="" onsubmit="getLoginData(); return false;">
	                	    <label for="loginUser">Email:</label> <input type="text" id="loginUser" />
	                	    <label for="loginPass">Password:</label> <input type="password" id="loginPass" />
	                	    <input id="loginSubmit" class="button" type="submit" value="Login" />
	                        <div id="loginError" class="my-error" hidden>Incorrect Username or Password</div>
	                     </form>
	                </div>
				</div>
				<div class="small-4 columns">
					<a href="https://github.com/RHITJuniorProjects/JrProj-1415-Mike/blob/master/Code/Platform/Installer/Windows/Henry_Installer.msi?raw=true">
					<button class="expand">Download</button></a>
	               <!--  <div id="myAboutModal" class="reveal-modal" data-reveal>
	                    <h2>About</h2> <br />
	                    <p>
	                        Henry is a service that provides a simple way to create and manage projects and project tasks.<br>
	                        Register or login to begin!
	                    </p>
	                    <div class="button" onclick="$('#myAboutModal').foundation('reveal', 'close');">Close</div>
	                </div> -->
				</div>
				<div class="small-4 columns">
					<a href="https://github.com/RHITJuniorProjects/JrProj-1415-Mike/wiki">
						<button class="expand">Documentation</button>
					</a>
				</div>
		</div>
		</div>
		<div class="main-box lightblue">
			<br/>
			<h1>What Henry has to offer.</h1>
			<div class="centereddiv">
				<ol>
					<li> Scalability
						From small projects to large ones, Henry can manage both effectively and efficiently, with as little overhead as possible.
					</li>
					<li>
						Cross-platform support
						Henry is available on multiple devices, customized to make the experience the best for each platform.
					</li>
					<li>
						Integration with Git
						Your commits will be linked to the specific task the commit is for to have even more traceability.
					</li>
					<li>
						Metrics
						Metrics are instantly generated and updated that will give a complete snapshot of a project and its milestones.
					</li>	
				</ol> 
			</div>
		</div>
		<div class="main-box gray">
			<br/>
			<h1>What does Henry use</h1>
			<div class="centereddiv">
				It uses Firebase to update at suprising speeds. <br />

				Firebase allows storage and complete synchronization of data in realtime to make the site as fast as possible.
			</div>
		</div>
		<div class="main-box">
			<br/>
			<h1>Who created Henry</h1>
			<div class="centereddiv">
				A class of undergraduate students at Rose-Hulman Institute of Technology created this application to be used by future students.
				<br />
				It was created for the client Mike MacDonald, a representative of Firebase.
			</div>
		</div>
	</body>
</html>
