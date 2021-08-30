package org.edgeration.sdk.widget;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import org.edgeration.sdk.R;
import org.edgeration.sdk.view.EdgerationViewBase;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class ContextualStabPortrait extends EdgerationViewBase {
    protected String mTitle, mSummary;
    protected int mTitleTextColor, mSummaryTextColor, mBackImageRes, mIconRes, mBackImageTint, mIconTint, mBlurOverlayColor;
    protected float mElevation;
    protected boolean mEnableBlur, mEnableElevateEffect;

    protected CardView mRootCard;
    protected ImageView mBackImageView, mIconView;
    protected TextView mTitleView, mSummaryView;
    protected RealtimeBlurView mBlurView;

    public ContextualStabPortrait(Context context) {
        super(context);
        sanitize();
    }

    public ContextualStabPortrait(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
        loadAttributes(attrs);
    }

    public ContextualStabPortrait(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
        loadAttributes(attrs);
    }

    public ContextualStabPortrait(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
        loadAttributes(attrs);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.widget_contextual_stab_portrait;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mRootCard.setOnClickListener(l);
    }

    private void sanitize() {
        mRootCard = findViewById(R.id.root);
        mBackImageView = findViewById(R.id.back_image);
        mTitleView = findViewById(android.R.id.title);
        mSummaryView = findViewById(android.R.id.summary);
        mIconView = findViewById(android.R.id.icon);
        mBlurView = findViewById(R.id.blur_layer);
    }

    private void loadAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.ContextualStabPortrait);
            mTitle = array.getString(R.styleable.ContextualStabPortrait_title);
            mTitleTextColor = array.getColor(R.styleable.ContextualStabPortrait_titleTextColor, 0);
            mSummary = array.getString(R.styleable.ContextualStabPortrait_summary);
            mSummaryTextColor = array.getColor(R.styleable.ContextualStabPortrait_summaryTextColor, 0);
            mBackImageRes = array.getResourceId(R.styleable.ContextualStabPortrait_imageSrc, 0);
            mBackImageTint = array.getColor(R.styleable.ContextualStabPortrait_imageTint, 0);
            mIconRes = array.getResourceId(R.styleable.ContextualStabPortrait_iconSrc, 0);
            mIconTint = array.getColor(R.styleable.ContextualStabPortrait_iconTint, 0);
            mBlurOverlayColor = array.getColor(R.styleable.ContextualStabPortrait_blurOverlayColor, 0);
            mElevation = array.getDimension(R.styleable.ContextualStabPortrait_widgetElevation, 0);
            mEnableBlur = array.getBoolean(R.styleable.ContextualStabPortrait_enableBlur, false);
            mEnableElevateEffect = array.getBoolean(R.styleable.ContextualStabPortrait_enableElevateEffect, true);
            array.recycle();
            updateView();
        }
    }

    public void updateView() {
        mTitleView.setText(mTitle);
        if (mTitleTextColor != 0) {
            mTitleView.setTextColor(mTitleTextColor);
        }
        mSummaryView.setText(mSummary);
        if (mSummaryTextColor != 0) {
            mSummaryView.setTextColor(mSummaryTextColor);
        }
        if (mBackImageRes != 0) {
            mBackImageView.setImageResource(mBackImageRes);
        }
        if (mBackImageTint != 0) {
            ViewOp.reTintImage(mBackImageView, mBackImageTint);
        }
        if (!mEnableBlur) {
            mBlurView.setVisibility(GONE);
        }
        if (mBlurOverlayColor != 0) {
            mBlurView.setOverlayColor(mBlurOverlayColor);
        }
        if (mIconRes != 0) {
            mIconView.setImageResource(mIconRes);
        }
        if (mIconTint != 0) {
            ViewOp.reTintImage(mIconView, mIconTint);
        }
        if (mEnableElevateEffect) {
            StateListAnimator elevateAnimator
                    = AnimatorInflater.loadStateListAnimator(getContext(), R.animator.elevator);
            mRootCard.setStateListAnimator(elevateAnimator);
        } else {
            mRootCard.setStateListAnimator(null);
        }
        mRootCard.setCardElevation(mElevation);
    }
}
