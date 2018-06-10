package com.anmol.coinpanda

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class IconewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iconews)
        val iconame = intent.getStringExtra("iconame")

        title = iconame
        val icoicon:ImageView = findViewById(R.id.icoimage)
        Glide.with(this).load(intent.getStringExtra("iconlink")).into(icoicon)
        val newstitle:TextView = findViewById(R.id.title)
        newstitle.text = intent.getStringExtra("icotitle")

    }
}
