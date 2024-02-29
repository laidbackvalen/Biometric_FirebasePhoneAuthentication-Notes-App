package com.example.notes.authenticaton

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.notes.R
import com.example.notes.activities.crud.All_Notes_Activity
import com.google.firebase.auth.FirebaseAuth

class Biometric_Authentication_Activity : AppCompatActivity() {

    // create a CancellationSignal variable and assign a value null to it
    private var cancellationSignal: CancellationSignal? = null

    // create an authenticationCallback
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            // here we need to implement two methods
            // onAuthenticationError and onAuthenticationSucceeded
            // If the fingerprint is not recognized by the app it will call
            // onAuthenticationError and show a toast
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                notifyUser("Authentication Error : $errString")
            }

            // If the fingerprint is recognized by the app then it will call
            // onAuthenticationSucceeded and show a toast that Authentication has Succeed
            // Here you can also start a new activity after that
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                if(FirebaseAuth.getInstance().currentUser!=null){
                    startActivity(Intent(baseContext, All_Notes_Activity::class.java))
                    notifyUser("Authentication Succeeded")
                    finish()
                }
                else{
                    startActivity(Intent(baseContext, Login_Activity::class.java))
                }
                // or start a new Activity

            }
        }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric_authentication)

        checkBiometricSupport()
        // create a biometric dialog on Click of button

            // This creates a dialog of biometric auth and
            // it requires title , subtitle ,
            // and description
            // In our case there is a cancel button by
            // clicking it, it will cancel the process of
            // fingerprint authentication
            val biometricPrompt = BiometricPrompt.Builder(this)
                .setTitle("Login with Biometric")
                .setSubtitle("")
                .setDescription("")
                .setNegativeButton(
                    "Cancel",
                    this.mainExecutor,
                    DialogInterface.OnClickListener { dialog, which ->
                        notifyUser("Authentication Cancelled")
                        finish()
                    }).build()

            // start the authenticationCallback in mainExecutor
            biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
        }


    // it will be called when authentication is cancelled by the user
    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was Cancelled by the user")
            finish()
        }
        return cancellationSignal as CancellationSignal

    }

    // it checks whether the app the app has fingerprint permission
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure) {
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED
        ) { notifyUser("Fingerprint Authentication Permission is not enabled")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    // this is a toast method which is responsible for showing toast
    // it takes a string as parameter
    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
