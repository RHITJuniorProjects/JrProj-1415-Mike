<html>
	<head>
		<title>Henry: Login</title>
		<?php require 'header.php';?>
	</head>
	<body>
		<?php require 'topbar.php';?>
		<div class="columns main-content">
			<h1>Henry: Login with Your Account</h1>
		</div>
		<div class="large-3 columns main-content left">
			Email: <input type="text" id="user">
			Password: <input type="password" id="pass">
			<input type="submit" value="Submit" onclick="getLoginData()">
			<span style="color:red" id="loginError" hidden>Incorrect Username or Password</span>
			<a href="register">Register an account</a>
		</div>
	</body>
</html>
