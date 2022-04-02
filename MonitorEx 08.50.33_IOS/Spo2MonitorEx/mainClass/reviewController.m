//
//  reviewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/27.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "reviewController.h"
#import "ReviewTableViewController.h"
#import "ReviewListViewController.h"
#import "consDefine.h"
#import "MXUserData.h"
#import "MXUserService.h"
#import "ZXCalendarView.h"
#import "MXUser.h"
#import "Constants.h"
#import "NSDate_Omesoft.h"
#import "BLTSpo2ChartViewController.h"
#import "OMESyncManager.h"
#import "xlsxwriter.h"
#import <MessageUI/MFMailComposeViewController.h>
#import "MBProgressHUD.h"
#import "ReviewSharePDFView.h"

@interface reviewController()<ZXCalendarViewDataSource,ZXCalendarViewDelegate, MFMailComposeViewControllerDelegate, UIDocumentInteractionControllerDelegate>
@property(nonatomic, strong) UIScrollView  *scrollView;      //声明一个UIScrollView
@property(nonatomic, strong) UIPageControl *pageControl;     //声明一个UIPageControl
@property(nonatomic, strong) NSMutableArray *viewControllers; //存放UIViewController的可变数组
@property(nonatomic, strong) MXUserData *userData;
@property(nonatomic, strong) NSDate *selectedDate;
@property(nonatomic, assign) NSInteger selectedFamilyId;
@property(nonatomic, strong) NSOperationQueue *operationQueue;
@property(nonatomic, strong) BLTSpo2ChartViewController* trendChart;
@property(nonatomic, strong) ReviewListViewController*  trendList;
@property(nonatomic, strong) ZXCalendarView *calendarView;
@property(nonatomic, strong) MBProgressHUD *myHud;
@property(nonatomic, strong) UIDocumentInteractionController *document;

@end
static CGFloat CALENDER_VIEW_HEIGHT = 100.f;
@implementation reviewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.operationQueue = [[NSOperationQueue alloc] init];
     self.selectedDate = [[[NSDate date] UTCDate] startOfDay];
    self.view.backgroundColor = UUBackGroundColor;
    NSUInteger numberPages =2;//= self.contentList.count;
    
    self.title = NSLocalizedString(@"Historical_Record",@"");
    self.navigationController.interactivePopGestureRecognizer.enabled = NO;
   [self.navigationController.navigationBar setBarTintColor:UUNavBackColor];
   [self.navigationController.navigationBar setTintColor:UUWhite];
   [self.navigationController.navigationBar setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    
    // view controllers are created lazily
    // in the meantime, load the array with placeholders which will be replaced on demand
    NSMutableArray *controllers = [[NSMutableArray alloc] init];
    for (NSUInteger i = 0; i < numberPages; i++)
    {
        [controllers addObject:[NSNull null]];
    }
    self.viewControllers = controllers;
    
    // a page is the width of the scroll view
    _scrollView = [[UIScrollView alloc]init];
    self.scrollView.frame = CGRectMake(0, self.calendarView.frame.origin.y + CALENDER_VIEW_HEIGHT, self.view.frame.size.width, self.view.frame.size.height - CALENDER_VIEW_HEIGHT - 90 - SafeAreaBottomHeight);
    self.scrollView.pagingEnabled = YES;
    self.scrollView.contentSize = CGSizeMake(CGRectGetWidth(self.scrollView.frame) * numberPages, 0);
    self.scrollView.showsHorizontalScrollIndicator = NO;
    self.scrollView.showsVerticalScrollIndicator = NO;
    self.scrollView.scrollsToTop = NO;
    self.scrollView.delegate = self;
    self.scrollView.backgroundColor = [UIColor clearColor];
    
    _pageControl = [[UIPageControl alloc]init];
    _pageControl.frame= CGRectMake(0, _scrollView.frame.origin.y+_scrollView.frame.size.height, self.view.frame.size.width, 20);
    _pageControl.numberOfPages = numberPages;
    _pageControl.currentPage = 0;
    _pageControl.backgroundColor = UUBackGroundColor;
    _pageControl.pageIndicatorTintColor = [UIColor grayColor];
    _pageControl.currentPageIndicatorTintColor = [UIColor whiteColor];
    [self.view addSubview:_scrollView];
    [self.view addSubview:_pageControl];
    [self.view addSubview:self.calendarView];
    [self reloadChartView];
    //
    // pages are created on demand
    // load the visible page
    // load the page on either side to avoid flashes when the user starts scrolling
    //
    [self loadScrollViewWithPage:0];
    [self loadScrollViewWithPage:1];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateRecordData) name:newTempDidUpdatedNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(significantTimeChangeNotif) name:UIApplicationSignificantTimeChangeNotification object:nil];
   [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateRecordData) name:syncDidUpdatedNotification object:nil];
    //
    //数据分享
    UIButton* btn = [[UIButton alloc]initWithFrame:CGRectMake(self.navigationController.navigationBar.frame.size.width - 100, 0, 100, self.navigationController.navigationBar.frame.size.height)];
