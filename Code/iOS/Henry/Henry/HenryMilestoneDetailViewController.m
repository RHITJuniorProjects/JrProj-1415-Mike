//
//  HenryMilestoneDetailViewController.m
//  Henry
//
//  Created by Grove, Carter J on 10/19/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryMilestoneDetailViewController.h"
#import "HenryTasksTableViewController.h"
#import "HenryFirebase.h"
#import "BEMSimpleLineGraphView.h"

@interface HenryMilestoneDetailViewController ()
@property Firebase *fb;
@property NSMutableArray *burndownData;
@end

@implementation HenryMilestoneDetailViewController

@synthesize pieChart;

- (void)viewDidLoad {
    @try{
        [super viewDidLoad];
        // Do any additional setup after loading the view.
        self.fb = [HenryFirebase getFirebaseObject];
        [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
        
        self.burndownData = [[NSMutableArray alloc] init];
        self.burndown.enableYAxisLabel = YES;
        self.burndown.enableXAxisLabel = YES;
        self.burndown.enableBezierCurve = NO;
        
        // Attach a block to read the data at our posts reference
        [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
            [self updateInfo: snapshot];
        } withCancelBlock:^(NSError *error) {
            NSLog(@"%@", error.description);
        }];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
}

-(NSInteger)numberOfPointsInLineGraph:(BEMSimpleLineGraphView *)graph {
    return [self.burndownData count];
}

-(NSInteger)numberOfYAxisLabelsOnLineGraph:(BEMSimpleLineGraphView *)graph {
    return 7;
}

-(CGFloat)lineGraph:(BEMSimpleLineGraphView *)graph valueForPointAtIndex:(NSInteger)index {
    NSArray *subArray = [self.burndownData objectAtIndex:index];
    return [[subArray objectAtIndex:1] floatValue];
}

- (IBAction)segControlClicked:(id)sender
{
    @try{
    //Figures out the last clicked segment.
    int clickedSegment = [sender selectedSegmentIndex];
    if(clickedSegment == 0){
        if(self.pieChart.hidden){
            self.pieChart.hidden = NO;
        }
        self.tasksHeader.hidden = NO;
        self.burndown.hidden = YES;

    }else{
        self.pieChart.hidden = YES;
        self.tasksHeader.hidden = YES;
        self.burndown.hidden = NO;
    }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
}

-(NSString *)lineGraph:(BEMSimpleLineGraphView *)graph labelOnXAxisForIndex:(NSInteger)index {
    NSArray *subArray = [self.burndownData objectAtIndex:index];
    NSDate *date = [subArray objectAtIndex:0];
    
    NSCalendar* calendar = [NSCalendar currentCalendar];
    NSDateComponents* components = [calendar components:NSYearCalendarUnit|NSMonthCalendarUnit|NSDayCalendarUnit fromDate:date];
    
//    if (index == 5 || index == [self.burndownData count] - 6) {
        NSString *dateStr = [NSString stringWithFormat:@"%d/%d", [components month], [components day]];
        NSLog(@"%@", dateStr);
        return dateStr;
////        return @"1";
//    } else
//        return @"";
}

-(NSInteger)numberOfGapsBetweenLabelsOnLineGraph:(BEMSimpleLineGraphView *)graph {
    return 5;
}

-(void)updateInfo:(FDataSnapshot *)snapshot {
    @try{
    NSDictionary *json = snapshot.value[@"projects"][self.ProjectID][@"milestones"][self.MileStoneID];
    
        NSDictionary *burndownData = [json objectForKey:@"burndown_data"];
        NSArray *burndownKeys = [burndownData allKeys];
        burndownKeys = [[burndownKeys reverseObjectEnumerator] allObjects];
        self.burndownData = [[NSMutableArray alloc] init];
        for (NSString *burndownKey in burndownKeys) {
            NSMutableArray *subArray = [[NSMutableArray alloc] init];
            NSDictionary *entry = [burndownData objectForKey:burndownKey];
            NSDate *date = [NSDate dateWithTimeIntervalSince1970:[[entry objectForKey:@"timestamp"] intValue]];
            [subArray addObject:date];
            [subArray addObject:[entry objectForKey:@"estimated_hours_remaining"]];
            [subArray addObject:[entry objectForKey:@"hours_completed"]];
            [self.burndownData addObject:subArray];
        }
        
    self.milestoneNameLabel.text = [json objectForKey:@"name"];
    self.dueDateLabel.text = [json objectForKey:@"due_date"];
    self.descriptionView.text = [json objectForKey:@"description"];
    self.tasksCompletedLabel.text = [NSString stringWithFormat:@"%@/%@", [json objectForKey:@"tasks_completed"],[json objectForKey:@"total_tasks"]];
    self.tasksCompleteBar.progress = [[json objectForKey:@"task_percent"] floatValue]/100;
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    
    NSMutableArray *dataArray = [[NSMutableArray alloc] init];
    self.assignableDevs = snapshot.value[@"projects"][self.ProjectID][@"members"];
    self.allDevs = snapshot.value[@"users"];
    NSMutableArray *developers = [[NSMutableArray alloc] init];
    NSArray *keys = [self.assignableDevs allKeys];
    self.names = [[NSMutableArray alloc] init];
    for (NSString *key in keys) {
        NSNumber *lines = [[[[[[self.allDevs objectForKey:key] objectForKey:@"projects"] objectForKey:self.ProjectID] objectForKey:@"milestones"] objectForKey:self.MileStoneID] objectForKey:@"added_lines_of_code"];
        
        NSString *name = [[self.allDevs objectForKey:key] objectForKey:@"name"];
        if(name != NULL && lines!=0){
            [self.names addObject:name];
            [dataArray addObject:lines];
            [developers addObject:key];
        }
    }
        
        [self.burndown reloadGraph];
    
    [self.pieChart renderInLayer:self.pieChart dataArray:dataArray nameArray:self.names];
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
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    HenryTasksTableViewController *vc = [segue destinationViewController];
    vc.ProjectID = self.ProjectID;
    vc.MileStoneID = self.MileStoneID;
    vc.milestoneName = self.milestoneNameLabel.text;
    vc.userTasks = self.userTasks;
    vc.uid = self.uid;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

@end