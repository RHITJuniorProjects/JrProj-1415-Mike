//
//  UserModel.h
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface UserModel : NSObject
@property (nonatomic, strong) NSString* name;
@property (nonatomic, strong) NSString* gitName;
@property (nonatomic, strong) NSString* email;
@property (nonatomic, strong) NSString* key;
@property (nonatomic, strong) NSNumber* totalPoints;
@property (nonatomic, strong) NSNumber* availablePoints;
@property (nonatomic, weak) NSMutableDictionary* trophies;
// list change notifier...
// map of projects...
@property (nonatomic, strong) NSMutableArray* tasks;
@end
