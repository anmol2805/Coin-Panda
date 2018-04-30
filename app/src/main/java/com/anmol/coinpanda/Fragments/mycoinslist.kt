package com.anmol.coinpanda.Fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.anmol.coinpanda.Adapters.GridnewAdapter
import com.anmol.coinpanda.Helper.Dbcoinshelper
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.Model.Sqlcoin
import com.anmol.coinpanda.R
import com.anmol.coinpanda.TweetsActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Created by anmol on 3/15/2018.
 */
class mycoinslist : Fragment(){
    private var coingrid: GridView?=null
    private lateinit var gridAdapter: GridnewAdapter
    lateinit var allcoins:MutableList<Allcoin>
    lateinit var itemClickListener : ItemClickListener
    var db = FirebaseFirestore.getInstance()
    var empty: TextView? = null
    val auth = FirebaseAuth.getInstance()
    val messaging = FirebaseMessaging.getInstance()
    var pgr :ProgressBar?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.mycoinslist, container, false)
        coingrid = vi.findViewById(R.id.coingrid)
        empty = vi.findViewById(R.id.empty)
        pgr = vi.findViewById(R.id.pgr)
        empty?.visibility = View.GONE
        allcoins = ArrayList()
        val handler = Handler()
        handler.postDelayed({
            loaddata()
        },200)

        coingrid?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            if(activity!=null){
                val dialog = Dialog(activity)
                dialog.setContentView(R.layout.dialoglayout)
                val prg : ProgressBar? = dialog.findViewById(R.id.prgbr)
                val coinimg: ImageView? = dialog.findViewById(R.id.coinimg)
                val cn :TextView? = dialog.findViewById(R.id.cn)
                val cs:TextView? = dialog.findViewById(R.id.cs)
                val cp:TextView? = dialog.findViewById(R.id.cp)
                val atp:Button? = dialog.findViewById(R.id.atp)
                val portfoliolay: LinearLayout?= dialog.findViewById(R.id.portfoliolay)
                val viewtweet:Button?=dialog.findViewById(R.id.viewtweets)
                val notificationswitch: Switch?= dialog.findViewById(R.id.notification)
                val remove:Button? = dialog.findViewById(R.id.remove)
                atp?.visibility = View.VISIBLE
                portfoliolay?.visibility = View.GONE
                db.collection("users").document(auth.currentUser!!.uid).collection("topics").get().addOnCompleteListener { task ->
                    val snapshot = task.result
                    for(doc in snapshot){
                        if(doc.id.contains(allcoins[i].coinname!!) && doc.getString("coinname").contains(allcoins[i].coin!!)){
                            val notify = doc.getBoolean("notify")
                            if (!notify!!){
                                notificationswitch?.isChecked = false
                            }
                        }

                    }



                }
                notificationswitch?.isChecked = true
                notificationswitch?.setOnCheckedChangeListener { _, b ->
                    if (b){
                        val map = HashMap<String,Any>()
                        map["notify"] = true
                        topicsearch(0,allcoins[i].coinname,allcoins[i].coin)
                        db.collection("users").document(auth.currentUser!!.uid).collection("portfolio").document(allcoins[i].coinname!!).update(map)
                    }
                    else{
                        db.collection("users").document(auth.currentUser!!.uid).collection("topics").get().addOnCompleteListener{task->
                            val documenSnapshot = task.result
                            for(doc in documenSnapshot){
                                if(doc.id.contains(allcoins[i].coinname!!) && doc.getString("coinname") == allcoins[i].coin){
                                    db.collection("users").document(auth.currentUser!!.uid).collection("topics").document(doc.id).delete().addOnSuccessListener{
                                        messaging.unsubscribeFromTopic(doc.id)
                                        db.collection("topics").document(doc.id).get().addOnCompleteListener{task ->
                                            val documentSnapshot = task.result
                                            val count = documentSnapshot.getLong("count")
                                            if (count>0){
                                                val map  = java.util.HashMap<String, Any>()
                                                map["count"] = count - 1
                                                db.collection("topics").document(doc.id).update(map)
                                            }
                                            else{

                                            }
                                        }


                                    }

                                }

                            }

                        }
                        val map = HashMap<String,Any>()
                        map["notify"] = false
                        db.collection("users").document(auth.currentUser!!.uid).collection("portfolio").document(allcoins[i].coinname!!).update(map)
                    }
                }
                db.collection("users").document(auth.currentUser!!.uid).collection("portfolio").get().addOnCompleteListener {task ->
                    for(doc in task.result){
                        if(doc.id == allcoins[i].coinname!!){
                            atp?.visibility = View.GONE
                            portfoliolay?.visibility = View.VISIBLE
                        }
                    }
                }
                cn?.text = allcoins[i].coin
                cs?.text = allcoins[i].coinname
                cp?.text = "#"+ allcoins[i].coinpage
                val testurl = "https://twitter.com/" + allcoins[i].coinpage + "/profile_image?size=original"
                if(activity!=null){
                    Glide.with(activity).load(testurl).into(coinimg)
                }
                viewtweet?.setOnClickListener {
                    if(activity!=null){
                        val intent = Intent(activity, TweetsActivity::class.java)
                        intent.putExtra("coin", allcoins[i].coinname)
                        startActivity(intent)
                    }

                }
                remove?.setOnClickListener {
                    val sqlcoin = Sqlcoin(allcoins[i].coin, allcoins[i].coinname, allcoins[i].coinpage)
                    val dcb = Dbcoinshelper(activity!!)
                    dcb.deleteCoin(sqlcoin)
                    db.collection("users").document(auth.currentUser!!.uid).collection("topics").get().addOnCompleteListener{task->
                        val documenSnapshot = task.result
                        for(doc in documenSnapshot){
                            if(doc.id.contains(allcoins[i].coinname!!) && doc.getString("coinname") == allcoins[i].coin){
                                db.collection("users").document(auth.currentUser!!.uid).collection("topics").document(doc.id).delete().addOnSuccessListener{
                                    messaging.unsubscribeFromTopic(doc.id)
                                    db.collection("users").document(auth.currentUser!!.uid).collection("portfolio").document(allcoins[i].coinname!!)
                                            .delete().addOnSuccessListener {
                                                if(activity!=null){
                                                    Toast.makeText(activity,"Removed from your Portfolio", Toast.LENGTH_SHORT).show()
                                                }

                                            }
                                    db.collection("topics").document(doc.id).get().addOnCompleteListener{task ->
                                        val documentSnapshot = task.result
                                        val count = documentSnapshot.getLong("count")
                                        if (count>0){
                                            val map  = java.util.HashMap<String, Any>()
                                            map["count"] = count - 1
                                            db.collection("topics").document(doc.id).update(map)
                                        }
                                        else{

                                        }
                                    }


                                }

                            }

                        }

                    }

                    dialog.dismiss()

                }
                atp?.setOnClickListener {
                    //                val intent = Intent(activity,PaymentActivity::class.java)
//                intent.putExtra("coin",allcoins[i].coin)
//                startActivity(intent)
                    val sqlcoin = Sqlcoin(allcoins[i].coin, allcoins[i].coinname, allcoins[i].coinpage)
                    val dcb = Dbcoinshelper(activity!!)
                    dcb.insertData(sqlcoin)
                    topicsearch(0, allcoins[i].coinname, allcoins[i].coin)
                    prg?.visibility = View.VISIBLE
                    atp.visibility = View.GONE
                    val map = HashMap<String,Any>()
                    map["coin_name"] = allcoins[i].coin.toString()
                    map["coinPage"] = allcoins[i].coinpage.toString()
                    map["notify"] = true
                    db.collection("users").document(auth.currentUser!!.uid).collection("portfolio").document(allcoins[i].coinname!!).set(map).addOnSuccessListener {
                        if(activity!=null){
                            Toast.makeText(activity,"Added to your Portfolio", Toast.LENGTH_SHORT).show()
                        }


                    }
                    topicsearch(0, allcoins[i].coinname, allcoins[i].coin)
                    dialog.dismiss()
                }
                dialog.show()
            }

        }
        return vi

    }
    private fun removetopic(id: String) {

        db.collection("topics").document(id).get().addOnCompleteListener{task ->
            val documentSnapshot = task.result
            val count = documentSnapshot.getLong("count")
            if (count>0){
                val map  = java.util.HashMap<String, Any>()
                map["count"] = count - 1
                db.collection("topics").document(id).set(map)
            }
            else{

            }
        }
        db.collection("users").document(auth.currentUser!!.uid).collection("topics").document(id).delete()
    }
    private fun topicsearch(i: Int, coinname: String?, coin: String?) {
        db.collection("topics").document(coinname + i.toString()).get().addOnCompleteListener { task->
            val documentSnapshot = task.result
            if(documentSnapshot.exists()){
                val count = documentSnapshot.getLong("count")
                if(count > 990){
                    topicsearch(i+1, coinname, allcoins[i].coin)
                }
                else{
                    val map  = HashMap<String,Any>()
                    map["notify"] = true
                    map["coinname"] = coin!!
                    db.collection("users").document(auth.currentUser!!.uid).collection("topics").document(coinname + i.toString())
                            .set(map).addOnSuccessListener {
                                messaging.subscribeToTopic(coinname + i.toString())
                                val countmap = java.util.HashMap<String, Any>()
                                countmap["count"] = count+1
                                countmap["coin_symbol"] = coinname.toString()
                                db.collection("topics").document(coinname + i.toString()).set(countmap)
                            }
                }
            }
            else{
                val map  = HashMap<String,Any>()
                map["notify"] = true
                map["coinname"] = coin!!
                db.collection("users").document(auth.currentUser!!.uid).collection("topics").document(coinname + i.toString())
                        .set(map).addOnSuccessListener {
                            messaging.subscribeToTopic(coinname + i.toString())
                            val count = HashMap<String,Any>()
                            count["count"] = 1
                            count["coin_symbol"] = coinname.toString()
                            db.collection("topics").document(coinname + i.toString()).set(count)
                        }
            }

        }
    }

    private fun loaddata() {
        allcoins.clear()
        pgr?.visibility = View.VISIBLE
        if(activity!=null){
            val dcb = Dbcoinshelper(activity!!)
            val data = dcb.readData()
            allcoins = data
            if(!allcoins.isEmpty()){
                pgr?.visibility = View.GONE
                empty?.visibility = View.GONE
                gridAdapter = GridnewAdapter(activity!!, allcoins)
                gridAdapter.notifyDataSetChanged()
                coingrid?.adapter = gridAdapter
            }
            else{
                pgr?.visibility = View.GONE
                empty?.visibility = View.VISIBLE
            }
        }
//        db.collection("users").document(auth.currentUser!!.uid).collection("portfolio")
//                .get().addOnCompleteListener{
//                    task ->
//                    allcoins.clear()
//                    for(doc in task.result.documents){
//                        val coinname = doc.id
//                        val name = doc.getString("coin_name")
//                        val coinpage = doc.getString("coinPage")
//                        val allcoin = Allcoin(coinname,name,coinpage)
//                        allcoins.add(allcoin)
//                    }
//                    if(activity!=null){
//                        if(!allcoins.isEmpty()){
//                            pgr?.visibility = View.GONE
//                            empty?.visibility = View.GONE
//                            gridAdapter = GridnewAdapter(activity!!, allcoins)
//                            gridAdapter.notifyDataSetChanged()
//                            coingrid?.adapter = gridAdapter
//                        }
//                        else{
//                            pgr?.visibility = View.GONE
//                            empty?.visibility = View.VISIBLE
//                        }
//                    }
//
//                }
    }

}