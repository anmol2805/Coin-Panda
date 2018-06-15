package com.anmol.coinpanda

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.anmol.coinpanda.Fragments.*
import com.anmol.coinpanda.Helper.Dbbookshelper
import com.anmol.coinpanda.Services.BookmarksdbService
import com.anmol.coinpanda.Services.TweetsdbService
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId

class Home2Activity : AppCompatActivity() {
    private var back_pressed: Long = 0
    var coinslayout:LinearLayout?=null
    var bookmarkslayout:LinearLayout?=null
    var tweetslayout:LinearLayout?=null
    var icolayout:LinearLayout?=null
    var settingslayout:LinearLayout?=null
    var porticon:ImageView?=null
    var bookmarksicon:ImageView?=null
    var tweeticon:ImageView?=null
    var icoicon:ImageView?=null
    var settingsicon:ImageView?=null
    var currentfragment:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_home2)
        System.out.println("token" + FirebaseInstanceId.getInstance().token)
        coinslayout = findViewById(R.id.coinslayout)
        bookmarkslayout = findViewById(R.id.bookmarklayout)
        tweetslayout = findViewById(R.id.tweetlayout)
        icolayout = findViewById(R.id.icolayout)
        settingslayout = findViewById(R.id.settingslayout)
        porticon = findViewById(R.id.portfolioicon)
        bookmarksicon =  findViewById(R.id.bookmarksicon)
        tweeticon = findViewById(R.id.tweetsicon)
        icoicon = findViewById(R.id.icoicon)
        settingsicon = findViewById(R.id.settingsicon)
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
        }
        else{

            val intent = Intent(this, TweetsdbService::class.java)
            startService(intent)
            val dbb = Dbbookshelper(this)
            val bookmarkdata = dbb.readbook()
            if(bookmarkdata.isEmpty()){
                val intent1 = Intent(this, BookmarksdbService::class.java)
                startService(intent1)
            }
            val databaseReference = FirebaseDatabase.getInstance().reference
            databaseReference.root.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if(p0!!.child("vname").value!=null){
                        val updateversion = p0.child("vname").value
                        val pInfo = packageManager.getPackageInfo(packageName, 0)
                        val version = pInfo.versionName.toString().trim()
                        System.out.println("version $updateversion $version")
                        val dialog = AlertDialog.Builder(this@Home2Activity)
                                .setTitle("New version available")
                                .setMessage("Please, update app to new version to continue our services")
                                .setCancelable(false)
                                .setPositiveButton("Update"
                                ) { dialog, which -> val uri = Uri.parse("market://details?id=" + "com.anmol.coinpanda")
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
                                    } }.create()
                        if (version == updateversion){
                            System.out.println("update not needed")
                            dialog.cancel()
                            dialog.dismiss()
                        }
                        else{

                            dialog.show()
                        }

                    }
                    if(p0.child("maintenance").value!=null){
                        val status:Boolean = p0.child("maintenance").value as Boolean
                        val dialog = AlertDialog.Builder(this@Home2Activity)
                                .setTitle("Maintenance downtime")
                                .setMessage("Server is undergoing some maintenance procedures.Stay tuned, we'll be back after some time")
                                .setCancelable(false)
                                .create()
                        if (!status){
                            dialog.cancel()
                            dialog.dismiss()
                            System.out.println("update not needed")
                        }
                        else{

                            dialog.show()
                        }

                    }
                }

            })
            supportFragmentManager.beginTransaction().replace(R.id.appframe,dashboard()).commitAllowingStateLoss()
            supportFragmentManager.executePendingTransactions()
            currentfragment = 3
            Glide.with(this).load(R.drawable.newsfilled).into(tweeticon)
            Glide.with(this).load(R.drawable.mycoinsunfilled).into(porticon)
            Glide.with(this).load(R.drawable.bookmarksunfilled).into(bookmarksicon)
            //Glide.with(this).load(R.drawable.icounfilled).into(icoicon)
            Glide.with(this).load(R.drawable.settingsunfiled).into(settingsicon)
        }
        coinslayout?.setOnClickListener{
            setFragment(home(),1)
            //currentfragment = 1
            Glide.with(this).load(R.drawable.newsunfilled).into(tweeticon)
            Glide.with(this).load(R.drawable.mycoinsfilled).into(porticon)
            Glide.with(this).load(R.drawable.bookmarksunfilled).into(bookmarksicon)
            Glide.with(this).load(R.drawable.icounfilled).into(icoicon)
            Glide.with(this).load(R.drawable.settingsunfiled).into(settingsicon)
        }
        bookmarkslayout?.setOnClickListener{
            setFragment(bookmarks(),2)
            //currentfragment = 2
            Glide.with(this).load(R.drawable.newsunfilled).into(tweeticon)
            Glide.with(this).load(R.drawable.mycoinsunfilled).into(porticon)
            Glide.with(this).load(R.drawable.bookmarksfilled).into(bookmarksicon)
            Glide.with(this).load(R.drawable.icounfilled).into(icoicon)
            Glide.with(this).load(R.drawable.settingsunfiled).into(settingsicon)
        }
        tweetslayout?.setOnClickListener{
            setFragment(dashboard(),3)
            //currentfragment = 3
            Glide.with(this).load(R.drawable.newsfilled).into(tweeticon)
            Glide.with(this).load(R.drawable.mycoinsunfilled).into(porticon)
            Glide.with(this).load(R.drawable.bookmarksunfilled).into(bookmarksicon)
            Glide.with(this).load(R.drawable.icounfilled).into(icoicon)
            Glide.with(this).load(R.drawable.settingsunfiled).into(settingsicon)
        }
        icolayout?.setOnClickListener{
//            setFragment(ico(),4)
//            //currentfragment = 4
//            Glide.with(this).load(R.drawable.newsunfilled).into(tweeticon)
//            Glide.with(this).load(R.drawable.mycoinsunfilled).into(porticon)
//            Glide.with(this).load(R.drawable.bookmarksunfilled).into(bookmarksicon)
//            Glide.with(this).load(R.drawable.icofilled).into(icoicon)
//            Glide.with(this).load(R.drawable.settingsunfiled).into(settingsicon)
        }
        settingslayout?.setOnClickListener {
            setFragment(settings(),5)
            //currentfragment = 5
            Glide.with(this).load(R.drawable.newsunfilled).into(tweeticon)
            Glide.with(this).load(R.drawable.mycoinsunfilled).into(porticon)
            Glide.with(this).load(R.drawable.bookmarksunfilled).into(bookmarksicon)
            Glide.with(this).load(R.drawable.icounfilled).into(icoicon)
            Glide.with(this).load(R.drawable.settingsfilled).into(settingsicon)
        }

    }
    private fun setFragment(fragment: Fragment, i: Int) {
        if (i<currentfragment!!){
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit).replace(R.id.appframe,fragment).commitAllowingStateLoss()
            supportFragmentManager.executePendingTransactions()
            currentfragment = i
        }
        else if(i>currentfragment!!){
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit).replace(R.id.appframe,fragment).commitAllowingStateLoss()
            supportFragmentManager.executePendingTransactions()
            currentfragment = i
        }


    }
    override fun onPostResume() {
        super.onPostResume()
        val databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.root.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.child("vname").value!=null){
                    val updateversion = p0.child("vname").value.toString().trim()
                    val pInfo = packageManager.getPackageInfo(packageName, 0)
                    val version = pInfo.versionName
                    System.out.println("version $updateversion $version")
                    val dialog = AlertDialog.Builder(this@Home2Activity)
                            .setTitle("New version available")
                            .setMessage("Please, update app to new version to continue our services")
                            .setCancelable(false)
                            .setPositiveButton("Update"
                            ) { dialog, which -> val uri = Uri.parse("market://details?id=" + "com.anmol.coinpanda")
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
                                } }.create()
                    if (version == updateversion){
                        dialog.cancel()
                        dialog.dismiss()
                        System.out.println("update not needed")
                    }
                    else{

                        dialog.show()

                    }

                }
                if(p0.child("maintenance").value!=null){
                    val status:Boolean = p0.child("maintenance").value as Boolean
                    val dialog = AlertDialog.Builder(this@Home2Activity)
                            .setTitle("Maintenance downtime")
                            .setMessage("Server is undergoing some maintenance procedures.Stay tuned, we'll be back after some time")
                            .setCancelable(false)
                            .create()
                    if (!status){
                        dialog.cancel()
                        dialog.dismiss()
                        System.out.println("update not needed")
                    }
                    else{

                        dialog.show()
                    }

                }
            }

        })
    }

    override fun onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
            overridePendingTransition(R.anim.still, R.anim.slide_out_down)
        } else {
            back_pressed = System.currentTimeMillis()
            Toast.makeText(baseContext, "Double tap to exit!", Toast.LENGTH_SHORT).show()

        }

    }
}
