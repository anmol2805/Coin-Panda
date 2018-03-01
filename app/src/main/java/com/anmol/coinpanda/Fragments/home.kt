package com.anmol.coinpanda.Fragments

import android.support.v4.app.Fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import com.anmol.coinpanda.Adapters.AllCoinAdapter
import com.anmol.coinpanda.Adapters.CoinAdapter
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Allcoin
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
    private lateinit var mcoinselect: Switch
    lateinit var coins : MutableList<Coin>
    lateinit var allcoins:MutableList<Allcoin>
    lateinit var itemClickListener : ItemClickListener
    var db = FirebaseFirestore.getInstance()
    lateinit var coinAdapter : CoinAdapter
    lateinit var allCoinAdapter: AllCoinAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.home,
                container, false)
        val layoutManager = LinearLayoutManager(activity)
        mcoinrecycler = vi.findViewById(R.id.coinrecycler)
        mcoinselect = vi.findViewById(R.id.coinselect)
        mcoinrecycler?.layoutManager   = layoutManager
        mcoinrecycler?.setHasFixedSize(true)
        mcoinrecycler?.itemAnimator   = DefaultItemAnimator()
        coins = ArrayList()
        allcoins = ArrayList()
        itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {

            }

        }

        mcoinselect.isChecked = true
        loaddata()
        mcoinselect.setOnCheckedChangeListener({ compoundButton, b ->
            if (b){
                loaddata()
            }
            else{
                loadalldata()
            }
        })

        // Inflate the layout for this fragment
        return vi
    }

    private fun loadalldata() {
        coins.clear()
        allcoins.clear()
        db.collection("supernode").document("allcoins").collection("names").addSnapshotListener{documentSnapshot, firebaseFirestoreException ->
            coins.clear()
            allcoins.clear()
            for(doc in documentSnapshot.documents){
                val coinname = doc.id
                val allcoin = Allcoin(coinname)
                allcoins.add(allcoin)
            }
            if(activity!=null){
                if(!allcoins.isEmpty()){
                    allCoinAdapter = AllCoinAdapter(activity!!,allcoins,itemClickListener)
                    coinrecycler.adapter = allCoinAdapter
                }
            }

        }
    }

    private fun loaddata() {
        coins.clear()
        allcoins.clear()
        db.collection("users").document("Z2ycXxL6GyvPS23NTuYk").collection("portfolio").addSnapshotListener{documentSnapshot, e ->
            coins.clear()
            allcoins.clear()
            for(doc in documentSnapshot.documents){
                val coinname = doc.id
                val coinnotify = doc.getBoolean("notify")
                val coin = Coin(coinname,coinnotify)
                coins.add(coin)
            }
            if(activity!=null){
                if(!coins.isEmpty()){
                    coinAdapter = CoinAdapter(activity!!,coins,itemClickListener)
                    coinrecycler.adapter = coinAdapter
                }
            }


        }
    }


}