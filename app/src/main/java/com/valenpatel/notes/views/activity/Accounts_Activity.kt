package com.valenpatel.notes.views.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.valenpatel.notes.R
import com.valenpatel.notes.authenticaton.Login_Activity
import com.valenpatel.notes.views.crud.Main_Retrieve_All_Notes_Activity
import java.util.concurrent.TimeUnit

class Accounts_Activity : AppCompatActivity() {

    private val fireStorage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.uid.toString()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val notesReference = firebaseDatabase.getReference("Notes").child(userId)

    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_accounts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        findViewById<ImageView>(R.id.backArrowImage).setOnClickListener {
            startActivity(Intent(this, Main_Retrieve_All_Notes_Activity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }

        findViewById<View>(R.id.removeUserInfo).setOnClickListener {
            showRemoveUserInfoDialog()
        }

        findViewById<View>(R.id.removeData).setOnClickListener {
            showRemoveDataDialog()
        }

        findViewById<View>(R.id.closeAccount).setOnClickListener {
            showCloseAccountDialog()
        }
    }

    private fun showRemoveUserInfoDialog() {
        val userInfoAlertDialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setTitle("Remove User Info")
            .setMessage("The user information will be removed from the device. Are you sure you want to remove it? (ONCE REMOVED CANNOT BE RECOVERED)")
            .setPositiveButton("Yes") { dialog, _ ->
                notesReference.child("UserInfo").removeValue()
                Toast.makeText(this, "User info removed successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        userInfoAlertDialog.show()
        userInfoAlertDialog.findViewById<TextView>(android.R.id.message)?.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun showRemoveDataDialog() {
        val userInfoAlertDialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setTitle("Remove Data")
            .setMessage("The data related to all notes will be completely removed from the device. Are you sure you want to remove it? (ONCE REMOVED CANNOT BE RECOVERED)")
            .setPositiveButton("Yes") { dialog, _ ->
                notesReference.removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "All notes removed successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error removing notes: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        userInfoAlertDialog.show()
        userInfoAlertDialog.findViewById<TextView>(android.R.id.message)?.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun showCloseAccountDialog() {
        val userInfoAlertDialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setTitle("Close Account")
            .setMessage("The account and every data related to it will be completely removed and deleted from this action. Are you sure you want to delete it? (ONCE DELETED CANNOT BE RECOVERED)")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                showPhoneVerificationDialog()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        userInfoAlertDialog.show()
        userInfoAlertDialog.findViewById<TextView>(android.R.id.message)?.setTextColor(ContextCompat.getColor(this, R.color.black))
    }


    @SuppressLint("SuspiciousIndentation")
    private fun showPhoneVerificationDialog() {
    val accountDeletDialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
    val accountDeleteView = layoutInflater.inflate(R.layout.alertdialog_account_deletation_code, null)
        accountDeletDialog.setView(accountDeleteView)
        accountDeletDialog.create()
        accountDeletDialog.show()

        val phoneNumberEditText = accountDeleteView.findViewById<TextInputEditText>(R.id.userNumber)

        accountDeleteView.findViewById<Button>(R.id.addNumberButton).setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                sendVerificationCode(phoneNumber)
                Toast.makeText(this, "Verification code sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-retrieval or instant verification
                    reauthenticateAndDeleteUser(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@Accounts_Activity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    this@Accounts_Activity.verificationId = verificationId
                    showCodeVerificationDialog()
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun showCodeVerificationDialog() {
        val codeVerificationtDialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val codeVerificationView = layoutInflater.inflate(R.layout.code_verification_dialog, null)
        codeVerificationtDialog.setView(codeVerificationView)
        codeVerificationtDialog.create()
        codeVerificationtDialog.show()

        val verificationCodeEditText = codeVerificationView.findViewById<TextInputEditText>(R.id.verificationCode)

        codeVerificationView.findViewById<Button>(R.id.verifyCodeButton).setOnClickListener {
            val verificationCode = verificationCodeEditText.text.toString().trim()
            if (verificationCode.isNotEmpty() && verificationId != null) {
                val credential = PhoneAuthProvider.getCredential(verificationId!!, verificationCode)
                reauthenticateAndDeleteUser(credential)
                Toast.makeText(this, "Verification successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter the verification code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reauthenticateAndDeleteUser(credential: PhoneAuthCredential) {
        auth.currentUser?.reauthenticate(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    deleteUserAccount()
                } else {
                    Toast.makeText(this, "Re-authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun deleteUserAccount() {
        val user = auth.currentUser
        // Remove user data from Realtime Database
        notesReference.removeValue().addOnCompleteListener { removeTask ->
            if (removeTask.isSuccessful) {
                // Proceed with user deletion from Firebase Authentication
                user?.delete()?.addOnCompleteListener { deleteTask ->
                    if (deleteTask.isSuccessful) {
                        Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this, Login_Activity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                    } else {
                        Toast.makeText(
                            this,
                            "Account deletion failed: ${deleteTask.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Failed to remove user data: ${removeTask.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
