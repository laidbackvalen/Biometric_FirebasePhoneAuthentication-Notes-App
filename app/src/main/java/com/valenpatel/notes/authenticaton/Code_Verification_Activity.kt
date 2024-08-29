package com.valenpatel.notes.authenticaton

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.valenpatel.notes.R
import com.valenpatel.notes.views.crud.Main_Retrieve_All_Notes_Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class Code_Verification_Activity : AppCompatActivity() {

    // get reference of the firebase auth
    lateinit var auth: FirebaseAuth
    var timeOutSeconds = 60L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_verification)

        window.statusBarColor = getColor(R.color.black)
        //LINKING WITH XML
        //Getting value of phone number using intent coming from GenerateOTPActivity

            auth=FirebaseAuth.getInstance()
//        startResendTimer()
            // get storedVerificationId from the intent
            val storedVerificationId= intent.getStringExtra("storedVerificationId")
            val number= intent.getStringExtra("number")

        findViewById<TextView>(R.id.numberRetrieved).setText(number)

            // fill otp and call the on click on button
            findViewById<Button>(R.id.verifyAndContinueButton).setOnClickListener {
                val pinView = findViewById<PinView>(R.id.firstPinView).text.toString()
                if (pinView != null) {
                    if(pinView.isNotEmpty()){
                        val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                            storedVerificationId.toString(), pinView)
                        signInWithPhoneAuthCredential(credential)
                    }else{
                        Toast.makeText(this,"Enter OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        // verifies if the code matches sent by firebase
        // if success start the new activity in our case it is main Activity
        private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this , Main_Retrieve_All_Notes_Activity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Sign in failed, display a message and update the UI
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(this,"Invalid OTP", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }


//    //RESEND TIMER
//    private fun startResendTimer() {
//        //Declare the timer
//        val timer = Timer()
//        //Set the schedule function and rate
//        timer.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                runOnUiThread {
//                    val tv =
//                        findViewById<View>(R.id.resendingCodeTimer) as TextView
//                    tv.text = "Sending new OTP request in 00" + ":" + timeOutSeconds.toString()
//                    timeOutSeconds--
//                    if (timeOutSeconds < 0) {
//                        timeOutSeconds = 60L
//                        timer.cancel()
//                        tv.text = null
//                        tv.setTextColor(Color.WHITE)
//                        runOnUiThread {
//                            //RESEND ON CLICK   //58 Sec
//                            findViewById<TextView>(R.id.textView).setOnClickListener(View.OnClickListener {
//                                verifyPhoneNumber(phoneNumber)
//                                startResendTimer() //recursion
//                                //            String smsCode = phoneAuthCredential.getSmsCode(); //Gets the auto-retrieved SMS verification code
//                                //
//                                //            if (smsCode != null) {  //If code is non null
//                                //                verifyCode(smsCode);
//                                //                pinView.setText(smsCode);
//                                //            }
//                            })
//                        }
//                    }
//                }
//            }
//        }, 0, 1000)
//    }
//
//    private fun verifyPhoneNumber(phoneNumber: String) {
//        verifyPhoneNumber(
//            PhoneAuthOptions.newBuilder(auth)
//                .setPhoneNumber(phoneNumber)
//                .setTimeout(timeOutSeconds, TimeUnit.SECONDS)
//                .setActivity(this)
//                .setCallbacks(mCallbacks)
//                .setForceResendingToken(resendingToken)
//                .build()
//        )
//    }
    }





