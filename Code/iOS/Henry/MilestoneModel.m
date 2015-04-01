//
//  MilestoneModel.m
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "MilestoneModel.h"
#import "HenryFirebase.h"

@implementation MilestoneModel
HenryFirebase* firebase;
NSMutableArray* tasks;
NSString* name;
// due date
NSNumber* taskPercent;
NSString* parentProjectID;
NSString* parentProjectName;
NSString* description;
// list change notifier listviewcallback
NSString* milestoneId;
// list change notifier tasklistviewcallback
@end