//    UIImageView* imageView = [[UIImageView alloc]initWithFrame:CGRectMake(5, 5, 30, 30)];
//    imageView.image = [UIImage imageNamed:@"share.png"];
//    [btn addSubview:imageView];
    [btn setTitle:NSLocalizedString(@"Review_Export",@"") forState:UIControlStateNormal];
    btn.titleLabel.shadowOffset = CGSizeMake(0, -1);
    btn.contentHorizontalAlignment = UIControlContentHorizontalAlignmentRight;
    [btn addTarget: self action: @selector(exportHistoryData) forControlEvents: UIControlEventTouchUpInside];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:btn];
}

-(void)exportHistoryData
{
    
    UIAlertController *alertControl = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alertControl addAction:([UIAlertAction actionWithTitle:NSLocalizedString(@"Sys_Cancel",@"") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }])];
    
    DefineWeakSelf;
    [alertControl addAction:([UIAlertAction actionWithTitle:NSLocalizedString(@"Other_applications",@"") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
      
        [weakSelf sureExportDataUserInfoWithShareNum:0];

   }])];
    
    [alertControl addAction:([UIAlertAction actionWithTitle:NSLocalizedString(@"Open_Email", @"") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
      
        if ([MFMailComposeViewController canSendMail]) {
            
            [weakSelf sureExportDataUserInfoWithShareNum:1];
            
        }else {
            
            [weakSelf dismissViewControllerAnimated:YES completion:nil];
            weakSelf.myHud.mode = MBProgressHUDModeText;
            weakSelf.myHud.labelText = NSLocalizedString(@"Send_Email_Failed", @"iPhone邮箱未配置");
            [weakSelf.myHud hide:YES afterDelay:2];
        }

   }])];
    
   [self presentViewController:alertControl animated:YES completion:nil];
    
}

-(void)updateRecordData
{
     [self reloadChartView];
}

/** 导出事件处理 */
- (void)sureExportDataUserInfoWithShareNum:(NSInteger)shareNum {
    
    if (self.pageControl.currentPage == 0) {
        
        [self sureExportPDFDataUserInfoWithShareNum:shareNum];
        
    }else {
        
        [self sureExportListDataWithShareNum:shareNum];
    }
    
}

/** 列表 */
- (void)sureExportListDataWithShareNum:(NSInteger)shareNum {
    
    NSArray *documents = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *docementDir = [documents objectAtIndex:0];
    NSString *fileName = @"exportSpo2Data.xlsx";
    NSString *filePath = [docementDir stringByAppendingPathComponent:fileName];
    //    NSLog(@"filePath = %@", filePath);
    if([self createFile:filePath])
    {
        if([self exportCSV:filePath])
        {
         
            if (shareNum==0) {
                
                [self openUseOtherAppPath:filePath];
                
            }else {
                
                NSData* pData = [[NSData alloc]initWithContentsOfFile:filePath];
                [self exportDataByEmail:fileName andData:pData];

            }
            
        }
    }
    
}

