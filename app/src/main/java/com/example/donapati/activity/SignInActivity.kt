package com.example.donapati.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.donapati.activity.dashboardactivity.DashboardActivity
import com.example.donapati.databinding.ActivitySignInBinding
import com.example.donapati.db.GoogleSheetCredentialsAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val TAG = "SignInActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
    }

    private fun onClick() {
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
                CoroutineScope(Dispatchers.IO).launch {
                    val googleSheetApi = GoogleSheetCredentialsAPI(applicationContext)
                    val isValid = validateUser(googleSheetApi, userName, password)
                    withContext(Dispatchers.Main) {
                        if (isValid) {
                            Toast.makeText(this@SignInActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignInActivity, DashboardActivity::class.java))
                        } else {
                            Toast.makeText(this@SignInActivity, "Login Failed/User Not Found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        binding.forgotPasswordTextView.setOnClickListener {
            startActivity(Intent(this@SignInActivity, ForgotPasswordActivity::class.java))
        }
    }

    private suspend fun validateUser(googleSheetApi: GoogleSheetCredentialsAPI, userName: String, password: String): Boolean {
        return try {
            val response = googleSheetApi.getSheetsService().spreadsheets().values()
                .get("19zGEh_ZxbXnA0A1DCoJ2iF4s_IevHszVAbUqbHn2Mz0", "LoginCredentials")
                .execute()
            val values = response.getValues()
            if (values != null) {
                for (row in values) {
                    if (row.size >= 4 && row[0] == userName && row[3] == password) {
                        return true
                    }
                }
            }
            false
        } catch (e: Exception) {
            Log.e(TAG, "Error reading Google Sheet", e)
            false
        }
    }
}