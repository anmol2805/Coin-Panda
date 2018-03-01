package com.anmol.coinpanda.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Coin
import com.anmol.coinpanda.R
import com.bumptech.glide.Glide

/**
 * Created by anmol on 2/27/2018.
 */
class CoinAdapter(internal var c: Context,internal var coins: List<Coin>,private val mitemClickListener: ItemClickListener): RecyclerView.Adapter<CoinAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(c).inflate(R.layout.coinrow,parent,false)
        return MyViewHolder(v,mitemClickListener)
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coindata = coins[position]
        holder.mcoinname?.text = coindata.coinname
        if(coindata.coinnotify){
            Glide.with(c).load(R.drawable.notifon).into(holder.mcoinnotify)
        }
        else{
            Glide.with(c).load(R.drawable.notifoff).into(holder.mcoinnotify)
        }
    }

    inner class MyViewHolder(itemView: View, private val mitemClickListener: ItemClickListener):RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mcoinname:TextView? = null
        var mcoinnotify:ImageView? = null
        init {
            this.mcoinname = itemView.findViewById(R.id.namecoin)
            this.mcoinnotify = itemView.findViewById(R.id.notifstatus)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            mitemClickListener.onItemClick(this.adapterPosition)
        }


    }

}