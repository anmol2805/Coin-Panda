package com.anmol.coinpanda.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.ico, container, false)
        val layoutManager = LinearLayoutManager(activity!!)
        cointweetrecycler = vi.findViewById(R.id.cointweetrecycler)
        cointweetrecycler?.layoutManager   = layoutManager
        cointweetrecycler?.setHasFixedSize(true)
        cointweetrecycler?.itemAnimator   = DefaultItemAnimator()

        val intent = Intent(activity,IcodbService::class.java)
        activity!!.startService(intent)
        icocoins = ArrayList()
        val db = Dbicohelper(activity!!)
        val query ="Select * from $TABLE_ICO"
        val data = db.readData(query)
        icocoins = data
        itemClickListener = object : ItemClickListener{
            override fun onItemClick(pos: Int) {
                val intent2 = Intent(activity,IcodataActivity::class.java)
                intent2.putExtra("iconame",icocoins[pos].ico_name)
                startActivity(intent2)
            }

        }
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
                    icocoins.add(icocoin)
                    c++
                }
                icoAdapter = IcoAdapter(activity!!,icocoins,itemClickListener)
                icoAdapter!!.notifyDataSetChanged()
                cointweetrecycler?.adapter = icoAdapter

            }, Response.ErrorListener {

            })
            Mysingleton.getInstance(activity).addToRequestqueue(jsonArray)
        }

        return vi
    }
}