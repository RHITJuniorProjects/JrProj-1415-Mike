//
//  HenryFirebase.m
//  Henry
//
//  Created by Schneider, Mason on 10/29/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryFirebase.h"
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
//    currentFB = henryFB.
    [HenryFirebase getFirebaseObject];
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

- (NSDictionary *) getAllProjects{
    [self updateDataSnapshot];
    NSDictionary *projects = self.snapshot.value[@"projects"];
    NSLog(@"%@",projects);
    return projects;
}

- (NSDictionary *) getMilestones:(NSString*) projectId{
    [self updateDataSnapshot];
    NSDictionary *milestones = self.snapshot.value[@"projects"][projectId][@"milestones"];
    return milestones;
}

- (NSDictionary *) getTasksForMilestone:(NSString*) milestoneId projectId:(NSString*) projectId{
    [self updateDataSnapshot];
    NSDictionary *tasks = self.snapshot.value[@"projects"][projectId][@"milestones"][milestoneId];
    return tasks;
}

- (NSDictionary *) getTasksForUser:(NSString*) userId{
    [self updateDataSnapshot];
    
    return nil ;
}

- (NSDictionary *) getAllTrophies {
    [self updateDataSnapshot];
    NSDictionary *trophies = self.snapshot.value[@"trophies"];
    return trophies;
}

-(NSArray*) getBounties:(NSString*) taskId{
    [self updateDataSnapshot];
    return nil ;
}



@end
