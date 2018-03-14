package com.anmol.coinpanda

import android.app.Application
import android.util.Log

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

import java.util.HashMap

/**
 * Created by anmol on 3/14/2018.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // set in-app defaults
        val remoteConfigDefaults = HashMap<String,Any>()
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false)
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0.0")
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
                "https://play.google.com/store/apps/details?id=com.anmol.coinpanda")

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults)
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "remote config is fetched.")
                        firebaseRemoteConfig.activateFetched()
                    }
                }
    }

    companion object {

        private val TAG = App::class.java.simpleName
    }
}
