package com.example.donapati.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.donapati.databinding.PreLoginActivityBinding

class PreLoginActivity : AppCompatActivity() {

    private lateinit var binding: PreLoginActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = PreLoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
    }

    fun onClick() {
        binding.apply {
            signupBtn.setOnClickListener {
                startActivity(Intent(this@PreLoginActivity, SignUpActivity::class.java))
            }
            signinBtn.setOnClickListener {
                startActivity(Intent(this@PreLoginActivity, SignInActivity::class.java))
            }
        }
    }
}