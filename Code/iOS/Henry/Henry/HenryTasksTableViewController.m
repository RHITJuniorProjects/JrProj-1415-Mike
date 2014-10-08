//
//  HenryTasksTableViewController.m
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryTasksTableViewController.h"
#import <Firebase/Firebase.h>

@interface HenryTasksTableViewController ()

@property NSMutableArray *tasks;
@property Firebase *fb;
@end

@implementation HenryTasksTableViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.tasks = [[NSMutableArray alloc] init];
    
//    if ([self.ProjectID isEqualToString:@"Project 1"]) {
//        if ([self.MileStoneID isEqualToString:@"Milestone P-1-1"]) {
//            [self.tasks addObject:@"P1: M1: Task 1"];
//            [self.tasks addObject:@"P1: M1: Task 2"];
//        } else if ([self.MileStoneID isEqualToString:@"Milestone P-1-2"]) {
//            [self.tasks addObject:@"P1: M2: Task 1"];
//            [self.tasks addObject:@"P1: M2: Task 2"];
//        }
//    } else if ([self.ProjectID isEqualToString:@"Project 2"]) {
//        if ([self.MileStoneID isEqualToString:@"Milestone P-2-1"]) {
//            [self.tasks addObject:@"P2: M1: Task 1"];
//            [self.tasks addObject:@"P2: M1: Task 2"];
//        } else if ([self.MileStoneID isEqualToString:@"Milestone P-2-2"]) {
//            [self.tasks addObject:@"P2: M2: Task 1"];
//            [self.tasks addObject:@"P2: M2: Task 2"];
//        }
//    } else {
//        self.MileStoneID = @"Null";
//    }
    
    self.fb = [[Firebase alloc] initWithUrl:@"https://henry371.firebaseio.com/projects/"];
    //    self.fb = [[Firebase alloc] initWithUrl:url];
    
    // Attach a block to read the data at our posts reference
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateTable];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    
    self.title = self.milestoneName;
}

-(void)updateTable {
    NSString *urlString = [NSString stringWithFormat:@"https:henry371.firebaseio.com/projects/%@/milestones/%@/tasks.json", self.ProjectID, self.MileStoneID];
    NSURL *jsonURL = [NSURL URLWithString:urlString];
    NSData *data = [NSData dataWithContentsOfURL:jsonURL];
    NSError *error;
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&error];
    NSArray *keys = [json allKeys];
    self.tasks = [[NSMutableArray alloc] init];
    
    for (NSString *key in keys) {
        NSString *name = [[json objectForKey:key] objectForKey:@"name"];
        [self.tasks addObject:name];
    }
    [self.tableView reloadData];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [self.tasks count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"TaskCell" forIndexPath:indexPath];
    
    // Configure the cell...
    cell.textLabel.text = [self.tasks objectAtIndex:indexPath.row];
    
    return cell;
}


/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
