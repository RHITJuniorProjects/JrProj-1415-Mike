//
//  UserModel.m
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "UserModel.h"
#import "ProjectModel.h"
#import "TrophyModel.h"

@implementation UserModel

+ (UserModel*)constructModelFromDictionary:(NSDictionary*) dict;
{
    UserModel* tempUser = [UserModel new];
    tempUser.availablePoints = [dict objectForKey:@"available_points"];
    tempUser.email = [dict objectForKey:@"email"];
    tempUser.name = [dict objectForKey:@"name"];
    
    tempUser.totalPoints = [dict objectForKey:@"total_points"];
    
    NSDictionary* rawProjects =[dict objectForKey:@"projects"];
    NSMutableDictionary* projectObjects = [NSMutableDictionary new];
    NSArray* projectKeys = [rawProjects allKeys];
    for (NSString* key in projectKeys) {
        ProjectModel* tempProject = [ProjectModel constructModelFromDictionary:[rawProjects objectForKey:key]];
        [projectObjects setObject:tempProject forKey:key];
    }
    tempUser.projects = projectObjects;
    
    NSDictionary* rawTrophies =[dict objectForKey:@"trophies"];
    NSMutableDictionary* trophyObjects = [NSMutableDictionary new];
    NSArray* trophyKeys = [rawTrophies allKeys];
    for (NSString* key in trophyKeys) {
        TrophyModel* tempTrophy = [TrophyModel constructModelFromDictionary:[rawTrophies objectForKey:key]];
        [trophyObjects setObject:tempTrophy forKey:key];
    }
    tempUser.trophies = trophyObjects;
    return tempUser;
}

@end
