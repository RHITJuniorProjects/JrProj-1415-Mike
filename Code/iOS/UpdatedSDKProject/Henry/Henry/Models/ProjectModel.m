//
//  ProjectModel.m
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "ProjectModel.h"
#import "HenryFirebase.h"

@implementation ProjectModel

+ (ProjectModel*) constructModelFromDictionary: (NSDictionary*) dict {
    //TODO: Finish making this class
    ProjectModel* projectToReturn = [ProjectModel new];
    projectToReturn.name = [dict objectForKey:@"name"];
    projectToReturn.description = [dict objectForKey:@"description"];
    projectToReturn.hoursPercent = [dict objectForKey:@"hours_percent"];
    projectToReturn.taskPercent = [dict objectForKey:@"task_percent"];
    projectToReturn.milestonePercent = [dict objectForKey:@"milestone_percent"];
    projectToReturn.projectId = [dict objectForKey:@"projectId"];
    
    
    return projectToReturn;
}

@end
