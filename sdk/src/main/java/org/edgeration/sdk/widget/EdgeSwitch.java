package org.edgeration.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import org.edgeration.sdk.R;
import org.edgeration.sdk.misc.Constants;
import org.edgeration.sdk.utils.AnimateUtils;
import org.edgeration.sdk.view.EdgerationViewBase;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class EdgeSwitch extends EdgerationViewBase implements Checkable {
    protected RelativeLayout mSwitchLayer;
    protected CardView mBackLayer;
    protected boolean mChecked, mClickJudge = true;

    protected float mLeftMaxWidth, mDefWidth;

    protected Object mListener;

    public EdgeSwitch(Context context) {
        super(context);
        sanitize();
    }

    public EdgeSwitch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
    }

    public EdgeSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
    }

    public EdgeSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.widget_edge_switch;
    }

    private void sanitize() {
        mSwitchLayer = findViewById(R.id.switch_layer);
        mBackLayer = findViewById(R.id.switch_back_layer);
        mLeftMaxWidth = getResources().getDimension(R.dimen.switch_left_max_width);
        mDefWidth = getResources().getDimension(R.dimen.min_width_for_switch);
        handleTouchEvent();

    }

    private void handleTouchEvent() {
        setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    EdgeSwitch.this.requestDisallowInterceptTouchEvent(true);
                    mClickJudge = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_SCROLL:
                    mClickJudge = false;
                    ViewGroup.LayoutParams params = mSwitchLayer.getLayoutParams();
                    float deltaWidth = event.getX() - mSwitchLayer.getX();
                    params.width = (int) deltaWidth;
                    if (deltaWidth <= mLeftMaxWidth) {
                        params.width = (int) mLeftMaxWidth;
                    } else if (deltaWidth >= mDefWidth) {
                        params.width = (int) mDefWidth;
                    }
                    mSwitchLayer.setLayoutParams(params);
                    break;
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_UP:
                    if (mClickJudge) {
                        mClickJudge = false;
                        performClick();
                        break;
                    }
                    ViewGroup.LayoutParams finalState = mSwitchLayer.getLayoutParams();
                    setChecked(!(finalState.width <= mDefWidth * 3 / 4), true);
                    break;
            }
            return true;
        });
    }

    public void setChecked(boolean checked, boolean animate) {
        if (animate) {
            AnimateUtils.doWidthHeightValueAnimator(mSwitchLayer,
                    new AccelerateDecelerateInterpolator(),
                    Constants.DEF_ANIMATION_DURATION_FOR_SWITCH,
                    AnimateUtils.VALUE_ANIMATOR_FLAG_WIDTH,
                    mSwitchLayer.getWidth(), (int) (checked ? mDefWidth : mLeftMaxWidth));
        } else {
            ViewGroup.LayoutParams params = mSwitchLayer.getLayoutParams();
            params.width = (int) (checked ? mDefWidth : mLeftMaxWidth);
            mSwitchLayer.setLayoutParams(params);
        }
        mChecked = checked;
        checkedChanged();
    }

    protected void checkedChanged() {
        if (mListener != null) {
            if (mListener instanceof CompoundButton.OnCheckedChangeListener)
                ((CompoundButton.OnCheckedChangeListener) mListener).onCheckedChanged(null, mChecked);
            else if (mListener instanceof OnCheckedChangeListener)
                ((OnCheckedChangeListener) mListener).onCheckedChanged(this, mChecked);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mListener = listener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mListener = listener;
    }

    @Override
    public boolean performClick() {
        setChecked(!mChecked, true);
        return super.performClick();
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(EdgeSwitch edgeSwitch, boolean checked);
    }
}
