function MyTasks(firebase) {
	Task.call(this,firebase);
};

MyTasks.prototype = Object.create(Task.prototype);

MyTasks.prototype.getTableRow =  function () {
	var row = $('<tr class="task-row" data-reveal-id="task-modal">');
	var name = $('<td>');
	var desc = $('<td>');
	var cat = $('<td>');
	var cats = null;
	var stat = $('<td>');
	var userTD = $('<td>');
	var due = $('<td>');
	var hoursEstimate = $('<td>');
	var bountyPoints = $('<td class="gamification">');
	var task = this;
	var modal = $('#task-modal');

	row.append(name, desc, userTD, cat, stat, due, hoursEstimate, bountyPoints);
	this.getName(function (nameStr) {
		name.html(nameStr);
	});
	this.getDescription(function (descriptionStr) {
		desc.html(descriptionStr);
	});
	this.getAssignedUser(function (assignedUser) {
		assignedUser.getName(function (name) {
			userTD.html(name);
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
				bountyA = $('<a href="#">View Bounties</a>');

			var check = $('#gamification-switch');
			if(!check.is(':checked')){
				nav.hide();
			}

			nav.append(editA,bountyA);

			// make edit page
			var	taskEdit = $('<div id="task-edit">'),
				vals = snap.val(),
				nameInput = $('<input type="text" value="' + vals.name + '">'),
				descriptionInput = $('<textarea>' + vals.description + '</textarea>'),
				newCategory = $('<option id="newCategory" value="Add Category">Add Category</option>');
				categoriesSelect = makeSelect(defaultCategories.concat(cats), vals.category).append(newCategory);
				categoriesText = $('<input type="text">'),
				statusSelect = makeSelect(Task.Statuses, vals.status),
				dueInput = $('<input type="date" placeholder="yyyy-mm-dd" value="' + vals.due_date + '">'),
				// flagInput = makeSelect(Task.Flags, String(vals.is_completed)),
				estHoursInput = $('<input type="text" value="' + vals.updated_hour_estimate + '">'),
				eNameH = $('<h3>'),
				bNameH = $('<h3>'),
				submit = $('<input class="button" value="Edit Task" />'),
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
				label(categoriesSelect, 'Category'),
				categoriesText,
				label(statusSelect, 'Status'),
				label(dueInput, 'Due Date'),
				label(estHoursInput, 'Estimated Hours'),
				//label(bountyPoints, 'Bounty Points')
				submit,
				taskError
			);

			// make bounty page
			var	taskBounties = $('<div id="task-bounties">'),
				list = $('<div>'),
				newBounty = $('<div id="row">'),
				bName = $('<input type="text" placeholder="name"></input>'),
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
				bountyA.attr('class','current');
				editA.attr('class','');
				taskEdit.hide();
				taskBounties.show();
			});

			add.click(createNewBounty(points, typeS, num, condS, date, task));

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

				if(!nameInput.val() || !descriptionInput.val() || !dueInput.val() || !categoriesSelect.val() || !statusSelect.val() || !estHoursInput.val()){
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
					due_date: dueInput.val(),
					category: categoryName,
					//status: statusSelect.val(),
					// is_completed: ,
					//bounties: {points: bountyPoints.val()}
					updated_hour_estimate: estHours
				});
				var milestoneID = task.__firebase.parent().parent().key();
				var projectID = task.__firebase.parent().parent().parent().parent().key();
				var commit = {
					added_lines_of_code : 0,
					hours : 0,
					message : "direct change to task from UI",
					milestone : milestoneID,
					project : projectID,
					removed_lines_of_code : 0,
					status : statusSelect.val(),
					user : user.uid,
					task : task.uid,
					timestamp : (new Date).getTime(),
					updated_hour_estimate : Number(estHoursInput.val()),
				};
				// console.log(commit);
				task.__root.child("commits/" + projectID).push(commit);
				if(categoryName){
					var cate = {};
					cate[categoryName] = true;
					//console.log(cate);
					projects.get(projectID).__custom_categories.update(cate);
				}
				$("#task-modal").foundation('reveal', 'close');
			});
		});
	});
	return row;
};

function createNewBounty(points, typeS, num, condS, date, task) {
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
}
