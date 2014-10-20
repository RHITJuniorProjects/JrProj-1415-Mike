//
//  LoginViewController.m
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "LoginViewController.h"
#import "Henry/HenryRootNavigationController.h"

@interface LoginViewController ()
@end

@implementation LoginViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.fb = [[Firebase alloc] initWithUrl:@"https://henry-staging.firebaseio.com/projects/"];
}

- (IBAction)loginAction:(id)sender
{
    NSLog(@"Attempting login");
    self.errorLabel.hidden = YES;
    self.loginIndicator.hidden = NO;
    [self.fb authUser:self.emailText.text password:self.passwordText.text withCompletionBlock:^(NSError *error, FAuthData *authData) {
        self.loginIndicator.hidden = YES;
        if (error) {
            NSLog(@"Failed to login");
            self.errorLabel.hidden = NO;
            self.errorLabel.text = @"Failed to login";
            return;
        }
        NSLog(@"Logged in");
        UIStoryboard *sb = [UIStoryboard storyboardWithName:@"iPhoneStoryboard" bundle:nil];
        HenryRootNavigationController *initialView = [sb instantiateInitialViewController];
        initialView.uid = authData.uid;
        [self presentViewController:initialView animated:YES completion:nil];

    }];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
