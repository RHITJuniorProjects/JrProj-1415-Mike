//
//  HenryLeaderboardTableViewController.h
//  Henry
//
//  Created by Schneider, Mason on 1/28/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryLeaderboardTableViewController : UITableViewController
@property (weak, nonatomic) IBOutlet UISegmentedControl *pointsOrTrophiesSegControlOutlet;
- (IBAction)pointsOrTrophiesSegControlClicked:(id)sender;

@end
