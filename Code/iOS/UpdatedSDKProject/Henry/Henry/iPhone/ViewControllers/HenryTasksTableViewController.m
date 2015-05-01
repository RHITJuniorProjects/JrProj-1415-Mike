//
//  HenryTasksTableViewController.m
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryTasksTableViewController.h"
#import "HenryTaskDetailViewController.h"
#import "HenryFirebase.h"

@interface HenryTasksTableViewController ()

@property NSMutableArray *tasks;
@property HenryFirebase* henryFB;
@property NSMutableArray *tasksDescriptions;
@property NSMutableArray *taskIDs;
@property NSMutableArray *taskDueDates;
@property NSIndexPath *previousIndex;
@property NSMutableArray *taskStatuses;
@end

@implementation HenryTasksTableViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    @try{
        self = [super initWithStyle:style];
        if (self) {
            // Custom initialization
        }
        return self;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        NSLog(@"%@", [exception description]);
        exit(0);
        
    }
}


-(void)viewWillAppear:(BOOL)animated{
    self.henryFB = [HenryFirebase new];
    [self updateTable];
}
-(void)viewWillDisappear:(BOOL)animated{
    [self.henryFB removeAllObservers];
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    @try{
        if ( UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad )
        {
            if (self.previousIndex != nil) {
                [self.tableView deselectRowAtIndexPath:self.previousIndex animated:YES];
            }
            
            [self.tableView selectRowAtIndexPath:indexPath animated:YES scrollPosition:YES];
            self.previousIndex = indexPath;
        }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        NSLog(@"%@", [exception description]);
        exit(0);
        
    }
}

- (void)viewDidLoad
{
    @try{
        [super viewDidLoad];
        
        self.tasks = [[NSMutableArray alloc] init];        
        //[UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
        
        // Attach a block to read the data at our posts reference
//        [self updateTable:snapshot];
        
        if ([self.milestoneName isKindOfClass:[UILabel class]]) {
            self.title = @"error";
        } else {
            self.title = self.milestoneName;
        }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        NSLog(@"%@", [exception description]);
        exit(0);
        
    }
}

-(void)updateTable {
    @try{
        [self.henryFB getTasksWithMilestoneKey:self.MileStoneID projectId:self.ProjectID withBlock:^(NSDictionary *tasksDictionary, BOOL success, NSError *error) {
            NSArray *keys = [tasksDictionary allKeys];
            
            self.tasks = [[NSMutableArray alloc] init];
            self.tasksDescriptions = [[NSMutableArray alloc] init];
            self.taskDueDates = [[NSMutableArray alloc] init];
            self.taskIDs = [[NSMutableArray alloc] init];
            self.taskStatuses = [[NSMutableArray alloc] init];
            
            for (NSString *key in keys) {
                NSString *name = [[tasksDictionary objectForKey:key] objectForKey:@"name"];
                NSString *description = [[tasksDictionary objectForKey:key] objectForKey:@"description"];
                NSString *dueDate = [[tasksDictionary objectForKey:key] objectForKey:@"due_date"];
                [self.tasks addObject:name];
                [self.tasksDescriptions addObject:description];
                [self.taskIDs addObject:key];
                [self.taskDueDates addObject:dueDate];
                [self.taskStatuses addObject:[[tasksDictionary objectForKey:key] objectForKey:@"status"]];
            }
            
            [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
            [self.tableView reloadData];
        }];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        NSLog(@"%@", [exception description]);
        exit(0);
        
    }
}


- (void)didReceiveMemoryWarning
{
    @try{
        [super didReceiveMemoryWarning];
        // Dispose of any resources that can be recreated.
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        NSLog(@"%@", [exception description]);
        exit(0);
        
    }
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    @try{
        // Return the number of sections.
        return 1;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        NSLog(@"%@", [exception description]);
        exit(0);
        
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    @try{
        // Return the number of rows in the section.
        return [self.tasks count];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        NSLog(@"%@", [exception description]);
        exit(0);
        
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    @try{
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"TaskCell" forIndexPath:indexPath];
        
        // Configure the cell...
        cell.textLabel.text = [self.tasks objectAtIndex:indexPath.row];
        cell.detailTextLabel.text = [self.taskDueDates objectAtIndex:indexPath.row];
        
        if (![[self.taskStatuses objectAtIndex:indexPath.row ] isEqualToString:@"Closed"])
            cell.imageView.image = [UIImage imageNamed:@"ic_action_flag.png"];
        else
            cell.imageView.image = [UIImage imageNamed:@"ic_action_flag_green.png"];
        
        [self.tableView setSeparatorInset:UIEdgeInsetsZero];
        
        return cell;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        NSLog(@"%@", [exception description]);
        exit(0);
        
    }
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    @try{
        NSIndexPath *indexPath = [self.tableView indexPathForCell:sender];
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
        HenryTaskDetailViewController *vc = [segue destinationViewController];
        vc.ProjectID = self.ProjectID;
        vc.MileStoneID = self.MileStoneID;
        vc.taskID = [self.taskIDs objectAtIndex:indexPath.row];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        NSLog(@"%@", [exception description]);
        exit(0);
        
    }
    
}

- (IBAction)addTask:(id)sender {
    @try{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Add Task"
                                                        message:nil
                                                       delegate:self
                                              cancelButtonTitle:@"Cancel"
                                              otherButtonTitles:@"Add", nil];
        [alert setAlertViewStyle:UIAlertViewStyleLoginAndPasswordInput];
        [alert textFieldAtIndex:0].placeholder = @"Task Name";
        [alert textFieldAtIndex:1].secureTextEntry = false;
        [alert textFieldAtIndex:1].placeholder = @"Description";
        
        [alert show];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        NSLog(@"%@", [exception description]);
        exit(0);
        
    }
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    @try{
        if (buttonIndex == 1) {
            NSString *taskName = [alertView textFieldAtIndex:0].text;
            NSString *description = [alertView textFieldAtIndex:1].text;
            if ([taskName length] > 0 && [description length] > 0) {
                [self.henryFB createTaskWithName:taskName description:description assignedToUserKey:self.uid dueDate:@"No Due Date" status:@"New" hourEstimate:@0 category:@"No Category" projectKey:self.ProjectID milestoneKey:self.MileStoneID];
                
                // Resign keyboard first responder
                [self.parentViewController.view endEditing:YES];
            } else {
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Invalid Input"
                                                                message:@"You have an empty field."
                                                               delegate:nil
                                                      cancelButtonTitle:@"OK"
                                                      otherButtonTitles:nil];
                [alert show];
            }
        }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        NSLog(@"%@", [exception description]);
        exit(0);
        
    }
}

@end
