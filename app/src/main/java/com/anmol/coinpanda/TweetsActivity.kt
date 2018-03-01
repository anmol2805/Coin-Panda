package com.anmol.coinpanda

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class TweetsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweets)
        title = intent.getStringExtra("coin")
    }
}