/** 折线图 */
-(void)sureExportPDFDataUserInfoWithShareNum:(NSInteger)shareNum
{
 
    CGRect tempFrame = self.scrollView.frame;
    CGFloat h = 180+tempFrame.size.height;
    ReviewSharePDFView *pdfView = [[ReviewSharePDFView alloc] initWithSpo2yVals:self.trendChart.Spo2DataVals andPRyValues:self.trendChart.PRDataValues];
    pdfView.frame = CGRectMake(0, 0, SCREEN_W, h>SCREEN_H?h:SCREEN_H);

    //开启一个图片的上下文
    UIGraphicsBeginImageContextWithOptions(self.scrollView.bounds.size, NO, 0);
    //拿到我们开启的上下文
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    //把需要截屏View的layer渲染到上下文中
    [self.scrollView.layer renderInContext:ctx];
    //从上下文中拿出图片
    UIImage *img = UIGraphicsGetImageFromCurrentImageContext();
    //因为上下文是我们自己开启的，所以用完之后要关闭掉
    UIGraphicsEndImageContext();
    
    UIImageView *imgView = [[UIImageView alloc] initWithImage:img];
    imgView.frame = CGRectMake(0, 180, SCREEN_W, tempFrame.size.height);
    [pdfView addSubview:imgView];

    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"YYYY/MM/dd HH:mm"];
    pdfView.timeLb.text = [dateFormatter stringFromDate:self.selectedDate];
    
    NSMutableData *pdfData = [NSMutableData data];

    UIGraphicsBeginPDFContextToData(pdfData, pdfView.bounds, nil);

    UIGraphicsBeginPDFPage();

    CGContextRef pdfContext = UIGraphicsGetCurrentContext();

    [pdfView.layer renderInContext:pdfContext];

    UIGraphicsEndPDFContext();


    NSTimeInterval timeInterval = [[NSDate date] timeIntervalSince1970];
    NSString *name = [NSString stringWithFormat:@"%@",@(timeInterval)];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths lastObject];
    NSString *fileName = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"%@.pdf",name]];
    NSLog(@"fileName:%@",fileName);
    if (shareNum==0) {
        
        if ([self createFile:fileName]) {
    //        [SVProgressHUD showMessage:@"生成PDF文件成功"];
            NSLog(@"生成PDF文件成功");
            [pdfData writeToFile:fileName atomically:YES];
            [self openUseOtherAppPath:fileName];
    
        }else{
    //        [SVProgressHUD showMessage:@"生成PDF文件失败"];
             NSLog(@"生成PDF文件失败");
            self.myHud.mode = MBProgressHUDModeText;
            self.myHud.labelText = NSLocalizedString(@"Send_PDF_Failed", @"生成PDF文件失败");
            [self.myHud hide:YES afterDelay:2];
    
        }
        
    }else {
        
        [self exportDataByEmail:fileName andData:pdfData];

    }


       
    //开启一个图片的上下文
//    UIGraphicsBeginImageContextWithOptions(self.view.bounds.size, NO, 0);
//    //拿到我们开启的上下文
//    CGContextRef ctx = UIGraphicsGetCurrentContext();
//    //把需要截屏View的layer渲染到上下文中
//    [self.view.layer renderInContext:ctx];
//    //从上下文中拿出图片
//    UIImage *img = UIGraphicsGetImageFromCurrentImageContext();
//    //因为上下文是我们自己开启的，所以用完之后要关闭掉
//    UIGraphicsEndImageContext();
//

//
//    [self setupPDFDocumentNamed:name width:self.view.frame.size.width height:self.view.frame.size.height];
//    [self addImage:img atPoint:CGPointMake(img.size.width/2, img.size.height/2)];
//
//    NSFileManager *manager = [NSFileManager defaultManager];
//    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
//    NSString *path = [paths lastObject];
//    NSString * path1 = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"%@.pdf",name]];
//    if ([manager fileExistsAtPath:path1]) {
////        [SVProgressHUD showMessage:@"生成PDF文件成功"];
//        NSLog(@"生成PDF文件成功");
//
//
//    }else{
////        [SVProgressHUD showMessage:@"生成PDF文件失败"];
//         NSLog(@"生成PDF文件失败");
//
//    }

//
//        //为了查看截屏效果，我把图片写到了电脑上
//        //生成jpg格式的图片
//        NSData *imgData = UIImageJPEGRepresentation(img, 1);
//        //如果想生成png格式的图片调用下面的方法
//        //NSData *imgData = UIImagePNGRepresentation(img);
//        [imgData writeToFile:@"/Users/olddevil/Desktop/图片/img.jpg" atomically:YES];
        
}

