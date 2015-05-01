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
@property HenryFirebase* henryFB;
@property NSDictionary *trophies;
@property NSMutableArray *userTrophies;
@end

@implementation HenryUsersProfileViewController
@synthesize UsersTrophyTable;

- (void)viewDidLoad {
    [super viewDidLoad];
    self.userTrophies = [[NSMutableArray alloc] init];
    [[self.profile valueForKey:@"trophies"] removeObjectForKey:@"placeholder"];
    self.henryFB = [HenryFirebase new];
}

- (void) viewWillDisappear:(BOOL)animated {
    [self.henryFB removeAllObservers];
}

-(void)updateInfo {
    @try{
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        [self.henryFB getAllTrophiesWithBlock:^(NSDictionary *trophiesDictionary, BOOL success, NSError *error) {
            self.trophies = trophiesDictionary;
            [self.henryFB getTrophiesBelongingToUserId:self.upid withBlock:^(NSDictionary *trophiesDictionary, BOOL success, NSError *error) {
                NSArray *userTrophyKeys = [trophiesDictionary allKeys];
                for (int i = 0; i < userTrophyKeys.count; i++) {
                    if (![userTrophyKeys[i]  isEqual: @"placeholder"]) {
                        [self.userTrophies addObject:userTrophyKeys[i]];
                    }
                }
                if ([self.userTrophies count] == 0) {
                    self.UsersTrophyTable.hidden = true;
                }
            }];
        }];
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
    return cell;
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if ([[[self.profile valueForKey:@"trophies"] allKeys]  count] -([[self.profile valueForKey:@"trophies"] containsObject:@"placeholder"] ? 1 : 0) < 1) {
        self.UsersTrophyTable.hidden = true;
    }

    return [[[self.profile valueForKey:@"trophies"] allKeys]  count];
}

@end
