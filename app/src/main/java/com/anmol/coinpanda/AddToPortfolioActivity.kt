package com.anmol.coinpanda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.anmol.coinpanda.Adapters.AddCoinAdapter
import com.anmol.coinpanda.Adapters.AllCoinAdapter
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Allcoin
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home.*

class AddToPortfolioActivity : AppCompatActivity() {

    private var addcoinrecycler:RecyclerView? = null
    lateinit var allcoins :MutableList<Allcoin>
    lateinit var addCoinAdapter:AddCoinAdapter
    lateinit var itemClickListener : ItemClickListener
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_portfolio)
        val layoutManager = LinearLayoutManager(this)
        addcoinrecycler = findViewById(R.id.addcoinrecycler)
        addcoinrecycler?.layoutManager  = layoutManager
        addcoinrecycler?.setHasFixedSize(true)
        addcoinrecycler?.itemAnimator   = DefaultItemAnimator()
        allcoins = ArrayList()
        itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {
            }

        }
        loadalldata()
    }

    private fun loadalldata() {
        allcoins.clear()
        db.collection("supernode").document("allcoins").collection("names").addSnapshotListener{documentSnapshot, firebaseFirestoreException ->
            allcoins.clear()
            for(doc in documentSnapshot.documents){
                val coinname = doc.id
                val allcoin = Allcoin(coinname)
                allcoins.add(allcoin)
            }
            if(!allcoins.isEmpty()){
                    addCoinAdapter = AddCoinAdapter(this,allcoins,itemClickListener)
                    addcoinrecycler?.adapter = addCoinAdapter
            }


        }
    }
}
