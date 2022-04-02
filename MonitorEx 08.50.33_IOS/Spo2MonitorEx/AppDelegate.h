//
//  AppDelegate.h
//  Spo2MonitorEx
//
//  Created by luteng on 2018/12/15.
//  Copyright © 2018年 kim. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

extern AppDelegate *app;

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong) NSPersistentContainer *persistentContainer;

- (void)saveContext;

- (void)gotoRootVC;


@end

