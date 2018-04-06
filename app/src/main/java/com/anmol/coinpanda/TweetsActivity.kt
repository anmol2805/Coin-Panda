package com.anmol.coinpanda

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.anmol.coinpanda.Adapters.DividerItemDecoration
import com.anmol.coinpanda.Adapters.TweetsAdapter
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Tweet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.lang.reflect.Method
import java.sql.Date

class TweetsActivity : AppCompatActivity() {
    lateinit var tweets:MutableList<Tweet>
    private var mtweetrecycler:RecyclerView?=null
    lateinit var itemClickListener : ItemClickListener
    lateinit var tweetsAdapter:TweetsAdapter
    var db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    var empty: TextView? = null
    var pgr:ProgressBar? = null
    var retry: Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweets)
        if(intent.getStringExtra("coin")!=null){
            val coin : String = intent.getStringExtra("coin")
            title = coin
            pgr = findViewById(R.id.pgr)
            val layoutManager = LinearLayoutManager(this)
            mtweetrecycler = findViewById(R.id.tweetrecycler)
            retry = findViewById(R.id.retry)
            retry?.visibility = View.GONE
            mtweetrecycler?.layoutManager = layoutManager
            mtweetrecycler?.setHasFixedSize(true)
            mtweetrecycler?.itemAnimator = DefaultItemAnimator()
            tweets = ArrayList()
            empty = findViewById(R.id.empty)
            empty?.visibility = View.GONE
            itemClickListener = object : ItemClickListener {
                override fun onItemClick(pos: Int) {
//                val url = tweets[pos].url
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse(url)
//                startActivity(intent)
                }

            }

            val bookmarks : ArrayList<String> = ArrayList()
            db.collection("users").document(auth.currentUser!!.uid).collection("bookmarks")
                    .get().addOnCompleteListener{task->
                        bookmarks.clear()
                        for (doc in task.result.documents){
                            val tweetid = doc.id
                            bookmarks.add(tweetid)
                        }
                        loadtweets(coin,bookmarks)
                        retry?.setOnClickListener{
                            loadtweets(coin,bookmarks)
                        }
                    }
        }


    }

    private fun loadtweets(coin: String, bookmarks: ArrayList<String>) {
        pgr?.visibility = View.VISIBLE
        retry?.visibility = View.GONE
        empty?.visibility = View.GONE
        val url = "http://165.227.98.190/tweets/$coin"
        val jsonObject = JsonArrayRequest(Request.Method.GET,url,null,Response.Listener {
            response ->
            tweets.clear()
            var i = 0
            while (i<response.length()){
                val doc = response.getJSONObject(i)
                val mcoin = doc.getString("coin_name")
                val coin_symbol = doc.getString("coin_symbol")
                val mtweet = doc.getString("tweet")
                val url = doc.getString("url")
                val keyword = doc.getString("keyword")
                val id = doc.getString("id")
                val dates = doc.getString("date")
                val coinpage = doc.getString("coin_handle")
                var j = 0
                var booked = false
                while (j<bookmarks.size){
                    if(id.contains(bookmarks[j])){
                        booked = true
                    }
                    j++
                }
                val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,booked,dates,"mc",coinpage)
                tweets.add(tweet)
                i++
            }
            if(!tweets.isEmpty()){
                val tweetsAdapter = TweetsAdapter(this,tweets,itemClickListener)
                tweetsAdapter.notifyDataSetChanged()
                mtweetrecycler?.adapter = tweetsAdapter
                pgr?.visibility = View.GONE
                //mtweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(applicationContext,R.drawable.item_decorator)!!))
            }
            else{
                pgr?.visibility = View.GONE
                empty?.visibility = View.VISIBLE
            }
        },Response.ErrorListener {
            retry?.visibility = View.VISIBLE
            empty?.visibility = View.VISIBLE
            empty?.text = "Network Error"
            pgr?.visibility = View.GONE

        })
        Mysingleton.getInstance(this).addToRequestqueue(jsonObject)

    }
}
