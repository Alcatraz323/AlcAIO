package io.alcatraz.alcaio.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import io.alcatraz.alcaio.extended.CompatWithPipeActivity
import io.alcatraz.alcaio.utils.NfcUtils


class NfcActivity: CompatWithPipeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageManager = this.packageManager
        val b1 = packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)
        Toast.makeText(this, "是否支持nfc：$b1", Toast.LENGTH_SHORT).show()

        val nfcUtils = NfcUtils(this)

    }

    override fun onResume() {
        super.onResume()
        NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);
    }

    override fun onPause() {
        super.onPause()
        NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        NfcUtils.writeNFCToTag("AlcAIO://34:80:B3:90:2B:7F", intent!!);
    }

}