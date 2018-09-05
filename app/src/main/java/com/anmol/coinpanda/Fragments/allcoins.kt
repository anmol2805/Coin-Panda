package com.anmol.coinpanda.Fragments

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.anmol.coinpanda.Adapters.TweetsAdapter
import com.anmol.coinpanda.Helper.*
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.Mysingleton
import com.anmol.coinpanda.R
import com.github.ybq.android.spinkit.SpinKitView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException
import kotlin.collections.ArrayList

/**
 * Created by anmol on 3/11/2018.
 */
class allcoins : Fragment() {
    private var cointweetrecycler: RecyclerView? = null
    private var keywordrecycler: RecyclerView? = null
    var keywords: ArrayList<String>? = null
    lateinit var tweets: MutableList<Tweet>
    lateinit var itemClickListener: ItemClickListener
    lateinit var keyClickListener: ItemClickListener
    var retry: Button? = null
    var sedit: EditText? = null
    var srch: Button? = null
    var db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    var tweetsAdapter: TweetsAdapter? = null
    var pgr: SpinKitView? = null
    var empty: ImageView? = null
    var srl: SwipeRefreshLayout? = null
    var dcb: Dbcoinshelper? = null
    var isLoading: Boolean = false
    lateinit var loadtweets: ArrayList<Tweet>
    var lasttweetid:Int = 0
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
        srl = vi.findViewById(R.id.srl)

        cointweetrecycler?.layoutManager   = layoutManager
        cointweetrecycler?.setHasFixedSize(true)
        cointweetrecycler?.itemAnimator   = DefaultItemAnimator()



        tweets = ArrayList()
        // list of tweets
        loadtweets = ArrayList()


        itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {

                //loadquery(keywords!![pos])
//                sedit?.setText(keywords!![pos])

            }

        }

        tweetsAdapter = TweetsAdapter(activity!!, loadtweets, itemClickListener)
        cointweetrecycler?.adapter = tweetsAdapter


        dcb = Dbcoinshelper(activity!!)

        val handler = Handler()
        handler.postDelayed({
            loadquery(0, 20)
        },200)
        cointweetrecycler?.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + pastVisibleItems >= totalItemCount && !isLoading) {
                        pgr?.visibility = View.VISIBLE
                        val handler2 = Handler()
                        handler2.postDelayed({
                            loadquery(20, 0)
                        },2500)

                        isLoading = true
                    }
                }
            }
        })




        keyClickListener = object :ItemClickListener{
            override fun onItemClick(pos: Int) {
            }
        }

            retry?.setOnClickListener{
                loadquery(0, 0)
            }
            srl?.setColorSchemeColors(
                    resources.getColor(R.color.colorAccent)
            )

        srl?.isRefreshing = true
        val handler2 = Handler()
        handler2.postDelayed({
            srl?.isRefreshing = false

        },2000)

            srl?.setOnRefreshListener {
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, "https://www.cryptohype.live/tweets", null, Response.Listener { response ->
                    var c = 0
                    try {
                        val jsonArray = response.getJSONArray("tweets")
                        val sqltweets = java.util.ArrayList<Sqltweet>()
                        sqltweets.clear()


                        while (c < 10) {
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
                            val db = Dbhelper(activity!!)
                            db.insertData(sqltweet)
                            println("tweetno$c")
                            c++
                        }
                        srl?.isRefreshing = false
                        loadquery(0, 0)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
                    srl?.isRefreshing = false
                    Toast.makeText(activity,"Unable to refresh tweets",Toast.LENGTH_SHORT).show()

                })
                Mysingleton.getInstance(activity!!).addToRequestqueue(jsonObjectRequest)
            }

            return vi
    }

    private fun loadquery(offset: Int, numOfTweets: Int) {
        pgr?.visibility = View.VISIBLE
        retry?.visibility = View.GONE
        empty?.visibility = View.GONE
        tweets.clear()
        if(activity!=null){
            val db = Dbhelper(activity!!)

            val dataquery: String = if(numOfTweets == 0) {
                "SELECT * FROM $TABLE_NAME ORDER BY $COL_ID DESC LIMIT -1 OFFSET $offset"
            } else {
                "SELECT * FROM $TABLE_NAME ORDER BY $COL_ID DESC LIMIT $numOfTweets"
            }

            var bookmarks = ArrayList<String>()
            val dbb = Dbbookshelper(activity!!)
            bookmarks = dbb.readbook()

            //loadtweets.clear()
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
                    val coins = dcb!!.readData()
                    var k = 0
                    var coinadded = false
                    while (k<coins.size){
                        if(tweets[i].coin_symbol == coins[k].coinname){
                            coinadded = true
                        }
                        k++
                    }
                    val tweet = Tweet(tweets[i].coin,tweets[i].coin_symbol,tweets[i].tweet,tweets[i].url,tweets[i].keyword,tweets[i].tweetid,booked,tweets[i].dates,"mc",tweets[i].coinpage,coinadded)
                    loadtweets.add(tweet)
                    i++
                }

                if(!loadtweets.isEmpty()){
                    pgr?.visibility = View.GONE
                    retry?.visibility = View.GONE
                    empty?.visibility = View.GONE
                    //tweetsAdapter = TweetsAdapter(activity!!, loadtweets, itemClickListener)
                    tweetsAdapter!!.notifyDataSetChanged()
                    //cointweetrecycler?.adapter = tweetsAdapter
                }
                else{
                    pgr?.visibility = View.GONE
                    retry?.visibility = View.VISIBLE
                    empty?.visibility = View.VISIBLE
                }
                //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))

            }
            else{

                val jsonobjectrequest = JsonObjectRequest(Request.Method.GET,"https://www.cryptohype.live/tweets",null,
                        Response.Listener { response ->

                            var c = 0
                            val jsonArray = response.getJSONArray("tweets")
                            tweets.clear()

                            while (c<450){
                                val obj = jsonArray.getJSONObject(c)
                                val id = obj.getString("tweetid")
                                val coin = obj.getString("coin_name")
                                val coin_symbol = obj.getString("coin_symbol")
                                val mtweet = obj.getString("tweet")
                                val url = obj.getString("url")
                                val keyword = obj.getString("keyword")
                                val dates = obj.getString("date")
                                val coinpage = obj.getString("coin_handle")
                                val coins = dcb!!.readData()
                                var k = 0
                                var coinadded = false
                                while (k<coins.size){
                                    if(coin_symbol == coins[k].coinname){
                                        coinadded = true
                                    }
                                    k++
                                }
                                val tweet = Tweet(coin,coin_symbol,mtweet,url,keyword,id,false,dates,"ac",coinpage,coinadded)
                                tweets.add(tweet)
                                c++
                            }
                            if (activity != null) {
                                if (!tweets.isEmpty()) {
                                    pgr?.visibility = View.GONE
                                    retry?.visibility = View.GONE
                                    empty?.visibility = View.GONE
                                    //tweetsAdapter = TweetsAdapter(activity!!, tweets, itemClickListener)
                                    tweetsAdapter!!.notifyDataSetChanged()
                                    //cointweetrecycler?.adapter = tweetsAdapter
                                    //cointweetrecycler?.addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(activity!!,R.drawable.item_decorator)!!))

                                }
                                else{
                                    pgr?.visibility = View.GONE
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

            }
        }

        isLoading = false




    }
}