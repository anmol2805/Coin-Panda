package com.anmol.coinpanda.Fragments


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.anmol.coinpanda.Helper.COL_ID
import com.anmol.coinpanda.Helper.Dbcoinshelper
import com.anmol.coinpanda.Helper.Dbhelper
import com.anmol.coinpanda.Helper.TABLE_NAME
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Tweet
import com.anmol.coinpanda.Mysingleton
import com.anmol.coinpanda.R
import com.anmol.coinpanda.ReferralDetailsActivity
import com.anmol.coinpanda.Services.CoinsdbService
import com.anmol.coinpanda.Services.TweetsdbService
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

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
    var databaseReference:DatabaseReference?=null
    var noticelayout:LinearLayout?=null
    var noticetext:TextView?=null
    var noticebutton:ImageButton?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.dashboard, container, false)
        tweetselect = vi.findViewById(R.id.cointweetselect)
        pgr = vi.findViewById(R.id.pgr)
        noticelayout = vi.findViewById(R.id.noticelayout)
        noticebutton = vi.findViewById(R.id.noticebutton)
        noticetext = vi.findViewById(R.id.noticetext)
        pgr?.visibility = View.VISIBLE
        //updaterequest()
        //moverequest()
//        pgr?.visibility = View.GONE
//        tweetselect.isChecked = false
//        setFragment(allcoins())

//            val dtb = Dbhelper(activity!!)
//            val dataquery = "Select * from $TABLE_NAME ORDER BY $COL_ID DESC"
//            val data = dtb.readData(dataquery)
//            if(data.isEmpty()){
//                val intent = Intent(activity,TweetsdbService::class.java)
//                activity!!.startService(intent)
//            }
        pgr?.visibility = View.GONE
        tweetselect.isChecked = false
        setFragment(allcoins())


            tweetselect.setOnCheckedChangeListener { _, b ->
                if(b){
                    setFragment(mycoins())
                }
                else{
                    setFragment(allcoins())
                }
            }
        databaseReference = FirebaseDatabase.getInstance().reference.child("banner")
        databaseReference!!.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    if(p0.child("text").value!!.equals("") || p0.child("text").value.toString().isEmpty()){
                        noticelayout!!.visibility = View.GONE
                    }
                    else{
                        noticelayout!!.visibility = View.VISIBLE
                        val text = p0.child("text").value.toString()
                        noticetext!!.text = text
                    }

                    val code = p0.child("buttoncode").value
                    when(code){
                        "hide" -> {
                            System.out.println("firebaseresponse0")
                            noticebutton!!.visibility = View.GONE
                        }
                        "link" ->{
                            System.out.println("firebaseresponse1")
                            noticebutton!!.visibility = View.VISIBLE
                            noticebutton!!.setOnClickListener {
                                val url = p0.child("buttonlink").value.toString()
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(url)
                                try{
                                    startActivity(intent)
                                }
                                catch (e: ActivityNotFoundException){
                                    e.printStackTrace()
                                }
                            }
                        }
                        "update" ->{
                            System.out.println("firebaseresponse2")
                            noticebutton!!.visibility = View.VISIBLE
                            noticebutton!!.setOnClickListener {
                                val uri = Uri.parse("market://details?id=" + "com.anmol.coinpanda")
                                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                                // To count with Play market backstack, After pressing back button,
                                // to taken back to our application, we need to add following flags to intent.
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                                try {
                                    startActivity(goToMarket)
                                } catch (e: ActivityNotFoundException) {
                                    startActivity(Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://play.google.com/store/apps/details?id=" + "com.anmol.coinpanda")))
                                }
                            }
                        }
                        "activity" ->{
                            System.out.println("firebaseresponse3")
                            noticebutton!!.visibility = View.VISIBLE
                            noticebutton!!.setOnClickListener {
                                startActivity(Intent(activity!!, ReferralDetailsActivity::class.java))
                            }
                        }
                        else ->{
                            System.out.println("firebaseresponseelse")
                            noticebutton!!.visibility = View.GONE
                        }
                    }

                    val buttonimagelink = p0.child("buttonimagelink").value
                    try{
                        Glide.with(activity!!).load(buttonimagelink).into(noticebutton)
                    }catch (e:KotlinNullPointerException){
                        e.printStackTrace()
                    }



                }
            }

        })
        
        return vi
    }

    private fun setFragment(fragment: Fragment) {
        if(activity!=null && !activity!!.isFinishing){
            activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.coinframe,fragment).commitAllowingStateLoss()
        }



    }
    private fun updaterequest() {
        val stringRequest = StringRequest(Request.Method.GET,"https://www.cryptohype.live/update", Response.Listener { response ->
            System.out.println(response)
        }, Response.ErrorListener { error->
            System.out.println(error)
        })
        if(activity!=null){
            Mysingleton.getInstance(activity).addToRequestqueue(stringRequest)
        }

    }
    private fun moverequest() {
        val stringRequest = StringRequest(Request.Method.GET,"https://www.cryptohype.live/move", Response.Listener { response ->
            System.out.println(response)
        }, Response.ErrorListener { error->
            System.out.println(error)
        })
        if(activity!=null){
            Mysingleton.getInstance(activity).addToRequestqueue(stringRequest)
        }

    }

}