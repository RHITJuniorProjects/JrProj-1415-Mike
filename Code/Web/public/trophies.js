
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
	button.click(function() {
		buyTrophy(name.html(), desc.html(), cost.html(), image.html(), trophy);
	});
	buttonCell.append(button);
	
	return row;
};

Trophy.prototype.getUserTableRow =  function () {
	var row = $('<tr class="trophy-row">');//for later: data-reveal-id="trophy-modal">');
	var name = $('<td>');
	var desc = $('<td>');
	var image = $('<td>');
	var trophy = this;
	var modal = $('#trophy-modal');

	row.append(name, desc, image);
	this.getName(function (nameStr) {
		name.html(nameStr);
	});
	this.getDescription(function (descriptionStr) {
		desc.html(descriptionStr);
	});
	this.getImage(function (imageStr) {
		image.html("<img src=" + imageStr + " height=42 width=42>");
	});

	return row;
};

function buyTrophy(name, desc, cost, img, trophy) {
	user.getBountyPoints(function(pts) {
	var haveTrophy = false;
		for(var i = 0; i < userTrophies.length; i++) {
			if(userTrophies[i].uid == trophy.uid){
				haveTrophy = true;
				break;
			}
		}
		if (!haveTrophy){
				if(pts >= cost) {
					user.setPoints(pts - cost);
					 firebase.child("users/" + user.uid + "/trophies/" + trophy.uid).push();
					 firebase.child("users/" + user.uid + "/trophies/" + trophy.uid).set(name, getTrophies);
				}
				else {
					console.log("not enough points");
				}
			}
	});
	getTrophies();
}

function getTrophies() {
	userTrophies = [];
	var $panel = $('#trophy-store-rows');
	$panel.children().remove();
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
Trophy.prototype.addTrophy = function(){
	var trophyName = $("#trophyName").val();
	var trophyDescription = $("#trophyDescription").val();
	var trophyCost = $("#trophyCost").val();
	var trophyImage = $("#trophyImage").val();

	if (!trophyName || !trophyDescription || !trophyCost || !trophyImage) {
		$("#trophy-error").show();
		return;
	} else {
		$("#trophy-error").hide();
	}

	var costOfTrophy = Number(trophyCost);

	firebase.child("trophies").push({
		'cost': trophyCost,
		'description': description,
		'image': trophyImage,
		'name' : trophyName
	});

}

