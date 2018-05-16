package com.anmol.coinpanda.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.anmol.coinpanda.Adapters.DividerItemDecoration
import com.anmol.coinpanda.Adapters.KeywordAdapter
import com.anmol.coinpanda.Adapters.TweetsAdapter
import com.anmol.coinpanda.Helper.*
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.Mysingleton
import com.anmol.coinpanda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.tweetrow.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by anmol on 3/11/2018.
 */
class mycoins : Fragment(){
    private var cointweetrecycler: RecyclerView?=null
    private var keywordrecycler: RecyclerView?=null
    var keywords:ArrayList<String>?=null
    private lateinit var tweetselect: Switch
    lateinit var tweets : MutableList<Tweet>
    lateinit var itemClickListener : ItemClickListener
    var sedit: EditText? = null
    var srch: Button? = null
    var db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    var empty: TextView? = null
    lateinit var keyClickListener : ItemClickListener
    var pgr :ProgressBar?=null
    var retry:Button?=null
    var tweetsAdapter : TweetsAdapter?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.mycoins, container, false)
        if(activity!=null){
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            val layoutManager = LinearLayoutManager(activity)
            cointweetrecycler = vi.findViewById(R.id.cointweetrecycler)
            keywordrecycler = vi.findViewById(R.id.keywordrecycler)
            sedit = vi.findViewById(R.id.sc)
            srch = vi.findViewById(R.id.scb)
            empty = vi.findViewById(R.id.empty)
            retry = vi.findViewById(R.id.retry)
            retry?.visibility = View.GONE
            pgr = vi.findViewById(R.id.pgr)
            keywordrecycler?.setHasFixedSize(true)
            keywordrecycler?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            empty?.visibility = View.GONE
            cointweetrecycler?.layoutManager   = layoutManager
            cointweetrecycler?.setHasFixedSize(true)
            cointweetrecycler?.itemAnimator   = DefaultItemAnimator()
            tweets = ArrayList()
            keywords = ArrayList()
            keywords?.add("win")
            keywords?.add("partnership")
            keywords?.add("listing")
            keywords?.add("mainnet")
            keywords?.add("announcement")
            keywords?.add("exchange")
            keywords?.add("beta")
            keywords?.add("collaboration")
            keywords?.add("airdrop")
            keywords?.add("release")
            keywords?.add("government")
            keywords?.add("update")
            keywords?.add("association")
            keywords?.add("achievement")
            keywordrecycler?.itemAnimator = DefaultItemAnimator()
            val handler =  Handler()
            handler.postDelayed({
                loadquery(null)
            },200)

            itemClickListener = object : ItemClickListener {
                override fun onItemClick(pos: Int) {


                }

            }
            keyClickListener = object :ItemClickListener{
                override fun onItemClick(pos: Int) {
                    System.out.println("clicked" + keywords!![pos])
                    sedit?.setText(keywords!![pos])

                }
            }
            if(activity!=null){
                val keywordAdapter = KeywordAdapter(activity!!, keywords!!,keyClickListener)
                keywordAdapter.notifyDataSetChanged()
                keywordrecycler?.adapter = keywordAdapter
            }

        }

        retry?.setOnClickListener{
            loadquery(null)
        }

        return vi
    }

    private fun loadquery(p0: CharSequence?) {
        pgr?.visibility = View.VISIBLE
        retry?.visibility = View.GONE
        empty?.visibility = View.GONE
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH,-1)
        val stringtime = format.format(cal.time)
        val prevtime = Timestamp.valueOf(stringtime)
        if(activity!=null){
            val db = Dbhelper(activity!!)
            val dataquery = "Select * from $TABLE_NAME ORDER BY $COL_ID DESC"
            var bookmarks = ArrayList<String>()
            val dbb = Dbbookshelper(activity!!)
            bookmarks = dbb.readbook()
            val dcb = Dbcoinshelper(activity!!)
            var coins :MutableList<Allcoin> = ArrayList()
            coins = dcb.readData()
            val loadtweets = ArrayList<Tweet>()
            loadtweets.clear()
            val data = db.readData(dataquery)
            tweets = data
            if (!tweets.isEmpty()) {
                var i = 0
                while (i<data.size){
                    var booked = false
                    var j = 0
                    while (j<bookmarks.size){
                        if(bookmarks[j] == tweets[i].tweetid){
                            booked = true
                        }
                        j++

                    }
                    var k = 0
                    while (k<coins.size){
                        if(coins[k].coinname == tweets[i].coin_symbol){
                            val tweet = Tweet(tweets[i].coin,tweets[i].coin_symbol,tweets[i].tweet,tweets[i].url,tweets[i].keyword,tweets[i].tweetid,booked,tweets[i].dates,"mc",tweets[i].coinpage)
                            loadtweets.add(tweet)        
                        }
                        k++
                    }
                    
                    i++
                }
                if (!loadtweets.isEmpty()){
                    pgr?.visibility = View.GONE
                    retry?.visibility = View.GONE
                    empty?.visibility = View.GONE
                    tweetsAdapter = TweetsAdapter(activity!!, loadtweets, itemClickListener)
                    tweetsAdapter!!.notifyDataSetChanged()
                    cointweetrecycler?.adapter = tweetsAdapter
                }
                else{
                    pgr?.visibility = View.GONE
                    empty?.visibility = View.VISIBLE
                }
                //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))


            }
            else{
                pgr?.visibility = View.GONE
                retry?.visibility = View.VISIBLE
                empty?.visibility = View.VISIBLE
            }

            sedit?.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(booktext: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    tweets.clear()
                    var b=0
                    while(b<loadtweets.size) {
                        if (loadtweets[b].coin!!.toLowerCase().contains(booktext!!) || loadtweets[b].coin_symbol!!.toLowerCase().contains(booktext) || loadtweets[b].tweet!!.toLowerCase().contains(booktext) || loadtweets[b].keyword!!.toLowerCase().contains(booktext) || loadtweets[b].coin!!.toUpperCase().contains(booktext) || loadtweets[b].coin_symbol!!.toUpperCase().contains(booktext) || loadtweets[b].tweet!!.toUpperCase().contains(booktext) || loadtweets[b].keyword!!.toUpperCase().contains(booktext)) {
                            tweets.add(loadtweets[b])
                        }
                        b++

                    }
                    if(activity!=null){
                                                    if(!tweets.isEmpty()){
                                                        pgr?.visibility = View.GONE
                                                        System.out.println("logging:$tweets")
                                                        val tweetsAdapter = TweetsAdapter(activity!!,tweets,itemClickListener)
                                                        tweetsAdapter.notifyDataSetChanged()
                                                        cointweetrecycler?.adapter = tweetsAdapter
                                                        empty?.visibility = View.GONE
                                                        //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))
                                                    }
                                                    else{
                                                        pgr?.visibility = View.GONE
                                                        empty?.visibility = View.VISIBLE
                                                        empty?.text = "No Results found"
                                                    }
                                                }
                }

            })
        }

