//
//  HenryProjectsTableViewController.h
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryProjectsTableViewController : UITableViewController
@property NSString *uid;
@property (weak, nonatomic) IBOutlet UISegmentedControl *sortSelector;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *navButton;
- (IBAction)segControlClicked:(id)sender;
- (IBAction)logoutButtonPressed:(id)sender;
@end
