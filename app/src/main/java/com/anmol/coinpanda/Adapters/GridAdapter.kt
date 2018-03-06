package com.anmol.coinpanda.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.VideoView
import com.anmol.coinpanda.Model.Allcoin

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
        internal var img: ImageView? = null
        internal var pimg: ImageView? = null
        internal var mvid: VideoView? = null
        internal var vidlayout: RelativeLayout? = null
    }

    override fun getView(position: Int, view: View, viewGroup: ViewGroup): View? {
        var row: View? = view
        var holder = ViewHolder()
        if (row == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            row = inflater.inflate(resource, null)
            //holder.img = row!!.findViewById(R.id.galleryimg) as ImageView
            //holder.pimg = row.findViewById(R.id.playicon) as ImageView
            row.tag = holder
        } else {
            holder = row.tag as ViewHolder
        }



        return row
    }
}
