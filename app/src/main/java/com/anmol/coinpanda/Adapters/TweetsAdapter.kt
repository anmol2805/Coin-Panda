package com.anmol.coinpanda.Adapters

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
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.Model.Coin
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coindata = tweets[position] 
        holder.mtweet?.text = coindata.tweet
        holder.mtweet?.setOnClickListener {
            val url = tweets[position].url
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            c.startActivity(intent)
        }
        holder.sharebtn?.setOnClickListener {
            val shareintent = Intent()
            shareintent.action = Intent.ACTION_SEND
            shareintent.type = "text/plain"
            shareintent.putExtra(Intent.EXTRA_TEXT,tweets[position].url)
            c.startActivity(Intent.createChooser(shareintent,"Share tweet"))
        }
        holder.mcoin?.text = coindata.coin
        holder.coinname?.text = coindata.coin_symbol
        holder.keyword?.text = coindata.keyword
        holder.timestamp?.text = coindata.dates
        val urlpng = "https://raw.githubusercontent.com/crypti/cryptocurrencies/master/images/"+coindata.coin_symbol+".png"
        val urljpg = "https://raw.githubusercontent.com/crypti/cryptocurrencies/master/images/"+coindata.coin_symbol+".jpg"
        val urljpeg = "https://raw.githubusercontent.com/crypti/cryptocurrencies/master/images/"+coindata.coin_symbol+".jpeg"
        Glide.with(c).load(urlpng).listener(object : RequestListener<Drawable> {
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                return false
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                Glide.with(c).load(urljpg).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        Glide.with(c).load(urljpeg).into(holder.image)
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                }).into(holder.image)
                return true
            }

        }).into(holder.image)
        val db = FirebaseFirestore.getInstance()
        holder.bookmark?.setOnClickListener {

            val map = HashMap<String,Any>()
            map["bookmark"] = true
            val auth = FirebaseAuth.getInstance()
            db.collection("users").document(auth.currentUser!!.uid).collection("bookmarks").document(coindata.tweetid!!).set(map).addOnSuccessListener {
                Glide.with(c).load(R.drawable.starfilled).into(holder.bookmark)
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