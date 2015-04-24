//
//  HenryTasksTableViewController.h
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryTasksTableViewController : UITableViewController <UIAlertViewDelegate>

@property NSString *ProjectID;
@property NSString *milestoneName;
@property NSString *MileStoneID;
@property NSArray *userTasks;
@property NSString *uid;
- (IBAction)addTask:(id)sender;
@end
