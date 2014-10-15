<html>
	<head>
		<title>Henry: Login</title>
		<?php require 'header.php';?>
	</head>
	<body>
		<?php require 'topbar.php';?>
		<div>
			<h2>Henry: Login</h2>
			<form id="logininput" onsubmit="login()">
				Username: <input type="text" id="user">
				Password: <input type="password" id="pwd">
				<input type="submit" value="Submit">
			</form>
		</div>
	</body>
</html>
