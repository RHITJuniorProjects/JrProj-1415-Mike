//
//  HenryProfileViewController.m
//  Henry
//
//  Created by Trizna, Kevin J on 12/5/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryProfileViewController.h"
#import "HenryFirebase.h"

@interface HenryProfileViewController ()
//@property Firebase *fb;
//@property FDataSnapshot* snapshot;
@property HenryFirebase *firebase;
@property NSMutableDictionary *userTrophies;
@property NSArray* userTrophiesKeys;
@end

@implementation HenryProfileViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    self.userid = [defaults objectForKey:@"id"];
    self.firebase = [HenryFirebase new];
    //NSLog([NSString stringWithFormat:@"The user id is: %@", self.userid]);
    //self.navigationItem.title = @"USER PROFILE";
//    self.fb = [HenryFirebase getFirebaseObject];
    [self updateInfo];
    
    // Do any additional setup after loading the view.
}

- (void) viewWillDisappear:(BOOL)animated {
//    [self.fb removeAllObservers];
}

-(void)viewWillAppear:(BOOL)animated {
//    self.fb = [HenryFirebase getFirebaseObject];
//    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
//        [self updateInfo:snapshot];
//    } withCancelBlock:^(NSError *error) {
//        NSLog(@"%@", error.description);
//    }];
}

-(void)updateInfo {
    @try{
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        [self.firebase getUserInfoWithUserId:self.userid withBlock:^(NSDictionary *userInfoDictionary, BOOL success, NSError *error) {
            NSDictionary *userInfo = userInfoDictionary;
            NSString *points = [userInfo objectForKey:@"total_points"];
            self.pointsLabel.text = [NSString stringWithFormat:@"Total Points: %@",points];
            self.availablePointsLabel.text = [NSString stringWithFormat:@"Available Points to Spend: %@",[userInfo objectForKey:@"available_points"]];
            [self.firebase getTrophiesBelongingToUserId:self.userid withBlock:^(NSDictionary *trophiesDictionary, BOOL success, NSError *error) {
                self.userTrophies = [trophiesDictionary mutableCopy];
                [self.userTrophies removeObjectForKey:@"placeholder"];
                self.userTrophiesKeys = [self.userTrophies allKeys];
                self.navigationItem.title = [userInfo objectForKey:@"name"];

                //NSLog(@"%@",[self.trophies[self.userTrophies[0]]]);
                if ([self.userTrophiesKeys count] == 0) {
                    self.trophyTable.hidden = true;
                }
                [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
                NSLog(@"%@",self.userTrophiesKeys);
                [self.trophyTable reloadData];
            }];
        }];
//        self.userTrophies = [NSMutableArray arrayWithArray:[snapshot.value[@"users"][self.userid][@"trophies"] allKeys]];

        
        //NSLog([NSString stringWithFormat:@"%@",[userInfo objectForKey:@"total_points"]]);
    }
    @catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        
        exit(0);
        
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)logoutButtonPressed:(id)sender {
    @try{
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults removeObjectForKey:@"id"];
        [defaults removeObjectForKey:@"token"];
        [defaults synchronize];
        UIStoryboard *sb = [UIStoryboard storyboardWithName:@"iPhoneLoginStoryboard" bundle:nil];
        UIViewController *initialView = [sb instantiateInitialViewController];
        [self presentViewController:initialView animated:YES completion:nil];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ProfileTrophyCell"];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"ProfileTrophyCell"];
    }
    TrophyModel* tempTrophy = [self.userTrophies valueForKey:self.userTrophiesKeys[indexPath.row]];
    cell.textLabel.text = [[self.userTrophies valueForKey:self.userTrophiesKeys[indexPath.row]]valueForKey:@"name"];;
    cell.detailTextLabel.text= [[self.userTrophies valueForKey:self.userTrophiesKeys[indexPath.row]]valueForKey:@"description"];
    NSData * imageData =[[NSData alloc] initWithContentsOfURL: [NSURL URLWithString: [[self.userTrophies valueForKey:self.userTrophiesKeys[indexPath.row]]valueForKey:@"image"]]];
    //NSLog(@"%@", tempTrophy.imageTrophy);
    cell.imageView.image = [UIImage imageWithData:imageData];
    
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return [self.userTrophies count];
}

-(void)updateTable:(FDataSnapshot *)snapshot {

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
