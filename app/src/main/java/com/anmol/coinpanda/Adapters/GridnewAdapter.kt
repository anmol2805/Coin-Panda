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
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
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
        val urlpng = "https://raw.githubusercontent.com/crypti/cryptocurrencies/master/images/"+allcoins[p0].coinname+".png"
        val urljpg = "https://raw.githubusercontent.com/crypti/cryptocurrencies/master/images/"+allcoins[p0].coinname+".jpg"
        val urljpeg = "https://raw.githubusercontent.com/crypti/cryptocurrencies/master/images/"+allcoins[p0].coinname+".jpeg"

        nametext?.text = allcoins[p0].coin
        Glide.with(c).load(urlpng).listener(object : RequestListener<Drawable>{
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                return false
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                Glide.with(c).load(urljpg).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        Glide.with(c).load(urljpeg).into(imageicon)
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                }).into(imageicon)
                return true
            }

        }).into(imageicon)

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