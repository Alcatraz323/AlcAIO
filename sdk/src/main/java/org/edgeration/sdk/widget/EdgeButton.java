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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class EdgeButton extends EdgerationViewBase {
    protected String mTitle;
    protected int mTitleTextColor, mBackImageRes, mIconRes, mBackImageTint, mIconTint;
    protected float mElevation;
    protected boolean mEnableBlur, mEnableElevateEffect;

    protected CardView mRootCard;
    protected ImageView mBackImageView, mIconView;
    protected TextView mTitleView;

    public EdgeButton(@NonNull Context context) {
        super(context);
        sanitize();
    }

    public EdgeButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
        loadAttributes(attrs);
    }

    public EdgeButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
        loadAttributes(attrs);
    }

    public EdgeButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
        loadAttributes(attrs);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.widget_edge_button;
    }

    private void sanitize() {
        mRootCard = findViewById(R.id.root);
        mBackImageView = findViewById(R.id.back);
        mTitleView = findViewById(R.id.title);
        mIconView = findViewById(R.id.icon);
    }

    private void loadAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.EdgeButton);
            mTitle = array.getString(R.styleable.EdgeButton_title);
            mTitleTextColor = array.getColor(R.styleable.EdgeButton_titleTextColor, 0);
            mBackImageRes = array.getResourceId(R.styleable.EdgeButton_imageSrc, 0);
            mBackImageTint = array.getColor(R.styleable.EdgeButton_imageTint, 0);
            mIconRes = array.getResourceId(R.styleable.EdgeButton_iconSrc, 0);
            mIconTint = array.getColor(R.styleable.EdgeButton_iconTint, 0);
            mElevation = array.getDimension(R.styleable.EdgeButton_widgetElevation, 0);
            mEnableElevateEffect = array.getBoolean(R.styleable.EdgeButton_enableElevateEffect, true);
            array.recycle();
            updateView();
        }
    }

    public void updateView() {
        mTitleView.setText(mTitle);
        if (mTitleTextColor != 0) {
            mTitleView.setTextColor(mTitleTextColor);
        }
        if (mBackImageRes != 0) {
            mBackImageView.setImageResource(mBackImageRes);
        }
        if (mBackImageTint != 0) {
            ViewOp.reTintImage(mBackImageView, mBackImageTint);
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
