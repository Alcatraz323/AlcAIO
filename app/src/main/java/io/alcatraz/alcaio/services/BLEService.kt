package io.alcatraz.alcaio.services

class BLEService {
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
}