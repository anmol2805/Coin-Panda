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




class HomeActivity : AppCompatActivity() {

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
            val db = FirebaseFirestore.getInstance()
            db.collection("appversion").document("versioninfo").get().addOnCompleteListener {task ->
                val snapshot = task.result
                val updateversion = snapshot.getString("version")
                val pInfo = this.packageManager.getPackageInfo(packageName, 0)
                val version = pInfo.versionName
                if (version != updateversion){
                    val dialog = AlertDialog.Builder(this)
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
                    dialog.show()
                }
                else{
                    System.out.println("update not needed")
                }


            }
            setFragment(dashboard())
            navigation.selectedItemId = R.id.navigation_dashboard
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }

    }

    override fun onPostResume() {
        super.onPostResume()
        val db = FirebaseFirestore.getInstance()
        db.collection("appversion").document("versioninfo").get().addOnCompleteListener {task ->
            val snapshot = task.result
            val updateversion = snapshot.getString("version")
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            if (version != updateversion){
                val dialog = AlertDialog.Builder(this)
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
                dialog.show()
            }
            else{
                System.out.println("update not needed")
            }


        }
    }

}
