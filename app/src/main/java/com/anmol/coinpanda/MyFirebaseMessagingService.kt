package com.anmol.coinpanda

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.widget.RemoteViews
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.os.Build
import io.fabric.sdk.android.services.settings.IconRequest.build
import android.support.v4.app.NotificationManagerCompat
import android.transition.VisibilityPropagation
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.anmol.coinpanda.Fragments.ico


/**
 * Created by anmol on 2017-08-12.
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val CHANNEL_ID = "cryptohype_notification_id"
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        createNotificationChannel()
        if (remoteMessage!!.data.isNotEmpty()) {
            val payload = remoteMessage.data
            showNotification(payload)
        }
    }

    private fun showNotification(payload: Map<String, String>) {
//        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val builder = NotificationCompat.Builder(this)
//        builder.setSmallIcon(R.drawable.alpha)
//        builder.setBadgeIconType(R.drawable.alpha)
//        builder.color = 255
//        builder.setWhen(System.currentTimeMillis())
////        builder.setContentTitle(payload["title"])
////        builder.setContentText(payload["body"])
//        builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
//        builder.setSound(alarmSound)
//        // Get the layouts to use in the custom notification
//        val notificationLayout = RemoteViews(packageName, R.layout.notification_collapsed)
//        notificationLayout.setTextViewText(R.id.notification_title,payload["title"])
//        notificationLayout.setTextViewText(R.id.notification_info,payload["body"])
//        notificationLayout.setImageViewResource(R.id.coinnotifyicon,R.drawable.alpha)
//        builder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
//        builder.setCustomContentView(notificationLayout)

        val resultIntent = Intent(this, Home2Activity::class.java)
        val stackbuilder = TaskStackBuilder.create(this)
        stackbuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackbuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//        builder.setContentIntent(resultPendingIntent)
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(0, builder.build())

        val icon = BitmapFactory.decodeResource(resources, R.drawable.alpha)
        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.alpha)
                .setLargeIcon(icon)
                .setBadgeIconType(R.drawable.alpha)
                .setWhen(System.currentTimeMillis())
                .setColor(255)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(payload["title"])
                .setContentText(payload["body"])
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
        val newnotificationManager = NotificationManagerCompat.from(this)
        // notificationId is a unique int for each notification that you must define
        newnotificationManager.notify(123, mBuilder.build())


    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "cryptohype_notification_channel"
            val description = "notifications to keep you updated about latest Cryptocurrency news"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }
}
