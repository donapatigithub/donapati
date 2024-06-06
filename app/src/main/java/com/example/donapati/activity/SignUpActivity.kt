package com.example.donapati.activity

import android.Manifest
import android.accounts.AccountManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.donapati.databinding.ActivitySignUpBinding
import android.util.Log

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val READ_CONTACTS_PERMISSION_REQUEST = 1
    private val TAG = "SignUpActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
    }

    private fun onClick() {
        binding.apply {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)

            signUpButton.setOnClickListener {
                startActivity(intent)
            }
            loginLink.setOnClickListener {
                startActivity(intent)
            }
            backButtonInclude.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, PreLoginActivity::class.java))
            }
            googleButton.setOnClickListener {
                openApp("com.google")
            }
            facebookButton.setOnClickListener {
                openApp("com.facebook.katana")
            }

            if (ContextCompat.checkSelfPermission(this@SignUpActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@SignUpActivity, arrayOf(Manifest.permission.READ_CONTACTS), READ_CONTACTS_PERMISSION_REQUEST)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGoogleAccounts()
            } else {
                // Handle permission denial (e.g., show a message)
                Toast.makeText(this, "Permission denied to access accounts", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
