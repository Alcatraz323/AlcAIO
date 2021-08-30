package org.edgeration.sdk.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

public class BaseCategory extends PreferenceCategory {
    public BaseCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseCategory(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        titleView.setTextSize(20);
        titleView.setTextAppearance(android.R.style.TextAppearance_Widget);
    }
}
