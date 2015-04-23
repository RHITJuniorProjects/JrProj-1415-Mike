//
//  HenryStoreCellTableViewCell.h
//  Henry
//
//  Created by CSSE Department on 3/17/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryStoreCellTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *trophyName;
@property (weak, nonatomic) IBOutlet UILabel *trophyDescription;
@property (weak, nonatomic) IBOutlet UILabel *trophyPrice;
@property (weak, nonatomic) IBOutlet UIImageView *trophyImage;

@end
