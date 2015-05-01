//
//  HenryCategoryTableViewController.h
//  Henry
//
//  Created by Carter Grove on 2/15/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HenryTaskDetailViewController.h"

@interface HenryCategoryTableViewController : UITableViewController
@property NSString *projectID;
@property NSString *milestoneID;
@property NSString *taskID;
@property NSString *initialSelection;
@property HenryTaskDetailViewController *detailView;
@end
