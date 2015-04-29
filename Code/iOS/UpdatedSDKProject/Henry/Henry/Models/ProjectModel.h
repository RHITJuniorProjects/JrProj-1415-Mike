//
//  ProjectModel.h
//  Henry
//
//  Created by CSSE Department on 3/31/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ProjectModel : NSObject
@property (nonatomic, strong) NSMutableArray* milestones;
@property (nonatomic, strong) NSString* name;
@property (nonatomic, strong) NSString* description;
@property (nonatomic, strong) NSNumber* hoursPercent;
@property (nonatomic, strong) NSNumber* taskPercent;
@property (nonatomic, strong) NSNumber* milestonePercent;
@property (nonatomic, strong) NSString* projectId;
@end
