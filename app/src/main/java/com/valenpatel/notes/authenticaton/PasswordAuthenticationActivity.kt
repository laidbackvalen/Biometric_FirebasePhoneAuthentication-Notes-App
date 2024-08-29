package com.valenpatel.notes.authenticaton

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.valenpatel.notes.R
import com.valenpatel.notes.views.crud.Main_Retrieve_All_Notes_Activity
import com.valenpatel.paisefy.preferences.SharedPreference

class PasswordAuthenticationActivity : AppCompatActivity() {
    // Initialize shared preferences with application context
    private lateinit var sharedPreference: SharedPreference
    private lateinit var PASSWORD: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_authentication)

        // Initialize shared preference
        sharedPreference = SharedPreference(context = applicationContext)
        PASSWORD = sharedPreference.getPassword().toString()

        // Show password dialog
        findViewById<Button>(R.id.submitButton).setOnClickListener{
            showPasswordDialog()
        }
        findViewById<ImageView>(R.id.imageView5).setOnClickListener{
            startActivity(Intent(this, Biometric_Authentication_Activity::class.java))
            finish()
        }
    }

    private fun showPasswordDialog() {
        val enterPassword = findViewById<TextInputEditText>(R.id.passwordToVerify).text.toString()
        if (enterPassword == PASSWORD) {
            val intent = if (FirebaseAuth.getInstance().currentUser != null) {
                Intent(this, Main_Retrieve_All_Notes_Activity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            } else {
                Intent(this, Login_Activity::class.java)
            }
            startActivity(intent)
            notifyUser("Authentication Succeeded")
            finish() // Ensure this activity finishes before starting the new one
        } else {
            Toast.makeText(this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
