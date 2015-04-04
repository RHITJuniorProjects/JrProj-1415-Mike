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

@interface HenryFirebase : Firebase

+(Firebase *)getFirebaseObject;
+ (NSString *)getFirebaseURL;
- (void) updateDataSnapshot;
- (NSDictionary *) getAllProjects;
- (NSDictionary *) getMilestones:(NSString*) projectId;
- (NSDictionary *) getTasksForMilestone:(NSString*) milestoneId projectId:(NSString*) projectId;
- (NSDictionary *) getTasksForUser:(NSString*) userId;
- (NSDictionary *) getAllTrophies;
- (NSArray*) getBounties:(NSString*) taskId;
@end
