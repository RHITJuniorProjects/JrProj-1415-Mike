<html>
	<head>
		<title>Henry - Homepage</title>
		<?php require 'header.php';?>
	</head>
	<body class="wide">
		<div id="pictures" class="main-content">
			<ul class="example-orbit" data-orbit>
			  <li>
			  	<div class="bg">
			    	<img src="forest1.jpg" alt="slide 1" />
			    </div>
			    <div class="orbit-caption">
			      Henry is a cross-platform user experience for a multi-user webscale application.
			    </div>
			  </li>
			  <!-- li>
              :a
              :q
			  	<div class="bg">
			    	<img src="forest2.jpg" alt="slide 2" />
			  	</div>
			    <div class="orbit-caption">
			      Users can create projects, tasks, milestones, and much more.
			    </div>
			  </li>-->
			  <li>
 				<div class="bg">
			    	<img src="water.jpg" alt="slide 2" />
				</div>
			    <div class="orbit-caption">
			      Henry lets teams work together for the future.
			    </div>
			  </li>
			  <li>
 				<div class="bg">
			    	<img src="grass.JPG" alt="slide 3" />
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
				var orbit = $('li');
				function adjust(){
					var height = $(window).height()-150;
					if(height > 0){
						footer.css({'top':height+'px'});
					}
					orbit.height({'height':height-45+'px'});
				}
				if(!addedResize){
					$(window).bind("resize",adjust);
				}
				adjust();
			});
		</script>
	</body>
</html>
