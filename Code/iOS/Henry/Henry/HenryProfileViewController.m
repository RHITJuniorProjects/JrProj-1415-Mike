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
@property Firebase *fb;
@property FDataSnapshot* snapshot;
@property NSArray *trophies;
@property NSMutableArray *userTrophies;
@end

@implementation HenryProfileViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    self.trophies = [[NSArray alloc] init];
    self.userTrophies = [[NSMutableArray alloc] init];
    self.userid = [defaults objectForKey:@"id"];
    //NSLog([NSString stringWithFormat:@"The user id is: %@", self.userid]);
    //self.navigationItem.title = @"USER PROFILE";
    self.fb = [HenryFirebase getFirebaseObject];
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateInfo:snapshot];
        [self updateTable:snapshot];
        self.snapshot = snapshot;
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    // Do any additional setup after loading the view.
}

- (void) viewWillDisappear:(BOOL)animated {
    [self.fb removeAllObservers];
}

-(void)viewWillAppear:(BOOL)animated{
    self.fb = [HenryFirebase getFirebaseObject];
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateInfo:snapshot];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
}

-(void)updateInfo:(FDataSnapshot *)snapshot {
    @try{
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        NSDictionary *userInfo = snapshot.value[@"users"][self.userid];
        self.trophies = snapshot.value[@"trophies"];
        NSArray *userT = [snapshot.value[@"users"][self.userid][@"trophies"] allKeys] ;
        for (int i = 0; i < userT.count; i++) {
            if (![userT[i]  isEqual: @"placeholder"]) {
                [self.userTrophies addObject:userT[i]];
            }
        }
                                        //DEPRECATED: self.githubLabel.text = [NSString stringWithFormat:@"Github: %@",[userInfo objectForKey:@"github"]];

        self.navigationItem.title = [userInfo objectForKey:@"name"];
        NSString *points = [userInfo objectForKey:@"total_points"];
        //NSLog([NSString stringWithFormat:@"%@",[userInfo objectForKey:@"total_points"]]);
        self.pointsLabel.text = [NSString stringWithFormat:@"Total Points: %@",points];
        [self.trophyTable reloadData];
        //NSLog(@"%@",[self.trophies[self.userTrophies[0]]]);
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
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
    cell.textLabel.text = [[self.trophies valueForKey:self.userTrophies[indexPath.row]]valueForKey:@"name"];
    cell.detailTextLabel.text= [[self.trophies valueForKey:self.userTrophies[indexPath.row]]valueForKey:@"description"];
   // cell.imageView.image = [UI]
    
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if ([self.userTrophies count] == 0) {
        self.trophyTable.hidden = true;
    }
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
