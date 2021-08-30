package org.edgeration.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import org.edgeration.sdk.R;
import org.edgeration.sdk.utils.StatusBarUtil;
import org.edgeration.sdk.view.EdgerationViewBase;

import androidx.annotation.Nullable;

public class AccountBar extends EdgerationViewBase {
    protected boolean mEnableBlur, mTopStatusBarExtend;

    protected RealtimeBlurView mBlurLayer;
    protected Toolbar mToolbar;
    protected ImageView mAccountAvatar, mDashBoard;

    public AccountBar(Context context) {
        super(context);
        sanitize();
    }

    public AccountBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
        loadAttributes(attrs);
    }

    public AccountBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
        loadAttributes(attrs);
    }

    public AccountBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
        loadAttributes(attrs);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.widget_account_bar;
    }

    private void loadAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.AccountBar);
            mEnableBlur = array.getBoolean(R.styleable.AccountBar_enableBlur, false);
            mTopStatusBarExtend = array.getBoolean(R.styleable.AccountBar_topStatusBarExtend, false);
            array.recycle();
            updateView();
        }
    }

    private void sanitize() {
        mBlurLayer = findViewById(R.id.blur_layer);
        mToolbar = findViewById(R.id.bar_toolbar);
        mAccountAvatar = findViewById(R.id.account_avatar);
        mDashBoard = findViewById(R.id.btn_dashboard);
    }

    public void updateView() {
        mBlurLayer.setVisibility(mEnableBlur ? VISIBLE : GONE);
        mToolbar.setBackgroundColor(mEnableBlur ? Color.TRANSPARENT : mToolbar.getSolidColor());

        post(() -> {
            if (mTopStatusBarExtend) {
                int statusBarHeight = StatusBarUtil.getStatusBarHeight(getContext());
                ViewGroup.LayoutParams barLp = getLayoutParams();
                barLp.height = getHeight() + statusBarHeight;
                setLayoutParams(barLp);
            }
        });
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public ImageView getAvatarView() {
        return mAccountAvatar;
    }

    public ImageView getDashBoardView() {
        return mDashBoard;
    }
}
