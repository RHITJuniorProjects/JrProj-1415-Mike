//
//  HenryTaskDetailViewController.m
//  Henry
//
//  Created by Carter Grove on 10/17/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#define kOFFSET_FOR_KEYBOARD 80.0

#import "HenryTaskDetailViewController.h"
#import "HenryTaskCategoryTableViewController.h"
#import "HenryFirebase.h"
#import "HenryAssignDevsTableViewController.h"
#import "HenryCreateBountyViewController.h"
#import "HenryCategoryTableViewController.h"

@interface HenryTaskDetailViewController ()
@property HenryFirebase* henryFB;
@property double originalTimeEstimate;
@property double currentTimeEstimate;
@property double hoursSpent;
@property NSString *due_date;
@end

@implementation HenryTaskDetailViewController

- (void)viewDidLoad {
    @try{
        [super viewDidLoad];
        self.henryFB = [HenryFirebase new];
        
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
    
}

-(void)viewWillAppear:(BOOL)animated{
    [self updateInfo];
}
-(void)viewWillDisappear:(BOOL)animated{

    [self.henryFB removeAllObservers];
}

- (IBAction)updateCurrentTimeEstimate:(id)sender {
    @try{
        bool status;
        NSScanner *scanner;
        double result;
        
        scanner = [NSScanner scannerWithString:self.currentEstimateField.text];
        status = [scanner scanDouble:&result];
        status = status && scanner.scanLocation == self.currentEstimateField.text.length;
        
        if (status) {
            NSNumber* newEstimate = [NSNumber numberWithDouble:[self.currentEstimateField.text doubleValue]];
            [self.henryFB updateTimeEstimateOnTaskKey:self.taskID milestoneKey:self.MileStoneID projectKey:self.ProjectID newTimeEstimate:newEstimate];
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Estimated hours must be a number" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
            [alert show];
            return;
        }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
}

-(void)updateInfo {
    @try{
        [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
        [self.henryFB getTasksWithMilestoneKey:self.MileStoneID projectId:self.ProjectID withBlock:^(NSDictionary *tasksDictionary, BOOL success, NSError *error) {
            NSDictionary* task = tasksDictionary[self.taskID];
            [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
            [self.henryFB getAllUsersWithBlock:^(NSDictionary *usersDictionary, BOOL success, NSError *error) {
                NSDictionary* userAssignedToTask = usersDictionary[task[@"assignedTo"]];
                
                self.due_date = [task objectForKey:@"due_date"];
                self.taskNameLabel.text = [task objectForKey:@"name"];
                self.descriptionView.text = [task objectForKey:@"description"];
                [self.statusButton setTitle:[task objectForKey:@"category"] forState:UIControlStateNormal];
                [self.categoryButton setTitle:task[@"status"] forState:UIControlStateNormal];
                [self.statusButton setTitle:[task objectForKey:@"category"] forState:UIControlStateHighlighted];
                
                [self.assigneeNameLabel setText:[userAssignedToTask objectForKey:@"name"]];
                self.originalTimeEstimate = [[task objectForKey:@"original_time_estimate"] integerValue];;
                self.currentTimeEstimate = [[NSString stringWithFormat:@"%@",[task objectForKey:@"updated_time_estimate"]] doubleValue];
                self.hoursSpent = [[task objectForKey:@"total_time_spent"] integerValue];
                self.originalEstimateLabel.text = [NSString stringWithFormat:@"%.2f", (double)self.originalTimeEstimate];
                self.currentEstimateField.text = [NSString stringWithFormat:@"%.2f", (double)self.currentTimeEstimate];
                self.hoursLabel.text = [NSString stringWithFormat:@"%.2f/%.2f hours", (double)self.hoursSpent, (double)self.currentTimeEstimate];
                
                [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
            }];
            [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        }];
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        NSString *username = [defaults objectForKey:@"id"];
        [self.henryFB getMembersOnProjectWithProjectID:self.ProjectID withBlock:^(NSDictionary *usersDictionary, BOOL success, NSError *error) {
            NSDictionary* permissions = usersDictionary;
            if ([[permissions objectForKey:username] isEqualToString:@"Lead"]) {
                self.bountyButton.hidden = NO;
            }
        }];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (void)didReceiveMemoryWarning {
    @try{
        [super didReceiveMemoryWarning];
        // Dispose of any resources that can be recreated.
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    @try{
        if([segue.identifier isEqualToString:@"projectStatus"]){
            HenryTaskCategoryTableViewController *vc = [segue destinationViewController];
            vc.initialSelection = self.statusButton.titleLabel.text;
            vc.detailView = self;
            vc.taskID = self.taskID;
            vc.milestoneID = self.MileStoneID;
            vc.taskID = self.taskID;
            vc.projectID = self.ProjectID;
        } else if([segue.identifier isEqualToString:@"CreateBounty"]){
            HenryCreateBountyViewController *vc = [segue destinationViewController];
            vc.projectId = self.ProjectID;
            vc.milestoneId = self.MileStoneID;
            vc.taskId = self.taskID;
            
        }else if ([segue.identifier isEqualToString:@"projectCategory"]) {
            HenryCategoryTableViewController *vc = [segue destinationViewController];
            vc.initialSelection = self.categoryButton.titleLabel.text;
            vc.detailView = self;
            vc.taskID = self.taskID;
            vc.milestoneID = self.MileStoneID;
            vc.taskID = self.taskID;
            vc.projectID = self.ProjectID;
        } else {
            HenryAssignDevsTableViewController *vc = [segue destinationViewController];
            vc.initialSelection = self.statusButton.titleLabel.text;
            //vc.detailView = self;
            vc.taskID = self.taskID;
            vc.milestoneID = self.MileStoneID;
            vc.taskID = self.taskID;
            vc.projectID = self.ProjectID;
            vc.initialSelection = self.assigneeNameLabel.text;
            
        }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
}

- (IBAction)bountyButtonPressed:(id)sender {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Set Bounty"
                                                    message:nil
                                                   delegate:self
                                          cancelButtonTitle:@"Cancel"
                                          otherButtonTitles:@"Set", nil];
    [alert setAlertViewStyle:UIAlertViewStylePlainTextInput];
    [alert textFieldAtIndex:0].placeholder = @"Number of Bounty Points";
    [alert textFieldAtIndex:0].keyboardType = UIKeyboardTypeDecimalPad;
    
    [alert show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    @try{
        if (buttonIndex == 1) {
            NSString *pointsString = [alertView textFieldAtIndex:0].text;
            NSNumber *pointsNumber = [NSNumber numberWithInteger:[pointsString integerValue]];
            if ([pointsString length] > 0) {
                [self.henryFB createBountyWithName:@"Completion Bounty" DueDate:self.due_date HourLimit:@"None" LineLimit:@"None" PointValue:pointsNumber ProjectKey:self.ProjectID MilestoneKey:self.MileStoneID TaskKey:self.taskID];
            } else {
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Invalid Input"
                                                                message:@"You have an empty field."
                                                               delegate:nil
                                                      cancelButtonTitle:@"OK"
                                                      otherButtonTitles:nil];
                [alert show];
            }
        }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);

    }
}

@end
