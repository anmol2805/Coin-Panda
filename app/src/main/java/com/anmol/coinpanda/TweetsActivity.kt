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
import android.widget.TextView
import com.anmol.coinpanda.Adapters.DividerItemDecoration
import com.anmol.coinpanda.Adapters.TweetsAdapter
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Tweet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.sql.Date

class TweetsActivity : AppCompatActivity() {
    lateinit var tweets:MutableList<Tweet>
    private var mtweetrecycler:RecyclerView?=null
    lateinit var itemClickListener : ItemClickListener
    lateinit var tweetsAdapter:TweetsAdapter
    var db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    var empty: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweets)
        val coin : String = intent.getStringExtra("coin")
        title = coin
        val layoutManager = LinearLayoutManager(this)
        mtweetrecycler = findViewById(R.id.tweetrecycler)
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
        db.collection("users").document(auth.currentUser!!.uid).collection("bookmarks").addSnapshotListener{documnentSnapshot,e->
            bookmarks.clear()
            for (doc in documnentSnapshot.documents){
                val tweetid = doc.id
                bookmarks.add(tweetid)
            }
            loadtweets(coin,bookmarks)
        }

    }

    private fun loadtweets(coin: String, bookmarks: ArrayList<String>) {
        db.collection("Tweets")
                .orderBy("date",Query.Direction.DESCENDING).addSnapshotListener{documentSnapshot,e->
                    tweets.clear()
                    for(doc in documentSnapshot.documents){
                        var i = 0
                        var booked = false
                        while (i<bookmarks.size){
                            if(doc.id.contains(bookmarks[i])){
                                booked = true
                            }
                            i++
                        }
                        val mcoin = doc.getString("coin")
                        val coin_symbol = doc.getString("coin_symbol")
                        val mtweet = doc.getString("tweet")
                        val url = doc.getString("url")
                        val keyword = doc.getString("keyword")
                        val id = doc.id
                        val dates = doc.getString("dates")
                        if(coin_symbol.contains(coin)){
                            val tweet = Tweet(mcoin,coin_symbol,mtweet,url,keyword,id,booked,dates,"mc")
                            tweets.add(tweet)
                        }

                    }

                        if(!tweets.isEmpty()){
                            val tweetsAdapter = TweetsAdapter(this,tweets,itemClickListener)
                            mtweetrecycler?.adapter = tweetsAdapter
                            //mtweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(applicationContext,R.drawable.item_decorator)!!))
                        }
                        else{
                            empty?.visibility = View.VISIBLE
                        }

                }
    }
}
