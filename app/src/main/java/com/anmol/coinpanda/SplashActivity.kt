package com.anmol.coinpanda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if(auth!=null){
            val handler = Handler()
            handler.postDelayed({
                startActivity(Intent(this,HomeActivity::class.java))
            },3000)
        }
        else{
            val handler = Handler()
            handler.postDelayed({
                startActivity(Intent(this,LoginActivity::class.java))
            },3000)
        }

    }
}
