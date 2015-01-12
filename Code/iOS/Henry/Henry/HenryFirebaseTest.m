//
//  HenryFirebaseTest.m
//  Henry
//
//  Created by Mason Schneider on 1/12/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "HenryFirebase.h"

@interface HenryFirebaseTest : XCTestCase

@end

@implementation HenryFirebaseTest


- (void)testExample {
    // This is an example of a functional test case.
    Firebase *fb = [HenryFirebase getFirebaseObject];
    XCTAssert([fb isKindOfClass:[Firebase class]], @"Pass");
}

@end
