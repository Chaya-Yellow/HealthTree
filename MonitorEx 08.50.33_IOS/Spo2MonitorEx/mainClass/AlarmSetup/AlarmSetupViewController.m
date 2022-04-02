//
//  AlarmSetupViewController.m
//  Spo2MonitorEx
//
//  Created by loyal on 2020/2/17.
//  Copyright © 2020 kim. All rights reserved.
//

#import "AlarmSetupViewController.h"
#import "AlarmSetupHeaderView.h"
#import "AlarmSetupTableViewCell.h"

@interface AlarmSetupViewController ()<UITableViewDelegate,UITableViewDataSource>
@property(nonatomic,strong)UITableView *tableView;

@end

@implementation AlarmSetupViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self initAlarmSetuoInit];
    
    [self creatAlarmSetupUI];
    
}

- (void)initAlarmSetuoInit {
 
    self.title = NSLocalizedString(@"AlarmSetup",@"报警设置");
    
    self.view.backgroundColor = UUCEllFrontColor;
    
    // 禁止使用系统自带的滑动手势
    self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    [self.navigationController.navigationBar setBarTintColor:UUNavBackColor];
    [self.navigationController.navigationBar setTintColor:UUWhite];
    [self.navigationController.navigationBar setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    
}

- (void)creatAlarmSetupUI {
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_W, SCREEN_NavH) style:UITableViewStyleGrouped];
    self.tableView.backgroundColor = UUCEllFrontColor;
    [self.tableView registerClass:[AlarmSetupTableViewCell class] forCellReuseIdentifier:@"alarmCell"];
    [self.tableView registerClass:[AlarmSetupHeaderView class] forHeaderFooterViewReuseIdentifier:@"alarmHeader"];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.tableView.estimatedRowHeight = 74;
    self.tableView.estimatedSectionFooterHeight = self.tableView.estimatedSectionHeaderHeight = 0;
    [self.view addSubview:self.tableView];
    
}

#pragma mark - UITableViewDelegate,UITableViewDataSource -
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {

    return 3;
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return 1;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    AlarmSetupTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"alarmCell" forIndexPath:indexPath];
    
    cell.backgroundColor = UUCEllFrontColor;
    
    cell.section = indexPath.section;
        
    return cell;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 205;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
 
    return 0.01;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
 
    return 31;
    
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
 
    AlarmSetupHeaderView *header = [tableView dequeueReusableHeaderFooterViewWithIdentifier:@"alarmHeader"];
    
    switch (section) {
        case 0:{
            
            header.titleLb.text = NSLocalizedString(@"OxygenSetup", "血氧饱和度");
        }
            break;
            
        case 1:{
                   
               header.titleLb.text = NSLocalizedString(@"PulseRate", "脉搏率");
           }
               break;
        
        case 2:{
                   
               header.titleLb.text = NSLocalizedString(@"PerfusionIndex", "血流灌注指数");
           }
               break;
            
        default:
            break;
    }
    
    return header;
    
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
