//
//  HenryTests.m
//  HenryTests
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "HenryProjectObject.h"
#import "HenryFirebase.h"
#import "OCMock.h"
#import "OCMockObject.h"


@interface HenryTests : XCTestCase
@property Firebase *fb;
@property NSString *testEmail;
@property NSString *testName;
@property NSString *actualEmail;
@property NSString *actualName;
@end

@implementation HenryTests

- (void)setUp
{
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
    self.fb = [HenryFirebase getFirebaseObject];
    self.actualEmail = @"grovecj@rose-hulman.edu";
    self.actualName = @"Carter Grove";
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateInfo:snapshot];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
}


//Gets the info of one of our test accounts. We're just making sure we can pull correctly.
-(void)updateInfo:(FDataSnapshot *)snapshot {
    @try{
        NSUserDefaults *nud = [NSUserDefaults standardUserDefaults];
        NSString *myID = [nud objectForKey:@"id"];
        NSLog(@"%@",myID);
        NSDictionary *nsd = snapshot.value[@"users"][myID];
        NSLog(@"email is: %@",self.testEmail);
        NSLog(@"name is: %@",self.testName);
        self.testEmail = [nsd objectForKey:@"email"];
        self.testName = [nsd objectForKey:@"name"];
        [self testPullData:1];
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

//We know what the name should be, let's be sure we pulled it correctly
- (void) testPullData:(NSInteger) integ{
    
    XCTAssertEqual(self.testEmail,self.actualEmail);
    XCTAssertEqual(self.testName,self.actualName);
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

- (void) testGetFirebaseURL {
    NSString* urlReal = @"https://henry-test.firebaseio.com";
    NSString* urlToTest = [HenryFirebase getFirebaseURL];
    XCTAssertEqual(urlReal, urlToTest);
}

- (void) testGetAllProjects {
//    HenryFirebase* mockFirebase = 
}

- (void)tearDown
{
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}
@end
