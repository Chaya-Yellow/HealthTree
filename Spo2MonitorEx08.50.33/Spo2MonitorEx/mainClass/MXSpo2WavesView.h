//
//  MXSpo2WavesView.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/21.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MXSpo2WavesView : UIView
-(void)drawWaves:(NSArray*)waves;
-(void)canDrawBaseLine:(bool)bDraw;
//-(void)reDrawSpo2Waves;
@end
