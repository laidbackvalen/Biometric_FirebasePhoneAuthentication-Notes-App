package com.valenpatel.notes.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import com.valenpatel.notes.R
import com.valenpatel.notes.views.crud.Main_Retrieve_All_Notes_Activity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    lateinit var notification: Notification
    val CHANNEL_ID: String = "My Channel" //there can be many channels, you can specify a specific channel id to work with
    val REQUEST_CODE: Int = 100 //for pending intent
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
        //https://www.youtube.com/watch?v=Pen_en0zhIY     //to know more

        var notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // NotificationManager is a class in the Android SDK that provides functionality to manage notifications shown to the user.
        // It allows you to create, update, and cancel notifications that appear in the notification drawer or as pop-up alerts.

        var iNotify = Intent(applicationContext, Main_Retrieve_All_Notes_Activity::class.java)
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //This flag is used to start an activity in a new task. If the activity is already running in another task, it will be moved to the front.

        var pendingIntent: PendingIntent = PendingIntent.getActivities(
            this, REQUEST_CODE,
            arrayOf(iNotify), PendingIntent.FLAG_IMMUTABLE
        ) //For example, if you want to create a notification that opens an activity when clicked, you would create a PendingIntent that opens the activity and attach it to the notification.
        //A PendingIntent in Android is a token that you can give to another application (like NotificationManager, AlarmManager, etc.)
        //which allows this application to execute a predefined piece of code on your behalf at a later time, even if your application is not running.

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            var name: CharSequence = "Custom Channel"
            var description: String = "Channel for push notification"
            var importance = NotificationManager.IMPORTANCE_DEFAULT

            var channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)

                notification = Notification.Builder(this)
                    //The Notification.Builder has been added to make it easier to construct Notifications.
                    //the NotificationBuilder is a class used to construct and customize notifications that are displayed to users.
                    // The NotificationBuilder is typically used in conjunction with the NotificationManager to create and manage notifications within an Android application.
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentIntent(pendingIntent)  //Pending Intent
                    .setContentTitle("Tired of forgetting things? note it down here")
                    .setSubText("Message from Valen")
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    // A "channel ID" in notifications typically refers to a unique identifier associated with a communication channel or endpoint through which notifications are sent or received.
                    //it's important to note that starting from Android 8.0 (API level 26), notification channels were introduced.
                    //Notification channels allow developers to categorize notifications and give users more control over which types of notifications they want to receive and how they are presented.
                    //When creating notifications using the NotificationBuilder, you need to specify a notification channel for the notification to be associated with.
                    // This ensures that the notification is properly categorized and follows the user's notification preferences.
                    .build()
            }else{
                notification = Notification.Builder(this)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentIntent(pendingIntent)  //Pending Intent
                    .setContentTitle(title)
                    .setSubText(msg)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .build()
            }
            if (notificationManager!=null){
              notificationManager.notify(1, notification)
            }
        }
    }


}
