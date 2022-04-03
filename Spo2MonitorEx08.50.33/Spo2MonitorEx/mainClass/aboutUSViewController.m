//
//  aboutUSViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/11/24.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "aboutUSViewController.h"
#import "consDefine.h"
@interface aboutUSViewController ()
@property (strong, nonatomic) UIImageView* logoView;
@property (strong, nonatomic) UILabel* titleLabel;
@property (strong, nonatomic) UILabel* versionLabel;
@property (strong, nonatomic) UILabel* companyInfoLabel;
@end
@implementation aboutUSViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    self.tabBarController.tabBar.hidden = YES;
    CGRect rect = self.view.bounds;
    float height = CGRectGetHeight(rect);
    float width = CGRectGetWidth(rect);
    UIImage* image = [UIImage imageNamed:@"aboutus.png"];
    _logoView = [[UIImageView alloc] initWithFrame:CGRectMake((width - 80) / 2, 115, 80, 80)];
    _logoView.backgroundColor = [UIColor clearColor];
    _logoView.image = image;
    _logoView.clipsToBounds = YES;
    _logoView.layer.cornerRadius = 8;
    [self.view addSubview:_logoView];
    //title
    _titleLabel = [[UILabel alloc] init];
    _titleLabel.font = [UIFont boldSystemFontOfSize:15.0f];
    NSString* titleStr = NSLocalizedString(@"OXIMETER", @"血氧仪");
    CGSize titleSize = [titleStr sizeWithFont:_titleLabel.font constrainedToSize:CGSizeMake(200, 20) lineBreakMode:NSLineBreakByWordWrapping];
    _titleLabel.frame = CGRectMake(_logoView.frame.origin.x, _logoView.frame.origin.y + _logoView.frame.size.height + 16, titleSize.width, 20);
    _titleLabel.center = CGPointMake(_logoView.center.x, _titleLabel.center.y);
    _titleLabel.textColor = UUBlack;
    _titleLabel.backgroundColor = [UIColor clearColor];
    _titleLabel.textAlignment = NSTextAlignmentCenter;
    _titleLabel.text = titleStr;
    [self.view addSubview:_titleLabel];
    //version
    _versionLabel = [[UILabel alloc] initWithFrame:CGRectMake(_titleLabel.frame.origin.x, _titleLabel.frame.origin.y + 10, _titleLabel.frame.size.width, 40)];
    _versionLabel.backgroundColor = [UIColor clearColor];
    _versionLabel.textAlignment = NSTextAlignmentCenter;
    _versionLabel.textColor = UUBlack;
    _versionLabel.font = [UIFont systemFontOfSize:12];
    NSDictionary* infoDictionary = [[NSBundle mainBundle] infoDictionary];
    NSString *version = [NSString stringWithFormat:@"v%@", [infoDictionary objectForKey:@"CFBundleShortVersionString"]];
    _versionLabel.text = version;
    [self.view addSubview:_versionLabel];
    //companyInfo
    NSString* copyrightInfoStr = @"Copyright © Guangdong Biolight Meditech Co.,Ltd.\n All Rights Reserved.";
    _companyInfoLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, height - 80, width, 40)];
    _companyInfoLabel.backgroundColor = [UIColor clearColor];
    _companyInfoLabel.textAlignment = NSTextAlignmentCenter;
    _companyInfoLabel.lineBreakMode = NSLineBreakByCharWrapping;
    _companyInfoLabel.numberOfLines = 2;
    _companyInfoLabel.textColor = UUBlack;
    _companyInfoLabel.font = [UIFont systemFontOfSize:14.0f];
    _companyInfoLabel.text = copyrightInfoStr;
    [self.view addSubview:_companyInfoLabel];
}

@end
