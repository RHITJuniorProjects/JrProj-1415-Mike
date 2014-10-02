
/* this file contains classes and utility functions that are used everywhere on the website */

var database = new Firebase('https://shining-fire-545.firebaseapp.com');

function Table(name){
	this.entry = database.get(name);
}

Table.

function User(id){

}

var users = new Table('users');
