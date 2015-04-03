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
@property (nonatomic, strong) NSString* trophyModelDescription;
@property (nonatomic, strong) NSString* image;
@property (nonatomic, strong) NSString* name;
@property (nonatomic, strong) NSString* key;

- (TrophyModel*)initWithName:(NSString*) name Description:(NSString*) desc Cost: (NSNumber*) cost;
@end
