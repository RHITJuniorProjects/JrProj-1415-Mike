//
//  UserModel.m
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "UserModel.h"
#import "HenryFirebase.h"

@implementation UserModel
HenryFirebase* firebase;
NSString* name;
NSString* gitName;
NSString* email;
NSString* key;
NSNumber* totalPoints;
NSNumber* availablePoints;
NSMutableDictionary* trophies;
// list change notifier...
// map of projects...
NSMutableArray* tasks;

@end
