//
//  HenryLeaderboardTableViewController.m
//  Henry
//
//  Created by Schneider, Mason on 1/28/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "HenryLeaderboardTableViewController.h"
#import "HenryLeaderboardCellTableViewCell.h"
#import "HenryUsersProfileViewController.h"
#import "HenryFirebase.h"

@interface HenryLeaderboardTableViewController ()
@property Firebase *fb;
@property NSString *uid;
@property NSDictionary *users;
@property NSMutableArray *top25;
@property NSMutableArray *top25Trophies;

@end

@implementation HenryLeaderboardTableViewController
@synthesize leaderboardSegmentedControl;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    self.uid = [defaults objectForKey:@"id"];
    
    self.top25 = [[NSMutableArray alloc] init];
    self.top25Trophies = [[NSMutableArray alloc] init];
    self.users = [[NSDictionary alloc] init];
    
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
    NSMutableArray *idT = [NSMutableArray arrayWithArray:[self.users allKeys]];
    long points = 0;
    long trophies = 0;
    for (int n = 0; n < 25 && 0 < idT.count; n++) {
        int idt = 0;
        for (int i = 0; i < idT.count; i++) {
            if (n == 0) {
                NSLog(@"%@: %@", idT[i], [[self.users valueForKey:idT[i]] valueForKey:@"trophies"]);
            }
            trophies = [[[self.users valueForKey:idT[i]] valueForKey:@"trophies"] containsObject:@"placeholder"] ? -1 : 0;
            trophies += [[[self.users valueForKey:idT[i]] valueForKey:@"trophies"] count];
            
            if (trophies > [[[self.users valueForKey:idT[idt]] valueForKey:@"trophies"] count]-([[[self.users valueForKey:idT[idt]] valueForKey:@"trophies"] containsObject:@"placeholder"] ? 1 : 0)) {
                idt = i;
            }
        }
        [self.top25Trophies addObject:idT[idt]];
        [idT removeObjectAtIndex:idt];
    }
    for (int n = 0; n < 25 && 0 < ids.count; n++) {
        int id = 0;
        for (int i = 0; i < ids.count; i++) {
            points = [[[self.users valueForKey:ids[i]] valueForKey:@"total_points"] longValue];
            
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
    
    NSLog(@"%@", self.top25);
    return [self.top25Trophies count]>[self.top25 count]?[self.top25Trophies count] + 1 : [self.top25 count] + 1;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HenryLeaderboardCellTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"leaderboardCell" forIndexPath:indexPath];
    
    if (indexPath.row == 0) {
        cell.nameLabel.font = [UIFont boldSystemFontOfSize:17];
        cell.pointsLabel.font = [UIFont boldSystemFontOfSize:17];
        cell.nameLabel.text = [[self.users valueForKey:self.uid] valueForKey:@"name"];
        cell.rankLabel.text = @"";
        if (leaderboardSegmentedControl.selectedSegmentIndex == 1) {
            cell.pointsLabel.text = [NSString stringWithFormat:@"%i", (int)[[[[self.users valueForKey:self.uid] valueForKey:@"trophies"] allKeys] count]-([[[[self.users valueForKey:self.uid] valueForKey:@"trophies"] allKeys] containsObject:@"placeholder"]? 1:0)];
        } else {
        cell.pointsLabel.text = [[[self.users valueForKey:self.uid] valueForKey:@"total_points"] stringValue];
        }
    } else {
        
        BOOL userRow = false;
        cell.rankLabel.text = [NSString stringWithFormat:@"%i.", (int)indexPath.row];
        if (leaderboardSegmentedControl.selectedSegmentIndex == 1) {
            userRow = [self.top25Trophies[indexPath.row-1] isEqualToString:self.uid];
            cell.nameLabel.text = [[self.users valueForKey:self.top25Trophies[indexPath.row-1]] valueForKey:@"name"];
            cell.pointsLabel.text = [NSString stringWithFormat:@"%i", (int)[[[[self.users valueForKey:self.top25Trophies[indexPath.row-1]] valueForKey:@"trophies"] allKeys] count]- ([[[[self.users valueForKey:self.top25Trophies[indexPath.row-1]] valueForKey:@"trophies"] allKeys] containsObject:@"placeholder"]? 1:0)];
            if (![[[self.users valueForKey:self.top25Trophies[indexPath.row-1]] allKeys] containsObject:@"trophies"]) {
                cell.pointsLabel.text = @"0";
            }

        } else {
            userRow = [self.top25[indexPath.row-1] isEqualToString:self.uid];
        cell.nameLabel.text = [[self.users valueForKey:self.top25[indexPath.row-1]] valueForKey:@"name"];
        cell.pointsLabel.text = [[[self.users valueForKey:self.top25[indexPath.row-1]] valueForKey:@"total_points"] stringValue];
    
        if (![[[self.users valueForKey:self.top25[indexPath.row-1]] allKeys] containsObject:@"total_points"]) {
            cell.pointsLabel.text = @"0";
        }
        }
        
//        if (leaderboardSegmentedControl.selectedSegmentIndex == 1) {
//            cell.pointsLabel.text = @"0";
//        }
        if (userRow) {
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

- (IBAction)leaderboardSegControlClicked:(id)sender {
    @try {
        //Figures out the last clicked segment.
        int clickedSegment = (int)[sender selectedSegmentIndex];
        switch(clickedSegment)
        {
                //Segment 1 is Points
            case 0:
                [self.tableView reloadData];
                break;
                
                //Segment 2 is Trophies
            case 1:
                 [self.tableView reloadData];
//                @try{
//                    NSSortDescriptor *sort2 = [NSSortDescriptor sortDescriptorWithKey:@"total_points" ascending:NO];
//                    [self.top25 sortUsingDescriptors:[NSArray arrayWithObject:sort2]];
//                    [self.tableView reloadData];
//                }@catch(NSException *exception){
//                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
//                    [alert show];
//                    exit(0);
//                    
//                }
                break;
        }
    }
    @catch (NSException *exception) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
    }
    
    
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    @try {
        NSLog(@"%@",self.top25[2]);
        NSIndexPath *indexPath = [self.tableView indexPathForCell:sender];
        HenryUsersProfileViewController *vc = [segue destinationViewController];
         if (leaderboardSegmentedControl.selectedSegmentIndex == 1) {
             vc.upid =[self.top25Trophies objectAtIndex:indexPath.row-1];
             vc.profile = [self.users valueForKey:[self.top25Trophies objectAtIndex:indexPath.row-1]];
         } else {
             vc.upid = [self.top25 objectAtIndex:indexPath.row-1];
             vc.profile = [self.users valueForKey:[self.top25 objectAtIndex:indexPath.row-1]];
         }
        
    }
    @catch (NSException *exception) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
    }

    
}

-(void)sortByPoints{
    @try{
        NSSortDescriptor *sort = [NSSortDescriptor sortDescriptorWithKey:@"total_points" ascending:YES];
        [self.top25 sortUsingDescriptors:[NSArray arrayWithObject:sort]];
        [self.tableView reloadData];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }

}
@end
