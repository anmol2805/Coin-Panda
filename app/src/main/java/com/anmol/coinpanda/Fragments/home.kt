package com.anmol.coinpanda.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.v4.app.Fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.anmol.coinpanda.Adapters.GridnewAdapter
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.PaymentActivity
import com.anmol.coinpanda.R
import com.anmol.coinpanda.TweetsActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * Created by anmol on 2/26/2018.
 */
class home : Fragment() {

    private lateinit var mcoinselect: Switch
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.home,
                container, false)
        mcoinselect = vi.findViewById(R.id.coinselect)
        db.collection("users").document(auth.currentUser!!.uid).collection("portfolio").get().addOnCompleteListener {
            task ->
            val documentSnapshot = task.result
            val s = documentSnapshot.size()
            if(s!=0){
                mcoinselect.isChecked = true
                setFragment(mycoinslist())
            }
            else{
                mcoinselect.isChecked = false
                setFragment(coinslist())
            }
        }


        mcoinselect.setOnCheckedChangeListener({ compoundButton, b ->
            if (b){
                setFragment(mycoinslist())
            }
            else{
                setFragment(coinslist())
            }
        })


        // Inflate the layout for this fragment
        return vi
    }
    private fun setFragment(fragment: Fragment) {
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment,fragment).commit()


    }





}