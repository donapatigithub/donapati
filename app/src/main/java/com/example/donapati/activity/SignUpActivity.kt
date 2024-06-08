package com.example.donapati.activity

import android.Manifest
import android.accounts.AccountManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.donapati.databinding.ActivitySignUpBinding
import com.example.donapati.db.DatabaseHelper

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var dbHelper: DatabaseHelper
    private val GET_ACCOUNTS_PERMISSION_REQUEST = 1
    private val TAG = "SignUpActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseHelper(this)
        onClick()
    }

    private fun onClick() {
        binding.apply {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)

            signUpButton.setOnClickListener {
                val username = usernameEditText.text.toString()
                val email = emailEditText.text.toString()
                val phone = phoneEditText.text.toString()
                val password = passwordEditText.text.toString()
                val confirmPassword = cnfPasswordEditText.text.toString()

                if (password == confirmPassword) {
                    val success = dbHelper.addUser(username, email, phone, password)
                    if (success) {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Sign up successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        Thread.sleep(1000)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignUpActivity, "Sign up failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            loginLink.setOnClickListener {
                startActivity(intent)
            }
            backButtonInclude.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, PreLoginActivity::class.java))
            }
            googleButton.setOnClickListener {
                openApp("com.google.android.apps.gmail")
            }
            facebookButton.setOnClickListener {
                openApp("com.facebook.katana")
            }

            if (ContextCompat.checkSelfPermission(
                    this@SignUpActivity,
                    Manifest.permission.GET_ACCOUNTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@SignUpActivity,
                    arrayOf(Manifest.permission.GET_ACCOUNTS),
                    GET_ACCOUNTS_PERMISSION_REQUEST
                )
            } else {
                getGoogleAccounts()
            }
        }
    }

    private fun getGoogleAccounts() {
        val accountManager = AccountManager.get(this)
        val googleAccounts = accountManager.getAccountsByType("com.google")

        for (account in googleAccounts) {
            val email = account.name
            Log.d(TAG, "Google Account: $email")
        }
    }

    private fun openApp(packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            startActivity(intent)
        } else {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GET_ACCOUNTS_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGoogleAccounts()
            } else {
                Toast.makeText(this, "Permission denied to access accounts", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}