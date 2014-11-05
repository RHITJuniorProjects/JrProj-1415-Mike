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
    //    NSString *urlString = [NSString stringWithFormat:@"https://henry-staging.firebaseio.com/projects/%@.json", self.projectID];
    //    NSURL *jsonURL = [NSURL URLWithString:urlString];
    //    NSData *data = [NSData dataWithContentsOfURL:jsonURL];
    //    NSError *error;
    //    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
    
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
    self.tasksCompletedProgressBar.progress = [[json objectForKey:@"task_percent"] intValue] / 100;
    self.milestonesCompletedLabel.text = [NSString stringWithFormat:@"%@/%@",[json objectForKey:@"milestones_completed"],[json objectForKey:@"total_milestones"]];
    self.milestonesCompletedProgressBar.progress = [[json objectForKey:@"milestonePercent"] intValue] / 100;
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    
    
    self.assignableDevs = snapshot.value[@"projects"][self.projectID][@"members"];
    NSMutableArray *dataArray = [[NSMutableArray alloc] init];
    
    self.allDevs = snapshot.value[@"users"];
    NSMutableArray *developers = [[NSMutableArray alloc] init];
    NSArray *keys = [self.assignableDevs allKeys];
    self.names = [[NSMutableArray alloc] init];
    for (NSString *key in keys) {
        NSString *name = [[self.allDevs objectForKey:key] objectForKey:@"name"];
        NSNumber *lines = [[[[self.allDevs objectForKey:key] objectForKey:@"projects"] objectForKey:self.projectID] objectForKey:@"added_lines_of_code"];
        //NSLog(key);
        if(name != NULL && lines!=0){
            [self.names addObject:name];
            [dataArray addObject:lines];
            [developers addObject:key];
        }
    }
    
    
    [self.pieChart renderInLayer:self.pieChart dataArray:dataArray nameArray:self.names];
    
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