//        val jsonobjectrequest = JsonObjectRequest(Request.Method.GET,"http://165.227.98.190/tweets",null, Response.Listener {
//            response ->
//            tweets.clear()
//            val mycoins :ArrayList<String> = ArrayList()
//            db.collection("users").document(auth.currentUser!!.uid).collection("portfolio")
//                    .get().addOnCompleteListener{ds ->
//                        mycoins.clear()
//                        for(docs in ds.result.documents){
//                            val name  = docs.id
//                            mycoins.add(name)
//                        }
//                        val bookmarks : ArrayList<String> = ArrayList()
//                        db.collection("users").document(auth.currentUser!!.uid).collection("bookmarks")
//                                .get().addOnCompleteListener{ds2->
//                                    bookmarks.clear()
//                                    for (doc in ds2.result.documents){
//                                        val tweetid = doc.id
//                                        bookmarks.add(tweetid)
//                                    }
//                                    if (p0 == null){
//
//
//                                        val tweetarray = response.getJSONArray("tweets")
//                                        var i = 0
//                                        while (i<tweetarray.length()){
//                                            val doc = tweetarray.getJSONObject(i)
//                                            val id = doc.getString("id")
//                                            val coin = doc.getString("coin_name")
//                                            val coin_symbol = doc.getString("coin_symbol")
//                                            val mtweet = doc.getString("tweet")
//                                            val url = doc.getString("url")
//                                            val keyword = doc.getString("keyword")
//                                            val dates = doc.getString("date")
//                                            val coinpage = doc.getString("coin_handle")
//                                            var k = 0
//                                            var booked = false
//                                            while (k< bookmarks.size){
//                                                if(id.contains(bookmarks[k])){
//                                                    booked = true
//                                                }
//                                                k++
//                                            }
//                                            var j = 0
//                                            while (j<mycoins.size){
//                                                if (mycoins[j] == coin_symbol) {
//                                                    val tweet = Tweet(coin, coin_symbol, mtweet, url,keyword,id,booked,dates,"mc",coinpage)
//                                                    tweets.add(tweet)
//                                                }
//                                                j++
//
//                                            }
//                                            i++
//                                        }
//                                        if(activity!=null){
//                                            if(!tweets.isEmpty()){
//                                                pgr?.visibility = View.GONE
//                                                System.out.println("logging:$tweets")
//                                                val tweetsAdapter = TweetsAdapter(activity!!,tweets,itemClickListener)
//                                                tweetsAdapter.notifyDataSetChanged()
//                                                cointweetrecycler?.adapter = tweetsAdapter
//                                                empty?.visibility = View.GONE
//                                                retry?.visibility = View.GONE
//                                                //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))
//                                            }
//                                            else{
//                                                pgr?.visibility = View.GONE
//                                                empty?.visibility = View.VISIBLE
//                                            }
//                                        }
//                                        sedit?.addTextChangedListener(object : TextWatcher {
//                                            override fun afterTextChanged(p0: Editable?) {
//
//                                            }
//
//                                            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//                                            }
//
//                                            override fun onTextChanged(booktext: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                                                tweets.clear()
//                                                val tweetarray2 = response.getJSONArray("tweets")
//                                                var b = 0
//                                                while (b<tweetarray2.length()){
//                                                    val doc = tweetarray.getJSONObject(b)
//                                                    val id = doc.getString("id")
//                                                    val coin = doc.getString("coin_name")
//                                                    val coin_symbol = doc.getString("coin_symbol")
//                                                    val mtweet = doc.getString("tweet")
//                                                    val url = doc.getString("url")
//                                                    val keyword = doc.getString("keyword")
//                                                    val dates = doc.getString("date")
//                                                    val coinpage = doc.getString("coin_handle")
//                                                    var k = 0
//                                                    var booked = false
//                                                    while (k< bookmarks.size){
//                                                        if(id.contains(bookmarks[k])){
//                                                            booked = true
//                                                        }
//                                                        k++
//                                                    }
//                                                    var j = 0
//                                                    while (j<mycoins.size){
//                                                        if (mycoins[j] == coin_symbol) {
//                                                            if (coin.toLowerCase().contains(booktext!!) || coin_symbol.toLowerCase().contains(booktext) || mtweet.toLowerCase().contains(booktext) || keyword.toLowerCase().contains(booktext) ||coin.toUpperCase().contains(booktext) || coin_symbol.toUpperCase().contains(booktext) || mtweet.toUpperCase().contains(booktext) || keyword.toUpperCase().contains(booktext)){
//                                                                val tweet = Tweet(coin, coin_symbol, mtweet, url,keyword,id,booked,dates,"mc",coinpage)
//                                                                tweets.add(tweet)
//                                                            }
//                                                        }
//                                                        j++
//
//                                                    }
//                                                    b++
//                                                }
//                                                if(activity!=null){
//                                                    if(!tweets.isEmpty()){
//                                                        pgr?.visibility = View.GONE
//                                                        System.out.println("logging:$tweets")
//                                                        val tweetsAdapter = TweetsAdapter(activity!!,tweets,itemClickListener)
//                                                        tweetsAdapter.notifyDataSetChanged()
//                                                        cointweetrecycler?.adapter = tweetsAdapter
//                                                        empty?.visibility = View.GONE
//                                                        //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))
//                                                    }
//                                                    else{
//                                                        pgr?.visibility = View.GONE
//                                                        empty?.visibility = View.VISIBLE
//                                                        empty?.text = "No Results found"
//                                                    }
//                                                }
//                                            }
//
//                                        })
//
//                                    }
////                                    else{
////
////                                        val tweetarray = response.getJSONArray("tweets")
////                                        var i = 0
////                                        while (i<tweetarray.length()){
////                                            val doc = tweetarray.getJSONObject(i)
////                                            val id = doc.getString("id")
////                                            val coin = doc.getString("coin_name")
////                                            val coin_symbol = doc.getString("coin_symbol")
////                                            val mtweet = doc.getString("tweet")
////                                            val url = doc.getString("url")
////                                            val keyword = doc.getString("keyword")
////                                            val dates = doc.getString("date")
////                                            val coinpage = doc.getString("coin_handle")
////                                            var k = 0
////                                            var booked = false
////                                            while (k< bookmarks.size){
////                                                if(id.contains(bookmarks[k])){
////                                                    booked = true
////                                                }
////                                                k++
////                                            }
////                                            var j = 0
////                                            while (j<mycoins.size){
////                                                if (mycoins[j] == coin_symbol) {
////                                                    if (coin.toLowerCase().contains(p0) || coin_symbol.toLowerCase().contains(p0) || mtweet.toLowerCase().contains(p0) || keyword.toLowerCase().contains(p0) ||coin.toUpperCase().contains(p0) || coin_symbol.toUpperCase().contains(p0) || mtweet.toUpperCase().contains(p0) || keyword.toUpperCase().contains(p0)){
////                                                        val tweet = Tweet(coin, coin_symbol, mtweet, url,keyword,id,booked,dates,"mc",coinpage)
////                                                        tweets.add(tweet)
////                                                    }
////                                                }
////                                                j++
////
////                                            }
////
////                                            i++
////                                        }
////                                        if(activity!=null){
////                                            if(!tweets.isEmpty()){
////                                                pgr?.visibility = View.GONE
////                                                System.out.println("logging:$tweets")
////                                                val tweetsAdapter = TweetsAdapter(activity!!,tweets,itemClickListener)
////                                                tweetsAdapter.notifyDataSetChanged()
////                                                cointweetrecycler?.adapter = tweetsAdapter
////                                                empty?.visibility = View.GONE
////                                                //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))
////                                            }
////                                            else{
////                                                pgr?.visibility = View.GONE
////                                                empty?.text = "No Results found"
////                                                empty?.visibility = View.VISIBLE
////                                            }
////                                        }
////                                    }
//
//                                }
//
//
//                    }
//
//
//        }, Response.ErrorListener {
//            retry?.visibility = View.VISIBLE
//            empty?.visibility = View.VISIBLE
//            empty?.text = "Network Error"
//            pgr?.visibility = View.GONE
//            //Toast.makeText(activity,"Network Error",Toast.LENGTH_LONG).show()
//        })
//        if(activity!=null){
//            Mysingleton.getInstance(activity).addToRequestqueue(jsonobjectrequest)
//        }

//        db.collection("Tweets").whereGreaterThanOrEqualTo("date",prevtime).orderBy("date", Query.Direction.DESCENDING)
//                .get().addOnCompleteListener{ds1->
//                    tweets.clear()
//
//                }

    }
}
