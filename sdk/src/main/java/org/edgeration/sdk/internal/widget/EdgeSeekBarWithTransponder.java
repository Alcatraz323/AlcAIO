package org.edgeration.sdk.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

import org.edgeration.sdk.transponder.SeekBarTransponder;
import org.edgeration.sdk.widget.EdgeSeekBar;

public class EdgeSeekBarWithTransponder extends EdgeSeekBar {
    SeekBarTransponder mTransponder;

    public EdgeSeekBarWithTransponder(Context context) {
        super(context);
        sanitize();
    }

    public EdgeSeekBarWithTransponder(Context context, AttributeSet attrs) {
        super(context, attrs);
        sanitize();
    }

    public EdgeSeekBarWithTransponder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
    }

    private void sanitize() {
        mTransponder = new SeekBarTransponder(getContext());
        mTransponder.bindEdgeSeekBar(this);
        mTransponder.setVisibility(GONE);
        addView(mTransponder);
    }

    @Override
    protected void startTrackingTouch() {
        if (mListener != null) {
            if (mListener instanceof OnSeekBarChangeListener)
                ((OnSeekBarChangeListener) mListener).onStartTrackingTouch(this);
            else if (mListener instanceof SeekBar.OnSeekBarChangeListener)
                ((SeekBar.OnSeekBarChangeListener) mListener)
                        .onStartTrackingTouch(mTransponder);
        }
    }

    @Override
    protected void stopTrackingTouch() {
        if (mListener != null) {
            if (mListener instanceof OnSeekBarChangeListener)
                ((OnSeekBarChangeListener) mListener).onStopTrackingTouch(this);
            else if (mListener instanceof SeekBar.OnSeekBarChangeListener)
                ((SeekBar.OnSeekBarChangeListener) mListener)
                        .onStopTrackingTouch(mTransponder);
        }
    }

    @Override
    protected void progressChange(boolean fromUser) {
        if (mListener != null) {
            if (mListener instanceof OnSeekBarChangeListener)
                ((OnSeekBarChangeListener) mListener).onProgressChange(this, mRatio, fromUser);
            else if (mListener instanceof SeekBar.OnSeekBarChangeListener)
                ((SeekBar.OnSeekBarChangeListener) mListener)
                        .onProgressChanged(mTransponder, getCurrentValue(), fromUser);
        }
    }

    public void requestTransponderSync() {
        setMax(mTransponder.getMax());
        setMin(mTransponder.getMin());
        setProgress(mTransponder.getProgress());
    }

    public SeekBar getSeekBar() {
        return mTransponder;
    }
}
