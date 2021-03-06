//
//  HenryLeaderboardTableViewController.m
//  Henry
//
//  Created by Schneider, Mason on 1/28/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "HenryLeaderboardTableViewController.h"
#import "HenryLeaderboardCellTableViewCell.h"
#import "HenryFirebase.h"

@interface HenryLeaderboardTableViewController ()
@property Firebase *fb;
@property NSString *uid;
@property NSDictionary *users;
@property NSMutableArray *top25;
@end

@implementation HenryLeaderboardTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    self.uid = [defaults objectForKey:@"id"];
    
    self.top25 = [[NSMutableArray alloc] init];
    
    self.fb = [HenryFirebase getFirebaseObject];
    self.fb = [self.fb childByAppendingPath:@"/users"];
    
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateTable:snapshot];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
}


-(void)updateTable:(FDataSnapshot *)snapshot {
    self.users = snapshot.value;
    NSMutableArray *ids = [NSMutableArray arrayWithArray:[self.users allKeys]];
    for (int n = 0; n < 25 && 0 < ids.count; n++) {
        int id = 0;
        for (int i = 0; i < ids.count; i++) {
            long points = [[[self.users valueForKey:ids[i]] valueForKey:@"total_points"] longValue];
            
            if (points > [[[self.users valueForKey:ids[id]] valueForKey:@"total_points"] longValue]) {
                id = i;
            }
        }
        [self.top25 addObject:ids[id]];
        [ids removeObjectAtIndex:id];
    }
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
    return [self.top25 count] + 1;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HenryLeaderboardCellTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"leaderboardCell" forIndexPath:indexPath];
    
    if (indexPath.row == 0) {
        cell.nameLabel.text = [[self.users valueForKey:self.uid] valueForKey:@"name"];
        cell.rankLabel.text = @"";
        cell.pointsLabel.text = [[[self.users valueForKey:self.uid] valueForKey:@"total_points"] stringValue];
    } else {
    
        cell.nameLabel.text = [[self.users valueForKey:self.top25[indexPath.row-1]] valueForKey:@"name"];
        cell.rankLabel.text = [NSString stringWithFormat:@"%i.", (int)indexPath.row];
        cell.pointsLabel.text = [[[self.users valueForKey:self.top25[indexPath.row-1]] valueForKey:@"total_points"] stringValue];
    
        if (![[[self.users valueForKey:self.top25[indexPath.row-1]] allKeys] containsObject:@"total_points"]) {
            cell.pointsLabel.text = @"0";
        }
    
        if ([self.top25[indexPath.row-1] isEqualToString:self.uid]) {
            cell.backgroundColor = [UIColor lightGrayColor];
        } else {
            cell.backgroundColor = [UIColor whiteColor];
        }
    }
    
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

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
