<html>
	<head>
		<title>Henry: Registering</title>
		<?php require 'header.php';?>
	</head>
	<body>
		<?php require 'topbar.php';?>
		<div>
			<h2>Henry: Register New Account</h2>
			<form id="registerinput" onsubmit="register()">
				Username: <input type="text" id="user">
				Password: <input type="password" id="pass">
				<input type="submit" value="Submit">
			</form>
		</div>
	</body>
</html>
