package com.anmol.coinpanda.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anmol.coinpanda.R

/**
 * Created by anmol on 3/11/2018.
 */
class ico : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.ico, container, false)
        return vi
    }
}