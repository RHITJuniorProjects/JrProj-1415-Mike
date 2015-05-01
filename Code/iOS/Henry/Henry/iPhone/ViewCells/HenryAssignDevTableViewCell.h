//
//  HenryAssignDevTableViewCell.h
//  Henry
//
//  Created by Trizna, Kevin J on 10/20/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryAssignDevTableViewCell : UITableViewCell

@property (strong, nonatomic) IBOutlet UILabel *devNameLabel;
@property NSString *devName;
@property BOOL isAssignedDev;

@end
