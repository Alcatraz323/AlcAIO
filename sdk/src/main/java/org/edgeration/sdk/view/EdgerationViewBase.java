package org.edgeration.sdk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.edgeration.sdk.misc.Constants;

import androidx.annotation.Nullable;

public abstract class EdgerationViewBase extends FrameLayout {
    protected LayoutInflater mLayoutInflater;
    protected View mContentView;

    public EdgerationViewBase(Context context) {
        super(context);
        callSystemService(context);
        init();
    }

    public EdgerationViewBase(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        callSystemService(context);
        init();
    }

    public EdgerationViewBase(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        callSystemService(context);
        init();
    }

    public EdgerationViewBase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        callSystemService(context);
        init();
    }

    private void callSystemService(Context context) {
        if (mLayoutInflater == null) {
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }

    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    public abstract int getLayoutResId();

    @SuppressLint("InflateParams")
    private void init() {
        mContentView = getLayoutInflater().inflate(getLayoutResId(), null);
        addView(mContentView);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            setAlpha(Constants.DEF_DISABLED_ALPHA);
        }
    }
}
