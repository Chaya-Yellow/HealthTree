//
//  familyListViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/5.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "familyListViewController.h"
#import "MXDataCell.h"
#import "OMESoft.h"
#import "MXUser.h"
#import "consDefine.h"
#import "MBProgressHUD.h"
#import "MXUserData.h"
#import "MXUser.h"
#import "OMEFamilyDataService.h"
#import "MXUserService.h"
#import "Constants.h"
#import "newFamilyMemberController.h"
#import "settingMemViewController.h"
#import "MJRefresh.h"

@interface familyListViewController() <OMEDataServiceDelegate>
@property (nonatomic, retain) UITableView *dataTableView;
@property (nonatomic, retain) UIButton *editButton;
@property (nonatomic, retain) NSMutableArray *Datas;
@property (nonatomic, strong) MXUserData *userData;
@property (nonatomic, retain) OMEFamilyDataService *dataService;
@property (nonatomic, strong) UIView *emptyView;
@property (nonatomic, assign) NSInteger selectedFamilyId;
@property (nonatomic, strong) MBProgressHUD *progressHUD;
@end

@implementation familyListViewController

BOOL isEditting;

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    _dataTableView.delegate = nil;
    _dataTableView.dataSource = nil;
    _dataService.delegate = nil;
    _dataService = nil;
    _Datas = nil;
    _editButton = nil;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        isEditting = NO;
        //askDoctor
        _isAskDoctor = NO;
        //self.title =NSLocalizedString(@"Family_Member_List",@"家庭成员");
    }
    return self;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    isEditting = NO;
    [_editButton setTitle: isEditting ? NSLocalizedString(@"Family_Edit_End",@"完成") :NSLocalizedString(@"Family_Editing", @"编辑") forState: UIControlStateNormal];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    CGRect mainScreen = [[UIScreen mainScreen] applicationFrame];
    mainScreen.size.height -= self.navigationController.navigationBar.frame.size.height;
    //
    [self.navigationController.navigationBar setBarTintColor:UUNavBackColor];
    [self.navigationController.navigationBar setTintColor:UUWhite];
    UILabel *customLab = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 100, 30)];
    [customLab setTextColor:UUWhite];
    [customLab setText:NSLocalizedString(@"Family_Member_List",@"家庭成员")];
    customLab.font = [UIFont boldSystemFontOfSize:20];
    self.navigationItem.titleView = customLab;
    //
    MXUser *user = [[MXUserService sharedInstance] getSelectedUser];
    self.selectedFamilyId = user.familyId;
    
    self.userData = [[MXUserData alloc] init];
    self.view.backgroundColor = [UIColor whiteColor];
    
    self.dataService = [[OMEFamilyDataService alloc] init];
    _dataService.delegate = self;
    
    //  编辑按钮
    self.editButton = [UIButton buttonWithType: UIButtonTypeCustom];
    _editButton.frame = CGRectMake(0, 0, 48, 29);
    [_editButton setTitle:NSLocalizedString(@"Family_Editing", @"编辑") forState: UIControlStateNormal];
    [_editButton setTitleColor: [UIColor blackColor] forState: UIControlStateNormal];
    _editButton.titleLabel.font = [UIFont systemFontOfSize: 14.0f];
    [_editButton setTintColor:[UIColor clearColor]];
    _editButton.titleLabel.font = [UIFont systemFontOfSize: 13.0];
    _editButton.titleLabel.shadowOffset = CGSizeMake(0, -1);
    [_editButton addTarget: self action: @selector(editButtonPressed) forControlEvents: UIControlEventTouchUpInside];
    
    //  放置按钮
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView: _editButton];
    
    //  数据表格
    self.dataTableView = [[UITableView alloc]initWithFrame: [[UIScreen mainScreen] applicationFrame] style: UITableViewStyleGrouped];
    _dataTableView.frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height);
    _dataTableView.delegate = self;
    _dataTableView.dataSource = self;
    [_dataTableView setAllowsSelectionDuringEditing: YES];
    _dataTableView.backgroundColor = UUBackGroundColor;
    _dataTableView.backgroundView.alpha = 1;
    [self.view addSubview: _dataTableView];
    //
    self.emptyView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    _emptyView.backgroundColor = UUFrontColor;
    [self.view addSubview:_emptyView];
    
    UIView *addFamilyView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, mainScreen.size.width,260)];
    addFamilyView.backgroundColor = [UIColor clearColor];
    addFamilyView.center = CGPointMake(self.view.center.x, mainScreen.size.height/2.0);
    
    UIImage *emptyImage = [UIImage imageNamed:@"img_empty_logo"];
    UIImageView *emptyImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, emptyImage.size.width, emptyImage.size.height)];
    emptyImageView.image = emptyImage;
    emptyImageView.center = CGPointMake(self.view.center.x, emptyImageView.frame.size.height/2.0);
    [addFamilyView addSubview:emptyImageView];
    
    NSString *emptyStr =NSLocalizedString(@"Family_No_Member",@"没有添加家庭成员");
    CGSize emptySize = [emptyStr sizeWithFont:[UIFont systemFontOfSize:15.0f] constrainedToSize:CGSizeMake(addFamilyView.frame.size.width, 30)];
    UILabel *emptyLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, emptyImageView.frame.origin.y + emptyImageView.frame.size.height + 27, emptySize.width, emptySize.height)];
    emptyLabel.textAlignment = NSTextAlignmentCenter;
    emptyLabel.text = emptyStr;
    emptyLabel.font = [UIFont systemFontOfSize:15.0f];
    emptyLabel.textColor = [UIColor colorWithHexString:@"#a3a3a3"];
    emptyLabel.backgroundColor = [UIColor clearColor];
    emptyLabel.center = CGPointMake(addFamilyView.center.x, emptyLabel.center.y);
    [addFamilyView addSubview:emptyLabel];
    
    UIImage *emptyBtnImage = [UIImage imageNamed:@"btn_enter_normal"];
    UIImage *emptyBtnSelectedImage = [UIImage imageNamed:@"btn_enter_pressed"];
    NSString *emptyBtnTitle =NSLocalizedString(@"Family_AddFamily_Member",@"请添加家庭成员");
    UIButton *addButton = [UIButton buttonWithType:UIButtonTypeCustom];
    addButton.frame = CGRectMake(0, emptyLabel.frame.origin.y+emptyLabel.frame.size.height+19, 178, 44);
    addButton.backgroundColor = [UIColor clearColor];
    addButton.center = CGPointMake(addFamilyView.center.x, addButton.center.y);
    addButton.titleLabel.font = [UIFont systemFontOfSize:14.0f];
    
    [addButton setBackgroundImage:emptyBtnImage forState:UIControlStateNormal];
    [addButton setBackgroundImage:emptyBtnSelectedImage forState:UIControlStateHighlighted];
    [addButton setTitle:emptyBtnTitle forState:UIControlStateNormal];
    [addButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [addButton addTarget:self action:@selector(addButtonPressed) forControlEvents:UIControlEventTouchUpInside];
    [addFamilyView addSubview:addButton];
    
    [_emptyView addSubview:addFamilyView];
    //[self reloadTableViewDataSource];
    [self setupRefresh];
    [self reloadUserData];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reloadUserData) name:familyDidUpdatedNotification object:nil];
}

