package com.anmol.coinpanda.Fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Switch
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.anmol.coinpanda.Adapters.TweetsAdapter
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.Mysingleton
import com.anmol.coinpanda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by anmol on 2/26/2018.
 */
class dashboard : Fragment() {
    private var cointweetrecycler:RecyclerView?=null

    private lateinit var tweetselect:Switch
    lateinit var tweets : MutableList<Tweet>
    lateinit var itemClickListener : ItemClickListener
    var sedit: EditText? = null
    var srch: Button? = null
    var db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    var pgr :ProgressBar?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.dashboard, container, false)
        tweetselect = vi.findViewById(R.id.cointweetselect)
        pgr = vi.findViewById(R.id.pgr)
        pgr?.visibility = View.VISIBLE
        //updaterequest()
        //moverequest()
        pgr?.visibility = View.GONE
        tweetselect.isChecked = false
        setFragment(allcoins())
//        db.collection("users").document(auth.currentUser!!.uid).collection("portfolio").get().addOnCompleteListener {
//            task ->
//            val documentSnapshot = task.result
//            val s = documentSnapshot.size()
//            if(s!=0){
//                pgr?.visibility = View.GONE
//                tweetselect.isChecked = true
//                setFragment(mycoins())
//            }
//            else{
//                pgr?.visibility = View.GONE
//                tweetselect.isChecked = false
//                setFragment(allcoins())
//            }
//        }

        tweetselect.setOnCheckedChangeListener { _, b ->
            if(b){
                setFragment(mycoins())
            }
            else{
                setFragment(allcoins())
            }
        }
        
        return vi
    }

    private fun setFragment(fragment: Fragment) {
        if(activity!=null){
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.coinframe,fragment).commit()
        }



    }
    private fun updaterequest() {
        val stringRequest = StringRequest(Request.Method.GET,"http://165.227.98.190/update", Response.Listener { response ->
            System.out.println(response)
        }, Response.ErrorListener { error->
            System.out.println(error)
        })
        if(activity!=null){
            Mysingleton.getInstance(activity).addToRequestqueue(stringRequest)
        }

    }
    private fun moverequest() {
        val stringRequest = StringRequest(Request.Method.GET,"http://165.227.98.190/move", Response.Listener { response ->
            System.out.println(response)
        }, Response.ErrorListener { error->
            System.out.println(error)
        })
        if(activity!=null){
            Mysingleton.getInstance(activity).addToRequestqueue(stringRequest)
        }

    }

}