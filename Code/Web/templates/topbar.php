<!-- topbar -->
<nav class="top-bar" data-topbar role="navigation">
	<ul class="title-area">
		<li class="name">
			<h1><a href="/">Henry</a></h1>
		</li>
		<li class="menu-icon">
			<!-- put icon here -->
		</li>
	</ul>

	<section class="top-bar-section">
		<ul class="left loginRequired" hidden>
			<li><a href="projects">My Projects</a></li>
			<li><a href="trophystore">Store</a></li>
			<!-- <li><a onclick="showMyTrophyStore()">Trophy Store</a></li> -->
			<!-- <li><a onclick="showMyTasksPage()">My Tasks</a></li> -->
			<!-- <li><a onclick="showMyStatsticsPage()">MyStatistics</a></li> -->
			<li><a data-reveal-id="preference-modal" href="#">User Preferences</a><li>
			<!-- add top bar links here> -->
		</ul>
		<ul class="right">
            <li class="notLoggedIn" id="loginButton"><a href="javascript:showLoginModal()">Login</a></li>
			<li class="loginRequired gamification" hidden><a id="currentPoints"></a></li>
			<li class="loginRequired" hidden><a id="currentUser"></a></li>
			<li class="loginRequired" hidden><a id="logoutButton" href="javascript:logout()">Logout</a></li>
			<!-- <li class="has-form">
				<div class="row collapse">
					<div class="small-8 columns">
						<input type="text" placeholder="search bar">
					</div>
					<div class="small-4 columns">
						<a href="#" class="button expand">Search</a>
					</div>
				</div>
			</li> -->
		</ul>
	</section>
</nav>
<div id="preference-modal" class="reveal-modal tiny" data-reveal>
	<div class="row collapse">
		<h1>Gamification</h1>
	</div>
	<div class="row collapse">
		<div class="small-8 colums">
			<span>Toggles bounty points and User leaderboard display</span>
		</div>
		<div class="small-4 columns">
			<div class="switch radius">
				<input id="gamification-switch" type="checkbox" name="testGroup">
				<label for="gamification-switch"></label>
			</div> 
		</div>
		<!-- <a class="close-reveal-modal">&#215;</a> -->
	</div>
</div>
