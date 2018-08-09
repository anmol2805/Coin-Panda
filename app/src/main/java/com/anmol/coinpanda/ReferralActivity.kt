package com.anmol.coinpanda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.HashMap

class ReferralActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_referral)
        refersubmit.setOnClickListener(View.OnClickListener {
            val referalcode = refercode.getText().toString()
            val databaseReference = FirebaseDatabase.getInstance().reference
            databaseReference.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        if (data.getValue(String::class.java) == referalcode) {
                            if (data.key != mAuth.getCurrentUser()!!.getUid()) {
                                val referrerid = data.key
                                val map = HashMap<String, Any>()
                                map[mAuth.getCurrentUser()!!.getUid()] = true
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
                                                        val intent = Intent(this@LoginActivity, LoadingActivity::class.java)
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
                                    //                                            databaseReference.child("referrers").child(referrerid).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                                    //                                                @Override
                                    //                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //                                                    if(dataSnapshot.exists()){
                                    //                                                        Integer counter = dataSnapshot.getValue(Integer.class);
                                    //                                                        counter = counter + 1;
                                    //                                                        Map<String,Object> map1 = new HashMap<>();
                                    //                                                        map1.put("count",counter);
                                    //                                                        databaseReference.child("referrers").child(referrerid).updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    //                                                            @Override
                                    //                                                            public void onComplete(@NonNull Task<Void> task) {
                                    //
                                    //                                                            }
                                    //                                                        });
                                    //                                                    }
                                    //                                                    else{
                                    //                                                        Map<String,Object> map1 = new HashMap<>();
                                    //                                                        map1.put("count",1);
                                    //                                                        databaseReference.child("referrers").child(referrerid).updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    //                                                            @Override
                                    //                                                            public void onComplete(@NonNull Task<Void> task) {
                                    //                                                                Intent intent = new Intent(LoginActivity.this,LoadingActivity.class);
                                    //                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    //                                                                startActivity(intent);
                                    //                                                                finish();
                                    //                                                                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
                                    //                                                            }
                                    //                                                        });
                                    //                                                    }
                                    //                                                }
                                    //
                                    //                                                @Override
                                    //                                                public void onCancelled(DatabaseError databaseError) {
                                    //
                                    //                                                }
                                    //                                            });
                                }
                            } else {
                                println("refererror internal")
                                Toast.makeText(this@LoginActivity, "Invalid referral code", Toast.LENGTH_SHORT).show()
                            }
                        }


                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        })
        skip.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, LoadingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out)
        })
    }
}
