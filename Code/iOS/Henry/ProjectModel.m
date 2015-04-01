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
Firebase* firebase;
NSMutableArray* milestones;
NSString* name;
// due date
// map
NSString* description;
NSNumber* hoursPercent;
NSNumber* tasksPercent;
NSNumber* milestonesPercent;
NSString* projectId;
// listchangenotifier listviewcallback
// listchangenotifier milestonelistviewcallback

- (instancetype)init
{
    self = [super init];
    if (self) {
        firebase = [HenryFirebase getFirebaseObject];
    }
    return self;
}
@end
