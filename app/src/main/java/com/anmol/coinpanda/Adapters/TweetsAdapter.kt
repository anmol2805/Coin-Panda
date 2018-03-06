package com.anmol.coinpanda.Adapters

import android.content.Context
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



    }

    inner class MyViewHolder(itemView: View, private val mitemClickListener: ItemClickListener):RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mtweet:TextView?=null
        var mcoin:TextView?=null
        init {
            this.mtweet = itemView.findViewById(R.id.tweet)
            this.mcoin = itemView.findViewById(R.id.coin)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            mitemClickListener.onItemClick(this.adapterPosition)
        }


    }

}