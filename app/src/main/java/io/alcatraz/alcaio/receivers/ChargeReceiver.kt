package io.alcatraz.alcaio.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import io.alcatraz.alcaio.AIOApplication

class ChargeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
            val statusInt = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0)
            val levelInt = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val volInt = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
            val tempInt = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)

            var statusStr = ""
            when (statusInt) {
                BatteryManager.BATTERY_STATUS_CHARGING -> statusStr = "Charging"
                BatteryManager.BATTERY_STATUS_DISCHARGING -> statusStr = "Discharging"
                BatteryManager.BATTERY_STATUS_FULL -> statusStr = "Fully charged"
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> statusStr = "Not charging"
                BatteryManager.BATTERY_STATUS_UNKNOWN -> statusStr = "Unknown"
            }
            synchronized(AIOApplication.chargeInfo) {
                AIOApplication.chargeInfo.status = statusStr
                AIOApplication.chargeInfo.level = levelInt.toString()
                AIOApplication.chargeInfo.voltage = volInt.toString()
                AIOApplication.chargeInfo.temperature = tempInt.toString()
            }

        }
    }
}