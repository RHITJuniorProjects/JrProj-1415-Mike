<html>
	<head>
		<title>Henry - Homepage</title>
		<?php require 'header.php';?>
	</head>
	<body class="wide">
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
				<a href="/register" class="button expand">Register</a>
			</div>
			<div class="small-4 columns">
				<a href="/login" class="button expand">Login</a>
			</div>
			<div class="small-4 columns">
				<a class="button expand">About</a>
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
				$('#one').attr('src','forest1.jpg');
				$('#two').attr('src','grass.JPG');
				$('#three').attr('src','water.jpg');
			});
		</script>
	</body>
</html>
