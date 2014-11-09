//
//  HenryFirebase.m
//  Henry
//
//  Created by Schneider, Mason on 10/29/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryFirebase.h"

@implementation HenryFirebase

+(Firebase *)getFirebaseObject {
    return [[Firebase alloc] initWithUrl:@"https://ios-safe.firebaseio.com"];
}

@end
