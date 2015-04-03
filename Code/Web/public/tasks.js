function Task(firebase) {
	this.__firebase = firebase;
	this.__root = firebase.root();
	this.uid = firebase.key();
	this.__name = firebase.child('name');
	this.__description = firebase.child('description');
	this.__assigned_user = firebase.child('assignedTo');
	this.__custom_categories = firebase.parent().parent().parent().parent().child('custom_categories');
	this.__category = firebase.child('category');
	this.__status = firebase.child('status');
	this.__lines_of_code = firebase.child('total_lines_of_code');
	this.__due_date = firebase.child('due_date');
	// this.__is_completed = firebase.child('is_completed');
	this.__hour_estimate = firebase.child('updated_hour_estimate');
	this.__bounties = firebase.child('bounties');
	this.__members = firebase.parent().parent().parent().parent().child('members');
};

Task.Statuses = [
	'New',
	'Implementation',
	'Testing',
	'Verify',
	'Regression',
	'Closed'
];

Task.prototype = {
	getBounties: function(){
		return new Table(
			function(ref){
				return new Bounty(ref);
			},
			this.__bounties
		);
	},
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
	getAssignedUser: function (callback) {
		this.__assigned_user.on('value', function (dat) {
			var user = users.get(dat.val());
			callback(user);
		});
	},
	getCustomCategories: function (callback) {
		this.__custom_categories.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getCategory: function (callback) {
		this.__category.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getStatus: function (callback) {
		this.__status.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getDueDate: function (callback) {
		this.__due_date.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getBountyPoints: function (callback) {
		var totalPoints = 0;
		callback(0);
		this.getBounties().onItemAdded(function(bounty){
			if(bounty === null){
				return;
			}
			bounty.getPoints(function(points){
				totalPoints += points;
				callback(totalPoints);
			});
		});
	},
	// getFlag: function (callback) {
	//     this.__is_completed.on('value', function (dat) {
	//         callback(dat.val());
	//     });
	// },
	getTotalLinesOfCode: function (callback) {
		this.__total_lines_of_code.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getTimeEstimate: function (callback) {
		this.__hour_estimate.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getDescriptionDiv: function () {
		var task = $('<div>'),
			nameH4 = $('<h4>'),
			descSpan = $('<span>'),
			catSpan = $('<span>');
		this.getName(function (name) {
			nameH4.text(name);
		});
		this.getDescription(function (description) {
			descSpan.text(description);
		});
		this.getCategory(function (cat) {
			catSpan.text(cat);
		});
		task.append(nameH4, descSpan, catSpan);
		return task;
	},
	getTableRow: function () {
		var row = $('<tr class="task-row" data-reveal-id="task-modal">');
		var name = $('<td>');
		var desc = $('<td>');
		var cat = $('<td>');
		var cats = null;
		var stat = $('<td>');
		var user = $('<td>');
		var due = $('<td>');
		// var flag = $('<td>');
		var hoursEstimate = $('<td>');
		var bountyPoints = $('<td class="gamification">');
		var task = this;
		var modal = $('#task-modal');

		row.append(name, desc, user, cat, stat, due, hoursEstimate, bountyPoints);
		this.getName(function (nameStr) {
			name.html(nameStr);
		});
		this.getDescription(function (descriptionStr) {
			desc.html(descriptionStr);
		});
		this.getAssignedUser(function (assignedUser) {
			assignedUser.getName(function (name) {
				user.html(name);
			});
		});
		this.getCustomCategories(function (categories) {
			if(categories != null) {
				cats = Object.keys(categories);
			} else {
				cats = [];
			}
		});
		this.getCategory(function (categoryStr) {
			cat.html(categoryStr);
		});
		this.getStatus(function (statStr) {
			stat.html(statStr);
		});
		this.getDueDate(function (dueDate) {
			due.html(dueDate);
		});
		// this.getFlag(function (f) {
		//     if(f){
		//         flag.html('<img src="completed.png" />');
		//     } else {
		//         flag.html('<img src="notcompleted.png" />');
		//     }
		// });
		this.getTimeEstimate(function (hour_estimateStr) {
			hoursEstimate.html(hour_estimateStr);
		});
		this.getBountyPoints(function (bounty_point) {
			bountyPoints.html(bounty_point);
		});
		row.click(function () {
			modal.children().remove();
			task.__firebase.once('value', function (snap) {

				var nav = $('<nav class="breadcrumbs gamification">'),
					editA = $('<a class="current" href="#">Edit Task</a>'),
					bountyA = $('<a href="#">View Bounties</a>'),
					check = $('#gamification-switch');

				if(!check.is(':checked')){
					nav.hide();
				}

				nav.append(editA,bountyA);

				// make edit page
				var	taskEdit = $('<div id="task-edit">'),
					vals = snap.val(),
					nameInput = $('<input type="text" value="' + vals.name + '">'),
					descriptionInput = $('<textarea>' + vals.description + '</textarea>'),
					userSelect = new ReferenceTable(users, task.__members).getSelect(function (user) {
						selectedUser = user;
					}, vals.assignedTo),
					newCategory = $('<option id="newCategory" value="Add Category">Add Category</option>');
					categoriesSelect = makeSelect(defaultCategories.concat(cats), vals.category).append(newCategory);
					categoriesText = $('<input type="text">'),
					statusSelect = makeSelect(Task.Statuses, vals.status),
					dueInput = $('<input type="date" placeholder="yyyy-mm-dd" value="' + vals.due_date + '">'),
					// flagInput = makeSelect(Task.Flags, String(vals.is_completed)),
					estHoursInput = $('<input type="text" value="' + vals.updated_hour_estimate + '">'),
					eNameH = $('<h3>'),
					bNameH = $('<h3>'),
					submit = $('<input class="button" value="Submit" />'),
					taskError = $('<div id="task-error" class="my-error" hidden>All fields must be specified</div>');
				if(vals.bounties != undefined) {
					bountyPoints = $('<input type="text" value="' + vals.bounties.points + '">');
				} else {
					bountyPoints = $('<input type="text" value="' + 0 + '">');
				}
				task.getName(function (name) {
					eNameH.text('Edit Task: ' + name);
					bNameH.text('Bounties for Task: ' + name);
				});
				$(categoriesText).hide();
				taskEdit.append(
					eNameH,
					label(nameInput, 'Name'),
					label(descriptionInput, 'Description'),
					label(userSelect, 'User'),
					label(categoriesSelect, 'Category'),
					categoriesText,
					label(statusSelect, 'Status'),
					label(dueInput, 'Due Date'),
					// label(flagInput, 'Is Complete'),
					label(estHoursInput, 'Estimated Hours'),
					//label(bountyPoints, 'Bounty Points')
					submit,
					taskError
				);

				// make bounty page
				var	taskBounties = $('<div id="task-bounties">'),
					list = $('<div>'),
					newBounty = $('<div id="row">'),
					numc = $('<div class="small-2 columns">'),
					num = $('<input type="number" placeholder="amount">'),
					addc = $('<div class="small-2 columns">'),
					add = $('<input type="button" class="button small" value="Add Bounty">'),
					datec = $('<div class="small-2 columns">'),
					date = $('<input type="date" placeholder="yyyy-mm-dd">'),
					typec = $('<div class="small-2 columns">'),
					typeS = Bounty.makeTypeSelect(),
					condc = $('<div class="small-2 columns">'),
					condS = Bounty.makeConditionSelect(function(){
						if(condS.val() === "Whenever"){
							datec.hide();
						} else {
							datec.show();
						}
					}),
					pointsc = $('<div class="small-2 columns">'),
					points = $('<input type="number" placeholder="points">');

					addc.append(add);
					numc.append(num);
					typec.append(typeS);
					datec.append(date);
					condc.append(condS);
					pointsc.append(points);
					newBounty.append(
						addc,
						numc,
						typec,
						condc,
						datec,
						pointsc
					);
					date.click(function(){
						date.fdatepicker({format: 'yyyy-mm-dd'});
						date.fdatepicker('show');
					});
				list.append(newBounty);
				task.getBounties().onItemAdded(function(bounty){
					list.prepend(bounty.getRow());
				});

				taskBounties.append(bNameH,list);
				taskBounties.hide();
				modal.append(nav,taskEdit,taskBounties);
				editA.click(function(){
					editA.attr('class','current');
					bountyA.attr('class','');
					taskEdit.show();
					taskBounties.hide();
				});

				bountyA.click(function(){
					bountyA.attr('class', 'current');
					editA.attr('class', '');
					taskEdit.hide();
					taskBounties.show();
				});

				add.click(function(){
					var msg = {
						claimed:"None",
						points:Number(points.val()),
						name:"Name",
						description:'Description'
					};
					if(typeS.val() === "Lines"){
						msg.line_limit = Number(num.val());
						msg.hour_limit = 'None';
					} else {
						msg.hour_limit = Number(num.val());
						msg.line_limit = 'None';
					}

					if(condS.val() === "Whenever"){
						msg.due_date = 'No Due Date';
					} else {
						msg.due_date = date.val();
					}
					task.__firebase.child('bounties').push(msg);
				});

				modal.keypress(function (e) {
					if (e.which == 13) {
						submit.click();
					}
				});
				newCategory.click(function() {
					$(categoriesSelect).hide();
					$(categoriesText).show();
				});
				dueInput.click(function () {
					dueInput.fdatepicker({format: 'yyyy-mm-dd'});
					dueInput.fdatepicker('show');
				});
				submit.click(function () {

					if(!nameInput.val() || !descriptionInput.val() || !userSelect.val() ||
						!dueInput.val() || !categoriesSelect.val() || !statusSelect.val() || !estHoursInput.val()){
						$("#task-error").show();
						return;
					} else {
						$("#task-error").hide();
					}

					var estHours = Number(estHoursInput.val());

					if(isNaN(estHours) || !isFinite(estHours) || estHours < 0){
						$("#task-error").show();
						return;
					}

					if(!dueInput.val().match(/^(19|20)[0-9][0-9][-\\/. ](0[1-9]|1[012])[-\\/. ](0[1-9]|[12][0-9]|3[01])$/)){
						$("#task-error").show();
						return;
					}

					var categoryName = null;
					if($(categoriesSelect).is(":visible")) {
						categoryName = categoriesSelect.val();
					} else {
						categoryName = categoriesText.val();
					}

					task.__firebase.update({
						name: nameInput.val(),
						description: descriptionInput.val(),
						assignedTo: userSelect.val(),
						due_date: dueInput.val(),
						category: categoryName,
						updated_hour_estimate: estHours
					});
					var commit = {
						added_lines_of_code : 0,
						hours : 0,
						message : "direct change to task from UI",
						milestone : selectedMilestone.uid,
						project : selectedProject.uid,
						removed_lines_of_code : 0,
						status : statusSelect.val(),
						task : task.uid,
						timestamp : (new Date).getTime(),
						updated_hour_estimate : Number(estHoursInput.val()),
						user : userSelect.val()
					};

					task.__root.child("commits/" + selectedProject.uid).push(commit);
					
					var cate = {};
					cate[categoryName] = true;
					selectedProject.__custom_categories.update(cate);
					$("#task-modal").foundation('reveal', 'close');
				});
			});
		});

		return row;
	},
	setUser: function (user) {
		var id = user;
		if (typeof id != 'string') {
			id = user.uid;
		}
		this.__assigned_user.set(id);
	},
	setCustomCategories: function (cat) {
		this.__custom_categories.update({cat : true});
	},
	setCategory: function (cat) {
		this.__category.set(cat);
	},
	setDescription: function (desc) {
		this.__description.set(desc);
	},
	setEstimatedHours: function (hours) {
		this.__original_hour_estimate.set(hours);
	},
	setName: function (name) {
		this.__name.set(name);
	},
	setDueDate: function (dueDate) {
		this.__due_date.set(dueDate);
	},
	setBountyPoints: function (points) {
		this.__bountiesPoints.set(points);
	},
	off: function () {
		this.__firebase.off();
	}
};

function newTask() {
	var cats;
	var nameInput = $('<input type="text">'),
		descriptionInput = $('<textarea>'),
		userSelect = users.getSelect(function (user) {
			selectedUser = user;
		}, user.uid),
		newCategory = $('<option id="newCategory" value="Add Category">Add Category</option>'),
		categoriesSelect = makeSelect(defaultCategories.concat(cats), "Feature").append(newCategory),
		categoriesText = $('<input type="text" hidden=true>'),
		statusSelect = makeSelect(Task.Statuses, "New"),
		estHoursInput = $('<input type="text">'),
		nameH = '<h3>Add New Task</h3>',
		submit = $('<input class="button" value="Add Task" />'),
		modal = $('#task-modal'),
		dueInput = $('<input type="text" placeholder="yyyy-mm-dd">'),
		taskError = $('<div id="task-error" class="my-error" hidden>All fields must be specified</div>');

	$('#task-modal').foundation('reveal', 'open');
	selectedProject.getCustomCategories(function(categories){
		if(categories){
			cats = Object.keys(categories);
		} else {
			cats = [];
		}
		modal.children().remove();
		$(categoriesText).hide();
		modal.append(
			nameH,
			label(nameInput, 'Name'),
			label(descriptionInput, 'Description'),
			label(userSelect, 'User'),
			label(categoriesSelect, 'Category'),
			categoriesText,
			label(statusSelect, 'Status'),
			label(estHoursInput, 'Estimated Hours'),
			label(dueInput, "Due Date"),
			submit,
			taskError
		);
		newCategory.click(function() {
			$(categoriesSelect).hide();
			$(categoriesText).show();
		});
		dueInput.click(function () {
			dueInput.fdatepicker({format: 'yyyy-mm-dd'});
			dueInput.fdatepicker('show');
		});
		modal.keypress(function (e) {
			if (e.which == 13) {
				submit.click();
			}
		});
		submit.click(function () {

			if(!nameInput.val() || !descriptionInput.val() || !userSelect.val() || !dueInput.val() ||
				!categoriesSelect.val() || !statusSelect.val() || !estHoursInput.val()){
				$("#task-error").show();
				return;
			} else {
				$("#task-error").hide();
			}

			var estHours = Number(estHoursInput.val());

			if(isNaN(estHours) || !isFinite(estHours) || estHours < 0){
				$("#task-error").show();
				return;
			}

			if(!dueInput.val().match(/^(19|20)[0-9][0-9][-\\/. ](0[1-9]|1[012])[-\\/. ](0[1-9]|[12][0-9]|3[01])$/)){
				$("#task-error").show();
				return;
			}
			var categoryName = null;
			if($(categoriesSelect).is(":visible")) {
				categoryName = categoriesSelect.val();
			} else {
				categoryName = categoriesText.val();
			}
			if(categoryName){
				var category = {};
				category[categoryName] = true;
				selectedProject.__custom_categories.update(category);
			}
			selectedMilestone.__tasks.push({
				name: nameInput.val(),
				description: descriptionInput.val(),
				assignedTo: userSelect.val(),
				category: categoryName,
				status: statusSelect.val(),
				original_hour_estimate: estHours,
				due_date: dueInput.val() 
			});
			$("#task-modal").foundation('reveal', 'close');
		});

	});
}

function taskStatistics(){
	$('#taskContainer').foundation('reveal', 'open');
	drawTaskStuff(selectedProject.uid, selectedMilestone.uid, firebase);
}
