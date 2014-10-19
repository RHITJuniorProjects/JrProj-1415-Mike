//
//  HenryProjectDetailViewController.m
//  Henry
//
//  Created by Grove, Carter J on 10/19/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryProjectDetailViewController.h"
#import <Firebase/Firebase.h>
#import "HenryMilestonesTableViewController.h"

@interface HenryProjectDetailViewController ()
@property Firebase *fb;
@end

@implementation HenryProjectDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.fb = [[Firebase alloc] initWithUrl:[NSString stringWithFormat:@"https://henry-test.firebaseio.com/projects/%@", self.projectID]];
    
    // Attach a block to read the data at our posts reference
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateInfo];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
}

-(void)updateInfo {
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    NSString *urlString = [NSString stringWithFormat:@"https://henry-test.firebaseio.com/projects/%@.json", self.projectID];
    NSURL *jsonURL = [NSURL URLWithString:urlString];
    NSData *data = [NSData dataWithContentsOfURL:jsonURL];
    NSError *error;
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
    
    self.projectNameLabel.text = [json objectForKey:@"name"];
    self.projectDescriptionView.text = [json objectForKey:@"description"];
    self.dueDateLabel.text = @"Not/In/Database";
    double totalHours = [[json objectForKey:@"total_hours"] doubleValue];
    double estimatedHours = [[json objectForKey:@"total_estimated_hours"] doubleValue];
    self.hoursLoggedLabel.text = [NSString stringWithFormat:@"%0.2f/%0.2f", totalHours, estimatedHours];
    NSLog(@"%0.2f", totalHours/estimatedHours);
    self.hoursLoggedProgressBar.progress = totalHours/estimatedHours;
    self.tasksCompletedLabel.text = @"Ask/Platform";
    self.tasksCompletedProgressBar.progress = [[json objectForKey:@"task_percent"] intValue] / 100;
    self.milestonesCompletedLabel.text = @"Ask/Platform";
    self.milestonesCompletedProgressBar.progress = [[json objectForKey:@"milestonePercent"] intValue] / 100;
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
    HenryMilestonesTableViewController *vc = [segue destinationViewController];
    vc.ProjectID = self.projectID;
    vc.tasks = self.tasks;
}

@end
