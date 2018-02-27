package com.anmol.coinpanda.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Coin
import com.anmol.coinpanda.R

/**
 * Created by anmol on 2/27/2018.
 */
class CoinAdapter(internal var c: Context,internal var coins: List<Coin>,private val mitemClickListener: ItemClickListener): RecyclerView.Adapter<CoinAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder{
        val v = LayoutInflater.from(c).inflate(R.layout.coinrow,parent,false)
        return MyViewHolder(v,mitemClickListener)
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coindata = coins[position]
        holder.mcoinname?.text = coindata.coinname
    }

    inner class MyViewHolder(itemView: View, private val mitemClickListener: ItemClickListener):RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mcoinname:TextView? = null
        init {
            this.mcoinname = itemView.findViewById<TextView>(R.id.namecoin)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            mitemClickListener.onItemClick(this.adapterPosition)
        }


    }

}