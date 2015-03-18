function Trophy(firebase) {
	this.__firebase = firebase;
	this.uid = firebase.key();
	this.__name = firebase.child('name');
	this.__description = firebase.child('description');    
	this.__image = firebase.child('image')
	this.__cost = firebase.child('cost');
}

Trophy.prototype = {
	getName: function (callback) {
		this.__name.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getDescription: function (callback) {
		this.__description.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getCost: function (callback) {
		this.__cost.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getImage: function(callback) {
		this.__image.on('value', function (dat) {
			callback(dat.val());
		});
	}
};
Trophy.prototype.getTableRow =  function () {
	var row = $('<tr class="trophy-row">');//for later: data-reveal-id="trophy-modal">');
	var name = $('<td>');
	var desc = $('<td>');
	var cost = $('<td>');
	var image = $('<td>');
	var buttonCell = $('<td>');
	var trophy = this;
	var modal = $('#trophy-modal');
	console.log(this.uid);

	row.append(name, desc, cost, image, buttonCell);
	this.getName(function (nameStr) {
		name.html(nameStr);
	});
	this.getDescription(function (descriptionStr) {
		desc.html(descriptionStr);
	});
	this.getCost(function (costStr) {
		cost.html(costStr);
	});
	this.getImage(function (imageStr) {
		image.html("<img src=" + imageStr + " height=42 width=42>");
	});

	var button = $('<button>');
	button.html("Buy");
	button.attr("id", this.uid);
	button.click(function() {
		buyTrophy(name.html(), desc.html(), cost.html(), image.html(), $(this).attr("id"));
	});
	buttonCell.append(button);
	
	return row;
};

function buyTrophy(name, desc, cost, img, tid) {
	user.getBountyPoints(function(pts) {
		console.log(pts)
		for(var i = 0; i < userTrophies.length; i++) {
			if(userTrophies[i].uid == tid) break;
			else if(i === userTrophies.length - 1) {
				if(pts >= cost) {
					user.setPoints(pts - cost);
					user.__trophies.push({
						// value: name
						cost: cost,
						name: name,
						image: img,
						description: desc
					});
				}
				else console.log("not enough points");
			}
		}
		console.log("you already have that trophy");
	});
	console.log(tid);
}

function getTrophies() {
	// console.log("get called");
	var $panel = $('#trophy-store-rows');
	$panel.children().remove();
	//var userTrophies = [];
	user.__trophies.orderByChild("name").on('child_added', function (snap) {
			var val = new Trophy(snap.ref());
			userTrophies[userTrophies.length] = val;
		});
	trophies.onItemAdded(function(trophy){
		var arrayLength = userTrophies.length;
		var needs = true;
		for (var i = 0; i < arrayLength; i++) {
			if(userTrophies[i].uid == trophy.uid) {

				needs = false;
				break;
			}
			
		}
		if (Boolean(needs)) {
			$panel.append(trophy.getTableRow());
		}              
	});
}
