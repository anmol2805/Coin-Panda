package com.anmol.coinpanda.Adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.coinlayout.view.*

/**
 * Created by anmol on 3/6/2018.
 */
class GridnewAdapter(internal var c: Context, internal var allcoins: List<Allcoin>): BaseAdapter(){
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val v = inflate(c, R.layout.coinlayout,null)
        val imageicon:ImageView? = v.findViewById(R.id.imageView3)
        val nametext:TextView? = v.findViewById(R.id.namecoinall)
        val testurl = "https://twitter.com/" + allcoins[p0].coinpage + "/profile_image?size=original"
        Glide.with(c).load(testurl).into(imageicon)
        nametext?.text = allcoins[p0].coin

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