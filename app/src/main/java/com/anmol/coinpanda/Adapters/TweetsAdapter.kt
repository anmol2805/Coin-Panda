package com.anmol.coinpanda.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.anmol.coinpanda.Helper.Dbbookshelper
import com.anmol.coinpanda.Helper.Dbcoinshelper
import com.anmol.coinpanda.Helper.Dbhelper
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.Model.Coin
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by anmol on 2/27/2018.
 */
class TweetsAdapter(internal var c: Context, internal var tweets: List<Tweet>, private val mitemClickListener: ItemClickListener): RecyclerView.Adapter<TweetsAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(c).inflate(R.layout.tweetrow,parent,false)
        return MyViewHolder(v,mitemClickListener)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val auth = FirebaseAuth.getInstance()
        val databaseReference = FirebaseDatabase.getInstance().reference.child("database").child(auth.currentUser!!.uid).child("bookmarks")
        val coindata = tweets[position] 
        holder.mtweet?.text = coindata.tweet
        holder.mtweet?.setOnClickListener {
            val url = tweets[position].url
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            c.startActivity(intent)
        }
        if(coindata.source!!.contains("ac")){
            holder.bookmark!!.visibility = View.INVISIBLE
        }
        else{
            holder.bookmark!!.visibility = View.VISIBLE
        }
        holder.sharebtn?.setOnClickListener {
            val shareintent = Intent()
            shareintent.action = Intent.ACTION_SEND
            shareintent.type = "text/plain"
            shareintent.putExtra(Intent.EXTRA_TEXT,"https://cryptohype.live/shared/" + coindata.tweetid)
            c.startActivity(Intent.createChooser(shareintent,"Share tweet"))
        }

        holder.mcoin?.text = coindata.coin
        holder.coinname?.text = coindata.coin_symbol
        holder.keyword?.text = "#" + coindata.keyword

        val oldFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        oldFormatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = oldFormatter.parse(coindata.dates)
        val newFormatter = SimpleDateFormat("dd-MM-yyyy hh:mm a")
        newFormatter.timeZone = TimeZone.getDefault()
        val dueDateAsNormal = newFormatter.format(value)
        System.out.println("checkdate$dueDateAsNormal")
        holder.timestamp?.text = dueDateAsNormal


        val testurl = "https://twitter.com/" + coindata.coinpage + "/profile_image?size=original"
        println("testurltweets$testurl")
        Glide.with(c).load(testurl).into(holder.image)
        val db = FirebaseFirestore.getInstance()
        holder.bookmark?.setOnClickListener {
            val dcb = Dbcoinshelper(c)
            var allcoins: MutableList<Allcoin> = ArrayList()
            allcoins.clear()
            allcoins = dcb.readData()
            if (!allcoins.isEmpty()) {
                println("not empty")
                val coins = ArrayList<String>()
                for (i in allcoins.indices) {
                    coins.add(allcoins[i].coinname!!)
                }
                var mytweet = 0
                for (j in coins.indices) {
                    if (coins[j] == tweets[position].coin_symbol) {
                        mytweet = 1
                    }
                }
                val dbc = Dbhelper(c)
                val dbb = Dbbookshelper(c)
                if(tweets[position].booked){
                    dbb.deletebook(tweets[position].tweetid!!)
//                    val sqltweet = Sqltweet(tweets[position].coin,tweets[position].coin_symbol,tweets[position].tweet,tweets[position].url,tweets[position].keyword,tweets[position].tweetid,tweets[position].dates,tweets[position].coin_symbol,mytweet,0)
//                    dbc.updatetweet(sqltweet)
                    Glide.with(c).load(R.drawable.starunfilled).into(holder.bookmark)

                    databaseReference.child(tweets[position].tweetid!!).removeValue().addOnCompleteListener {
                        Glide.with(c).load(R.drawable.starunfilled).into(holder.bookmark)
                    }

                }
                else{
                    dbb.insertData(tweets[position].tweetid!!)
//                    val sqltweet = Sqltweet(tweets[position].coin,tweets[position].coin_symbol,tweets[position].tweet,tweets[position].url,tweets[position].keyword,tweets[position].tweetid,tweets[position].dates,tweets[position].coin_symbol,mytweet,1)
//                    dbc.updatetweet(sqltweet)
                    Glide.with(c).load(R.drawable.starfilled).into(holder.bookmark)
                    val map = HashMap<String,Any>()
                    map[tweets[position].tweetid!!] = true
                    databaseReference.updateChildren(map).addOnCompleteListener {
                        Glide.with(c).load(R.drawable.starfilled).into(holder.bookmark)
                    }


                }

            }



        }
        if(tweets[position].booked){
            Glide.with(c).load(R.drawable.starfilled).into(holder.bookmark)
        }
        else{
            Glide.with(c).load(R.drawable.starunfilled).into(holder.bookmark)
        }




    }

    inner class MyViewHolder(itemView: View, private val mitemClickListener: ItemClickListener):RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mtweet:TextView?=null
        var mcoin:TextView?=null
        var coinname:TextView?=null
        var image:ImageView?=null
        var keyword:TextView?=null
        var bookmark:ImageView?=null
        var timestamp:TextView?=null
        var sharebtn:Button?=null
        init {
            this.mtweet = itemView.findViewById(R.id.tweet)
            this.mcoin = itemView.findViewById(R.id.coin)
            this.coinname = itemView.findViewById(R.id.coinname)
            this.image = itemView.findViewById(R.id.coinicon)
            this.keyword =itemView.findViewById(R.id.keyword)
            this.bookmark = itemView.findViewById(R.id.bookmark)
            this.timestamp = itemView.findViewById(R.id.time)
            this.sharebtn = itemView.findViewById(R.id.sharebtn)
            itemView.setOnClickListener(this)
        }

        override fun onClick(position: View?) {
            mitemClickListener.onItemClick(this.adapterPosition)
        }


    }

}