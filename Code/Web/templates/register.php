<html>
	<head>
		<title>Henry: Registering</title>
		<?php require 'header.php';?>
	</head>
	<body>
		<?php require 'topbar.php';?>
		<div>
			<h1>Henry: Register New Account</h1>
			Username: <input type="text" id="user">
			Password: <input type="password" id="pass">
			<input type="submit" value="Submit" onclick="register()">
			<span style="color:red" id="registerError" hidden>Email already in use</span>
		</div>
	</body>
</html>
