package com.valenpatel.notes.authenticaton

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.valenpatel.notes.R
import com.valenpatel.notes.databinding.ActivityAddPasswordBinding
import com.valenpatel.paisefy.preferences.SharedPreference

class Add_Password : AppCompatActivity() {
    private lateinit var binding: ActivityAddPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val preference = SharedPreference(context = applicationContext)
        if (preference.getPassword() != null) {
            startActivity(Intent(this, PasswordAuthenticationActivity::class.java))
        }

        binding.submitButton.setOnClickListener {
            val pass = binding.passwordAdd.text
            if (pass.toString().isEmpty()) {
                binding.passwordAdd.error = "Enter Password"
                return@setOnClickListener
            }else {
                val preference =
                    SharedPreference(context = applicationContext).setPassword(pass.toString())
                startActivity(Intent(this, PasswordAuthenticationActivity::class.java))
            }
        }
    }
}