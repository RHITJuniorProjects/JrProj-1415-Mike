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
#import "JBLineChartView.h"

@interface HenryMilestoneDetailViewController ()
@property Firebase *fb;
@property JBLineChartView *lineChartView;
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
    
        self.lineChartView = [[JBLineChartView alloc] init];
        self.lineChartView.delegate = self;
        self.lineChartView.dataSource = self;
        [self.burndown addSubview:self.lineChartView];
        
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

-(NSUInteger)numberOfLinesInLineChartView:(JBLineChartView *)lineChartView {
    return 2;
}

-(NSUInteger)lineChartView:(JBLineChartView *)lineChartView numberOfVerticalValuesAtLineIndex:(NSUInteger)lineIndex {
    return [self.burndownData count];
}

-(CGFloat)lineChartView:(JBLineChartView *)lineChartView verticalValueForHorizontalIndex:(NSUInteger)horizontalIndex atLineIndex:(NSUInteger)lineIndex {
    NSArray *temp = [self.burndownData objectAtIndex:horizontalIndex];
    if (lineIndex == 0) {
        return [[temp objectAtIndex:1] doubleValue];
    } else {
        return [[temp objectAtIndex:2] doubleValue];
    }
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
//        self.tasksCompletedLabel.hidden = NO;
//        self.tasksCompleteBar.hidden = NO;
        self.burndown.hidden = YES;
    }else{
        self.pieChart.hidden = YES;
        self.tasksHeader.hidden = YES;
//        self.tasksCompletedLabel.hidden = YES;
//        self.tasksCompleteBar.hidden = YES;
        self.burndown.hidden = NO;
    }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
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
            [subArray addObject:[entry objectForKey:@"hours_remaining"]];
            [subArray addObject:[entry objectForKey:@"hours_complete"]];
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
        
        [self.lineChartView reloadData];
    
    
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