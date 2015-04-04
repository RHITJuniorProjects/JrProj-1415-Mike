//
//  HenryFirebase.m
//  Henry
//
//  Created by Schneider, Mason on 10/29/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryFirebase.h"
#import <UIKit/UIKit.h>
@interface HenryFirebase()
@property FDataSnapshot* snapshot;
@end

@implementation HenryFirebase

Firebase *henryFB;

// This is an instance of FireBase that can be changed to whatever you need to write to the database.
// When writing to the database make sure you don't expect that this is set to what you want.
// You should always set it to what you need (meaning do whatever childByAppendingPath stuff you need to do)
// so you are sure that it is going to do what you want it to.
Firebase *writeToFB;





+(Firebase *)getFirebaseObject {
    @try{
    henryFB =[[Firebase alloc] initWithUrl:[HenryFirebase getFirebaseURL]];
        return henryFB;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (void) updateDataSnapshot{
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        self.snapshot = snapshot;
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
}

// Tested
+(NSString *)getFirebaseURL {
    return @"https://henry-test.firebaseio.com";
}

- (void) getAllProjectsWithBlock:(ProjectCallback) completionBlock {
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *projects = snapshot.value[@"projects"];
        completionBlock(projects, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}

- (void) getMilestonesWithProjectId:(NSString*) projectId withBlock:(MilestoneCallback) completionBlock {
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *milestones = snapshot.value[@"projects"][projectId][@"milestones"];
        completionBlock(milestones, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}

- (void) getTasksForMilestone:(NSString*) milestoneId projectId:(NSString*) projectId withBlock:(TasksForMilestoneCallback) completionBlock {
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *tasks = snapshot.value[@"projects"][projectId][@"milestones"][milestoneId];
        completionBlock(tasks, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}

- (void) getAllTrophiesWithBlock: (TrophiesCallback) completionBlock;
{
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *trophies = snapshot.value[@"trophies"];
        completionBlock(trophies, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}
- (void) getAllUsersWithBlock: (UsersCallback) completionBlock;
{
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *users = snapshot.value[@"users"];
        completionBlock(users, YES, nil);
    }];
}

- (void) getUserInfoWithUserId: (NSString*) userid withBlock: (UserInfoCallback) completionBlock;
{
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *userInfo = snapshot.value[@"users"][userid];
        completionBlock(userInfo, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}

- (void) getMembersOnProjectWithProjectID: (NSString*) projectId withBlock: (UsersCallback) completionBlock;
{
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *membersOnProject = snapshot.value[@"projects"][projectId][@"members"];
        completionBlock(membersOnProject, YES, nil);
    }];
}

- (void) purchaseTrophyWithTrophyModel: (TrophyModel*) trophy withUserId: (NSString*) userid;
{
    writeToFB = [HenryFirebase getFirebaseObject];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"/users/%@/", userid]];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"trophies/%@",trophy.key]];
    [writeToFB setValue:trophy.name];
}
- (void) assignMemberToTask: (TaskModel*) task withMember: (UserModel*) user;
{
    writeToFB = [HenryFirebase getFirebaseObject];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"/projects/%@/milestones/%@/tasks/%@",task.parentProjectID, task.parentMilestoneID, task.taskID]];
    NSDictionary *newValue = @{@"assignedTo":user.key};
    [writeToFB updateChildValues:newValue];
    
}

// This was never implemented, so that needs to happen
-(void) getBounties:(NSString*) taskId{
    [self updateDataSnapshot];
}



@end
