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
@property Firebase *fb;
@end

@implementation HenryProjectDetailViewController

/*
 * View details for the first project when the user logs in
 */
-(void)viewWillAppear:(BOOL)animated {
    @try{
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
            [self.fb observeSingleEventOfType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
                NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
                self.uid = [defaults objectForKey:@"id"];
                NSArray *projects = [snapshot.value[@"users"][self.uid][@"projects"] allKeys];
                self.projectID = [projects objectAtIndex:0];
                [self populateMembers:snapshot];
            } withCancelBlock:^(NSError *error) {
                NSLog(@"%@", error.description);
            }];
        }
    }
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
    self.fb = [HenryFirebase getFirebaseObject];
    
    
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

- (IBAction)segControlClicked:(id)sender
{
    @try{
    //Figures out the last clicked segment.
    int clickedSegment = [sender selectedSegmentIndex];
    if(clickedSegment == 0){
        self.pieChart.hidden = YES;
    }else{
        self.pieChart.hidden = NO;
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
        int clickedSegment = [sender selectedSegmentIndex];
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

-(void)updateInfo:(FDataSnapshot *)snapshot {
    @try{
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    NSDictionary *json = snapshot.value[@"projects"][self.projectID];
    
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
        if(name != NULL && lines!=0){
            [self.names addObject:name];
            [dataArray addObject:lines];
            [developers addObject:key];
        }
    }
    
    [self.pieChart renderInLayer:self.pieChart dataArray:dataArray nameArray:self.names];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

-(void)populateMembers:(FDataSnapshot *) snapshot {
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    NSDictionary *json = snapshot.value[@"projects"][self.projectID];
    self.projectJson = json;
    self.members = [json objectForKey:@"members"];
    NSDictionary *json2 = snapshot.value[@"users"];
    self.allMembers = json2;
    [self.memberTableView reloadData];
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
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