//
//  OMEImageView.h
//  FirstAid
//
//  Created by Omesoft on 12-10-22.
//
//

#import <UIKit/UIKit.h>
#import "EGOImageLoader.h"

@interface OMEImageView : UIImageView<EGOImageLoaderObserver> {
@private
	NSURL *imageURL;
	UIImage* placeholderImage;
}

- (id)initWithPlaceholderImage:(UIImage*)anImage; // delegate:nil
- (void)cancelImageLoad;

@property (nonatomic,retain) NSString *imageName;
@property (nonatomic,retain) UIImage* placeholderImage;
@property (nonatomic, assign) NSUInteger memberId;
@property (nonatomic, strong) NSURL *currentImageURL;
@end
