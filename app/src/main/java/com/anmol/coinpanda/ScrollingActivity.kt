package com.anmol.coinpanda

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.anmol.coinpanda.Adapters.IcoAdapter
import com.anmol.coinpanda.Adapters.IcomsgAdapter
import com.anmol.coinpanda.Helper.COL_ICOPIN_ID
import com.anmol.coinpanda.Helper.Dbicopinhelper
import com.anmol.coinpanda.Helper.ICOPIN_TABLE_NAME
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Icocoin
import com.anmol.coinpanda.Model.Icopin
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifBitmapProvider
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import java.text.SimpleDateFormat

class ScrollingActivity : AppCompatActivity() {
    private var cointweetrecycler: RecyclerView?=null
    lateinit var icopins : MutableList<Icopin>
    var icomsgAdapter : IcomsgAdapter?=null
    lateinit var itemClickListener : ItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        val title = intent.getStringExtra("iconame")
        setTitle(title)
        content.text = intent.getStringExtra("description")
        industry.text = intent.getStringExtra("industry")
        icostatus.text = intent.getStringExtra("status")
        if(intent.getStringExtra("crowdsale") == ""){
            icocrowdsaledate.text = ""
        }
        else{
//            val olddate = "Crowdsale date:" + intent.getStringExtra("crowdsale")
//            val sdf = SimpleDateFormat("MM/dd/yyyy")
//            val date = sdf.format(olddate)
//            val sdfnew = SimpleDateFormat("dd-MM-yyyy")
//            val newdate = sdfnew.p
            icocrowdsaledate.text = "Crowdsale date:" + intent.getStringExtra("crowdsale")
        }

        if(intent.getStringExtra("hardcap") == "" || intent.getStringExtra("hardcap") == "?"){
            hardcaptext.text = ""
        }
        else{
            hardcaptext.text = "Hardcap:" + intent.getStringExtra("hardcap")
        }
        if(intent.getStringExtra("softcap") == "" || intent.getStringExtra("softcap") == "?"){
            softcaptext.text = ""
        }
        else
        {
            softcaptext.text = "Softcap:" + intent.getStringExtra("softcap")
        }


        val layoutManager = LinearLayoutManager(this)
        cointweetrecycler = findViewById(R.id.iconewsrecycler)
        cointweetrecycler?.layoutManager   = layoutManager
        cointweetrecycler?.setHasFixedSize(true)
        cointweetrecycler?.itemAnimator   = DefaultItemAnimator()
        icopins = ArrayList()
        val db = Dbicopinhelper(this)
        icotweetrefresh.setColorSchemeColors(
                resources.getColor(R.color.colorAccent)
        )
        val handler = Handler()
        handler.postDelayed({
            icotweetrefresh.isRefreshing = true
            var m = 0
            val jsonArray1 = JsonArrayRequest(Request.Method.GET,"http://198.199.90.139/ico/pinned",null, Response.Listener { response ->
                icopins.clear()
                while (m<response.length()){
                    val jsonObject = response.getJSONObject(m)
                    val messageid = jsonObject.getInt("cID")
                    val message = jsonObject.getString("pinned_messages")
                    val messagetime = jsonObject.getString("date")
                    val ico_name = jsonObject.getString("coin_name")
                    val icopin = Icopin(messageid,ico_name,message,messagetime)
                    db.insertData(icopin)
                    m++
                }
                val query ="Select * from $ICOPIN_TABLE_NAME ORDER BY $COL_ICOPIN_ID DESC"
                val data = db.readData(query)
                var x = 0
                while(x<data.size){
                    if(data[x].icocoin_name == title){
                        icopins.add(data[x])
                    }
                    x++
                }
                if(icopins.isEmpty()){
                    icotweetrefresh.isRefreshing = false
                    scrollinglayout.visibility = View.INVISIBLE
                }
                else{
                    icotweetrefresh.isRefreshing = false
                    scrollinglayout.visibility = View.VISIBLE
                    icomsgAdapter = IcomsgAdapter(this,icopins,itemClickListener)
                    icomsgAdapter!!.notifyDataSetChanged()
                    cointweetrecycler?.adapter = icomsgAdapter
                }


            }, Response.ErrorListener {
                icotweetrefresh.isRefreshing = false
                Toast.makeText(this,"Unable to refresh news",Toast.LENGTH_SHORT).show()
            })
            Mysingleton.getInstance(this).addToRequestqueue(jsonArray1)
        },100)

