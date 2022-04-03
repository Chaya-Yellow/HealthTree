//
//  GuideViewController.m
//  Spo2MonitorEx
//
//  Created by loyal on 2020/2/16.
//  Copyright © 2020 kim. All rights reserved.
//

#import "GuideViewController.h"
#import "consDefine.h"
#import "AppDelegate.h"

@interface GuideViewController ()<UIScrollViewDelegate>

/**
 滚动视图
 */
@property(nonatomic,strong)UIScrollView *scrollView;

@end

@implementation GuideViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
        
    self.view.backgroundColor = [UIColor whiteColor];
    
    NSUserDefaults *user = [NSUserDefaults standardUserDefaults];
    [user setInteger:50 forKey:@"SP_L"];
    [user setInteger:100 forKey:@"SP_H"];
    [user setBool:YES forKey:@"SP_H_swi"];
    [user setBool:YES forKey:@"SP_L_swi"];
    
    [user setInteger:40 forKey:@"PR_L"];
    [user setInteger:200 forKey:@"PR_H"];
    [user setBool:YES forKey:@"PR_H_swi"];
    [user setBool:YES forKey:@"PR_L_swi"];
    
    [user setInteger:0 forKey:@"PI_L"];
    [user setInteger:20 forKey:@"PI_H"];
    [user setBool:NO forKey:@"PI_H_swi"];
    [user setBool:NO forKey:@"PI_L_swi"];


    [self creatScrollView];

}

/** 布局主控件 */
- (void)creatScrollView {

    self.scrollView = [[UIScrollView alloc] initWithFrame:self.view.bounds];
    self.scrollView.delegate = self;
    self.scrollView.pagingEnabled = YES;
    self.scrollView.showsVerticalScrollIndicator = NO;
    self.scrollView.showsHorizontalScrollIndicator = NO;
    self.scrollView.backgroundColor = [UIColor whiteColor];
    self.scrollView.contentSize = CGSizeMake(SCREEN_W * 2, SCREEN_H);
    [self.view addSubview:self.scrollView];
    
    CGFloat y = [UIApplication sharedApplication].statusBarFrame.size.height;
    
    UIImageView *img = [[UIImageView alloc] initWithFrame:CGRectMake(0, y, SCREEN_W, SCREEN_H-y)];
    img.image = [UIImage imageNamed:@"guide1"];
//    img.contentMode = UIViewContentModeScaleAspectFill;
//    img.clipsToBounds = YES;
    [self.scrollView addSubview:img];
    
    UIImageView *img_2 = [[UIImageView alloc] initWithFrame:CGRectMake(SCREEN_W, y, SCREEN_W, SCREEN_H-y)];
    img_2.image = [UIImage imageNamed:@"guide2"];
//    img_2.contentMode = UIViewContentModeScaleAspectFill;
//    img_2.clipsToBounds = YES;
    [self.scrollView addSubview:img_2];
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(touchUpInside)];
    img_2.userInteractionEnabled = YES;
    [img_2 addGestureRecognizer:tap];
    
}

- (void)touchUpInside {
    
    [[NSUserDefaults standardUserDefaults] setObject:[NSNumber numberWithBool:YES] forKey:@"isFinIshGuideScroll"];
    
    [app gotoRootVC];
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
