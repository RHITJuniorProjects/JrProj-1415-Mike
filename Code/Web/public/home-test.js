
var projectID;
var linesOfCodeArray = [];
var nameArray = [];
var milestoneNameArray = [];
var milestonePercentArray = [];
var projectHoursArray = [];
var projectNameArray = [];
var projectTaskArray = [];
var getLinesOfCode;
var getNameAndDraw;
var pieChartDrawer;
var getMilestoneData;
var getProjectData;
var UserMilestoneName;
var UserLocAdded;
var UserLocRemoved;
var UserTotalLoc;
var UserTotalHours;


function drawProjectStuff (fb) {

 fb.child("projects").on('value', function(snapshot) {
        var projectHoursArray = [];
        var projectNameArray = [];
        var projectTaskArray = [];
        var project = snapshot.val();
        for(var id in snapshot.val()){
            projectNameArray.push(project[id].name);
            projectHoursArray.push(project[id].hours_percent);
            projectTaskArray.push(project[id].task_percent);
        }
        projectDrawerHours(projectNameArray, projectHoursArray);
        projectDrawerTask(projectNameArray, projectTaskArray);

    });
};


//by hours
function projectDrawerHours(name, hours) {
   $('#projContainer1').highcharts({
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
            text: 'Progress of Projects',
            style: { "color": "#333333", "fontSize": "30px" },
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: name
        },
        yAxis: {
            opposite: false,
            tickInterval: 20,
            max: 100,
            title: {
                text: 'Percent Complete'
            }
        },
        series: [{
            name: 'Percent Complete by Hours',
            data: hours
        }],
        colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
                '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
};

//by task
function projectDrawerTask(name, taskData) {
    $('#projContainer2').highcharts({
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
            text: 'Project completeness based on Task',
            style: { "color": "#333333", "fontSize": "30px" },
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: name
        },
        yAxis: {
            opposite: false,
            tickInterval: 20,
            max: 100,
            title: {
                text: 'Percent Complete'
            }
        },
        series: [{
            name: 'Percent Complete',
            data: taskData
        }],
        colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
                '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
};

var change = {
    20: 'New',
    40: 'Implementation',
    60: 'Testing',
    80: 'Verifying',
    100: 'Regression',
    120: 'Closed'
};
function drawMilestoneStuff(projId, fb){        
    milestoneNameArray = [];
    milestonePercentArray = [];
projectID = projId.toString();


//fills workerArray and linesOfCodeArray
fb.child("projects/" + projectID +"/members").on('value', function (snapshot) {
    //var userData = snapshot.val();
    var workerArray = [];

    for(var item in snapshot.val()){
    //  console.log(item);
        workerArray.push(item);
    }

    for(i = 0; i<workerArray.length; i++){
        getLinesOfCode(workerArray[i], workerArray);
    }
});

fb.child("projects/" + projectID + "/milestones").on('value', function(snapshot) {
    var milestoneArray = [];

    for(var item in snapshot.val()){
        milestoneArray.push(item);
    }

    for(i = 0; i<milestoneArray.length; i++){
       //console.log("milestone id " + milestoneArray[i]);
        getMilestoneData(milestoneArray[i],milestoneArray,projectID);
    }

});


function getLinesOfCode(item, array){
    fb.child("users/" + item + "/projects/" + projectID + "/total_lines_of_code").on('value', function(snapshot){
        linesOfCodeArray.push(snapshot.val());

        if(array.length == linesOfCodeArray.length){
            for(i = 0; i<array.length; i++){
                getNameAndDraw(array[i], array, linesOfCodeArray);
            }   
        }                   
    });
};

function getMilestoneData(item, array, projectID){
    fb.child("projects/" + projectID + "/milestones/" + item + "/name").on('value', function(snapshot){
        milestoneNameArray.push(snapshot.val());
        // console.log("out of if" + snapshot.val());
        // console.log("projectID"+projectID);
       
        
  //     if(array.length == milestoneNameArray.length){
            fb.child("projects/" + projectID + "/milestones/" + item + "/task_percent").on('value', function(snapshot){
                milestonePercentArray.push(snapshot.val());
             
        // console.log("in if" +snapshot.val());
         // console.log("projectID"+projectID);
            
     });
    
    milestoneDrawer(milestoneNameArray,milestonePercentArray);  
    });

};



// function getMilestoneName(item, array, projectID){
//     fb.child("projects/" + projectID + "/milestones/" + item + "/task_percent").on('value', function(snapshot){
//         milestonePercentArray.push(snapshot.val());
//         // console.log("sbao" +snapshot.val());
//         // console.log("projectID"+projectID);
//         milestoneDrawer(milestonePercentArray);


//     });

// };

function getNameAndDraw(current, userArray, linesArray){
    var totalArray = [];
    
    fb.child("users/" + current + "/name").on('value', function(snapshot){
        nameArray.push(snapshot.val());
        
        if(nameArray.length == userArray.length){
            for(i = 0; i<linesArray.length; i++){
                totalArray.push(new Array(nameArray[i], linesArray[i]));
               
            }
            console.log(totalArray);
            pieChartDrawer(totalArray);

        }
                        });
};
                        

/*
fb.child("users/simplelogin:25/projects/" + projectID + "/total_lines_)of_code").on('value', function (snapshot) {
  //var userData = snapshot.val();
  console.log(snapshot.val());
}, function (errorObject) {
  console.log('The read failed: ' + errorObject.code);
});
*/



//console.log(projectLines);


function milestoneDrawer(name, data) {
    //console.log(data);

    $('#mileContainer').highcharts({
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
            text: 'Progress of Milestones',
            style: { "color": "#333333", "fontSize": "30px" },
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: name
        },
        yAxis: {
            opposite: false,
            tickInterval: 20,
            max: 100,
            title: {
                text: 'Percent Complete'
            }
        },
        series: [{
            name: 'Percent Complete',
            data: data
        }],
        colors: ['#0099FF', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
                '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'] 
    });
};


var change = {
    20: 'New',
    40: 'Implementation',
    60: 'Testing',
    80: 'Verifying',
    100: 'Regression',
    120: 'Closed'
};

function pieChartDrawer(temp){
    $(function () {
        $('#linesOfCode').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: 1,//null,
                plotShadow: false
            },
            title: {
                text: 'Breakup of Committed Lines of Code for Project'
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
                data: temp
            }]
        });
    });
};
};

