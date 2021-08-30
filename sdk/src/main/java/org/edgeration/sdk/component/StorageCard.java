package org.edgeration.sdk.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.TextView;

import org.edgeration.sdk.R;
import org.edgeration.sdk.misc.Constants;
import org.edgeration.sdk.view.EdgerationViewBase;
import org.edgeration.sdk.widget.CircularProgressView;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class StorageCard extends EdgerationViewBase {
    protected TextView mExactUsedIndicator, mExactTotalIndicator, mPercentageUsedIndicator;
    protected CircularProgressView mProgressView;
    protected CardView mCard;

    public StorageCard(Context context) {
        super(context);
        sanitize();
    }

    public StorageCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
    }

    public StorageCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
    }

    public StorageCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.component_storage_card;
    }

    private void sanitize() {
        mExactTotalIndicator = findViewById(R.id.total_space_exact_indicator);
        mExactUsedIndicator = findViewById(R.id.used_space_exact_indicator);
        mPercentageUsedIndicator = findViewById(R.id.used_space_percentage_indicator);
        mProgressView = findViewById(R.id.storage_circular_progress_view);
        mCard = findViewById(R.id.storage_card);
        updateStorageInfo();
        mCard.setOnClickListener(v -> getContext().startActivity(new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS)));
    }

    @SuppressLint("SetTextI18n")
    public void updateStorageInfo() {
        StatFs fsStat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        float totalSize = fsStat.getTotalBytes();
        float usedSize = fsStat.getTotalBytes() - fsStat.getAvailableBytes();
        float percentage = usedSize / totalSize;
        mExactUsedIndicator.setText(getUnit(usedSize));
        mExactTotalIndicator.setText(getUnit(totalSize));
        mPercentageUsedIndicator.setText(
                String.format(Locale.getDefault(), "%2.1f", percentage * 100) + "%");
        if (percentage > 0.9) {
            mProgressView.setProgColor(R.color.preference_color_almost_full);
        }
        mProgressView.setProgress((int) (percentage * 100), Constants.DEF_ANIMATION_DURATION_FOR_STORAGE_CARD);
    }

    private String getUnit(float size) {
        String[] units = {"B", "K", "M", "G", "T"};
        int index = 0;
        while (size > 1024 && index < 4) {
            size = size / 1024;
            index++;
        }
        return java.lang.String.format(Locale.getDefault(), "%.1f%s", size, units[index]);
    }
}
