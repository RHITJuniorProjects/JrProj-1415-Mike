//
//  HenryProjectsTableViewController.m
//  Henry
//
//  Created by Mason Schneider on 9/16/14.
//  Copyright (c) 2014 Rose-Hulman. All rights reserved.
//

#import "HenryProjectsTableViewController.h"
#import "HenryProjectDetailViewController.h"
#import "HenryMilestonesTableViewController.h"
#import "HenryRootNavigationController.h"
#import "SWRevealViewController.h"
#import "HenryFirebase.h"
#import "HenryProjectObject.h"

@interface HenryProjectsTableViewController ()
@property NSMutableArray *cellText;
@property NSMutableArray *projectDescriptions;
@property NSMutableArray *projectIDs;
@property NSMutableArray *projects;
@property Firebase *fb;
@property (strong, nonatomic) NSMutableArray *tasks;
@end

@implementation HenryProjectsTableViewController

-(IBAction)logoutButtonPressed:(id)sender {
    @try{
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults removeObjectForKey:@"id"];
        [defaults removeObjectForKey:@"token"];
        [defaults synchronize];
        UIStoryboard *sb = [UIStoryboard storyboardWithName:@"iPadLoginStoryboard" bundle:nil];
        UIViewController *initialView = [sb instantiateInitialViewController];
        [self presentViewController:initialView animated:YES completion:nil];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (id)initWithStyle:(UITableViewStyle)style
{
    @try{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}


- (void)viewDidLoad
{
    @try{
    [super viewDidLoad];
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    self.uid = [defaults objectForKey:@"id"];
    
    //self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    SWRevealViewController *revealViewController = self.revealViewController;
    if ( revealViewController )
    {
        [self.navButton setTarget: self.revealViewController];
        [self.navButton setAction: @selector( revealToggle: )];
        [self.navigationController.navigationBar addGestureRecognizer: self.revealViewController.panGestureRecognizer];
    }
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    self.cellText = [[NSMutableArray alloc] init];
    self.fb = [HenryFirebase getFirebaseObject];
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    // Table will be updated when the projects a user is assigned to changes
    [self.fb observeEventType:FEventTypeValue withBlock:^(FDataSnapshot *snapshot) {
        [self updateTable:snapshot];
    } withCancelBlock:^(NSError *error) {
        NSLog(@"%@", error.description);
    }];
    }@catch(NSException *exception){
        NSLog(@"we caught the exception");
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

-(void)updateTable:(FDataSnapshot *)snapshot {
    @try{
    NSArray *userProjects = [snapshot.value[@"users"][self.uid][@"projects"] allKeys];
    NSArray *projects = [snapshot.value[@"projects"] allKeys];
    self.projects = [[NSMutableArray alloc] init];
    for (NSString *project in projects) {
        if ([userProjects containsObject:project]) {
            HenryProjectObject *projectObject = [[HenryProjectObject alloc] init];
            NSString *name = snapshot.value[@"projects"][project][@"name"];
            projectObject.name = name;
            NSString *dueDate = snapshot.value[@"projects"][project][@"due_date"];
            projectObject.dueDate = dueDate;
            projectObject.projectID = project;
            [self.projects addObject:projectObject];
        }
    }
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    [self sortByAlphabeticalAToZ];
    [self.tableView reloadData];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (void)didReceiveMemoryWarning
{
    @try{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    @try{
    return 1;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    @try{
    // Return the number of rows in the section.
    return self.projects.count;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    @try{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ProjectCell" forIndexPath:indexPath];
    
    // Configure the cell...
    HenryProjectObject *hpo = [self.projects objectAtIndex:indexPath.row];
    cell.textLabel.text = hpo.name;
    cell.detailTextLabel.text = hpo.dueDate;
    
    return cell;
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}


- (IBAction)segControlClicked:(id)sender
{
    @try{
    //Figures out the last clicked segment.
    int clickedSegment = [sender selectedSegmentIndex];
    switch(clickedSegment)
    {
        //Segment 1 is A-Z
        case 0:
            [self sortByAlphabeticalAToZ];
            break;
            
        //Segment 2 is Z-A
        case 1:
            [self sortByAlphabeticalZToA];
            break;
            
        //Segment 3 is Due Date
        case 2:
            [self sortByDueDate];
            break;
    }
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

-(void)sortByAlphabeticalAToZ
{
    @try{
    NSSortDescriptor *sort = [NSSortDescriptor sortDescriptorWithKey:@"name" ascending:YES];
    [self.projects sortUsingDescriptors:[NSArray arrayWithObject:sort]];
    [self.tableView reloadData];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}

-(void)sortByAlphabeticalZToA
{
    @try{
    NSSortDescriptor *sort = [NSSortDescriptor sortDescriptorWithKey:@"name" ascending:NO];
    [self.projects sortUsingDescriptors:[NSArray arrayWithObject:sort]];
    [self.tableView reloadData];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
}
-(void)sortByDueDate
{
    @try{
    NSSortDescriptor *sort = [NSSortDescriptor sortDescriptorWithKey:@"dueDate" ascending:YES];
    [self.projects sortUsingDescriptors:[NSArray arrayWithObject:sort]];
    [self.tableView reloadData];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
    
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


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    @try{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    NSIndexPath *indexPath = [self.tableView indexPathForCell:sender];
    
    if ([segue.identifier isEqualToString:@"PtoM"]) {
        HenryMilestonesTableViewController *vc = [segue destinationViewController];
        HenryProjectObject *hpo = [self.projects objectAtIndex:indexPath.row];
        vc.ProjectID = hpo.projectID;
        vc.tasks = self.tasks;
        vc.uid = self.uid;
    } else {
        NSIndexPath *indexPath = [self.tableView indexPathForCell:(UITableViewCell *)[(UIView*)[(UIView*)sender superview] superview]];
        HenryProjectDetailViewController *vc = [segue destinationViewController];
        HenryProjectObject *hpo = [self.projects objectAtIndex:indexPath.row];
        vc.projectID = hpo.projectID;
        vc.tasks = self.tasks;
        vc.uid = self.uid;
    }
    
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
    }@catch(NSException *exception){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failing Gracefully" message:@"Something strange has happened. App is closing." delegate:self cancelButtonTitle:nil otherButtonTitles:nil];
        [alert show];
        exit(0);
        
    }
}


- (IBAction)navButton:(id)sender {
}
@end
