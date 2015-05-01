//
//  UserModel.h
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface UserModel : NSObject

@property NSNumber* availablePoints;
@property NSString* email;
@property NSString* name;
@property NSMutableDictionary* projects;
@property NSNumber* totalPoints;
@property NSMutableDictionary* trophies;
@property NSString* key;

+ (UserModel*)constructModelFromDictionary:(NSDictionary*) dict;
@end
