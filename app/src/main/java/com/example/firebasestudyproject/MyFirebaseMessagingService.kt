package com.example.firebasestudyproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.firebasestudyproject.ui.dashboard.ui.dashboard.DashboardFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("MyFirebaseMessagingService", "onMessageReceived: ${message.from}")

        message.data.isNotEmpty().let {
            Log.d(
                "MyFirebaseMessagingService",
                "onMessageReceived: Message data payload ${message.data}"
            )
        }
        message.notification?.let {
            Log.d("MyFirebaseMessagingService", "onMessageReceived: NOTIFICATION BODY ${it.body}")
            sendNotification(it.body)
        }

    }

    private fun sendNotification(messageBody: String?) {
        val intent = Intent(this, DashboardFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("NOTIFICATION_DATA", messageBody)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_add_photo)
            .setContentText(messageBody)
            .setContentTitle(getString(R.string.fcm_message))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel Human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(channelId, 1, notificationBuilder.build())
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegisterToToken(token)
    }

    private fun sendRegisterToToken(token: String) {

    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()

    }
}