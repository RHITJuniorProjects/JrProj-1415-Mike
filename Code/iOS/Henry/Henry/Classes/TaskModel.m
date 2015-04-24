//
//  TaskModel.m
//  Henry
//
//  Created by Mark Van Aken on 3/28/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "TaskModel.h"
#import "BountyModel.h"
#import "HenryFirebase.h"
#define MAX_POINTS @100;
#define MIN_POINTS @0;
#define defaultAssignedUserName @"default";
#define pointsName @"points"

@implementation TaskModel
// Android: public TaskDetailFragment hp;

// A reference to Firebase to keep the data up to date
HenryFirebase* firebase;

// The tasks name
NSString* name = @"No name assigned";

// A description of the task
NSString* taskDescription = @"No description assigned";

// A list of the user ids of the users assigned to the task
NSString* assignedUserId = @"No User ID assigned";

// The name of the user assigned to this task
NSString* assignedUserName = defaultAssignedUserName;

// The status of the task.
NSString* status = @"No Status Assigned";

// The number of hours logged for this task
double hoursComplete = 0;

// The total number of hours currently estimated for this task
double hoursEstimatedCurrent = 0;

// The total number of hours originally estimated for this task
double hoursEstimatedOriginal = 0;

// The number of lines of code added to this task
NSInteger addedLines = 0;

// The number of lines of code removed from this task
NSInteger removedLines = 0;

// The total number of lines of code for this task
NSInteger totalLines = 0;

/**
 * This is the class that onChange is called from to when a field in
 * Firebase is updated. This then notifies the object that is displaying the
 * task that this object has been updated.
 */
//private ListChangeNotifier<Task> listViewCallback;
//private ListChangeNotifier<Bounty> bountyListViewCallback;


BountyModel* completionBounty;
NSString* completionBountyID;

// The bounty's parent project ID
HenryFirebase* parentProjectFB;

// The bounty's parent milestone ID
HenryFirebase* parentMilestoneFB;

// The bounty's parent project name
NSString* parentProjectName;

// The bounty's parent milestone name
NSString* parentMilestoneName;

// Gamification points
NSInteger points;

// An array of bounties that are contained within the task
NSMutableArray* bounties;
NSString* taskID;

#pragma mark MethodsFromAndroid
// TODO: Implement methods after line: 140 from Android


@end
