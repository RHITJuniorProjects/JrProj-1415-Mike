//
//  ProjectModel.m
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "ProjectModel.h"
#import "TaskModel.h"
#import "MilestoneModel.h"

@implementation ProjectModel

+ (ProjectModel*) constructModelFromDictionary: (NSDictionary*) dict;
{
    ProjectModel* projectToReturn = [ProjectModel new];
    projectToReturn.addedLinesOfCode = [dict objectForKey:@"added_lines_of_code"];
    projectToReturn.customCategories = [dict objectForKey:@"custom_categories"];
    projectToReturn.projectDescription = [dict objectForKey:@"description"];
    projectToReturn.dueDate = [dict objectForKey:@"due_date"];
    projectToReturn.hoursPercent = [dict objectForKey:@"hours_percent"];
    projectToReturn.memberRoles = [dict objectForKey:@"members"];
    projectToReturn.milestonePercent = [dict objectForKey:@"milestone_percent"];
    projectToReturn.milestonesCompleted = [dict objectForKey:@"milestones_completed"];
    projectToReturn.name = [dict objectForKey:@"name"];
    projectToReturn.removedLinesOfCode = [dict objectForKey:@"removed_lines_of_code"];
    projectToReturn.taskPercent = [dict objectForKey:@"task_percent"];
    projectToReturn.tasksCompleted = [dict objectForKey:@"tasks_completed"];
    projectToReturn.totalEstimatedHours = [dict objectForKey:@"total_estimated_hours"];
    projectToReturn.totalHours = [dict objectForKey:@"total_hours"];
    projectToReturn.totalLinesOfCode = [dict objectForKey:@"total_lines_of_code"];
    projectToReturn.totalMilestones = [dict objectForKey:@"total_milestones"];
    projectToReturn.totalTasks = [dict objectForKey:@"total_tasks"];
    
    
    NSDictionary* rawBacklog = [dict objectForKey:@"backlog"];
    NSArray* backlogKeys = [rawBacklog allKeys];
    NSMutableDictionary* backlogObjects = [NSMutableDictionary new];
    for (NSString* key in backlogKeys) {
        TaskModel* tempTask = [TaskModel constructModelFromDictionary:[rawBacklog objectForKey:key]];
        [backlogObjects setObject:tempTask forKey:key];
    }
    projectToReturn.backlog = backlogObjects;
    
    
    NSDictionary* rawMilestones = [dict objectForKey:@"milestones"];
    NSArray* milestoneKeys = [rawMilestones allKeys];
    NSMutableDictionary* milestoneObjects = [NSMutableDictionary new];
    for (NSString* key in milestoneKeys) {
        MilestoneModel* tempMilestone = [MilestoneModel constructModelFromDictionary:[rawMilestones objectForKey:key]];
        [milestoneObjects setObject:tempMilestone forKey:key];
    }
    projectToReturn.milestones = milestoneObjects;
    
    return projectToReturn;
}

+ (ProjectModel*) constructModelFromDictionaryIsolated: (NSDictionary*) dict;
{
    ProjectModel* projectToReturn = [ProjectModel new];
    projectToReturn.addedLinesOfCode = [dict objectForKey:@"added_lines_of_code"];
    projectToReturn.customCategories = [dict objectForKey:@"custom_categories"];
    projectToReturn.projectDescription = [dict objectForKey:@"description"];
    projectToReturn.dueDate = [dict objectForKey:@"due_date"];
    projectToReturn.hoursPercent = [dict objectForKey:@"hours_percent"];
    projectToReturn.memberRoles = [dict objectForKey:@"members"];
    projectToReturn.milestonePercent = [dict objectForKey:@"milestone_percent"];
    projectToReturn.milestonesCompleted = [dict objectForKey:@"milestones_completed"];
    projectToReturn.name = [dict objectForKey:@"name"];
    projectToReturn.removedLinesOfCode = [dict objectForKey:@"removed_lines_of_code"];
    projectToReturn.taskPercent = [dict objectForKey:@"task_percent"];
    projectToReturn.tasksCompleted = [dict objectForKey:@"tasks_completed"];
    projectToReturn.totalEstimatedHours = [dict objectForKey:@"total_estimated_hours"];
    projectToReturn.totalHours = [dict objectForKey:@"total_hours"];
    projectToReturn.totalLinesOfCode = [dict objectForKey:@"total_lines_of_code"];
    projectToReturn.totalMilestones = [dict objectForKey:@"total_milestones"];
    projectToReturn.totalTasks = [dict objectForKey:@"total_tasks"];
    
    
    NSDictionary* rawBacklog = [dict objectForKey:@"backlog"];
    NSArray* backlogKeys = [rawBacklog allKeys];
    NSMutableDictionary* backlogObjects = [NSMutableDictionary new];
    for (NSString* key in backlogKeys) {
        TaskModel* tempTask = [TaskModel constructModelFromDictionary:[rawBacklog objectForKey:key]];
        [backlogObjects setObject:tempTask forKey:key];
    }
    projectToReturn.backlog = backlogObjects;
    
    
    NSDictionary* rawMilestones = [dict objectForKey:@"milestones"];
    NSArray* milestoneKeys = [rawMilestones allKeys];
    NSMutableDictionary* milestoneObjects = [NSMutableDictionary new];
    for (NSString* key in milestoneKeys) {
        MilestoneModel* tempMilestone = [MilestoneModel constructModelFromDictionary:[rawMilestones objectForKey:key]];
        [milestoneObjects setObject:tempMilestone forKey:key];
    }
    projectToReturn.milestones = milestoneObjects;
    
    return projectToReturn;
}

@end
