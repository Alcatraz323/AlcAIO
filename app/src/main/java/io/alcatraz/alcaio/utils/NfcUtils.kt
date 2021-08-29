package io.alcatraz.alcaio.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.nfc.*
import android.nfc.tech.Ndef
import android.provider.Settings
import io.alcatraz.alcaio.LogBuff
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class NfcUtils(activity: Activity) {
    companion object {
        lateinit var mNfcAdapter: NfcAdapter
        var mIntentFilter: Array<IntentFilter>? = null
        var mPendingIntent: PendingIntent? = null
        var mTechList: Array<Array<String>>? = null

        fun nfcCheck(activity: Activity): NfcAdapter? {
            val mNfcAdapter = NfcAdapter.getDefaultAdapter(activity)
            if (mNfcAdapter == null) {
                return null
            } else {
                if (!mNfcAdapter.isEnabled) {
                    val setNfc = Intent(Settings.ACTION_NFC_SETTINGS)
                    activity.startActivity(setNfc)
                }
            }
            return mNfcAdapter
        }

        fun nfcInit(activity: Activity) {
            mPendingIntent = PendingIntent.getActivity(
                activity,
                0,
                Intent(activity, activity.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0
            )
            val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
            val filter2 = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
            try {
                filter.addDataType("*/*")
            } catch (e: MalformedMimeTypeException) {
                e.printStackTrace()
            }
            mIntentFilter = arrayOf(filter, filter2)
            mTechList = null
        }

        fun readNFCFromTag(intent: Intent): String {
            val rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawArray != null) {
                val mNdefMsg = rawArray[0] as NdefMessage
                val mNdefRecord = mNdefMsg.records[0]
                if (mNdefRecord != null) {
                    return String(mNdefRecord.payload, Charset.forName("Utf8"))
                }
            }
            return ""
        }

        fun writeNFCToTag(data: String?, intent: Intent) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            val ndef = Ndef.get(tag)
            try {
                ndef.connect()
                val ndefRecord = NdefRecord.createTextRecord(Locale.CHINESE.toString(), data)
                val records = arrayOf(ndefRecord)
                val ndefMessage = NdefMessage(records)
                ndef.writeNdefMessage(ndefMessage)
            } catch (e: IOException) {
                LogBuff.E(e.message!!);
            }
        }

        fun readNFCId(intent: Intent): String {
            val tag: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
            return byteArrayToHexString(tag.id)
        }

        private fun byteArrayToHexString(inArray: ByteArray): String {
            var i: Int
            var integer: Int
            val hex = arrayOf(
                "0",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "A",
                "B",
                "C",
                "D",
                "E",
                "F"
            )
            var out = ""
            var j: Int = 0
            while (j < inArray.size) {
                integer = inArray[j].toInt() and 0xff
                i = integer shr 4 and 0x0f
                out += hex[i]
                i = integer and 0x0f
                out += hex[i]
                ++j
            }
            return out
        }
    }

    /**
     * 构造函数，用于初始化nfc
     */
    init {
        mNfcAdapter = nfcCheck(activity)!!
        nfcInit(activity)
    }
}