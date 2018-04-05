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
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.Mysingleton
import com.anmol.coinpanda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.request.*
import kotlinx.android.synthetic.main.tweetrow.*
import org.json.JSONArray
import java.lang.reflect.Method
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by anmol on 3/11/2018.
 */
class allcoins : Fragment(){
    private var cointweetrecycler: RecyclerView?=null
    private var keywordrecycler: RecyclerView?=null
    var keywords:ArrayList<String>?=null
    lateinit var tweets : MutableList<Tweet>
    lateinit var itemClickListener : ItemClickListener
    lateinit var keyClickListener : ItemClickListener
    var retry:Button?=null
    var sedit: EditText? = null
    var srch: Button? = null
    var db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    var tweetsAdapter : TweetsAdapter?=null
    var pgr:ProgressBar?=null
    var empty:ImageView?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.allcoins, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val layoutManager = LinearLayoutManager(activity)
        cointweetrecycler = vi.findViewById(R.id.cointweetrecycler)
        pgr = vi.findViewById(R.id.pgr)
        empty = vi.findViewById(R.id.empty)
        retry = vi.findViewById(R.id.retry)
        retry?.visibility = View.GONE
        empty?.visibility = View.GONE
//        keywordrecycler = vi.findViewById(R.id.keywordrecycler)
//        sedit = vi.findViewById(R.id.sc)
//        srch = vi.findViewById(R.id.scb)
//        keywordrecycler?.setHasFixedSize(true)
//        keywordrecycler?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        cointweetrecycler?.layoutManager   = layoutManager
        cointweetrecycler?.setHasFixedSize(true)
        cointweetrecycler?.itemAnimator   = DefaultItemAnimator()
//        keywordrecycler?.itemAnimator = DefaultItemAnimator()
        tweets = ArrayList()
//        keywords = ArrayList()
//        keywords?.add("win")
//        keywords?.add("partnership")
//        keywords?.add("listing")
//        keywords?.add("mainnet")
//        keywords?.add("announcement")
//        keywords?.add("exchange")
//        keywords?.add("beta")
//        keywords?.add("collaboration")
//        keywords?.add("airdrop")
//        keywords?.add("release")
//        keywords?.add("government")
//        keywords?.add("update")
//        keywords?.add("association")
//        keywords?.add("achievement")
        val handler = Handler()
        handler.postDelayed({
            loadquery(null)
        },200)

        itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {

                //loadquery(keywords!![pos])
//                sedit?.setText(keywords!![pos])

            }

        }

        keyClickListener = object :ItemClickListener{
            override fun onItemClick(pos: Int) {
            }
        }
//        if(activity!=null){
//            val keywordAdapter = KeywordAdapter(activity!!, keywords!!,itemClickListener)
//            keywordrecycler?.adapter = keywordAdapter
//        }
//        sedit?.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                loadquery(p0)
//            }
//
//        })
            retry?.setOnClickListener{
                loadquery(null)
            }
            return vi
    }

    private fun loadquery(p0: CharSequence?) {
        pgr?.visibility = View.VISIBLE
        retry?.visibility = View.GONE
        empty?.visibility = View.GONE
        tweets.clear()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH,-1)
        val stringtime = format.format(cal.time)
        val prevtime = Timestamp.valueOf(stringtime)
        val jsonobjectrequest = JsonObjectRequest(Request.Method.GET,"http://165.227.98.190/tweets",null,
                Response.Listener { response ->

                    var c = 0
                    val jsonArray = response.getJSONArray("tweets")
                    tweets.clear()

                    while (c<jsonArray.length()){
                                val obj = jsonArray.getJSONObject(c)
                                                                val id = obj.getString("id")
                                val coin = obj.getString("coin_name")
                                val coin_symbol = obj.getString("coin_symbol")
                                val mtweet = obj.getString("tweet")
                                val url = obj.getString("url")
                                val keyword = obj.getString("keyword")
                                val dates = obj.getString("date")
                                val coinpage = obj.getString("coin_handle")
                                val tweet = Tweet(coin, coin_symbol, mtweet, url, keyword, id, false, dates,"ac",coinpage)
                                tweets.add(tweet)
                                c++
                            }
                    if (activity != null) {
                                if (!tweets.isEmpty()) {
                                    pgr?.visibility = View.GONE
                                    retry?.visibility = View.GONE
                                    empty?.visibility = View.GONE
                                    tweetsAdapter = TweetsAdapter(activity!!, tweets, itemClickListener)
                                    tweetsAdapter!!.notifyDataSetChanged()
                                    cointweetrecycler?.adapter = tweetsAdapter
                                    //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))

                                }
                                else{
                                    retry?.visibility = View.VISIBLE
                                    empty?.visibility = View.VISIBLE
                                }
                            }





        }, Response.ErrorListener {error ->
            System.out.println("error:"+error.message)
            empty?.visibility = View.VISIBLE
            retry?.visibility = View.VISIBLE
            pgr?.visibility = View.GONE
            if(activity!=null){
                Toast.makeText(activity,"Network Error",Toast.LENGTH_LONG).show()

            }


        })
        if(activity!=null){
            Mysingleton.getInstance(activity).addToRequestqueue(jsonobjectrequest)
        }