- (void)setupRefresh
{
    // 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
    DefineWeakSelf;
    MJRefreshNormalHeader *mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        
        [weakSelf headerRereshing];
        
    }];
    self.dataTableView.mj_header = mj_header;
    //#warning 自动刷新(一进入程序就下拉刷新)
    [self.dataTableView.mj_header beginRefreshing];
    // 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
//    self.dataTableView.headerPullToRefreshText = NSLocalizedString(@"Pull_down_reflash",@"下拉可以刷新了");
//    self.dataTableView.headerReleaseToRefreshText =NSLocalizedString(@"Relax_reflashing",@"松开马上刷新了");
//    self.dataTableView.headerRefreshingText = NSLocalizedString(@"Data_reflashing",@"数据刷新中...");
}

- (void)headerRereshing
{
    [self reloadTableViewDataSource];
    // 2.2秒后刷新表格UI
    //dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        // (最好在刷新表格后调用)调用endRefreshing可以结束刷新状态
    //});
}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark - Private Methods

- (void)reloadRightBarButtonItem
{
    if ([_Datas count] != 0) {
        NSString *title;
        if (_dataTableView.editing) {
            title = NSLocalizedString(@"Family_Edit_End",@"完成");
        } else {
            title = NSLocalizedString(@"Family_Editing", @"编辑");
        }
        self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:title style:UIBarButtonItemStyleBordered target:self action:@selector(editButtonPressed)];
    } else {
        self.navigationItem.rightBarButtonItem = nil;
    }
}

