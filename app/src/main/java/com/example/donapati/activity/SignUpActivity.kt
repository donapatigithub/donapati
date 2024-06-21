package com.example.donapati.activity

import android.Manifest
import android.accounts.AccountManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.donapati.databinding.ActivitySignUpBinding
import com.example.donapati.db.GoogleSheetCredentialsAPI
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val GET_ACCOUNTS_PERMISSION_REQUEST = 1
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
                val Username = usernameEditText.text.toString()
                val Email = emailEditText.text.toString()
                val Phone = phoneEditText.text.toString()
                val Password = passwordEditText.text.toString()
                val ConfirmPassword = cnfPasswordEditText.text.toString()

                if (Username.isEmpty() || Email.isEmpty() || Phone.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()) {
                    Toast.makeText(this@SignUpActivity, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                } else if (!isValidPassword(Password)) {
                    binding.passwordRequirements.visibility = View.VISIBLE
                } else if (Password != ConfirmPassword) {
                    Toast.makeText(this@SignUpActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val googleSheetApi = GoogleSheetCredentialsAPI(applicationContext)
                        val success = addUserToGoogleSheet(googleSheetApi, Username, Email, Phone, Password)
                        withContext(Dispatchers.Main) {
                            if (success) {
                                binding.passwordRequirements.visibility = View.GONE
                                Toast.makeText(this@SignUpActivity, "Sign up successful", Toast.LENGTH_SHORT).show()
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@SignUpActivity, "Sign up failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
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

            if (ContextCompat.checkSelfPermission(this@SignUpActivity, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@SignUpActivity, arrayOf(Manifest.permission.GET_ACCOUNTS), GET_ACCOUNTS_PERMISSION_REQUEST)
            } else {
                getGoogleAccounts()
            }
        }
    }

    private suspend fun addUserToGoogleSheet(googleSheetApi: GoogleSheetCredentialsAPI, Username: String, Email: String, Phone: String, Password: String): Boolean {
        return try {
            val values = listOf(listOf(Username, Email, Phone, Password))
            val body = ValueRange().setValues(values)
            googleSheetApi.getSheetsService().spreadsheets().values()
                .append("19zGEh_ZxbXnA0A1DCoJ2iF4s_IevHszVAbUqbHn2Mz0", "LoginCredentials", body)
                .setValueInputOption("RAW")
                .execute()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error adding user to Google Sheet", e)
            false
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$"
        val passwordMatcher = Regex(passwordPattern)
        return passwordMatcher.matches(password)
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
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            } catch (e: Exception) {
                Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GET_ACCOUNTS_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGoogleAccounts()
            } else {
                Toast.makeText(this, "Permission denied to access accounts", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
