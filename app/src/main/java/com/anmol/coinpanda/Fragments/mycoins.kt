package com.anmol.coinpanda.Fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
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
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.Mysingleton
import com.anmol.coinpanda.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.tweetrow.*
import org.json.JSONException
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
    var srl:SwipeRefreshLayout?=null
    var dateedit:Button?=null
    var dbhelper:Dbhelper?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.mycoins, container, false)
        if(activity!=null){
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            val layoutManager = LinearLayoutManager(activity)
            dbhelper = Dbhelper(activity!!)
            cointweetrecycler = vi.findViewById(R.id.cointweetrecycler)
            keywordrecycler = vi.findViewById(R.id.keywordrecycler)
            sedit = vi.findViewById(R.id.sc)
            sedit?.clearFocus()
            srch = vi.findViewById(R.id.scb)
            empty = vi.findViewById(R.id.empty)
            retry = vi.findViewById(R.id.retry)
            retry?.visibility = View.GONE
            pgr = vi.findViewById(R.id.pgr)
            srl = vi.findViewById(R.id.srl)
            dateedit = vi.findViewById(R.id.searchdate)
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
        dateedit?.setOnClickListener{
            val mcurrentdate = Calendar.getInstance()
            val myear = mcurrentdate.get(Calendar.YEAR)
            val mMonth = mcurrentdate.get(Calendar.MONTH)
            val mDay =  mcurrentdate.get(Calendar.DAY_OF_MONTH)
            val mDatepicker = DatePickerDialog(activity,DatePickerDialog.OnDateSetListener { datePicker:DatePicker, y:Int, m:Int, d:Int ->
                var month = m
                month += 1
                dateedit?.text = "$y-$month-$d"
            },myear,mMonth,mDay)
            mDatepicker.setTitle("Select Date")
            mDatepicker.show()
        }

        retry?.setOnClickListener{
            loadquery(null)
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
            System.out.println("refreshing")
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
                        try{

                            dbhelper!!.insertData(sqltweet)
                            println("tweetno$c")
                        }
                        catch (e:KotlinNullPointerException){
                            e.printStackTrace()
                        }

                        c++
                    }
                    srl?.isRefreshing = false
                    loadquery(null)

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

    private fun loadquery(p0: CharSequence?) {
        tweets.clear()
        pgr?.visibility = View.VISIBLE
        retry?.visibility = View.GONE
        empty?.visibility = View.GONE
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH,-1)
        val stringtime = format.format(cal.time)
        val prevtime = Timestamp.valueOf(stringtime)
        if(activity!=null){
            val dataquery = "Select * from $TABLE_NAME ORDER BY $COL_ID DESC"
            var bookmarks = ArrayList<String>()
            val dbb = Dbbookshelper(activity!!)
            bookmarks = dbb.readbook()
            val dcb = Dbcoinshelper(activity!!)
            var coins :MutableList<Allcoin> = ArrayList()
            coins = dcb.readData()
            val loadtweets = ArrayList<Tweet>()
            loadtweets.clear()
            val data = dbhelper!!.readData(dataquery)
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
                            val tweet = Tweet(tweets[i].coin,tweets[i].coin_symbol,tweets[i].tweet,tweets[i].url,tweets[i].keyword,tweets[i].tweetid,booked,tweets[i].dates,"mc",tweets[i].coinpage,true)
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
                                                        val tweetsAdapter = TweetsAdapter(activity!!,tweets,itemClickListener)
                                                        tweetsAdapter.notifyDataSetChanged()
                                                        cointweetrecycler?.adapter = tweetsAdapter
                                                        pgr?.visibility = View.GONE
                                                        empty?.visibility = View.VISIBLE
                                                        empty?.text = "No Results found"
                                                    }
                                                }
                }

            })
        }



    }
}
