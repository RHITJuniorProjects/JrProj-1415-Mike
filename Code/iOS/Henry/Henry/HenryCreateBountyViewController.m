//
//  HenryCreateBountyViewController.m
//  Henry
//
//  Created by CSSE Department on 2/7/15.
//  Copyright (c) 2015 Rose-Hulman. All rights reserved.
//

#import "HenryCreateBountyViewController.h"

@interface HenryCreateBountyViewController ()

@property NSArray *titles;

@end

@implementation HenryCreateBountyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.titles = [[NSArray alloc] initWithObjects: @"Completion", @"Complete before: date", @"Complete Within: x hours", @"Write X or more lines", nil];
}

-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [self.titles objectAtIndex:row];
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return [self.titles count];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)ComfirmationButton:(id)sender {
//    {"bounty 1":{"claimed":"None","description":"Complete this task by 2015-01-31","due_date":"2015-01-31","hour_limit":"None","line_limit":"None","name":"Done by end of January","points":8}}
    
    NSMutableDictionary *newData = [[NSMutableDictionary alloc] init];
    [newData setObject:@"None" forKey:@"claimed"];
    [newData setObject:@"None" forKey:@"description"];
    [newData setObject:self.PointsField.text forKey:@"points"];
    
    NSUInteger selectedRow = [self.ConditionPicker selectedRowInComponent:0];
    
    if (selectedRow == 0) {
        // Completion Bounty
        [newData setObject:@"Completion" forKey:@"name"];
        [newData setObject:@"None" forKey:@"due_date"];
        [newData setObject:@"None" forKey:@"hour_limit"];
        [newData setObject:@"None" forKey:@"line_limit"];
    } else if (selectedRow == 1) {
        // Date Bounty
        [newData setObject:@"Date" forKey:@"name"];
        
        NSDate *date = self.DatePicker.date;
        NSDateFormatter *df = [[NSDateFormatter alloc] init];
        [df setDateFormat:@"yyyy-MM-dd"];
        
        [newData setObject:[df stringFromDate:date] forKey:@"due_date"];
        [newData setObject:@"None" forKey:@"hour_limit"];
        [newData setObject:@"None" forKey:@"line_limit"];
    } else if (selectedRow == 2) {
        // Hours Bonty
        [newData setObject:@"Hours" forKey:@"name"];
        [newData setObject:@"None" forKey:@"due_date"];
        [newData setObject:self.MutatableField.text forKey:@"hour_limit"];
        [newData setObject:@"None" forKey:@"line_limit"];
    } else {
        // Min Lines Bounty
        [newData setObject:@"Lines" forKey:@"name"];
        [newData setObject:@"None" forKey:@"due_date"];
        [newData setObject:@"None" forKey:@"hour_limit"];
        [newData setObject:self.MutatableField.text forKey:@"line_limit"];
    }
    
    [self.fb setValue:newData];
    
    [self dismissViewControllerAnimated:YES completion:nil];
    
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    if (pickerView != self.ConditionPicker)
        return;
    
    if (row == 1) {
        self.DatePicker.hidden = NO;
    } else {
        self.DatePicker.hidden = YES;
    }
    
    if (row == 2) {
        self.MutatableField.hidden = NO;
        self.MuatableLabel.hidden = NO;
        [self.MuatableLabel setText:@"Max Hours"];
    } else if (row == 3) {
        self.MutatableField.hidden = NO;
        self.MuatableLabel.hidden = NO;
        [self.MuatableLabel setText:@"Min. Lines"];
    } else {
        self.MutatableField.hidden = YES;
        self.MuatableLabel.hidden = YES;
    }
}

- (IBAction)cancelButtonPressed:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)dismissKeyboard:(id)sender {
    [self.MutatableField resignFirstResponder];
    [self.PointsField resignFirstResponder];
}
@end
