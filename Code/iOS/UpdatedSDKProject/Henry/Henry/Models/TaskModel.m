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

@implementation TaskModel

+ (TaskModel*) constructModelFromDictionary: (NSDictionary*) dict;
{
    TaskModel* tempTask = [TaskModel new];
    tempTask.addedLinesOfCode = [dict objectForKey:@"added_lines_of_code"];
    tempTask.assignedTo = [dict objectForKey:@"assignedTo"];
    tempTask.category = [dict objectForKey:@"category"];
    tempTask.taskDescription = [dict objectForKey:@"description"];
    tempTask.dueDate = [dict objectForKey:@"due_date"];
    tempTask.isCompleted = [dict objectForKey:@"isCompleted"];
    tempTask.name = [dict objectForKey:@"name"];
    tempTask.originalHourEstimate = [dict objectForKey:@"original_hour_estimate"];
    tempTask.percentComplete = [dict objectForKey:@"percent_complete"];
    tempTask.removedLinesOfCode = [dict objectForKey:@"removed_lines_of_code"];
    tempTask.status = [dict objectForKey:@"status"];
    tempTask.totalHours = [dict objectForKey:@"total_hours"];
    tempTask.totalLinesOfCode = [dict objectForKey:@"total_lines_of_code"];
    tempTask.updatedHourEstimate = [dict objectForKey:@"updated_hour_estimate"];
    
    NSDictionary* rawBounties =[dict objectForKey:@"bounties"];
    NSMutableDictionary* bountyObjects = [NSMutableDictionary new];
    NSArray* bountyKeys = [rawBounties allKeys];
    for (NSString* key in bountyKeys) {
        BountyModel* tempBounty = [BountyModel constructModelFromDictionary:[rawBounties objectForKey:key]];
        [bountyObjects setObject:tempBounty forKey:key];
    }
    tempTask.bounties = bountyObjects;
    return tempTask;
}

@end
