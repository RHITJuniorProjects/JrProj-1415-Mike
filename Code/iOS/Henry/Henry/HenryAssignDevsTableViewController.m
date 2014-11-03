//
//  HenryAssignDevsTableViewController.m
//  Henry
//
//  Created by Trizna, Kevin J on 10/20/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryAssignDevsTableViewController.h"
#import "HenryAssignDevTableViewCell.h"
#import "HenryDevDisplayObject.h"
#import "HenryTaskDetailViewController.h"
@interface HenryAssignDevsTableViewController ()

@end

@implementation HenryAssignDevsTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    self.developers = [[NSMutableArray alloc] init];
    HenryDevDisplayObject *devOne = [[HenryDevDisplayObject alloc] init];
    HenryDevDisplayObject *devTwo = [[HenryDevDisplayObject alloc] init];
    HenryDevDisplayObject *devThree = [[HenryDevDisplayObject alloc] init];
    devOne.devName = @"Dev one";
    devOne.isAssignedDev = false;
    devTwo.devName = @"Dev two";
    devTwo.isAssignedDev = false;
    devThree.devName = @"Dev Three";
    devThree.isAssignedDev = false;
    [self.developers addObject:devOne];
    [self.developers addObject:devTwo];
    [self.developers addObject:devThree];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
//#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
//#warning Incomplete method implementation.
    // Return the number of rows in the section.
    return self.developers.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HenryAssignDevTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"DevCell" forIndexPath:indexPath];

 // Configure the cell...
    HenryDevDisplayObject *dev = [self.developers objectAtIndex:indexPath.row];
    NSString *stringVersionOfDevName = dev.devName;
    cell.devNameLabel.text = stringVersionOfDevName;
    cell.devName = stringVersionOfDevName;
 
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    for(HenryDevDisplayObject *d in self.developers){
        d.isAssignedDev = false;
    }
    HenryDevDisplayObject *newAssignedDev = [self.developers objectAtIndex:indexPath.row];
    newAssignedDev.isAssignedDev = true;
    [tableView reloadRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationNone];
    
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
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    //HenryTaskDetailViewController *tasksVC = [segue destinationViewController];
    NSIndexPath *indexPath = [self.tableView indexPathForCell:sender];
    
    HenryTaskDetailViewController *vc = [segue destinationViewController];
    vc.primaryDev = [self.developers objectAtIndex:indexPath.row];
    vc.ProjectID = self.ProjectID;
    vc.MileStoneID = self.MilestoneID;
    vc.taskID = self.taskID;
}


@end
