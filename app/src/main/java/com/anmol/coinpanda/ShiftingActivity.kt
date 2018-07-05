package com.anmol.coinpanda

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anmol.coinpanda.Model.Sqlcoin
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ShiftingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shifting)
        var counter = 0
        val db = FirebaseFirestore.getInstance()
        val databaseReference = FirebaseDatabase.getInstance().reference.child("database")
        db.collection("topics").get().addOnCompleteListener {

            task ->
            System.out.print("counter activity started")
            val documentSnapshot = task.result
            for(doc in task.result.documents){
                System.out.print("counteruid " + doc.id)
//                db.collection("users").document(doc.id).collection("portfolio").get().addOnCompleteListener {
//                    task2 ->
//                    val documentSnapshot2 = task2.result
//
//                    val s = documentSnapshot2.size()
//
//                    if(s!=0){
//                        for(doc2 in documentSnapshot2){
//                            val coinname = doc2.getString("coin_name")
//                            val coinsymbol = doc2.id
//                            val coinpage = doc2.getString("coinPage")
//                            val sqlcoin = Sqlcoin(coinname,coinsymbol,coinpage)
//                            databaseReference.child(doc.id).child("portfolio").child(coinsymbol).setValue(sqlcoin)
//                        }
//
//
//                    }
//
//                }
//                db.collection("users").document(doc.id).collection("topics").get().addOnCompleteListener {
//                    task3 ->
//                    val documentSnapshot3 = task3.result
//
//                    val s = documentSnapshot3.size()
//
//                    if(s!=0){
//                        for(doc3 in documentSnapshot3){
//                            val coinname = doc3.getString("coinname")
//                            val map = HashMap<String,Any>()
//                            map["coinname"] = coinname!!
//                            map["notify"] = true
//                            databaseReference.child(doc.id).child("topics").child(doc3.id).setValue(map)
//                        }
//
//
//                    }
//
//                }
//                db.collection("users").document(doc.id).collection("bookmarks").get().addOnCompleteListener {
//                    task4 ->
//                    for (doc4 in task4.result.documents){
//                        val tweetid = doc4.id
//                        val map = HashMap<String,Any>()
//                        map[tweetid] = true
//                        databaseReference.child(doc.id).child("bookmarks").updateChildren(map)
//                    }
//
//                }
                counter += 1
            }
            System.out.print("counter = $counter")
        }
    }
}
