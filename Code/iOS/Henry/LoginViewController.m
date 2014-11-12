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
    @try{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (void)viewDidLoad
{
    @try{
    [super viewDidLoad];
    self.fb = [HenryFirebase getFirebaseObject];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    @try{
    [self.view endEditing:YES];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

-(void)viewDidAppear:(BOOL)animated {
    @try{
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
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (IBAction)loginAction:(id)sender
{
    @try{
    [self.view endEditing:YES];
    self.loginIndicator.hidden = NO;
    NSString *regex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regex];
    if (![emailTest evaluateWithObject:self.emailText.text]) {
        UIAlertView *badEmailAlert = [[UIAlertView alloc] initWithTitle:@"Login Failed" message:@"Incorrect e-mail format" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [badEmailAlert show];
        self.loginIndicator.hidden = YES;
        return;
    }else{
    [self.fb authUser:self.emailText.text password:self.passwordText.text withCompletionBlock:^(NSError *error, FAuthData *authData) {
        self.loginIndicator.hidden = YES;
        NSString *errorMsg = [[NSString alloc] init];
        if (error) {
            switch(error.code) {
                case FAuthenticationErrorUserDoesNotExist:
                case FAuthenticationErrorInvalidEmail:
                case FAuthenticationErrorInvalidPassword:
                    errorMsg = @"Username or password is incorrect.";
                    break;
                default:
                    errorMsg = @"Failed to Login";
                    break;
            }
            
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Login Failed" message:errorMsg delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
            [alert show];
            return;
        }
        
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
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField {
    @try{
    [textField resignFirstResponder];
    [self loginAction:nil];
    
    return YES;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (void)didReceiveMemoryWarning
{
    @try{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
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
