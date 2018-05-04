package com.anmol.coinpanda.Services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.anmol.coinpanda.Helper.Dbhelper
import com.anmol.coinpanda.Model.Sqltweet
import com.anmol.coinpanda.Mysingleton
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList


class CoinsdbService : IntentService("CoinsdbService") {

    override fun onHandleIntent(intent: Intent?) {

    }
}
