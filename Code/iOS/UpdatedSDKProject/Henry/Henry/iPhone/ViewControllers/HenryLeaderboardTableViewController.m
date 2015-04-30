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
@property NSDictionary *trophies;
@property NSInteger numUsersToDisplay;

@end

@implementation HenryLeaderboardTableViewController
@synthesize leaderboardSegmentedControl;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.numUsersToDisplay = 10;
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    self.uid = [defaults objectForKey:@"id"];
    
    self.top25 = [[NSMutableArray alloc] init];
    self.top25Trophies = [[NSMutableArray alloc] init];
    self.users = [[NSDictionary alloc] init];
    self.trophies = [[NSDictionary alloc] init];
    self.fb = [HenryFirebase getFirebaseObject];
    
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateTable:snapshot];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
}


-(void)updateTable:(FDataSnapshot *)snapshot {
    self.users = [snapshot.value valueForKey:@"users"];
    self.trophies = [snapshot.value valueForKey:@"trophies"];
    NSMutableArray *ids = [NSMutableArray arrayWithArray:[self.users allKeys]];
    NSMutableArray *idT = [NSMutableArray arrayWithArray:[self.users allKeys]];
    long points = 0;
    long trophies = 0;
    for (int n = 0; n < ids.count && 0 < idT.count; n++) {
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
    for (int n = 0; n < 50 && 0 < ids.count; n++) {
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
    return [self.top25 count] < self.numUsersToDisplay ?[self.top25 count] + 1 : self.numUsersToDisplay + 1;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HenryLeaderboardCellTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"leaderboardCell" forIndexPath:indexPath];
    
    if (indexPath.row == 0) {
        cell.nameLabel.font = [UIFont boldSystemFontOfSize:17];
        cell.pointsLabel.font = [UIFont boldSystemFontOfSize:17];
        cell.nameLabel.text = [[self.users valueForKey:self.uid] valueForKey:@"name"];
        cell.rankLabel.text = @"";
        NSArray *trophies = [[[self.users valueForKey:self.uid] valueForKey:@"trophies"] allKeys];
        NSString *trophy = [trophies objectAtIndex:0];
        if(![trophy isEqualToString:@"placeholder"]){
        NSString *imageURL = [[self.trophies valueForKey:trophy] valueForKey:@"image"];
        NSData * data = [[NSData alloc] initWithContentsOfURL: [NSURL URLWithString: imageURL]];
          
                // WARNING: is the cell still using the same data by this point??
        cell.image.image = [UIImage imageWithData: data];
        }
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
            NSArray *trophies = [[[self.users valueForKey:self.top25[indexPath.row-1]] valueForKey:@"trophies"] allKeys];
            NSString *trophy = [trophies objectAtIndex:0];
            if(![trophy isEqualToString:@"placeholder"]){
                NSString *imageURL = [[self.trophies valueForKey:trophy] valueForKey:@"image"];
                NSData * data = [[NSData alloc] initWithContentsOfURL: [NSURL URLWithString: imageURL]];
                
                // WARNING: is the cell still using the same data by this point??
                cell.image.image = [UIImage imageWithData: data];
            }

            if (![[[self.users valueForKey:self.top25Trophies[indexPath.row-1]] allKeys] containsObject:@"trophies"]) {
                cell.pointsLabel.text = @"0";
            }

        } else {
            userRow = [self.top25[indexPath.row-1] isEqualToString:self.uid];
        cell.nameLabel.text = [[self.users valueForKey:self.top25[indexPath.row-1]] valueForKey:@"name"];
        cell.pointsLabel.text = [[[self.users valueForKey:self.top25[indexPath.row-1]] valueForKey:@"total_points"] stringValue];
            NSArray *trophies = [[[self.users valueForKey:self.top25[indexPath.row-1]] valueForKey:@"trophies"] allKeys];
            NSString *trophy = [trophies objectAtIndex:0];
            if(![trophy isEqualToString:@"placeholder"]){
                NSString *imageURL = [[self.trophies valueForKey:trophy] valueForKey:@"image"];
                NSData * data = [[NSData alloc] initWithContentsOfURL: [NSURL URLWithString: imageURL]];
                
                // WARNING: is the cell still using the same data by this point??
                cell.image.image = [UIImage imageWithData: data];
            }
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


- (IBAction)numUsersToDisplaySegControlClicked:(id)sender {
    //Figures out the last clicked segment.
    int clickedSegment = (int)[sender selectedSegmentIndex];
    switch(clickedSegment)
    {
            //Segment 1 is Top 10
        case 0:
            self.numUsersToDisplay = 10;
            break;
            
            //Segment 2 Top 25
        case 1:
            self.numUsersToDisplay = 25;
            break;
            //Segment 3 Top 50
        case 2:
            self.numUsersToDisplay = 50;
            break;
    }
    [self.tableView reloadData];

}

- (IBAction)pointsOrTrophiesSegControlClicked:(id)sender {
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
