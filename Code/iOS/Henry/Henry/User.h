//
//  User.h
//  Henry
//
//  Created by CSSE Department on 3/28/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HenryFirebase.h"

@interface User : NSObject
@property(weak, nonatomic) Firebase* firebase;
@property(weak, nonatomic) NSString* name;
@property(weak, nonatomic) NSString* gitName;
@property(weak, nonatomic) NSString* email;
@property(weak, nonatomic) NSString* key;
@property(weak, nonatomic) NSNumber* totalPoints;
@property(weak, nonatomic) NSNumber* availablePoints;
@property(weak, nonatomic) NSMutableDictionary* trophies;
// list change notifier
@property(weak, nonatomic) NSMutableDictionary* projects;
@property(weak, nonatomic) NSMutableArray* tasks;
@end
