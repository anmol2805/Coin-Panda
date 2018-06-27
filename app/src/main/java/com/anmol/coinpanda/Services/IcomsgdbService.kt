package com.anmol.coinpanda.Services

import android.app.IntentService
import android.content.Intent
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.anmol.coinpanda.Adapters.IcoAdapter
import com.anmol.coinpanda.Adapters.IcomsgAdapter
import com.anmol.coinpanda.Fragments.ico
import com.anmol.coinpanda.Helper.Dbicohelper
import com.anmol.coinpanda.Helper.Dbicopinhelper
import com.anmol.coinpanda.Model.Icocoin
import com.anmol.coinpanda.Model.Icopin
import com.anmol.coinpanda.Mysingleton

class IcomsgdbService:IntentService("IcomsgdbService"){
    override fun onHandleIntent(p0: Intent?) {
        val db = Dbicopinhelper(this)
        var c = 0
        val jsonArray = JsonArrayRequest(Request.Method.GET,"https://www.cryptohype.live/ico/pinned",null, Response.Listener { response ->
            while (c<response.length()){
                val jsonObject = response.getJSONObject(c)
                val messageid = jsonObject.getInt("cID")
                val message = jsonObject.getString("pinned_messages")
                val messagetime = jsonObject.getString("date")
                val ico_name = jsonObject.getString("coin_name")
                val icopin = Icopin(messageid,ico_name,message,messagetime)
                db.insertData(icopin)
                c++
            }



        }, Response.ErrorListener {

        })
        Mysingleton.getInstance(applicationContext).addToRequestqueue(jsonArray)
    }
}