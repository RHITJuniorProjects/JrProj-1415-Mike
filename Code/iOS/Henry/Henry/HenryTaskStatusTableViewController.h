//
//  HenryTaskStatusTableViewController.h
//  Henry
//
//  Created by Carter Grove on 10/17/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HenryTaskDetailViewController.h"

@interface HenryTaskStatusTableViewController : UITableViewController
@property NSString *projectID;
@property NSString *milestoneID;
@property NSString *taskID;
@property NSString *initialSelection;
@property HenryTaskDetailViewController *detailView;
@end
