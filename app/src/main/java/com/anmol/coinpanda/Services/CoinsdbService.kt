package com.anmol.coinpanda.Services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.anmol.coinpanda.Fragments.coinslist
import com.anmol.coinpanda.Helper.Dbcoinshelper
import com.anmol.coinpanda.Helper.Dbhelper
import com.anmol.coinpanda.Model.Sqlcoin
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Mysingleton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList


class CoinsdbService : IntentService("CoinsdbService") {

    override fun onHandleIntent(intent: Intent?) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        db.collection("users").document(auth.currentUser!!.uid).collection("portfolio").get().addOnCompleteListener {
            task ->
            val documentSnapshot = task.result

            val s = documentSnapshot.size()
            val dcb = Dbcoinshelper(baseContext)
            if(s!=0){
                for(doc in documentSnapshot){
                    val coinname = doc.getString("coin_name")
                    val coinsymbol = doc.id
                    val coinpage = doc.getString("coinPage")
                    val sqlcoin = Sqlcoin(coinname,coinsymbol,coinpage)
                    dcb.insertData(sqlcoin)
                }


            }

        }
    }
}
