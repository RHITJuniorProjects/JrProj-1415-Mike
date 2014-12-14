//
//  HenryMilestonesTableViewController.h
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryMilestonesTableViewController : UITableViewController <UIAlertViewDelegate>
@property NSString *MilestoneID;
@property NSString *taskID;
@property NSString *ProjectID;
@property NSArray *tasks;
@property NSString *uid;
@property NSDictionary *milestones;
- (IBAction)addMilestones:(id)sender;
@end
