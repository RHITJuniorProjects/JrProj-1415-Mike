//
//  MilestoneModel.h
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MilestoneModel : NSObject
@property NSNumber* addedLinesOfCode;
@property NSDictionary* burndownData;
@property NSString* milestoneDescription;
@property NSString* dueDate;
@property NSNumber* hoursPercent;
@property NSString* name;
@property NSNumber* removedLinesOfCode;
@property NSNumber* taskPercent;
@property NSMutableDictionary* tasks;
@property NSNumber* tasksCompleted;
@property NSNumber* totalEstimatedHours;
@property NSNumber* totalHours;
@property NSNumber* totalLinesOfCode;
@property NSNumber* totalTasks;

+ (MilestoneModel*) constructModelFromDictionary: (NSDictionary*) dict;
@end
