package com.valenpatel.notes.views.notUsing

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.valenpatel.notes.views.crud.Create_Update_Data_Activity
import com.valenpatel.notes.R
import com.google.android.material.textfield.TextInputEditText
import java.text.DateFormat
import java.util.Calendar

class Add_Notes_Activity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var titleTXT = findViewById<TextInputEditText>(R.id.titleText)
        var descriptionTXT = findViewById<TextInputEditText>(R.id.descriptiontext)
        var addNotesbutton = findViewById<Button>(R.id.addnotesButton)
        var backIcon = findViewById<ImageView>(R.id.backImg)
        var addMoreButton = findViewById<TextView>(R.id.addmoretoremindButton)

        addMoreButton.setOnClickListener {
            startActivity(Intent(this, Create_Update_Data_Activity::class.java))
            finish()
        }
        backIcon.setOnClickListener {
            finish()
        }


        addNotesbutton.setOnClickListener {
            if (titleTXT.text.isNullOrEmpty()) {
                titleTXT.setError("Enter Title")
            } else if (descriptionTXT.text.isNullOrEmpty()) {
                descriptionTXT.setError("Enter Description")
            } else {
                var title = titleTXT?.text.toString()
                var description = descriptionTXT.text.toString()

                val date = Calendar.getInstance().time
                //  val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                // val formatedDate = formatter.format(date)
                val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(date).toString()
                val timeFormat = DateFormat.getTimeInstance(DateFormat.FULL).format(date).toString()

//                addNotes(title, description, dateFormat, timeFormat)

            }
        }
    }

//ERROR
//    private fun addNotes(title: String, description: String, date: String, timeFormat: String) {
////        Toast.makeText(applicationContext, "$title $description", Toast.LENGTH_SHORT).show()
//        val auth : FirebaseAuth = FirebaseAuth.getInstance()
//
//        var a = auth.uid.toString()
//        var firebaseDatabase = FirebaseDatabase.getInstance()
//        var databaseReference = firebaseDatabase.getReference("Notes").child(a)
//        val notekey = databaseReference.push().key
//        val userdata: ImageDataClass = ImageDataClass(title, description,date,timeFormat, notekey as String)
//
//
//        if (notekey != null) {
//            databaseReference.child(notekey).setValue(userdata).addOnCompleteListener {
//                if (it.isSuccessful) {
//                    Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show()
//                    findViewById<TextInputEditText>(R.id.titleText).text = null
//                    findViewById<TextInputEditText>(R.id.descriptiontext).text = null
//                }
//            }.addOnFailureListener {
//                Toast.makeText(this, "" + it.message.toString(), Toast.LENGTH_SHORT).show()
//
//            }
//        }
//    }
}