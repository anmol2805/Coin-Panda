package com.anmol.coinpanda.Adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        holder.mcoin?.text = coindata.coin
        holder.coinname?.text = coindata.coin_symbol
        holder.keyword?.text = coindata.keyword
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



    }

    inner class MyViewHolder(itemView: View, private val mitemClickListener: ItemClickListener):RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mtweet:TextView?=null
        var mcoin:TextView?=null
        var coinname:TextView?=null
        var image:ImageView?=null
        var keyword:TextView?=null
        init {
            this.mtweet = itemView.findViewById(R.id.tweet)
            this.mcoin = itemView.findViewById(R.id.coin)
            this.coinname = itemView.findViewById(R.id.coinname)
            this.image = itemView.findViewById(R.id.coinicon)
            this.keyword =itemView.findViewById(R.id.keyword)
            itemView.setOnClickListener(this)
        }

        override fun onClick(position: View?) {
            mitemClickListener.onItemClick(this.adapterPosition)
        }


    }

}