package com.valenpatel.notes.views.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.valenpatel.notes.R

class Notifications_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        findViewById<ImageView>(R.id.goBackImageViewNotificationsFragment).setOnClickListener {
            finish()


        }
    }
}