        icotweetrefresh.setOnRefreshListener {
            icotweetrefresh.isRefreshing = true
            var m = 0
            val jsonArray1 = JsonArrayRequest(Request.Method.GET,"http://198.199.90.139/ico/pinned",null, Response.Listener { response ->
                icopins.clear()
                while (m<response.length()){
                    val jsonObject = response.getJSONObject(m)
                    val messageid = jsonObject.getInt("cID")
                    val message = jsonObject.getString("pinned_messages")
                    val messagetime = jsonObject.getString("date")
                    val ico_name = jsonObject.getString("coin_name")
                    val icopin = Icopin(messageid,ico_name,message,messagetime)
                    db.insertData(icopin)
                    m++
                }
                val query ="Select * from $ICOPIN_TABLE_NAME ORDER BY $COL_ICOPIN_ID DESC"
                val data = db.readData(query)
                var x = 0
                while(x<data.size){
                    if(data[x].icocoin_name == title){
                        icopins.add(data[x])
                    }
                    x++
                }

                if(icopins.isEmpty()){
                    icotweetrefresh.isRefreshing = false
                    scrollinglayout.visibility = View.INVISIBLE
                }
                else{
                    scrollinglayout.visibility = View.VISIBLE
                    icotweetrefresh.isRefreshing = false
                    icomsgAdapter = IcomsgAdapter(this,icopins,itemClickListener)
                    icomsgAdapter!!.notifyDataSetChanged()
                    cointweetrecycler?.adapter = icomsgAdapter
                }


            }, Response.ErrorListener {
                icotweetrefresh.isRefreshing = false
                Toast.makeText(this,"Unable to refresh news",Toast.LENGTH_SHORT).show()
            })
            Mysingleton.getInstance(this).addToRequestqueue(jsonArray1)
        }
        val query ="Select * from $ICOPIN_TABLE_NAME ORDER BY $COL_ICOPIN_ID DESC"
        val data = db.readData(query)
        itemClickListener = object :ItemClickListener{
            override fun onItemClick(pos: Int) {

            }

        }
        if(!data.isEmpty()){
            var c = 0
            icopins.clear()
            while (c<data.size){
                if(data[c].icocoin_name == title){
                    icopins.add(data[c])
                }
                c++
            }
            if(icopins.isEmpty()){
                scrollinglayout.visibility = View.VISIBLE
            }
            else{
                scrollinglayout.visibility = View.VISIBLE
                icomsgAdapter = IcomsgAdapter(this,icopins,itemClickListener)
                icomsgAdapter!!.notifyDataSetChanged()
                cointweetrecycler?.adapter = icomsgAdapter
            }

        }
        else{
            var c = 0
            val jsonArray = JsonArrayRequest(Request.Method.GET,"http://198.199.90.139/ico/pinned",null, Response.Listener { response ->
                icopins.clear()
                while (c<response.length()){
                    val jsonObject = response.getJSONObject(c)
                    val messageid = jsonObject.getInt("cID")
                    val message = jsonObject.getString("pinned_messages")
                    val messagetime = jsonObject.getString("date")
                    val ico_name = jsonObject.getString("coin_name")
                    val icopin = Icopin(messageid,ico_name,message,messagetime)
                    if(ico_name == title){
                        icopins.add(icopin)
                    }
                    db.insertData(icopin)
                    c++
                }
                if(icopins.isEmpty()){
                    scrollinglayout.visibility = View.INVISIBLE
                }
                else{
                    scrollinglayout.visibility = View.VISIBLE
                    icomsgAdapter = IcomsgAdapter(this,icopins,itemClickListener)
                    icomsgAdapter!!.notifyDataSetChanged()
                    cointweetrecycler?.adapter = icomsgAdapter
                }


            }, Response.ErrorListener {
                Toast.makeText(this,"Network Error!!! Please try again.",Toast.LENGTH_SHORT).show()
            })
            Mysingleton.getInstance(this).addToRequestqueue(jsonArray)
        }

        btntelegram.setOnClickListener { view ->
            val url = intent.getStringExtra("telegramurl")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            try {
                startActivity(intent)
            }
            catch (e:ActivityNotFoundException){
                e.printStackTrace()
            }


        }
        btnmedium.setOnClickListener{view ->
            val url = intent.getStringExtra("mediumurl")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            try{
                startActivity(intent)
            }
            catch (e:ActivityNotFoundException){
                e.printStackTrace()
            }


        }
        btntwitter.setOnClickListener{
            view ->
            val url = intent.getStringExtra("twitterurl")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            try{
                startActivity(intent)
            }
            catch (e:ActivityNotFoundException){
                e.printStackTrace()
            }


        }
        website.setOnClickListener {
            view ->
            val url = intent.getStringExtra("websiteurl")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            try{
                startActivity(intent)
            }
            catch (e:ActivityNotFoundException){
                e.printStackTrace()
            }


        }
        Glide.with(this).load(intent.getStringExtra("twitterurl") + "/profile_image?size=original").listener(object:RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                Glide.with(this@ScrollingActivity).load(R.drawable.imgno).into(icolargeimg)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }

        }).into(icolargeimg)

    }
}
