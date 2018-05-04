package com.anmol.coinpanda.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anmol.coinpanda.Helper.Dbhelper;
import com.anmol.coinpanda.Model.Sqltweet;
import com.anmol.coinpanda.Mysingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TweetsdbService extends IntentService {

    public TweetsdbService() {
        super("TweetsdbService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://165.227.98.190/tweets", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int c = 0;
                try {
                    JSONArray jsonArray = response.getJSONArray("tweets");
                    List<Sqltweet> sqltweets = new ArrayList<>();
                    sqltweets.clear();

                    while (c<450){
                        JSONObject obj = jsonArray.getJSONObject(c);
                        String id = obj.getString("id");
                        String coin = obj.getString("coin_name");
                        String coin_symbol = obj.getString("coin_symbol");
                        String mtweet = obj.getString("tweet");
                        String url = obj.getString("url");
                        String keyword = obj.getString("keyword");
                        String dates = obj.getString("date");
                        String coinpage = obj.getString("coin_handle");
                        Sqltweet sqltweet = new Sqltweet(coin,coin_symbol,mtweet,url,keyword,id,dates,coinpage);
                        Dbhelper db = new Dbhelper(getBaseContext());
                        db.insertData(sqltweet);
                        System.out.println("tweetno" + c);
                        c++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Mysingleton.getInstance(getBaseContext()).addToRequestqueue(jsonObjectRequest);
    }
}
