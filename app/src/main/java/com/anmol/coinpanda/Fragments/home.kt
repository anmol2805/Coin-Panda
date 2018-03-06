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
import com.anmol.coinpanda.Adapters.GridnewAdapter
import com.anmol.coinpanda.AddToPortfolioActivity
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.Model.Coin
import com.anmol.coinpanda.R
import com.anmol.coinpanda.TweetsActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.home.*

/**
 * Created by anmol on 2/26/2018.
 */
class home : Fragment() {
    private var coingrid:GridView?=null
    private lateinit var gridAdapter:GridnewAdapter
    private lateinit var mcoinselect: Switch
    lateinit var allcoins:MutableList<Allcoin>
    lateinit var itemClickListener : ItemClickListener
    var db = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.home,
                container, false)
        coingrid = vi.findViewById(R.id.coingrid)
        mcoinselect = vi.findViewById(R.id.coinselect)
        allcoins = ArrayList()
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
        allcoins.clear()
        db.collection("AllCoins").orderBy("lastUpdate",Query.Direction.DESCENDING).addSnapshotListener{ documentSnapshot, firebaseFirestoreException ->
            allcoins.clear()
            for(doc in documentSnapshot.documents){
                val coinname = doc.getString("coin_symbol")
                val name = doc.getString("coin_name")
                val allcoin = Allcoin(coinname,name)
                allcoins.add(allcoin)
            }
            if(activity!=null){
                if(!allcoins.isEmpty()){
                    gridAdapter = GridnewAdapter(activity!!,allcoins)
                    coingrid?.adapter = gridAdapter
                }
            }

        }
    }

    private fun loaddata() {
        allcoins.clear()
        db.collection("users").document("MhqeP5vqgdadnSodwzPo").collection("portfolio").addSnapshotListener{documentSnapshot, e ->
            allcoins.clear()
            for(doc in documentSnapshot.documents){
                val coinname = doc.id
                val name = doc.getString("coin_name")
                val allcoin = Allcoin(coinname,name)
                allcoins.add(allcoin)
            }
            if(activity!=null){
                if(!allcoins.isEmpty()){
                    gridAdapter = GridnewAdapter(activity!!,allcoins)
                    coingrid?.adapter = gridAdapter
                }
            }


        }
    }


}