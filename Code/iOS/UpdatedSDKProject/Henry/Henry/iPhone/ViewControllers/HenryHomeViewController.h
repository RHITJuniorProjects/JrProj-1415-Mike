//
//  HenryHomeViewController.h
//  Henry
//
//  Created by Schneider, Mason on 10/20/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HenryHomeViewController : UIViewController<UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UIBarButtonItem *navButton;

@property (strong, nonatomic) IBOutlet UITableViewCell *projectsCell;
@property (strong, nonatomic) IBOutlet UITableViewCell *tasksCell;
@property (strong, nonatomic) IBOutlet UITableViewCell *profileCell;

@end
