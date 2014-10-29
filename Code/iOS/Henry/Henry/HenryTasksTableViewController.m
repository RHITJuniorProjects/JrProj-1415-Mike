//
//  HenryTasksTableViewController.m
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryTasksTableViewController.h"
#import "HenryTaskDetailViewController.h"
#import <Firebase/Firebase.h>

@interface HenryTasksTableViewController ()

@property NSMutableArray *tasks;
@property Firebase *fb;
@property NSMutableArray *tasksDescriptions;
@property NSMutableArray *taskIDs;
@end

@implementation HenryTasksTableViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.tasks = [[NSMutableArray alloc] init];
    
    self.fb = [[Firebase alloc] initWithUrl:@"https://henry-staging.firebaseio.com/"];

    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    // Attach a block to read the data at our posts reference
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateTable];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    if ([self.milestoneName isKindOfClass:[UILabel class]]) {
        self.title = @"error";
    } else {
        self.title = self.milestoneName;
    }
}

-(void)updateTable {
    NSString *urlString = [NSString stringWithFormat:@"https:henry-staging.firebaseio.com/projects/%@/milestones/%@/tasks.json", self.ProjectID, self.MileStoneID];
    NSLog(@"%@", urlString);
    NSURL *jsonURL = [NSURL URLWithString:urlString];
    NSData *data = [NSData dataWithContentsOfURL:jsonURL];
    NSError *error;
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
    NSArray *keys = [json allKeys];
    
    self.tasks = [[NSMutableArray alloc] init];
    self.tasksDescriptions = [[NSMutableArray alloc] init];
    self.taskIDs = [[NSMutableArray alloc] init];
    
    for (NSString *key in keys) {
        // Hardcoded user for now...
//        if (![[[json objectForKey:key] objectForKey:@"assignedTo"] isEqualToString:self.uid]) {
//            continue;
//        }
//        if (![self.userTasks containsObject:key]) {
//            continue;
//        }
        NSString *name = [[json objectForKey:key] objectForKey:@"name"];
        NSString *description = [[json objectForKey:key] objectForKey:@"description"];
        [self.tasks addObject:name];
        [self.tasksDescriptions addObject:description];
        [self.taskIDs addObject:key];
    }
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    [self.tableView reloadData];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [self.tasks count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"TaskCell" forIndexPath:indexPath];
    
    // Configure the cell...
    cell.textLabel.text = [self.tasks objectAtIndex:indexPath.row];
    cell.detailTextLabel.text = [self.tasksDescriptions objectAtIndex:indexPath.row];
    
    return cell;
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
    
    NSIndexPath *indexPath = [self.tableView indexPathForCell:sender];
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    HenryTaskDetailViewController *vc = [segue destinationViewController];
    vc.ProjectID = self.ProjectID;
    vc.MileStoneID = self.MileStoneID;
    vc.taskID = [self.taskIDs objectAtIndex:indexPath.row];
 
}
 
- (IBAction)addTask:(id)sender {
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
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
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
                                   @"assignedTo": self.uid
                                   };
            [newTask setValue:task];
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Invalid Input"
                                                            message:@"You have an empty field."
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
        }
    }
}

@end
