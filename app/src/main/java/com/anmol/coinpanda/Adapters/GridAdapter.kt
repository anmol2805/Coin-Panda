package com.anmol.coinpanda.Adapters

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

    private inner class ViewHolder {
        var coinnameall:TextView?=null

    }

    override fun getView(position: Int, view: View, viewGroup: ViewGroup): View? {
        var row: View? = view
        var holder = ViewHolder()
        if (row == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            row = inflater.inflate(resource, null)
            holder.coinnameall = row!!.findViewById(R.id.namecoinall)
            //holder.img = row!!.findViewById(R.id.galleryimg) as ImageView
            //holder.pimg = row.findViewById(R.id.playicon) as ImageView
            row.tag = holder
        } else {
            holder = row.tag as ViewHolder
        }
        holder.coinnameall?.text = allcoins[position].coinname


        return row
    }
}
