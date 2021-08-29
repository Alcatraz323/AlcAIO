package io.alcatraz.alcaio.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import io.alcatraz.alcaio.Constants
import io.alcatraz.alcaio.R
import io.alcatraz.alcaio.adapters.PreferenceListAdapter
import io.alcatraz.alcaio.beans.PreferenceHeader
import io.alcatraz.alcaio.extended.CompatWithPipeActivity
import kotlinx.android.synthetic.main.activity_preference.*

class PreferenceActivity : CompatWithPipeActivity() {
    private lateinit var headers: List<PreferenceHeader>
    private lateinit var adapter: PreferenceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)
        prepareHeader()
        initViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        sendBroadcast(Intent().setAction(Constants.BROADCAST_ACTION_UPDATE_PREFERENCES))
    }

    private fun initViews() {
        setSupportActionBar(preference_act_toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        adapter = PreferenceListAdapter(this, headers)
        preference_act_list.adapter = adapter

        preference_act_list.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
            val intent = Intent(this@PreferenceActivity, PreferenceInnerActivity::class.java)
            intent.putExtra(
                PreferenceInnerActivity.PREFERENCE_TRANSFER_HEADER,
                adapterView.getItemAtPosition(i) as PreferenceHeader
            )
            this@PreferenceActivity.startActivity(intent)
        }
    }

    private fun prepareHeader() {
        headers = listOf()
    }
}
