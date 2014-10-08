//
//  HenryMilestonesTableViewController.m
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryMilestonesTableViewController.h"
#import "HenryTasksTableViewController.h"
#import <Firebase/Firebase.h>

@interface HenryMilestonesTableViewController ()
@property NSMutableArray *staticData;
@property Firebase *fb;
@property NSMutableArray *milestoneIDs;
@end

@implementation HenryMilestonesTableViewController

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
    
        
    self.staticData = [[NSMutableArray alloc] init];
    if([self.ProjectID  isEqual: @"Project 1"]){
        [self.staticData addObject:@"Milestone P-1-1"];
        [self.staticData addObject:@"Milestone P-1-2"];
    }else if ([self.ProjectID  isEqual: @"Project 2"]){
        [self.staticData addObject:@"Milestone P-2-1"];
        [self.staticData addObject:@"Milestone P-2-2"];
    }
//    NSString *fbURL = [NSString stringWithFormat:@"https:henry371.firebaseio.com/projects/%@/milestones", self.ProjectID];
//    NSString *url = [[NSString alloc] initWithFormat:@"https:henry371.firebaseio.com/projects/%@/milestones", self.ProjectID];
    self.fb = [[Firebase alloc] initWithUrl:@"https://henry371.firebaseio.com/projects/"];
//    self.fb = [[Firebase alloc] initWithUrl:url];

    // Attach a block to read the data at our posts reference
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateTable];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

-(void)updateTable {
    NSString *urlString = [NSString stringWithFormat:@"https:henry371.firebaseio.com/projects/%@/milestones.json", self.ProjectID];
    NSURL *jsonURL = [NSURL URLWithString:urlString];
    NSData *data = [NSData dataWithContentsOfURL:jsonURL];
    NSError *error;
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
    NSArray *keys = [json allKeys];
    self.staticData = [[NSMutableArray alloc] init];
    self.milestoneIDs = [[NSMutableArray alloc] init];
    for (NSString *key in keys) {
        NSString *name = [[json objectForKey:key] objectForKey:@"name"];
        [self.staticData addObject:name];
        [self.milestoneIDs addObject:key];
    }
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
    return self.staticData.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MilestoneCell" forIndexPath:indexPath];
    
    // Configure the cell...
    cell.textLabel.text = [self.staticData objectAtIndex:indexPath.row];
    
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
    
    HenryTasksTableViewController *vc = [segue destinationViewController];
    vc.ProjectID = self.ProjectID;
    vc.MileStoneID = [self.milestoneIDs objectAtIndex:indexPath.row];
    vc.milestoneName = [self.staticData objectAtIndex:indexPath.row];
    vc.userTasks = self.tasks;
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}


@end
