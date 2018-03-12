package com.anmol.coinpanda

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.anmol.coinpanda.Fragments.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

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
            setFragment(dashboard())
            navigation.selectedItemId = R.id.navigation_dashboard
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }

    }
}
