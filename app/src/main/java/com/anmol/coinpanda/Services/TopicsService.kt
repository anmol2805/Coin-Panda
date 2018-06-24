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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList


class TopicsService : IntentService("TopicsService") {

    override fun onHandleIntent(intent: Intent?) {

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val databaseReference = FirebaseDatabase.getInstance().reference.child("topics")
        db.collection("topics").get().addOnCompleteListener {
            task ->
            val documentSnapshot = task.result

            val s = documentSnapshot.size()

            if(s!=0){
                for(doc in documentSnapshot){
                    val coin_symbol = doc.getString("coin_symbol")
                    val count = doc.getLong("count")
                    val map = HashMap<String,Any>()
                    map["coin_symbol"] = coin_symbol!!
                    map["count"] = count!!
                    databaseReference.child(doc.id).setValue(map)
                }


            }

        }
    }
}
