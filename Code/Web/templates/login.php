<html>
	<head>
		<title>Henry: Login</title>
		<?php require 'header.php';?>
	</head>
	<body>
		<?php require 'topbar.php';?>
		<div>
			<h1>Henry: Login with Your Account</h1>
			Username: <input type="text" id="user">
			Password: <input type="password" id="pass">
			<input type="submit" value="Submit" onclick="getLoginData()">
			<span style="color:red" id="loginerror" hidden>Incorrect Username or Password</span>
		</div>
	</body>
</html>
