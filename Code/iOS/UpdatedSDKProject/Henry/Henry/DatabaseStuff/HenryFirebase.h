//
//  HenryFirebase.h
//  Henry
//
//  Created by Schneider, Mason on 10/29/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Firebase/Firebase.h>
#import <Firebase/FDataSnapshot.h>
#import "TrophyModel.h"
#import "TaskModel.h"
#import "UserModel.h"

typedef void(^ProjectCallback)(NSDictionary *projectsDictionary,BOOL success, NSError *error);
typedef void(^MilestoneCallback)(NSDictionary *milestonesDictionary,BOOL success, NSError *error);
typedef void(^TasksForMilestoneCallback)(NSDictionary *tasksDictionary,BOOL success, NSError *error);
typedef void(^TrophiesCallback)(NSDictionary *trophiesDictionary,BOOL success, NSError *error);
typedef void(^BountiesCallback)(NSDictionary *bountysDictionary,BOOL success, NSError *error);
typedef void(^UserInfoCallback)(NSDictionary *userInfoDictionary,BOOL success, NSError *error);
typedef void(^UsersCallback)(NSDictionary *usersDictionary,BOOL success, NSError *error);

@interface HenryFirebase : Firebase

+(Firebase *)getFirebaseObject;
+ (NSString *)getFirebaseURL;
+(void)setFirebaseWritingObjectTo: (Firebase*) fb;
+(void)setFirebaseReadingObjectTo: (Firebase*) fb;
- (void) updateDataSnapshot;
- (void) getAllProjectsWithBlock:(ProjectCallback) completionBlock;
- (void) getMilestonesWithProjectId:(NSString*) projectId withBlock:(MilestoneCallback) completionBlock;
- (void) getTasksForMilestone:(NSString*) milestoneId projectId:(NSString*) projectId withBlock:(TasksForMilestoneCallback) completionBlock;
- (void) getAllTrophiesWithBlock: (TrophiesCallback) completionBlock;
- (void) getUserInfoWithUserId: (NSString*) userid withBlock: (UserInfoCallback) completionBlock;
- (void) getAllUsersWithBlock: (UsersCallback) completionBlock;
- (void) getMembersOnProjectWithProjectID: (NSString*) projectId withBlock: (UsersCallback) completionBlock;
- (void) getTrophiesBelongingToUserId: (NSString*) userId withBlock: (TrophiesCallback) completionBlock;

- (void) purchaseTrophyWithTrophyModel: (TrophyModel*) trophy withUserId: (NSString*) userid withOldAvailablePoints: (NSNumber*) oldAvailPoints;
- (void) assignMemberToTaskWithTaskKey: (NSString*) taskId UserKey: (NSString*) userId ProjectKey: (NSString*) projectId MilestoneKey: (NSString*) milestoneId;


    
// Never implemented, so this signature will change
- (void) getBounties:(NSString*) taskId;
@end

