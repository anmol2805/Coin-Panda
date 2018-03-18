package com.anmol.coinpanda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        updaterequest()
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
    private fun updaterequest() {
        val stringRequest = StringRequest(Request.Method.GET,"http://165.227.98.190/update", Response.Listener { response ->
            System.out.println(response)
        }, Response.ErrorListener { error->
            System.out.println(error)
        })
        Mysingleton.getInstance(this).addToRequestqueue(stringRequest)


    }
}
