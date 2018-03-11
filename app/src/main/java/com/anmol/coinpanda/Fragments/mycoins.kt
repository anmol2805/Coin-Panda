package com.anmol.coinpanda.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.R
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by anmol on 3/11/2018.
 */
class mycoins : Fragment(){
    private var cointweetrecycler: RecyclerView?=null

    private lateinit var tweetselect: Switch
    lateinit var tweets : MutableList<Tweet>
    lateinit var itemClickListener : ItemClickListener
    var sedit: EditText? = null
    var srch: Button? = null
    var db = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.mycoins, container, false)

        return vi
    }
}