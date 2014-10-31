<html>
	<head>
		<title>Henry: Registering</title>
		<?php require 'header.php';?>
	</head>
	<body>
		<?php require 'topbar.php';?>
		<div class="columns main-content">
			<h1>Henry: Register New Account</h1>
			Email: <input type="text" id="user">
			Password: <input type="password" id="pass">
			Github User Name: <input type="text" id="githubuser">
			Name: <input type="text" id="name">
			<input type="submit" value="Submit" onclick="register()">
			<span style="color:red" id="registerError" hidden>Error registering, make sure all the fields are filled in.</span>
			<span style="color:red" id="emailError" hidden>Email already in use.</span>
		</div>
	</body>
</html>
