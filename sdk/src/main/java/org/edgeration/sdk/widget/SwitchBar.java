package org.edgeration.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import org.edgeration.sdk.R;
import org.edgeration.sdk.view.EdgerationViewBase;

import androidx.annotation.Nullable;

public class SwitchBar extends EdgerationViewBase {
    TextView mTextView;
    ImageView mRestrictedIcon;
    EdgeSwitch mEdgeSwitch;

    public SwitchBar(Context context) {
        super(context);
        sanitize();
    }

    public SwitchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
    }

    public SwitchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
    }

    public SwitchBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.widget_edge_switch_bar;
    }

    private void sanitize() {
        mTextView = findViewById(R.id.switch_text);
        mRestrictedIcon = findViewById(R.id.restricted_icon);
        mEdgeSwitch = findViewById(R.id.switch_widget);
    }

    public TextView getSwitchBarTextView() {
        return mTextView;
    }

    public ImageView getRestrictedIcon() {
        return mRestrictedIcon;
    }
    
    public EdgeSwitch getEdgeSwitch() {
        return mEdgeSwitch;
    }
}
