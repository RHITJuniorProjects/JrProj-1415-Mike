
var users,
	username,
	userList;

function submitName(){
	users.push(username.value);
	username.value = '';
}

function addUser(username){
	var li = document.createElement('li');
	li.innerHTML = username;
	userList.appendChild(li);
}

window.onload = function(){
	users = new Firebase('https://shining-fire-545.firebaseio.com');
	console.log('LOADERD');
	username = document.getElementById('username');
	userList = document.getElementById('user-list');
	users.on('child_added',function(message){
		addUser(message.val());
	});
}
