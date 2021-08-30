package org.edgeration.sdk.preference;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;

import org.edgeration.sdk.R;

import androidx.preference.PreferenceViewHolder;

public class BottomCornerPreference extends BasePreference {
    protected View mHolder;

    public BottomCornerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BottomCornerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BottomCornerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomCornerPreference(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        mHolder = holder.itemView;

        Configuration mConfiguration = getContext().getResources().getConfiguration();
        switch (mConfiguration.uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                mHolder.setBackground(getContext().getDrawable(R.drawable.bottom_item_corner_dark));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                mHolder.setBackground(getContext().getDrawable(R.drawable.bottom_item_corner_light));
                break;
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_basic_list_item_bottom_corner;
    }
}
