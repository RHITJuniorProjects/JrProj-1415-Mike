//
//  HenryMilestoneDetailViewController.h
//  Henry
//
//  Created by Grove, Carter J on 10/19/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DLPieChart.h"
#import "BEMSimpleLineGraphView.h"
@interface HenryMilestoneDetailViewController : UIViewController <BEMSimpleLineGraphDataSource, BEMSimpleLineGraphDelegate>
@property NSString *ProjectID;
@property NSString *MileStoneID;
@property NSString *milestoneName;
@property (weak, nonatomic) IBOutlet UILabel *milestoneNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *dueDateLabel;
@property (weak, nonatomic) IBOutlet UITextView *descriptionView;
@property (weak, nonatomic) IBOutlet UILabel *tasksCompletedLabel;
@property (strong, nonatomic) IBOutlet UIProgressView *tasksCompleteBar;
@property NSArray *userTasks;
@property NSString *uid;
@property (nonatomic, retain) IBOutlet DLPieChart *pieChart;
@property (weak, nonatomic) IBOutlet UISegmentedControl *dataSelector;
- (IBAction)segControlClicked:(id)sender;
@property NSDictionary *assignableDevs;
@property NSDictionary *allDevs;
@property NSMutableArray *names;
@property NSMutableArray *devs;
@property (weak, nonatomic) IBOutlet BEMSimpleLineGraphView *burndown;
@property (weak, nonatomic) IBOutlet UILabel *tasksHeader;



@end