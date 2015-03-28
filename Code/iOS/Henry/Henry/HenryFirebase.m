//
//  HenryFirebase.m
//  Henry
//
//  Created by Schneider, Mason on 10/29/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryFirebase.h"

@implementation HenryFirebase

Firebase *henryFB;



+(Firebase *)getFirebaseObject {
    @try{
    henryFB =[[Firebase alloc] initWithUrl:[HenryFirebase getFirebaseURL]];
        return henryFB;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

+(NSString *)getFirebaseURL {
    return @"https://henry-test.firebaseio.com";
}



@end
