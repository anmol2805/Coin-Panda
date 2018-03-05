package com.anmol.coinpanda.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anmol.coinpanda.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by anmol on 2/26/2018.
 */
class dashboard : Fragment() {
    private var cointweetrecycler:RecyclerView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.dashboard, container, false)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH,-1)
        System.out.println("previousdate:" + format.format(cal.time))
        return vi
    }
}