//        db.collection("Tweets").whereGreaterThanOrEqualTo("date",prevtime)
//                .orderBy("date", Query.Direction.DESCENDING).addSnapshotListener{ documentSnapshot, e->
//            tweets.clear()
//                    val bookmarks : ArrayList<String> = ArrayList()
//                    db.collection("users").document(auth.currentUser!!.uid).collection("bookmarks").addSnapshotListener { documnentSnapshot, e ->
//                        bookmarks.clear()
//                        tweets.clear()
//                        for (doc in documnentSnapshot.documents) {
//                            val tweetid = doc.id
//                            bookmarks.add(tweetid)
//                        }
//
//
//                        if (p0 == null) {
//                            for (doc in documentSnapshot.documents) {
//                                var booked = false
//                                var i = 0
//                                while(i<bookmarks.size){
//                                    if(doc.id.contains(bookmarks[i])){
//                                    booked = true
//                                }
//                                i++
//                                }
//                                val id = doc.id
//                                val coin = doc.getString("coin")
//                                val coin_symbol = doc.getString("coin_symbol")
//                                val mtweet = doc.getString("tweet")
//                                val url = doc.getString("url")
//                                val keyword = doc.getString("keyword")
//                                val dates = doc.getString("dates")
//                                val tweet = Tweet(coin, coin_symbol, mtweet, url, keyword, id, booked, dates)
//                                tweets.add(tweet)
//                            }
//                            if (activity != null) {
//                                if (!tweets.isEmpty()) {
//                                    val tweetsAdapter = TweetsAdapter(activity!!, tweets, itemClickListener)
//                                    cointweetrecycler?.adapter = tweetsAdapter
//                                    //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))
//
//                                }
//                            }
//                        } else {
//                            for (doc in documentSnapshot.documents) {
//                                var booked = false
//                                var i = 0
//                                while(i<bookmarks.size){
//                                if(doc.id.contains(bookmarks[i])){
//                                    booked = true
//                                }
//                                i++
//                                }
//                                val id = doc.id
//                                val coin = doc.getString("coin")
//                                val coin_symbol = doc.getString("coin_symbol")
//                                val mtweet = doc.getString("tweet")
//                                val url = doc.getString("url")
//                                val keyword = doc.getString("keyword")
//                                val dates = doc.getString("dates")
//                                if (coin.toLowerCase().contains(p0) || coin_symbol.toLowerCase().contains(p0) || mtweet.toLowerCase().contains(p0) || keyword.toLowerCase().contains(p0) || coin.toUpperCase().contains(p0) || coin_symbol.toUpperCase().contains(p0) || mtweet.toUpperCase().contains(p0) || keyword.toUpperCase().contains(p0)) {
//                                    val tweet = Tweet(coin, coin_symbol, mtweet, url, keyword, id, booked, dates)
//                                    tweets.add(tweet)
//                                }
//
//                            }
//                            if (activity != null) {
//                                if (!tweets.isEmpty()) {
//                                    val tweetsAdapter = TweetsAdapter(activity!!, tweets, itemClickListener)
//                                    cointweetrecycler?.adapter = tweetsAdapter
//                                    //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))
//
//                                }
//                            }
//                        }
//                    }


    }
}