- (void)setupPDFDocumentNamed:(NSString*)name width:(float)width height:(float)height {
    //设置存储的PDFframe，默认我传的屏幕宽高，根据你的情况改变
    CGSize _pageSize = CGSizeMake(width, height);
    //根据传进来的文件名字  创建文件夹，在内部存储PDF文件
    NSString *newPDFName = [NSString stringWithFormat:@"%@.pdf",name];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *pdfPath = [documentsDirectory stringByAppendingPathComponent:newPDFName];
    //开始绘制PDF
    UIGraphicsBeginPDFContextToFile(pdfPath, CGRectZero, nil);
    UIGraphicsBeginPDFPageWithInfo(CGRectMake(0, 0, _pageSize.width, _pageSize.height), nil);
}
 
- (void)addImage:(UIImage*)image atPoint:(CGPoint)point {
    //向PDF内添加图片
    CGRect imageFrame = CGRectMake(point.x, point.y, image.size.width, image.size.height);
    [image drawInRect:imageFrame];
    UIGraphicsEndPDFContext();
}

#pragma exportDeleagte End....

/** 其他应用打开 */
- (void)openUseOtherAppPath:(NSString *)filePath {

    NSURL *path = [NSURL fileURLWithPath:filePath];

    self.document =[UIDocumentInteractionController interactionControllerWithURL:path];
//    self.document.UTI = [self UTIForURL:path];
    self.document.delegate = self;
    
    if (![self.document presentOpenInMenuFromRect:CGRectZero inView:self.view animated:YES]) {
        
        self.myHud.mode = MBProgressHUDModeText;
        self.myHud.labelText = NSLocalizedString(@"No_apps_top_open", @"没有应用可以打开");
        [self.myHud hide:YES afterDelay:2];    }
    
}

-(void)exportDataByEmail:(NSString *)fileName andData:(NSData *)data
{
    if ([fileName isEqualToString:@""])
        return;
    
    //    NSLog(@"FileName:%@", fileName);
    
    MFMailComposeViewController *mailCompose = [[MFMailComposeViewController alloc] init];
    if(mailCompose)
    {
        //设置代理
        [mailCompose setMailComposeDelegate:self];
        NSString *emailBody = NSLocalizedString(@"Historical_Data", @"Historical_Data");
        //设置收件人
        //        [mailCompose setToRecipients:toAddress];
        //设置抄送人
        //        [mailCompose setCcRecipients:ccAddress];
        //设置邮件内容
        [mailCompose setMessageBody:emailBody isHTML:YES];
        
        //设置邮件主题
        [mailCompose setSubject:NSLocalizedString(@"OXIMETER", @"OXIMETER")];
        //设置邮件附件{mimeType:文件格式|fileName:文件名}
        if (self.pageControl.currentPage==0) {
            
            [mailCompose addAttachmentData:data mimeType:@"application" fileName:fileName];

        }else {
            
            [mailCompose addAttachmentData:data mimeType:@"xlsx" fileName:@"DataRecord.xlsx"];

        }
        //设置邮件视图在当前视图上显示方式
        [self presentViewController:mailCompose animated:YES completion:nil];
    }
}

- (BOOL)createFile:(NSString *)fileName {
    
    NSFileManager *fileManager = [NSFileManager defaultManager];
    [fileManager removeItemAtPath:fileName error:nil];
    if (![fileManager createFileAtPath:fileName contents:nil attributes:nil]) {
        NSLog(@"不能创建文件");
        return false;
    }
    return true;
}

- (BOOL)exportCSV:(NSString *)fileName
{
    lxw_workbook *workbook  = workbook_new([fileName UTF8String]);// 创建新xlsx文件，路径需要转成c字符串
    lxw_worksheet *sheet = workbook_add_worksheet(workbook, NULL);// 创建sheet
    [self writeHeadInfo:sheet withWorkbook:workbook];
    //
    NSMutableArray *chartData = [self.userData getRecordTempDataWithStyle:MXTemperatureChartStyleDay familyId:self.selectedFamilyId targetDate:self.selectedDate];
    [self writeSpo2Data:sheet withWorkBook:workbook withData:chartData];
    //
    workbook_close(workbook);
    return true;
}

