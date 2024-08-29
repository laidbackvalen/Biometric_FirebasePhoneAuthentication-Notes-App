package com.valenpatel.notes.views.crud

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.valenpatel.notes.R
import com.valenpatel.notes.model.DataClass
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.valenpatel.notes.views.activity.Show_Image_Activity
import com.valenpatel.notes.views.fragments.AddResource_Fragment
import com.valenpatel.notes.views.fragments.Color_Fragment
import com.valenpatel.notes.views.fragments.More_Fragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class Create_Update_Data_Activity : AppCompatActivity(),
    AddResource_Fragment.OnBottomSheetItemClickListener,
    More_Fragment.OnBottomSheetItemClickListener, Color_Fragment.OnColorSelectedListener {

    lateinit var editedOntext: TextView
    var count = 0;
    var dataClass: DataClass? = null
    lateinit var colorHex: String
    lateinit var titlefromIntent: TextInputEditText
    lateinit var notefromIntent: TextInputEditText
    lateinit var progressBar: ProgressBar
    var image: String = ""
    var image2: String = ""
    var image3: String = ""
    var image4: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_update_data_activity)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        findViewById<ImageView>(R.id.backImgMoreData).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.moreImageView).setOnClickListener {
            More_Fragment().show(supportFragmentManager, null)
        }

        findViewById<ImageView>(R.id.colorColorImageView).setOnClickListener {
            Color_Fragment().show(supportFragmentManager, null)
        }

        colorHex = "#1C1C1C"
        editedOntext = findViewById(R.id.editedOnTextView)

        //NEW CODE For NEW UI Starts
        dataClass = intent.getSerializableExtra("value") as DataClass?

        titlefromIntent = findViewById<TextInputEditText>(R.id.titleTextAddMore)
        notefromIntent = findViewById<TextInputEditText>(R.id.descriptiontextAddMore)

        // Request focus on the EditText
        notefromIntent.requestFocus()

        progressBar = findViewById(R.id.progressBarImage1Create)

        // Show the keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(notefromIntent, InputMethodManager.SHOW_IMPLICIT)

        if (dataClass != null) {
            // Set the background color based on the colorHex value
            colorHex = dataClass?.backgroundColor.toString()
            val color = Color.parseColor(colorHex)
            findViewById<View>(R.id.activity_create_update_data).setBackgroundColor(color)
            // Set the text in the EditText
            titlefromIntent.setText(dataClass?.title)
            notefromIntent.setText(dataClass?.description)
            editedOntext.text = dataClass?.time

            // Load existing images
            loadExistingImage(R.id.imageOneMoreData, dataClass?.image1)
            loadExistingImage(R.id.imageTwoMoreData, dataClass?.image2)
            loadExistingImage(R.id.imageThreeMoreData, dataClass?.image3)
            loadExistingImage(R.id.imageFourMoreData, dataClass?.image4)
        } else {
            var date = System.currentTimeMillis()
            var sdf = SimpleDateFormat("HH:mm:ss")
            editedOntext.text = sdf.format(date)
        }

        findViewById<ImageView>(R.id.addOptionsImageView).setOnClickListener {
            AddResource_Fragment().show(supportFragmentManager, null)
                count++
        }


        findViewById<Button>(R.id.addnotesButtonMoreData).setOnClickListener {
            if (notefromIntent.text.isNullOrEmpty()) {
                notefromIntent.error = "Must enter a note before you save."
            } else {
                val titleToAdd = titlefromIntent.text.toString().trim()
                val descriptionToAdd = notefromIntent.text.toString()

                // Get current date and time in the required format
                val date = Calendar.getInstance().time
                val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(date)
                val timeFormat = DateFormat.getTimeInstance(DateFormat.FULL).format(date)

                // Set default color if colorHex is not set or invalid
                val defaultColor = "#1C1C1C"
                val validColorHex =
                    if (::colorHex.isInitialized && colorHex.length >= 2) colorHex else defaultColor

                // Check if we're updating or adding a new note
                if (dataClass != null) {
                    // Update note
                    dataClass?.key?.let { key ->

                        updateNote(
                            titleToAdd,
                            descriptionToAdd,
                            "a",
                            "b",
                            "c",
                            dateFormat,
                            timeFormat,
                            key,
                            validColorHex
                        )
                    }
                } else {
                    // Add new note
                    addNotes(
                        titleToAdd,
                        descriptionToAdd,
                        "a",
                        "b",
                        "c",
                        dateFormat,
                        timeFormat,
                        validColorHex
                    )
                }
            }
        }
        findViewById<ImageView>(R.id.imageOneMoreData).setOnClickListener {
          val intent1 = Intent(this, Show_Image_Activity::class.java)
            intent.putExtra("image1", dataClass?.image1)
            startActivity(intent1)
        }
        findViewById<ImageView>(R.id.imageTwoMoreData).setOnClickListener {
          val intent2 = Intent(this, Show_Image_Activity::class.java)
            intent.putExtra("image2", dataClass?.image2)
            startActivity(intent2)
        }
        findViewById<ImageView>(R.id.imageThreeMoreData).setOnClickListener {
          val intent3 = Intent(this, Show_Image_Activity::class.java)
            intent.putExtra("image3", dataClass?.image3)
            startActivity(intent3)
        }
        findViewById<ImageView>(R.id.imageFourMoreData).setOnClickListener {
          val intent4 = Intent(this, Show_Image_Activity::class.java)
            intent.putExtra("image4", dataClass?.image4)
            startActivity(intent4)
        }
    }

    private fun loadExistingImage(imageViewId: Int, imageUrl: String?) {
        val imageView = findViewById<ImageView>(imageViewId)
        if (!imageUrl.isNullOrEmpty()) {
            imageView.visibility = View.VISIBLE
            Glide.with(applicationContext).asBitmap().load(imageUrl).into(imageView)
            count++
        } else {
            imageView.visibility = View.GONE
        }
    }

    //onCreate Ends Here
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var a = auth.uid.toString()
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var databaseReference = firebaseDatabase.getReference("Notes").child(a)
    val notekey = databaseReference.push().key

    private fun addNotes(titleToAdd: String, descriptionToAdd: String, priceToAdd: String, quantityToAdd: String, moreToAdd: String, dateFormat: String, timeFormat: String, colorHex: String) {
        val userdata: DataClass = DataClass(titleToAdd, descriptionToAdd, priceToAdd, quantityToAdd, moreToAdd, dateFormat, timeFormat, notekey as String, image, image2, image3, image4, colorHex)
        databaseReference.child(notekey).setValue(userdata).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "" + it.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateNote(titlefromIntent: String, notefromIntent: String, priceToAdd: String, quantityToAdd: String, moreToAdd: String, dateFormat: String, timeFormat: String, key: String, toString: String) {

        val userdata = DataClass(titlefromIntent, notefromIntent, priceToAdd, quantityToAdd, moreToAdd, dateFormat, timeFormat, key, image, image2, image3, image4, colorHex)
        databaseReference.child(key).setValue(userdata).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "" + it.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    val fireStorage = FirebaseStorage.getInstance()
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent(), fun(result: Uri?) {
            progressBar.visibility = View.VISIBLE
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
                            progressBar.visibility = View.GONE
                        }
                        // Handle the URI, for example, display the selected image
                    }
            } else {
                findViewById<ImageView>(R.id.imageOneMoreData).visibility = View.GONE
                count = 0
                progressBar.visibility = View.GONE
            }
        })


    private val activityResultLauncher2 =
        registerForActivityResult(ActivityResultContracts.GetContent(), fun(result: Uri?) {
            val progressBar2 = findViewById<ProgressBar>(R.id.progressBarImage2Create)
            progressBar2.visibility = View.VISIBLE
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
                            progressBar2.visibility = View.GONE
                        }
                        // Handle the URI, for example, display the selected image
                    }
            } else {
                findViewById<ImageView>(R.id.imageTwoMoreData).visibility = View.GONE
                count = 1
                progressBar2.visibility = View.GONE
            }
        })

    private val activityResultLauncher3 =
        registerForActivityResult(ActivityResultContracts.GetContent(), fun(result: Uri?) {
            val progressBar3 = findViewById<ProgressBar>(R.id.progressBarImage3Create)
            progressBar3.visibility = View.VISIBLE
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
                            progressBar3.visibility = View.GONE
                        }
                        // Handle the URI, for example, display the selected image
                    }
            } else {
                findViewById<ImageView>(R.id.imageThreeMoreData).visibility = View.GONE
                count = 2
                progressBar3.visibility = View.GONE
            }
        })
    private val activityResultLauncher4 =
        registerForActivityResult(ActivityResultContracts.GetContent(), fun(result: Uri?) {
            val progressBar4 = findViewById<ProgressBar>(R.id.progressBar4ImageCreate)
            progressBar4.visibility = View.VISIBLE
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
                            progressBar4.visibility = View.GONE
                        }

                        // Handle the URI, for example, display the selected image
                    }
            } else {
                findViewById<ImageView>(R.id.imageFourMoreData).visibility = View.GONE
                count = 3
                progressBar4.visibility = View.GONE
            }
        })

    override fun onItemSelected() {
        if (count == 1) {
            findViewById<ImageView>(R.id.imageOneMoreData).visibility = View.VISIBLE
            activityResultLauncher.launch("image/*")
        }
        if (count == 2) {
            findViewById<ImageView>(R.id.imageTwoMoreData).visibility = View.VISIBLE
            activityResultLauncher2.launch("image/*")
        }
        if (count == 3) {
            val imageThreeView = findViewById<ImageView>(R.id.imageThreeMoreData)
            imageThreeView.visibility = View.VISIBLE
            activityResultLauncher3.launch("image/*")
        }
        if (count == 4) {
            val imageFourView = findViewById<ImageView>(R.id.imageFourMoreData)
            imageFourView.visibility = View.VISIBLE
            activityResultLauncher4.launch("image/*")
        }
        if (count > 4) {
            Toast.makeText(this, "You can only add 4 images", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDelete() {
        // Assuming `dataClass` and `databaseReference` are accessible in this context
        dataClass?.key?.let { key ->
            databaseReference.child(key).removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity if needed
                } else {
                    Toast.makeText(this, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } ?: run {
            Toast.makeText(
                this,
                "No data to delete, Firstly save data! If you haven't!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCopy() {
        dataClass?.let { data ->
            val newKey = databaseReference.push().key ?: return
            val newData = data.copy(key = newKey)
            databaseReference.child(newKey).setValue(newData).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successfully Copied", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
            ?: run {
                Toast.makeText(
                    this,
                    "No data to copy, Firstly save data! If you haven't!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onShare() {
        dataClass?.let { data ->
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Title : ${data.title}\n\nNote : ${data.description}\n\nDate : ${data.date}\n\nTime : ${data.time}"
                )
            }
            startActivity(Intent.createChooser(shareIntent, "Share Note"))
        } ?: run {
            Toast.makeText(
                this,
                "No data to share, Firstly save data! If you haven't!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onColorSelected(color: Int) {
        colorHex = String.format("#%06X", 0xFFFFFF and color)  // Convert color to hex format
        findViewById<View>(R.id.activity_create_update_data).setBackgroundColor(color)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
