//
//  HenryFirebase.h
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Firebase/Firebase.h>

@interface HenryFirebase : NSObject
@property Firebase *fb;
- (int)login:(NSString *)email passwordForUser:(NSString *)password;

@end
