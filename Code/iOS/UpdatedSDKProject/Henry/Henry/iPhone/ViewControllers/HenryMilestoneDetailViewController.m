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

@interface HenryMilestoneDetailViewController ()
@property HenryFirebase *henryFB;
@property NSMutableArray *burndownData;
@end

@implementation HenryMilestoneDetailViewController

@synthesize pieChart;

-(void)viewWillDisappear:(BOOL)animated{
    [self.henryFB removeAllObservers];
}

- (void)viewDidLoad {
    @try{
        [super viewDidLoad];
        // Do any additional setup after loading the view.
        self.henryFB = [HenryFirebase new];
        //[UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
        self.burndownData = [[NSMutableArray alloc] init];
        self.burndown.enableYAxisLabel = YES;
        self.burndown.enableXAxisLabel = YES;
        self.burndown.enableBezierCurve = NO;
        self.burndown.enableReferenceXAxisLines = YES;
        self.burndown.enableReferenceYAxisLines = YES;
        
        [self updateInfo];

    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
}

- (void)updateBurndownAndGeneralProjectInfoFromDictionary:(NSDictionary *)milestoneDictionary {
    NSDictionary *burndownData = [milestoneDictionary objectForKey:@"burndown_data"];
    NSArray *burndownKeys = [burndownData allKeys];
    burndownKeys = [[burndownKeys reverseObjectEnumerator] allObjects];
    self.burndownData = [[NSMutableArray alloc] init];
    for (NSString *burndownKey in burndownKeys) {
        NSMutableArray *subArray = [[NSMutableArray alloc] init];
        NSDictionary *entry = [burndownData objectForKey:burndownKey];
        NSDate *date = [NSDate dateWithTimeIntervalSince1970:[[entry objectForKey:@"timestamp"] doubleValue]/1000];
        [subArray addObject:date];
        [subArray addObject:[entry objectForKey:@"estimated_hours_remaining"]];
        [subArray addObject:[entry objectForKey:@"hours_completed"]];
        [self.burndownData addObject:subArray];
        [self.burndown reloadGraph];
    }
    
    self.milestoneNameLabel.text = [milestoneDictionary objectForKey:@"name"];
    self.dueDateLabel.text = [milestoneDictionary objectForKey:@"due_date"];
    self.descriptionView.text = [milestoneDictionary objectForKey:@"description"];
    self.tasksCompletedLabel.text = [NSString stringWithFormat:@"%@/%@", [milestoneDictionary objectForKey:@"tasks_completed"],[milestoneDictionary objectForKey:@"total_tasks"]];
    self.tasksCompleteBar.progress = [[milestoneDictionary objectForKey:@"task_percent"] floatValue]/100;
}

- (void)updateDeveloperDisplayInformation {
    NSMutableArray *dataArray = [[NSMutableArray alloc] init];
    NSMutableArray *developers = [[NSMutableArray alloc] init];
    NSArray *assignableDevsKeys = [self.assignableDevs allKeys];
    self.names = [[NSMutableArray alloc] init];
    for (NSString *key in assignableDevsKeys) {
        NSNumber *lines = [[[[[[self.allDevs objectForKey:key] objectForKey:@"projects"] objectForKey:self.ProjectID] objectForKey:@"milestones"] objectForKey:self.MileStoneID] objectForKey:@"added_lines_of_code"];
        
        NSString *name = [[self.allDevs objectForKey:key] objectForKey:@"name"];
        if(name != NULL && lines!=0){
            [self.names addObject:name];
            [dataArray addObject:lines];
            [developers addObject:key];
        }
    }
    
    [self.pieChart renderInLayer:self.pieChart dataArray:dataArray nameArray:self.names];
}

-(void)updateInfo {
    @try{
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        [self.henryFB getMilestoneWithMilestoneKey:self.MileStoneID projectId:self.ProjectID withBlock:^(NSDictionary *milestoneDictionary, BOOL success, NSError *error) {
            [self updateBurndownAndGeneralProjectInfoFromDictionary:milestoneDictionary];
        }];

        [self.henryFB getAllUsersWithBlock:^(NSDictionary *usersDictionary, BOOL success, NSError *error) {
            self.allDevs = usersDictionary;
            [self.henryFB getMembersOnProjectWithProjectID:self.ProjectID withBlock:^(NSDictionary *usersDictionary, BOOL success, NSError *error) {
                [self updateDeveloperDisplayInformation];
            }];
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
    int clickedSegment = (int)[sender selectedSegmentIndex];
    if(clickedSegment == 0){
        /*
        if(self.pieChart.hidden){
            self.pieChart.hidden = NO;
        }
        self.tasksHeader.hidden = NO;
        self.burndown.hidden = YES;
         */
        self.descriptionView.hidden = NO;
        self.tasksCompleteBar.hidden = NO;
        self.burndown.center = CGPointMake(0,2000);
        self.pieChart.center = CGPointMake(900,2000);
    }else if(clickedSegment == 1){
        /*
        self.pieChart.hidden = YES;
        self.tasksHeader.hidden = YES;
        self.burndown.hidden = NO;
         */
        self.burndown.hidden = NO;
        self.descriptionView.hidden = YES;
        self.tasksCompleteBar.hidden = YES;
        self.pieChart.center = CGPointMake(900, 2000);
        self.burndown.center = CGPointMake(157,315);
    }else{
        self.pieChart.hidden = NO;
        self.descriptionView.hidden = YES;
        self.tasksCompleteBar.hidden = YES;
        self.pieChart.center = CGPointMake(147,315);
        self.burndown.center = CGPointMake(0,2000);
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
        NSString *dateStr = [NSString stringWithFormat:@"%d/%d", (int)[components month], (int)[components day]];
        NSLog(@"%@", dateStr);
        return dateStr;
////        return @"1";
//    } else
//        return @"";
}

-(NSInteger)numberOfGapsBetweenLabelsOnLineGraph:(BEMSimpleLineGraphView *)graph {
    return [self.burndownData count] / 7;
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