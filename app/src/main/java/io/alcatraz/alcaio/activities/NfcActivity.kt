package io.alcatraz.alcaio.activities

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import io.alcatraz.alcaio.AsyncInterface
import io.alcatraz.alcaio.Constants
import io.alcatraz.alcaio.LogBuff
import io.alcatraz.alcaio.R
import io.alcatraz.alcaio.beans.AlcAIOTag
import io.alcatraz.alcaio.databinding.DialogWriteTagBinding
import io.alcatraz.alcaio.extended.CompatWithPipeActivity
import io.alcatraz.alcaio.receivers.ChargeReceiver
import io.alcatraz.alcaio.services.BLEService
import io.alcatraz.alcaio.utils.NfcUtils
import io.alcatraz.alcaio.utils.ReportUtils
import kotlinx.android.synthetic.main.activity_nfc.*
import java.lang.Exception


class NfcActivity : CompatWithPipeActivity() {
    private var waitingForWrite = false
    private var currentStatus = BLEService.Companion.BLEStatus.UNCONNECTED
    private lateinit var currentReadTag: AlcAIOTag
    private lateinit var writeDialogBinding: DialogWriteTagBinding
    private lateinit var writeDialog: AlertDialog
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private lateinit var mChargeReceiver: ChargeReceiver

