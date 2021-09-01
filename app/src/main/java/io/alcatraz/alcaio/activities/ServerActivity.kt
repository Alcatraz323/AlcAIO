package io.alcatraz.alcaio.activities

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import io.alcatraz.alcaio.AsyncInterface
import io.alcatraz.alcaio.LogBuffBLE
import io.alcatraz.alcaio.R
import io.alcatraz.alcaio.extended.CompatWithPipeActivity
import kotlinx.android.synthetic.main.activity_server.*
import java.io.*
import java.nio.charset.Charset
import java.util.*

class ServerActivity : CompatWithPipeActivity() {
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private var serverThread: AcceptThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server)
        initialize()
    }

    private fun initViews() {
        setSupportActionBar(server_toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initData() {
        server_console_box.text = LogBuffBLE.finalLog
    }

    private fun initialize() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (serverThread == null)
            serverThread = AcceptThread()
        serverThread?.setMsgInterface(object : AsyncInterface<String> {
            override fun onDone(result: String) {
                runOnUiThread {
                    initData()
                }
            }
        })
        serverThread?.start()
        initViews()
        initData()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.activity_log_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_log_refresh -> initData()
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class AcceptThread : Thread() {
        private var serverSocket: BluetoothServerSocket? = null
        private var socket: BluetoothSocket? = null
        private var inputStream: InputStream? = null

        private var asyncInterface: AsyncInterface<String>? = null
        override fun run() {
            while (true) {
                try {
                    LogBuffBLE.D("Server listening... Blocked")
                    asyncInterface?.onDone("initialize")
                    socket = serverSocket!!.accept() // Hold
                    if (socket != null) {
                        inputStream = socket!!.inputStream
                        while (true) {
                            val buffer = ByteArray(1024)
                            val count: Int = inputStream!!.read(buffer)
                            val line = String(buffer, 0, count, Charset.forName("utf-8"))
                            LogBuffBLE.I("remote: $line")
                            asyncInterface?.onDone(line)
                        }
                    }
                } catch (e: Exception) {
                    LogBuffBLE.E("Ran into exception: ${e.message}")
                    LogBuffBLE.W("Rerun listen thread...")
                    asyncInterface?.onDone("error")
                }
            }
        }

        fun setMsgInterface(asyncInterface: AsyncInterface<String>) {
            this.asyncInterface = asyncInterface
        }

        init {
            try {
                serverSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
                    SERVICE_NAME, SERVICE_UUID
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        val SERVICE_UUID = UUID.fromString("db764ac8-4b08-7f25-aafe-59d03c27ba6c")
        val SERVICE_NAME = "AlcAIO_BLE"
    }
}