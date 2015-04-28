function Table(factory, firebase) {
	this.__factory = factory;
	this.__firebase = firebase;
};

Table.prototype = {
	get: function (uid) {
		return this.__factory(this.__firebase.child(uid));
	},
	onItemAdded: function (callback) {
		var table = this;
		this.__firebase.orderByChild("name").on('child_added', function (snap) {
			var val = table.__factory(snap.ref());
			callback(val);
		});
	},
	onItemRemoved: function (callback) {
		var table = this;
		this.__firebase.on('child_removed', function (snap) {
			var val = table.__factory(snap.ref());
			callback(val);
		});
	},
	off: function () {
		this.__firebase.off();
	},
	add: function (id) {
		var ref;
		if (id) {
			ref = this.__firebase.child(id);
		} else {
			ref = this.__firebase.push();
		}
		return this.__factory(ref);
	},
	getSelect: function (onselect, defaultId) {
		var select = $('<select>');
		if (onselect) {
			var table = this;
			select.change(function () {
				onselect(table.get(select.val()));
			});
		}
		this.onItemAdded(function (item) {
			select.prepend(item.getOption());
			select.val(defaultId);
		});
		return select;
	},
	sort: function (comparator) {

	}
};

function ReferenceTable(referencedTable, firebase) {
	this.__factory = function (ref) {
		return referencedTable.get(ref.key());
	};
	this.__firebase = firebase;
}

ReferenceTable.prototype = Table.prototype;
