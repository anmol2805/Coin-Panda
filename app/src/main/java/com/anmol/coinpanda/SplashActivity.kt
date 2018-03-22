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
        moverequest()
        if(auth!=null){
            val handler = Handler()
            handler.postDelayed({
                startActivity(Intent(this,HomeActivity::class.java))
            },3000)
        }
        else{
            val handler = Handler()
            handler.postDelayed({
                val intent = Intent(this,LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
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
    private fun moverequest() {
        val stringRequest = StringRequest(Request.Method.GET,"http://165.227.98.190/move", Response.Listener { response ->
            System.out.println(response)
        }, Response.ErrorListener { error->
            System.out.println(error)
        })
        Mysingleton.getInstance(this).addToRequestqueue(stringRequest)


    }
}
