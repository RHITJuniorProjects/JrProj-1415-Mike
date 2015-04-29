//
//  HenryStoreTableViewController.h
//  Henry
//
//  Created by Alexis Fink on 3/16/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryStoreTableViewController : UITableViewController <UIAlertViewDelegate>
@property (weak, nonatomic) IBOutlet UILabel *pointsAvailable;
@property NSMutableArray *trophyObjectArray;
//@property NSString* userid;
//@property NSDictionary *userInfo;
//@property NSNumber *availablePoints;
@end
