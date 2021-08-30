package org.edgeration.sdk.widget;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import org.edgeration.sdk.R;
import org.edgeration.sdk.misc.Constants;
import org.edgeration.sdk.view.EdgerationViewBase;

public class ImageButton extends EdgerationViewBase {
    protected int mIconRes, mIconTint, mBackgroundTint;
    protected float mElevation, mBackgroundAlpha, mCardCornerRadius;
    protected boolean mEnableElevateEffect;

    protected CardView mRootCard;
    protected ImageView mIconView;
    protected LinearLayout mBackgroundView;

    public ImageButton(Context context) {
        super(context);
        sanitize();
    }

    public ImageButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
        loadAttributes(attrs);
    }

    public ImageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
        loadAttributes(attrs);
    }

    public ImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
        loadAttributes(attrs);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.widget_image_button;
    }

    private void sanitize() {
        mRootCard = findViewById(R.id.root);
        mBackgroundView = findViewById(R.id.back);
        mIconView = findViewById(R.id.icon);
    }

    private void loadAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.ImageButton);
            mIconRes = array.getResourceId(R.styleable.ImageButton_imageSrc, 0);
            mIconTint = array.getResourceId(R.styleable.ImageButton_imageTint, 0);
            mBackgroundTint = array.getColor(R.styleable.ImageButton_backgroundColorTint, 0);
            mBackgroundAlpha = array.getFloat(R.styleable.ImageButton_backgroundAlpha, 1);
            mElevation = array.getDimension(R.styleable.ImageButton_widgetElevation, 0);
            mCardCornerRadius = array.getDimension(R.styleable.ImageButton_widgetCornerRadius,
                    ViewOp.Dp2Px(getContext(), Constants.CORNER_RADIUS_IN_DP));
            mEnableElevateEffect = array.getBoolean(R.styleable.ImageButton_enableElevateEffect, true);
            array.recycle();
            updateView();
        }
    }

    public void updateView() {
        if (mIconRes != 0) {
            mIconView.setImageResource(mIconRes);
        }
        if (mIconTint != 0) {
            ViewOp.reTintImage(mIconView, mIconTint);
        }
        if (mBackgroundTint != 0) {
            mBackgroundView.setBackgroundColor(mBackgroundTint);
        }
        if (mEnableElevateEffect) {
            StateListAnimator elevateAnimator
                    = AnimatorInflater.loadStateListAnimator(getContext(), R.animator.elevator);
            mRootCard.setStateListAnimator(elevateAnimator);
        } else {
            mRootCard.setStateListAnimator(null);
        }
        mBackgroundView.setAlpha(mBackgroundAlpha);
        mRootCard.setCardElevation(mElevation);
        mRootCard.setRadius(mCardCornerRadius);
    }
}
