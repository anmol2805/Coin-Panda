package com.anmol.coinpanda.Adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.anmol.coinpanda.Helper.Dbbookshelper
import com.anmol.coinpanda.Helper.Dbcoinshelper
import com.anmol.coinpanda.Helper.Dbhelper
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.*
import com.anmol.coinpanda.R
import com.anmol.coinpanda.TweetsActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by anmol on 2/27/2018.
 */
class TweetsAdapter(internal var c: Context, internal var tweets: List<Tweet>, private val mitemClickListener: ItemClickListener): RecyclerView.Adapter<TweetsAdapter.MyViewHolder>(){
    val auth = FirebaseAuth.getInstance()!!
    val topicsReference = FirebaseDatabase.getInstance().reference
    val messaging = FirebaseMessaging.getInstance()!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(c).inflate(R.layout.tweetrow,parent,false)
        return MyViewHolder(v,mitemClickListener)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

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
        val dcb = Dbcoinshelper(c)
        val coins = dcb.readData()
        var j = 0
        while (j<coins.size){
            if(coindata.coin_symbol == coins[j].coinname){
                holder.addbtn!!.visibility = View.INVISIBLE
            }
            j++
        }
        holder.addbtn!!.setOnClickListener{
            val dialog = Dialog(c)
            dialog.setContentView(R.layout.dialoglayout)
            val prg: ProgressBar? = dialog.findViewById(R.id.prgbr)
            val coinimg: ImageView? = dialog.findViewById(R.id.coinimg)
            val cn: TextView? = dialog.findViewById(R.id.cn)
            val cs: TextView? = dialog.findViewById(R.id.cs)
            val cp: TextView? = dialog.findViewById(R.id.cp)
            val atp: Button? = dialog.findViewById(R.id.atp)
            val portfoliolay: LinearLayout? = dialog.findViewById(R.id.portfoliolay)

            atp?.visibility = View.VISIBLE
            portfoliolay?.visibility = View.GONE
            cn?.text = coindata.coin
            cs?.text = coindata.coin_symbol
            cp?.text = "#"+ coindata.coinpage
            val testurl = "https://twitter.com/" + coindata.coinpage + "/profile_image?size=original"
            Glide.with(c).load(testurl).into(coinimg)
            atp?.setOnClickListener {
                val sqlcoin = Sqlcoin(coindata.coin, coindata.coin_symbol, coindata.coinpage)
                dcb.insertData(sqlcoin)
                topicsearch(0,coindata.coin_symbol,coindata.coin)
                prg?.visibility = View.VISIBLE
                atp.visibility = View.GONE
                val map = java.util.HashMap<String, Any>()
                map["coin"] = coindata.coin.toString()
                map["coinpage"] = coindata.coinpage.toString()
                map["coin_symbol"] = coindata.coin_symbol.toString()
                topicsReference.child("database").child(auth.currentUser!!.uid).child("portfolio").child(coindata.coin_symbol!!).setValue(map).addOnCompleteListener {
                    holder.addbtn!!.visibility = View.INVISIBLE
                    Toast.makeText(c,"Added to your Portfolio", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            }
            dialog.show()
        }
        holder.sharebtn?.setOnClickListener {
            val shareintent = Intent()
            shareintent.action = Intent.ACTION_SEND
            shareintent.type = "text/plain"
            shareintent.putExtra(Intent.EXTRA_TEXT,coindata.coin + " news:\nhttps://cryptohype.live/shared/" + coindata.tweetid)
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
        holder.bookmark?.setOnClickListener {
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
        var addbtn:Button?=null
        init {
            this.mtweet = itemView.findViewById(R.id.tweet)
            this.mcoin = itemView.findViewById(R.id.coin)
            this.coinname = itemView.findViewById(R.id.coinname)
            this.image = itemView.findViewById(R.id.coinicon)
            this.keyword =itemView.findViewById(R.id.keyword)
            this.bookmark = itemView.findViewById(R.id.bookmark)
            this.timestamp = itemView.findViewById(R.id.time)
            this.sharebtn = itemView.findViewById(R.id.sharebtn)
            this.addbtn = itemView.findViewById(R.id.addcoin)
            itemView.setOnClickListener(this)
        }

        override fun onClick(position: View?) {
            mitemClickListener.onItemClick(this.adapterPosition)
        }


    }
    private fun topicsearch(i: Int, coinname: String?, coin: String?) {

        topicsReference.child("topics").child(coinname + i.toString()).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val count:Long = p0.child("count").value as Long
                    if(count > 990){
                        topicsearch(i+1, coinname, coin)
                    }
                    else{
                        val map  = java.util.HashMap<String, Any>()
                        map["notify"] = true
                        map["coinname"] = coin!!
                        topicsReference.child("database").child(auth.currentUser!!.uid).child("topics").child(coinname + i.toString()).setValue(map)
                                .addOnCompleteListener {
                                    messaging.subscribeToTopic(coinname + i.toString())
                                    val countmap = java.util.HashMap<String, Any>()
                                    countmap["count"] = count+1
                                    countmap["coin_symbol"] = coinname.toString()
                                    topicsReference.child("topics").child(coinname + i.toString()).setValue(countmap)
                                }
                    }
                }
                else{
                    val map  = java.util.HashMap<String, Any>()
                    map["notify"] = true
                    map["coinname"] = coin!!
                    topicsReference.child("database").child(auth.currentUser!!.uid).child("topics").child(coinname + i.toString())
                            .setValue(map).addOnSuccessListener {
                                messaging.subscribeToTopic(coinname + i.toString())
                                val count = java.util.HashMap<String, Any>()
                                count["count"] = 1
                                count["coin_symbol"] = coinname.toString()
                                topicsReference.child("topics").child(coinname + i.toString()).setValue(count)
                            }
                }
            }

        })

    }

}