    private var mClientThread: ClientThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        initialize()
    }

    override fun onResume() {
        super.onResume()
        NfcUtils.mNfcAdapter.enableForegroundDispatch(
            this,
            NfcUtils.mPendingIntent,
            NfcUtils.mIntentFilter,
            NfcUtils.mTechList
        );
    }

    override fun onPause() {
        super.onPause()
        NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (waitingForWrite) {
            writeWithSetData(intent)
        } else {
            updateViewWithTag(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterChargeReceiver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_nfc_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_nfc_write)
            writeDialog.show()
        return super.onOptionsItemSelected(item)
    }

    fun updateViewWithTag(intent: Intent?) {
        val tag: Tag? = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        val tagData: String
        if (tag!!.techList.asList().contains("android.nfc.tech.MifareUltralight")) {
            tagData = NfcUtils.readMifareUltraLight(intent)
        } else {
            tagData = NfcUtils.readNFCFromTag(intent)
        }
        currentReadTag = AlcAIOTag.createFromLink(tagData)
        updateView()
        connectNfcDevice()
    }

    fun updateView() {
        updateStatusCard()
        nfc_connected_api.text = currentReadTag.api

        var functionsInline = ""
        currentReadTag.functions?.forEachIndexed { index, s ->
            functionsInline += s
            if (index != currentReadTag.functions!!.size - 1)
                functionsInline += "\n"
        }
        nfc_connected_function.text = functionsInline
        nfc_connected_remote_address.text = currentReadTag.remoteAddress
        nfc_connected_share_link.text = currentReadTag.rawLink
    }

    fun updateStatusCard() {
        if (Constants.API_VERSION != currentReadTag.api) {
            currentStatus = BLEService.Companion.BLEStatus.MISMATCH_API
        }
        if (!(currentReadTag.createFromSupported)!!) {
            currentStatus = BLEService.Companion.BLEStatus.INCORRECT_TAG
        }
        when (currentStatus) {
            BLEService.Companion.BLEStatus.CONNECTED -> {
                nfc_status_card_overlay.setBackgroundColor(getColor(R.color.lime_colorPrimary))
                nfc_status_card_indicator.setText(
                    String.format(
                        getString(R.string.nfc_status_connected),
                        currentReadTag.name,
                        currentReadTag.mac
                    )
                )
            }
            BLEService.Companion.BLEStatus.CONNECTING -> {
                nfc_status_card_overlay.setBackgroundColor(Color.DKGRAY)
                nfc_status_card_indicator.setText(R.string.nfc_status_connecting)
            }
            BLEService.Companion.BLEStatus.UNCONNECTED -> {
                nfc_status_card_overlay.setBackgroundColor(Color.DKGRAY)
                nfc_status_card_indicator.setText(R.string.nfc_status_disconnected)
            }
            BLEService.Companion.BLEStatus.INCORRECT_TAG -> {
                nfc_status_card_overlay.setBackgroundColor(Color.RED)
                nfc_status_card_indicator.setText(R.string.nfc_status_unsupported_tag)
            }
            BLEService.Companion.BLEStatus.MISMATCH_API -> {
                nfc_status_card_overlay.setBackgroundColor(Color.RED)
                nfc_status_card_indicator.setText(
                    String.format(
                        getString(R.string.nfc_status_unsupported_version),
                        currentReadTag.api,
                        currentReadTag.mac
                    )
                )
            }
        }
    }

    fun writeWithSetData(intent: Intent?) {
        val toWrite = genToWriteContent()

        LogBuff.D("ToWrite_Final: $toWrite")
        val tag: Tag? = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        if (tag!!.techList.asList().contains("android.nfc.tech.MifareUltralight")) {
            NfcUtils.writeMifareUltraLight(intent, toWrite)
        } else {
            NfcUtils.writeNFCToTag(toWrite, intent)
        }

        toast(R.string.nfc_write_tag_success)
        waitingForWrite = false
        writeDialog.dismiss()
    }

    @Suppress("UNNECESSARY_SAFE_CALL")
    fun connectNfcDevice() {
        mClientThread?.kill()
        mClientThread = ClientThread()
        mClientThread?.setStatusInterface(object : AsyncInterface<BLEService.Companion.BLEStatus> {
            override fun onDone(result: BLEService.Companion.BLEStatus) {
                runOnUiThread {
                    currentStatus = result
                    updateStatusCard()
                }
            }
        })
        mClientThread?.start()
    }

    fun prepareWriteDialog() {
        writeDialogBinding = DialogWriteTagBinding.inflate(layoutInflater)
        writeDialogBinding.nfcToWriteApi.setText(Constants.API_VERSION.toString())
        writeDialog = AlertDialog.Builder(this)
            .setTitle(R.string.nfc_write_entry)
            .setView(writeDialogBinding.root)
            .create()

        writeDialogBinding.nfcWriteCancel.setOnClickListener {
            writeDialog.dismiss()
            writeDialogBinding.nfcWriteConfirm.isEnabled = true
            writeDialogBinding.nfcWriteInputGroup.isEnabled = true
        }

        writeDialog.setOnDismissListener {
            if (waitingForWrite) {
                toast(R.string.nfc_write_tag_cancel)
                waitingForWrite = false
            }
            writeDialogBinding.nfcWriteConfirm.isEnabled = true
            writeDialogBinding.nfcWriteInputGroup.isEnabled = true
        }

        writeDialogBinding.nfcWriteConfirm.setOnClickListener {
            waitingForWrite = true
            writeDialogBinding.nfcToWriteFinal.text = genToWriteContent()
            writeDialogBinding.nfcWriteConfirm.isEnabled = false
            writeDialogBinding.nfcWriteInputGroup.isEnabled = false
        }
    }

    private fun initialize() {
        NfcUtils(this)
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        currentReadTag = AlcAIOTag("", "", "", "", "", true, arrayListOf())
        mChargeReceiver = ChargeReceiver()
        prepareWriteDialog()
        registerChargeReceiver()
        setSupportActionBar(nfc_toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun genToWriteContent(): String {
        val funcArray = arrayListOf<String>()
        if (writeDialogBinding.nfcFunctionWirelessCharge.isChecked)
            funcArray.add(writeDialogBinding.nfcFunctionWirelessCharge.text as String)
        if (writeDialogBinding.nfcFunctionChargeInfo.isChecked)
            funcArray.add(writeDialogBinding.nfcFunctionChargeInfo.text as String)
        if (writeDialogBinding.nfcFunctionUsbTransponder.isChecked)
            funcArray.add(writeDialogBinding.nfcFunctionUsbTransponder.text as String)

        val toWrite = AlcAIOTag(
            "",
            writeDialogBinding.nfcToWriteName.editText?.text.toString(),
            writeDialogBinding.nfcToWriteMac.editText?.text.toString(),
            Constants.API_VERSION,
            writeDialogBinding.nfcToWriteRemoteAddress.editText?.text.toString(),
            true,
            funcArray
        ).convertToLink()
        return toWrite
    }

    private fun registerChargeReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(mChargeReceiver, intentFilter)
    }

    private fun unregisterChargeReceiver() {
        unregisterReceiver(mChargeReceiver)
    }

    private inner class ClientThread : Thread() {
        private var statusInterface: AsyncInterface<BLEService.Companion.BLEStatus>? = null
        private var killed = false

        override fun run() {
            mBluetoothAdapter.cancelDiscovery();
            val device = mBluetoothAdapter.getRemoteDevice(currentReadTag.mac)
            val bleSocket = device.createRfcommSocketToServiceRecord(ServerActivity.SERVICE_UUID)
            statusInterface?.onDone(BLEService.Companion.BLEStatus.CONNECTING)
            try {
                bleSocket.connect()
                val outputStream = bleSocket.outputStream
                statusInterface?.onDone(BLEService.Companion.BLEStatus.CONNECTED)
                while (!killed) {
                    outputStream?.write(ReportUtils.genPhoneStatusReport().toByteArray())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogBuff.E("Bluetooth client ran into exception: ${e.message}")
            }
            statusInterface?.onDone(BLEService.Companion.BLEStatus.UNCONNECTED)
        }

        fun setStatusInterface(statusInterface: AsyncInterface<BLEService.Companion.BLEStatus>) {
            this.statusInterface = statusInterface
        }

        fun kill() {
            killed = true
        }
    }

}