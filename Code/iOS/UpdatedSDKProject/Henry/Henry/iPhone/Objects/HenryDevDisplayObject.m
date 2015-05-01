//
//  HenryDevDisplayObject.m
//  Henry
//
//  Created by Trizna, Kevin J on 10/20/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryDevDisplayObject.h"
#import <UIKit/UIKit.h>

@implementation HenryDevDisplayObject

-(void)setFalse{
    @try{
    self.isAssignedDev = false;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

@end
