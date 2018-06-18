package com.anmol.coinpanda.Fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.anmol.coinpanda.Adapters.IcoAdapter
import com.anmol.coinpanda.Helper.Dbicohelper
import com.anmol.coinpanda.Helper.TABLE_ICO
import com.anmol.coinpanda.IcodataActivity
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Icocoin
import com.anmol.coinpanda.Mysingleton
import com.anmol.coinpanda.R
import com.anmol.coinpanda.ScrollingActivity
import com.anmol.coinpanda.Services.IcodbService
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.startService
import org.json.JSONObject

/**
 * Created by anmol on 3/11/2018.
 */
class ico : Fragment(){
    private var cointweetrecycler: RecyclerView?=null
    lateinit var icocoins : MutableList<Icocoin>
    var icoAdapter : IcoAdapter?=null
    lateinit var itemClickListener : ItemClickListener
    var srl:SwipeRefreshLayout?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.ico, container, false)
        val layoutManager = LinearLayoutManager(activity!!)
        cointweetrecycler = vi.findViewById(R.id.cointweetrecycler)
        cointweetrecycler?.layoutManager   = layoutManager
        cointweetrecycler?.setHasFixedSize(true)
        cointweetrecycler?.itemAnimator   = DefaultItemAnimator()
        srl = vi.findViewById(R.id.srl)

        icocoins = ArrayList()
        val db = Dbicohelper(activity!!)





        itemClickListener = object : ItemClickListener{
            override fun onItemClick(pos: Int) {
                val intent2 = Intent(activity!!,ScrollingActivity::class.java)
                intent2.putExtra("iconame",icocoins[pos].ico_name)
                intent2.putExtra("telegramurl",icocoins[pos].telegram_url)
                intent2.putExtra("twitterurl",icocoins[pos].twitter_url)
                intent2.putExtra("mediumurl",icocoins[pos].medium_url)
                intent2.putExtra("websiteurl",icocoins[pos].website)
                intent2.putExtra("description",icocoins[pos].icodescription)
                intent2.putExtra("hardcap",icocoins[pos].hardcap)
                intent2.putExtra("softcap",icocoins[pos].softcap)
                intent2.putExtra("status",icocoins[pos].ico_status)
                intent2.putExtra("crowdsale",icocoins[pos].crowdsale_date)
                intent2.putExtra("industry",icocoins[pos].industry)
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!,vi.findViewById(R.id.coinicon),"myimage")
                startActivity(intent2,optionsCompat.toBundle())
            }

        }
        srl?.setColorSchemeColors(
                resources.getColor(R.color.colorAccent)
        )
        val handler = Handler()
        handler.postDelayed({
            srl?.isRefreshing = true
            var c = 0
            val jsonArray = JsonArrayRequest(Request.Method.GET,"http://198.199.90.139/ico",null, Response.Listener {response ->
                icocoins.clear()
                while (c<response.length()){
                    val jsonObject = response.getJSONObject(c)
                    val iconame = jsonObject.getString("ICO_Name")
                    val telegramurl = jsonObject.getString("Telegram_URL")
                    val website = jsonObject.getString("Website")
                    val mediumurl = jsonObject.getString("Medium_URL")
                    val crowdsale_date = jsonObject.getString("Crowdsale_Date")
                    val icostatus = jsonObject.getString("ICO_Status")
                    val industry = jsonObject.getString("Industry")
                    val icodescription = jsonObject.getString("Description")
                    val hardcap = jsonObject.getString("Hardcap")
                    val softcap = jsonObject.getString("Softcap")
                    val twitterurl = jsonObject.getString("Twitter_URL")
                    val rating = jsonObject.getString("Rating")
                    val icocoin = Icocoin(iconame,telegramurl,website,mediumurl,crowdsale_date,icostatus,industry,icodescription,hardcap,softcap,twitterurl,rating)
                    db.insertData(icocoin)
                    c++
                }
                val query ="Select * from $TABLE_ICO"
                val data = db.readData(query)
                icocoins = data
                srl?.isRefreshing = false
                icoAdapter = IcoAdapter(activity!!,icocoins,itemClickListener)
                icoAdapter!!.notifyDataSetChanged()
                cointweetrecycler?.adapter = icoAdapter

            }, Response.ErrorListener {
                srl?.isRefreshing = false
                Toast.makeText(activity!!,"Unable to refresh Ico news",Toast.LENGTH_SHORT).show()
            })
            Mysingleton.getInstance(activity!!).addToRequestqueue(jsonArray)

        },200)
        srl?.setOnRefreshListener {
            srl?.isRefreshing = true
            var c = 0
            val jsonArray = JsonArrayRequest(Request.Method.GET,"http://198.199.90.139/ico",null, Response.Listener {response ->
                icocoins.clear()
                while (c<response.length()){
                    val jsonObject = response.getJSONObject(c)
                    val iconame = jsonObject.getString("ICO_Name")
                    val telegramurl = jsonObject.getString("Telegram_URL")
                    val website = jsonObject.getString("Website")
                    val mediumurl = jsonObject.getString("Medium_URL")
                    val crowdsale_date = jsonObject.getString("Crowdsale_Date")
                    val icostatus = jsonObject.getString("ICO_Status")
                    val industry = jsonObject.getString("Industry")
                    val icodescription = jsonObject.getString("Description")
                    val hardcap = jsonObject.getString("Hardcap")
                    val softcap = jsonObject.getString("Softcap")
                    val twitterurl = jsonObject.getString("Twitter_URL")
                    val rating = jsonObject.getString("Rating")
                    val icocoin = Icocoin(iconame,telegramurl,website,mediumurl,crowdsale_date,icostatus,industry,icodescription,hardcap,softcap,twitterurl,rating)
                    db.insertData(icocoin)
                    c++
                }
                val query ="Select * from $TABLE_ICO"
                val data = db.readData(query)
                icocoins = data
                srl?.isRefreshing = false
                icoAdapter = IcoAdapter(activity!!,icocoins,itemClickListener)
                icoAdapter!!.notifyDataSetChanged()
                cointweetrecycler?.adapter = icoAdapter

            }, Response.ErrorListener {
                srl?.isRefreshing = false
                Toast.makeText(activity!!,"Unable to refresh Ico news",Toast.LENGTH_SHORT).show()
            })
            Mysingleton.getInstance(activity!!).addToRequestqueue(jsonArray)
        }
        val query ="Select * from $TABLE_ICO"
        val data = db.readData(query)
        icocoins = data
        if(!icocoins.isEmpty()){
            icoAdapter = IcoAdapter(activity!!,icocoins,itemClickListener)
            icoAdapter!!.notifyDataSetChanged()
            cointweetrecycler?.adapter = icoAdapter
        }
        else{
            var c = 0
            val jsonArray = JsonArrayRequest(Request.Method.GET,"http://198.199.90.139/ico",null, Response.Listener {response ->
                while (c<response.length()){
                    val jsonObject = response.getJSONObject(c)
                    val iconame = jsonObject.getString("ICO_Name")
                    val telegramurl = jsonObject.getString("Telegram_URL")
                    val website = jsonObject.getString("Website")
                    val mediumurl = jsonObject.getString("Medium_URL")
                    val crowdsale_date = jsonObject.getString("Crowdsale_Date")
                    val icostatus = jsonObject.getString("ICO_Status")
                    val industry = jsonObject.getString("Industry")
                    val icodescription = jsonObject.getString("Description")
                    val hardcap = jsonObject.getString("Hardcap")
                    val softcap = jsonObject.getString("Softcap")
                    val twitterurl = jsonObject.getString("Twitter_URL")
                    val rating = jsonObject.getString("Rating")
                    val icocoin = Icocoin(iconame,telegramurl,website,mediumurl,crowdsale_date,icostatus,industry,icodescription,hardcap,softcap,twitterurl,rating)
                    db.insertData(icocoin)
                    icocoins.add(icocoin)
                    c++
                }
                icoAdapter = IcoAdapter(activity!!,icocoins,itemClickListener)
                icoAdapter!!.notifyDataSetChanged()
                cointweetrecycler?.adapter = icoAdapter

            }, Response.ErrorListener {
                Toast.makeText(activity!!,"Network Error!!! Please try again",Toast.LENGTH_SHORT).show()
            })
            Mysingleton.getInstance(activity!!).addToRequestqueue(jsonArray)
        }

        return vi
    }
}