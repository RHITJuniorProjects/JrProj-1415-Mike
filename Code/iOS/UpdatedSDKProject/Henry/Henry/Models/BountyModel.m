//
//  BountyModel.m
//  Henry
//
//  Created by Mark Van Aken on 3/28/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "BountyModel.h"
#import "TaskModel.h"
#import "HenryFirebase.h"
#import <UIKit/UIKit.h>
#define COMPLETION_NAME @"completion"

@implementation BountyModel


#pragma mark - Model Conversion
+ (BountyModel*) constructModelFromDictionary:(NSDictionary*) dict;
{
    BountyModel* tempBounty = [BountyModel new];
    tempBounty.claimed = [dict objectForKey:@"claimed"];
    tempBounty.bountyDescription = [dict objectForKey:@"description"];
    tempBounty.dueDate = [dict objectForKey:@"due_date"];
    tempBounty.hourLimit = [dict objectForKey:@"hour_limit"];
    tempBounty.lineLimit = [dict objectForKey:@"line_limit"];
    tempBounty.name = [dict objectForKey:@"name"];
    tempBounty.points = [dict objectForKey:@"points"];
    return tempBounty;
}

- (BOOL) isEqual:(BountyModel*) object {
    return [self.claimed isEqualToString:object.claimed] && [self.bountyDescription isEqualToString:object.bountyDescription] && [self.dueDate isEqualToString:object.dueDate] && [self.hourLimit isEqualToString:object.hourLimit] && [self.lineLimit isEqualToString:object.lineLimit] && [self.name isEqualToString:object.name] && [self.points isEqualToNumber:object.points];
}



@end
