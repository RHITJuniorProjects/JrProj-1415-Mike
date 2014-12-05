//
//  HenryProfileViewController.h
//  Henry
//
//  Created by Trizna, Kevin J on 12/5/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryProfileViewController : UIViewController
@property NSString* userid;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *emailLabel;
@property (weak, nonatomic) IBOutlet UILabel *githubLabel;

@end
