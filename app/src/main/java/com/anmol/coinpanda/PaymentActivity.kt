package com.anmol.coinpanda

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        val coin  = intent.getStringExtra("coin")
        title = coin
    }
}