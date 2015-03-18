//
//  HenryUsersProfileViewController.h
//  Henry
//
//  Created by Alexis Fink on 3/17/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryUsersProfileViewController : UIViewController <UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *UsersTrophyTable;
@property NSString *upid;
@property NSMutableArray *profile;

@end
