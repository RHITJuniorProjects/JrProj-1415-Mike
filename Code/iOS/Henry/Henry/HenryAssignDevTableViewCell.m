//
//  HenryAssignDevTableViewCell.m
//  Henry
//
//  Created by Trizna, Kevin J on 10/20/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryAssignDevTableViewCell.h"

@implementation HenryAssignDevTableViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    @try{
    [super setSelected:selected animated:animated];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    // Configure the view for the selected state
}

@end
