//
//  OMESyncManager.h
//  OMEDataServiceTest
//
//  Created by omesoft on 14-10-29.
//  Copyright (c) 2014年 Xin. All rights reserved.
//

#import <Foundation/Foundation.h>

#define isSyncFamily 1


static NSString * const syncDidUpdatedNotification = @"SyncDidUpdatedNotification";

@interface OMESyncManager : NSObject

+(id)sharedInstance;
- (void)startSync;
- (void)stopSync;
- (void)startSyncDelay;
@end
