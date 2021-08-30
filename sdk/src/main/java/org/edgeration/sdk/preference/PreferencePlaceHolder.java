package org.edgeration.sdk.preference;

import android.content.Context;
import android.util.AttributeSet;

import org.edgeration.sdk.R;

public class PreferencePlaceHolder extends BasePreference{
    public PreferencePlaceHolder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PreferencePlaceHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PreferencePlaceHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PreferencePlaceHolder(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_preference_place_holder;
    }
}
