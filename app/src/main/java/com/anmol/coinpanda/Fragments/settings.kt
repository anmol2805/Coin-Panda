package com.anmol.coinpanda.Fragments

import android.content.Intent
import android.support.v4.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.anmol.coinpanda.R
import android.content.ActivityNotFoundException
import android.net.Uri


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
        telegram = vi.findViewById(R.id.telegram)
        review = vi.findViewById(R.id.review)
        request = vi.findViewById(R.id.request)
        donate = vi.findViewById(R.id.donate)
        share = vi.findViewById(R.id.share)
        help = vi.findViewById(R.id.support)
        share?.setOnClickListener {
            val shareintent = Intent()
            shareintent.action = Intent.ACTION_SEND
            shareintent.type = "text/plain"
            shareintent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.anmol.coinpanda")
            startActivity(Intent.createChooser(shareintent,"Share app via..."))

        }
        review?.setOnClickListener{
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
        telegram?.setOnClickListener{

        }
        request?.setOnClickListener {

        }
        help?.setOnClickListener {

        }

        return vi
    }
}