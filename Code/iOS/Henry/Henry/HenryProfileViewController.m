//
//  HenryProfileViewController.m
//  Henry
//
//  Created by Trizna, Kevin J on 12/5/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryProfileViewController.h"
#import "HenryFirebase.h"

@interface HenryProfileViewController ()
@property Firebase *fb;
@property FDataSnapshot* snapshot;
@end

@implementation HenryProfileViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    //NSLog([NSString stringWithFormat:@"The user id is: %@", self.userid]);
    //self.navigationItem.title = @"USER PROFILE";
    self.fb = [HenryFirebase getFirebaseObject];
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateInfo:snapshot];
        self.snapshot = snapshot;
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    // Do any additional setup after loading the view.
}

-(void)updateInfo:(FDataSnapshot *)snapshot {
    @try{
        [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
        NSLog(@"Got here");
        NSDictionary *userInfo = snapshot.value[@"users"][self.userid];
        self.nameLabel.text = [NSString stringWithFormat:@"Name: %@",[userInfo objectForKey:@"name"]];
        self.emailLabel.text = [NSString stringWithFormat:@"Email: %@",[userInfo objectForKey:@"email"]];
        self.githubLabel.text = [NSString stringWithFormat:@"Github: %@",[userInfo objectForKey:@"github"]];
        self.navigationItem.title = [userInfo objectForKey:@"name"];
        
        
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
