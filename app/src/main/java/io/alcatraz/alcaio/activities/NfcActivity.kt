package io.alcatraz.alcaio.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.widget.Toast
import io.alcatraz.alcaio.R
import io.alcatraz.alcaio.extended.CompatWithPipeActivity
import io.alcatraz.alcaio.utils.NfcUtils


class NfcActivity: CompatWithPipeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NfcUtils(this)
        setContentView(R.layout.activity_nfc)
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
        val tag: Tag? = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        toast(NfcUtils.readMifareUltraLight(intent!!))
    }

}