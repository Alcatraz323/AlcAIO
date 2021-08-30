package org.edgeration.sdk.component;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import org.edgeration.sdk.R;
import org.edgeration.sdk.misc.Constants;
import org.edgeration.sdk.utils.SystemPropertiesProxy;
import org.edgeration.sdk.view.EdgerationViewBase;
import org.edgeration.sdk.widget.CentralImageCard;

import java.io.BufferedReader;
import java.io.FileReader;

import androidx.annotation.Nullable;

public class HardwareInfoCards extends EdgerationViewBase {
    protected CentralImageCard mCpuCard, mBatteryCard, mCameraCard, mCellularCard, mScreenCard, mRamCard;
    protected TextView mModelInfo;

    public HardwareInfoCards(Context context) {
        super(context);
        sanitize();
    }

    public HardwareInfoCards(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sanitize();
    }

    public HardwareInfoCards(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sanitize();
    }

    public HardwareInfoCards(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sanitize();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.component_hardware_info_cards;
    }

    private void sanitize() {
        mCpuCard = findViewById(R.id.hardware_cpu);
        mBatteryCard = findViewById(R.id.hardware_battery);
        mCameraCard = findViewById(R.id.hardware_camera);
        mCellularCard = findViewById(R.id.hardware_cellular);
        mScreenCard = findViewById(R.id.hardware_screen);
        mRamCard = findViewById(R.id.hardware_ram);
        mModelInfo = findViewById(R.id.model_summary);
        updateInfo();
    }

    public void updateInfo() {
        String cpu = SystemPropertiesProxy.getStringWithDefUnknown(getContext(),
                Constants.Properties.EDGERATION_CPU_INFO_KEY);
        String battery = SystemPropertiesProxy.getStringWithDefUnknown(getContext(),
                Constants.Properties.EDGERATION_BATTERY_INFO_KEY);
        String camera = SystemPropertiesProxy.getStringWithDefUnknown(getContext(),
                Constants.Properties.EDGERATION_CAMERA_INFO_KEY);
        String cellular = SystemPropertiesProxy.getStringWithDefUnknown(getContext(),
                Constants.Properties.EDGERATION_CELLULAR_INFO_KEY);
        String screen = SystemPropertiesProxy.getStringWithDefUnknown(getContext(),
                Constants.Properties.EDGERATION_SCREEN_INFO_KEY);
        String ram = SystemPropertiesProxy.getStringWithDefUnknown(getContext(),
                Constants.Properties.EDGERATION_RAM_INFO_KEY);
        mCpuCard.setSummary(cpu);
        mBatteryCard.setSummary(battery);
        mCameraCard.setSummary(camera);
        mCellularCard.setSummary(cellular);
        mScreenCard.setSummary(screen);
        mRamCard.setSummary(String.format("%s %s", ram.split("#")[0], getTotalRam()));
        mModelInfo.setText(String.format("%s %s", Build.MANUFACTURER, Build.DEVICE));
    }

    public static String getTotalRam() {
        String path = "/proc/meminfo";
        String firstLine = null;
        int totalRam = 0;
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader, 8192);
            firstLine = br.readLine().split("\\s+")[1];
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (firstLine != null) {
            totalRam = (int) Math.ceil((Float.valueOf(Float.parseFloat(firstLine) / (1024 * 1024)).doubleValue()));
        }

        return totalRam + "GB";
    }
}
