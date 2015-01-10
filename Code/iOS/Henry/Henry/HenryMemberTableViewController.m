//
//  HenryMemberTableViewController.m
//  Henry
//
//  Created by Trizna, Kevin J on 12/5/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryMemberTableViewController.h"
#import "HenryMemberTableViewCell.h"
#import "HenryFirebase.h"
#import "HenryProfileViewController.h"
@interface HenryMemberTableViewController ()
@property NSMutableArray *cellText;
@property NSMutableArray *memberNames;
@property NSMutableArray *memberEmails;
@property NSMutableArray *memberRoles;
@property NSMutableArray *memberIds;
@property Firebase *fb;
@end

//NOTE
//THIS CLASS HAS
//NOT BEEN WRAPPED
//IN TRY/CATCH STATEMENTS
//YET
@implementation HenryMemberTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationItem.title = @"Members";
    
    self.memberNames = [[NSMutableArray alloc] init];
    
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
}


-(void)updateTable:(FDataSnapshot *)snapshot {
    @try{
        NSDictionary *projectMembers = snapshot.value[@"projects"][self.ProjectID][@"members"];
        NSDictionary *allMembers = snapshot.value[@"users"];
        NSArray *keys = [projectMembers allKeys];
        //NSArray *allKeys = [allMembers allKeys];
        
        self.memberNames = [[NSMutableArray alloc] init];
        self.memberEmails = [[NSMutableArray alloc] init];
        self.memberRoles = [[NSMutableArray alloc] init];
        self.memberIds = [[NSMutableArray alloc] init];
        for (NSString *key in keys) {
            NSString *role = [projectMembers objectForKey:key];
            NSString *name = [[allMembers objectForKey:key] objectForKey:@"name"];
            NSString *email = [[allMembers objectForKey:key] objectForKey:@"email"];
            [self.memberNames addObject:name];
            [self.memberEmails addObject:email];
            [self.memberRoles addObject:role];
            [self.memberIds addObject:key];
        }
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        [self.tableView reloadData];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
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
    return self.memberEmails.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HenryMemberTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MemberCell" forIndexPath:indexPath];
    cell.emailLabel.text = [self.memberEmails objectAtIndex:indexPath.row];
    cell.roleLabel.text = [self.memberRoles objectAtIndex:indexPath.row];
    cell.nameLabel.text = [self.memberNames objectAtIndex:indexPath.row];
    // Configure the cell...
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *recipients = [self.memberEmails objectAtIndex:indexPath.row];
    NSString *email = [NSString stringWithFormat:@"mailto:%@",recipients];
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:email]];
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
    
    if([segue.identifier isEqualToString:@"goToUserProfile"]){
        HenryProfileViewController *hpvc = [segue destinationViewController];
        hpvc.userid = [self.memberIds objectAtIndex:indexPath.row];
        
    }
}


@end
