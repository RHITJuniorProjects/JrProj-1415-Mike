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
@property NSNumber* addedLinesOfCode;
@property NSString* assignedTo;
@property NSMutableDictionary* bounties;
@property NSString* category;
@property NSString* taskDescription;
@property NSString* dueDate;
@property BOOL isCompleted;
@property NSString* name;
@property NSNumber* originalHourEstimate;
@property NSNumber* percentComplete;
@property NSNumber* removedLinesOfCode;
@property NSString* status;
@property NSNumber* totalHours;
@property NSNumber* totalLinesOfCode;
@property NSNumber* updatedHourEstimate;
+ (TaskModel*) constructModelFromDictionary: (NSDictionary*) dict;
@end
