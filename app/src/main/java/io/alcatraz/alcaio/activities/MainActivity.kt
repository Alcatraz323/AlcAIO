package io.alcatraz.alcaio.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import io.alcatraz.alcaio.Constants
import io.alcatraz.alcaio.R
import io.alcatraz.alcaio.extended.CompatWithPipeActivity
import io.alcatraz.alcaio.utils.NfcUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CompatWithPipeActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_about -> startActivity(
                Intent(
                    this@MainActivity,
                    AboutActivity::class.java
                )
            )
            R.id.menu_log_refresh -> initData()
            R.id.menu_main_log -> startActivity(Intent(this@MainActivity, LogActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.main_card_help -> startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.SUPPORT_URL)
                )
            )

            R.id.main_card_nfc -> startActivity(
                Intent(
                    this,
                    NfcActivity::class.java
                )
            )
        }
    }

    private fun initialize() {
        initData()
        initViews()
    }

    private fun initViews() {
        setSupportActionBar(main_toolbar)
        main_card_nfc.setOnClickListener(this)
        main_card_mcu_mgr.setOnClickListener(this)
        main_profile_mgr_modify.setOnClickListener(this)
        main_card_setting.setOnClickListener(this)
        main_card_help.setOnClickListener(this)
    }

    private fun initData() {
        main_card_nfc.post {
            val isNfcSupported = NfcUtils.isNfcSupported(this)
            if (!isNfcSupported) {
                main_card_nfc_overlay.setBackgroundColor(Color.LTGRAY)
                main_card_nfc.isEnabled = false
                main_card_nfc.isClickable = false
                main_card_nfc_indicator.setText(R.string.main_card_nfc_indicator_not_supported)
            }
        }
    }
}