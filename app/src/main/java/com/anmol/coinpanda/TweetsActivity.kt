package com.anmol.coinpanda

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.anmol.coinpanda.Adapters.TweetsAdapter
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Tweet
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TweetsActivity : AppCompatActivity() {
    lateinit var tweets:MutableList<Tweet>
    private var mtweetrecycler:RecyclerView?=null
    lateinit var itemClickListener : ItemClickListener
    lateinit var tweetsAdapter:TweetsAdapter
    var db = FirebaseFirestore.getInstance()
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
        itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {
                val url = tweets[pos].url
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }

        }

        loadtweets(coin)
    }

    private fun loadtweets(coin: String) {
        db.collection("Tweets")
                .orderBy("date",Query.Direction.DESCENDING).addSnapshotListener{documentSnapshot,e->
                    tweets.clear()
                    for(doc in documentSnapshot.documents){
                        val mcoin = doc.getString("coin")
                        val coin_symbol = doc.getString("coin_symbol")
                        val mtweet = doc.getString("tweet")
                        val url = doc.getString("url")
                        if(coin_symbol.contains(coin)){
                            val tweet = Tweet(mcoin,coin_symbol,mtweet,url)
                            tweets.add(tweet)
                        }

                    }

                        if(!tweets.isEmpty()){
                            val tweetsAdapter = TweetsAdapter(this,tweets,itemClickListener)
                            mtweetrecycler?.adapter = tweetsAdapter
                        }

                }
    }
}
