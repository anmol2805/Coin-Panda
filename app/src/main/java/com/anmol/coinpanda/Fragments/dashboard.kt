package com.anmol.coinpanda.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anmol.coinpanda.R

/**
 * Created by anmol on 2/26/2018.
 */
class dashboard : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dashboard,
                container, false)
    }
}