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
import com.anmol.coinpanda.Model.Value
import com.anmol.coinpanda.Services.BookmarksdbService
import com.anmol.coinpanda.Services.TweetsdbService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.ArrayList
import java.util.HashMap

class LoadingActivity : AppCompatActivity() {
    var retry: Button? = null
    var loadpgr: ProgressBar? = null
    var pw: TextView? = null
    var db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val messaging = FirebaseMessaging.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        retry = findViewById<Button>(R.id.retry)
        loadpgr = findViewById<ProgressBar>(R.id.loadpgr)
        pw = findViewById<TextView>(R.id.pw)
        pw?.text = "Welcome to Cryptohype...!!!"
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            val databse = Dbtopicshelper(this)
            val value = Value("secret", 0)
            databse.insertData(value)
            val datavalue = databse.readData()
            System.out.println("procedure data" + datavalue[0].keyvalue)
            if (datavalue[0].keyvalue == 0) {
                System.out.println("procedure 0")
                val databaseReference = FirebaseDatabase.getInstance().reference
                databaseReference.child("database").child(auth.currentUser!!.uid).child("portfolio").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        if (p0!!.exists()) {
                            for (data in p0.children) {
                                val coinname = data.child("coin").value.toString()
                                val coinsymbol = data.child("coin_symbol").value.toString()
                                topicsearch(0, coinsymbol, coinname)

                            }
                            val valuenew = Value("secret", 1)
                            databse.updatedata(valuenew)

                        }
                    }

                })

            } else {
                System.out.println("procedure 1")
            }
            startloading()
            retry?.setOnClickListener {
                pw?.text = "Please Wait!!!"
                startloading()

            }
        }


    }

    private fun startloading() {
        retry?.visibility = View.GONE
        loadpgr?.visibility = View.VISIBLE
        pw?.visibility = View.VISIBLE

        val dcb = Dbcoinshelper(this)
        val dtb = Dbhelper(this)
        val dbb = Dbbookshelper(this)
        val data = dcb.readData()
        val dataquery = "Select * from $TABLE_NAME ORDER BY $COL_ID DESC"
        val tweetdata = dtb.readData(dataquery)
        val bookmarkdata = dbb.readbook()
        pw?.text = "Please Wait!!!"
        if (data.isEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val databaseReference = FirebaseDatabase.getInstance().reference
            databaseReference.child("database").child(auth.currentUser!!.uid).child("portfolio").addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    retry?.visibility = View.VISIBLE
                    loadpgr?.visibility = View.GONE
                    pw?.text = "Network Error"
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    pw?.text = "Please Wait!!!"
                    if(p0!!.exists()){
                        for (doc in p0.children){
                            val coinname = doc.child("coin").value.toString()
                            val coinsymbol = doc.child("coin_symbol").value.toString()
                            val coinpage = doc.child("coinpage").value.toString()
                            topicsearch(0, coinsymbol, coinname)
                            val sqlcoin = Sqlcoin(coinname, coinsymbol, coinpage)
                            dcb.insertData(sqlcoin)
                        }
                        startloading()
                    }
                    else{
                        val intent = Intent(this@LoadingActivity, TweetsdbService::class.java)
                        startService(intent)
                        val intent2 = Intent(this@LoadingActivity, Home2Activity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent2)
                        finish()
                    }
                }

            })

        } else {
            if (tweetdata.isEmpty()) {
                pw?.text = "We're setting up few things for you..."
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, "https://www.cryptohype.live/tweets", null, Response.Listener { response ->
                    pw?.text = "We're almost done...!"
                    var c = 0
                    try {
                        val jsonArray = response.getJSONArray("tweets")
                        val sqltweets = ArrayList<Sqltweet>()
                        sqltweets.clear()


                        while (c < 150) {
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
                            val db = Dbhelper(baseContext)
                            db.insertData(sqltweet)
                            println("tweetno$c")
                            c++
                        }
                        val intent = Intent(this, Home2Activity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
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
            } else {
                val intent = Intent(this, Home2Activity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()

            }
        }
    }

    private fun topicsearch(i: Int, coinname: String?, coin: String?) {
        val databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference!!.child("topics").child(coinname + i.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.exists()) {
                    val count: Long = p0.child("count").value as Long
                    if (count > 990) {
                        topicsearch(i + 1, coinname, coin)
                    } else {
                        val map = HashMap<String, Any>()
                        map["notify"] = true
                        map["coinname"] = coin!!
                        databaseReference.child("database").child(auth.currentUser!!.uid).child("topics").child(coinname + i.toString()).setValue(map)
                                .addOnCompleteListener {
                                    messaging.subscribeToTopic(coinname + i.toString())
                                    val countmap = HashMap<String, Any>()
                                    countmap["count"] = count + 1
                                    countmap["coin_symbol"] = coinname.toString()
                                    databaseReference.child("topics").child(coinname + i.toString()).setValue(countmap)
                                }
                    }
                } else {
                    val map = HashMap<String, Any>()
                    map["notify"] = true
                    map["coinname"] = coin!!
                    databaseReference.child("database").child(auth.currentUser!!.uid).child("topics").child(coinname + i.toString())
                            .setValue(map).addOnSuccessListener {
                                messaging.subscribeToTopic(coinname + i.toString())
                                val count = HashMap<String, Any>()
                                count["count"] = 1
                                count["coin_symbol"] = coinname.toString()
                                databaseReference.child("topics").child(coinname + i.toString()).setValue(count)
                            }
                }
            }

        })


    }
}