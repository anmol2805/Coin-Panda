package com.anmol.coinpanda

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.anmol.coinpanda.Adapters.IcoAdapter
import com.anmol.coinpanda.Adapters.IcomsgAdapter
import com.anmol.coinpanda.Helper.Dbicopinhelper
import com.anmol.coinpanda.Helper.ICOPIN_TABLE_NAME
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Icocoin
import com.anmol.coinpanda.Model.Icopin
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity() {
    private var cointweetrecycler: RecyclerView?=null
    lateinit var icopins : MutableList<Icopin>
    var icomsgAdapter : IcomsgAdapter?=null
    lateinit var itemClickListener : ItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        val title = intent.getStringExtra("iconame")

        setTitle(title)

        val layoutManager = LinearLayoutManager(this)
        cointweetrecycler = findViewById(R.id.iconewsrecycler)
        cointweetrecycler?.layoutManager   = layoutManager
        cointweetrecycler?.setHasFixedSize(true)
        cointweetrecycler?.itemAnimator   = DefaultItemAnimator()
        icopins = ArrayList()
        val db = Dbicopinhelper(this)
        val query ="Select * from $ICOPIN_TABLE_NAME"
        val data = db.readData(query)
        icopins = data
        if(!icopins.isEmpty()){
            icomsgAdapter = IcomsgAdapter(this,icopins,itemClickListener)
            icomsgAdapter!!.notifyDataSetChanged()
            cointweetrecycler?.adapter = icomsgAdapter
        }
        else{
            var c = 0
            val jsonArray = JsonArrayRequest(Request.Method.GET,"http://198.199.90.139/ico/pinned",null, Response.Listener { response ->
                while (c<response.length()){
                    val jsonObject = response.getJSONObject(c)
                    val messageid = jsonObject.getString("cID")
                    val message = jsonObject.getString("pinned_messages")
                    val messagetime = jsonObject.getString("date")
                    val ico_name = jsonObject.getString("coin_name")
                    val icopin = Icopin(messageid,ico_name,message,messagetime)
                    if(ico_name == title){
                        icopins.add(icopin)
                    }
                    db.insertData(icopin)
                    c++
                }
                icomsgAdapter = IcomsgAdapter(this,icopins,itemClickListener)
                icomsgAdapter!!.notifyDataSetChanged()
                cointweetrecycler?.adapter = icomsgAdapter

            }, Response.ErrorListener {

            })
            Mysingleton.getInstance(this).addToRequestqueue(jsonArray)
        }

        btntelegram.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


    }
}
