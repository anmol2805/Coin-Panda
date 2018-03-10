package com.anmol.coinpanda.Fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import com.anmol.coinpanda.Adapters.TweetsAdapter
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by anmol on 2/26/2018.
 */
class dashboard : Fragment() {
    private var cointweetrecycler:RecyclerView?=null

    private lateinit var tweetselect:Switch
    lateinit var tweets : MutableList<Tweet>
    lateinit var itemClickListener : ItemClickListener
    var sedit: EditText? = null
    var srch: Button? = null
    var db = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.dashboard, container, false)
        val layoutManager = LinearLayoutManager(activity)
        cointweetrecycler = vi.findViewById(R.id.cointweetrecycler)
        tweetselect = vi.findViewById(R.id.cointweetselect)
        sedit = vi.findViewById(R.id.sc)
        srch = vi.findViewById(R.id.scb)
        cointweetrecycler?.layoutManager   = layoutManager
        cointweetrecycler?.setHasFixedSize(true)
        cointweetrecycler?.itemAnimator   = DefaultItemAnimator()
        tweets = ArrayList()
        tweetselect.isChecked = true
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH,-1)
        val stringtime = format.format(cal.time)

        System.out.println("previousdate:" + format.format(cal.time))
        itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {
                val url = tweets[pos].url
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)

            }

        }
        loadquery(null)
        sedit?.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                loadquery(p0)
            }

        })

        return vi
    }

