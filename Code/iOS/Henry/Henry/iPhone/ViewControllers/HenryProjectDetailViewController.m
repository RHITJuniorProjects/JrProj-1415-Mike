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
#import "HenryMemberTableViewCell.h"

@interface HenryProjectDetailViewController ()
@property HenryFirebase* henryFB;
@property NSMutableArray *lineGraphData;
@property NSMutableArray *hoursGraphData;
@property int activeChart;
@end

@implementation HenryProjectDetailViewController

/*
 * View details for the first project when the user logs in
 */

-(void)viewWillDisappear:(BOOL)animated{
    [self.henryFB removeAllObservers];
}
-(void)viewWillAppear:(BOOL)animated {
    @try{
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
            if (self.projectID == nil) {
                NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
                self.uid = [defaults objectForKey:@"id"];
            
                [self.henryFB getProjectsUserIsOnWithUserKey:self.uid withBlock:^(NSDictionary *projectsDictionary, BOOL success, NSError *error) {
                NSArray *projects = [projectsDictionary allKeys];
                self.projectID = [projects objectAtIndex:0];
                [self updateInfo];
                [self populateMembers];
                }];
            }
        }
        [self updateInfo];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (void)viewDidLoad {
    @try{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
        [self.lineGraph setName:@"lines"];
        [self.hoursLineChart setName:@"hours"];
        self.henryFB = [HenryFirebase new];
        self.hoursGraphData = [[NSMutableArray alloc] init];
        self.hoursLineChart.alwaysDisplayPopUpLabels = NO;
        self.hoursLineChart.enablePopUpReport = YES;
        self.hoursLineChart.alwaysDisplayDots = YES;
        self.hoursLineChart.dataSource = self;
        self.hoursLineChart.enableYAxisLabel = YES;
        self.hoursLineChart.enableXAxisLabel = YES;
        self.hoursLineChart.enableBezierCurve = YES;
        self.hoursLineChart.enableReferenceXAxisLines = YES;
        self.hoursLineChart.enableReferenceYAxisLines = YES;
        [self.hoursLineChart changeFontSize:5];
        self.lineGraphData = [[NSMutableArray alloc] init];
        self.lineGraph.alwaysDisplayPopUpLabels = NO;
        self.lineGraph.enablePopUpReport = YES;
        self.lineGraph.alwaysDisplayDots = YES;
        self.lineGraph.dataSource = self;
        self.lineGraph.enableYAxisLabel = YES;
        self.lineGraph.enableXAxisLabel = YES;
        self.lineGraph.enableBezierCurve = YES;
        self.lineGraph.enableReferenceXAxisLines = YES;
        self.lineGraph.enableReferenceYAxisLines = YES;
        [self.lineGraph changeFontSize:5];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (void)updateLabelsAndProgressBars:(NSDictionary *)json {
    self.projectNameLabel.text = [json objectForKey:@"name"];
    self.projectDescriptionView.text = [json objectForKey:@"description"];
    self.dueDateLabel.text = [json objectForKey:@"due_date"];
    double totalHours = [[json objectForKey:@"total_hours"] doubleValue];
    double estimatedHours = [[json objectForKey:@"total_estimated_hours"] doubleValue];
    self.hoursLoggedLabel.text = [NSString stringWithFormat:@"%0.2f/%0.2f", totalHours, estimatedHours];
    self.hoursLoggedProgressBar.progress = totalHours/estimatedHours;
    self.tasksCompletedLabel.text = [NSString stringWithFormat:@"%@/%@",[json objectForKey:@"tasks_completed"],[json objectForKey:@"total_tasks"]];
    self.tasksCompletedProgressBar.progress = [[json objectForKey:@"task_percent"] floatValue] / 100;
    self.milestonesCompletedLabel.text = [NSString stringWithFormat:@"%@/%@",[json objectForKey:@"milestones_completed"],[json objectForKey:@"total_milestones"]];
    self.milestonesCompletedProgressBar.progress = [[json objectForKey:@"milestone_percent"] floatValue] / 100;
}

- (void)updateBurndown:(NSDictionary *)desiredProject {
    NSDictionary *burndownData = [desiredProject objectForKey:@"milestones"];
    NSArray *burndownKeys = [burndownData allKeys];
    burndownKeys = [[burndownKeys reverseObjectEnumerator] allObjects];
    self.lineGraphData = [[NSMutableArray alloc] init];
    NSInteger i = 0;
    for (NSString *burndownKey in burndownKeys) {
        NSMutableArray *subArray = [[NSMutableArray alloc] init];
        NSMutableArray *hoursSubArray = [[NSMutableArray alloc] init];
        NSDictionary *entry = [burndownData objectForKey:burndownKey];
        //NSLog(@"Adding %@",[entry objectForKey:@"total_lines_of_code"]);
        //NSLog(@"adding %@",[entry objectForKey:@"name"]);
        //NSLog(@"Aadding %ld",(long)i);
        [hoursSubArray addObject:[entry objectForKey:@"total_hours"]];
        [hoursSubArray addObject:[entry objectForKey:@"name"]];
        [hoursSubArray addObject:[NSNumber numberWithInteger:i]];
        [subArray addObject:[entry objectForKey:@"total_lines_of_code"]];
        [subArray addObject:[entry objectForKey:@"name"]];
        [subArray addObject:[NSNumber numberWithInteger:i]];
        i++;
        [self.lineGraphData addObject:subArray];
        [self.hoursGraphData addObject:hoursSubArray];
        [self.lineGraph reloadGraph];
        [self.hoursLineChart reloadGraph];
    }
}

-(void)updateInfo {
    @try{
        [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
        [self.henryFB getAllProjectsWithBlock:^(NSDictionary *projectsDictionary, BOOL success, NSError *error) {
            NSDictionary* desiredProject = projectsDictionary[self.projectID];
            [self updateLabelsAndProgressBars:desiredProject];
            [self updateBurndown:desiredProject];
            [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        }];
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
        [self.henryFB getAllUsersWithBlock:^(NSDictionary *usersDictionary, BOOL success, NSError *error) {
            self.allDevs = usersDictionary;
            [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
            [self.henryFB getMembersOnProjectWithProjectID:self.projectID withBlock:^(NSDictionary *usersDictionary, BOOL success, NSError *error) {
                self.assignableDevs = usersDictionary;
                
                NSArray *assignableDevKeys = [self.assignableDevs allKeys];
                NSMutableArray *dataArray = [[NSMutableArray alloc] init];
                NSMutableArray *developers = [[NSMutableArray alloc] init];
                self.names = [[NSMutableArray alloc] init];
                
                for (NSString *key in assignableDevKeys) {
                    NSString *name = [[self.allDevs objectForKey:key] objectForKey:@"name"];
                    NSNumber *lines = [[[[self.allDevs objectForKey:key] objectForKey:@"projects"] objectForKey:self.projectID] objectForKey:@"added_lines_of_code"];
                    if(name != NULL && lines!=0){
                        [self.names addObject:name];
                        [dataArray addObject:lines];
                        [developers addObject:key];
                    }
                }
                [self.pieChart renderInLayer:self.pieChart dataArray:dataArray nameArray:self.names];
                [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
            }];
            [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        }];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

-(NSInteger)numberOfPointsInLineGraph:(BEMSimpleLineGraphView *)graph {
    if([graph.name isEqualToString:@"lines"]){
        return [self.lineGraphData count];
    }
    return [self.hoursGraphData count];
}

-(NSInteger)numberOfYAxisLabelsOnLineGraph:(BEMSimpleLineGraphView *)graph {
    return 3;
}

-(CGFloat)lineGraph:(BEMSimpleLineGraphView *)graph valueForPointAtIndex:(NSInteger)index {
    if([graph.name isEqualToString:@"lines"]){
        NSArray *subArray = [self.lineGraphData objectAtIndex:index];
        return [[subArray objectAtIndex:0] floatValue];
    }
    NSArray *subArray = [self.hoursGraphData objectAtIndex:index];
    return [[subArray objectAtIndex:0] floatValue];
}

-(NSString *)lineGraph:(BEMSimpleLineGraphView *)graph labelOnXAxisForIndex:(NSInteger)index {
    NSArray *subArray = [self.lineGraphData objectAtIndex:index];
    return [subArray objectAtIndex:1];
}

-(NSInteger)numberOfGapsBetweenLabelsOnLineGraph:(BEMSimpleLineGraphView *)graph {
    return 1;
}

- (IBAction)segControlClicked:(id)sender
{
    @try{
    //Figures out the last clicked segment.
    int clickedSegment = (int)[sender selectedSegmentIndex];
    
    //Get the info
    if(clickedSegment == 0){
        self.pieChart.center = CGPointMake(0,1000);
        self.lineGraph.center = CGPointMake(0,2000);
        self.hoursLineChart.center = CGPointMake(900,900);
    }else if(clickedSegment == 1){
        self.pieChart.hidden = NO;
        self.lineGraph.center = CGPointMake(0,2000);
        self.pieChart.center = CGPointMake(147,300);
        self.hoursLineChart.center = CGPointMake(900,900);
    }else if(clickedSegment == 2){
        self.lineGraph.hidden = NO;
        self.pieChart.center = CGPointMake(0, 1000);
        self.lineGraph.center = CGPointMake(157,310);
        self.hoursLineChart.center = CGPointMake(900,900);
    
    }else{
        self.lineGraph.hidden = NO;
        self.pieChart.center = CGPointMake(0, 1000);
        self.hoursLineChart.center = CGPointMake(157,310);
        self.lineGraph.center = CGPointMake(0,2000);
    }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
}

- (IBAction)ipadSegControlClicked:(id)sender {
    @try{
        //Figures out the last clicked segment.
        int clickedSegment = (int)[sender selectedSegmentIndex];
        if(clickedSegment == 0){
            self.pieChart.hidden = YES;
            self.hoursLoggedLabel.hidden = NO;
            self.tasksCompletedLabel.hidden = NO;
            self.milestonesCompletedLabel.hidden = NO;
            self.hoursLoggedProgressBar.hidden = NO;
            self.tasksCompletedProgressBar.hidden = NO;
            self.milestonesCompletedProgressBar.hidden = NO;
            self.hoursHeader.hidden = NO;
            self.tasksHeader.hidden = NO;
            self.milestonesHeader.hidden = NO;
            self.memberTableView.hidden = YES;
        }else{
            self.pieChart.hidden = NO;
            self.hoursLoggedLabel.hidden = YES;
            self.tasksCompletedLabel.hidden = YES;
            self.milestonesCompletedLabel.hidden = YES;
            self.hoursLoggedProgressBar.hidden = YES;
            self.tasksCompletedProgressBar.hidden = YES;
            self.milestonesCompletedProgressBar.hidden = YES;
            self.hoursHeader.hidden = YES;
            self.tasksHeader.hidden = YES;
            self.milestonesHeader.hidden = YES;
            self.memberTableView.hidden = NO;
        }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.members.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {    
    HenryMemberTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MemberCell" forIndexPath:indexPath];
    NSString *memberId = [self.members allKeys][indexPath.row];
    
    NSString *name = [[self.allMembers objectForKey:memberId] objectForKey:@"name"];
    NSString *email = [[self.allMembers objectForKey:memberId] objectForKey:@"email"];
    
    cell.nameLabel.text = name;
    cell.emailLabel.text = email;
    cell.roleLabel.text = [self.members objectForKey:memberId];
    
    if ([[self.members objectForKey:self.uid] isEqual: @"Lead"]) {
        double totalHours = [[self.projectJson objectForKey:@"total_estimated_hours"] doubleValue];
        double memberHours = [[[[[self.allMembers objectForKey:memberId] objectForKey:@"projects"] objectForKey:self.projectID] objectForKey:@"total_hours"] doubleValue];
        cell.progressLabel.text = [NSString stringWithFormat:@"Hours: %.0f/%.0f", memberHours, totalHours];
        cell.progressBar.progress = memberHours / totalHours;
        cell.progressBar.hidden = NO;
        cell.progressLabel.hidden = NO;
    }
    return cell;
}

-(void)populateMembers {
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    [self.henryFB getAllProjectsWithBlock:^(NSDictionary *projectsDictionary, BOOL success, NSError *error) {
        NSDictionary* json = projectsDictionary[self.projectID];
        self.projectJson = json;
        self.members = [json objectForKey:@"members"];
        [self.henryFB getAllUsersWithBlock:^(NSDictionary *usersDictionary, BOOL success, NSError *error) {
            NSDictionary *json2 = usersDictionary;
            self.allMembers = json2;
            [self.memberTableView reloadData];
            [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        }];
    }];
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
    HenryMilestonesTableViewController *vc = [segue destinationViewController];
    vc.ProjectID = self.projectID;
    vc.tasks = self.tasks;
    vc.uid = self.uid;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

@end