package com.example.donapati.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.donapati.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickListener()
    }

    fun onClickListener() {
        binding.signUpTextView.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.confirmButton.setOnClickListener {
            Toast.makeText(this, "E-Mail Sent", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}