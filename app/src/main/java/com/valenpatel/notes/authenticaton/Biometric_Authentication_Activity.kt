package com.valenpatel.notes.authenticaton

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.valenpatel.notes.R
import com.valenpatel.notes.views.crud.Main_Retrieve_All_Notes_Activity
import com.google.firebase.auth.FirebaseAuth

class Biometric_Authentication_Activity : AppCompatActivity() {

    private var cancellationSignal: CancellationSignal? = null
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                notifyUser("Authentication Error: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                if (FirebaseAuth.getInstance().currentUser != null) {
                    startActivity(Intent(baseContext, Main_Retrieve_All_Notes_Activity::class.java))
                    notifyUser("Authentication Succeeded")
                    finish()
                } else {
                    startActivity(Intent(baseContext, Login_Activity::class.java))
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric_authentication)

        findViewById<TextView>(R.id.loginWithPasswordTextView).setOnClickListener {
            handlePasswordLogin()
        }

        checkBiometricSupport()

        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("Login with Biometric")
            .setSubtitle("")
            .setDescription("")
            .setNegativeButton(
                "Cancel",
                this.mainExecutor,
                DialogInterface.OnClickListener { dialog, which ->
                    notifyUser("Authentication Cancelled")
                    handlePasswordLogin()
                }).build()

        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was Cancelled by the user")
            handlePasswordLogin()
        }
        return cancellationSignal as CancellationSignal
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure) {
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint Authentication Permission is not enabled")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else {
            notifyUser("Fingerprint feature is not supported")
            false
        }
    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun handlePasswordLogin() {
        val password = sharedPreferences.getString("password_key", null)
        if (password != null) {
            startActivity(Intent(this, PasswordAuthenticationActivity::class.java))
        } else {
            startActivity(Intent(this, Add_Password::class.java))
        }
        finish()
    }
}
