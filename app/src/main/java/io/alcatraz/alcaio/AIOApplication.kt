package io.alcatraz.alcaio

import android.app.Application
import android.content.Context
import io.alcatraz.alcaio.beans.ChargeInfo

class AIOApplication : Application() {
    var overallContext: Context? = null
        private set


    //TODO : Check string.xml/Setup versionCode/build.gradle when release update
    //TODO : Set Empty View for all adapter views
    override fun onCreate() {
        overallContext = applicationContext
        super.onCreate()
    }

    companion object {
        val chargeInfo = ChargeInfo("", "", "", "")
    }

}
