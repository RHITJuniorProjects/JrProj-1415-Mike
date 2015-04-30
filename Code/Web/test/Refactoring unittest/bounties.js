function Bounty(firebase){
	this.uid = firebase.key();
	this.__claimed = firebase.child('claimed');
	this.__description = firebase.child('description');
	this.__due_date = firebase.child('due_date');
	this.__hour_limit = firebase.child('hour_limit');
	this.__line_limit = firebase.child('line_limit');
	this.__name = firebase.child('name');
	this.__points = firebase.child('points');
}

Bounty.Types = ["Lines", "Hours"];

Bounty.Conditions = ["By Date", "Whenever"];

Bounty.makeTypeSelect = function(onselect){
	return makeSelect(Bounty.Types, Bounty.Types[0], onselect);
};

Bounty.makeConditionSelect = function(onselect){
	return makeSelect(Bounty.Conditions, Bounty.Conditions[0], onselect);
};

Bounty.prototype = {
	getName: function(callback){
		this.__name.on('value', function(snap){
			callback(snap.val);
		});
	},
	getType: function(callback){
		this.getHourLimit(function(hours){
			if(hours > 0){
				callback('Hours');
			} else {
				callback('Lines');
			}
		});
	},
	getCondition: function(callback){
		this.getDueDate(function(date){
			if(date === 'No Due Date'){
				callback('Whenever');
			} else {
				callback('By Date');
			}
		});
	},
	getClaiment: function(callback){
		this.__claimed.on('value', function(snap){
			callback(snap.val());
		});
	},
	getHourLimit: function(callback){
		this.__hour_limit.on('value', function(snap){
			callback(snap.val());
		});
	},
	getLineLimit: function(callback){
		this.__line_limit.on('value', function(snap){
			callback(snap.val());
		});
	},
	getName: function(callback){
		this.__name.on('value', function(snap){
			callback(snap.val());
		});
	},
	getPoints: function(callback){
		this.__points.on('value', function(snap){
			var v = snap.val();
			if(v === null){
				callback(0);
			}
			callback(v);
		});
	},
	getDueDate: function(callback){
		this.__due_date.on('value', function(snap){
			callback(snap.val());
		});
	},
	getRow: function(){
		var row = $('<div class="row">'),
			typeSpan = $('<span>'),
			tc = $('<div class="small-3 columns">'),
			condSpan = $('<span>'),
			coc = $('<div class="small-3 columns">'),
			claimSpan = $('<span>'),
			clc = $('<div class="small-3 columns">'),
			pointsSpan = $('<span>'),
			pc = $('<div class="small-3 columns">'),
			bounty = this;

		this.getType(function(type){
			if(type === 'Lines'){
				bounty.getLineLimit(function(lim){
					typeSpan.text(lim.toString() + ' Lines');
				});
			} else {
				bounty.getHourLimit(function(lim){
					typeSpan.text(lim.toString() + ' Hours');
				});
			}
		});

		this.getPoints(function(p){
			pointsSpan.html(p.toString() + ' Points');
		});

		this.getCondition(function(cond){
			if(cond === "Whenever"){
				condSpan.text('');
			} else {
				bounty.getDueDate(function(date){
					condSpan.text("By " + date);
				});
			}
		});

		this.getClaiment(function(claiment){
			claimSpan.text('claimed by ' + claiment);
		});
		row.append(
			tc.append(typeSpan),
			coc.append(condSpan),
			clc.append(claimSpan),
			pc.append(pointsSpan)
		);
		return row;
	}
}
