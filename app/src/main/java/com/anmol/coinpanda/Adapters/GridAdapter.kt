package com.anmol.coinpanda.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.R
import kotlinx.android.synthetic.main.coinlayout.view.*

/**
 * Created by anmol on 12/28/2017.
 */

class GridAdapter(private val ctx: Context, private val resource: Int, private val allcoins: MutableList<Allcoin>) : BaseAdapter() {

    override fun getCount(): Int {
        return allcoins.size
    }

    override fun getItem(position: Int): Any {
        return allcoins[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }



    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View, viewGroup: ViewGroup): View? {

            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val row = inflater.inflate(resource, null)
        row.namecoinall?.text = allcoins[position].coinname
        return row
    }
}
