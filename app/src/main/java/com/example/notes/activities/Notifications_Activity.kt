package com.example.notes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.notes.R

class Notifications_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        findViewById<ImageView>(R.id.goBackImageViewNotificationsFragment).setOnClickListener {
            finish()
        }
    }
}