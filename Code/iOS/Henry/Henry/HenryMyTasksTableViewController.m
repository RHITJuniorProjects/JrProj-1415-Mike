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
@property NSMutableArray *tasks;
@property NSString *uid;
@end

@implementation HenryMyTasksTableViewController

- (void)viewDidLoad {
    @try{
        [super viewDidLoad];
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        self.uid = [defaults objectForKey:@"id"];
        
        self.tasks = [[NSMutableArray alloc] init];
        
        self.fb = [HenryFirebase getFirebaseObject];
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
        
        // Attach a block to read the data at our posts reference
        [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
            [self updateTable:snapshot];
        } withCancelBlock:^(NSError *error) {
            NSLog(@"%@", error.description);
        }];
        
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

-(void)updateTable:(FDataSnapshot *)snapshot {
    NSArray *myProjects = [snapshot.value[@"users"][self.uid][@"projects"] allKeys];

    NSMutableArray *allTasks = [[NSMutableArray alloc] init];
    
    for (NSString *project in myProjects) {
        NSArray *milestones = [snapshot.value[@"projects"][project][@"milestones"] allKeys];
        
        for (NSString *milestone in milestones) {
            NSArray *tasks = [snapshot.value[@"projects"][project][@"milestones"][milestone][@"tasks"] allKeys];
            
            for (NSString *task in tasks) {
                NSString *assignedTo = snapshot.value[@"projects"][project][@"milestones"][milestone][@"tasks"][task][@"assignedTo"];
                
                if ([assignedTo isEqualToString:self.uid]) {
                    NSMutableDictionary *taskInfo = snapshot.value[@"projects"][project][@"milestones"][milestone][@"tasks"][task];
                    taskInfo[@"projectID"] = project;
                    taskInfo[@"milestoneID"] = milestone;
                    taskInfo[@"taskID"] = task;
                    
                    [allTasks addObject:taskInfo];
                }
            }
        }
    }
    
    self.tasks = allTasks;
    [self.tableView reloadData];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return 1;
}

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


/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
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
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

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