- (void)popViewController
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)reloadUserData
{
    MXUser *user = [[MXUserService sharedInstance] getSelectedUser];
    self.selectedFamilyId = user.familyId;
    NSInteger idNum = [[MXUserService sharedInstance] getCurrentLoginedMemeberId];
    self.Datas = [_userData getAllUserWithMemberId:idNum];
    [self.dataTableView reloadData];
    
    if ([_Datas count] != 0) {
        [_emptyView setHidden:YES];
        NSString *title;
        if (_dataTableView.editing) {
            title = NSLocalizedString(@"Family_Edit_End",@"完成");
        } else {
            title = NSLocalizedString(@"Family_Editing", @"编辑");
        }
        self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:title style:UIBarButtonItemStyleBordered target:self action:@selector(editButtonPressed)];
    } else {
        [_emptyView setHidden:NO];
        self.navigationItem.rightBarButtonItem = nil;
    }
}

- (void)deleteFamilyAction
{
    MXUserData *userData = [[MXUserData alloc] init];
    NSArray *loginedArray = [userData getLoginInfo];
    NSDictionary *dataDic = [loginedArray objectAtIndex:0];
    NSInteger idNum = [[dataDic objectForKey:@"id"] intValue];
    NSString *key = [dataDic objectForKey:@"clientKey"];
    [_dataService requestOMEDeleteFamilyByMemberId:idNum Key:key familyId:deleteId];
    _dataService.tag = 2;
}

#pragma mark -ButtonEvent

- (void) editButtonPressed
{
    [_dataTableView setEditing:!_dataTableView.editing animated:YES];
    if (_dataTableView.editing) {
        [_dataTableView deleteSections:[NSIndexSet indexSetWithIndex:1] withRowAnimation:UITableViewRowAnimationFade];
    } else {
        
        [_dataTableView insertSections:[NSIndexSet indexSetWithIndex:1] withRowAnimation:UITableViewRowAnimationNone];
    }
    [self reloadRightBarButtonItem];
}

- (void)addButtonPressed
{
    NSLog(@"add...");
    newFamilyMemberController* newController = [[newFamilyMemberController alloc]init];
    [self.navigationController pushViewController:newController animated:YES];
    /*
    MXNewNameViewController *nameViewController = [[MXNewNameViewController alloc]init];
    [self.navigationController pushViewController: nameViewController animated: YES];
     */
}

