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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * Created by anmol on 2/26/2018.
 */
class home : Fragment() {
    private var coingrid:GridView?=null
    private lateinit var gridAdapter:GridnewAdapter
    private lateinit var mcoinselect: Switch
    lateinit var allcoins:MutableList<Allcoin>
    lateinit var itemClickListener : ItemClickListener
    var db = FirebaseFirestore.getInstance()
    var sedit:EditText? = null
    var srch:Button? = null
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.home,
                container, false)
        coingrid = vi.findViewById(R.id.coingrid)
        mcoinselect = vi.findViewById(R.id.coinselect)
        sedit = vi.findViewById(R.id.sc)
        srch = vi.findViewById(R.id.scb)
        allcoins = ArrayList()
        mcoinselect.isChecked = true
        val handler = Handler()
        handler.postDelayed({
            loaddata()
        },200)

        mcoinselect.setOnCheckedChangeListener({ compoundButton, b ->
            if (b){
                loaddata()
                mcoinselect.isChecked = true
            }
            else{
                loadalldata(null)
                mcoinselect.isChecked = false
            }
        })
        coingrid?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val dialog = Dialog(activity)
            dialog.setContentView(R.layout.dialoglayout)
            val prg : ProgressBar? = dialog.findViewById(R.id.prgbr)
            val coinimg:ImageView? = dialog.findViewById(R.id.coinimg)
            val cn :TextView? = dialog.findViewById(R.id.cn)
            val cs:TextView? = dialog.findViewById(R.id.cs)
            val cp:TextView? = dialog.findViewById(R.id.cp)
            val atp:Button? = dialog.findViewById(R.id.atp)
            val portfoliolay:LinearLayout?= dialog.findViewById(R.id.portfoliolay)
            val viewtweet:Button?=dialog.findViewById(R.id.viewtweets)
            val notificationswitch:Switch?= dialog.findViewById(R.id.notification)
            val remove:Button? = dialog.findViewById(R.id.remove)
            atp?.visibility = View.VISIBLE
            portfoliolay?.visibility = View.GONE
            db.collection("users").document("MhqeP5vqgdadnSodwzPo").collection("portfolio").get().addOnCompleteListener {task ->
                for(doc in task.result){
                    if(doc.id.contains(allcoins[i].coinname!!)){
                        atp?.visibility = View.GONE
                        portfoliolay?.visibility = View.VISIBLE
                    }
                }
            }
            cn?.text = allcoins[i].coin
            cs?.text = allcoins[i].coinname
            cp?.text = "#"+allcoins[i].coinpage
            val urlpng = "https://raw.githubusercontent.com/crypti/cryptocurrencies/master/images/"+allcoins[i].coinname+".png"
            val urljpg = "https://raw.githubusercontent.com/crypti/cryptocurrencies/master/images/"+allcoins[i].coinname+".jpg"
            val urljpeg = "https://raw.githubusercontent.com/crypti/cryptocurrencies/master/images/"+allcoins[i].coinname+".jpeg"
            Glide.with(activity).load(urlpng).listener(object : RequestListener<Drawable> {
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    return false
                }

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    Glide.with(activity).load(urljpg).listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            Glide.with(activity).load(urljpeg).into(coinimg)
                            return true
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                    }).into(coinimg)
                    return true
                }

            }).into(coinimg)

            viewtweet?.setOnClickListener {
                val intent = Intent(activity,TweetsActivity::class.java)
                intent.putExtra("coin",allcoins[i].coinname)
                startActivity(intent)
            }
            remove?.setOnClickListener {
                db.collection("users").document("MhqeP5vqgdadnSodwzPo").collection("portfolio").document(allcoins[i].coinname!!)
                        .delete().addOnSuccessListener {
                            Toast.makeText(activity,"Removed from your Portfolio", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
            }
            atp?.setOnClickListener {
//                val intent = Intent(activity,PaymentActivity::class.java)
//                intent.putExtra("coin",allcoins[i].coin)
//                startActivity(intent)
                prg?.visibility = View.VISIBLE
                atp.visibility = View.GONE
                val map = HashMap<String,Any>()
                map["coin_name"] = allcoins[i].coin.toString()
                map["coinPage"] = allcoins[i].coinpage.toString()
                db.collection("users").document("MhqeP5vqgdadnSodwzPo").collection("portfolio").document(allcoins[i].coinname!!).set(map).addOnSuccessListener {
                    Toast.makeText(activity,"Added to your Portfolio", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
        sedit?.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                System.out.println("textchange:$p0")
                loadalldata(p0)
            }

        })
        // Inflate the layout for this fragment
        return vi
    }

    private fun loadalldata(p0: CharSequence?) {
        sedit?.visibility = View.VISIBLE
        srch?.visibility = View.VISIBLE
        allcoins.clear()
        db.collection("AllCoins").orderBy("lastUpdate",Query.Direction.DESCENDING).addSnapshotListener{ documentSnapshot, firebaseFirestoreException ->
            allcoins.clear()
            if(p0 == null){
                for(doc in documentSnapshot.documents){
                    val coinname = doc.getString("coin_symbol")
                    val name = doc.getString("coin_name")
                    val coinpage = doc.getString("coinPage")
                    val allcoin = Allcoin(coinname,name,coinpage)
                    allcoins.add(allcoin)
                }
                if(activity!=null){
                    if(!allcoins.isEmpty()){
                        gridAdapter = GridnewAdapter(activity!!,allcoins)
                        coingrid?.adapter = gridAdapter
                    }
                }
            }
            else{
                for(doc in documentSnapshot.documents){
                    val coinname = doc.getString("coin_symbol")
                    val name = doc.getString("coin_name")
                    val coinpage = doc.getString("coinPage")
                    if (name.toLowerCase().contains(p0) || coinname.toLowerCase().contains(p0) || name.toUpperCase().contains(p0) || coinname.toUpperCase().contains(p0)){
                        val allcoin = Allcoin(coinname,name,coinpage)
                        allcoins.add(allcoin)
                    }

                }
                if(activity!=null){
                    if(!allcoins.isEmpty()){
                        gridAdapter = GridnewAdapter(activity!!,allcoins)
                        coingrid?.adapter = gridAdapter
                    }
                }
            }


        }
    }

    private fun loaddata() {
        sedit?.visibility =View.GONE
        srch?.visibility  =View.GONE
        allcoins.clear()
        db.collection("users").document("MhqeP5vqgdadnSodwzPo").collection("portfolio").addSnapshotListener{documentSnapshot, e ->
            allcoins.clear()
            for(doc in documentSnapshot.documents){
                val coinname = doc.id
                val name = doc.getString("coin_name")
                val coinpage = doc.getString("coinPage")
                val allcoin = Allcoin(coinname,name,coinpage)
                allcoins.add(allcoin)
            }
            if(activity!=null){
                if(!allcoins.isEmpty()){
                    gridAdapter = GridnewAdapter(activity!!,allcoins)
                    coingrid?.adapter = gridAdapter
                }
            }


        }
    }


}