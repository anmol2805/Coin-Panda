package com.anmol.coinpanda

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class TweetsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweets)
        val coin : String = intent.getStringExtra("coin")
        title = coin
        loadtweets(coin)
    }

    private fun loadtweets(coin: String) {
            
    }
}
