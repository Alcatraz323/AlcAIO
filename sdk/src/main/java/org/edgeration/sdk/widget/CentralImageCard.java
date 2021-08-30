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

public class CentralImageCard extends EdgerationViewBase {
    protected String mTitle, mSummary;
    protected int mTitleTextColor, mSummaryTextColor, mIconRes, mIconTint;
    protected float mElevation;
    protected boolean mEnableElevateEffect;

    protected CardView mRootCard;
    protected ImageView mIconView;
    protected TextView mTitleView, mSummaryView;

    public CentralImageCard(Context context) {
        super(context);
        sanitize();
    }

    public CentralImageCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
        loadAttributes(attrs);
    }

    public CentralImageCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
        loadAttributes(attrs);
    }

    public CentralImageCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
        loadAttributes(attrs);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.widget_central_image_card;
    }

    private void sanitize() {
        mRootCard = findViewById(R.id.root_card);
        mTitleView = findViewById(R.id.card_title);
        mSummaryView = findViewById(R.id.card_summary);
        mIconView = findViewById(R.id.card_icon);
    }

    private void loadAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.CentralImageCard);
            mTitle = array.getString(R.styleable.CentralImageCard_title);
            mTitleTextColor = array.getColor(R.styleable.CentralImageCard_titleTextColor, 0);
            mSummary = array.getString(R.styleable.CentralImageCard_summary);
            mSummaryTextColor = array.getColor(R.styleable.CentralImageCard_summaryTextColor, 0);
            mIconRes = array.getResourceId(R.styleable.CentralImageCard_iconSrc, 0);
            mIconTint = array.getColor(R.styleable.CentralImageCard_iconTint, 0);
            mElevation = array.getDimension(R.styleable.CentralImageCard_widgetElevation, 0);
            mEnableElevateEffect = array.getBoolean(R.styleable.CentralImageCard_enableElevateEffect, true);
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

    public void setSummary(CharSequence summary) {
        mSummary = summary.toString();
        mSummaryView.setText(summary);
    }
}
