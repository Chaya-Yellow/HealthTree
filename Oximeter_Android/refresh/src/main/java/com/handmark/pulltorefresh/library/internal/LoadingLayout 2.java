/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PixelConvertUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.extras.WaveAnimationView;

import net.frakbot.jumpingbeans.JumpingBeans;

@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {

    private int STATE_NORMAL = 0;
    private int STATE_RELEASE_TO_REFRESH = 1;
    private int STATE_REFRESHING = 2;
    private int STATE_DONE = 3;

	static final String LOG_TAG = "PullToRefresh-LoadingLayout";

	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

	private FrameLayout mInnerLayout;

	protected final ImageView mHeaderImage;
	protected final ProgressBar mHeaderProgress;
    protected final ImageView mMyHeaderProgress;
	protected final ImageView mArrowImageView;
	protected final ProgressBar mMyHeaderProgress1;

	private boolean mUseIntrinsicAnimation;

	private final TextView mHeaderText;
	private final TextView mSubHeaderText;
	private final WaveAnimationView mHeaderWave;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private static final int ROTATE_ANIM_DURATION = 180;

	protected final Mode mMode;
	protected final Orientation mScrollDirection;

	private CharSequence mPullLabel;
	private CharSequence mRefreshingLabel;
	private CharSequence mReleaseLabel;

    private int arrowState = STATE_NORMAL;

	public LoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
		super(context);
		mMode = mode;
		mScrollDirection = scrollDirection;

		switch (scrollDirection) {
			case HORIZONTAL:
				LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_horizontal, this);
				break;
			case VERTICAL:
			default:
				LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_vertical, this);
				break;
		}

		mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
		mHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_text);
        mMyHeaderProgress = (ImageView) mInnerLayout.findViewById(R.id.my_pull_to_refresh_progress);
		mMyHeaderProgress1 = (ProgressBar) mInnerLayout.findViewById(R.id.device_connectioning_progressbar1);
		mHeaderProgress = (ProgressBar) mInnerLayout.findViewById(R.id.pull_to_refresh_progress);
        mArrowImageView = (ImageView)findViewById(R.id.listview_header_arrow);
		mSubHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_sub_text);
		mHeaderImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image);
		mHeaderWave = (WaveAnimationView) mInnerLayout.findViewById(R.id.pull_to_refresh_wave);
		if (PixelConvertUtil.getScreenWidth((Activity) context) > 720) {
			mHeaderWave.getLayoutParams().height = 48;
			mHeaderWave.getLayoutParams().width = 180;
		}else {
			mHeaderWave.getLayoutParams().height = 40;
			mHeaderWave.getLayoutParams().width = 150;
		}

        //???????????????
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInnerLayout.getLayoutParams();

		switch (mode) {
			case PULL_FROM_END:
				lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;

				// Load in labels
				mPullLabel = context.getString(R.string.pull_to_refresh_from_bottom_pull_label);
				mRefreshingLabel = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label);
				mReleaseLabel = context.getString(R.string.pull_to_refresh_from_bottom_release_label);
				break;

			case PULL_FROM_START:
			default:
				lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;

				// Load in labels
				mPullLabel = context.getString(R.string.pull_to_refresh_pull_label);
				mRefreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label);
				mReleaseLabel = context.getString(R.string.pull_to_refresh_release_label);
				break;
		}

		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
			Drawable background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
			if (null != background) {
				ViewCompat.setBackground(this, background);
			}
		}

		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance)) {
			TypedValue styleID = new TypedValue();
			attrs.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance, styleID);
			setTextAppearance(styleID.data);
		}
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance)) {
			TypedValue styleID = new TypedValue();
			attrs.getValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance, styleID);
			setSubTextAppearance(styleID.data);
		}

		// Text Color attrs need to be set after TextAppearance attrs
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
			ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
			if (null != colors) {
				setTextColor(colors);
			}
		}
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor)) {
			ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor);
			if (null != colors) {
				setSubTextColor(colors);
			}
		}

		// Try and get defined drawable from Attrs
		Drawable imageDrawable = null;
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
			imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawable);
		}

		// Check Specific Drawable from Attrs, these overrite the generic
		// drawable attr above
		switch (mode) {
			case PULL_FROM_START:
			default:
				if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableStart)) {
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableStart);
				} else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableTop)) {
					Utils.warnDeprecation("ptrDrawableTop", "ptrDrawableStart");
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
				}
				break;

			case PULL_FROM_END:
				if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
				} else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)) {
					Utils.warnDeprecation("ptrDrawableBottom", "ptrDrawableEnd");
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom);
				}
				break;
		}

		// If we don't have a user defined drawable, load the default
		if (null == imageDrawable) {
			imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
		}

		// Set Drawable, and save width/height
		setLoadingDrawable(imageDrawable);

		reset();
	}

	public final void setHeight(int height) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.height = height;
		requestLayout();
	}

	public final void setWidth(int width) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.width = width;
		requestLayout();
	}

	public final int getContentSize() {
		switch (mScrollDirection) {
			case HORIZONTAL:
				return mInnerLayout.getWidth();
			case VERTICAL:
			default:
				return mInnerLayout.getHeight();
		}
	}

	public final void hideAllViews() {
		if (View.VISIBLE == mHeaderWave.getVisibility()) {
			mHeaderWave.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mHeaderText.getVisibility()) {
			mHeaderText.setVisibility(View.INVISIBLE);
            mMyHeaderProgress.setVisibility(GONE);
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.GONE);
			setProgressShowOrHide(View.GONE);
		}
		if (View.VISIBLE == mHeaderProgress.getVisibility()) {
			mHeaderProgress.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mHeaderImage.getVisibility()) {
			mHeaderImage.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mSubHeaderText.getVisibility()) {
			mSubHeaderText.setVisibility(View.INVISIBLE);
		}
	}

	public final void onPull(float scaleOfLayout) {
		if (!mUseIntrinsicAnimation) {
			onPullImpl(scaleOfLayout);
		}
	}

	public final void pullToRefresh() {
		if (null != mHeaderText) {
			mHeaderWave.setVisibility(VISIBLE);
			mHeaderText.setText(mPullLabel);
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(VISIBLE);
            if (arrowState == STATE_RELEASE_TO_REFRESH) {
                mArrowImageView.startAnimation(mRotateDownAnim);
            }
            arrowState = STATE_NORMAL;
            mMyHeaderProgress.setVisibility(GONE);
			setProgressShowOrHide(View.GONE);
		}

		// Now call the callback
		pullToRefreshImpl();
	}

	public final void refreshing() {
		mHeaderWave.startAnimation();
		if (null != mHeaderText) {
			mHeaderText.setText(mRefreshingLabel);
            setTextDotJumping(mHeaderText);
            mMyHeaderProgress.setVisibility(VISIBLE);
            mArrowImageView.setVisibility(View.GONE);
            mArrowImageView.clearAnimation();
            arrowState = STATE_REFRESHING;
			setProgressShowOrHide(View.VISIBLE);
		}

		if (mUseIntrinsicAnimation) {
//			((AnimationDrawable) mHeaderImage.getDrawable()).start();
		} else {
			// Now call the callback
			refreshingImpl();
		}

		if (null != mSubHeaderText) {
			mSubHeaderText.setVisibility(View.GONE);
		}
	}

	public final void releaseToRefresh() {
		if (null != mHeaderText) {
			mHeaderText.setText(mReleaseLabel);
            mMyHeaderProgress.setVisibility(GONE);
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(VISIBLE);
            mArrowImageView.startAnimation(mRotateUpAnim);
            arrowState = STATE_RELEASE_TO_REFRESH;
			setProgressShowOrHide(View.GONE);
		}

		// Now call the callback
		releaseToRefreshImpl();
	}

	public final void reset() {
		mHeaderWave.stopAnimation();
		if (null != mHeaderText) {
			mHeaderText.setText(mPullLabel);
            mMyHeaderProgress.setVisibility(GONE);
            mArrowImageView.setVisibility(VISIBLE);
            mArrowImageView.clearAnimation();
            arrowState = STATE_NORMAL;
			setProgressShowOrHide(View.GONE);

		}
//		mHeaderImage.setVisibility(View.VISIBLE);

		if (mUseIntrinsicAnimation) {
//			((AnimationDrawable) mHeaderImage.getDrawable()).stop();
		} else {
			// Now call the callback
			resetImpl();
		}

		if (null != mSubHeaderText) {
			if (TextUtils.isEmpty(mSubHeaderText.getText())) {
				mSubHeaderText.setVisibility(View.GONE);
			} else {
				mSubHeaderText.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		setSubHeaderText(label);
	}

	public final void setLoadingDrawable(Drawable imageDrawable) {
		// Set Drawable
//		mHeaderImage.setImageDrawable(imageDrawable);
//		mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);
//
//		// Now call the callback
//		onLoadingDrawableSet(imageDrawable);
	}

	public void setPullLabel(CharSequence pullLabel) {
		mPullLabel = pullLabel;
	}

	public void setRefreshingLabel(CharSequence refreshingLabel) {
		mRefreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(CharSequence releaseLabel) {
		mReleaseLabel = releaseLabel;
	}

	@Override
	public void setTextTypeface(Typeface tf) {
		mHeaderText.setTypeface(tf);
	}

	public final void showInvisibleViews() {
		if (View.VISIBLE == mHeaderWave.getVisibility()) {
			mHeaderWave.setVisibility(View.VISIBLE);
		}
		if (View.INVISIBLE == mHeaderText.getVisibility()) {
			mHeaderText.setVisibility(View.VISIBLE);
		}
//		if (View.INVISIBLE == mHeaderProgress.getVisibility()) {
//			mHeaderProgress.setVisibility(View.VISIBLE);
//		}
//		if (View.INVISIBLE == mHeaderImage.getVisibility()) {
//			mHeaderImage.setVisibility(View.VISIBLE);
//		}
		if (View.INVISIBLE == mSubHeaderText.getVisibility()) {
			mSubHeaderText.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Callbacks for derivative Layouts
	 */

	protected abstract int getDefaultDrawableResId();

	protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

	protected abstract void onPullImpl(float scaleOfLayout);

	protected abstract void pullToRefreshImpl();

	protected abstract void refreshingImpl();

	protected abstract void releaseToRefreshImpl();

	protected abstract void resetImpl();

	private void setSubHeaderText(CharSequence label) {
		if (null != mSubHeaderText) {
			if (TextUtils.isEmpty(label)) {
				mSubHeaderText.setVisibility(View.GONE);
			} else {
				mSubHeaderText.setText(label);

				// Only set it to Visible if we're GONE, otherwise VISIBLE will
				// be set soon
				if (View.GONE == mSubHeaderText.getVisibility()) {
					mSubHeaderText.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private void setSubTextAppearance(int value) {
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextAppearance(getContext(), value);
		}
	}

	private void setSubTextColor(ColorStateList color) {
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextColor(color);
		}
	}

	private void setTextAppearance(int value) {
		if (null != mHeaderText) {
			mHeaderText.setTextAppearance(getContext(), value);
		}
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextAppearance(getContext(), value);
		}
	}

	private void setTextColor(ColorStateList color) {
		if (null != mHeaderText) {
			mHeaderText.setTextColor(color);
		}
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextColor(color);
		}
	}

	private void setProgressShowOrHide(int type) {
		if (getSystemVersion() < 5) {
			mMyHeaderProgress1.setVisibility(GONE);
			mMyHeaderProgress.setVisibility(type);
		}else {
			mMyHeaderProgress.setVisibility(GONE);
			mMyHeaderProgress1.setVisibility(type);
		}
	}

	private int getSystemVersion() {
		int version = Integer.parseInt(android.os.Build.VERSION.RELEASE.charAt(0) + "");
		return version;
	}

     void setTextDotJumping(TextView textView) {
        JumpingBeans.with(textView)
                .appendJumpingDots()
                .build();
    }
}
