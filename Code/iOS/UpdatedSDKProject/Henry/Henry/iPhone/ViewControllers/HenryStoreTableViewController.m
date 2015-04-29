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
#import "TrophyModel.h"

@interface HenryStoreTableViewController ()
@property HenryFirebase *firebase;
@property NSDictionary *trophies;
@property NSMutableArray *users;
@property NSMutableArray *trophykey;
@property NSString* userid;
@property NSMutableDictionary *userInfo;
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

- (void)addAvailableTrophiesToTrophyObjectArrayFromDictionary:(NSDictionary *)trophiesDictionary {
    self.trophies = trophiesDictionary;
    self.trophykey = [NSMutableArray arrayWithArray:[self.trophies allKeys]];
    [self.trophykey removeObjectsInArray:[[self.userInfo valueForKey:@"trophies"] allKeys]];
    self.trophyObjectArray = [NSMutableArray new];
    for (int i = 0; i < [self.trophykey count]; i++) {
        NSDictionary* tempFBTrophy = [self.trophies valueForKey:[self.trophykey objectAtIndex:i]];
        TrophyModel* tempTrophyModel = [[TrophyModel alloc] initWithName:[tempFBTrophy valueForKey:@"name"] Description:[tempFBTrophy valueForKey:@"description"] Cost:[tempFBTrophy valueForKey:@"cost"] Image:[tempFBTrophy valueForKey:@"image"]];
        [self.trophyObjectArray addObject:tempTrophyModel];
    }
}

-(void)updateInfo {
    @try{
       
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        self.navigationItem.title = @"Trophy Store";
        
        [self.firebase getUserInfoWithUserId:self.userid withBlock:^(NSDictionary *userInfoDictionary, BOOL success, NSError *error) {
            self.userInfo = [userInfoDictionary mutableCopy];
            [self.firebase getAllTrophiesWithBlock:^(NSDictionary *trophiesDictionary, BOOL success, NSError *error) {
                [self addAvailableTrophiesToTrophyObjectArrayFromDictionary:trophiesDictionary];
                [self.tableView reloadData];
            }];
            self.availablePoints = [self.userInfo objectForKey:@"available_points"];
            self.pointsAvailable.text = [NSString stringWithFormat:@"Available Points to Spend: %@", [self.availablePoints stringValue]];
            NSLog(@"%@", self.userInfo);
            
        }];
        
//        NSLog([NSString stringWithFormat:@"Available points: %@",[self.userInfo objectForKey:@"available_points"]]);
    }@catch(NSException *exception) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        
        exit(0);
        
    }
}

#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.trophykey count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HenryStoreCellTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"trophyCell" forIndexPath:indexPath];
    NSInteger rowIndex = indexPath.row;
    TrophyModel* tempModel = [self.trophyObjectArray objectAtIndex:rowIndex];
    //Changed
    cell.trophyName.text = tempModel.name;
    cell.trophyDescription.text = tempModel.trophyModelDescription;
    NSData * imageData =[[NSData alloc] initWithContentsOfURL: [NSURL URLWithString: tempModel.imageTrophy]];
    NSLog(@"%@", tempModel.imageTrophy);
    cell.trophyImage.image = [UIImage imageWithData:imageData];
    //[imageData release];
    
    NSLog(@"%@",tempModel.cost.stringValue);
    
    cell.trophyPrice.text = tempModel.cost.stringValue;
    
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
        TrophyModel* trophyToPurchase = [[TrophyModel alloc] initWithName:[[self.trophies valueForKey:[self.trophykey objectAtIndex:(int)self.trophyIndex]] valueForKey:@"name"] Description:nil Cost:[[self.trophies valueForKey:[self.trophykey objectAtIndex:(int)self.trophyIndex]] valueForKey:@"cost"] Image:[[self.trophies valueForKey:[self.trophykey objectAtIndex:(int)self.trophyIndex]] valueForKey:@"image"]];
        trophyToPurchase.key = [self.trophykey objectAtIndex:(int)self.trophyIndex];
        
        NSLog(@"Trophy selected: %@", trophyToPurchase.name);
        NSLog(@"Trophy cost: %@", trophyToPurchase.cost);
        
        [self.firebase purchaseTrophyWithTrophyModel:trophyToPurchase withUserId:self.userid withOldAvailablePoints: self.availablePoints];
        [self.tableView reloadData];
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
