package com.nipa.healthcaremobile.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.nipa.healthcaremobile.R
import com.nipa.healthcaremobile.persistence.AppDatabase
import com.nipa.healthcaremobile.repository.auth.AuthRepository
import com.nipa.healthcaremobile.viewmodel.auth.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var goToLoginTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Log.d("RegisterActivity", "onCreate called")

        // --- Temporary Dependency Instantiation (Same as LoginActivity) ---
        val firebaseAuthInstance = FirebaseAuth.getInstance()
        val appDatabaseInstance = AppDatabase.getInstance(applicationContext)
        val authRepositoryInstance = AuthRepository(firebaseAuthInstance, appDatabaseInstance.userDao())
        // AuthViewModelFactory is defined in LoginActivity.kt.
        // For this to compile if they are in different files without further setup,
        // AuthViewModelFactory would need to be in its own file and public.
        // Assuming it's accessible for the purpose of this subtask.
        val factory = AuthViewModelFactory(authRepositoryInstance)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        // --- End Temporary Dependency Instantiation ---

        emailEditText = findViewById(R.id.et_register_email)
        passwordEditText = findViewById(R.id.et_register_password)
        confirmPasswordEditText = findViewById(R.id.et_register_confirm_password)
        registerButton = findViewById(R.id.btn_register)
        loadingProgressBar = findViewById(R.id.pb_register_loading)
        errorTextView = findViewById(R.id.tv_register_error)
        goToLoginTextView = findViewById(R.id.tv_go_to_login)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()
            val userType = "Client" // Hardcoded for now. UI for this selection to be added later.

            var valid = true
            if (email.isEmpty()) {
                emailEditText.error = "Email cannot be empty"
                if(valid) emailEditText.requestFocus()
                valid = false
            } else emailEditText.error = null

            if (password.isEmpty()) {
                passwordEditText.error = "Password cannot be empty"
                if(valid) passwordEditText.requestFocus()
                valid = false
            } else passwordEditText.error = null

            if (confirmPassword.isEmpty()) {
                confirmPasswordEditText.error = "Confirm password cannot be empty"
                if(valid) confirmPasswordEditText.requestFocus()
                valid = false
            } else confirmPasswordEditText.error = null

            if (password != confirmPassword && !password.isEmpty() && !confirmPassword.isEmpty()) {
                confirmPasswordEditText.error = "Passwords do not match"
                // Potentially clear the confirmPassword field or both password fields
                if(valid) confirmPasswordEditText.requestFocus()
                valid = false
            } else if (!confirmPassword.isEmpty() && password == confirmPassword) {
                 confirmPasswordEditText.error = null // Clear error if they match
            }

            if (!valid) {
                return@setOnClickListener
            }

            Log.i("RegisterActivity", "Register button clicked for email: \$email, type: \$userType")
            authViewModel.clearAuthError() // Clear previous errors
            errorTextView.visibility = View.GONE // Hide error view explicitly
            authViewModel.register(email, password, userType)
        }

        goToLoginTextView.setOnClickListener {
            Log.i("RegisterActivity", "Go to Login clicked")
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        authViewModel.registrationSuccess.observe(this) { isSuccess ->
            if (isSuccess == true) {
                Log.i("RegisterActivity", "Registration successful, navigating to LoginActivity.")
                Toast.makeText(this, "Registration Successful! Please login.", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        }

        authViewModel.isLoading.observe(this) { isLoading ->
            loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            registerButton.isEnabled = !isLoading
            emailEditText.isEnabled = !isLoading
            passwordEditText.isEnabled = !isLoading
            confirmPasswordEditText.isEnabled = !isLoading
            goToLoginTextView.isEnabled = !isLoading
        }

        authViewModel.authError.observe(this) { errorMessage ->
            errorMessage?.let {
                Log.e("RegisterActivity", "Auth Error: \$it")
                errorTextView.text = it
                errorTextView.visibility = View.VISIBLE
            } ?: run {
                errorTextView.visibility = View.GONE
            }
        }
    }
}
