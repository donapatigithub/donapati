package com.example.donapati.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.donapati.databinding.ActivitySignInBinding
import com.example.donapati.db.DatabaseHelper

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var db: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)
        onClick()
    }

    fun onClick() {
        binding.signUpTextView.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }
        binding.loginButton.setOnClickListener {
            val userName = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (userName.isEmpty() || password.isEmpty()) {
                binding.errorMessage.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                binding.errorMessage.visibility = View.GONE
                if (validateUser(userName, password)) {
                    Toast.makeText(this@SignInActivity, "Login Successful", Toast.LENGTH_SHORT)
                        .show()
                    Thread.sleep(500)
                    startActivity(Intent(this@SignInActivity, DashboardActivity::class.java))
                } else {
                    Toast.makeText(
                        this@SignInActivity,
                        "Login Failed/User Not Found",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
        binding.forgotPasswordTextView.setOnClickListener {
            startActivity(Intent(this@SignInActivity, ForgotPasswordActivity::class.java))
        }
    }

    private fun validateUser(userName: String, password: String): Boolean {
        return db.getUser(userName, password)
    }
}