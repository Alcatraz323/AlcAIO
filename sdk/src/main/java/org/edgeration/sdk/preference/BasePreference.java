package org.edgeration.sdk.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;

public abstract class BasePreference extends Preference {
    public BasePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public BasePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BasePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BasePreference(Context context) {
        super(context);
        init();
    }

    public abstract int getLayoutRes();

    private void init() {
        setLayoutResource(getLayoutRes());
    }
}
