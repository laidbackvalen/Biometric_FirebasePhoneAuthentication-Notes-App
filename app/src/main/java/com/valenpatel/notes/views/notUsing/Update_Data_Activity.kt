package com.valenpatel.notes.views.notUsing

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.valenpatel.notes.R
import com.valenpatel.notes.model.DataClass
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.Calendar

class Update_Data_Activity : AppCompatActivity() {

    lateinit var key: String
    lateinit var image: String
    lateinit var image2: String
    lateinit var image3: String
    lateinit var image4: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data)

        findViewById<ImageView>(R.id.backImgUpdateData).setOnClickListener {
            finish()
        }
        var dataClass: DataClass = intent.getSerializableExtra("user") as DataClass
        Toast.makeText(this, ""+dataClass, Toast.LENGTH_SHORT).show()
//        Toast.makeText(this, "" + dataClass.title, Toast.LENGTH_SHORT).show()
        key = dataClass.key
        image = dataClass.image1.toString()
        image2 = dataClass.image2.toString()
        image3 = dataClass.image3.toString()
        image4 = dataClass.image4.toString()

        findViewById<TextInputEditText>(R.id.titleTextUpdateData).setText(dataClass.title)
        findViewById<TextInputEditText>(R.id.descriptiontextUpdateData).setText(dataClass.description)
        findViewById<TextInputEditText>(R.id.pricetextUpdateData).setText(dataClass.price)
        findViewById<TextInputEditText>(R.id.quantitytextUpdateData).setText(dataClass.quantity)
        findViewById<TextInputEditText>(R.id.moreUpdateData).setText(dataClass.more)

        Glide.with(this)
            .asBitmap()
            .load(dataClass.image1)
            .placeholder(R.drawable.baseline_image_24)
//            .error(R.drawable.ic_launcher_background)
            .into(findViewById<ImageView>(R.id.imageOneUpdateData))

        Glide.with(this)
            .asBitmap()
            .load(dataClass.image2)
            .placeholder(R.drawable.baseline_image_24)
            .into(findViewById<ImageView>(R.id.imageTwoUpdateData))

        Glide.with(this)
            .asBitmap()
            .load(dataClass.image3)
            .placeholder(R.drawable.baseline_image_24)
            .into(findViewById<ImageView>(R.id.imageThreeUpdateData))

        Glide.with(this)
            .asBitmap()
            .load(dataClass.image4)
            .placeholder(R.drawable.baseline_image_24)
            .into(findViewById<ImageView>(R.id.imageFourUpdateData))


            var title = findViewById<TextInputEditText>(R.id.titleTextUpdateData)
            var description = findViewById<TextInputEditText>(R.id.descriptiontextUpdateData)
            var price = findViewById<TextInputEditText>(R.id.pricetextUpdateData)
            var quantity = findViewById<TextInputEditText>(R.id.quantitytextUpdateData)
            var more = findViewById<TextInputEditText>(R.id.moreUpdateData)


            findViewById<Button>(R.id.addnotesButtonUpdateData).setOnClickListener {
                if (title.text.isNullOrEmpty()) {
                    title.setError("Must Enter Title")
                } else if (description.text.isNullOrEmpty()) {
                    description.setError("Must Enter Description")
                } else {
                    var titleToAdd = title?.text.toString()
                    var descriptionToAdd = description?.text.toString()
                    var priceToAdd = price?.text.toString()
                    var quantityToAdd = quantity?.text.toString()
                    var moreToAdd = more?.text.toString()

                    val date = Calendar.getInstance().time
                    //  val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                    // val formatedDate = formatter.format(date)
                    val dateFormat =
                        DateFormat.getDateInstance(DateFormat.FULL).format(date).toString()
                    val timeFormat =
                        DateFormat.getTimeInstance(DateFormat.FULL).format(date).toString()

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




            findViewById<ImageView>(R.id.imageOneUpdateData).setOnClickListener {
                activityResultLauncher.launch("image/*") // "*/*" //for any types of files
            }
            findViewById<ImageView>(R.id.imageTwoUpdateData).setOnClickListener {
                activityResultLauncher2.launch("image/*") // "*/*" //for any types of files
            }
            findViewById<ImageView>(R.id.imageThreeUpdateData).setOnClickListener {
                activityResultLauncher3.launch("image/*") // "*/*" //for any types of files
            }
            findViewById<ImageView>(R.id.imageFourUpdateData).setOnClickListener {
                activityResultLauncher4.launch("image/*") // "*/*" //for any types of files
            }
        }


    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var a = auth.uid.toString()
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var databaseReference = firebaseDatabase.getReference("Notes").child(a)
    val notekey = databaseReference.push().key



    private fun addNotes(titleToAdd: String, descriptionToAdd: String, priceToAdd: String, quantityToAdd: String, moreToAdd: String, dateFormat: String, timeFormat: String) {

        val userdata: DataClass = DataClass(titleToAdd, descriptionToAdd, priceToAdd, quantityToAdd, moreToAdd, dateFormat, timeFormat, key, image, image2, image3, image4,"")
        databaseReference.child(key).setValue(userdata).addOnCompleteListener {
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
                            findViewById<ImageView>(R.id.imageOneUpdateData).setImageURI(uri)
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
                            findViewById<ImageView>(R.id.imageTwoUpdateData).setImageURI(uri2)
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
                            findViewById<ImageView>(R.id.imageThreeUpdateData).setImageURI(uri3)
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
                            findViewById<ImageView>(R.id.imageFourUpdateData).setImageURI(result)
                            image4 = it.toString() //actual image link
                        }

                        // Handle the URI, for example, display the selected image
                    }
            } else {
                // Handle null result
            }
        })


}
