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
#import <Firebase/Firebase.h>

@interface HenryTaskDetailViewController ()
@property Firebase *fb;
@property NSInteger originalTimeEstimate;
@property NSInteger currentTimeEstimate;
@property NSInteger hoursSpent;
@end

@implementation HenryTaskDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.fb = [[Firebase alloc] initWithUrl:@"https://henry-test.firebaseio.com/projects/"];
    // Attach a block to read the data at our posts reference
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateInfo];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
}

-(void)updateInfo {
    NSString *urlString = [NSString stringWithFormat:@"https:henry-test.firebaseio.com/projects/%@/milestones/%@/tasks/%@.json", self.ProjectID, self.MileStoneID, self.taskID];
    NSURL *jsonURL = [NSURL URLWithString:urlString];
    NSData *data = [NSData dataWithContentsOfURL:jsonURL];
    NSError *error;
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
    
    self.taskNameLabel.text = [json objectForKey:@"name"];
    self.descriptionView.text = [json objectForKey:@"description"];
    self.statusButton.titleLabel.text = [json objectForKey:@"status"];
    self.assigneeNameLabel.text = [json objectForKey:@"assignedTo"];
    self.originalTimeEstimate = [[json objectForKey:@"original_time_estimate"] integerValue];;
    self.currentTimeEstimate = [[NSString stringWithFormat:@"%@",[json objectForKey:@"updated_time_estimate"]] integerValue];
    self.hoursSpent = [[json objectForKey:@"total_time_spent"] integerValue];
    self.originalEstimateLabel.text = [NSString stringWithFormat:@"%d", self.originalTimeEstimate];
    self.currentEstimateField.text = [NSString stringWithFormat:@"%d", self.currentTimeEstimate];
    
    self.hoursLabel.text = [NSString stringWithFormat:@"%d/%d hours", self.hoursSpent, self.currentTimeEstimate];
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    HenryTaskStatusTableViewController *vc = [segue destinationViewController];
    vc.initialSelection = self.statusButton.titleLabel.text;
    vc.detailView = self;
}

@end
