//
//  HenryProjectDetailViewController.h
//  Henry
//
//  Created by Grove, Carter J on 10/19/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryProjectDetailViewController : UIViewController
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
@property NSArray *tasks;
@end
