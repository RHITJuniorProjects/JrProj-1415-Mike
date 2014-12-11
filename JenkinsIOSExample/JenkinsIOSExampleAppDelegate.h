//
//  JenkinsIOSExampleAppDelegate.h
//  JenkinsIOSExample
//
//  Created by Shaun Ervine on 23/06/11.
//  Copyright 2011 Shine Technologies. All rights reserved.
//

#import <UIKit/UIKit.h>

@class JenkinsIOSExampleViewController;

@interface JenkinsIOSExampleAppDelegate : NSObject <UIApplicationDelegate> {

}

@property (nonatomic, retain) IBOutlet UIWindow *window;

@property (nonatomic, retain) IBOutlet JenkinsIOSExampleViewController *viewController;

@end
