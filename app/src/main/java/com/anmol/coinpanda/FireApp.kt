package com.anmol.coinpanda

import android.app.Application
import android.util.Log

import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import java.util.HashMap

/**
 * Created by anmol on 2017-07-19.
 */

class FireApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }


    }


}