-(bool) writeSpo2Data:(lxw_worksheet*)sheet withWorkBook:(lxw_workbook*)workbook withData:(NSMutableArray*)dataArray
{
    if (!sheet || !workbook || !dataArray)
        return false;
    
    if (dataArray.count == 0)
        return false;
    
    lxw_format *normalFormat = workbook_add_format(workbook);
    format_set_font_size(normalFormat, 12);
    format_set_bold(normalFormat);
    format_set_align(normalFormat, LXW_ALIGN_CENTER);//水平居中
    format_set_align(normalFormat, LXW_ALIGN_VERTICAL_CENTER);//垂直居中
    
    lxw_format *dataFormat = workbook_add_format(workbook);
    format_set_font_size(dataFormat, 12);
    format_set_bold(dataFormat);
    format_set_align(dataFormat, LXW_ALIGN_CENTER);//水平居中
    format_set_align(dataFormat, LXW_ALIGN_VERTICAL_CENTER);//垂直居中
    NSLog(@"histpry Data count:%ld",dataArray.count);
    for (int i = 0; i < dataArray.count; i++)
    {
        NSDictionary *dic = dataArray[dataArray.count - 1 - i];
//        NSLog(@"%@", dic);
        NSInteger Spo2Value =  [[dic objectForKey:@"SPO2"]integerValue];
        NSInteger PRValue =   [[dic objectForKey:@"PR"] integerValue];
        worksheet_write_string(sheet, i + 1, 0, [[NSString stringWithFormat:@"%d",i + 1] UTF8String], normalFormat);
        worksheet_write_string(sheet, i + 1, 1, [[NSString stringWithFormat:@"%@",[dic objectForKey:@"Date"]] UTF8String], dataFormat);
        if (Spo2Value > 0 && Spo2Value <= 100 && PRValue > 0 && PRValue < 255)
        {
            worksheet_write_string(sheet, i + 1, 2, [[NSString stringWithFormat:@"%ld",Spo2Value] UTF8String], dataFormat);
            worksheet_write_string(sheet, i + 1, 3, [[NSString stringWithFormat:@"%ld",PRValue] UTF8String], dataFormat);
        }
        else
        {
            worksheet_write_string(sheet, i + 1, 2, [[NSString stringWithFormat:@"%@",@"--"] UTF8String], dataFormat);
            worksheet_write_string(sheet, i + 1, 3, [[NSString stringWithFormat:@"%@",@"--"] UTF8String], dataFormat);
        }
    }
    return true;
}

-(BOOL)writeHeadInfo:(lxw_worksheet*)sheet withWorkbook:(lxw_workbook*)workbook
{
    if (!sheet || !workbook)
        return false;
    
    NSArray *headArray = [NSArray arrayWithObjects:@"Id", @"Time", @"Spo2(%)", @"PR(bpm)",nil];
    lxw_format *headerFormat = workbook_add_format(workbook);
    format_set_font_size(headerFormat, 12);
    format_set_bold(headerFormat);
    format_set_align(headerFormat, LXW_ALIGN_CENTER);//水平居中
    format_set_align(headerFormat, LXW_ALIGN_VERTICAL_CENTER);//垂直居中
    format_set_bg_color(headerFormat, 0xA9A9A9);
    worksheet_set_column(sheet, 0, headArray.count, 30, NULL);// 宽度
    for (int i = 0; i < headArray.count; i++)
    {
        worksheet_write_string(sheet, 0, i, [[headArray objectAtIndex:i] UTF8String], headerFormat);
    }
    return  true;
}

- (void)newTempDidUpdated
{
    //有新的体温数据。只修改标识，每5分钟更新一次。且只在当前时间才更新
    //    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    //    if (app.tabBarController && app.tabBarController.selectedIndex != 1) {
}

- (void)significantTimeChangeNotif
{
    [self performSelector:@selector(timeDidChagned) withObject:nil afterDelay:1.0];
    
}

- (void)timeDidChagned
{
    self.selectedDate = [[[NSDate date] UTCDate] startOfDay];
    [self.calendarView setSelectedDate:self.selectedDate animated:YES];
    [self.calendarView reloadCalendar];
    [self reloadChartView];
}


