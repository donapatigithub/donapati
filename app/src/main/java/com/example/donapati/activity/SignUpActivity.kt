package com.example.donapati.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.donapati.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val intent = Intent(this@SignUpActivity, PreLoginActivity::class.java)

            signUpButton.setOnClickListener {
                startActivity(intent)
            }
            loginLink.setOnClickListener {
                startActivity(intent)
            }
            backButtonInclude.setOnClickListener {
                startActivity(intent)
            }
        }
    }
}

