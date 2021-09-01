package io.alcatraz.alcaio.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BLEService : Service() {
    companion object{
        enum class BLEStatus {
            CONNECTED,
            CONNECTING,
            UNCONNECTED,
            // Below is not BLE status, but I place it here for better administration
            INCORRECT_TAG,
            MISMATCH_API
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}