//
//  HenryMyTasksTableViewController.m
//  Henry
//
//  Created by Grove, Carter J on 12/13/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryMyTasksTableViewController.h"
#import "HenryFirebase.h"
#import "HenryTaskDetailViewController.h"

@interface HenryMyTasksTableViewController ()
@property Firebase *fb;
@property HenryFirebase* henryFB;
@property NSMutableArray *tasks;
@property NSString *uid;
@end

@implementation HenryMyTasksTableViewController

-(void)viewWillAppear:(BOOL)animated{
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    [self updateTable];
}
-(void)viewWillDisappear:(BOOL)animated{
    [self.fb removeAllObservers];
    [self.henryFB removeAllObservers];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    //show the second view..
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (void)viewDidLoad {
    @try{
        [super viewDidLoad];
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        self.uid = [defaults objectForKey:@"id"];
        
        self.tasks = [[NSMutableArray alloc] init];
        
        self.fb = [HenryFirebase getFirebaseObject];
        self.henryFB = [HenryFirebase new];
        
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

-(void)updateTable {
    NSMutableArray *allTasks = [[NSMutableArray alloc] init];
    
    [self.henryFB getProjectsUserIsOnWithUserKey:self.uid withBlock:^(NSDictionary *projectsDictionary, BOOL success, NSError *error) {
        NSArray* myProjects = [projectsDictionary allKeys];
        for (NSString *project in myProjects) {
            [self.henryFB getMilestonesWithProjectId:project withBlock:^(NSDictionary *milestonesDictionary, BOOL success, NSError *error) {
                NSArray *milestoneKeys = [milestonesDictionary allKeys];
                for (NSString *milestone in milestoneKeys) {
                    [self.henryFB getTasksWithMilestoneKey:milestone projectId:project withBlock:^(NSDictionary *tasksDictionary, BOOL success, NSError *error) {
                        NSArray* taskKeys = [tasksDictionary allKeys];
                        for (NSString *task in taskKeys) {
                            NSDictionary* singleTask = [tasksDictionary objectForKey:task];
                            NSString* assignedTo = [singleTask objectForKey:@"assignedTo"];
                            if ([assignedTo isEqualToString:self.uid]) {
                                NSMutableDictionary *taskInfo = [singleTask mutableCopy];
                                taskInfo[@"projectID"] = project;
                                taskInfo[@"milestoneID"] = milestone;
                                taskInfo[@"taskID"] = task;
                                
                                [allTasks addObject:taskInfo];
                            }
                        }
                        self.tasks = allTasks;
                        [self.tableView reloadData];
                    }];
                }
            }];
        }
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    }];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return [self.tasks count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MyTasksIdentifier" forIndexPath:indexPath];
    
    cell.textLabel.text = [self.tasks objectAtIndex:indexPath.row][@"name"];
    cell.detailTextLabel.text = [self.tasks objectAtIndex:indexPath.row][@"due_date"];
    
    if (![[self.tasks objectAtIndex:indexPath.row][@"status"] isEqualToString:@"Closed"])
        cell.imageView.image = [UIImage imageNamed:@"ic_action_flag.png"];
    else
       cell.imageView.image = [UIImage imageNamed:@"ic_action_flag_green.png"];
    
    [self.tableView setSeparatorInset:UIEdgeInsetsZero];
    
    return cell;
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    NSIndexPath *indexPath = [self.tableView indexPathForCell:sender];
    NSMutableDictionary *taskInfo = [self.tasks objectAtIndex:indexPath.row];
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    HenryTaskDetailViewController *vc = [segue destinationViewController];
    vc.ProjectID = taskInfo[@"projectID"];
    vc.MileStoneID = taskInfo[@"milestoneID"];
    vc.taskID = taskInfo[@"taskID"];
}

@end


