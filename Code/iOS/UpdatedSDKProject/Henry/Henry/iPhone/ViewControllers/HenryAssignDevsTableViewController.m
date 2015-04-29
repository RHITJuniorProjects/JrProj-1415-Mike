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
#import "UserModel.h"
#import "TaskModel.h"
#import "HenryFirebase.h"
@interface HenryAssignDevsTableViewController ()
@property Firebase* fb;
@property HenryFirebase* henryFB;
@property UITableViewCell *previouslySelected; 
@property NSInteger selectedIndex;
@property BOOL firstTime;
@property BOOL clearChecksOnSelection;
@property BOOL hasClicked;
@end

@implementation HenryAssignDevsTableViewController

-(void)viewWillDisappear:(BOOL)animated {
    @try{
    [super viewWillDisappear:animated];
    if(self.hasClicked){
        UserModel* selectedUser = [self.developerObjects objectAtIndex:self.selectedIndex];
        self.detailView.statusButton.titleLabel.text = selectedUser.name;
        NSDictionary *newValue = @{@"assignedTo":selectedUser.key};
        
        self.fb = [self.fb childByAppendingPath:[NSString stringWithFormat:@"/projects/%@/milestones/%@/tasks/%@",self.ProjectID, self.MilestoneID, self.taskID] ];
        [self.fb updateChildValues:newValue];
    }
        [self.fb removeAllObservers];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
}
-(void)viewWillAppear:(BOOL)animated{
    self.fb = [HenryFirebase getFirebaseObject];
    
    [self updateTableFromFirebase];
}

- (void)viewDidLoad {
    @try{
    [super viewDidLoad];
    
    self.firstTime = YES;
    self.fb = [HenryFirebase getFirebaseObject];
    self.henryFB = [HenryFirebase new];
    self.hasClicked = NO;
    //[UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    // Attach a block to read the data at our posts reference
    [self updateTableFromFirebase];
    
    self.fb = [self.fb childByAppendingPath:[NSString stringWithFormat:@"projects/%@/milestones/%@/tasks/%@", self.ProjectID, self.MilestoneID, self.taskID]];
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

- (void)updateTableWithAvailableDevsFromDictionary:(NSDictionary *)usersDictionary {
    self.assignableDevs = usersDictionary;
    NSArray* keys = [self.assignableDevs allKeys];
    self.developerObjects = [NSMutableArray new];
    
    for(NSString* key in keys) {
        NSString* name = [[self.allDevs objectForKey:key] objectForKey:@"name"];
        //NSLog(key);
        if(name != NULL){
            UserModel* tempModel = [UserModel new];
            tempModel.name = name;
            tempModel.key = key;
            [self.developerObjects addObject:tempModel];
        }
    }
    [self.tableView reloadData];
}

-(void)updateTableFromFirebase {
    @try{

        [self.henryFB getAllUsersWithBlock:^(NSDictionary *usersDictionary, BOOL success, NSError *error) {
            self.allDevs = usersDictionary;
            [self.henryFB getMembersOnProjectWithProjectID:self.ProjectID withBlock:^(NSDictionary *usersDictionary, BOOL success, NSError *error) {
                [self updateTableWithAvailableDevsFromDictionary:usersDictionary];
            }];
        }];
    
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (void)didReceiveMemoryWarning {
    @try{
    [super didReceiveMemoryWarning];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    @try{
    // Return the number of sections.
    return 1;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    @try{
        return self.developerObjects.count;
        
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    @try{
    HenryAssignDevTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"DevCell" forIndexPath:indexPath];

 // Configure the cell...
        UserModel *userAtIndexPath = [self.developerObjects objectAtIndex:indexPath.row];
        NSString *dev = userAtIndexPath.name;
        
    cell.devNameLabel.text = dev;
    if ([cell.devNameLabel.text isEqualToString:self.initialSelection] && self.firstTime) {
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
        self.firstTime = NO;
        self.clearChecksOnSelection = YES;
    }
 
    return cell;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    @try{
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
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
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


@end
