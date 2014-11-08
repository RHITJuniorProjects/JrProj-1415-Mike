//
//  HenryTests.m
//  HenryTests
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <XCTest/XCTest.h>

@interface HenryTests : XCTestCase

@end

@implementation HenryTests

- (void)setUp
{
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown
{
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testExample
{
    XCTFail(@"No implementation for \"%s\"", __PRETTY_FUNCTION__);
}

//When you put in an e-mail, it should be in proper email format.
- (void)testBadEmail{
    
}

//When you update the current estimate on a task, it should only contain numbers
-(void)testBadEstimateInput
{
    
}

-(void)testEmptyTaskNameAndDescription
{
    
}
@end
