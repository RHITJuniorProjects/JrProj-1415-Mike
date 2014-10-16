
/* this file contains classes and utility functions that are used everywhere on the website */
var firebase = new Firebase("https://henry-production.firebaseIO.com");

// table object manages a table of values in the database, use get to get objects from the database
// by uid
function Table(factory,firebase){
	this.__factory = factory;
	this.__firebase = firebase;
};

Table.prototype = {
	get:function(uid){
		return this.__factory(this.__firebase.child(uid));
	},
	add:function(obj){

	}
};

function Project(firebase){
	this.firebase = firebase;
	this.uid = firebase.name();
	this.__name = firebase.child('name');
	this.__milestones = firebase.child('milestones');
};

Project.prototype = {
	getName:function(callback){
		this.__name.once('value',function(dat){
			callback(dat.val());
		});
	}
};

function login(){
	var user = document.getElementById("user").value;
	var pass = document.getElementById("pwd").value;

	firebase.authWithPassword({
		"email": user,
		"password": pass
	}, function(error, authData) {
		if (error === null) {
			console.log("User ID: " + authData.uid + ", Provider: " + authData.provider);
		} else {
			console.log("Error authenticating user:", error);
		}
	});;
};

var projects = new Table(function(fb){ return new Project(fb);},firebase.child('projects'));
