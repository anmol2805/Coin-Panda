package com.anmol.coinpanda

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.anmol.coinpanda.Fragments.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import android.content.DialogInterface
import android.net.Uri
import android.support.v7.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import android.content.pm.PackageManager
import android.R.attr.versionName
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.widget.Toast
import com.anmol.coinpanda.Services.CoinsdbService
import com.anmol.coinpanda.Services.TweetsdbService


class HomeActivity : AppCompatActivity() {
    private var back_pressed: Long = 0

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                setFragment(home())
                //message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                setFragment(dashboard())
                //message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_ico -> {
                setFragment(ico())
                return@OnNavigationItemSelectedListener true

            }
            R.id.navigation_notifications -> {
                setFragment(settings())
                //message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_bookmarks ->{
                setFragment(bookmarks())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.content,fragment).commit()
        supportFragmentManager.executePendingTransactions()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_home)
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
        }
        else{
            val intent = Intent(this, TweetsdbService::class.java)
            startService(intent)
            val intent2 = Intent(this,CoinsdbService::class.java)
            startService(intent2)
            val databaseReference = FirebaseDatabase.getInstance().reference
            databaseReference.root.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {

                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if(p0!!.child("vname").value!=null){
                        val updateversion = p0.child("vname").value
                        val pInfo = packageManager.getPackageInfo(packageName, 0)
                        val version = pInfo.versionName.toString().trim()
                        System.out.println("version $updateversion $version")
                        val dialog = AlertDialog.Builder(this@HomeActivity)
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
                        val dialog = AlertDialog.Builder(this@HomeActivity)
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

            setFragment(dashboard())
            navigation.selectedItemId = R.id.navigation_dashboard
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
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
                    val dialog = AlertDialog.Builder(this@HomeActivity)
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
                    val dialog = AlertDialog.Builder(this@HomeActivity)
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
