package com.anmol.coinpanda

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class IconewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iconews)
        val iconame = intent.getStringExtra("iconame")
        title = iconame
    }
}
