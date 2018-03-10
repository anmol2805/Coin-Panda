package com.anmol.coinpanda.Fragments

import android.support.v4.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.anmol.coinpanda.R

/**
 * Created by anmol on 2/26/2018.
 */
class settings : Fragment() {
    var telegram : Button? = null
    var review : Button? = null
    var request : Button? = null
    var donate : Button? = null
    var share : Button? = null
    var help : Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.settings,
                container, false)
//        telegram = vi.findViewById(R.id.telegram)
//        review = vi.findViewById(R.id.review)
//        request = vi.findViewById(R.id.request)
//        donate = vi.findViewById(R.id.donate)
//        share = vi.findViewById(R.id.share)
//        help = vi.findViewById(R.id.support)

        return vi
    }
}