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
@property Firebase *fb;
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
        exit(0);
        
    }
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
        exit(0);
        
    }
}

- (void)viewDidLoad
{
    @try{
    [super viewDidLoad];
    
    self.tasks = [[NSMutableArray alloc] init];
    
    self.fb = [HenryFirebase getFirebaseObject];

    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    // Attach a block to read the data at our posts reference
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateTable:snapshot];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    if ([self.milestoneName isKindOfClass:[UILabel class]]) {
        self.title = @"error";
    } else {
        self.title = self.milestoneName;
    }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

-(void)updateTable:(FDataSnapshot *)snapshot {
    @try{
    NSDictionary *tasks = snapshot.value[@"projects"][self.ProjectID][@"milestones"][self.MileStoneID][@"tasks"];
    NSArray *keys = [tasks allKeys];
    
    self.tasks = [[NSMutableArray alloc] init];
    self.tasksDescriptions = [[NSMutableArray alloc] init];
    self.taskDueDates = [[NSMutableArray alloc] init];
    self.taskIDs = [[NSMutableArray alloc] init];
        self.taskStatuses = [[NSMutableArray alloc] init];
    
    for (NSString *key in keys) {
        NSString *name = [[tasks objectForKey:key] objectForKey:@"name"];
        NSString *description = [[tasks objectForKey:key] objectForKey:@"description"];
        NSString *dueDate = [[tasks objectForKey:key] objectForKey:@"due_date"];
        [self.tasks addObject:name];
        [self.tasksDescriptions addObject:description];
        [self.taskIDs addObject:key];
        [self.taskDueDates addObject:dueDate];
        [self.taskStatuses addObject:[[tasks objectForKey:key] objectForKey:@"status"]];
    }
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    [self.tableView reloadData];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
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
        exit(0);
        
    }
}


/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

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
        exit(0);
        
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    @try{
    if (buttonIndex == 1) {
        NSString *taskName = [alertView textFieldAtIndex:0].text;
        NSString *description = [alertView textFieldAtIndex:1].text;
        if ([taskName length] > 0 && [description length] > 0) {
            NSString *urlString = [NSString stringWithFormat:@"projects/%@/milestones/%@/tasks", self.ProjectID, self.MileStoneID];
            Firebase *tasksRef = [self.fb childByAppendingPath: urlString];
            Firebase *newTask = [tasksRef childByAutoId];
            
            NSDictionary *task = @{
                                   @"name": taskName,
                                   @"description": description,
                                   @"assignedTo": self.uid,
                                   @"due_date": @"No Due Date",
                                   @"status": @"New",
                                   @"original_hour_estimate":@0,
                                   @"category":@"No Category"
                                   };
            [newTask setValue:task];
            
            // Resign keyboard first responder
            [self.parentViewController.view endEditing:YES];
            // Update table with new cell so user doesn't have to wait on firebase
            [self.tasks addObject:taskName];
            [self.taskDueDates addObject:@"No Due Date"];
            [self.tableView reloadData];
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
        exit(0);
        
    }
}

@end
