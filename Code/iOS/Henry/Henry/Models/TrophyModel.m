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

+ (TrophyModel*)constructModelFromDictionary:(NSDictionary*) dict;
{
    TrophyModel* tempTrophy = [TrophyModel new];
    tempTrophy.cost = [dict objectForKey:@"cost"];
    tempTrophy.trophyDescription = [dict objectForKey:@"description"];
    tempTrophy.image = [dict objectForKey:@"image"];
    tempTrophy.name = [dict objectForKey:@"name"];
    return tempTrophy;
}

- (TrophyModel*)initWithName:(NSString*) name Description:(NSString*) desc Cost: (NSNumber*) cost Image: (NSString *) trophyimage;
{
    self = super.init;
    if(self) {
        _name = name;
        _trophyDescription = desc;
        _cost = cost;
        _image = trophyimage;
    }

    return self;
}

- (BOOL) isEqual:(TrophyModel*) object {
    return [self.name isEqualToString:object.name] && [self.trophyDescription isEqualToString:object.trophyDescription] && [self.cost isEqualToNumber:object.cost] && [self.image isEqualToString:object.image];
}
@end
