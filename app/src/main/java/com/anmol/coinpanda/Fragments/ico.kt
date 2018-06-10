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
import com.anmol.coinpanda.Adapters.IcoAdapter
import com.anmol.coinpanda.IconewsActivity
import com.anmol.coinpanda.Interfaces.ItemClickListener
import com.anmol.coinpanda.Model.Iconew
import com.anmol.coinpanda.R

/**
 * Created by anmol on 3/11/2018.
 */
class ico : Fragment(){
    private var cointweetrecycler: RecyclerView?=null
    lateinit var iconews : MutableList<Iconew>
    var icoAdapter : IcoAdapter?=null
    lateinit var itemClickListener : ItemClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.ico, container, false)
        val layoutManager = LinearLayoutManager(activity)
        cointweetrecycler = vi.findViewById(R.id.cointweetrecycler)
        cointweetrecycler?.layoutManager   = layoutManager
        cointweetrecycler?.setHasFixedSize(true)
        cointweetrecycler?.itemAnimator   = DefaultItemAnimator()
        iconews = ArrayList()
        val iconew1 = Iconew("Arcona","11-06-2018","Augmented Reality World Discovered","https://www.icohotlist.com/wp-content/uploads/2018/04/243.png")
        val iconew2 = Iconew("Cargocoin","11-06-2018","Revolutionizing Global Trade and Transport by Decentralization","https://www.icohotlist.com/wp-content/uploads/2018/06/cargocoin300x300-100x100.png")
        val iconew3 = Iconew("Etheal","11-06-2018","The 7.6 Trillion Healthcare Industry's New Operating System: a blockchain-based incentivized global health platform","https://www.icohotlist.com/wp-content/uploads/2018/03/1-ydlbuo81n6er6qg4l8-j1g-100x100.png")
        val iconew4 = Iconew("Gluon","11-06-2018","An Intelligent Connected Automotive Marketplace","https://www.icohotlist.com/wp-content/uploads/2018/06/gluon-100x100.jpg")
        val iconew5 = Iconew("Ternio","11-06-2018","The only scalable blockchain. Lexicon delivers over 1 million transactions per second","https://www.icohotlist.com/wp-content/uploads/2018/03/screen-shot-2017-12-28-at-51355-pm-100x31.png")
        val iconew6 = Iconew("Ubex","11-06-2018","Artificial Intelligence in Advertising","https://www.icohotlist.com/wp-content/uploads/2018/05/256x256-100x100.png")
        val iconew7 = Iconew("Vivalid","11-06-2018","ViValid is decentralized, a community-driven ledger of collectibles that contains the history of value and ownership","https://www.icohotlist.com/wp-content/uploads/2018/04/1000x300-100x32.png")
        val iconew8 = Iconew("Localcoinswap","11-06-2018","The world's most inclusive and expansive P2P crypto marketplace","https://www.icohotlist.com/wp-content/uploads/2018/01/green-logo-square-100x100.png")
        iconews.clear()
        iconews.add(iconew1)
        iconews.add(iconew2)
        iconews.add(iconew3)
        iconews.add(iconew4)
        iconews.add(iconew5)
        iconews.add(iconew6)
        iconews.add(iconew7)
        iconews.add(iconew8)
        itemClickListener = object : ItemClickListener {
            override fun onItemClick(pos: Int) {
                val intent = Intent(activity,IconewsActivity::class.java)
                intent.putExtra("iconame",iconews[pos].iconame)
                intent.putExtra("icotitle",iconews[pos].news)
                intent.putExtra("iconlink",iconews[pos].link)
                startActivity(intent)

            }

        }
        System.out.println("iconews$iconews")
        icoAdapter = IcoAdapter(activity!!,iconews,itemClickListener)
        icoAdapter!!.notifyDataSetChanged()
        cointweetrecycler?.adapter = icoAdapter
        return vi
    }
}