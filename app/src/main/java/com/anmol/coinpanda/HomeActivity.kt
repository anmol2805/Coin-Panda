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


class HomeActivity : AppCompatActivity(),ForceUpdateChecker.OnUpdateNeededListener {
    override fun onUpdateNeeded(updateUrl: String) {
        val dialog = AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue reposting.")
                .setPositiveButton("Update",
                        DialogInterface.OnClickListener { dialog, which -> redirectStore(updateUrl) }).setNegativeButton("No, thanks",
                        DialogInterface.OnClickListener { dialog, which -> finish() }).create()
        dialog.show()
    }

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
            setFragment(dashboard())
            navigation.selectedItemId = R.id.navigation_dashboard
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }

    }

    private fun redirectStore(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
