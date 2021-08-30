package org.edgeration.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import org.edgeration.sdk.R;
import org.edgeration.sdk.misc.Constants;
import org.edgeration.sdk.view.EdgerationViewBase;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class SearchBox extends EdgerationViewBase {
    protected String mHint;
    protected boolean mUseAsStaticEntrance;
    protected float mElevation, mCardCornerRadius;

    protected SearchView mSearchView;
    protected ImageButton mSearchButton;
    protected TextView mStaticTextView;
    protected CardView mRootCard;
    protected Toolbar mToolbar;

    public SearchBox(Context context) {
        super(context);
        sanitize();
    }

    public SearchBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
        loadAttributes(attrs);
    }

    public SearchBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
        loadAttributes(attrs);
    }

    public SearchBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
        loadAttributes(attrs);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.widget_search_box;
    }

    private void sanitize() {
        mRootCard = findViewById(R.id.root);
        mSearchView = findViewById(R.id.search_view);
        mSearchButton = findViewById(R.id.search_button);
        mStaticTextView = findViewById(R.id.static_text);
        mToolbar = findViewById(R.id.search_action_bar);

        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = mSearchView.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        int plateId = getResources().getIdentifier("android:id/search_plate", null, null);
        View underline = mSearchView.findViewById(plateId);
        underline.setBackgroundColor(Color.TRANSPARENT);
    }

    private void loadAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.SearchBox);
            mHint = array.getString(R.styleable.SearchBox_hint);
            mUseAsStaticEntrance = array.getBoolean(R.styleable.SearchBox_useAsStaticEntrance, false);
            mElevation = array.getDimension(R.styleable.SearchBox_widgetElevation, 0);
            mCardCornerRadius = array.getDimension(R.styleable.SearchBox_widgetCornerRadius,
                    ViewOp.Dp2Px(getContext(), Constants.CORNER_RADIUS_IN_DP));
            array.recycle();
            updateView();
        }
    }

    public void updateView() {
        if (mUseAsStaticEntrance) {
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
            int[] attribute = new int[]{android.R.attr.selectableItemBackground};
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
            Drawable drawable = typedArray.getDrawable(0);
            typedArray.recycle();

            mRootCard.setForeground(drawable);
            mRootCard.setClickable(true);
            mSearchButton.setEnabled(false);

            mSearchView.setVisibility(GONE);
            mStaticTextView.setVisibility(VISIBLE);
        } else {
            mRootCard.setForeground(null);
            mRootCard.setClickable(false);
            mSearchButton.setEnabled(true);

            mSearchView.setVisibility(VISIBLE);
            mStaticTextView.setVisibility(GONE);
        }

        mRootCard.setCardElevation(mElevation);
        mRootCard.setRadius(mCardCornerRadius);
        mSearchView.setQueryHint(mHint);
        mStaticTextView.setText(mHint);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
