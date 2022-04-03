//
//  MXDataCell.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/5.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "MXDataCell.h"
#import "MXUser.h"
#import "OMESoft.h"
#import "OMEImageView.h"
#import "MXUserDefaults.h"

@interface MXDataCell()
@property (nonatomic, retain) UILabel *nameLabel;
@property (nonatomic, retain) UILabel *detailLabel;
@property (nonatomic, strong) OMEImageView *userImageView;
@end
@implementation MXDataCell
@synthesize nameLabel = _nameLabel;
@synthesize detailLabel = _detailLabel;
@synthesize userData = _userData;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        self.userImageView = [[OMEImageView alloc] initWithFrame:CGRectMake(10, 5, 50, 50)];
        _userImageView.placeholderImage = [UIImage imageNamed:@"avatar_default"];
        [self.contentView addSubview:_userImageView];
        self.nameLabel = [[UILabel alloc]initWithFrame: CGRectMake(70, 10, 210, 20)];
        _nameLabel.backgroundColor = [UIColor clearColor];
        _nameLabel.font = [UIFont systemFontOfSize: 17.0f];
        _nameLabel.textColor = [UIColor blackColor];
        [self.contentView addSubview: _nameLabel];
        self.detailLabel = [[UILabel alloc]initWithFrame: CGRectMake(70, 30, 230, 20)];
        _detailLabel.backgroundColor = [UIColor clearColor];
        _detailLabel.font = [UIFont systemFontOfSize: 15.0f];
        _detailLabel.textColor = [UIColor grayColor];
        [self.contentView addSubview: _detailLabel];
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];
    // Configure the view for the selected state
}

- (void)drawRect:(CGRect)rect
{
    if (_userData) {
        _nameLabel.hidden = NO;
        _detailLabel.hidden = NO;
        _nameLabel.textColor = [UIColor whiteColor];
        _nameLabel.text = _userData.name;
        NSString *detailStr = @"";
        BOOL iskg = [[MXUserDefaults sharedInstance] isKG];
        BOOL iscm = [[MXUserDefaults sharedInstance] isCM];
        NSString *weightUnit = iskg ? NSLocalizedString(@"Weight_Unit_KG",@"KG"): NSLocalizedString(@"Weight_Unit_LB", @"lb");
        NSString *heightUnit = iscm ? NSLocalizedString(@"Height_Unit_CM",@"CM"):NSLocalizedString(@"Height_Unit_IN",@"IN");
        if (_userData.height > 0 ) {
            detailStr = iscm ? [detailStr stringByAppendingString:[NSString stringWithFormat:@"%0.f%@  ",_userData.height,heightUnit]] : [detailStr stringByAppendingString:[NSString stringWithFormat:@"%0.f%@  ",_userData.height/2.54,heightUnit]];
        }
//        NSLog(@"___----------->>>>>>>>%f", _userData.weight);
        if (_userData.weight > 0) {
            detailStr = iskg ? detailStr = [detailStr stringByAppendingString:[NSString stringWithFormat:@"%0.f%@",_userData.weight, weightUnit]] : [detailStr stringByAppendingString:[NSString stringWithFormat:@"%0.f%@",_userData.weight/0.4535924, weightUnit]];
        }
        _detailLabel.text = detailStr.length > 0 ? detailStr : @"- -";
        _userImageView.memberId = _userData.memberId;
        _userImageView.imageName = _userData.imageName;
        
    } else {
        _nameLabel.text = @"";
        _detailLabel.text = @"";
        _nameLabel.hidden = YES;
        _detailLabel.hidden = YES;
        _userImageView.memberId = 0;
        _userImageView.imageName = @"";
    }
}

- (void)setUserData:(MXUser *)u
{
    _userData = u;
    [self setNeedsDisplay];
}
@end