- (void)loadScrollViewWithPage:(NSUInteger)page
{
    if (page >= 2)//self.contentList.count)
        return;
    if(page ==1)
    {
        // replace the placeholder if necessary
        ReviewListViewController *controller = [self.viewControllers objectAtIndex:page];
        if ((NSNull *)controller == [NSNull null])
        {
            controller = self.trendList;
            [self.viewControllers replaceObjectAtIndex:page withObject:controller];
        }
        // add the controller's view to the scroll view
        if (controller.view.superview == nil)
        {
            CGRect frame = self.scrollView.frame;
            frame.origin.x = CGRectGetWidth(frame) * page;
            frame.origin.y = 0;
            controller.view.frame = frame;
            [self addChildViewController:controller];
            [self.scrollView addSubview:controller.view];
            [controller didMoveToParentViewController:self];
        }
    }
    else if(page == 0)
    {
        // replace the placeholder if necessary
        BLTSpo2ChartViewController *controller = [self.viewControllers objectAtIndex:page];
        if ((NSNull *)controller == [NSNull null])
        {
            controller = self.trendChart;
            [self.viewControllers replaceObjectAtIndex:page withObject:controller];
        }
        // add the controller's view to the scroll view
        if (controller.view.superview == nil)
        {
            CGRect frame = self.scrollView.frame;
            frame.origin.x = CGRectGetWidth(frame) * page;
            frame.origin.y = 0;
            controller.view.frame = frame;
            [self addChildViewController:controller];
            [self.scrollView addSubview:controller.view];
            [controller didMoveToParentViewController:self];
        }
    }
}

// at the end of scroll animation, reset the boolean used when scrolls originate from the UIPageControl
- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    // switch the indicator when more than 50% of the previous/next page is visible
    CGFloat pageWidth = CGRectGetWidth(self.scrollView.frame);
    NSUInteger page = floor((self.scrollView.contentOffset.x - pageWidth / 2) / pageWidth) + 1;
    self.pageControl.currentPage = page;
    
    // load the visible page and the page on either side of it (to avoid flashes when the user starts scrolling)
    [self loadScrollViewWithPage:page - 1];
    [self loadScrollViewWithPage:page];
    [self loadScrollViewWithPage:page + 1];
    
    // a possible optimization would be to unload the views+controllers which are no longer visible
}

-(ReviewListViewController*)trendList
{
    if(!_trendList)
    {
        _trendList = [[ReviewListViewController alloc]init];
    }
    return _trendList;
}

-(BLTSpo2ChartViewController*)trendChart
{
    if(!_trendChart)
    {
        _trendChart = [[BLTSpo2ChartViewController alloc]init];
    }
    return _trendChart;
}

