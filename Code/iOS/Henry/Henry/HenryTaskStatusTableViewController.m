//
//  HenryTaskStatusTableViewController.m
//  Henry
//
//  Created by Carter Grove on 10/17/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryTaskStatusTableViewController.h"
#import "HenryFirebase.h"

@interface HenryTaskStatusTableViewController ()
@property NSArray *cellTitles;
@property UITableViewCell *previouslySelected;
@property int selectedIndex;
@property BOOL firstTime;
@property BOOL clearChecksOnSelection;
@property Firebase *fb;
@end

@implementation HenryTaskStatusTableViewController

-(void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    self.detailView.statusButton.titleLabel.text = [self.cellTitles objectAtIndex:_selectedIndex];
    UITableViewCell *selectedCell = [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:self.selectedIndex inSection:0]];
    NSDictionary *newValue = @{@"status":selectedCell.textLabel.text};
    [self.fb updateChildValues:newValue];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    self.cellTitles = [[NSArray alloc] initWithObjects:@"New", @"Implementation", @"Testing", @"Verify", @"Regression", @"Closed", nil];
    int initialIndex = [self.cellTitles indexOfObject:self.initialSelection];
    self.selectedIndex = initialIndex;
    self.firstTime = YES;
    self.clearChecksOnSelection = NO;
    
    NSLog([NSString stringWithFormat:@"https://henry-staging.firebaseio.com/projects/%@/milestones/%@/tasks/%@", self.projectID, self.milestoneID, self.taskID]);
    self.fb = [HenryFirebase getFirebaseObject]; //[[Firebase alloc] initWithUrl:[NSString stringWithFormat:@"https://henry-staging.firebaseio.com/projects/%@/milestones/%@/tasks/%@", self.projectID, self.milestoneID, self.taskID]];
    self.fb = [self.fb childByAppendingPath:[NSString stringWithFormat:@"projects/%@/milestones/%@/tasks/%@", self.projectID, self.milestoneID, self.taskID]];
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
    return [self.cellTitles count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"StatusCellIdentifier" forIndexPath:indexPath];
    
    // Configure the cell...
    if (self.firstTime && indexPath.row == self.selectedIndex) {
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
        self.firstTime = NO;
        self.clearChecksOnSelection = YES;
    }
    cell.textLabel.text = [self.cellTitles objectAtIndex:indexPath.row];
    self.previouslySelected = cell;
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
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

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
