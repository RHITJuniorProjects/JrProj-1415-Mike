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
#import "HenryFirebase.h"
@interface HenryAssignDevsTableViewController ()
@property Firebase* fb;
@property UITableViewCell *previouslySelected; 
@property int selectedIndex;
@property BOOL firstTime;
@property BOOL clearChecksOnSelection;
@property BOOL hasClicked;
@end

@implementation HenryAssignDevsTableViewController

-(void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    if(self.hasClicked){
        self.detailView.statusButton.titleLabel.text = [self.names objectAtIndex:self.selectedIndex];
        NSDictionary *newValue = @{@"assignedTo":[self.developers objectAtIndex:self.selectedIndex]};
        [self.fb updateChildValues:newValue];
    }
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.firstTime = YES;
    self.fb = [HenryFirebase getFirebaseObject];
    self.hasClicked = NO;
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    // Attach a block to read the data at our posts reference
    
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateTable:snapshot];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    self.fb = [self.fb childByAppendingPath:[NSString stringWithFormat:@"projects/%@/milestones/%@/tasks/%@", self.ProjectID, self.MilestoneID, self.taskID]];
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
}

-(void)updateTable:(FDataSnapshot *)snapshot {
    self.assignableDevs = snapshot.value[@"projects"][self.ProjectID][@"members"];
    
    self.allDevs = snapshot.value[@"users"];
    self.developers = [[NSMutableArray alloc] init];
    NSArray *keys = [self.assignableDevs allKeys];
    self.names = [[NSMutableArray alloc] init];
    for (NSString *key in keys) {
        NSString *name = [[self.allDevs objectForKey:key] objectForKey:@"name"];
        //NSLog(key);
        if(name != NULL){
            [self.names addObject:name];
            [self.developers addObject:key];
        }
    }
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
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
    return self.names.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HenryAssignDevTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"DevCell" forIndexPath:indexPath];

 // Configure the cell...
    NSString *dev = [self.names objectAtIndex:indexPath.row];
    cell.devNameLabel.text = dev;
    if ([cell.devNameLabel.text isEqualToString:self.initialSelection] && self.firstTime) {
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
        self.firstTime = NO;
        self.clearChecksOnSelection = YES;
    }
 
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    self.hasClicked = YES;
    if (self.clearChecksOnSelection) {
        NSArray *cells = [self.tableView visibleCells];
        for (UITableViewCell *cell in cells) {
            cell.accessoryType = UITableViewCellAccessoryNone;
        }
        self.clearChecksOnSelection = NO;
    } else if (self.previouslySelected != nil) {
        self.previouslySelected.accessoryType = UITableViewCellAccessoryNone;
    }
    UITableViewCell *selectedCell = [self.tableView cellForRowAtIndexPath:indexPath];
    selectedCell.accessoryType = UITableViewCellAccessoryCheckmark;
    self.previouslySelected = selectedCell;
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
    self.selectedIndex = indexPath.row;
    
    [self.navigationController popViewControllerAnimated:YES];
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
    NSIndexPath *indexPath = [self.tableView indexPathForCell:sender];
    
    HenryTaskDetailViewController *vc = [segue destinationViewController];
    vc.primaryDev = [self.developers objectAtIndex:indexPath.row];
    vc.ProjectID = self.ProjectID;
    vc.MileStoneID = self.MilestoneID;
    vc.taskID = self.taskID;
}


@end
