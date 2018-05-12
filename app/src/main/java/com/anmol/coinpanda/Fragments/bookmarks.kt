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
import com.anmol.coinpanda.Adapters.TweetsAdapter
import com.anmol.coinpanda.Helper.*
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.Mysingleton
import com.anmol.coinpanda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by anmol on 3/10/2018.
 */
class bookmarks : Fragment() {
    private var cointweetrecycler: RecyclerView?=null
    lateinit var tweets : MutableList<Tweet>
    lateinit var itemClickListener : ItemClickListener
    var sedit: EditText? = null
    var srch: Button? = null
    var db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    var empty: TextView? = null
    var pgr :ProgressBar?=null
    var retry:Button?=null
    var tweetsAdapter : TweetsAdapter?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.bookmarks,
                container, false)
        if(activity!=null) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            val layoutManager = LinearLayoutManager(activity)


            cointweetrecycler = vi.findViewById(R.id.cointweetrecycler)
            sedit = vi.findViewById(R.id.sc)
            srch = vi.findViewById(R.id.scb)
            empty = vi.findViewById(R.id.empty)
            pgr = vi.findViewById(R.id.pgr)
            retry = vi.findViewById(R.id.retry)
            retry?.visibility = View.GONE
            empty?.visibility = View.GONE
            cointweetrecycler?.layoutManager = layoutManager
            cointweetrecycler?.setHasFixedSize(true)
            cointweetrecycler?.itemAnimator = DefaultItemAnimator()
            tweets = ArrayList()
            val handler = Handler()
            handler.postDelayed({ loadquery(null) }, 200)

            itemClickListener = object : ItemClickListener {
                override fun onItemClick(pos: Int) {
//                val url = tweets[pos].url
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse(url)
//                startActivity(intent)

                }

            }
            
        }
        retry?.setOnClickListener{
            loadquery(null)
        }
        return vi
    }

    private fun loadquery(p0: CharSequence?) {
        tweets.clear()
        retry?.visibility = View.GONE
        empty?.visibility = View.GONE
        pgr?.visibility = View.VISIBLE
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
            val loadtweets = ArrayList<Tweet>()
            loadtweets.clear()
            val data = db.readData(dataquery)
            tweets = data
            if (!tweets.isEmpty()) {
                var i = 0
                while (i<data.size){
                    var j = 0
                    while (j<bookmarks.size){
                        if(bookmarks[j] == tweets[i].tweetid){
                            val tweet = Tweet(tweets[i].coin,tweets[i].coin_symbol,tweets[i].tweet,tweets[i].url,tweets[i].keyword,tweets[i].tweetid,true,tweets[i].dates,"mc",tweets[i].coin_symbol)
                            loadtweets.add(tweet)        
                        }
                        j++

                    }
                    
                    i++
                }
                pgr?.visibility = View.GONE
                retry?.visibility = View.GONE
                empty?.visibility = View.GONE
                tweetsAdapter = TweetsAdapter(activity!!, loadtweets, itemClickListener)
                tweetsAdapter!!.notifyDataSetChanged()
                cointweetrecycler?.adapter = tweetsAdapter
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
//            val bookmarks : ArrayList<String> = ArrayList()
//            db.collection("users").document(auth.currentUser!!.uid).collection("bookmarks")
//                    .get().addOnCompleteListener{
//                        task2->
//                        bookmarks.clear()
//                        for (doc in task2.result.documents) {
//                            val tweetid = doc.id
//                            bookmarks.add(tweetid)
//                        }
//
//
//                        if (p0 == null) {
//                            val tweetarray = response.getJSONArray("tweets")
//                            var j = 0
//                            while (j<tweetarray.length()){
//                                val doc = tweetarray.getJSONObject(j)
//                                val id = doc.getString("id")
//                                val coin = doc.getString("coin_name")
//                                val coin_symbol = doc.getString("coin_symbol")
//                                val mtweet = doc.getString("tweet")
//                                val url = doc.getString("url")
//                                val keyword = doc.getString("keyword")
//                                val dates = doc.getString("date")
//                                val coinpage = doc.getString("coin_handle")
//                                var booked = false
//                                var i = 0
//                                while(i<bookmarks.size){
//                                    if(id.contains(bookmarks[i])){
//                                        booked = true
//                                    }
//                                    i++
//                                }
//                                if(booked){
//                                    val tweet = Tweet(coin, coin_symbol, mtweet, url, keyword, id, booked, dates,"mc",coinpage)
//                                    tweets.add(tweet)
//                                }
//                                j++
//                            }
//                            if (activity != null) {
//                                if (!tweets.isEmpty()) {
//                                    pgr?.visibility = View.GONE
//                                    val tweetsAdapter = TweetsAdapter(activity!!, tweets, itemClickListener)
//                                    tweetsAdapter.notifyDataSetChanged()
//                                    cointweetrecycler?.adapter = tweetsAdapter
//                                    //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))
//
//                                }
//                                else{
//                                    pgr?.visibility = View.GONE
//                                    empty?.visibility = View.VISIBLE
//                                }
//                            }
//                            sedit?.addTextChangedListener(object : TextWatcher {
//                                override fun afterTextChanged(p0: Editable?) {
//
//                                }
//
//                                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//                                }
//
//                                override fun onTextChanged(brk: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                                    tweets.clear()
//                                    val tweetarray3 = response.getJSONArray("tweets")
//                                    var bk = 0
//                                    while (bk<tweetarray3.length()){
//                                        val doc = tweetarray.getJSONObject(bk)
//                                        val id = doc.getString("id")
//                                        val coin = doc.getString("coin_name")
//                                        val coin_symbol = doc.getString("coin_symbol")
//                                        val mtweet = doc.getString("tweet")
//                                        val url = doc.getString("url")
//                                        val keyword = doc.getString("keyword")
//                                        val dates = doc.getString("date")
//                                        val coinpage = doc.getString("coin_handle")
//                                        var booked = false
//                                        var i = 0
//                                        while(i<bookmarks.size){
//                                            if(id.contains(bookmarks[i])){
//                                                booked = true
//                                            }
//                                            i++
//                                        }
//                                        if(booked){
//                                            if(coin!=null && coin_symbol!=null && mtweet!=null && keyword!=null){
//                                                if ((coin.toLowerCase().contains(brk!!) || coin_symbol.toLowerCase().contains(brk) || mtweet.toLowerCase().contains(brk) || keyword.toLowerCase().contains(brk) || coin.toUpperCase().contains(brk) || coin_symbol.toUpperCase().contains(brk) || mtweet.toUpperCase().contains(brk) || keyword.toUpperCase().contains(brk))&& booked) {
//                                                    val tweet = Tweet(coin, coin_symbol, mtweet, url, keyword, id, booked, dates,"mc",coinpage)
//                                                    tweets.add(tweet)
//                                                }
//                                            }
//                                        }
//                                        bk++
//                                    }
//                                    if (activity != null) {
//                                        if (!tweets.isEmpty()) {
//                                            pgr?.visibility = View.GONE
//                                            val tweetsAdapter = TweetsAdapter(activity!!, tweets, itemClickListener)
//                                            tweetsAdapter.notifyDataSetChanged()
//                                            cointweetrecycler?.adapter = tweetsAdapter
//                                            //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))
//
//                                        }
//                                        else{
//                                            pgr?.visibility = View.GONE
//                                            empty?.visibility = View.VISIBLE
//                                            empty?.text = "No Results found"
//                                        }
//                                    }
//                                }
//
//                            })
//
//                        }
////                        else {
////                            val tweetarray = response.getJSONArray("tweets")
////                            var j = 0
////                            while (j<tweetarray.length()){
////                                val doc = tweetarray.getJSONObject(j)
////                                val id = doc.getString("id")
////                                val coin = doc.getString("coin_name")
////                                val coin_symbol = doc.getString("coin_symbol")
////                                val mtweet = doc.getString("tweet")
////                                val url = doc.getString("url")
////                                val keyword = doc.getString("keyword")
////                                val dates = doc.getString("date")
////                                val coinpage = doc.getString("coin_handle")
////                                var booked = false
////                                var i = 0
////                                while(i<bookmarks.size){
////                                    if(id.contains(bookmarks[i])){
////                                        booked = true
////                                    }
////                                    i++
////                                }
////                                if(booked){
////                                    if(coin!=null && coin_symbol!=null && mtweet!=null && keyword!=null){
////                                        if ((coin.toLowerCase().contains(p0) || coin_symbol.toLowerCase().contains(p0) || mtweet.toLowerCase().contains(p0) || keyword.toLowerCase().contains(p0) || coin.toUpperCase().contains(p0) || coin_symbol.toUpperCase().contains(p0) || mtweet.toUpperCase().contains(p0) || keyword.toUpperCase().contains(p0))&& booked) {
////                                            val tweet = Tweet(coin, coin_symbol, mtweet, url, keyword, id, booked, dates,"mc",coinpage)
////                                            tweets.add(tweet)
////                                        }
////                                    }
////                                }
////                                j++
////                            }
////                            if (activity != null) {
////                                if (!tweets.isEmpty()) {
////                                    pgr?.visibility = View.GONE
////                                    val tweetsAdapter = TweetsAdapter(activity!!, tweets, itemClickListener)
////                                    tweetsAdapter.notifyDataSetChanged()
////                                    cointweetrecycler?.adapter = tweetsAdapter
////                                    //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))
////
////                                }
////                                else{
////                                    pgr?.visibility = View.GONE
////                                    empty?.visibility = View.VISIBLE
////                                    empty?.text = "No results found"
////
////                                }
////                            }
////
////                        }
//                    }
//        },Response.ErrorListener {
//            retry?.visibility = View.VISIBLE
//            empty?.visibility = View.VISIBLE
//            empty?.text = "Network Error"
//            pgr?.visibility = View.GONE
//        })
//        if(activity!=null){
//            Mysingleton.getInstance(activity).addToRequestqueue(jsonobjectrequest)
//        }




    }
}