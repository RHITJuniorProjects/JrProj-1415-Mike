//
//  HenryProjectsTableViewController.m
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryProjectsTableViewController.h"
#import "HenryProjectDetailViewController.h"
#import "HenryMilestonesTableViewController.h"
#import "HenryRootNavigationController.h"
#import <Firebase/Firebase.h>

@interface HenryProjectsTableViewController ()
@property NSMutableArray *cellText;
@property NSMutableArray *projectDescriptions;
@property NSArray *projectIDs;
@property Firebase *fbUsers;
@property (strong, nonatomic) NSMutableArray *tasks;
@end

@implementation HenryProjectsTableViewController

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
    HenryRootNavigationController *parentVC = (HenryRootNavigationController *)[self navigationController];
    self.uid = parentVC.uid;
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    self.cellText = [[NSMutableArray alloc] init];
    self.fbUsers = [[Firebase alloc] initWithUrl:[NSString stringWithFormat:@"https://henry-staging.firebaseio.com/users/%@/projects", self.uid]];
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    // Attach a block to read the data at our posts reference
    [self.fbUsers observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateTable];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
}

-(void)updateTable {
    NSURL *jsonURL = [NSURL URLWithString:[NSString stringWithFormat:@"https://henry-staging.firebaseio.com/users/%@/projects.json", self.uid]];
    NSData *data = [NSData dataWithContentsOfURL:jsonURL];
    NSError *error;
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
    self.projectIDs = [json allKeys];
    
    NSURL *jsonURL2 = [NSURL URLWithString:[NSString stringWithFormat:@"https://henry-staging.firebaseio.com/users/%@/projects.json", self.uid]];
    NSData *data3 = [NSData dataWithContentsOfURL:jsonURL2];
    NSDictionary *json2 = [NSJSONSerialization JSONObjectWithData:data3 options:0 error:&error];
    NSArray *projectIDs = [json2 allKeys];
    self.tasks = [[NSMutableArray alloc] init];
    for (NSString *pid in projectIDs) {
        NSArray *milestones = [[[json2 objectForKey:pid] objectForKey:@"milestones"] allKeys];
        for (NSString *milestone in milestones) {
            [self.tasks addObjectsFromArray:[[[[[json2 objectForKey:pid] objectForKey:@"milestones"] objectForKey:milestone] objectForKey:@"tasks"] allKeys]];
        }
    }
    
    NSURL *projectsURL = [NSURL URLWithString:@"https://henry-staging.firebaseio.com/projects.json"];
    NSData *data2 = [NSData dataWithContentsOfURL:projectsURL];
    NSDictionary *projectsJSON = [NSJSONSerialization JSONObjectWithData:data2 options:0 error:&error];
    NSArray *projects = [projectsJSON allKeys];
    
    //Empty out hard-coded values
    self.cellText = [[NSMutableArray alloc] init];
    self.projectDescriptions = [[NSMutableArray alloc] init];
    
    for (NSString *project in projects) {
        if ([self.projectIDs containsObject:project]) {
            NSString *name = [[projectsJSON objectForKey:project] objectForKey:@"name"];
            NSString *description = [[projectsJSON objectForKey:project] objectForKey:@"description"];
            [self.projectDescriptions addObject:description];
            [self.cellText addObject:name];
        }
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
    return [self.cellText count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ProjectCell" forIndexPath:indexPath];
    
    // Configure the cell...
    cell.textLabel.text = [self.cellText objectAtIndex:indexPath.row];
    cell.detailTextLabel.text = [self.projectDescriptions objectAtIndex:indexPath.row];
    
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
    
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    NSIndexPath *indexPath = [self.tableView indexPathForCell:sender];
    
    if ([segue.identifier isEqualToString:@"PtoM"]) {
        HenryMilestonesTableViewController *vc = [segue destinationViewController];
        vc.ProjectID = [self.projectIDs objectAtIndex:indexPath.row];
        vc.tasks = self.tasks;
        vc.uid = self.uid;
    } else {
        HenryProjectDetailViewController *vc = [segue destinationViewController];
        vc.projectID = [self.projectIDs objectAtIndex:indexPath.row];
        vc.tasks = self.tasks;
        vc.uid = self.uid;
    }
    
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
}


@end
