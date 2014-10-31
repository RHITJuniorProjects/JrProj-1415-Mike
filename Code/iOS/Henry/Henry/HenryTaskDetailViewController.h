//
//  HenryTaskDetailViewController.h
//  Henry
//
//  Created by Carter Grove on 10/17/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HenryDevDisplayObject.h"
@interface HenryTaskDetailViewController : UIViewController
@property (weak, nonatomic) IBOutlet UILabel *taskNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *assigneeNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *hoursLabel;
@property (weak, nonatomic) IBOutlet UIButton *statusButton;
@property (weak, nonatomic) IBOutlet UITextView *descriptionView;
@property (weak, nonatomic) IBOutlet UILabel *originalEstimateLabel;
@property (weak, nonatomic) IBOutlet UITextField *currentEstimateField;
@property NSString *ProjectID;
@property NSString *MileStoneID;
@property NSString *taskID;
@property HenryDevDisplayObject *primaryDev;
@end
