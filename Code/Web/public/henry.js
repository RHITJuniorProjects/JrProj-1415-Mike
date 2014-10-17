
/* this file contains classes and utility functions that are used everywhere on the website */
var firebase = new Firebase("https://henry-production.firebaseIO.com");

// table object manages a table of values in the database, use get to get objects from the database
// by uid
function Table(factory,firebase){
	this.__onChildAdded = null;
	this.__onChildRemoved = null;
	this.__factory = factory;
	this.__firebase = firebase;
}

Table.prototype = {
	get:function(uid){
		return this.__factory(this.__firebase.child(uid));
	},
	onItemAdded:function(callback){
		if(!this.__onChildAdded){
			this.__onChildRemoved = [];
			this.__firebase.on('child_added',function(snap){
				var val = this.__factory(snap.val());
				this.__onChildAdded.forEach(function(callback){
					callback(val);
				});
			});
		}
		this.__onChildAdded.push(callback);
	},
	onItemRemoved:function(callback){
		if(!this.__onChildRemoved){
			this.__onChildRemoved = [];
			this.__firebase.on('child_removed',function(snap){
				var val = this.__factory(snap.val());
				this.__onChildRemoved.forEach(function(callback){
					callback(val);
				});
			});
		}
		this.__onChildRemoved.push(callback);
	}
};

var users = new Table(function(fb){return new User(fb);},firebase.child('users'));

function User(firebase){
	this.firebase = firebase;
	this.uid = firebase.name();
	this.__name = firebase.child('name');
	this.__email = firebase.chile('email');
}

User.prototype = {
	getName:function(callback){
		this.__name
	}

};

function Project(firebase){
	this.firebase = firebase;
	this.uid = firebase.name();
	this.__name = firebase.child('name');
	this.__milestones = firebase.child('milestones');
}

Project.prototype = {
	getName:function(callback){
		this.__name.once('value',function(dat){
			callback(dat.val());
		});
	}
};

var projects = new Table(function(fb){ return new Project(fb);},firebase.child('projects'));
