package com.anmol.coinpanda

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityOptionsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.anmol.coinpanda.Helper.COL_ID
import com.anmol.coinpanda.Helper.Dbhelper
import com.anmol.coinpanda.Helper.TABLE_NAME
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.Services.IcodbService
import com.anmol.coinpanda.Services.IcomsgdbService
import com.anmol.coinpanda.Services.TweetsdbService
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class SplashActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent1 = Intent(this,IcodbService::class.java)
        startService(intent1)
        val intent2 = Intent(this,IcomsgdbService::class.java)
        startService(intent2)

        updaterequest()
        moverequest()
        val db = Dbhelper(this)
        val tweets:List<Tweet> = db.readData("SELECT * FROM $TABLE_NAME ORDER BY $COL_ID DESC")
        val tweetids = ArrayList<String>()
        for (i in tweets.indices) {
            tweetids.add(tweets[i].tweetid!!)
        }
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, "https://www.cryptohype.live/tweets", null, Response.Listener { response ->
            var c = 0
            try {
                val jsonArray = response.getJSONArray("tweets")
                val sqltweets = ArrayList<Sqltweet>()
                sqltweets.clear()


                while (c < 225) {
                    val obj = jsonArray.getJSONObject(c)
                    val id = obj.getString("tweetid")
                    val coin = obj.getString("coin_name")
                    val coin_symbol = obj.getString("coin_symbol")
                    val mtweet = obj.getString("tweet")
                    val url = obj.getString("url")
                    val keyword = obj.getString("keyword")
                    val dates = obj.getString("date")
                    val coinpage = obj.getString("coin_handle")

                    val sqltweet = Sqltweet(coin, coin_symbol, mtweet, url, keyword, id, dates, coinpage)
                    var k = 0
                    for (j in tweetids.indices) {
                        if (tweetids[j] == id) {
                            k = 1
                        }
                    }
                    if (k == 0) {
                        print("noticestatus:newfeature entry")
                        db.insertData(sqltweet)
                    } else {
                        print("noticestatus:already present")
                    }
                    println("tweetno$c")
                    c++
                }
                movetoActivity()
            } catch (e:JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener {
            movetoActivity()
            println("network error") })
        Mysingleton.getInstance(this).addToRequestqueue(jsonObjectRequest)

    }

    private fun movetoActivity() {
        if(auth.currentUser!=null){
            val intent = Intent(this,LoadingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()

        }
        else{
            val intent = Intent(this,LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            //val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,findViewById(R.id.logoimage),"logotransition")
            startActivity(intent)
            finish()

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
