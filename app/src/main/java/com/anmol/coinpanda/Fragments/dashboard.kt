package com.anmol.coinpanda.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.R
import com.anmol.coinpanda.TweetsActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.dashboard.*
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
    var db = FirebaseFirestore.getInstance()
    lateinit var timestamp :Timestamp
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.dashboard, container, false)
        val layoutManager = LinearLayoutManager(activity)
        cointweetrecycler = vi.findViewById(R.id.cointweetrecycler)
        tweetselect = vi.findViewById(R.id.cointweetselect)
        cointweetrecycler?.layoutManager   = layoutManager
        cointweetrecycler?.setHasFixedSize(true)
        cointweetrecycler?.itemAnimator   = DefaultItemAnimator()
        tweets = ArrayList()
        cointweetselect.isChecked = true
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH,-1)
        val stringtime = format.format(cal.time)

        System.out.println("previousdate:" + format.format(cal.time))
        itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {

            }

        }
        loaddatatweet()
        cointweetselect.setOnCheckedChangeListener({ compoundButton, b ->
            if (b){
                loaddatatweet()
            }
            else{
                loadalldatatweet()
            }
        })


        return vi
    }

    private fun loadalldatatweet() {
        tweets.clear()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH,-1)
        val stringtime = format.format(cal.time)
        val prevtime = Timestamp.valueOf(stringtime)
        db.collection("supernode").document("coins").collection("tweets").whereGreaterThanOrEqualTo("timestamp",prevtime)
                .orderBy("timestamp",Query.Direction.DESCENDING).addSnapshotListener{documentSnapshot,e->
            tweets.clear()
        }


    }

    private fun loaddatatweet() {
        tweets.clear()
        val mycoins :ArrayList<String> = ArrayList()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH,-1)
        val stringtime = format.format(cal.time)
        val prevtime = Timestamp.valueOf(stringtime)
        db.collection("Tweets").document("coins").collection("tweets").whereGreaterThanOrEqualTo("timestamp",prevtime)
                .orderBy("timestamp",Query.Direction.DESCENDING).addSnapshotListener{documentSnapshot,e->
            tweets.clear()
            db.collection("users").document("Z2ycXxL6GyvPS23NTuYk").collection("portfolio").addSnapshotListener{ds,er->
                ds.mapTo(mycoins) { it.id }
            }
            for(doc in documentSnapshot){
                var i = 0
                while (i<mycoins.size){
                    if(doc.getString("coinname").contains(mycoins[i])){

                    }
                    i++
                }
            }

        }
    }
}