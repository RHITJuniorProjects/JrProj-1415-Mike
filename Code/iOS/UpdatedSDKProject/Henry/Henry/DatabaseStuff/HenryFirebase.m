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

- (void) getAllTrophiesWithBlock: (TrophiesCallback) completionBlock {
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *trophies = snapshot.value[@"trophies"];
        completionBlock(trophies, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}

- (void) getUserInfoWithUserId: (NSString*) userid withBlock: (UserInfoCallback) completionBlock {
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *userInfo = snapshot.value[@"users"][userid];
        completionBlock(userInfo, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}

// This was never implemented, so that needs to happen
-(void) getBounties:(NSString*) taskId{
    [self updateDataSnapshot];
}



@end
