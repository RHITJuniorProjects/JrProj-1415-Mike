//
//  BountyModel.m
//  Henry
//
//  Created by Mark Van Aken on 3/28/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "BountyModel.h"
#import "TaskModel.h"
#import "HenryFirebase.h"
#import <UIKit/UIKit.h>
#define COMPLETION_NAME @"completion"

@implementation BountyModel
NSString* claimed = @"None";
NSString* bountyDescription = @"None";
NSString* dueDate = @"No Due Date";
NSInteger hourLimit = -1;
NSInteger lineLimit = -1;
NSString* name;
NSInteger points = -1;

//  this field returns if this bounty is a completion bounty
bool isCompletion = false;

// this field is designed to make sure that the HorizontalPicker doesn't wipe out the value of points
// by setting it to zero before the value has been found
bool canChangePoints = false;
HenryFirebase* firebase;

// On Android this is simply called "id"
// We could not do this because id is already defined in Obj-C
NSString* bountyId;

// On Android this is a "Horizontal Picker" object called "hp"
UIPickerView* pickerView;

// This is the class that onChange is called from to when a field in
// Firebase is updated. This then notifies the object that is displaying the
// task that this object has been updated.
//  private ListChangeNotifier<Bounty> listViewCallback;

// The bounty's parent project ID
HenryFirebase* parentProjectFB;

// The bounty's parent milestone ID
HenryFirebase* parentMilestoneFB;

// The bounty's parent project name
NSString* parentProjectName;

// The bounty's parent milestone name
NSString* parentMilestoneName;

// The bounty's parent task ID
HenryFirebase* parentTaskFB;

// The bounty's task name
NSString* parentTaskName;

// The bounty's parent task
TaskModel* parentTask;

#pragma mark MethodsFromAndroid
// TODO: Add methods after Line: 75 from android



@end
