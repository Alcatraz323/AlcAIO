package io.alcatraz.alcaio.utils

import android.os.Build
import io.alcatraz.alcaio.AIOApplication

object ReportUtils {
    fun genPhoneStatusReport(): String {
        return Build.BRAND + ";" + AIOApplication.chargeInfo.convertToText()
    }
}
