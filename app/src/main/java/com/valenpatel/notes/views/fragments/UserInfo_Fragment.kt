package com.valenpatel.notes.views.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.valenpatel.notes.R
import com.valenpatel.notes.model.UserInfo

class UserInfo_Fragment : BottomSheetDialogFragment() {

    private lateinit var nameEditText: TextInputEditText
    private var image: String = ""

    private lateinit var progressBar: ProgressBar
    private val fireStorage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.uid.toString()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("Notes").child(user)

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { result: Uri? ->
        result?.let {
            uploadImage(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)

        // Initialize the TextInputEditText views
        nameEditText = view.findViewById(R.id.nameUserInfo)
        progressBar = view.findViewById(R.id.progressBar)

        view.findViewById<Button>(R.id.saveInfoButton).setOnClickListener {
            if (nameEditText.text.toString().trim().isNotEmpty()) {
                addUserInfo()
            } else {
                Toast.makeText(requireContext(), "You need to add name to save information.", Toast.LENGTH_SHORT).show()
            }
        }
        retrieveUserInfo()

        view.findViewById<ImageView>(R.id.userInfoImage).setOnClickListener {
            activityResultLauncher.launch("image/*")
        }
        return view
    }

    private fun uploadImage(imageUri: Uri) {
        val storageReference = fireStorage.getReference("images/${FirebaseAuth.getInstance().uid}/Img_${System.currentTimeMillis()}")
        progressBar.visibility = View.VISIBLE
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    if (isAdded && context != null) {  // Check if fragment is still attached
                        image = uri.toString()
                        view?.findViewById<ImageView>(R.id.userImageView)?.setImageURI(imageUri)
                        Toast.makeText(
                            requireContext(),
                            "Image uploaded successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar.visibility = View.GONE
                    }
                }
            }
            .addOnFailureListener {
                if (isAdded && context != null) {  // Check if fragment is still attached
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Failed to upload image: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun retrieveUserInfo() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    if (itemSnapshot.key == "UserInfo") {
                        val userInfo = itemSnapshot.getValue(UserInfo::class.java)
                        userInfo?.let {
                            if (isAdded && context != null) {  // Check if fragment is still attached
                                view?.findViewById<TextInputEditText>(R.id.nameUserInfo)?.setText(it.username)
                                val imageView = view?.findViewById<ImageView>(R.id.userImageView)
                                if (imageView != null) {
                                    Glide.with(requireActivity())
                                        .load(it.userProfileImage)
                                        .placeholder(R.drawable.baseline_person_24)
                                        .into(imageView)
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (isAdded && context != null) {  // Check if fragment is still attached
                    Toast.makeText(requireContext(), "Failed to retrieve user info.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun addUserInfo() {
        val userName = nameEditText.text.toString().trim()

        if (userName.isNotEmpty()) {
            val userInfo = UserInfo(userName, image)
            databaseReference.child("UserInfo").setValue(userInfo).addOnCompleteListener {
                if (isAdded && context != null) {  // Check if fragment is still attached
                    if (it.isSuccessful) {
                        Toast.makeText(requireActivity(), "Successfully saved user info", Toast.LENGTH_SHORT).show()
                        dismiss()  // Close the bottom sheet
                    } else {
                        val errorMessage = it.exception?.message ?: "Unknown error"
                        Log.e("UserInfo_Fragment", "Error: $errorMessage")
                        Toast.makeText(requireActivity(), "Failed to save user info: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            if (isAdded && context != null) {  // Check if fragment is still attached
                Toast.makeText(requireActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
