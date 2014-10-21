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
    self.fb = [[Firebase alloc] initWithUrl:@"https://henry-staging.firebaseio.com"];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

-(void)viewDidAppear:(BOOL)animated {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    if([defaults objectForKey:@"id"] != nil) {
        if ( UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad )
        {
            UIStoryboard *sb = [UIStoryboard storyboardWithName:@"iPadStoryboard" bundle:nil];
            UIViewController *initialView = [sb instantiateInitialViewController];
            [self presentViewController:initialView animated:YES completion:nil];
        } else {
            UIStoryboard *sb = [UIStoryboard storyboardWithName:@"iPhoneStoryboard" bundle:nil];
            UIViewController *initialView = [sb instantiateInitialViewController];
            [self presentViewController:initialView animated:YES completion:nil];
        }
    } else if ([defaults objectForKey:@"email"] != nil) {
        self.emailText.text = [defaults objectForKey:@"email"];
    }
}

- (IBAction)loginAction:(id)sender
{
    NSLog(@"Attempting login");
    [self.view endEditing:YES];
    self.errorLabel.hidden = YES;
    self.loginIndicator.hidden = NO;
    [self.fb authUser:self.emailText.text password:self.passwordText.text withCompletionBlock:^(NSError *error, FAuthData *authData) {
        self.loginIndicator.hidden = YES;
        if (error) {
            switch(error.code) {
                case FAuthenticationErrorUserDoesNotExist:
                    // Handle invalid user
                    self.errorLabel.text = @"Invalid User";
                    break;
                case FAuthenticationErrorInvalidEmail:
                    // Handle invalid email
                    self.errorLabel.text = @"Invalid Email";
                    break;
                case FAuthenticationErrorInvalidPassword:
                    // Handle invalid password
                    self.errorLabel.text = @"Invalid Password";
                    break;
                default:
                    self.errorLabel.text = @"Failed to Login";
                    break;
            }
            NSLog(@"Failed to login");
            self.errorLabel.hidden = NO;
            return;
        }
        NSLog(@"Logged in");
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults setObject:authData.uid forKey:@"id"];
        [defaults setObject:authData.token forKey:@"token"];
        [defaults setObject:authData.providerData[@"email"] forKey:@"email"];
        [defaults synchronize];
         
        
        if ( UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad )
        {
            UIStoryboard *sb = [UIStoryboard storyboardWithName:@"iPadStoryboard" bundle:nil];
            UIViewController *initialView = [sb instantiateInitialViewController];
            [self presentViewController:initialView animated:YES completion:nil];
        } else {
            UIStoryboard *sb = [UIStoryboard storyboardWithName:@"iPhoneStoryboard" bundle:nil];
            UIViewController *initialView = [sb instantiateInitialViewController];
            [self presentViewController:initialView animated:YES completion:nil];
        }

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
