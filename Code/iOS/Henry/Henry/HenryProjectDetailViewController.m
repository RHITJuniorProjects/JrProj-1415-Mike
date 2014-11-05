//
//  HenryProjectDetailViewController.m
//  Henry
//
//  Created by Grove, Carter J on 10/19/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryProjectDetailViewController.h"
#import "HenryFirebase.h"
#import "HenryMilestonesTableViewController.h"

@interface HenryProjectDetailViewController ()
@property Firebase *fb;
@end

@implementation HenryProjectDetailViewController

-(void)viewWillAppear:(BOOL)animated {
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
        if (self.projectID == nil) {
            self.fb = [HenryFirebase getFirebaseObject];
            [self.fb observeSingleEventOfType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
                NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
                self.uid = [defaults objectForKey:@"id"];
                NSArray *projects = [snapshot.value[@"users"][self.uid][@"projects"] allKeys];
                self.projectID = [projects objectAtIndex:0];
                [self updateInfo:snapshot];
            } withCancelBlock:^(NSError *error) {
                NSLog(@"%@", error.description);
            }];
        }
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.fb = [HenryFirebase getFirebaseObject];
    
    
    // Attach a block to read the data at our posts reference
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateInfo: snapshot];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    NSMutableArray *dataArray = [[NSMutableArray alloc] init];
    
    for(int i =0;i<4;i++){
        NSNumber *num = [NSNumber numberWithInt:1];
        [dataArray addObject:num];
    }
    
    [self.pieChart renderInLayer:self.pieChart dataArray:dataArray];
    
}

- (IBAction)segControlClicked:(id)sender
{
    
    //Figures out the last clicked segment.
    int clickedSegment = [sender selectedSegmentIndex];
    if(clickedSegment == 0){
        self.pieChart.hidden = YES;
    }else{
        self.pieChart.hidden = NO;
    }
    
}

-(void)updateInfo:(FDataSnapshot *)snapshot {
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    NSDictionary *json = snapshot.value[@"projects"][self.projectID];
    
    self.projectNameLabel.text = [json objectForKey:@"name"];
    self.projectDescriptionView.text = [json objectForKey:@"description"];
    self.dueDateLabel.text = [json objectForKey:@"due_date"];
    double totalHours = [[json objectForKey:@"total_hours"] doubleValue];
    double estimatedHours = [[json objectForKey:@"total_estimated_hours"] doubleValue];
    self.hoursLoggedLabel.text = [NSString stringWithFormat:@"%0.2f/%0.2f", totalHours, estimatedHours];
    NSLog(@"%0.2f", totalHours/estimatedHours);
    self.hoursLoggedProgressBar.progress = totalHours/estimatedHours;
    self.tasksCompletedLabel.text = [NSString stringWithFormat:@"%@/%@",[json objectForKey:@"tasks_completed"],[json objectForKey:@"total_tasks"]];
    self.tasksCompletedProgressBar.progress = [[json objectForKey:@"task_percent"] doubleValue] / 100;
    self.milestonesCompletedLabel.text = [NSString stringWithFormat:@"%@/%@",[json objectForKey:@"milestones_completed"],[json objectForKey:@"total_milestones"]];
    self.milestonesCompletedProgressBar.progress = [[json objectForKey:@"milestone_percent"] doubleValue] / 100.0;
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
    vc.uid = self.uid;
}

@end
