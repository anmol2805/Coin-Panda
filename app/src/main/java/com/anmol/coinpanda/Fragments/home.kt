package com.anmol.coinpanda.Fragments

import android.app.Fragment
import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Coin
import com.anmol.coinpanda.R
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home.*

/**
 * Created by anmol on 2/26/2018.
 */
class home : Fragment() {
    lateinit var coins : MutableList<Coin>
    lateinit var itemClickListener : ItemClickListener
    var db = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater?.inflate(R.layout.home,
                container, false)
        val layoutManager = LinearLayoutManager(activity)
        coinrecycler.layoutManager = layoutManager
        coinrecycler.setHasFixedSize(true)
        coinrecycler.itemAnimator = DefaultItemAnimator()
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
        db.collection("users").document("Z2ycXxL6GyvPS23NTuYk").addSnapshotListener{documentSnapshot, e ->
            Log.d(TAG, "Current data: " + documentSnapshot.data)


        }
    }


}