//    private fun loadbookmarks() {
//        val bookmarks : ArrayList<String> = ArrayList()
//        db.collection("users").document("MhqeP5vqgdadnSodwzPo").collection("bookmarks").addSnapshotListener{documnentSnapshot,e->
//            bookmarks.clear()
//            for (doc in documnentSnapshot.documents){
//                val tweetid = doc.id
//                bookmarks.add(tweetid)
//            }
//            loadquery(bookmarks)
//
//        }
//
//    }

    private fun loadquery(p0: CharSequence?) {
        loaddatatweet(p0)
        tweetselect.setOnCheckedChangeListener({ _, b ->
            if (b){
                loaddatatweet(p0)
            }
            else{
                loadalldatatweet(p0)
            }
        })

    }

    private fun loadalldatatweet(p0: CharSequence?) {
        tweets.clear()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH,-1)
        val stringtime = format.format(cal.time)
        val prevtime = Timestamp.valueOf(stringtime)
        db.collection("Tweets").whereGreaterThanOrEqualTo("date",prevtime)
                .orderBy("date",Query.Direction.DESCENDING).addSnapshotListener{documentSnapshot,e->
            tweets.clear()
                    if(p0 == null){
                        for(doc in documentSnapshot.documents){
                            val booked = false
//                        var i = 0
//                        while(i<bookmarks.size){
//                            if(doc.id.contains(bookmarks[i])){
//                                booked = true
//                            }
//                            i++
//                        }
                            val id = doc.id
                            val coin = doc.getString("coin")
                            val coin_symbol = doc.getString("coin_symbol")
                            val mtweet = doc.getString("tweet")
                            val url = doc.getString("url")
                            val keyword = doc.getString("keyword")
                            val tweet = Tweet(coin,coin_symbol,mtweet,url,keyword,id,booked)
                            tweets.add(tweet)
                        }
                        if(activity!=null){
                            if(!tweets.isEmpty()){
                                val tweetsAdapter = TweetsAdapter(activity!!,tweets,itemClickListener)
                                cointweetrecycler?.adapter = tweetsAdapter
                            }
                        }
                    }
                    else{
                        for(doc in documentSnapshot.documents){
                            val booked = false
//                        var i = 0
//                        while(i<bookmarks.size){
//                            if(doc.id.contains(bookmarks[i])){
//                                booked = true
//                            }
//                            i++
//                        }
                            val id = doc.id
                            val coin = doc.getString("coin")
                            val coin_symbol = doc.getString("coin_symbol")
                            val mtweet = doc.getString("tweet")
                            val url = doc.getString("url")
                            val keyword = doc.getString("keyword")
                            if(coin.toLowerCase().contains(p0) || coin_symbol.toLowerCase().contains(p0) || mtweet.toLowerCase().contains(p0) || keyword.toLowerCase().contains(p0) ||coin.toUpperCase().contains(p0) || coin_symbol.toUpperCase().contains(p0) || mtweet.toUpperCase().contains(p0) || keyword.toUpperCase().contains(p0)){
                                val tweet = Tweet(coin,coin_symbol,mtweet,url,keyword,id,booked)
                                tweets.add(tweet)
                            }

                        }
                        if(activity!=null){
                            if(!tweets.isEmpty()){
                                val tweetsAdapter = TweetsAdapter(activity!!,tweets,itemClickListener)
                                cointweetrecycler?.adapter = tweetsAdapter
                            }
                        }
                    }

        }


    }

    private fun loaddatatweet(p0: CharSequence?) {
        tweets.clear()
        val mycoins :ArrayList<String> = ArrayList()

        db.collection("users").document("MhqeP5vqgdadnSodwzPo").collection("portfolio").addSnapshotListener{ds,er->
            mycoins.clear()
            for(docs in ds.documents){
                val name  = docs.id
                mycoins.add(name)
            }

            querymytweets(mycoins,p0)
        }
    }

    private fun querymytweets(mycoins: ArrayList<String>, p0: CharSequence?) {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH,-1)
        val stringtime = format.format(cal.time)
        val prevtime = Timestamp.valueOf(stringtime)
        db.collection("Tweets").whereGreaterThanOrEqualTo("date",prevtime).orderBy("date",Query.Direction.DESCENDING).addSnapshotListener{documentSnapshot,e->
            tweets.clear()
            if(p0 == null){
                for(doc in documentSnapshot.documents) {
                    var i = 0
                    var j = 0
                    val booked = false
//                while (j<bookmarks.size){
//                    if(doc.id.contains(bookmarks[i])){
//                        booked = true
//                    }
//                    j++
//                }
                    while (i < mycoins.size) {

                        if (doc.getString("coin_symbol").contains(mycoins[i])) {
                            val id = doc.id
                            val coin = doc.getString("coin")
                            val coin_symbol = doc.getString("coin_symbol")
                            val mtweet = doc.getString("tweet")
                            val url = doc.getString("url")
                            val keyword = doc.getString("keyword")


                            val tweet = Tweet(coin, coin_symbol, mtweet, url,keyword,id,booked)
                            tweets.add(tweet)
                        }
                        i++
                    }
                }
                if(activity!=null){
                    if(!tweets.isEmpty()){
                        val tweetsAdapter = TweetsAdapter(activity!!,tweets,itemClickListener)
                        cointweetrecycler?.adapter = tweetsAdapter
                    }
                }

            }
            else{
                for(doc in documentSnapshot.documents) {
                    var i = 0
                    var j = 0
                    val booked = false
//                while (j<bookmarks.size){
//                    if(doc.id.contains(bookmarks[i])){
//                        booked = true
//                    }
//                    j++
//                }
                    while (i < mycoins.size) {

                        if (doc.getString("coin_symbol").contains(mycoins[i])) {
                            val id = doc.id
                            val coin = doc.getString("coin")
                            val coin_symbol = doc.getString("coin_symbol")
                            val mtweet = doc.getString("tweet")
                            val url = doc.getString("url")
                            val keyword = doc.getString("keyword")
                            if (coin.toLowerCase().contains(p0) || coin_symbol.toLowerCase().contains(p0) || mtweet.toLowerCase().contains(p0) || keyword.toLowerCase().contains(p0) ||coin.toUpperCase().contains(p0) || coin_symbol.toUpperCase().contains(p0) || mtweet.toUpperCase().contains(p0) || keyword.toUpperCase().contains(p0)){
                                val tweet = Tweet(coin, coin_symbol, mtweet, url,keyword,id,booked)
                                tweets.add(tweet)
                            }


                        }
                        i++
                    }
                }
                if(activity!=null){
                    if(!tweets.isEmpty()){
                        val tweetsAdapter = TweetsAdapter(activity!!,tweets,itemClickListener)
                        cointweetrecycler?.adapter = tweetsAdapter
                    }
                }

            }

        }

    }
}