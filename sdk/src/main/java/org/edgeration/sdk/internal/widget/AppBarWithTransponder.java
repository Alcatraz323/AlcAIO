package org.edgeration.sdk.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toolbar;

import org.edgeration.sdk.transponder.ToolbarTransponder;
import org.edgeration.sdk.widget.AppBar;

import androidx.annotation.Nullable;

public class AppBarWithTransponder extends AppBar {
    ToolbarTransponder mTransponder;

    public AppBarWithTransponder(Context context) {
        super(context);
        sanitize();
    }

    public AppBarWithTransponder(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
    }

    public AppBarWithTransponder(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
    }

    public AppBarWithTransponder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
    }

    private void sanitize() {
        mTransponder = new ToolbarTransponder(getContext());
        mTransponder.bindEdgeAppBar(this);
        mTransponder.setVisibility(GONE);
        addView(mTransponder);
    }

    @Override
    public Toolbar getToolbar() {
        return mTransponder;
    }
}
