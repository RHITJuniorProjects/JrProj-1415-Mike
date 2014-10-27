//
//  HenryMilestoneDetailViewController.m
//  Henry
//
//  Created by Grove, Carter J on 10/19/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryMilestoneDetailViewController.h"
#import "HenryTasksTableViewController.h"
#import <Firebase/Firebase.h>

@interface HenryMilestoneDetailViewController ()
@property Firebase *fb;
@end

@implementation HenryMilestoneDetailViewController

@synthesize pieChart;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.fb = [[Firebase alloc] initWithUrl:[NSString stringWithFormat:@"https://henry-staging.firebaseio.com/projects/%@/milestones/%@", self.ProjectID, self.MileStoneID]];
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    // Attach a block to read the data at our posts reference
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateInfo];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    NSMutableArray *dataArray = [[NSMutableArray alloc] init];
    
    for(int i =0;i<15;i++){
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
        if(self.pieChart.hidden){
            self.pieChart.hidden = NO;
        }
    }else{
        
        self.pieChart.hidden = YES;
    }
    
}

-(void)updateInfo {
    NSString *urlString = [NSString stringWithFormat:@"https:henry-staging.firebaseio.com/projects/%@/milestones/%@.json", self.ProjectID, self.MileStoneID];
    NSURL *jsonURL = [NSURL URLWithString:urlString];
    NSData *data = [NSData dataWithContentsOfURL:jsonURL];
    NSError *error;
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
    
    self.milestoneNameLabel.text = [json objectForKey:@"name"];
    self.dueDateLabel.text = @"Not/In/Database";
    self.descriptionView.text = [json objectForKey:@"description"];
    
    urlString = [NSString stringWithFormat:@"https:henry-staging.firebaseio.com/projects/%@/milestones/%@/tasks.json", self.ProjectID, self.MileStoneID];
    jsonURL = [NSURL URLWithString:urlString];
    data = [NSData dataWithContentsOfURL:jsonURL];
    json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
    
    NSArray *taskIDs = [json allKeys];
    int completed = 0;
    int total = 0;
    for (NSString *taskID in taskIDs) {
        if ([[[json objectForKey:taskID] objectForKey:@"status"] isEqualToString:@"Closed"]) {
            completed++;
        }
        total++;
    }
    self.tasksCompletedLabel.text = [NSString stringWithFormat:@"%d/%d", completed, total];
    self.tasksCompleteBar.progress = completed/total;
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
    HenryTasksTableViewController *vc = [segue destinationViewController];
    vc.ProjectID = self.ProjectID;
    vc.MileStoneID = self.MileStoneID;
    vc.milestoneName = self.milestoneNameLabel.text;
    vc.userTasks = self.userTasks;
    vc.uid = self.uid;
}

@end
