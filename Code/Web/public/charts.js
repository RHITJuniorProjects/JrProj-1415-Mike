function Chart(id,title){
	this.chart = {
		renderTo:id,
		ignoreHiddenSeries:true
	};
	this.title = {
		text:title
	};
	this.series = [];
}

function makeAxis(name,units){
	return {
		labels:{
			formatter:function(){
				if(units){
					return this.value+' '+units;
				}
				return this.value;
			}
		},
		title:{
			text:name
		}
	};
}

function dateFormatter(){
	return new Date(this.value).toLocaleDateString();
}

function dateAxis(name){
	return {
		labels:{
			formatter:dateFormatter
		},
		title:{
			text:'Date'
		}
	}
}

Chart.prototype = {
	setXAxis:function(name,units){
		this.xAxis = makeAxis(name,units);
	},
	setYAxis:function(name,units){
		this.yAxis = makeAxis(name,units);
	},
	render:function(){
		if(this._renderedChart){
			this._renderedChart.redraw();
		} else {
			this._renderedChart = new Highcharts.Chart(this);
		}
	}
};

function LineChart(id, title){
	Chart.call(this,id,title);
	this.chart.defaultSeriesType = 'line';
}

LineChart.prototype = new Chart(null,null);

LineChart.prototype.addSeries = function(points,name){
	if(name === undefined){
		name = points._id;
	}
	//console.log(name);
	points._charts.push(this);
	//console.log(points);
	var series = {
		data:points._points,
		id:points._id,
		name:name
	};
	this.series.push(series);
};

function BurndownChart(id,title,burndownData){
	LineChart.call(this,id,title);
	this.xAxis = dateAxis();
	this.tooltip = {
		formatter:function(){
			return new Date(this.x).toLocaleString()+'<br>'+this.y+' hours';
		}
	};
	this.setYAxis('Hours','hrs');
	this.addSeries(burndownData.estimatedHours());
	this.addSeries(burndownData.hoursCompleted());
}

BurndownChart.prototype = new LineChart(null,'Burn Down Chart');

function makeProgressBar(divClass, text, percentRef) {
	var div = $('<div>');
	var label = $('<h4>' + text + '</h4>');
	var progress = $('<div class="progress ' + divClass + '">');
	var span = $('<span class="meter">');
	div.append(label, progress);
	progress.append(span);
	percentRef.on('value', function (snap) {
		var percent = snap.val();
		if(percent > 100){
			percent = 100;
		}
		span.width(String(percent) + "%");
	});
	return div;
}

function label(elem, label) {
	var l = $('<label>');
	l.append(label, elem);
	return l;
}

function makeAddMemberTile(project){
	var selectedUser = null,
		selectButton = $('<div class="button expand text-center">Add Member</div>'),
		addMemberTile = $('<div class="column panel outlined">'),
		nameRow = '<div class="row">'
					+ '<div class="small-12 columns text-center">'
						+ '<h3>Add New Member</h3>'
					+ '</div>'
				+ '</div>',
		selectRow = $('<div class="row">'),
		selectColumn = $('<div class="small-7 columns">'),
		buttonColumn = $('<div class="small-5 columns">'),
		errorRow = $('<div class="row" hidden>'),
		errorColumn = $('<div class="small-12 columns">'),
		error = $('<div class="my-error">No Member Selected</div>'),
		userSelect = users.getSelect(function(user){
			selectedUser = user;
		});
	errorColumn.append(error);
	errorRow.append(errorColumn);
	buttonColumn.append(selectButton);
	selectColumn.append(userSelect);
	selectRow.append(selectColumn,buttonColumn);
	addMemberTile.append(nameRow,selectRow,errorRow);
	selectButton.click(function(){
		if(selectedUser){
			project.addMember(selectedUser);
		} else {
			errorRow.show();
		}
	});
	return addMemberTile;
}

function Series(id){
	this._points = [];
	this._id = id;
	this._charts = [];
}

Series.prototype.addPoint = function(point){
	this._points.push(point);
	var id = this._id;
	//console.log(id + ' add Point');
	var charts = this._charts;
	var chart;
	for(var i = charts.length;i--;){
		chart = charts[i];
		if(chart._renderedChart){
			chart._renderedChart.get(id).addPoint(point,true,true);
		}
	}
}

function CumulativeSeries(id){
	Series.call(this,id);
}

CumulativeSeries.prototype = new Series();
CumulativeSeries.prototype.addPoint = function(point){
	if(this._points.length > 0){
		var lastPoint = this._points[this._points.length-1];
		point = [point[0],point[1]+lastPoint[1]];
	}
	Series.prototype.addPoint.call(this,point);
};

function BurndownData(firebase){
	this._firebase = firebase;
}

function makeSeriesGetter(name){
	return function(){
		this._init();
		return this[name];
	}
}

BurndownData.prototype._init = function(){
	if(this._initialized){
		return;
	}
	this._estimHours = new Series('Estimated Hours');
	this._compHours = new Series('Completed Hours');
	this._remTasks = new Series('Remaining Tasks');
	this._compTasks = new Series('Completed Tasks');
	var bdd = this;
	this._firebase.on('child_added',function(snap){
		var obj = snap.val();
		var time = Number(snap.key());
		if(typeof obj === "string"){
			return;
		}
		bdd._compHours.addPoint([time, obj.hours_completed]);
		bdd._estimHours.addPoint([time,obj.estimated_hours_remaining]);
		bdd._remTasks.addPoint([time,  obj.tasks_remaining]);
		bdd._compTasks.addPoint([time, obj.tasks_completed]);
	});
	this._initialized = true;
};

function BarChart(xdata,ydata, xAxisName, yAxisName){
	this.xdata = xdata;
	this.ydata = ydata;
	this.xAxisName = xAxisName;
	this.yAxisName = yAxisName;

}

BarChart.prototype.create = function(render,title){
	 $(render).highcharts({
        chart: {
            type: 'column',
            margin: 75,
            options3d: {
                enabled: false,
                alpha: 10,
                beta: 25,
                depth: 70
            }
        },
        title: {
            text: title,
            style: { "color": "#333333", "fontSize": "30px" },
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: this.xdata
        },
        yAxis: {
            opposite: false,
            tickInterval: 20,
            max: 100,
            title: {
                text: this.xAxisName
            }
        },
        series: [{
            name: this.yAxisName,
            data: this.ydata
        }],
        colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
                '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
};


function PieChart(pieData){
	this.pieData = pieData;
	// console.log(pieData);

}

PieChart.prototype.create = function(render,title){
    $(render).highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: 1,//null,
            plotShadow: false
        },
        title: {
            text: 'Breakup of Committed Lines of Code for Project'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.y} lines</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}:</b> {point.y} lines',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
        series: [{
            type: 'pie',
            name: 'Line Breakup',
            data: this.pieData
        }]
	});
};
BurndownData.prototype.estimatedHours = makeSeriesGetter('_estimHours');
BurndownData.prototype.hoursCompleted = makeSeriesGetter('_compHours');
BurndownData.prototype.tasksCompleted = makeSeriesGetter('_compTasks');
BurndownData.prototype.tasksRemaining = makeSeriesGetter('_remTasks');
