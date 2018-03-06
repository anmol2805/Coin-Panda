package com.anmol.coinpanda.Adapters

import android.content.Context
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.R
import kotlinx.android.synthetic.main.coinlayout.view.*

/**
 * Created by anmol on 3/6/2018.
 */
class GridnewAdapter(internal var c: Context, internal var allcoins: List<Allcoin>): BaseAdapter(){
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val v = inflate(c, R.layout.coinlayout,null)
        val nametext:TextView? = v.findViewById(R.id.namecoinall)
        nametext?.text = allcoins[p0].coinname
        return v
    }

    override fun getItem(p0: Int): Any {
        return allcoins[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return allcoins.size
    }

}