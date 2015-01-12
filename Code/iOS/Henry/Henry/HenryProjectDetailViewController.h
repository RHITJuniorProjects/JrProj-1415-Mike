//
//  HenryProjectDetailViewController.h
//  Henry
//
//  Created by Grove, Carter J on 10/19/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DLPieChart.h"
#import "BEMSimpleLineGraphView.h"
@interface HenryProjectDetailViewController : UIViewController <BEMSimpleLineGraphDataSource, BEMSimpleLineGraphDelegate>
@property (weak, nonatomic) IBOutlet UILabel *projectNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *dueDateLabel;
@property (weak, nonatomic) IBOutlet UITextView *projectDescriptionView;
@property (weak, nonatomic) IBOutlet UILabel *hoursLoggedLabel;
@property (weak, nonatomic) IBOutlet UILabel *tasksCompletedLabel;
@property (weak, nonatomic) IBOutlet UILabel *milestonesCompletedLabel;
@property NSString *projectID;
@property (weak, nonatomic) IBOutlet UIProgressView *hoursLoggedProgressBar;
@property (weak, nonatomic) IBOutlet UIProgressView *tasksCompletedProgressBar;
@property (weak, nonatomic) IBOutlet UIProgressView *milestonesCompletedProgressBar;
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentControl;
-(IBAction)segControlClicked:(id)sender;
- (IBAction)ipadSegControlClicked:(id)sender;
@property (weak, nonatomic) IBOutlet UITableView *memberTableView;
@property (weak, nonatomic) IBOutlet UILabel *hoursHeader;
@property (weak, nonatomic) IBOutlet UILabel *tasksHeader;
@property (weak, nonatomic) IBOutlet UILabel *milestonesHeader;
@property NSArray *tasks;
@property NSString *uid;
@property (weak, nonatomic) IBOutlet DLPieChart *pieChart;
@property (weak, nonatomic) IBOutlet BEMSimpleLineGraphView *lineGraph;




@property NSMutableArray *names;
@property NSMutableArray *devs;
@property NSDictionary *assignableDevs;
@property NSDictionary *allDevs;
@property NSMutableArray *linesOfCode;
@property NSDictionary *members;
@property NSDictionary *allMembers;
@property NSDictionary *projectJson;
@end