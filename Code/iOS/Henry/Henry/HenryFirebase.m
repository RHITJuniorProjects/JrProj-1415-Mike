//
//  HenryFirebase.m
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryFirebase.h"

@implementation HenryFirebase

- (id)init {
    self.fb = [[Firebase alloc] initWithUrl:@"https://henry-test.firebaseio.com/projects/"];
    
    return [super init];
}

- (int)login:(NSString *)email passwordForUser:(NSString *)password {
    return 0;
}

@end
