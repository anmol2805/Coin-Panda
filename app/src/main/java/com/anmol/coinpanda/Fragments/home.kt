package com.anmol.coinpanda.Fragments

import android.app.Fragment
import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anmol.coinpanda.Adapters.CoinAdapter
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Coin
import com.anmol.coinpanda.R
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home.*
import org.json.JSONObject

/**
 * Created by anmol on 2/26/2018.
 */
class home : Fragment() {
    private var mcoinrecycler:RecyclerView? = null
    lateinit var coins : MutableList<Coin>
    lateinit var itemClickListener : ItemClickListener
    var db = FirebaseFirestore.getInstance()
    lateinit var coinAdapter : CoinAdapter
    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater?.inflate(R.layout.home,
                container, false)
        val layoutManager = LinearLayoutManager(activity)
        mcoinrecycler = vi?.findViewById(R.id.coinrecycler)
        mcoinrecycler?.layoutManager   = layoutManager
        mcoinrecycler?.setHasFixedSize(true)
        mcoinrecycler?.itemAnimator   = DefaultItemAnimator()
        coins = ArrayList()
        itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        loaddata()
        // Inflate the layout for this fragment
        return vi
    }

    private fun loaddata() {
        coins.clear()
        db.collection("users").document("Z2ycXxL6GyvPS23NTuYk").collection("portfolio").addSnapshotListener{documentSnapshot, e ->
            for(doc in documentSnapshot.documents){
                val coinname = doc.id
                val coinnotify = doc.getBoolean("notify")
                val coin = Coin(coinname)
                coins.add(coin)
            }
            if(activity!=null){
                if(!coins.isEmpty()){
                    coinAdapter = CoinAdapter(activity,coins,itemClickListener)
                    coinrecycler.adapter = coinAdapter
                }
            }


        }
    }


}