//
//  TrophyModel.m
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "TrophyModel.h"
#import "HenryFirebase.h"

@implementation TrophyModel
HenryFirebase* firebase;

- (TrophyModel*)initWithName:(NSString*) name Description:(NSString*) desc Cost: (NSNumber*) cost Image: (NSString *) trophyimage;
{
    self = super.init;
    if(self) {
        _name = name;
        _trophyModelDescription = desc;
        _cost = cost;
        _imageTrophy = trophyimage;
    }

    return self;
}

- (BOOL) isEqual:(TrophyModel*) object {
    return [self.name isEqualToString:object.name] && [self.trophyModelDescription isEqualToString:object.trophyModelDescription] && [self.cost isEqualToNumber:object.cost] && [self.imageTrophy isEqualToString:object.imageTrophy];
}
// listchangenotifier listchangenotifier
@end
