package com.example.notes.activities.crud

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.R
import com.example.notes.dataclass.DataClass
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.Calendar

class Create_Data_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_data)

        findViewById<ImageView>(R.id.backImgMoreData).setOnClickListener {
            finish()
        }
        var title = findViewById<TextInputEditText>(R.id.titleTextAddMore)
        var description = findViewById<TextInputEditText>(R.id.descriptiontextAddMore)
        var price = findViewById<TextInputEditText>(R.id.pricetextAddMore)
        var quantity = findViewById<TextInputEditText>(R.id.quantitytextAddMore)
        var more = findViewById<TextInputEditText>(R.id.moreAddMore)


        findViewById<Button>(R.id.addnotesButtonMoreData).setOnClickListener {
            if (title.text.isNullOrEmpty()) {
                title.setError("Must Enter Title")
            } else if (description.text.isNullOrEmpty()) {
                description.setError("Must Enter Description")
            }else {
                var titleToAdd = title?.text.toString()
                var descriptionToAdd = description?.text.toString()
                var priceToAdd = price?.text.toString()
                var quantityToAdd = quantity?.text.toString()
                var moreToAdd = more?.text.toString()

                val date = Calendar.getInstance().time
                //  val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                // val formatedDate = formatter.format(date)
                val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(date).toString()
                val timeFormat = DateFormat.getTimeInstance(DateFormat.FULL).format(date).toString()

                addNotes(
                    titleToAdd,
                    descriptionToAdd,
                    priceToAdd,
                    quantityToAdd,
                    moreToAdd,
                    dateFormat,
                    timeFormat
                )
            }
        }




        findViewById<ImageView>(R.id.imageOneMoreData).setOnClickListener {
            activityResultLauncher.launch("image/*") // "*/*" //for any types of files
        }
        findViewById<ImageView>(R.id.imageTwoMoreData).setOnClickListener {
            activityResultLauncher2.launch("image/*") // "*/*" //for any types of files
        }
        findViewById<ImageView>(R.id.imageThreeMoreData).setOnClickListener {
            activityResultLauncher3.launch("image/*") // "*/*" //for any types of files
        }
        findViewById<ImageView>(R.id.imageFourMoreData).setOnClickListener {
            activityResultLauncher4.launch("image/*") // "*/*" //for any types of files
        }


    }


    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var a = auth.uid.toString()
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var databaseReference = firebaseDatabase.getReference("Notes").child(a)
    val notekey = databaseReference.push().key

    private fun addNotes(
        titleToAdd: String,
        descriptionToAdd: String,
        priceToAdd: String,
        quantityToAdd: String,
        moreToAdd: String,
        dateFormat: String,
        timeFormat: String
    ) {

        val userdata: DataClass = DataClass(titleToAdd, descriptionToAdd, priceToAdd, quantityToAdd, moreToAdd, dateFormat, timeFormat, notekey as String, image, image2, image3, image4)
        databaseReference.child(notekey).setValue(userdata).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show()
//                    findViewById<TextInputEditText>(R.id.titleText).text = null
//                    findViewById<TextInputEditText>(R.id.descriptiontext).text = null
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "" + it.message.toString(), Toast.LENGTH_SHORT).show()

        }
    }

    var image: String = ""
    var image2: String = ""
     var image3: String = ""
     var image4: String = ""

    val fireStorage = FirebaseStorage.getInstance()
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent(), fun(result: Uri?) {

            // Handle the result URI here

            if (result != null) {
                fireStorage.getReference().child("images")
                    .child(FirebaseAuth.getInstance().uid.toString())
                    .child(notekey.toString())
                    .child("Img_" + System.currentTimeMillis()).putFile(result)
                    .addOnCompleteListener {
                        it.result.storage.downloadUrl.addOnSuccessListener {
                           var uri = result
                            findViewById<ImageView>(R.id.imageOneMoreData).setImageURI(uri)
                            image = it.toString() //actual image link
//                            Toast.makeText(this, "" + it, Toast.LENGTH_SHORT).show()
                        }


                        // Handle the URI, for example, display the selected image
                    }
            } else {
                // Handle null result
            }
        })

    private val activityResultLauncher2 =
        registerForActivityResult(ActivityResultContracts.GetContent(), fun(result: Uri?) {
            // Handle the result URI here
            if (result != null) {
                fireStorage.getReference().child("images")
                    .child(FirebaseAuth.getInstance().uid.toString())
                    .child(databaseReference.toString())
                    .child("Img_" + System.currentTimeMillis()).putFile(result)
                    .addOnCompleteListener {
                        it.result.storage.downloadUrl.addOnSuccessListener {
                           var uri2 = result
                            findViewById<ImageView>(R.id.imageTwoMoreData).setImageURI(uri2)
                            image2 = it.toString() //actual image link
                        }

                        // Handle the URI, for example, display the selected image
                    }
            } else {
                // Handle null result
            }
        })

    private val activityResultLauncher3 =
        registerForActivityResult(ActivityResultContracts.GetContent(), fun(result: Uri?) {
            // Handle the result URI here
            if (result != null) {
                fireStorage.getReference().child("images")
                    .child(FirebaseAuth.getInstance().uid.toString())
                    .child(notekey.toString())
                    .child("Img_" + System.currentTimeMillis()).putFile(result)
                    .addOnCompleteListener {
                        it.result.storage.downloadUrl.addOnSuccessListener {
                           var uri3 = result
                            findViewById<ImageView>(R.id.imageThreeMoreData).setImageURI(uri3)
                            image3 = it.toString() //actual image link
                        }

                        // Handle the URI, for example, display the selected image
                    }
            } else {
                // Handle null result
            }
        })
    private val activityResultLauncher4 =
        registerForActivityResult(ActivityResultContracts.GetContent(), fun(result: Uri?) {
            // Handle the result URI here
            if (result != null) {
                fireStorage.getReference().child("images")
                    .child(FirebaseAuth.getInstance().uid.toString())
                    .child(notekey.toString())
                    .child("Img_" + System.currentTimeMillis()).putFile(result)
                    .addOnCompleteListener {
                        it.result.storage.downloadUrl.addOnSuccessListener {
                           var uri4 = result
                            findViewById<ImageView>(R.id.imageFourMoreData).setImageURI(result)
                            image4 = it.toString() //actual image link
                        }

                        // Handle the URI, for example, display the selected image
                    }
            } else {
                // Handle null result
            }
        })



}
