//
//  HenryLeaderboardCellTableViewCell.h
//  Henry
//
//  Created by Schneider, Mason on 1/28/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryLeaderboardCellTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *rankLabel;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *image;
@property (weak, nonatomic) IBOutlet UILabel *pointsLabel;
@end
