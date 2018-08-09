package com.anmol.coinpanda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_referral.*
import java.util.HashMap

class ReferralActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_referral)
        submitref.setOnClickListener {
            val referalcode = referralcode.text.toString()
            val databaseReference = FirebaseDatabase.getInstance().reference
            databaseReference.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        if (data.getValue(String::class.java) == referalcode) {
                            if (data.key != mAuth.currentUser!!.uid) {
                                val referrerid = data.key
                                val map = HashMap<String, Any>()
                                map[mAuth.currentUser!!.uid] = true
                                databaseReference.child("referrers").child(referrerid!!).updateChildren(map).addOnCompleteListener {
                                    databaseReference.child("referrers").child(referrerid).addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val k = dataSnapshot.childrenCount
                                            val map1 = HashMap<String, Any>()
                                            if (k == 0L) {
                                                map1["count"] = 1
                                            } else {
                                                map1["count"] = k - 1
                                            }

                                            databaseReference.child("referrers").child(referrerid).updateChildren(map1)
                                                    .addOnCompleteListener {
                                                        val intent = Intent(this@ReferralActivity, LoadingActivity::class.java)
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                        startActivity(intent)
                                                        finish()
                                                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
                                                    }
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {

                                        }
                                    })

                                }
                            } else {
                                println("refererror internal")
                                Toast.makeText(this@ReferralActivity, "Invalid referral code", Toast.LENGTH_SHORT).show()
                            }
                        }


                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
        skip.setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        }
    }
}
