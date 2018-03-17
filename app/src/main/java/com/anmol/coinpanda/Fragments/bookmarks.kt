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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.anmol.coinpanda.Adapters.DividerItemDecoration
import com.anmol.coinpanda.Adapters.TweetsAdapter
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.R
import com.google.firebase.auth.FirebaseAuth
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.bookmarks,
                container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val layoutManager = LinearLayoutManager(activity)
        cointweetrecycler = vi.findViewById(R.id.cointweetrecycler)
        sedit = vi.findViewById(R.id.sc)
        srch = vi.findViewById(R.id.scb)
        empty = vi.findViewById(R.id.empty)
        empty?.visibility = View.GONE
        cointweetrecycler?.layoutManager   = layoutManager
        cointweetrecycler?.setHasFixedSize(true)
        cointweetrecycler?.itemAnimator   = DefaultItemAnimator()
        tweets = ArrayList()
        val handler = Handler()
        handler.postDelayed({loadquery(null)},200)

        itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {
//                val url = tweets[pos].url
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.data = Uri.parse(url)
//                startActivity(intent)

            }

        }
        sedit?.addTextChangedListener(object : TextWatcher {
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

    private fun loadquery(p0: CharSequence?) {
        tweets.clear()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH,-1)
        val stringtime = format.format(cal.time)
        val prevtime = Timestamp.valueOf(stringtime)
        db.collection("Tweets").whereGreaterThanOrEqualTo("date",prevtime)
                .orderBy("date", Query.Direction.DESCENDING).addSnapshotListener{ documentSnapshot, e->
                    tweets.clear()
                    val bookmarks : ArrayList<String> = ArrayList()
                    db.collection("users").document(auth.currentUser!!.uid).collection("bookmarks").addSnapshotListener { documnentSnapshot, e ->
                        bookmarks.clear()
                        tweets.clear()
                        for (doc in documnentSnapshot.documents) {
                            val tweetid = doc.id
                            bookmarks.add(tweetid)
                        }


                        if (p0 == null) {
                            for (doc in documentSnapshot.documents) {
                                var booked = false
                                var i = 0
                                while(i<bookmarks.size){
                                    if(doc.id.contains(bookmarks[i])){
                                        booked = true
                                    }
                                    i++
                                }
                                val id = doc.id
                                val coin = doc.getString("coin")
                                val coin_symbol = doc.getString("coin_symbol")
                                val mtweet = doc.getString("tweet")
                                val url = doc.getString("url")
                                val keyword = doc.getString("keyword")
                                val dates = doc.getString("dates")
                                if(booked){
                                    val tweet = Tweet(coin, coin_symbol, mtweet, url, keyword, id, booked, dates,"mc")
                                    tweets.add(tweet)
                                }

                            }
                            if (activity != null) {
                                if (!tweets.isEmpty()) {
                                    val tweetsAdapter = TweetsAdapter(activity!!, tweets, itemClickListener)
                                    cointweetrecycler?.adapter = tweetsAdapter
                                    //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))

                                }
                                else{
                                    empty?.visibility = View.VISIBLE
                                }
                            }
                        } else {
                            for (doc in documentSnapshot.documents) {
                                var booked = false
                                var i = 0
                                while(i<bookmarks.size){
                                    if(doc.id.contains(bookmarks[i])){
                                        booked = true
                                    }
                                    i++
                                }
                                val id = doc.id
                                val coin = doc.getString("coin")
                                val coin_symbol = doc.getString("coin_symbol")
                                val mtweet = doc.getString("tweet")
                                val url = doc.getString("url")
                                val keyword = doc.getString("keyword")
                                val dates = doc.getString("dates")
                                if(coin!=null && coin_symbol!=null && mtweet!=null && keyword!=null){
                                    if ((coin.toLowerCase().contains(p0) || coin_symbol.toLowerCase().contains(p0) || mtweet.toLowerCase().contains(p0) || keyword.toLowerCase().contains(p0) || coin.toUpperCase().contains(p0) || coin_symbol.toUpperCase().contains(p0) || mtweet.toUpperCase().contains(p0) || keyword.toUpperCase().contains(p0))&& booked) {
                                        val tweet = Tweet(coin, coin_symbol, mtweet, url, keyword, id, booked, dates,"mc")
                                        tweets.add(tweet)
                                    }
                                }


                            }
                            if (activity != null) {
                                if (!tweets.isEmpty()) {
                                    val tweetsAdapter = TweetsAdapter(activity!!, tweets, itemClickListener)
                                    cointweetrecycler?.adapter = tweetsAdapter
                                    //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))

                                }
                                else{
                                    empty?.visibility = View.VISIBLE
                                }
                            }
                        }
                    }

                }
    }
}