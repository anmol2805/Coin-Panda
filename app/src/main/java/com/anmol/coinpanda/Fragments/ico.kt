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
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Icocoin
import com.anmol.coinpanda.Mysingleton
import com.anmol.coinpanda.R
import com.anmol.coinpanda.ScrollingActivity
import com.anmol.coinpanda.Services.IcodbService
import com.anmol.coinpanda.Services.IcomsgdbService
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
    var db:Dbicohelper?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.ico, container, false)
        val layoutManager = LinearLayoutManager(activity!!)
        cointweetrecycler = vi.findViewById(R.id.cointweetrecycler)
        cointweetrecycler?.layoutManager   = layoutManager
        cointweetrecycler?.setHasFixedSize(true)
        cointweetrecycler?.setItemViewCacheSize(20)
        cointweetrecycler?.isDrawingCacheEnabled = true
        cointweetrecycler?.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        cointweetrecycler?.itemAnimator   = DefaultItemAnimator()
        srl = vi.findViewById(R.id.srl)
        //activity!!.startService(Intent(activity!!,IcomsgdbService::class.java))
        icocoins = ArrayList()
        db = Dbicohelper(activity!!)





        itemClickListener = object : ItemClickListener{
            override fun onItemClick(pos: Int) {

            }

        }
        srl?.setColorSchemeColors(
                resources.getColor(R.color.colorAccent)
        )
        srl?.isRefreshing = true
        val handler = Handler()
        handler.postDelayed({
            srl?.isRefreshing = false


        },2000)


        srl?.setOnRefreshListener {
            srl?.isRefreshing = true
            var c = 0
            val jsonArray = JsonArrayRequest(Request.Method.GET,"https://www.cryptohype.live/ico",null, Response.Listener {response ->
                try{

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
                        db!!.insertData(icocoin)
                        db!!.updatedata(icocoin)
                        c++
                    }
                    val query ="Select * from $TABLE_ICO"
                    val data = db!!.readData(query)
                    icocoins = data
                    srl?.isRefreshing = false
                    icoAdapter = IcoAdapter(activity!!,icocoins,itemClickListener)
                    icoAdapter!!.notifyDataSetChanged()
                    cointweetrecycler?.adapter = icoAdapter

                }
                catch (e:KotlinNullPointerException){
                    e.printStackTrace()
                }

            }, Response.ErrorListener {
                srl?.isRefreshing = false
                Toast.makeText(activity!!,"Unable to refresh Ico news",Toast.LENGTH_SHORT).show()
            })
            Mysingleton.getInstance(activity!!).addToRequestqueue(jsonArray)
        }

        try{

            val query ="Select * from $TABLE_ICO"
            val data = db!!.readData(query)
            icocoins = data
            if(!icocoins.isEmpty()){
                icoAdapter = IcoAdapter(activity!!,icocoins,itemClickListener)
                icoAdapter!!.notifyDataSetChanged()
                cointweetrecycler?.adapter = icoAdapter
            }
            else{
                var c = 0
                val jsonArray = JsonArrayRequest(Request.Method.GET,"https://www.cryptohype.live/ico",null, Response.Listener {response ->
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
                        db!!.insertData(icocoin)
                        db!!.updatedata(icocoin)
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
        }catch (e:KotlinNullPointerException){
            e.printStackTrace()
        }


        return vi
    }
}