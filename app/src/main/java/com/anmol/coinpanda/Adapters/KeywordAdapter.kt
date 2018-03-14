package com.anmol.coinpanda.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.R

/**
 * Created by anmol on 3/14/2018.
 */
class KeywordAdapter(internal var c: Context, internal var ketwords: ArrayList<String>, private val mitemClickListener: ItemClickListener) : RecyclerView.Adapter<KeywordAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(c).inflate(R.layout.keyword, parent, false)
        return MyViewHolder(v, mitemClickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text?.text = ketwords[position]
    }

    override fun getItemCount(): Int {
        return ketwords.size
    }

    inner class MyViewHolder(itemView: View, private val mitemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var text : TextView?=null
        init {
            this.text = itemView.findViewById(R.id.key)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mitemClickListener.onItemClick(this.adapterPosition)
        }
    }
}
