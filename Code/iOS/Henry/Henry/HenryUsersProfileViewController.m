//
//  HenryUsersProfileViewController.m
//  Henry
//
//  Created by Alexis Fink on 3/17/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "HenryUsersProfileViewController.h"
#import "HenryFirebase.h"

@interface HenryUsersProfileViewController ()
@property Firebase *fb;
@property FDataSnapshot* snapshot;
@property NSArray *trophies;
@property NSMutableArray *userTrophies;
@end

@implementation HenryUsersProfileViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.userTrophies = [[NSMutableArray alloc] init];
    [[self.profile valueForKey:@"trophies"] removeObjectForKey:@"placeholder"];
    self.fb = [HenryFirebase getFirebaseObject];
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateInfo:snapshot];
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
        self.trophies = snapshot.value[@"trophies"];
        NSArray *userT = [snapshot.value[@"users"][self.upid][@"trophies"] allKeys] ;
        for (int i = 0; i < userT.count; i++) {
            if (![userT[i]  isEqual: @"placeholder"]) {
                [self.userTrophies addObject:userT[i]];
            }
        }
        if ([self.userTrophies count] == 0) {
            self.UsersTrophyTable .hidden = true;
        }
        //NSLog(@"%@", self.trophies);
              NSLog(@"%@", [[self.trophies valueForKey:[[[self.profile valueForKey:@"trophies"] allKeys] objectAtIndex:0]]valueForKey:@"description"]);
        self.navigationItem.title = [self.profile valueForKey:@"name"];
        [self.UsersTrophyTable reloadData];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        
        exit(0);
        
    }
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ProfileUserTrophyCell" forIndexPath:indexPath];
    cell.textLabel.text = [[self.trophies valueForKey:[[[self.profile valueForKey:@"trophies"] allKeys] objectAtIndex:indexPath.row]]valueForKey:@"name"];
    cell.detailTextLabel.text= [[self.trophies valueForKey:[[[self.profile valueForKey:@"trophies"] allKeys] objectAtIndex:indexPath.row]]valueForKey:@"description"];
    return cell;
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return [[[self.profile valueForKey:@"trophies"] allKeys]  count];
}





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
