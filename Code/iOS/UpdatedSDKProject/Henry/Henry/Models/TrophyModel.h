//
//  TrophyModel.h
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TrophyModel : NSObject
@property (nonatomic, strong) NSNumber* cost;
@property (nonatomic, strong) NSString* trophyDescription;
@property (nonatomic, strong) NSString* image;
@property (nonatomic, strong) NSString* name;
@property NSString* key;

- (TrophyModel*)initWithName:(NSString*) name Description:(NSString*) desc Cost: (NSNumber*) cost Image: (NSString *) trophyimage;
+ (TrophyModel*)constructModelFromDictionary:(NSDictionary*) dict;
- (BOOL) isEqual:(TrophyModel*) object;

@end
