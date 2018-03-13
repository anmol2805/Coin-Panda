package com.anmol.coinpanda

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class SupportActivity : AppCompatActivity() {
    internal var feedback: EditText? = null
    internal var done: Button? = null
    internal var gmail: Button? = null
    internal var auth = FirebaseAuth.getInstance()
    internal var db = FirebaseFirestore.getInstance()
    internal var prgbr: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
        gmail = findViewById<View>(R.id.gmail) as Button
        feedback = findViewById<View>(R.id.feedback) as EditText
        done = findViewById<View>(R.id.done) as Button
        prgbr = findViewById<View>(R.id.prgbr) as ProgressBar
        prgbr?.visibility = View.INVISIBLE
        gmail?.setOnClickListener {
            val viewIntent = Intent("android.intent.action.VIEW",
                    Uri.parse("mailto:" + "canopydevelopers@gmail.com"))
            startActivity(viewIntent)
        }
        done?.setOnClickListener {
            if (feedback?.text != null && !feedback?.text.toString().isEmpty()) {
                prgbr?.visibility = View.VISIBLE
                val map = HashMap<String, Any>()
                map["feedback"] = feedback?.text.toString()
                map["uid"] = auth.currentUser!!.uid
                val ref = db.collection("feedback").document()
                val id = ref.id
                db.collection("feedback").document(id).set(map).addOnSuccessListener {
                    Toast.makeText(this@SupportActivity, "Feedback submitted!!!", Toast.LENGTH_SHORT).show()
                    prgbr?.visibility = View.INVISIBLE
                }.addOnFailureListener {
                    Toast.makeText(this@SupportActivity, "Network Error", Toast.LENGTH_SHORT).show()
                    prgbr?.visibility = View.INVISIBLE
                }
            }
        }
    }
}
