package org.edgeration.sdk.component;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import org.edgeration.sdk.R;
import org.edgeration.sdk.misc.Constants;
import org.edgeration.sdk.utils.SystemPropertiesProxy;
import org.edgeration.sdk.view.EdgerationViewBase;

import java.util.Locale;

import androidx.annotation.Nullable;

public class SystemVersionCard extends EdgerationViewBase {
    protected TextView mEdgerVersionIndicator, mAndroidVersionIndicator;

    public SystemVersionCard(Context context) {
        super(context);
        sanitize();
    }

    public SystemVersionCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
    }

    public SystemVersionCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
    }

    public SystemVersionCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.component_system_version_card;
    }

    private void sanitize() {
        mEdgerVersionIndicator = findViewById(R.id.edgeration_version_indicator);
        mAndroidVersionIndicator = findViewById(R.id.android_version_indicator);
        updateInfo();
    }

    public void updateInfo() {
        String edgerVersion
                = SystemPropertiesProxy.getString(getContext(),
                Constants.Properties.EDGERATION_SHORT_VER_KEY, Constants.Properties.EDGERATION_PROPERTIES_DEF_VAL_UNKNOWN);
        String[] androidVersions = new String[40];
        androidVersions[30] = "R";
        androidVersions[31] = "S";
        androidVersions[32] = "T";
        int sdkInt = Build.VERSION.SDK_INT;
        String androidVersion = String.format(Locale.getDefault(), "%s %d", androidVersions[sdkInt], sdkInt);
        mEdgerVersionIndicator.setText(edgerVersion);
        mAndroidVersionIndicator.setText(androidVersion);
    }
}
