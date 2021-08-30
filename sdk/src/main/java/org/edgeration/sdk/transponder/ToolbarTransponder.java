package org.edgeration.sdk.transponder;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toolbar;

import org.edgeration.sdk.widget.AppBar;

import java.lang.ref.WeakReference;

public class ToolbarTransponder extends Toolbar {
    WeakReference<AppBar> mAppBar;

    public ToolbarTransponder(Context context) {
        super(context);
    }

    public ToolbarTransponder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolbarTransponder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ToolbarTransponder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setTitle(int resId) {
        super.setTitle(resId);
        if(mAppBar!=null)
            mAppBar.get().getTitleView().setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if(mAppBar!=null)
            mAppBar.get().getTitleView().setText(title);
    }

    public void bindEdgeAppBar(AppBar appBar){
        mAppBar = new WeakReference<>(appBar);
    }
}
