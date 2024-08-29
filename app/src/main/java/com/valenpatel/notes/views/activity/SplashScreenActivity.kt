package com.valenpatel.notes.views.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.valenpatel.notes.R
import com.valenpatel.notes.views.crud.Main_Retrieve_All_Notes_Activity
import com.valenpatel.notes.authenticaton.Login_Activity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val handler = Handler().postDelayed({
            if(FirebaseAuth.getInstance().currentUser != null) {
                val intent = Intent(this, Main_Retrieve_All_Notes_Activity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, Login_Activity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)
    }
}