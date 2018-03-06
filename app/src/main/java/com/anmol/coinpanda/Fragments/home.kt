package com.anmol.coinpanda.Fragments

import android.support.v4.app.Fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.Switch
import com.anmol.coinpanda.Adapters.AllCoinAdapter
import com.anmol.coinpanda.Adapters.CoinAdapter
import com.anmol.coinpanda.Adapters.GridAdapter
import com.anmol.coinpanda.AddToPortfolioActivity
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.Model.Coin
import com.anmol.coinpanda.R
import com.anmol.coinpanda.TweetsActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home.*

/**
 * Created by anmol on 2/26/2018.
 */
class home : Fragment() {
    private var mcoinrecycler:RecyclerView? = null
    private var coingrid:GridView?=null
    private lateinit var gridAdapter:GridAdapter
    private lateinit var mcoinselect: Switch
    lateinit var coins : MutableList<Coin>
    lateinit var allcoins:MutableList<Allcoin>
    lateinit var itemClickListener : ItemClickListener
    var db = FirebaseFirestore.getInstance()
    lateinit var coinAdapter : CoinAdapter
    lateinit var allCoinAdapter: AllCoinAdapter
    private var portfolio:Button?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.home,
                container, false)
        val layoutManager = LinearLayoutManager(activity)
        portfolio = vi.findViewById(R.id.portfolio)
        mcoinrecycler = vi.findViewById(R.id.coinrecycler)
        mcoinselect = vi.findViewById(R.id.coinselect)
        mcoinrecycler?.layoutManager   = layoutManager
        mcoinrecycler?.setHasFixedSize(true)
        mcoinrecycler?.itemAnimator   = DefaultItemAnimator()
        coins = ArrayList()
        allcoins = ArrayList()
        itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {
                val intent = Intent(activity,TweetsActivity::class.java)
                if(!coins.isEmpty()){
                    intent.putExtra("coin",coins[pos].coinname)
                }
                else if(!allcoins.isEmpty()){
                    intent.putExtra("coin",allcoins[pos].coinname)
                }

                startActivity(intent)
            }

        }
        portfolio?.setOnClickListener({
            startActivity(Intent(activity,AddToPortfolioActivity::class.java))
        })

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
                    gridAdapter = GridAdapter(activity!!,R.layout.coinlayout,allcoins)
                    coingrid?.adapter = gridAdapter
//                    allCoinAdapter = AllCoinAdapter(activity!!,allcoins,itemClickListener)
//                    coinrecycler.adapter = allCoinAdapter
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
                val allcoin = Allcoin(coinname)
                allcoins.add(allcoin)
            }
            if(activity!=null){
                if(!allcoins.isEmpty()){
                    gridAdapter = GridAdapter(activity!!,R.layout.coinlayout,allcoins)
                    coingrid?.adapter = gridAdapter
//                    coinAdapter = CoinAdapter(activity!!,coins,itemClickListener)
//                    coinrecycler.adapter = coinAdapter
                }
            }


        }
    }


}