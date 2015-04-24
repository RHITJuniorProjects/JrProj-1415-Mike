//
//  TaskModel.h
//  Henry
//
//  Created by Mark Van Aken on 3/28/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BountyModel.h"

@interface TaskModel : NSObject
// The tasks name
@property (nonatomic, strong) NSString* name;

// A description of the task
@property (nonatomic, strong) NSString* taskDescription;

// A list of the user ids of the users assigned to the task
@property (nonatomic, strong) NSString* assignedUserId;

// The name of the user assigned to this task
@property (nonatomic, strong) NSString* assignedUserName;

// The status of the task.
@property (nonatomic, strong) NSString* status;

// The number of hours logged for this task
@property double hoursComplete;

// The total number of hours currently estimated for this task
@property double hoursEstimatedCurrent;

// The total number of hours originally estimated for this task
@property double hoursEstimatedOriginal;

// The number of lines of code added to this task
@property NSInteger addedLines;

// The number of lines of code removed from this task
@property NSInteger removedLines;

// The total number of lines of code for this task
@property NSInteger totalLines;

/**
 * This is the class that onChange is called from to when a field in
 * Firebase is updated. This then notifies the object that is displaying the
 * task that this object has been updated.
 */
//private ListChangeNotifier<Task> listViewCallback;
//private ListChangeNotifier<Bounty> bountyListViewCallback;


@property (nonatomic, weak) BountyModel* completionBounty;
@property (nonatomic, strong) NSString* completionBountyID;

// The bounty's parent project ID
@property (nonatomic, strong) NSString* parentProjectID;

// The bounty's parent milestone ID
@property (nonatomic, strong) NSString* parentMilestoneID;

// The bounty's parent project name
@property (nonatomic, strong) NSString* parentProjectName;

// The bounty's parent milestone name
@property (nonatomic, strong) NSString* parentMilestoneName;

// Gamification points
@property NSInteger points;

// An array of bounties that are contained within the task
@property (nonatomic, strong) NSMutableArray* bounties;
@property (nonatomic, strong) NSString* taskID;
@end
