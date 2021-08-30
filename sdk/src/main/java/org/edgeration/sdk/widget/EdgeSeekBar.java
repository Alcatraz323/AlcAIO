package org.edgeration.sdk.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import org.edgeration.sdk.R;
import org.edgeration.sdk.misc.Constants;
import org.edgeration.sdk.utils.AnimateUtils;
import org.edgeration.sdk.view.EdgerationViewBase;

import androidx.cardview.widget.CardView;

public class EdgeSeekBar extends EdgerationViewBase {
    protected RelativeLayout mSeekLayer;
    protected CardView mSeekIndicator, mBackLayer;
    protected boolean mExpanded = false;
    protected float mRatio;
    protected int mMaxValue, mMinValue;

    protected float mMinHeight, mExpandedHeight, mIndicatorDefRadius, mIndicatorActiveWidth;
    protected int mSeekLayerColor;

    protected Object mListener;

    public EdgeSeekBar(Context context) {
        super(context);
        sanitize();
    }

    public EdgeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        sanitize();
        loadAttributes(attrs);
    }

    public EdgeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
        loadAttributes(attrs);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.widget_edge_seekbar;
    }

    private void loadAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.EdgeSeekBar);
            mSeekLayerColor = array.getColor(R.styleable.EdgeSeekBar_seekLayerColor, getColorAccent());
            mMaxValue = array.getInteger(R.styleable.EdgeSeekBar_maxValue, 100);
            array.recycle();
            updateView();
        }
    }

    public void updateView() {
        ((CardView) mSeekLayer.getChildAt(0)).setCardBackgroundColor(mSeekLayerColor);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void sanitize() {
        mSeekIndicator = findViewById(R.id.seek_indicator);
        mSeekLayer = findViewById(R.id.seek_layer);
        mBackLayer = findViewById(R.id.seek_back_layer);
        mMinHeight = getResources().getDimension(R.dimen.min_height_for_seekbar);
        mExpandedHeight = getResources().getDimension(R.dimen.expand_height_for_seekbar);
        mIndicatorDefRadius = getResources().getDimension(R.dimen.seekbar_indicator_default_radius);
        mIndicatorActiveWidth = getResources().getDimension(R.dimen.indicator_active_state_width);

        handleTouchEvent();
    }

    public int getMax() {
        return mMaxValue;
    }

    public void setMax(int max) {
        mMaxValue = max;
    }

    public int getMin() {
        return mMinValue;
    }

    public void setMin(int mMinValue) {
        this.mMinValue = mMinValue;
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        mListener = listener;
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener listener) {
        mListener = listener;
    }

    public float getProgress() {
        return mRatio;
    }

    public void setProgress(int progress) {
        if (progress > mMaxValue)
            setProgress(1);
        else if (progress < mMinValue) {
            setProgress(0);
        }
        setProgress(calcRatio(progress));
    }

    public void setProgress(float ratio) {
        setProgress(ratio, true);
    }

    public void setProgress(float ratio, boolean animate) {
        mRatio = ratio;
        if (animate) {
            AnimateUtils.doWidthHeightValueAnimator(mSeekLayer,
                    new OvershootInterpolator(),
                    Constants.DEF_ANIMATION_DURATION,
                    AnimateUtils.VALUE_ANIMATOR_FLAG_WIDTH,
                    mSeekLayer.getWidth(), (int) (mBackLayer.getWidth() * ratio));
        } else {
            ViewGroup.LayoutParams params = mSeekLayer.getLayoutParams();
            params.width = (int) (mBackLayer.getWidth() * ratio);
            mSeekLayer.setLayoutParams(params);
        }
        progressChange(false);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void handleTouchEvent() {
        setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    EdgeSeekBar.this.requestDisallowInterceptTouchEvent(true);
                    expandSeekbar();
                    startTrackingTouch();
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_SCROLL:
                    ViewGroup.LayoutParams params = mSeekLayer.getLayoutParams();
                    int deltaWidth = (int) (event.getX() - mSeekLayer.getX());
                    float ratio = (event.getX() - mSeekLayer.getX()) / mBackLayer.getWidth();
                    params.width = deltaWidth;
                    if (ratio <= 0) {
                        ratio = 0.0f;
                        params.width = (int) mMinHeight;
                    } else if (ratio > 1) {
                        ratio = 1.0f;
                        params.width = mBackLayer.getWidth();
                    } else if (ratio <= 0.05) {
                        params.width = (int) mMinHeight;
                    }

                    mRatio = ratio;
                    mSeekLayer.setLayoutParams(params);
                    progressChange(true);
                    break;
                case MotionEvent.ACTION_UP:
                    foldSeekbar();
                    stopTrackingTouch();
                    break;
            }
            return true;
        });
    }

    protected void startTrackingTouch() {
        if (mListener != null) {
            if (mListener instanceof OnSeekBarChangeListener)
                ((OnSeekBarChangeListener) mListener).onStartTrackingTouch(EdgeSeekBar.this);
            else if (mListener instanceof SeekBar.OnSeekBarChangeListener)
                ((SeekBar.OnSeekBarChangeListener) mListener)
                        .onStartTrackingTouch(null);
        }
    }

    protected void stopTrackingTouch() {
        if (mListener != null) {
            if (mListener instanceof OnSeekBarChangeListener)
                ((OnSeekBarChangeListener) mListener).onStopTrackingTouch(EdgeSeekBar.this);
            else if (mListener instanceof SeekBar.OnSeekBarChangeListener)
                ((SeekBar.OnSeekBarChangeListener) mListener)
                        .onStopTrackingTouch(null);
        }
    }

    protected void progressChange(boolean fromUser) {
        if (mListener != null) {
            if (mListener instanceof OnSeekBarChangeListener)
                ((OnSeekBarChangeListener) mListener).onProgressChange(this, mRatio, fromUser);
            else if (mListener instanceof SeekBar.OnSeekBarChangeListener)
                ((SeekBar.OnSeekBarChangeListener) mListener)
                        .onProgressChanged(null, getCurrentValue(), fromUser);
        }
    }

    public int getCurrentValue() {
        return (int) (mMinValue + (mRatio * (mMaxValue - mMinValue)));
    }

    public float calcRatio(int progress) {
        return (float) (progress - mMinValue) / (mMaxValue - mMinValue);
    }

    private void expandSeekbar() {
        ValueAnimator layerAnimator = ValueAnimator.ofInt((int) mMinHeight, (int) mExpandedHeight);
        layerAnimator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams seekLayerParam = mSeekLayer.getLayoutParams();
            seekLayerParam.height = (int) layerAnimator.getAnimatedValue();
            mSeekLayer.setLayoutParams(seekLayerParam);
            ViewGroup.LayoutParams backLayerParam = mBackLayer.getLayoutParams();
            backLayerParam.height = (int) layerAnimator.getAnimatedValue();
            mBackLayer.setLayoutParams(backLayerParam);
        });
        layerAnimator.setInterpolator(new OvershootInterpolator());
        layerAnimator.setDuration(Constants.DEF_ANIMATION_DURATION);
        layerAnimator.start();

        int oneDp = ViewOp.Dp2Px(getContext(), 1);
        int defaultMargin = getResources().getDimensionPixelSize(R.dimen.indicator_constraint_margin);
        RelativeLayout.LayoutParams indicatorParam = (RelativeLayout.LayoutParams) mSeekIndicator.getLayoutParams();
        indicatorParam.setMargins(defaultMargin,
                defaultMargin + oneDp * 2,
                defaultMargin + oneDp * 3,
                defaultMargin + oneDp * 2);
        mSeekIndicator.setLayoutParams(indicatorParam);

        ValueAnimator indicatorAnimator = ValueAnimator.ofInt((int) mIndicatorDefRadius, (int) mIndicatorActiveWidth);
        indicatorAnimator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams indicatorParam1 = mSeekIndicator.getLayoutParams();
            indicatorParam1.width = (int) indicatorAnimator.getAnimatedValue();
            mSeekIndicator.setLayoutParams(indicatorParam1);
        });
        indicatorAnimator.setInterpolator(new OvershootInterpolator());
        indicatorAnimator.setDuration(Constants.DEF_ANIMATION_DURATION);
        indicatorAnimator.start();

        mExpanded = true;
    }

    private void foldSeekbar() {
        ValueAnimator layerAnimator = ValueAnimator.ofInt((int) mExpandedHeight, (int) mMinHeight);
        layerAnimator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams seekLayerParam = mSeekLayer.getLayoutParams();
            seekLayerParam.height = (int) layerAnimator.getAnimatedValue();
            mSeekLayer.setLayoutParams(seekLayerParam);
            ViewGroup.LayoutParams backLayerParam = mBackLayer.getLayoutParams();
            backLayerParam.height = (int) layerAnimator.getAnimatedValue();
            mBackLayer.setLayoutParams(backLayerParam);
        });
        layerAnimator.setInterpolator(new OvershootInterpolator());
        layerAnimator.setDuration(Constants.DEF_ANIMATION_DURATION);
        layerAnimator.start();

        int defaultMargin = getResources().getDimensionPixelSize(R.dimen.indicator_constraint_margin);
        RelativeLayout.LayoutParams indicatorParam = (RelativeLayout.LayoutParams) mSeekIndicator.getLayoutParams();
        indicatorParam.setMargins(defaultMargin, defaultMargin, defaultMargin, defaultMargin);
        mSeekIndicator.setLayoutParams(indicatorParam);

        ValueAnimator indicatorAnimator = ValueAnimator.ofInt((int) mIndicatorActiveWidth, (int) mIndicatorDefRadius);
        indicatorAnimator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams indicatorParam1 = mSeekIndicator.getLayoutParams();
            indicatorParam1.width = (int) indicatorAnimator.getAnimatedValue();
            mSeekIndicator.setLayoutParams(indicatorParam1);
        });
        indicatorAnimator.setInterpolator(new OvershootInterpolator());
        indicatorAnimator.setDuration(Constants.DEF_ANIMATION_DURATION);
        indicatorAnimator.start();

        mExpanded = false;
    }

    private int getColorAccent() {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

    public interface OnSeekBarChangeListener {
        void onProgressChange(EdgeSeekBar seekBar, float ratio, boolean fromUser);

        void onStartTrackingTouch(EdgeSeekBar seekBar);

        void onStopTrackingTouch(EdgeSeekBar seekBar);
    }
}