#pragma mark -TableDegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    int num = 0;
    num += [_Datas count] ? 1 : 0;
    if (!tableView.editing) {
        num += 1;
    }
    return num;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return [_Datas count] ? [_Datas count] : 1;
    } else {
        return 1;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        return [_Datas count] ? 60.0f : 44.0f;
    } else {
        return 44.0f;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (([_Datas count] > 0 && indexPath.section == 1) || ([_Datas count] == 0 && indexPath.section == 0)) {
        UITableViewCell *aCell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:nil];
        aCell.accessoryType = UITableViewCellAccessoryNone;
        aCell.selectionStyle = UITableViewCellSelectionStyleDefault;
        aCell.textLabel.text =NSLocalizedString(@"Family_Mem_Adding",@"添加家庭成员");
        aCell.textLabel.textAlignment = NSTextAlignmentCenter;
        aCell.textLabel.font = [UIFont systemFontOfSize:17.0f];
        aCell.textLabel.textColor = UUWhite;
        aCell.textLabel.highlightedTextColor = [UIColor whiteColor];
        aCell.textLabel.backgroundColor = [UIColor clearColor];
        aCell.backgroundView = [[UIImageView alloc] initWithImage:[[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:72.0/255 blue:105.0/255 alpha:1.0] ] stretchableImageWithLeftCapWidth:2 topCapHeight:2]];
        aCell.selectedBackgroundView = [[UIImageView alloc] initWithImage:[[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:102.0/255 blue:127.0/255 alpha:1.0]] stretchableImageWithLeftCapWidth:2 topCapHeight:2]];
        return aCell;
    }
    
    static NSString *CellIdentifier = @"Cell";
    
    MXDataCell *cell = (MXDataCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[MXDataCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:nil];
    }
    
    //    cell.textLabel.text = [[[_sectionData objectAtIndex:indexPath.section] objectAtIndex:indexPath.row] objectForKey:kTitleKey];
    cell.backgroundColor =[UUBackGroundColor colorWithAlphaComponent:1];
    //cell.textLabel.textColor = [UIColor whiteColor];
    cell.textLabel.font = [UIFont systemFontOfSize:16.0f];
    //cell.detailTextLabel.textColor = [UIColor colorWithHexString:@"#98989A"];
    //NSLog(@"_datas=%@", _Datas);
    MXUser *user = (MXUser *)[_Datas objectAtIndex:indexPath.row];
    cell.userData = user;
    
    if (user.familyId == self.selectedFamilyId) {
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
        cell.accessoryView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_checkmark.png"]];
    } else {
        cell.accessoryType = UITableViewCellAccessoryNone;
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleDefault;
    cell.editingAccessoryType = UITableViewCellAccessoryDisclosureIndicator;
    cell.selectedBackgroundView = [[UIImageView alloc] initWithImage:[UIImage createImageWithColor:[UIColor colorWithRed:117.0/255 green:136.0/255 blue:171.0/255 alpha:1.0]]];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (([_Datas count] > 0 && indexPath.section == 1) || ([_Datas count] == 0 && indexPath.section == 0)) {
        [self addButtonPressed];
    } else {
        MXUser *user = (MXUser *)[_Datas objectAtIndex:indexPath.row];
        if (tableView.editing) {
            settingMemViewController *userSettingViewController = [[settingMemViewController alloc] initWithUser:user];
            //userSettingViewController.title = NSLocalizedString(@"Family_EditUser", @"编辑用户");
            [self.navigationController pushViewController:userSettingViewController animated:YES];
        } else {
            /*
            if (_isAskDoctor) {
                MXUser *user = (MXUser *)[_Datas objectAtIndex:indexPath.row];
                FAQViewController *faqViewController = [[FAQViewController alloc] initWithFamilyId:user.familyId];
                [self.navigationController pushViewController:faqViewController animated:YES];
                return;
            }*/
            [[MXUserService sharedInstance] setFamilyId:user.familyId];
            MXUser *currentUser = [[MXUserService sharedInstance] getSelectedUser];
            self.selectedFamilyId = currentUser.familyId;
            //self.selectedFamilyId = user.familyId;
            [tableView reloadData];
            [self performSelector:@selector(popViewController) withObject:nil afterDelay:0.35];
            
            //            [[NSNotificationCenter defaultCenter] postNotificationName:FamilyIdDidChangedNotification object:nil];
        }
    }
    
}

- (void)tableView:(UITableView *)tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [_dataTableView cellForRowAtIndexPath: indexPath].accessoryType = UITableViewCellAccessoryNone;
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (!tableView.editing)
        return UITableViewCellEditingStyleNone;
    else {
        return UITableViewCellEditingStyleDelete;
    }
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    MXUser *selectedUser = [_Datas objectAtIndex:indexPath.row];
    deleteId = selectedUser.familyId;
    
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Family_Alert_Emptied", @"警告：所有记录将会清空") message:NSLocalizedString(@"Family_Alert_Really", @"真的要删除此用户？") delegate:self cancelButtonTitle:NSLocalizedString(@"Sys_Cancel", @"取消")  otherButtonTitles:NSLocalizedString(@"Sys_Ok", @"确定"), nil];
    alertView.tag = 100;
    [alertView show];
}


- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (([_Datas count] > 0 && indexPath.section == 1) || ([_Datas count] == 0 && indexPath.section == 0)) {
        return NO;
    }
    return YES;
}

#pragma mark - alertview delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    
    if (alertView.tag == 100) {
        if (buttonIndex == 1) {
            //delete
            [self editButtonPressed];
            self.dataTableView.userInteractionEnabled = NO;
            [self.navigationController.view addSubview:self.progressHUD];
            self.progressHUD.labelText = NSLocalizedString(@"Family_Remove_Member", @"删除成员...");
            [self.progressHUD show:YES];
            [self performSelector:@selector(deleteFamilyAction) withObject:nil afterDelay:0];
        }
    }
}

#pragma mark -ScrollViewDelegate
#pragma mark Data Source Loading / Reloading Methods

- (void)reloadTableViewDataSource{
    NSLog(@"000000000000");
    self.dataTableView.userInteractionEnabled = NO;
    MXUserData *userData = [[MXUserData alloc] init];
    NSArray *loginedArray = [userData getLoginInfo];
    if(loginedArray && loginedArray.count>0)
    {
        NSDictionary *dataDic = [loginedArray objectAtIndex:0];
        NSInteger idNum = [[dataDic objectForKey:@"id"] intValue];
        NSString *key = [dataDic objectForKey:@"clientKey"];
        _reloading = YES;
        [_dataService requestOMEGetFamiliesByMemberId:idNum Key:key];
        _dataService.tag = 1;
    }
    else
    {
        MBProgressHUD *progressHUD = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
        [self.navigationController.view addSubview:progressHUD];
        progressHUD.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"37x-Failed.png"]];
        progressHUD.labelText =NSLocalizedString(@"Layout_WithoutLogin", @"无效登陆");
        progressHUD.mode =MBProgressHUDModeCustomView;
        [progressHUD show:YES];
        [progressHUD hide:YES afterDelay:2.0];
    }
}

