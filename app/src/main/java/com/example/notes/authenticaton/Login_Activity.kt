package com.example.notes.authenticaton

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.R
import com.example.notes.activities.crud.All_Notes_Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit

class Login_Activity : AppCompatActivity() {
    lateinit var loadingPB: ProgressBar

    // this stores the phone number of the user
    var number: String = ""

    // create instance of firebase auth
    lateinit var auth: FirebaseAuth

    // we will use this to match the sent otp from firebase
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        loadingPB   = findViewById(R.id.progressBarLogin)
            loadingPB.visibility = View.INVISIBLE

        // start verification on click of the button
        findViewById<Button>(R.id.loginButton).setOnClickListener {
            login()

        }

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, All_Notes_Activity::class.java))
                finish()
                Log.d("GFG", "onVerificationCompleted Success")
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG", "onVerificationFailed  $e")
            }

            // On code is sent by the firebase this method is called
            // in here we start a new activity where user can enter the OTP
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("GFG", "onCodeSent: $verificationId")
                storedVerificationId = verificationId  //OTP
                resendToken = token //to resend OTP

                // Start a new activity using intent
                // also send the storedVerificationId using intent
                // we will use this id to send the otp back to firebase
                val intent = Intent(applicationContext, Code_Verification_Activity::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun login() {
        number = findViewById<EditText>(R.id.editTextPhoneNumber).text.trim().toString()
        // get the phone number from edit text and append the country cde with it
        if (number.isNotEmpty()) {
            //COUNTRY CODE PICKER
            var countrycodePicker = findViewById<CountryCodePicker>(R.id.countryCodePicker)
            countrycodePicker.registerCarrierNumberEditText(findViewById<EditText>(R.id.editTextPhoneNumber))
            number = countrycodePicker.fullNumberWithPlus
            sendVerificationCode(number)
            loadingPB.visibility = View.VISIBLE
        } else {
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    // this method sends the verification code
    // and starts the callback of verification
    // which is implemented above in onCreate
    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("GFG", "Auth started")
    }
}

