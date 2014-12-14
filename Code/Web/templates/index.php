<html>
	<head>
		<title>Henry - Homepage</title>
		<?php require 'header.php';?>
	</head>
	<body class="wide">
        <?php require 'topbar.php'; ?>
		<div id="title">
			<h1 id="henry">Henry</h1>
		</div>
		<div id="pictures" class="main-content">
			<ul class="example-orbit" data-orbit>
			  <li>
			  	<div class="bg">
			    	<!--<img src="forest1.jpg" alt="slide 1" />-->
			    	<img id="one" alt="slide 1" />
				</div>
			    <div class="orbit-caption">
			      Henry is a cross-platform user experience for a multi-user webscale application.
			    </div>
			  </li>
			  <li>
 				<div class="bg">
			    	<!--<img src="grass.JPG" alt="slide 3" />-->
			    	<img id="two" alt="slide 2" />
				</div>
			    <div class="orbit-caption">
			      Henry is a project management tool.
			    </div>
			  </li>
			  <li>
 				<div class="bg">
			    	<!--<img src="grass.JPG" alt="slide 3" />-->
			    	<img id="three" alt="slide 3" />
				</div>
			    <div class="orbit-caption">
			      Henry is a project management tool.
			    </div>
			  </li>
			</ul>
		</div>
		<div id="footer" class="row panel">
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
				<button data-reveal-id="myAboutModal" class="expand">About</button>
                <div id="myAboutModal" class="reveal-modal" data-reveal>
                    <h2>About</h2> <br />
                    <p>
                        Henry is a service that provides a simple way to create and manage projects and project tasks.<br>
                        Register or login to begin!
                    </p>
                    <div class="button" onclick="$('#myAboutModal').foundation('reveal', 'close');">Close</div>
                </div>
			</div>
		</div>
		<script>
			var addedResize = false;
			$(window).bind("load",function(){
				var footer = $('#footer');
				var orbit = $('#pictures');
				function adjust(){
					var height = $(window).height()-250;
					if(height > 0){
						footer.css({'top':height+'px'});
					}
					orbit.height({'height':height-45+'px'});
				}
				if(!addedResize){
					$(window).bind("resize",adjust);
				}
				adjust();
				var one = $('#one'),
					two = $('#two'),
					three = $('#three');
				one.load(function(){
					two.attr('src','grass.JPG');
				});
				two.load(function(){
					three.attr('src','water.jpg');
				});
				one.attr('src','forest1.jpg');
			});
		</script>
	</body>
</html>
