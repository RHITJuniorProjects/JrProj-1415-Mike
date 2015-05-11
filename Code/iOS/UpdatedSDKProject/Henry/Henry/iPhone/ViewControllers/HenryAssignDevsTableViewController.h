//
//  HenryAssignDevsTableViewController.h
//  Henry
//
//  Created by Trizna, Kevin J on 10/20/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HenryTaskDetailViewController.h"

@interface HenryAssignDevsTableViewController : UITableViewController
@property NSMutableArray *developers;
@property NSMutableArray *names;
@property NSMutableArray *developerObjects;
@property NSString *projectID;
@property NSString *milestoneID;
@property NSString *taskID;
@property HenryTaskDetailViewController *detailView;
@property NSDictionary *assignableDevs;
@property NSDictionary *allDevs;
@property NSString *initialSelection;
@end