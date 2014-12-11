//
//  HenryMilestonesTableViewController.m
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryMilestonesTableViewController.h"
#import "HenryTasksTableViewController.h"
#import "HenryMilestoneDetailViewController.h"
#import "HenryFirebase.h"

@interface HenryMilestonesTableViewController ()
@property NSMutableArray *staticData;
@property Firebase *fb;
@property NSMutableArray *milestoneIDs;
@property NSMutableArray *milestoneDescriptions;
@property NSMutableArray *milestoneDueDates;
@end

@implementation HenryMilestonesTableViewController

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

- (void)viewDidLoad
{
    @try{
    [super viewDidLoad];
    
    self.fb = [HenryFirebase getFirebaseObject];

    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    // Attach a block to read the data at our posts reference
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateTable:snapshot];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

-(void)updateTable:(FDataSnapshot *)snapshot {
    @try{
    self.milestones = snapshot.value[@"projects"][self.ProjectID][@"milestones"];
    NSArray *keys = [self.milestones allKeys];
    self.staticData = [[NSMutableArray alloc] init];
    self.milestoneDescriptions = [[NSMutableArray alloc] init];
    self.milestoneIDs = [[NSMutableArray alloc] init];
    self.milestoneDueDates = [[NSMutableArray alloc] init];
    for (NSString *key in keys) {
        NSString *name = [[self.milestones objectForKey:key] objectForKey:@"name"];
        NSString *description = [[self.milestones objectForKey:key] objectForKey:@"description"];
        NSString *dueDate = [[self.milestones objectForKey:key] objectForKey:@"due_date"];
        [self.staticData addObject:name];
        [self.milestoneIDs addObject:key];
        [self.milestoneDescriptions addObject:description];
        [self.milestoneDueDates addObject:dueDate];
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
    return self.staticData.count;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    @try{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MilestoneCell" forIndexPath:indexPath];
    // Configure the cell...
    
    cell.textLabel.text = [[self.staticData objectAtIndex:indexPath.row] substringToIndex:MIN(20, [[self.staticData objectAtIndex:indexPath.row] length])];
    if ([[self.staticData objectAtIndex:indexPath.row] length] > 20) {
        cell.textLabel.text = [NSString stringWithFormat:@"%@...", cell.textLabel.text];
    }
    cell.detailTextLabel.text = [[self.milestoneDueDates objectAtIndex:indexPath.row] substringToIndex:MIN(22, [[self.milestoneDueDates objectAtIndex:indexPath.row] length])];
    
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
    
    if ([segue.identifier isEqualToString:@"MtoT"]) {
        HenryTasksTableViewController *vc = [segue destinationViewController];
        
        vc.ProjectID = self.ProjectID;
        vc.MileStoneID = [self.milestoneIDs objectAtIndex:indexPath.row];
        vc.milestoneName = [self.staticData objectAtIndex:indexPath.row];
        vc.uid = self.uid;
    } else {
        NSIndexPath *indexPath = [self.tableView indexPathForCell:(UITableViewCell *)[(UIView*)[(UIView*)sender superview] superview]];
        HenryMilestoneDetailViewController *vc = [segue destinationViewController];
        vc.ProjectID = self.ProjectID;
        vc.MileStoneID = [self.milestoneIDs objectAtIndex:indexPath.row];
        vc.milestoneName = [self.staticData objectAtIndex:indexPath.row];
        vc.uid = self.uid;
    }

    
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}


- (IBAction)addMilestones:(id)sender {
    @try{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Add Milestone"
                                                    message:nil
                                                   delegate:self
                                          cancelButtonTitle:@"Cancel"
                                          otherButtonTitles:@"Add", nil];
    [alert setAlertViewStyle:UIAlertViewStyleLoginAndPasswordInput];
    [alert textFieldAtIndex:0].placeholder = @"Milestone Name";
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
        NSString *milestoneName = [alertView textFieldAtIndex:0].text;
        NSString *description = [alertView textFieldAtIndex:1].text;
        if ([milestoneName length] > 0 && [description length] > 0) {
            NSString *urlString = [NSString stringWithFormat:@"projects/%@/milestones", self.ProjectID];
            Firebase *milestonesRef = [self.fb childByAppendingPath: urlString];
            Firebase *newMilestone = [milestonesRef childByAutoId];
            
            NSDictionary *milestone = @{
                                   @"name": milestoneName,
                                   @"description": description,
                                   @"due_date": @"No Due Date"
                                   };
            [newMilestone setValue:milestone];
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
