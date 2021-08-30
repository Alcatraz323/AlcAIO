package org.edgeration.sdk.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import org.edgeration.sdk.R;
import org.edgeration.sdk.misc.Constants;
import org.edgeration.sdk.utils.AnimateUtils;
import org.edgeration.sdk.view.EdgerationViewBase;

import java.lang.reflect.Field;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class AppBar extends EdgerationViewBase {
    protected boolean mShowBackButton, mShowMenu, mEnableBlur,
            mUseSubtitleAsFoldedTitle, mAllowFold;
    protected String mTitle, mSubtitle;
    protected int mTitleTextColor, mSubtitleTextColor, mMenuRes;
    protected float mDefAppBarHeight, mFoldedAppbarHeight;

    protected TextView mTitleView, mSubTitleView;
    protected ImageButton mBackButton, mMenuButton;
    protected PopupMenu mPopupMenu;
    protected RelativeLayout mLayout;
    protected RealtimeBlurView mBlurLayer;
    protected Toolbar mToolbar;
    protected Activity mActivity;

    protected boolean mExpanded = true;
    private int mSavedScrollY = -1;

    private int mSavedTitleHeight, mSavedSubtitleHeight;
    private float mSavedSubtitleTextSize;

    public AppBar(Context context) {
        super(context);
        sanitize();
    }

    public AppBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
        loadAttributes(attrs);
    }

    public AppBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
        loadAttributes(attrs);
    }

    public AppBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
        loadAttributes(attrs);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.widget_app_bar;
    }

    private void loadAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.AppBar);
            mShowBackButton = array.getBoolean(R.styleable.AppBar_showBackButton, false);
            mEnableBlur = array.getBoolean(R.styleable.AppBar_enableBlur, false);
            mShowMenu = array.getBoolean(R.styleable.AppBar_showMenu, false);
            mUseSubtitleAsFoldedTitle = array.getBoolean(R.styleable.AppBar_useSubtitleAsFoldedTitle, false);
            mAllowFold = array.getBoolean(R.styleable.AppBar_allowFold, false);
            mTitle = array.getString(R.styleable.AppBar_title);
            mSubtitle = array.getString(R.styleable.AppBar_subtitle);
            mTitleTextColor = array.getColor(R.styleable.AppBar_titleTextColor, 0);
            mSubtitleTextColor = array.getColor(R.styleable.AppBar_subtitleTextColor, 0);
            mMenuRes = array.getResourceId(R.styleable.AppBar_menuRes, 0);
            mDefAppBarHeight = getResources().getDimension(R.dimen.default_appbar_height);
            mFoldedAppbarHeight = getResources().getDimension(R.dimen.default_folded_appbar_height);
            array.recycle();
            updateView();
        }
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    private void sanitize() {
        mTitleView = findViewById(R.id.title);
        mSubTitleView = findViewById(R.id.subtitle);
        mBackButton = findViewById(R.id.back_button);
        mMenuButton = findViewById(R.id.menu_switch);
        mLayout = findViewById(R.id.root_relative_layout);
        mBlurLayer = findViewById(R.id.blur_layer);
        mToolbar = findViewById(R.id.bar_toolbar);

        mBackButton.setOnClickListener(v -> {
            if (mActivity != null)
                mActivity.finish();
        });
    }

    public void updateView() {
        if (mShowBackButton) {
            mBackButton.setVisibility(VISIBLE);
        } else {
            mBackButton.setVisibility(GONE);
            mLayout.setPadding(mLayout.getPaddingLeft() * 2,
                    mLayout.getPaddingTop(), mLayout.getPaddingRight(), mLayout.getPaddingBottom());
        }
        mBackButton.setVisibility(mShowBackButton ? VISIBLE : GONE);
        mTitleView.setText(mTitle);
        if (mTitleTextColor != 0) {
            mTitleView.setTextColor(mTitleTextColor);
        }
        mSubTitleView.setText(mSubtitle);
        if (mSubtitleTextColor != 0) {
            mSubTitleView.setTextColor(mSubtitleTextColor);
        }

        updateMenu();

        mBlurLayer.setVisibility(mEnableBlur ? VISIBLE : GONE);
        mToolbar.setBackgroundColor(mEnableBlur ? Color.TRANSPARENT : mToolbar.getSolidColor());

        mTitleView.post(() -> {
            mSavedTitleHeight = mTitleView.getHeight();
            mTitleView.setTag(mSavedTitleHeight);
        });

        mSubTitleView.post(() -> {
            mSavedSubtitleHeight = mSubTitleView.getHeight();
            mSavedSubtitleTextSize = mSubTitleView.getTextSize();
            mSubTitleView.setTag(mSavedSubtitleHeight);
        });


    }

    public void updateMenu() {
        if (mShowMenu && mMenuRes != 0) {
            mPopupMenu = new PopupMenu(getContext(), mMenuButton);
            mPopupMenu.inflate(mMenuRes);
            mMenuButton.setOnClickListener(v -> {
                mPopupMenu.dismiss();
                mPopupMenu.show();
            });
            mMenuButton.setVisibility(VISIBLE);
        } else {
            mMenuButton.setVisibility(GONE);
        }
    }

    public void setMenu(Menu menu) {
        mPopupMenu = new PopupMenu(getContext(), mMenuButton);
        Class popupCls = PopupMenu.class;
        try {
            Field mMenu = popupCls.getDeclaredField("mMenu");
            mMenu.set(mPopupMenu, menu);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        mMenuButton.setOnClickListener(v -> {
            mPopupMenu.dismiss();
            mPopupMenu.show();
        });
        mMenuButton.setVisibility(VISIBLE);
    }

    public void setShowBackButton(boolean showBackButton) {
        mShowBackButton = showBackButton;
        mBackButton.setVisibility(mShowBackButton ? VISIBLE : GONE);
    }

    public boolean getShowBackButton() {
        return mShowBackButton;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
        mTitle = title.toString();
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(CharSequence subtitle) {
        mSubtitle = subtitle.toString();
        mSubTitleView.setText(subtitle);
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    public TextView getSubTitleView() {
        return mSubTitleView;
    }

    public ImageButton getBackButton() {
        return mBackButton;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public boolean getExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        if (expanded == mExpanded) {
            return;
        }
        if (mAllowFold) {
            TextView target = mUseSubtitleAsFoldedTitle ? mTitleView : mSubTitleView;
            if (expanded) {
                AnimateUtils.doWidthHeightValueAnimator(target,
                        new AccelerateDecelerateInterpolator(),
                        (int) (Constants.DEF_ANIMATION_DURATION / 1.5f),
                        AnimateUtils.VALUE_ANIMATOR_FLAG_HEIGHT,
                        0, (Integer) target.getTag());
                target.post(() -> AnimateUtils.fadeIn(target, null));
                AnimateUtils.doWidthHeightValueAnimator(this,
                        new AccelerateDecelerateInterpolator(),
                        (int) (Constants.DEF_ANIMATION_DURATION / 1.5f),
                        AnimateUtils.VALUE_ANIMATOR_FLAG_HEIGHT,
                        getLayoutParams().height, (int) mDefAppBarHeight);
                if (mUseSubtitleAsFoldedTitle) {
                    mSubTitleView.setTextAppearance(android.R.style.TextAppearance_Material_Small);
                    AnimateUtils.doTextSizeValueAnimator(mSubTitleView,
                            new AccelerateDecelerateInterpolator(),
                            (int) (Constants.DEF_ANIMATION_DURATION / 1.5f),
                            mTitleView.getTextSize() * 3 / 4, mSavedSubtitleTextSize);
                }
            } else {
                AnimateUtils.fadeOut(target, null);
                AnimateUtils.doWidthHeightValueAnimator(target,
                        new AccelerateDecelerateInterpolator(),
                        (int) (Constants.DEF_ANIMATION_DURATION / 1.5f),
                        AnimateUtils.VALUE_ANIMATOR_FLAG_HEIGHT,
                        (Integer) target.getTag(), 0);
                AnimateUtils.doWidthHeightValueAnimator(this,
                        new AccelerateDecelerateInterpolator(),
                        (int) (Constants.DEF_ANIMATION_DURATION / 1.5f),
                        AnimateUtils.VALUE_ANIMATOR_FLAG_HEIGHT,
                        getLayoutParams().height, (int) mFoldedAppbarHeight);
                if (mUseSubtitleAsFoldedTitle) {
                    mSubTitleView.setTextAppearance(android.R.style.TextAppearance_Material_Large);
                    AnimateUtils.doTextSizeValueAnimator(mSubTitleView,
                            new AccelerateDecelerateInterpolator(),
                            (int) (Constants.DEF_ANIMATION_DURATION / 1.5f),
                            mSavedSubtitleTextSize, mTitleView.getTextSize() * 3 / 4);
                }
            }
            mExpanded = expanded;
        }
    }

    public void bindScrollParent(NestedScrollView scrollView) {
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> processScrollData(scrollY));
    }

    public void bindScrollParent(RecyclerView scrollView) {
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY)
                -> processScrollData(scrollY));
    }

    private void processScrollData(int scrollY) {
        if (mSavedScrollY == -1) {
            mSavedScrollY = scrollY;
            return;
        }
        synchronized (this) {
            setExpanded(mSavedScrollY > scrollY);
        }
    }
}

