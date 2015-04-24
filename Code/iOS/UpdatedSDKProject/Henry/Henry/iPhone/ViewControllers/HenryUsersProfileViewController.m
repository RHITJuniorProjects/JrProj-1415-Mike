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
@synthesize UsersTrophyTable;

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
            self.UsersTrophyTable.hidden = true;
        }
        //NSLog(@"%@", self.trophies);
       
        self.navigationItem.title = [self.profile valueForKey:@"name"];
        self.totalPts.text = [NSString stringWithFormat:@"Total Points: %@",[self.profile valueForKey:@"total_points"]];
        [self.UsersTrophyTable reloadData];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        
        exit(0);
        
    }
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ProfileUserTrophyCell"];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"ProfileUserTrophyCell"];
    }
    cell.textLabel.text = [[self.trophies valueForKey:[[[self.profile valueForKey:@"trophies"] allKeys] objectAtIndex:indexPath.row]]valueForKey:@"name"];
    cell.detailTextLabel.text= [[self.trophies valueForKey:[[[self.profile valueForKey:@"trophies"] allKeys] objectAtIndex:indexPath.row]] valueForKey:@"description"];
    NSData * imageData =[[NSData alloc] initWithContentsOfURL: [NSURL URLWithString: [[self.trophies valueForKey:[[[self.profile valueForKey:@"trophies"] allKeys] objectAtIndex:indexPath.row]]valueForKey:@"image"]]];
        cell.imageView.image = [UIImage imageWithData:imageData];

//    dispatch_async(dispatch_get_global_queue(0,0), ^{
//        NSData * data = [[NSData alloc] initWithContentsOfURL: [NSURL URLWithString: [[self.trophies valueForKey:[[[self.profile valueForKey:@"trophies"] allKeys] objectAtIndex:indexPath.row]]valueForKey:@"image"]]];
//        if ( data == nil )
//            return;
//        dispatch_async(dispatch_get_main_queue(), ^{
//            // WARNING: is the cell still using the same data by this point??
//            cell.image = [UIImage imageWithData: data];
//        });
//       // [data release];
//    });
    
    return cell;
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if ([[[self.profile valueForKey:@"trophies"] allKeys]  count] -([[self.profile valueForKey:@"trophies"] containsObject:@"placeholder"] ? 1 : 0) < 1) {
        self.UsersTrophyTable.hidden = true;
    }

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
