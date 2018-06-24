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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList


class CoinsdbService : IntentService("CoinsdbService") {

    override fun onHandleIntent(intent: Intent?) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val databaseReference = FirebaseDatabase.getInstance().reference.child("database").child(auth.currentUser!!.uid).child("portfolio")
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                val dcb = Dbcoinshelper(baseContext)
                if(p0!!.exists()){
                    for(doc in p0.children){
                        val coinname = doc.child("coin").value.toString()
                        val coinsymbol = doc.child("coin_symbol").value.toString()
                        val coinpage = doc.child("coinpage").value.toString()
                        val sqlcoin = Sqlcoin(coinname,coinsymbol,coinpage)
                        System.out.println("realdbtesting:$sqlcoin")
                        dcb.insertData(sqlcoin)
                    }
                }
            }

        })

    }
}
