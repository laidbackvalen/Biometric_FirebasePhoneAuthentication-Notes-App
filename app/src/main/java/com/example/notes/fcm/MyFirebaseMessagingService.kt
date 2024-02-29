package com.example.notes.fcm

import android.app.Notification
import android.app.NotificationManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    override fun onMessageReceived(message: RemoteMessage) { //when message recieved // notification
        super.onMessageReceived(message)

        if (message.notification != null) {
            pushNotification(
                message.notification!!.title.toString(),
                message.notification!!.body.toString()
            ) //custom method with parameters
        }
    }

    private fun pushNotification(title: String, msg: String) {
       // NotificationManager is a class in the Android SDK that provides functionality to manage notifications shown to the user.
        // It allows you to create, update, and cancel notifications that appear in the notification drawer or as pop-up alerts.
        var notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager //to use functionality of notification we use Notification Manager

        lateinit var notification: Notification
    }


}
