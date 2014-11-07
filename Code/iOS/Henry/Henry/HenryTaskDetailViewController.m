//
//  HenryTaskDetailViewController.m
//  Henry
//
//  Created by Carter Grove on 10/17/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#define kOFFSET_FOR_KEYBOARD 80.0

#import "HenryTaskDetailViewController.h"
#import "HenryTaskStatusTableViewController.h"
#import "HenryFirebase.h"
#import "HenryAssignDevsTableViewController.h"

@interface HenryTaskDetailViewController ()
@property Firebase *fb;
@property double originalTimeEstimate;
@property double currentTimeEstimate;
@property double hoursSpent;
@property FDataSnapshot* snapshot;
@end

@implementation HenryTaskDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.fb = [HenryFirebase getFirebaseObject];
    // Attach a block to read the data at our posts reference
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateInfo: snapshot];
        self.snapshot = snapshot;
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    
}

-(void)viewDidAppear:(BOOL)animated {
    [self updateInfo: self.snapshot];
}

- (IBAction)updateCurrentTimeEstimate:(id)sender {
    bool status;
    NSScanner *scanner;
    double result;
    
    scanner = [NSScanner scannerWithString:self.currentEstimateField.text];
    status = [scanner scanDouble:&result];
    status = status && scanner.scanLocation == self.currentEstimateField.text.length;
    
    if (status) {
        NSDictionary *newValue = @{@"updated_time_estimate":[NSNumber numberWithDouble:[self.currentEstimateField.text doubleValue]]};
        self.fb = [self.fb childByAppendingPath:[NSString stringWithFormat:@"projects/%@/milestones/%@/tasks/%@", self.ProjectID, self.MileStoneID, self.taskID]];
        [self.fb updateChildValues:newValue];
        self.fb = [HenryFirebase getFirebaseObject];
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Estimated hours must be a number" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }

}

-(void)updateInfo:(FDataSnapshot *)snapshot {

    
    NSDictionary *json = snapshot.value[@"projects"][self.ProjectID][@"milestones"][self.MileStoneID][@"tasks"][self.taskID];
    
    NSDictionary *jsonForName = snapshot.value[@"users"][[json objectForKey:@"assignedTo"]];
    
    self.taskNameLabel.text = [json objectForKey:@"name"];
    self.descriptionView.text = [json objectForKey:@"description"];
    [self.statusButton setTitle:[json objectForKey:@"status"] forState:UIControlStateNormal];
    [self.statusButton setTitle:[json objectForKey:@"status"] forState:UIControlStateHighlighted];
    
    [self.assigneeNameLabel setText:[jsonForName objectForKey:@"name"]];
    self.originalTimeEstimate = [[json objectForKey:@"original_time_estimate"] integerValue];;
    self.currentTimeEstimate = [[NSString stringWithFormat:@"%@",[json objectForKey:@"updated_time_estimate"]] doubleValue];
    self.hoursSpent = [[json objectForKey:@"total_time_spent"] integerValue];
    self.originalEstimateLabel.text = [NSString stringWithFormat:@"%.2f", (double)self.originalTimeEstimate];
    self.currentEstimateField.text = [NSString stringWithFormat:@"%.2f", (double)self.currentTimeEstimate];
    self.hoursLabel.text = [NSString stringWithFormat:@"%.2f/%.2f hours", (double)self.hoursSpent, (double)self.currentTimeEstimate];

    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if([segue.identifier isEqualToString:@"projectStatus"]){
        HenryTaskStatusTableViewController *vc = [segue destinationViewController];
        vc.initialSelection = self.statusButton.titleLabel.text;
        vc.detailView = self;
        vc.taskID = self.taskID;
        vc.milestoneID = self.MileStoneID;
        vc.taskID = self.taskID;
        vc.projectID = self.ProjectID;
    }else{
        HenryAssignDevsTableViewController *vc = [segue destinationViewController];
        vc.initialSelection = self.statusButton.titleLabel.text;
        //vc.detailView = self;
        vc.taskID = self.taskID;
        vc.milestoneID = self.MileStoneID;
        vc.taskID = self.taskID;
        vc.projectID = self.ProjectID;
        vc.initialSelection = self.assigneeNameLabel.text;
    }
    
}

@end