- (IBAction)changePage:(id)sender
{
    //[self gotoPage:YES];    // YES = animate
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (MXUserData *)userData
{
    if (!_userData) {
        _userData = [[MXUserData alloc] init];
    }
    return _userData;
}

- (NSInteger)selectedFamilyId
{
    if (!_selectedFamilyId) {
        MXUser *user = [[MXUserService sharedInstance] getSelectedUser];
        if (user) {
            _selectedFamilyId = user.familyId;
        } else {
            _selectedFamilyId = 0;
        }
        
    }
    return _selectedFamilyId;
}


- (void)reloadChartView
{
    NSInvocation* getInvocation = [NSInvocation invocationWithMethodSignature:[self methodSignatureForSelector:@selector(getRecordTempData)]];
    [getInvocation setTarget:self];
    [getInvocation setSelector:@selector(getRecordTempData)];
    [self performGetRecordOperation:getInvocation];
}

- (void)getRecordTempData
{
    NSArray *chartData = [self.userData getRecordTempDataWithStyle:MXTemperatureChartStyleDay familyId:self.selectedFamilyId targetDate:self.selectedDate];
   // NSLog(@"--------------%ld pageIndex=%ld", chartData.count, self.pageControl.currentPage);
    [self performSelectorOnMainThread:@selector(updateChartView:) withObject:chartData waitUntilDone:YES];
}

-(void)updateChartView:(NSArray*)data
{
    [self.trendList setTrendDataWithArray:data];
    [self.trendChart setSpo2TrendData:data];
}

- (void)performGetRecordOperation:(NSInvocation *)invocation {
    [_operationQueue cancelAllOperations];
    NSInvocationOperation *operation = [[NSInvocationOperation alloc] initWithInvocation:invocation];
    [_operationQueue addOperation:operation];
}


- (ZXCalendarView *)calendarView
{
    if (!_calendarView) {
        _calendarView = [[ZXCalendarView alloc] initWithFrame:CGRectMake(0, SafeAreaTopHeight+44, self.view.frame.size.width, CALENDER_VIEW_HEIGHT)];
        _calendarView.delegate = self;
        _calendarView.dataSource = self;
        _calendarView.backgroundColor =  [UIColor colorWithRed:104/255.0 green:219/255.0 blue:176/255.0 alpha:1.0];
        
    }
    return _calendarView;
}

#pragma mark - UIDocumentInteractionControllerDelegate -
//- (UIViewController*)documentInteractionControllerViewControllerForPreview:(UIDocumentInteractionController*)controller{
//
//    return self;
//
//}
//
//- (UIView*)documentInteractionControllerViewForPreview:(UIDocumentInteractionController*)controller {
//
//    return self.view;
//
//}
//
//- (CGRect)documentInteractionControllerRectForPreview:(UIDocumentInteractionController*)controller {
//
//    return self.view.frame;
//
//}

//点击预览窗口的“Done”(完成)按钮时调用

- (void)documentInteractionControllerDidEndPreview:(UIDocumentInteractionController*)controller {

}

// 文件分享面板弹出的时候调用

- (void)documentInteractionControllerWillPresentOpenInMenu:(UIDocumentInteractionController*)controller{
    
    NSLog(@"WillPresentOpenInMenu");

}

// 当选择一个文件分享App的时候调用

- (void)documentInteractionController:(UIDocumentInteractionController*)controller willBeginSendingToApplication:(nullable NSString*)application{

    NSLog(@"begin send : %@", application);

}

// 弹框消失的时候走的方法

-(void)documentInteractionControllerDidDismissOpenInMenu:(UIDocumentInteractionController*)controller{

    NSLog(@"dissMiss");

}

#pragma mark - calendarDelegate
- (void)calendarView:(ZXCalendarView *)view didSelectedDate:(NSDate *)date
{
    //NSLog(@"----------------selectedDate = %@", date);
    self.selectedDate = date;
    //[self updateChartDateTitle];
    [self reloadChartView];
    //[self reloadEvents];
    //[self scrollToBestPositon];
}

- (void)calendarView:(ZXCalendarView *)view didChangedNewHeight:(float)height
{
    //NSLog(@"----------change height---------");
    _calendarView.frame = CGRectMake(0, SafeAreaTopHeight+44, _calendarView.frame.size.width, CALENDER_VIEW_HEIGHT);
}

- (NSArray *)calendarView:(ZXCalendarView *)view markFromDate:(NSDate *)startDate toDate:(NSDate *)endDate
{
    return [self.userData getMarksDateFromDate:startDate toDate:endDate family:self.selectedFamilyId];
    
}

- (void)mailComposeController:(MFMailComposeViewController *)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError *)error
{
    NSString *msg;
    switch (result)
    {
        case MFMailComposeResultCancelled:
            msg = NSLocalizedString(@"Cancel_sending_Email",@"邮件发送取消");
            break;
        case MFMailComposeResultSaved:
            msg = NSLocalizedString(@"Save_Email_Successfully",@"邮件保存成功");
            break;
        case MFMailComposeResultSent:
            msg = NSLocalizedString(@"Send_Email_Successfully",@"邮件发送成功");
            break;
        case MFMailComposeResultFailed:
            msg = NSLocalizedString(@"Send_Email_Failed", @"发送邮件失败");
            break;
        default:
            break;
    }
    NSLog(@"%@", msg);
    [self dismissViewControllerAnimated:YES completion:nil];
    self.myHud.mode = MBProgressHUDModeText;
    self.myHud.labelText = msg;
    [self.myHud hide:YES afterDelay:2];
}

-(MBProgressHUD*)myHud
{
    if (!_myHud)
    {
        _myHud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    }
    return _myHud;
}

@end
