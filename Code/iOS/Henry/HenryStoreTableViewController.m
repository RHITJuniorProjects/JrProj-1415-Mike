//
//  HenryStoreTableViewController.m
//  Henry
//
//  Created by Alexis Fink on 3/16/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "HenryStoreTableViewController.h"
#import "HenryStoreCellTableViewCell.h"
#import "HenryFirebase.h"

@interface HenryStoreTableViewController ()
@property Firebase *fb;
@property NSMutableArray *trophies;
@property NSArray *trophykey;
@end
@implementation HenryStoreTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    self.trophies = [[NSMutableArray alloc] init];
    self.trophykey = [[NSArray alloc] init];
    self.fb = [HenryFirebase getFirebaseObject];
    self.fb = [self.fb childByAppendingPath:@"/trophies"];
    
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateTable:snapshot];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
}

-(void)updateTable:(FDataSnapshot *)snapshot {
    self.trophies = snapshot.value;
    NSLog(@"%@", self.trophies);
    self.trophykey = [snapshot.value allKeys];
    [self.tableView reloadData];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
#warning Incomplete method implementation.
    // Return the number of rows in the section.
    return [self.trophies count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HenryStoreCellTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"trophyCell" forIndexPath:indexPath];
    
    cell.trophyName.text = [[self.trophies valueForKey:[self.trophykey objectAtIndex:indexPath.row]] valueForKey:@"name"];
    
    cell.trophyDescription.text = [[self.trophies valueForKey:[self.trophykey objectAtIndex:indexPath.row]] valueForKey:@"description"];
    
    NSLog(@"%@",[[self.trophies valueForKey:[self.trophykey objectAtIndex:indexPath.row]] valueForKey:@"cost"]);
    
    cell.trophyPrice.text = [[[self.trophies valueForKey:[self.trophykey objectAtIndex:indexPath.row]] valueForKey:@"cost"] stringValue];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
//    NSLog(@"Row %@ pressed!", [@(indexPath.row) stringValue]);
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Purchase" message:@"Are you sure you want to purchase this trophy?" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Other", nil];
    [alert show];
}

- (void)alertView:(UIAlertView *)alertView
clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == [alertView cancelButtonIndex]){
        NSLog(@"Cancel button clicked!");
    }else{
        NSLog(@"Other button clicked!");
    }
}

/*
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:<#@"reuseIdentifier"#> forIndexPath:indexPath];
    
    // Configure the cell...
    
    return cell;
}
*/

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
