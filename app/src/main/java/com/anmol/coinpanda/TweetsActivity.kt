package com.anmol.coinpanda

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anmol.coinpanda.Model.Tweet
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TweetsActivity : AppCompatActivity() {
    lateinit var tweets:MutableList<Tweet>
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweets)
        val coin : String = intent.getStringExtra("coin")
        title = coin
        tweets = ArrayList()

        loadtweets(coin)
    }

    private fun loadtweets(coin: String) {
        db.collection("supernode").document("coins").collection(coin).orderBy("timestamp",Query.Direction.DESCENDING)
                .addSnapshotListener{documentSnapshot, e ->
                    tweets.clear()
                    for (doc in documentSnapshot.documents){
                        System.out.println("tweet data:"+doc.data)
                        val mcoin = doc.getString("coin")
                        val mflag = doc.getBoolean("flag")
                        val mtid = doc.getString("id")
                        val mmain = doc.getString("main")
                        val mngram2 = doc.getString("ngram2")
                        val mpolarity = doc.get("polarity")
                        val msubjectivity = doc.get("subjectivity")
                        val mtweet = doc.getString("tweet")
                        val tweet = Tweet(mcoin,mflag,mtid,mmain,mngram2, mpolarity as Number?, msubjectivity as Number?,mtweet)
                        tweets.add(tweet)
                    }
                }
    }
}
