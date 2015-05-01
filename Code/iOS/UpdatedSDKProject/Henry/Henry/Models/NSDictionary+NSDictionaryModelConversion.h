//
//  NSDictionary+NSDictionaryModelConversion.h
//  Henry
//
//  Created by Mark Van Aken on 4/23/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSDictionary (NSDictionaryModelConversion)
- (id) constructModelFromDictionaryWithModelClass:(Class) modelClass;
@end
