package com.anmol.coinpanda.Fragments

import android.app.Dialog
import android.support.v4.app.Fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.anmol.coinpanda.Adapters.AllCoinAdapter
import com.anmol.coinpanda.Adapters.CoinAdapter
import com.anmol.coinpanda.Adapters.GridAdapter
import com.anmol.coinpanda.Adapters.GridnewAdapter
import com.anmol.coinpanda.AddToPortfolioActivity
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.Model.Coin
import com.anmol.coinpanda.R
import com.anmol.coinpanda.TweetsActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.home.*
import org.w3c.dom.Text

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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.home,
                container, false)
        coingrid = vi.findViewById(R.id.coingrid)
        mcoinselect = vi.findViewById(R.id.coinselect)
        allcoins = ArrayList()
        mcoinselect.isChecked = true
        loaddata()
        mcoinselect.setOnCheckedChangeListener({ compoundButton, b ->
            if (b){
                loaddata()
            }
            else{
                loadalldata()
            }
        })
        coingrid?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val dialog = Dialog(activity)
            dialog.setContentView(R.layout.dialoglayout)
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
            db.collection("users").document("MhqeP5vqgdadnSodwzPo").collection("portfolio").addSnapshotListener{documentSnapshot, e ->
                for(doc in documentSnapshot){
                    if(doc.id.contains(allcoins[i].coinname!!)){
                        atp?.visibility = View.GONE
                        portfoliolay?.visibility = View.VISIBLE
                    }
                }
            }
            cn?.text = allcoins[i].coin
            cs?.text = allcoins[i].coinname
            cp?.text = allcoins[i].coinpage
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

            }
            atp?.setOnClickListener {

            }
            dialog.show()
        }
        // Inflate the layout for this fragment
        return vi
    }

    private fun loadalldata() {
        allcoins.clear()
        db.collection("AllCoins").orderBy("lastUpdate",Query.Direction.DESCENDING).addSnapshotListener{ documentSnapshot, firebaseFirestoreException ->
            allcoins.clear()
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
    }

    private fun loaddata() {
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