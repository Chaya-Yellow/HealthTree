//
//  EHCardMessageTableViewCell.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "EHCardMessageTableViewCell.h"
#import "MXDeviceInfo.h"
#import "MXBTInfo.h"
#import "NSString+Omesoft.h"
@interface EHCardMessageTableViewCell()
@property (nonatomic, strong) UILabel *deviceLabel;
@property (nonatomic, strong) UILabel *departmentLabel;
@property (nonatomic, strong) UILabel *numberLabel;
@end

@implementation EHCardMessageTableViewCell
- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        UIImage *image = [UIImage imageNamed:@"img_blt_logo.png"];
        UIImageView *deviceImageView = [[UIImageView alloc] initWithFrame:CGRectMake(10, (44-image.size.height) / 2.0, image.size.width, image.size.height)];
        deviceImageView.image = image;
        [self addSubview:deviceImageView];
        //设备
        UIFont *deviceFont = [UIFont systemFontOfSize: 15.0f];
        self.deviceLabel = [[UILabel alloc]initWithFrame: CGRectMake(deviceImageView.frame.origin.x + deviceImageView.frame.size.width + 4, 8, 270, 16)];
        _deviceLabel.font = deviceFont;
        _deviceLabel.textColor = [UIColor blackColor];
        _deviceLabel.backgroundColor = [UIColor clearColor];
        [self addSubview: _deviceLabel];
        //类别
        self.departmentLabel = [[UILabel alloc]initWithFrame: CGRectMake(_deviceLabel.frame.size.width + 25, 12, 100, 14)];
        _departmentLabel.font = [UIFont systemFontOfSize: 12.0f];
        _departmentLabel.textColor = [UIColor blackColor];
        _departmentLabel.backgroundColor = [UIColor clearColor];
        [self addSubview:_departmentLabel];
        //编号
        UIFont *numberFont = [UIFont fontWithName: @"Helvetica-Light" size: 11.0f];
        //        NSString *numberStr = @"NO:1234567891234567";//[NSString stringWithFormat: @"NO:%@", [dic objectForKey: @"number"]];
        self.numberLabel = [[UILabel alloc]initWithFrame:CGRectMake(_deviceLabel.frame.origin.x, _departmentLabel.frame.size.height +12, 0, 0)];
        _numberLabel.font = numberFont;
        _numberLabel.textColor = [UIColor blackColor];
        _numberLabel.backgroundColor = [UIColor clearColor];
        [self addSubview:_numberLabel];
    }
    return self;
}

- (void)drawRect:(CGRect)rect
{
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextSetFillColorWithColor(context, [UIColor clearColor].CGColor);
    CGContextFillRect(context, rect);
    //下分割线
    CGContextSetStrokeColorWithColor(context, [UIColor lightGrayColor].CGColor);
    CGContextStrokeRect(context, CGRectMake(15, rect.size.height, rect.size.width-15, 1));
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];
    // Configure the view for the selected state
}


- (void)setBtInfo:(MXBTInfo *)bt
{
    _btInfo = bt;
    [self updateInfoView];
}

- (void)updateInfoView
{
    //deviceName
    NSString *deviceNameStr =NSLocalizedString(@"OXIMETER",@"智能血氧");
    CGSize deviceNameSize = [deviceNameStr sizeWithFont:_deviceLabel.font constrainedToSize: CGSizeMake(270, 16)];
    _deviceLabel.frame = CGRectMake(_deviceLabel.frame.origin.x, _deviceLabel.frame.origin.y, deviceNameSize.width, deviceNameSize.height);
    _deviceLabel.text = deviceNameStr;
    
    //department
    MXDeviceInfo *deviceInfo = [MXDeviceInfo DeviceInfoWithHex:[NSString byteToString:_btInfo.manufacturerData]];
    _departmentLabel.frame = CGRectMake(_deviceLabel.frame.origin.x + _deviceLabel.frame.size.width + 8, 12, 100, 14);
    if (deviceInfo.nickName.length > 0) {
        _departmentLabel.text = [NSString stringWithFormat:@"(%@)",deviceInfo.nickName];
    } else {
        _departmentLabel.text = @"";
    }
    
    
    //number
    NSString *numStr = [NSString stringWithFormat:@"SN:%@",deviceInfo.serialNumber];
    
    CGSize numSize = [numStr sizeWithFont:_numberLabel.font constrainedToSize:CGSizeMake(260, 13)];
    _numberLabel.frame = CGRectMake(_numberLabel.frame.origin.x, _departmentLabel.frame.size.height + 12, numSize.width, numSize.height);
    _numberLabel.text = numStr;
}

@end
