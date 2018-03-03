package com.anmol.coinpanda.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Allcoin
import com.anmol.coinpanda.Model.Coin
import com.anmol.coinpanda.PaymentActivity
import com.anmol.coinpanda.R
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by anmol on 2/27/2018.
 */
class AddCoinAdapter(internal var c: Context, internal var allcoins: List<Allcoin>, private val mitemClickListener: ItemClickListener): RecyclerView.Adapter<AddCoinAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(c).inflate(R.layout.addcoinrow,parent,false)
        return MyViewHolder(v,mitemClickListener)
    }

    override fun getItemCount(): Int {
        return allcoins.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coindata = allcoins[position]
        holder.mcoinname?.text = coindata.coinname
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document("Z2ycXxL6GyvPS23NTuYk").collection("portfolio").addSnapshotListener{documentSnapshot, e ->
            for(doc in documentSnapshot.documents){
                if(doc.id.contains(coindata.coinname!!)){
                    holder.addtoport?.visibility = View.GONE
                    holder.btnstatus?.visibility = View.VISIBLE
                }
            }
        }
        holder.addtoport?.setOnClickListener({
            val intent = Intent(c,PaymentActivity::class.java)
            intent.putExtra("coin",coindata.coinname)
            c.startActivity(intent)
        })


    }

    inner class MyViewHolder(itemView: View, private val mitemClickListener: ItemClickListener):RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mcoinname:TextView? = null
        var addtoport:Button?=null
        var btnstatus:TextView?=null
        init {
            this.mcoinname = itemView.findViewById(R.id.namecoin)
            this.addtoport = itemView.findViewById(R.id.addtoport)
            this.btnstatus = itemView.findViewById(R.id.btnstatus)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            mitemClickListener.onItemClick(this.adapterPosition)
        }


    }

}