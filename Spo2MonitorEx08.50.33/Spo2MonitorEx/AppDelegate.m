//
//  AppDelegate.m
//  Spo2MonitorEx
//
//  Created by luteng on 2018/12/15.
//  Copyright © 2018年 kim. All rights reserved.
//

#import "AppDelegate.h"
#import "GuideViewController.h"
#import "landViewController.h"

@interface AppDelegate ()<UINavigationBarDelegate,UINavigationControllerDelegate>
@property (strong, nonatomic) UINavigationController *launchNavigationViewController;
@property (strong, nonatomic) landViewController     *launchController;
//@property (strong, nonatomic) ViewController     *viewController;

@end

AppDelegate *app = nil;

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    //  设置全局变量
    app = self;
    // 创建app窗口及相关属性（窗口大小，背景颜色）
    self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
    [self.window makeKeyAndVisible];
    [self.window setBackgroundColor:[UIColor whiteColor]];
    // 设置程序入口
    //    BLTTabBarController *rootController = [[BLTTabBarController alloc] init];
    //    [self.window setRootViewController:rootController];
    
    NSNumber *guideNum = [[NSUserDefaults standardUserDefaults] objectForKey:@"isFinIshGuideScroll"];
       
    if (guideNum.boolValue) {
       
        [self gotoRootVC];
        
    }else {
        
        [self gotoGuideVC];
        
    }
    
    return YES;
}

-(landViewController*)launchController
{
    if(!_launchController)
    {
        _launchController = [[landViewController alloc]init];
    }
    return _launchController;
}

#pragma mark - 跳转 -

- (void)gotoGuideVC {
    
    GuideViewController *vc = [[GuideViewController alloc] init];
    self.window.rootViewController = vc;
}

- (void)gotoRootVC {
    
    [self.window setRootViewController:self.launchNavigationViewController];
    
}

#pragma mark - 懒加载 -

//- (ViewController *)viewController {
//
//    if (!_viewController) {
//
//        _viewController = [[ViewController alloc] init];
//    }
//
//    return _viewController;
//
//}


- (UINavigationController *)launchNavigationViewController
{
    if (!_launchNavigationViewController) {
        
        _launchNavigationViewController = [[UINavigationController alloc] initWithRootViewController:self.launchController];
//        [_launchNavigationViewController.navigationBar setBarTintColor:UUWeiboColor];
        [_launchNavigationViewController.navigationBar setTintColor:[UIColor whiteColor]];
        //        [_launchNavigationViewController setNavigationBarHidden:YES animated:YES];
        _launchNavigationViewController.delegate = self;
    }
    return _launchNavigationViewController;
}

#pragma mark -  UINavigationControllerDelegate
- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated {
    //登陆界面隐藏 NavigationBar 其他界面显示
    if ( viewController == self.launchController) {
        //        [navigationController setNavigationBarHidden:YES animated:animated];
    } else if ( [navigationController isNavigationBarHidden] ) {
        //        [navigationController setNavigationBarHidden:NO animated:animated];
    }
}


- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    UIDevice *device = [UIDevice currentDevice];
    BOOL backgroundSupported = NO;
    if ([device respondsToSelector:@selector(isMultitaskingSupported)]) {
        backgroundSupported = [device isMultitaskingSupported];
    }
    if (backgroundSupported) {
        __block UIBackgroundTaskIdentifier bgTaskId = [application beginBackgroundTaskWithExpirationHandler:^{
            [application endBackgroundTask:bgTaskId];
            bgTaskId = UIBackgroundTaskInvalid;
        }];
        //        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        //            //
        //        });
    }
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}


- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    // Saves changes in the application's managed object context before the application terminates.
    [self saveContext];
}


#pragma mark - Core Data stack

@synthesize persistentContainer = _persistentContainer;

- (NSPersistentContainer *)persistentContainer {
    // The persistent container for the application. This implementation creates and returns a container, having loaded the store for the application to it.
    @synchronized (self) {
        if (_persistentContainer == nil) {
            _persistentContainer = [[NSPersistentContainer alloc] initWithName:@"Spo2MonitorEx"];
            [_persistentContainer loadPersistentStoresWithCompletionHandler:^(NSPersistentStoreDescription *storeDescription, NSError *error) {
                if (error != nil) {
                    // Replace this implementation with code to handle the error appropriately.
                    // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                    
                    /*
                     Typical reasons for an error here include:
                     * The parent directory does not exist, cannot be created, or disallows writing.
                     * The persistent store is not accessible, due to permissions or data protection when the device is locked.
                     * The device is out of space.
                     * The store could not be migrated to the current model version.
                     Check the error message to determine what the actual problem was.
                    */
                    NSLog(@"Unresolved error %@, %@", error, error.userInfo);
                    abort();
                }
            }];
        }
    }
    
    return _persistentContainer;
}

#pragma mark - Core Data Saving support

- (void)saveContext {
    NSManagedObjectContext *context = self.persistentContainer.viewContext;
    NSError *error = nil;
    if ([context hasChanges] && ![context save:&error]) {
        // Replace this implementation with code to handle the error appropriately.
        // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
        NSLog(@"Unresolved error %@, %@", error, error.userInfo);
        abort();
    }
}

@end
