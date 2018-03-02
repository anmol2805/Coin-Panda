package com.anmol.coinpanda

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView

class AddToPortfolioActivity : AppCompatActivity() {

    private var addcoinrecycler:RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_portfolio)
        addcoinrecycler = findViewById(R.id.addcoinrecycler)
    }
}