// Charts for users milestone metrics based on 

function drawUserStatistics(fb, currentUser) {
    var projectArr = [];
    var UserMilestone = [];
    UserMilestoneName = [];
    UserLocAdded = [];
    UserLocRemoved = [];
    UserTotalLoc = [];
    UserTotalHours = [];

    fb.child("users/" + currentUser +"/projects").on('value', function(snapshot) {
   
    for(var item in snapshot.val()){
        projectArr.push(item);
    }

    for(i = 0; i<projectArr.length; i++){
        getUserMilestone(projectArr[i]);
       
        
     }

     UserStatistics1(UserMilestoneName,UserLocAdded,UserLocRemoved,UserTotalLoc);
     UserStatistics2(UserMilestoneName,UserTotalHours);
     UserStatistics3(UserMilestoneName,UserLocAdded,UserLocRemoved,UserTotalLoc);
     UserStatistics4(UserMilestoneName,UserTotalHours);
});


function getUserMilestone(projectID){
    fb.child("users/" + currentUser + "/projects/" + projectID + "/milestones").on('value',function(snapshot){
         for(var item in snapshot.val()){
            getUserLocAdded(projectArr[i],item);
            getUserLocRemoved(projectArr[i],item);
            getUserTotalLoc(projectArr[i],item);
            getUserMilestoneName(projectArr[i],item);
            getUserHours(projectID,item);
        }

    });

};
// projects/-JYcg488tAYS5rJJT4Kh/milestones/-JYc_9ZGEPFM8cjChyKl/name
function getUserMilestoneName(projectID,item){
        fb.child("projects/" + projectID + "/milestones/" + item +"/name").on('value',function(snapshot){
          UserMilestoneName.push(snapshot.val());
          //console.log(item);   
        });
    
};

function getUserLocAdded(projectID,item){
        fb.child("users/" + currentUser +"/projects/" + projectID + "/milestones/" + item +"/add_lines_of_code").on('value',function(snapshot){
           UserLocAdded.push(snapshot.val());  
        });
    
};

function getUserLocRemoved(projectID,item){
        fb.child("users/" + currentUser +"/projects/" + projectID + "/milestones/" + item +"/removed_lines_of_code").on('value',function(snapshot){
           UserLocRemoved.push(snapshot.val());   
        }); 
};

function getUserTotalLoc(projectID,item){
        fb.child("users/" + currentUser +"/projects/" + projectID + "/milestones/" + item +"/total_lines_of_code").on('value',function(snapshot){
           UserTotalLoc.push(snapshot.val());   
        });
};

function getUserHours(projectID,item){
        fb.child("users/" + currentUser +"/projects/" + projectID + "/milestones/" + item +"/total_hours").on('value',function(snapshot){
           UserTotalHours.push(snapshot.val());  
        });
    
};


function UserStatistics1(categories, loc_added, loc_removed, total_lines_of_code) {
    $('#UserStatistics1').highcharts({
        title: {
            text: 'User Line of Code Statistics',
            x: -20 //center
        },
        xAxis: {
            categories: categories
            },
       yAxis: {
            title: {
                text: 'Lines of Code'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '°C'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{
            name: 'Lines of Code Added',
            data: loc_added
        }, {
            name: 'Lines of Code Removed',
            data: loc_removed
        }, {
            name: 'Total Lines of Code',
            data: total_lines_of_code
        }]
    });
};

function UserStatistics2(categories, hours) {
    $('#UserStatistics2').highcharts({
        title: {
            text: 'User Hour Statistics',
            x: -20 //center
        },
        xAxis: {
            categories: categories
            },
       yAxis: {
            title: {
                text: 'Hours'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '°C'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{
            name: 'Hours',
            data: hours
        }]
    });
};
function UserStatistics3(categories, loc_added, loc_removed, total_lines_of_code) {
    $('#UserStatistics3').highcharts({
        title: {
            text: 'User Line of Code Statistics',
            x: -20 //center
        },
        xAxis: {
            categories: categories
            },
       yAxis: {
            title: {
                text: 'Lines of Code'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '°C'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{
            name: 'Lines of Code Added',
            data: loc_added
        }, {
            name: 'Lines of Code Removed',
            data: loc_removed
        }, {
            name: 'Total Lines of Code',
            data: total_lines_of_code
        }]
    });
};

function UserStatistics4(categories, hours) {
    $('#UserStatistics4').highcharts({
        title: {
            text: 'User Hour Statistics',
            x: -20 //center
        },
        xAxis: {
            categories: categories
            },
       yAxis: {
            title: {
                text: 'Hours'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '°C'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{
            name: 'Hours',
            data: hours
        }]
    });
};


};
