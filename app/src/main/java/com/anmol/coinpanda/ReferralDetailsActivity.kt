package com.anmol.coinpanda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_referral_details.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReferralDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_referral_details)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        neoaddress.clearFocus()
        ethaddress.clearFocus()
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseDatabase.getInstance().reference
        db.child("users").child(auth.currentUser!!.uid).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                System.out.println("Referralerror$p0")
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    System.out.println("Referralerror$p0")
                    val refercode:String = p0.value as String
                    referralcode.text = refercode

                    referralcode.setOnClickListener{

                        val shareintent = Intent()
                        shareintent.action = Intent.ACTION_SEND
                        shareintent.type = "text/plain"
                        shareintent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.anmol.coinpanda" + " to download the CryptoHype app and use my code " + p0.value.toString() + " after login to get monthly airdrop.")
                        startActivity(Intent.createChooser(shareintent,"Share code via..."))
                    }
                    referralshare.setOnClickListener{
                        val shareintent = Intent()
                        shareintent.action = Intent.ACTION_SEND
                        shareintent.type = "text/plain"
                        shareintent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.anmol.coinpanda" + " to download the CryptoHype app and use my code " + p0.value.toString() + " after login to get monthly airdrop.")
                        startActivity(Intent.createChooser(shareintent,"Share code via..."))
                    }
                }
                else{
                    referralcode.visibility = View.GONE
                }
            }

        })

        db.child("referrers").child(auth.currentUser!!.uid).child("count").addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    val refercount:Long = p0.value as Long
                    referralcount?.text = refercount.toString()
                }
                else{
                    referralcount?.text = "0"
                }
            }

        })

    }
}
