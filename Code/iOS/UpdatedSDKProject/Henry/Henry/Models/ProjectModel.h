//
//  ProjectModel.h
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ProjectModel : NSObject
@property NSNumber* addedLinesOfCode;
@property NSMutableDictionary* backlog;
@property NSMutableDictionary* customCategories;
@property NSString* projectDescription;
@property NSString* dueDate;
@property NSNumber* hoursPercent;
@property NSMutableDictionary* memberRoles;
@property NSNumber* milestonePercent;
@property NSMutableDictionary* milestones;
@property NSNumber* milestonesCompleted;
@property NSString* name;
@property NSNumber* removedLinesOfCode;
@property NSNumber* taskPercent;
@property NSNumber* tasksCompleted;
@property NSNumber* totalEstimatedHours;
@property NSNumber* totalHours;
@property NSNumber* totalLinesOfCode;
@property NSNumber* totalMilestones;
@property NSNumber* totalTasks;



+ (ProjectModel*) constructModelFromDictionary: (NSDictionary*) dict;
@end
