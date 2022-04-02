//
//  consDefine.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/21.
//  Copyright © 2015年 luteng. All rights reserved.
//

#ifndef consDefine_h
#define consDefine_h

#define IsFinIshGuideScroll @"isFinIshGuideScroll"

#define INVAL_VALUE 65535
#define PARAM_LABEL_ID_W 100         //参数标明宽
#define PARAM_LABEL_ID_H 30         //参数标明高度
#define PARAM_LABEL_ID_FONT 14
#define PARAM_VALUE_FONT_IPHONE 100
#define PARAM_VALUE_FONT_IPAD   80
#define PARAM_IMAGE_W 50            //参数image宽
#define PARAM_IMAGE_H 50            //参数image高
#define PARAM_BLOOD_W 20

#define TREND_LABEL_H 20
#define TREND_LABEL_W 100
#define COLOR_LABEL_H 10
#define COLOR_LABEL_W 30

#define BUTTON_HEIGHT 35                //按钮高度
#define TEXT_EDIT_HEIGHT 35             //text高度
#define BUTTON_H_IPAD 50                //ipad按钮高度
#define BUTTON_WIDTH_IPHONE  200        //iphone按钮宽度
#define BUTTON_WIDTH_IPAD   300         //ipad按钮宽度
#define LABEL_HEIGHT 20                 //label height
#define SPACING_W    2                  //边界宽度
#define TOP_SPACING_H (SafeAreaTopHeight+44)   //距顶端边界
#define TABLE_VIEW_HEAD_H_IPHONE       50      //iphone表头高度
#define TABLE_VIEW_CELL_H_IPHONE        42     //iphone列表高度
#define TABLE_VIEW_HEAD_H_IPAD      70          //ipad表头高
#define TABLE_VIEW_CELL_H_IPAD      60          //ipad子表高
#define BORDER_WIDTH    15               //border 宽度

//判断iPhoneX
#define IS_IPHONE_X (SCREEN_W >= 375.0f && SCREEN_H >= 812.0f)
#define SCREEN_W  [UIScreen mainScreen].bounds.size.width
#define SCREEN_H  [UIScreen mainScreen].bounds.size.height
#define SafeAreaTopHeight  [UIApplication sharedApplication].statusBarFrame.size.height
#define SafeAreaBottomHeight (IS_IPHONE_X? 34 : 0)
#define SCREEN_NavH  ([UIScreen mainScreen].bounds.size.height - SafeAreaBottomHeight)

#define SYSTEMFONT(FONTSIZE)    [UIFont systemFontOfSize:FONTSIZE]

