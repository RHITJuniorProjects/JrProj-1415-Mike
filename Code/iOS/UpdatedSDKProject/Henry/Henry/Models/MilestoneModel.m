//
//  MilestoneModel.m
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "MilestoneModel.h"
#import "TaskModel.h"

@implementation MilestoneModel
+ (MilestoneModel*) constructModelFromDictionary: (NSDictionary*) dict;
{
    MilestoneModel* tempMilestone = [MilestoneModel new];
    tempMilestone.addedLinesOfCode = [dict objectForKey:@"added_lines_of_code"];
    tempMilestone.burndownData = [dict objectForKey:@"burndown_data"];
    tempMilestone.milestoneDescription = [dict objectForKey:@"description"];
    tempMilestone.dueDate = [dict objectForKey:@"due_date"];
    tempMilestone.hoursPercent = [dict objectForKey:@"hours_percent"];
    tempMilestone.name = [dict objectForKey:@"name"];
    tempMilestone.removedLinesOfCode = [dict objectForKey:@"removed_lines_of_code"];
    tempMilestone.taskPercent = [dict objectForKey:@"task_percent"];
    tempMilestone.tasksCompleted = [dict objectForKey:@"tasks_completed"];
    tempMilestone.totalEstimatedHours = [dict objectForKey:@"total_estimated_hours"];
    tempMilestone.totalHours = [dict objectForKey:@"total_hours"];
    tempMilestone.totalLinesOfCode = [dict objectForKey:@"total_lines_of_code"];
    tempMilestone.totalTasks = [dict objectForKey:@"total_tasks"];
    
    NSDictionary* rawTasks = [dict objectForKey:@"tasks"];
    NSMutableDictionary* taskObjects = [NSMutableDictionary new];
    NSArray* taskKeys = [rawTasks allKeys];
    for (NSString* key in taskKeys) {
        TaskModel* tempTask = [TaskModel constructModelFromDictionary:[rawTasks objectForKey:key]];
        [taskObjects setObject:tempTask forKey:key];
    }
    tempMilestone.tasks = taskObjects;

    return tempMilestone;
}
@end
