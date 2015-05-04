//
//  HenryFirebase.m
//  Henry
//
//  Created by Schneider, Mason on 10/29/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryFirebase.h"
#import "BountyModel.h"
#import "MilestoneModel.h"
#import "ProjectModel.h"
#import "TaskModel.h"
#import "TrophyModel.h"
#import "UserModel.h"

#import <UIKit/UIKit.h>
@interface HenryFirebase()
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
    Firebase* firebaseInstance =[[Firebase alloc] initWithUrl:[HenryFirebase getFirebaseURL]];
        return firebaseInstance;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

+(void)setFirebaseReadingObjectTo: (Firebase*) fb;
{
    henryFB = fb;
}

+(void)setFirebaseWritingObjectTo: (Firebase*) fb;
{
    writeToFB = fb;
}

// Tested
+(NSString *)getFirebaseURL {
    return @"https://henry-test.firebaseio.com";
}

-(id)init {
    if ( self = [super init] ) {
        henryFB = [[Firebase alloc] initWithUrl:[HenryFirebase getFirebaseURL]];
    }
    return self;
}

- (void) authenticateUser: (NSString*) email password: (NSString*) password withBlock:(AuthenticationCallback) completionBlock {
    [henryFB authUser:email password:password withCompletionBlock:^(NSError *error, FAuthData *authData) {
        completionBlock(error, authData);
    }];
}

- (void) getAllProjectModelsWithBlock:(ProjectCallback)completionBlock {
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *rawProjects = snapshot.value[@"projects"];
        NSMutableDictionary *projectModels = [NSMutableDictionary new];
        NSArray *projectKeys = [rawProjects allKeys];
        for (NSString* key in projectKeys) {
            ProjectModel* tempProject = [ProjectModel constructModelFromDictionary:[rawProjects objectForKey:key]];
            [projectModels setObject:tempProject forKey:key];
        }
        completionBlock(rawProjects, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}

- (void) getAllProjectsWithBlock:(ProjectCallback) completionBlock {
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *projects = snapshot.value[@"projects"];
        completionBlock(projects, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}

- (void) getProjectsUserIsOnWithUserKey: (NSString*) userId withBlock:(ProjectCallback) completionBlock;
{
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *projects = snapshot.value[@"users"][userId][@"projects"];
        completionBlock(projects, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}

- (void) getMilestonesWithProjectId:(NSString*) projectId withBlock:(MilestonesCallback) completionBlock {
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *milestones = snapshot.value[@"projects"][projectId][@"milestones"];
        completionBlock(milestones, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}

- (void) getMilestoneWithMilestoneKey:(NSString*) milestoneId projectId:(NSString*) projectId withBlock:(MilestoneCallback) completionBlock;
{
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *tasks = snapshot.value[@"projects"][projectId][@"milestones"][milestoneId];
        completionBlock(tasks, YES, nil);
    } withCancelBlock:^(NSError *error) {
        completionBlock(nil,NO,error);
    }];
}

- (void) getTasksWithMilestoneKey:(NSString*) milestoneId projectId:(NSString*) projectId withBlock:(TasksForMilestoneCallback) completionBlock;
{
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        NSDictionary *tasks = snapshot.value[@"projects"][projectId][@"milestones"][milestoneId][@"tasks"];
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

- (void) getTrophiesBelongingToUserId: (NSString*) userId withBlock: (TrophiesCallback) completionBlock;
{
    [henryFB observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        // Contains all trophies. Each value is another dictionary with name key, description key, etc...
        NSDictionary* allTrophies = snapshot.value[@"trophies"];
        
        // Literally only contains key-values pairs where the key is the key for the trophie, and the value is the name. Lame.
        NSDictionary* userTrophies = snapshot.value[@"users"][userId][@"trophies"];
        
        NSMutableArray* userTrophiesKeys = [[userTrophies allKeys] mutableCopy];
        [userTrophiesKeys removeObject:@"placeholder"];
        NSMutableDictionary* userTrophiesWithAllValues = [NSMutableDictionary new];
        
        for (NSString* key in userTrophiesKeys) {
            [userTrophiesWithAllValues setObject:[allTrophies objectForKey:key] forKey:key];
        }
        
        completionBlock(userTrophiesWithAllValues, YES, nil);
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

- (void) purchaseTrophyWithTrophyModel: (TrophyModel*) trophy withUserId: (NSString*) userid withOldAvailablePoints: (NSNumber*) oldAvailPoints;
{
    writeToFB = [HenryFirebase getFirebaseObject];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"/users/%@/", userid]];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"trophies/%@",trophy.key]];
    [writeToFB setValue:trophy.name];
    writeToFB = [HenryFirebase getFirebaseObject];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"/users/%@/available_points", userid]];
    
    long currentAvailPoints = [oldAvailPoints longValue];
    currentAvailPoints = currentAvailPoints - [trophy.cost integerValue];
    NSNumber* newAvailablePoints = [[NSNumber alloc] initWithLong:currentAvailPoints];
    NSString* newAvailPointString = [newAvailablePoints stringValue];
    NSLog(@"DaNew Available POints: %@", newAvailPointString);
    [writeToFB setValue:newAvailablePoints];
}