#define CORNER_RADIUS_SIZE 8            //圆角大小       
#define UUGrey         [UIColor colorWithRed:246.0/255.0 green:246.0/255.0 blue:246.0/255.0 alpha:1.0f]
#define UULightBlue    [UIColor colorWithRed:94.0/255.0 green:147.0/255.0 blue:196.0/255.0 alpha:1.0f]
#define UUGreen        [UIColor colorWithRed:77.0/255.0 green:186.0/255.0 blue:122.0/255.0 alpha:1.0f]
#define UUTitleColor   [UIColor colorWithRed:0.0/255.0 green:189.0/255.0 blue:113.0/255.0 alpha:1.0f]
#define UUButtonGrey   [UIColor colorWithRed:141.0/255.0 green:141.0/255.0 blue:141.0/255.0 alpha:1.0f]
#define UUFreshGreen   [UIColor colorWithRed:77.0/255.0 green:196.0/255.0 blue:122.0/255.0 alpha:1.0f]
#define UURed          [UIColor colorWithRed:245.0/255.0 green:94.0/255.0 blue:78.0/255.0 alpha:0.7f]
#define UUMauve        [UIColor colorWithRed:88.0/255.0 green:75.0/255.0 blue:103.0/255.0 alpha:1.0f]
#define UUBrown        [UIColor colorWithRed:119.0/255.0 green:107.0/255.0 blue:95.0/255.0 alpha:1.0f]
#define UUBlue         [UIColor colorWithRed:82.0/255.0 green:116.0/255.0 blue:188.0/255.0 alpha:1.0f]
#define UUDarkBlue     [UIColor colorWithRed:121.0/255.0 green:134.0/255.0 blue:142.0/255.0 alpha:1.0f]
#define UUYellow       [UIColor colorWithRed:242.0/255.0 green:197.0/255.0 blue:117.0/255.0 alpha:1.0f]
#define UUWhite        [UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:255.0/255.0 alpha:1.0f]
#define UUDeepGrey     [UIColor colorWithRed:99.0/255.0 green:99.0/255.0 blue:99.0/255.0 alpha:1.0f]
#define UUPinkGrey     [UIColor colorWithRed:200.0/255.0 green:193.0/255.0 blue:193.0/255.0 alpha:1.0f]
#define UUHealYellow   [UIColor colorWithRed:245.0/255.0 green:242.0/255.0 blue:238.0/255.0 alpha:1.0f]
#define UULightGrey    [UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0f]
#define UUCleanGrey    [UIColor colorWithRed:251.0/255.0 green:251.0/255.0 blue:251.0/255.0 alpha:1.0f]
#define UULightYellow  [UIColor colorWithRed:241.0/255.0 green:240.0/255.0 blue:240.0/255.0 alpha:1.0f]
#define UUDarkYellow   [UIColor colorWithRed:152.0/255.0 green:150.0/255.0 blue:159.0/255.0 alpha:1.0f]
#define UUPinkDark     [UIColor colorWithRed:170.0/255.0 green:165.0/255.0 blue:165.0/255.0 alpha:1.0f]
#define UUCloudWhite   [UIColor colorWithRed:244.0/255.0 green:244.0/255.0 blue:244.0/255.0 alpha:1.0f]
#define UUBlack        [UIColor colorWithRed:45.0/255.0 green:45.0/255.0 blue:45.0/255.0 alpha:1.0f]
#define UUStarYellow   [UIColor colorWithRed:252.0/255.0 green:223.0/255.0 blue:101.0/255.0 alpha:1.0f]
#define UUTwitterColor [UIColor colorWithRed:0.0/255.0 green:171.0/255.0 blue:243.0/255.0 alpha:1.0]
#define UUWeiboColor   [UIColor colorWithRed:250.0/255.0 green:0.0/255.0 blue:33.0/255.0 alpha:1.0]
#define UUiOSGreenColor [UIColor colorWithRed:98.0/255.0 green:247.0/255.0 blue:77.0/255.0 alpha:1.0]
#define UUDeepGreen     [UIColor colorWithRed:50.0/255.0 green:114.0/255.0 blue:11.0/255.0 alpha:1.0]
#define UUDeepBlue      [UIColor colorWithRed:41.0/255.0 green:143.0/255.0 blue:194.0/255.0 alpha:1.0]
#define UUOrange        [UIColor colorWithRed:240.0/255.0 green:179.0/255.0 blue:35.0/255.0 alpha:1.0]
#define UUClear         [UIColor colorWithRed:0.0/255.0 green:0.0/255.0 blue:0.0/255.0 alpha:0.0]
//前景色
#define UUFrontColor    [UIColor colorWithRed:69.0/255.0 green:205.0/255.0 blue:141.0/255.0 alpha:1.0]
#define UUCEllFrontColor    [UIColor colorWithRed:38.0/255.0 green:37.0/255.0 blue:50.0/255.0 alpha:1.0]

//背景色
//#define UUBackGroundColor    [UIColor colorWithRed:37.0/255.0 green:48.0/255.0 blue:71.0/255.0 alpha:1.0]
#define UUBackGroundColor [UIColor colorWithRed:38.0/255.0 green:37.0/255.0 blue:50.0/255.0 alpha:1.0]
//Time参数标题背景色
#define UUTimeLabelColor    [UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:204.0/255.0 alpha:1.0]
//spo2参数标题背景色
#define UUSpo2LabelColor    [UIColor colorWithRed:69.0/255.0 green:189.0/255.0 blue:177.0/255.0 alpha:1.0]
//resp标题背景色
#define UURespLabelColor    [UIColor colorWithRed:65.0/255.0 green:124.0/255.0 blue:147.0/255.0 alpha:1.0]
//pr标题背景色
#define UUPrLabelColor    [UIColor colorWithRed:233.0/255.0 green:46.0/255.0 blue:53.0/255.0 alpha:1.0]
//导航栏背景色
#define UUNavBackColor    [UIColor colorWithRed:55.0/255.0 green:55.0/255.0 blue:71.0/255.0 alpha:1.0]
//注型图背景色
#define UUBloodBarBackColor [UIColor colorWithRed:62.0/255.0 green:104.0/255.0 blue:116.0/255.0 alpha:1.0]
//柱形图前景色
#define UUBloodBarFrontColor [UIColor colorWithRed:73.0/255.0 green:167.0/255.0 blue:148.0/255.0 alpha:1.0]
// 设置按钮滑条颜色
#define UUSetupTintColor [UIColor colorWithRed:104/255.0 green:219/255.0 blue:176/255.0 alpha:1.0]

// 设置按钮滑条背景颜色
#define UUSetupTintBackColor [UIColor colorWithRed:55/255.0 green:55/255.0 blue:71/255.0 alpha:1.0]

#define UURandomColor   [UIColor colorWithRed:arc4random()%255/255.0 green:arc4random()%255/255.0 blue:arc4random()%255/255.0 alpha:1.0f]


#endif /* consDefine_h */


