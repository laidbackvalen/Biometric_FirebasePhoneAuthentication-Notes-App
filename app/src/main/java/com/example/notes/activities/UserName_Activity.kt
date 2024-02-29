package com.example.notes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.notes.R
import com.example.notes.activities.crud.All_Notes_Activity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserName_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_name)

        findViewById<ImageView>(R.id.backIconUserNameActivity).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.submitUserNameButton).setOnClickListener {
            var name = findViewById<TextInputEditText>(R.id.usersName).text.toString()
//            addUserName(name)

        }
    }

//    private fun addUserName(name: String) {
//        val firebaseAuth = FirebaseAuth.getInstance()
//        val a = firebaseAuth.uid.toString()
//        val firebaseDatabase = FirebaseDatabase.getInstance()
//        val b = ImageDataClass(name)
//        val databaseReference = firebaseDatabase.getReference("Notes").child(a).setValue(b).addOnCompleteListener {
//                if (it.isSuccessful) {
//                    Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
//                    val naam = Intent(this, All_Notes_Activity::class.java)
//                    naam.putExtra("name", name)
//                    Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }.addOnFailureListener {
//                Toast.makeText(this, ""+it.toString(), Toast.LENGTH_SHORT).show()
//            }
    }

