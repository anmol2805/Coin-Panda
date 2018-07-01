package com.anmol.coinpanda

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityOptionsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.anmol.coinpanda.Services.TweetsdbService
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent1 = Intent(this,TweetsdbService::class.java)
        startService(intent1)
        updaterequest()
        moverequest()

        if(auth.currentUser!=null){
            val handler = Handler()
            handler.postDelayed({
                val intent = Intent(this,LoadingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            },3000)

        }
        else{
            val handler = Handler()
            handler.postDelayed({
                val intent = Intent(this,LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                //val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,findViewById(R.id.logoimage),"logotransition")
                startActivity(intent)
                finish()
            },3000)
        }

    }
    private fun updaterequest() {
        val stringRequest = StringRequest(Request.Method.GET,"https://www.cryptohype.live/update", Response.Listener { response ->
            System.out.println("responsefunction"+response)
        }, Response.ErrorListener { error->
            System.out.println(error)
        })
        Mysingleton.getInstance(this).addToRequestqueue(stringRequest)


    }
    private fun moverequest() {
        val stringRequest = StringRequest(Request.Method.GET,"https://www.cryptohype.live/move", Response.Listener { response ->
            System.out.println("responsefunction"+response)
        }, Response.ErrorListener { error->
            System.out.println(error)
        })
        Mysingleton.getInstance(this).addToRequestqueue(stringRequest)


    }

    override fun onPause() {
        super.onPause()
        finishAfterTransition()
        overridePendingTransition(R.anim.still, R.anim.slide_in_up)
    }
}