- (void)doneLoadingTableViewData:(BOOL)isSuccess{
    //  model should call this when its done loading
    _reloading = !isSuccess;
    if (!isSuccess) {
        MBProgressHUD *progressHUD = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
        [self.navigationController.view addSubview:progressHUD];
        progressHUD.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"37x-Failed.png"]];
        progressHUD.labelText =NSLocalizedString(@"Family_Update_Fail",@"更新失败");
        progressHUD.mode =MBProgressHUDModeCustomView;
        [progressHUD show:YES];
        [progressHUD hide:YES afterDelay:1.0];
    } else {
        NSLog(@"刷新成功！！！");
        [self reloadUserData];
    }
    self.dataTableView.userInteractionEnabled = YES;
}



#pragma mark - OMEDataService
- (void)dataService:(OMEDataService *)dataService didFinishWithData:(NSDictionary *)dataDic
{
    self.dataTableView.userInteractionEnabled = YES;
    //NSLog(@"dataDic = %@", dataDic);
    if (dataService.tag == 1) {
        NSDictionary *data = [dataDic objectForKey:@"data"];
        NSArray *familiesData = [data objectForKey:@"families"];
        if ([familiesData count]) {
            MXUserData *userData = [[MXUserData alloc] init];
            NSArray *loginedArray = [userData getLoginInfo];
            NSDictionary *dataDic = [loginedArray objectAtIndex:0];
            NSInteger idNum = [[dataDic objectForKey:@"id"] intValue];
            //NSLog(@"**********&&&&&&&&&&&&&&&&&idnum = %d", idNum);
            BOOL isInsert =  [userData syncUserData:familiesData memberId:idNum];
            //NSLog(@"isInsert =%d", isInsert);
            if (isInsert) {
                [self doneLoadingTableViewData:YES];
            } else {
                [self doneLoadingTableViewData:NO];
            }
        } else {
            [self doneLoadingTableViewData:YES];
        }
        [self.dataTableView.mj_header endRefreshing];
    } else if (dataService.tag == 2) {
        
        NSString *codeStr = [dataDic objectForKey:@"err_code"];
        if ([codeStr intValue]) {
            [self.progressHUD hide:YES];
            UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Guide_Network", @"网络错误") message:NSLocalizedString(@"Alert_Failure", @"失败，发生未知异常") delegate:nil cancelButtonTitle:NSLocalizedString(@"Sys_Ok", @"确定") otherButtonTitles:nil];
            [alertView show];
        } else {
            self.progressHUD.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"37x-Checkmark.png"]];
            self.progressHUD.labelText = NSLocalizedString(@"Family_Delete_Success", @"删除成功");
            [self.progressHUD hide:YES afterDelay:1.0];
            MXUserData *userData = [[MXUserData alloc] init];
            NSArray *loginedArray = [userData getLoginInfo];
            NSDictionary *dataDic = [loginedArray objectAtIndex:0];
            NSInteger idNum = [[dataDic objectForKey:@"id"] intValue];
            BOOL success = [userData deleteDataToUserWithId:deleteId memberId:idNum];
            if (success) {
                [self reloadUserData];
            }
        }
        
    }
}

- (void)dataService:(OMEDataService *)dataService didFailWithError:(NSError *)error
{
    if (dataService.tag == 1) {
        [self.dataTableView.mj_header endRefreshing];
        [self doneLoadingTableViewData:NO];
    } else if (dataService.tag == 2) {
        self.dataTableView.userInteractionEnabled = YES;
        self.progressHUD.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"37x-Checkmark.png"]];
        self.progressHUD.labelText = error.localizedDescription;
        [self.progressHUD hide:YES afterDelay:1.0];
    }
}

- (MBProgressHUD *)progressHUD
{
    if (!_progressHUD) {
        _progressHUD = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
        _progressHUD.removeFromSuperViewOnHide = YES;
    }
    return _progressHUD;
}
@end
