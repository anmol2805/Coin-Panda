package com.anmol.coinpanda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.anmol.coinpanda.Helper.*
import com.anmol.coinpanda.Model.Sqlcoin
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Services.BookmarksdbService
import com.anmol.coinpanda.Services.TweetsdbService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.ArrayList

class LoadingActivity : AppCompatActivity() {
    var retry:Button?=null
    var loadpgr:ProgressBar?=null
    var pw:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        retry = findViewById<Button>(R.id.retry)
        loadpgr = findViewById<ProgressBar>(R.id.loadpgr)
        pw = findViewById<TextView>(R.id.pw)
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
        }
        else{
            startloading()
            retry?.setOnClickListener({
                startloading()
            })
        }


    }

    private fun startloading() {
        retry?.visibility = View.GONE
        loadpgr?.visibility = View.VISIBLE
        pw?.visibility = View.VISIBLE
        pw?.text = "Please Wait!!!"
        val dcb = Dbcoinshelper(this)
        val dtb = Dbhelper(this)
        val dbb = Dbbookshelper(this)
        val data = dcb.readData()
        val dataquery = "Select * from $TABLE_NAME ORDER BY $COL_ID DESC"
        val tweetdata = dtb.readData(dataquery)
        val bookmarkdata = dbb.readbook()
        if(data.isEmpty()){
            val db = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            db.collection("users").document(auth.currentUser!!.uid).collection("portfolio").get().addOnCompleteListener {
                task ->
                val documentSnapshot = task.result
                val s = documentSnapshot.size()
                if(s!=0){
                    for(doc in documentSnapshot){
                        val coinname = doc.getString("coin_name")
                        val coinsymbol = doc.id
                        val coinpage = doc.getString("coinPage")
                        val sqlcoin = Sqlcoin(coinname,coinsymbol,coinpage)
                        dcb.insertData(sqlcoin)
                    }
                    startloading()

                }
                else{
                    val intent = Intent(this,TweetsdbService::class.java)
                    startService(intent)
                    val intent1 = Intent(this,BookmarksdbService::class.java)
                    startService(intent1)
                    val intent2 = Intent(this,HomeActivity::class.java)
                    startActivity(intent2)
                }

            }
        }
        else{
            if(tweetdata.isEmpty()){
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, "http://165.227.98.190/tweets", null, Response.Listener { response ->
                    var c = 0
                    try {
                        val jsonArray = response.getJSONArray("tweets")
                        val sqltweets = ArrayList<Sqltweet>()
                        sqltweets.clear()


                        while (c < 150) {
                            val obj = jsonArray.getJSONObject(c)
                            val id = obj.getString("id")
                            val coin = obj.getString("coin_name")
                            val coin_symbol = obj.getString("coin_symbol")
                            val mtweet = obj.getString("tweet")
                            val url = obj.getString("url")
                            val keyword = obj.getString("keyword")
                            val dates = obj.getString("date")
                            val coinpage = obj.getString("coin_handle")

                            val sqltweet = Sqltweet(coin, coin_symbol, mtweet, url, keyword, id, dates, coinpage)
                            val db = Dbhelper(baseContext)
                            db.insertData(sqltweet)
                            println("tweetno$c")
                            c++
                        }
                        startloading()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
                    println("network error")
                    retry?.visibility = View.VISIBLE
                    loadpgr?.visibility = View.GONE
                    pw?.text = "Network Error"
                })
                Mysingleton.getInstance(baseContext).addToRequestqueue(jsonObjectRequest)
            }
            else{
                if(bookmarkdata.isEmpty()){
                    val db = FirebaseFirestore.getInstance()
                    val auth = FirebaseAuth.getInstance()
                    db.collection("users").document(auth.currentUser!!.uid).collection("bookmarks").get().addOnCompleteListener {
                        task ->
                        for (doc in task.result.documents){
                            val tweetid = doc.id
                            dbb.insertData(tweetid)
                        }

                        startActivity(Intent(this,HomeActivity::class.java))
                    }
                }
                else{
                    startActivity(Intent(this,HomeActivity::class.java))
                }
            }
        }
    }


}
