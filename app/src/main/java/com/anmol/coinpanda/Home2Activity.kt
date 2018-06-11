package com.anmol.coinpanda

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class Home2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_home2)

    }
}
