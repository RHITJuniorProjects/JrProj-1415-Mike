//
//  HenryCreateBountyViewController.h
//  Henry
//
//  Created by CSSE Department on 2/7/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Firebase/Firebase.h>

@interface HenryCreateBountyViewController : UIViewController <UIPickerViewDelegate, UIPickerViewDataSource>
@property (weak, nonatomic) IBOutlet UIPickerView *ConditionPicker;
@property (weak, nonatomic) IBOutlet UIDatePicker *DatePicker;
@property (weak, nonatomic) IBOutlet UITextField *PointsField;
@property (weak, nonatomic) IBOutlet UITextField *MutatableField;
@property (weak, nonatomic) IBOutlet UILabel *MuatableLabel;
@property NSString* projectId;
@property NSString* milestoneId;
@property NSString* taskId;

- (IBAction)ComfirmationButton:(id)sender;
- (IBAction)cancelButtonPressed:(id)sender;
- (IBAction)dismissKeyboard:(id)sender;

@end
