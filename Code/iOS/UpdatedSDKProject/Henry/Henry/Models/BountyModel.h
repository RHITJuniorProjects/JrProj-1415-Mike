//
//  BountyModel.h
//  Henry
//
//  Created by Mark Van Aken on 3/28/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BountyModel : NSObject
@property NSString* claimed;
@property NSString* bountyDescription;
@property NSString* dueDate;
@property NSString* hourLimit;
@property NSString* lineLimit;
@property NSString* name;
@property NSNumber* points;
+ (BountyModel*) constructModelFromDictionary:(NSDictionary*) dict;
@end
