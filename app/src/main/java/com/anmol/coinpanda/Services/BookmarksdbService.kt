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
import com.anmol.coinpanda.Helper.Dbbookshelper
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


class BookmarksdbService : IntentService("BookmarksdbService") {

    override fun onHandleIntent(intent: Intent?) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val dbb = Dbbookshelper(baseContext)
        db.collection("users").document(auth.currentUser!!.uid).collection("bookmarks").get().addOnCompleteListener {
            task ->
            for (doc in task.result.documents){
                val tweetid = doc.id
                dbb.insertData(tweetid)
            }

        }
    }
}
