package com.nipa.healthcaremobile.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
// import com.nipa.healthcareapp.R // If needed for NotificationHelper

class NotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("NotificationService", "From: \${remoteMessage.from}")

        remoteMessage.data.isNotEmpty().let {
            Log.d("NotificationService", "Message data payload: " + remoteMessage.data)
            // TODO: Process data payload
        }

        remoteMessage.notification?.let {
            Log.d("NotificationService", "Message Notification Body: \${it.body}")
            // TODO: Use NotificationHelper to display notification
            // val notificationHelper = NotificationHelper(applicationContext)
            // notificationHelper.showNotification("New Message", it.body ?: "You have a new message.")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("NotificationService", "Refreshed token: \$token")
        // TODO: Send this token to your application server.
    }
}
