package com.example.notes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.notes.R
import com.google.firebase.messaging.FirebaseMessaging

class Notifications_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        findViewById<ImageView>(R.id.goBackImageViewNotificationsFragment).setOnClickListener {
            finish()


        }
    }
}