- (void) assignMemberToTaskWithTaskKey: (NSString*) taskId UserKey: (NSString*) userId ProjectKey: (NSString*) projectId MilestoneKey: (NSString*) milestoneId;
{
    NSDictionary *newValue = @{@"assignedTo":userId};
    writeToFB = [HenryFirebase getFirebaseObject];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"/projects/%@/milestones/%@/tasks/%@",projectId, milestoneId, taskId]];
    [writeToFB updateChildValues:newValue];
    
}

- (void) assignStatusToTaskWithTaskKey: (NSString*) taskId StatusKey: (NSString*) statusId ProjectKey: (NSString*) projectId MilestoneKey: (NSString*) milestoneId;
{
    NSDictionary* newValue = @{@"status":statusId};
    writeToFB = [HenryFirebase getFirebaseObject];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"projects/%@/milestones/%@/tasks/%@", projectId, milestoneId, taskId]];
    [writeToFB updateChildValues:newValue];
}

- (void) createBountyWithName: (NSString*) name DueDate: (NSString*) dueDate HourLimit: (id) hourLimit LineLimit: (id) lineLimit PointValue: (NSNumber*) pointValue ProjectKey: (NSString*) projectId MilestoneKey: (NSString*) milestoneId TaskKey: (NSString*)taskId;
{
    writeToFB = [HenryFirebase getFirebaseObject];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"projects/%@/milestones/%@/tasks/%@/bounties/", projectId, milestoneId, taskId]];
    writeToFB = [writeToFB childByAutoId];
    
    NSMutableDictionary *newData = [[NSMutableDictionary alloc] init];
    [newData setObject:@"None" forKey:@"claimed"];
    [newData setObject:@"None" forKey:@"description"];
    [newData setObject:pointValue forKey:@"points"];
    [newData setObject:name forKey:@"name"];
    [newData setObject:dueDate forKey:@"due_date"];
    [newData setObject:hourLimit forKey:@"hour_limit"];
    [newData setObject:lineLimit forKey:@"line_limit"];
    [writeToFB setValue: newData];
}

- (void) createMilestoneWithName: (NSString*) name Description: (NSString*) description DueDate: (NSString*) dueDate OnProjectWithProjectKey: (NSString*) projectId;
{
    writeToFB = [HenryFirebase getFirebaseObject];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"projects/%@/milestones", projectId]];
    writeToFB = [writeToFB childByAutoId];
    
    NSDictionary *milestone = @{
                                @"name": name,
                                @"description": description,
                                @"due_date": dueDate
                                };
    [writeToFB setValue:milestone];
}

- (void) createTaskWithName: (NSString*) name description: (NSString*) desc assignedToUserKey: (NSString*) userId dueDate: (NSString*) dueDate status: (NSString*) status hourEstimate: (id) hourEstimate category: (NSString*) category projectKey: (NSString*) projectId milestoneKey: (NSString*) milestoneId;
{
    NSString *urlString = [NSString stringWithFormat:@"projects/%@/milestones/%@/tasks", projectId, milestoneId];
    writeToFB = [HenryFirebase getFirebaseObject];
    writeToFB = [writeToFB childByAppendingPath: urlString];
    writeToFB = [writeToFB childByAutoId];
    
    NSDictionary *task = @{
                           @"name": name,
                           @"description": desc,
                           @"assignedTo": userId,
                           @"due_date": dueDate,
                           @"status": status,
                           @"original_hour_estimate":hourEstimate,
                           @"category":category
                           };
    [writeToFB setValue:task];
    
}

- (void) updateTimeEstimateOnTaskKey: (NSString*) taskId milestoneKey: (NSString*) milestoneId projectKey: (NSString*) projectId newTimeEstimate: (NSNumber*) newTimeEstimate;
{
    NSDictionary* newValue = @{@"updated_time_estimate":newTimeEstimate};
    writeToFB = [HenryFirebase getFirebaseObject];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"projects/%@/milestones/%@/tasks/%@", projectId, milestoneId, taskId]];
    [writeToFB updateChildValues:newValue];
}

- (void) updateTaskWithNewDictionary: (NSDictionary*) newTaskValues taskKey: (NSString*) taskId milestoneKey: (NSString*) milestoneId projectKey: (NSString*) projectId;
{
    writeToFB = [HenryFirebase getFirebaseObject];
    writeToFB = [writeToFB childByAppendingPath:[NSString stringWithFormat:@"projects/%@/milestones/%@/tasks/%@", projectId, milestoneId, taskId]];
    [writeToFB updateChildValues:newTaskValues];
}

- (void) removeAllObservers;
{
    [henryFB removeAllObservers];
    [writeToFB removeAllObservers];
}

@end
