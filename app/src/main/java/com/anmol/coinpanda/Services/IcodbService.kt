package com.anmol.coinpanda.Services

import android.app.IntentService
import android.content.Intent
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.anmol.coinpanda.Adapters.IcoAdapter
import com.anmol.coinpanda.Fragments.ico
import com.anmol.coinpanda.Helper.Dbicohelper
import com.anmol.coinpanda.Model.Icocoin
import com.anmol.coinpanda.Mysingleton

class IcodbService:IntentService("IcodbService"){
    override fun onHandleIntent(p0: Intent?) {
        val db = Dbicohelper(applicationContext)
        var c = 0
        val jsonArray = JsonArrayRequest(Request.Method.GET,"https://www.cryptohype.live/ico",null, Response.Listener { response ->
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
                db.updatedata(icocoin)
                c++
            }


        }, Response.ErrorListener {

        })
        Mysingleton.getInstance(applicationContext).addToRequestqueue(jsonArray)
    }
}