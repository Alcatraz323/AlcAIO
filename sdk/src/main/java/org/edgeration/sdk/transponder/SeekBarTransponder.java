package org.edgeration.sdk.transponder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

import org.edgeration.sdk.widget.EdgeSeekBar;

import java.lang.ref.WeakReference;

@SuppressLint("AppCompatCustomView")
public class SeekBarTransponder extends SeekBar {
    private WeakReference<EdgeSeekBar> mEdgeSeekBar;

    public SeekBarTransponder(Context context) {
        super(context);
    }

    public SeekBarTransponder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarTransponder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SeekBarTransponder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindEdgeSeekBar(EdgeSeekBar seekBar) {
        mEdgeSeekBar = new WeakReference<>(seekBar);
        seekBar.setMax(getMax());
        seekBar.setMin(getMin());
        seekBar.setProgress(getProgress());
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        super.setOnSeekBarChangeListener(l);
        if (mEdgeSeekBar != null)
            mEdgeSeekBar.get().setOnSeekBarChangeListener(l);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        if (mEdgeSeekBar != null)
            mEdgeSeekBar.get().setProgress(progress);
    }

    @Override
    public void setProgress(int progress, boolean animate) {
        super.setProgress(progress, animate);
        if (mEdgeSeekBar != null)
            mEdgeSeekBar.get().setProgress((float) progress / getMax(), animate);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mEdgeSeekBar != null)
            mEdgeSeekBar.get().setEnabled(enabled);
    }

    @Override
    public synchronized void setMax(int max) {
        super.setMax(max);
        if (mEdgeSeekBar != null)
            mEdgeSeekBar.get().setMax(max);
    }

    @Override
    public synchronized void setMin(int min) {
        super.setMin(min);
        if (mEdgeSeekBar != null)
            mEdgeSeekBar.get().setMin(min);
    }
}
