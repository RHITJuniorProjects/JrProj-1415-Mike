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
@property HenryFirebase *firebase;
@property NSMutableDictionary *trophies;
@property NSMutableArray *users;
@property NSMutableArray *trophykey;
@property NSString* userid;
@property NSDictionary *userInfo;
@property NSNumber *availablePoints;
@property NSInteger *trophyIndex;

@end
@implementation HenryStoreTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    self.userid = [defaults objectForKey:@"id"];
    self.userInfo = [[NSMutableDictionary alloc] init];
    self.trophies = [[NSMutableDictionary alloc] init];
    self.users = [[NSMutableArray alloc] init];
    self.trophykey = [[NSMutableArray alloc] init];
    self.trophyIndex = 0;
    self.firebase = [HenryFirebase new];
    
    [self updateInfo];
}

-(void)updateInfo {
    @try{
       
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        self.navigationItem.title = @"Trophy Store";
        
        [self.firebase getUserInfoWithUserId:self.userid withBlock:^(NSDictionary *userInfoDictionary, BOOL success, NSError *error) {
            self.userInfo = userInfoDictionary;
            [self.firebase getAllTrophiesWithBlock:^(NSDictionary *trophiesDictionary, BOOL success, NSError *error) {
                self.trophies = [trophiesDictionary mutableCopy];
                self.trophykey = [NSMutableArray arrayWithArray:[self.trophies allKeys]];
                [self.trophykey removeObjectsInArray:[[self.userInfo valueForKey:@"trophies"] allKeys]];
                [self.tableView reloadData];
            }];
            self.availablePoints = [self.userInfo objectForKey:@"available_points"];
            self.pointsAvailable.text = [NSString stringWithFormat:@"Available Points to Spend: %@", [self.availablePoints stringValue]];
        }];
//        self.userInfo = snapshot.value[@"users"][self.userid];
//        self.trophies = snapshot.value[@"trophies"];
//        self.trophykey = [NSMutableArray arrayWithArray:[self.trophies allKeys]];
//        [self.trophykey removeObjectsInArray:[[self.userInfo valueForKey:@"trophies"] allKeys]];
//        //DEPRECATED: self.githubLabel.text = [NSString stringWithFormat:@"Github: %@",[userInfo objectForKey:@"github"]];
//        
//        self.availablePoints = [self.userInfo objectForKey:@"available_points"];
//        self.pointsAvailable.text = [NSString stringWithFormat:@"Available Points to Spend: %@", [[self.userInfo objectForKey:@"available_points"] stringValue]];
        NSLog(@"%@", self.userInfo);
//        NSLog([NSString stringWithFormat:@"Available points: %@",[self.userInfo objectForKey:@"available_points"]]);
       // [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    }@catch(NSException *exception) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        
        exit(0);
        
    }
}

#pragma mark - Table view data source

// Unnecessary until we decide to separate them for some reason
//- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
//#warning Potentially incomplete method implementation.
//    // Return the number of sections.
//    return 1;
//}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return [self.trophykey count];
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
    self.trophyIndex = (NSInteger *)indexPath.row;
//    NSLog(@"Row %@ pressed!", [@(indexPath.row) stringValue]);
    if (self.availablePoints < [[self.trophies valueForKey:[self.trophykey objectAtIndex:indexPath.row]] valueForKey:@"cost"]){
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Can't Purchase Trophy" message:@"You don't have enough funds to buy this trophy." delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:nil, nil];
        [alert show];
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Purchase" message:@"Are you sure you want to purchase this trophy?" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Purchase", nil];
        [alert show];
    }
    
}

- (void)alertView:(UIAlertView *)alertView
clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == [alertView cancelButtonIndex]){
        NSLog(@"Cancel button clicked!");
    }else{
        NSLog(@"Other button clicked!");
        
//        self.fbUser = [self.fbUser childByAppendingPath:[NSString stringWithFormat:@"/users/%@/", self.userid]];
        
        // Decrement points by trophy point amount
        NSString *trophy = [[self.trophies valueForKey:[self.trophykey objectAtIndex:(int)self.trophyIndex]] valueForKey:@"name"];
        NSNumber *cost = [[self.trophies valueForKey:[self.trophykey objectAtIndex:(int)self.trophyIndex]] valueForKey:@"cost"];
        NSLog(@"Trophy selected: %@", trophy);
        NSLog(@"Trophy cost: %@", cost);
        
        NSNumber *newAvailablePoints = [[NSNumber alloc] initWithFloat:([self.availablePoints floatValue] - [cost floatValue])];
        
        // user can't buy trophy if not enough points
//        if(newAvailablePoints < 0) {
//            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"You don't have enough points to buy this trophy!" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:nil];
//            [alert show];
//        }
        NSMutableDictionary *newData = [[NSMutableDictionary alloc] init];
        // restoring original - FOR TESTING ONLY
        
//        NSNumber *originalPoints = [[NSNumber alloc] initWithFloat:29];
//        [newData setObject:originalPoints forKey:@"available_points"];
//        [self.fbUser updateChildValues:newData];
//        [self.tableView reloadData];
//        newData = [[NSMutableDictionary alloc] init];
        
        [newData setObject:newAvailablePoints forKey:@"available_points"];
        
        
        // Add trophy to inventory
        // TODO: add trophy!!!
       // [newData setObject:trophy forKey:@"trophies"];
        //[self.fbUser updateChildValues:newData];
//        [[self.fbUser childByAppendingPath:[NSString stringWithFormat:@"trophies/%@",[self.trophykey objectAtIndex:(int)self.trophyIndex]]] setValue:trophy];
        
        [self.tableView reloadData];
        NSLog(@"New points: %@", [self.userInfo objectForKey:@"available_points"]);
        //[self dismissViewControllerAnimated:YES completion:nil];
        
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

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
