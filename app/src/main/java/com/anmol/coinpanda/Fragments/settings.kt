package com.anmol.coinpanda.Fragments

import android.app.Dialog
import android.content.*
import android.support.v4.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anmol.coinpanda.R
import android.net.Uri
import android.widget.*
import com.anmol.coinpanda.ReferralDetailsActivity
import com.anmol.coinpanda.SupportActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.settings.*


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
    var facebook:Button?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.settings,
                container, false)
        telegram = vi.findViewById(R.id.telegram)
        review = vi.findViewById(R.id.review)
        request = vi.findViewById(R.id.request)
        donate = vi.findViewById(R.id.donate)
        share = vi.findViewById(R.id.share)
        help = vi.findViewById(R.id.support)
        facebook = vi.findViewById(R.id.likefb)
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
            val url = "https://t.me/joinchat/GNRigk-eugnxC1dMcW1dyQ"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)

        }
        facebook?.setOnClickListener{
            val url = "https://www.facebook.com/cryptohyype/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        donate?.setOnClickListener {
            if(activity!=null){
                val dialog = Dialog(activity)
                dialog.setContentView(R.layout.donate)
                val btc : Button? = dialog.findViewById(R.id.btcc)
                val eth : Button? = dialog.findViewById(R.id.ethc)
                val ltc : Button? = dialog.findViewById(R.id.ltcc)
                val neo : Button? = dialog.findViewById(R.id.neoc)
                val nano : Button? = dialog.findViewById(R.id.nanoc)
                val btcadd : TextView? = dialog.findViewById(R.id.btcadd)
                val ethadd : TextView? = dialog.findViewById(R.id.ethadd)
                val ltcadd : TextView? = dialog.findViewById(R.id.ltcadd)
                val neoadd : TextView? = dialog.findViewById(R.id.neoadd)
                val nanoadd : TextView? = dialog.findViewById(R.id.nanoadd)
                btc?.setOnClickListener {
                    ctclip(btcadd?.text.toString().trim())
                    dialog.dismiss()
                }
                eth?.setOnClickListener {
                    ctclip(ethadd?.text.toString().trim())
                    dialog.dismiss()

                }
                ltc?.setOnClickListener {
                    ctclip(ltcadd?.text.toString().trim())
                    dialog.dismiss()

                }
                neo?.setOnClickListener {
                    ctclip(neoadd?.text.toString().trim())
                    dialog.dismiss()

                }
                nano?.setOnClickListener {
                    ctclip(nanoadd?.text.toString().trim())
                    dialog.dismiss()

                }
                dialog.show()
            }

        }
        request?.setOnClickListener {
            if(activity!=null){
                val dialog = Dialog(activity)
                dialog.setContentView(R.layout.request)
                val rc : EditText? = dialog.findViewById(R.id.rc)
                val submit : Button? = dialog.findViewById(R.id.submit)
                val pg:ProgressBar? = dialog.findViewById(R.id.pg)
                submit?.setOnClickListener{
                    if (rc!!.text.isEmpty() || rc.text == null){
                        rc.error = "Please specify a valid coin name or symbol"
                    }
                    else{
                        pg?.visibility = View.VISIBLE
                        submit.visibility = View.GONE
                        val coin = rc.text.toString().trim()
                        val db = FirebaseFirestore.getInstance()
                        val ref = db.collection("request").document()
                        val id = ref.id
                        val map = HashMap<String,Any>()
                        map["coin"] = coin
                        db.collection("request").document(id).set(map).addOnSuccessListener {
                            Toast.makeText(activity,"We had received your request",Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
                dialog.show()
            }

        }
        help?.setOnClickListener {
            if(activity!=null){
                startActivity(Intent(activity,SupportActivity::class.java))
            }

        }
//        val auth = FirebaseAuth.getInstance()
//        val db = FirebaseDatabase.getInstance().reference
//        db.child("users").child(auth.currentUser!!.uid).addValueEventListener(object:ValueEventListener{
//            override fun onCancelled(p0: DatabaseError?) {
//                System.out.println("Referralerror$p0")
//            }
//
//            override fun onDataChange(p0: DataSnapshot?) {
//                if(p0!!.exists()){
//                    System.out.println("Referralerror$p0")
//                    val refercode:String = p0.value as String
//                    sharereferral?.text = "code"
//                    sharereferral?.text = refercode
//                    System.out.println("Referralerror${p0.value}")
//                    sharereferral?.setOnClickListener{
//
//                        val shareintent = Intent()
//                        shareintent.action = Intent.ACTION_SEND
//                        shareintent.type = "text/plain"
//                        shareintent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.anmol.coinpanda" + " to download the CryptoHype app and use my code " + p0.value.toString() + " after login.")
//                        startActivity(Intent.createChooser(shareintent,"Share code via..."))
//                    }
//                }
//                else{
//                    sharereferral?.visibility = View.GONE
//                }
//            }
//
//        })
//
//        db.child("referrers").child(auth.currentUser!!.uid).child("count").addValueEventListener(object:ValueEventListener{
//            override fun onCancelled(p0: DatabaseError?) {
//
//            }
//
//            override fun onDataChange(p0: DataSnapshot?) {
//                if(p0!!.exists()){
//                    val refercount:Long = p0.value as Long
//                    System.out.println("Referralerror${p0.value}")
//                    referralcount?.text = "abc"
//                    referralcount?.text = refercount.toString()
//                }
//                else{
//                    referralcount?.text = "0"
//                }
//            }
//
//        })

        airdrop.setOnClickListener {
            startActivity(Intent(activity!!,ReferralDetailsActivity::class.java))
        }
        return vi
    }

    private fun ctclip(address: String) {
        if(activity!=null){
            val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", address)
            clipboard.primaryClip = clip
            Toast.makeText(activity,"Address copied to clipboard", Toast.LENGTH_LONG).show()
        }

    }
}