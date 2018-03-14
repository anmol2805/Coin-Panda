package com.anmol.coinpanda

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by anmol on 2017-08-12.
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage!!.data.isNotEmpty()) {
            val payload = remoteMessage.data
            showNotification(payload)
        }
    }

    private fun showNotification(payload: Map<String, String>) {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this)
        builder.setSmallIcon(R.drawable.coinintern)
        builder.setContentTitle(payload["title"])
        builder.setContentText(payload["body"])
        builder.setSound(alarmSound)
        val resultIntent = Intent(this, HomeActivity::class.java)
        val stackbuilder = TaskStackBuilder.create(this)
        stackbuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackbuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(resultPendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }
}
