//Initializes the Milestone object
function Milestone(firebase) {
	this.__firebase = firebase;
	this.uid = firebase.key();
	this.__name = firebase.child('name');
	this.__description = firebase.child('description');
	this.__dueDate = firebase.child('due_date');
	this.__tasks = firebase.child('tasks');
	this.__members = firebase.child('members');
	this.__hoursPercent = firebase.child('hours_percent');
	this.__taskPercent = firebase.child('task_percent');
	this.__milestonePercent = firebase.child('milestone_percent');
	this.__burndownData = new BurndownData(firebase.child('burndown_data'));
};


Milestone.prototype = {
	getName: function (callback) {
		this.__name.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getDueDate: function (callback) {
		this.__dueDate.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getDescription: function (callback) {
		this.__description.on('value', function (dat) {
			callback(dat.val());
		});
	},
	getButtonDiv: function () {
		var milestone = $('<div class="row milestone">'),
			leftColumn = $('<div class="small-4 columns small-offset-1">'),
			rightColumn = $('<div class="small-4 columns small-offset-2 left">'),
			button = $('<div>'),
			a = $('<a class="button expand text-center">'),
			nameSpan = $('<span>'),
			descDiv = $('<div>'),
			dueDiv = $('<div>');

		a.append(nameSpan);
		button.append(a);
		leftColumn.append(button, descDiv, dueDiv);
		rightColumn.append(this.getTaskProgressBar(), this.getHoursProgressBar());
		milestone.append(leftColumn, rightColumn);
		var m = this;
		a.click(function () {
			selectMilestone(m);
		});
		this.getName(function (nameStr) {
			nameSpan.text(nameStr);
		});

		this.getDescription(function (descriptionStr) {
			descDiv.html(descriptionStr);
		});
		this.getDueDate(function (dueStr) {
			if (dueStr) {
				dueDiv.html('Due:' + dueStr);
			}
		});
		return milestone;
	},
	getTasks: function () {
		return new Table(function (fb) {
			return new Task(fb);
		}, this.__tasks);
	},
	getMembers: function () {
		return new ReferenceTable(users, this.__members);
	},
	getTaskProgressBar: function () {
		return makeProgressBar('small-12', 'Tasks Completed', this.__taskPercent);
	},
	getHoursProgressBar: function () {
		return makeProgressBar('small-12', 'Hours Completed', this.__hoursPercent);
	},
	off: function () {
		this.__firebase.off();
	},
	renderBurndownChart:function(id){
		var milestone = this;
		this.__name.once('value',function(name){
			var chart = new BurndownChart(id, name.val()+' Burn Down Chart',milestone.__burndownData);
			chart.render();
		});
	}
};

//Adds a new task to firebase based on the values of the modal's textfield inputs
Milestone.prototype.newTask = function() {
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
	   // bountyInput = $('<input type="text">'),
		nameH = '<h3>Add New Task</h3>',
		submit = $('<input class="button" value="Add Task" />'),
		modal = $('#task-modal'),
		//completed = makeSelect(Task.Flags, 'false');
		dueInput = $('<input type="text" placeholder="yyyy-mm-dd">'),
		taskError = $('<div id="task-error" class="my-error" hidden>All fields must be specified</div>');

	$('#task-modal').foundation('reveal','open');
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
			//label(completed, 'Is Completed'),
			label(dueInput, "Due Date"),
			//label(bountyInput,"Bounty Points"),
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

			if(!nameInput.val() || !descriptionInput.val() || !userSelect.val() || !dueInput.val() || !categoriesSelect.val() || !statusSelect.val() || !estHoursInput.val()){
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
				var cate = {};
				cate[categoryName] = true;
				selectedProject.__custom_categories.update(cate);
			}
			MilestoneDB.prototype.pushNewTask(nameInput.val(),descriptionInput.val(),userSelect.val(),categoryName,statusSelect.val(),estHours,dueInput.val());
		
			$("#task-modal").foundation('reveal', 'close');
		});

	});
}

Milestone.prototype.getTaskList = function(){
	return selectedMilestone.getTasks();
};

Milestone.prototype.getCharts = function(){
	var milestonePercentCompArray = [];
    var milestoneNameArray = [];
    var userLOC = [];
    var userName = [];
    var userID = [];
    var userNameLOCArray = [];
    var projectTotalLOC = 0;

	firebase.child("projects/"+selectedProject.uid+"/milestones").on('value', function(snapshot) {
        var milestone = snapshot.val();
        for(var id in snapshot.val()){
            milestoneNameArray.push(milestone[id].name);
            milestonePercentCompArray.push(milestone[id].hours_percent);
        }
    });
	firebase.child("projects/" + selectedProject.uid +"/total_lines_of_code").on('value', function (snapshot) {
	   projectTotalLOC = snapshot.val();
    });
	firebase.child("projects/" + selectedProject.uid +"/members").on('value', function (snapshot) {
	    for(var member in snapshot.val()){
	    	// console.log(member);
	        userID.push(member);
	    }
    });
	
	var i =0;
	firebase.child("users").on('value', function(snapshot){
		var user = snapshot.val();
		for(var id in snapshot.val()){
			if(id === userID[i] ){
				userLOC.push(user[id].projects[selectedProject.uid].total_lines_of_code);
				userName.push(user[id].name);
				userNameLOCArray.push(new Array(userName[i], userLOC[i]));
				i++
			}
		}
	});
		
    var milestoneHourBarChart = new BarChart(milestoneNameArray,milestonePercentCompArray,'Percent Completed by Hours', 'Percent Complete');
    milestoneHourBarChart.create('#mileContainer','Progress of Milestone');
    var pieChart = new PieChart(userNameLOCArray);
    pieChart.create('#linesOfCode','Breakup of Committed Lines of Code for Project');
};