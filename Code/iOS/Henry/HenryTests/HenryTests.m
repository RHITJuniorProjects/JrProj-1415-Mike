//
//  HenryTests.m
//  HenryTests
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "HenryProjectObject.h"
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

-(void)testSorting
{
    NSMutableArray *projects = [[NSMutableArray alloc] init];
    HenryProjectObject *p1 = [[HenryProjectObject alloc] init];
    HenryProjectObject *p2 = [[HenryProjectObject alloc] init];
    HenryProjectObject *p3 = [[HenryProjectObject alloc] init];
    HenryProjectObject *p4 = [[HenryProjectObject alloc] init];
    p1.name = @"Alphabet";
    p1.dueDate = @"1000-11-11";
    p2.name = @"Beta";
    p2.dueDate = @"2000-11-11";
    p3.name = @"Charlie";
    p3.dueDate = @"3000-12-11";
    p4.name = @"Delta";
    p4.dueDate = @"3000-11-11";
    [projects addObject:p1];
    [projects addObject:p2];
    [projects addObject:p3];
    [projects addObject:p4];
    
    NSSortDescriptor *sort = [NSSortDescriptor sortDescriptorWithKey:@"name" ascending:YES];
    [projects sortUsingDescriptors:[NSArray arrayWithObject:sort]];
    
    HenryProjectObject *pOne = [projects objectAtIndex:0];
    XCTAssertEqual(pOne.name,p1.name);
    
    HenryProjectObject *pTwo = [projects objectAtIndex:1];
    XCTAssertEqual(pTwo.name,p2.name);
    
    HenryProjectObject *pThree = [projects objectAtIndex:2];
    XCTAssertEqual(pThree.name,p3.name);
    
    HenryProjectObject *pFour =[projects objectAtIndex:3];
    XCTAssertEqual(pFour.name,p4.name);
    
    NSSortDescriptor *sorter = [NSSortDescriptor sortDescriptorWithKey:@"name" ascending:NO];
    [projects sortUsingDescriptors:[NSArray arrayWithObject:sorter]];
    
    HenryProjectObject *pOneT = [projects objectAtIndex:0];
    XCTAssertEqual(pOneT.name,p4.name);
    
    HenryProjectObject *pTwoT = [projects objectAtIndex:1];
    XCTAssertEqual(pTwoT.name,p3.name);
    
    HenryProjectObject *pThreeT = [projects objectAtIndex:2];
    XCTAssertEqual(pThreeT.name,p2.name);
    
    HenryProjectObject *pFourT =[projects objectAtIndex:3];
    XCTAssertEqual(pFourT.name,p1.name);
    
    NSSortDescriptor *sorting = [NSSortDescriptor sortDescriptorWithKey:@"dueDate" ascending:YES];
    [projects sortUsingDescriptors:[NSArray arrayWithObject:sorting]];
    
    HenryProjectObject *pOneF = [projects objectAtIndex:0];
    XCTAssertEqual(pOneF.name,p1.name);
    
    HenryProjectObject *pTwoF = [projects objectAtIndex:1];
    XCTAssertEqual(pTwoF.name,p2.name);
    
    HenryProjectObject *pThreeF = [projects objectAtIndex:2];
    XCTAssertEqual(pThreeF.name,p4.name);
    
    HenryProjectObject *pFourF =[projects objectAtIndex:3];
    XCTAssertEqual(pFourF.name,p3.name);
    
   
    
}
@end
