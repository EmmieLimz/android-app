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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.nipa.healthcaremobile.MainActivity
import com.nipa.healthcaremobile.R
import com.nipa.healthcaremobile.persistence.AppDatabase
import com.nipa.healthcaremobile.repository.auth.AuthRepository
import com.nipa.healthcaremobile.viewmodel.auth.AuthViewModel

// Simple ViewModelFactory for AuthViewModel (as AuthViewModel now has dependencies)
class AuthViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class for AuthViewModelFactory")
    }
}

class LoginActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var goToRegisterTextView: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d("LoginActivity", "onCreate called")

        // --- Temporary Dependency Instantiation ---
        // This is a simplified way to get dependencies. Proper DI (Hilt/Koin) would be better.
        val firebaseAuthInstance = FirebaseAuth.getInstance()
        // Ensure applicationContext is used for AppDatabase to avoid leaks if Activity context is used.
        val appDatabaseInstance = AppDatabase.getInstance(applicationContext)
        val authRepositoryInstance = AuthRepository(firebaseAuthInstance, appDatabaseInstance.userDao())
        val factory = AuthViewModelFactory(authRepositoryInstance)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        // --- End Temporary Dependency Instantiation ---

        emailEditText = findViewById(R.id.et_login_email)
        passwordEditText = findViewById(R.id.et_login_password)
        loginButton = findViewById(R.id.btn_login)
        goToRegisterTextView = findViewById(R.id.tv_go_to_register)
        loadingProgressBar = findViewById(R.id.pb_login_loading)
        errorTextView = findViewById(R.id.tv_login_error)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            var valid = true
            if (email.isEmpty()) {
                emailEditText.error = "Email cannot be empty"
                if (valid) emailEditText.requestFocus() // Request focus on the first error field
                valid = false
            } else {
                emailEditText.error = null // Clear error if not empty
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password cannot be empty"
                if (valid) passwordEditText.requestFocus() // Only request focus if email was valid
                valid = false
            } else {
                passwordEditText.error = null // Clear error if not empty
            }

            if (!valid) {
                return@setOnClickListener
            }

            Log.i("LoginActivity", "Login button clicked with email: \$email")
            authViewModel.clearAuthError() // Clear previous errors
            errorTextView.visibility = View.GONE // Hide error view explicitly
            authViewModel.login(email, password)
        }

        goToRegisterTextView.setOnClickListener {
            Log.i("LoginActivity", "Go to Register clicked")
            // Disable button during navigation to prevent multiple clicks
            it.isEnabled = false
            startActivity(Intent(this, RegisterActivity::class.java))
            // Re-enable after a short delay or in onResume, for simplicity now, just re-enable.
            // However, this activity might be finishing or pausing, so direct re-enable might not be seen.
            // it.isEnabled = true // This might not be effective if activity is paused immediately.
        }

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        // Re-enable the register link if it was disabled,
        // in case user navigates back to this screen.
        goToRegisterTextView.isEnabled = true
    }

    private fun observeViewModel() {
        authViewModel.loginSuccess.observe(this) { isSuccess ->
            // Only react if isSuccess is explicitly true, to avoid issues with initial LiveData values
            if (isSuccess == true) {
                Log.i("LoginActivity", "Login successful, navigating to MainActivity.")
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        authViewModel.isLoading.observe(this) { isLoading ->
            loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            loginButton.isEnabled = !isLoading
            emailEditText.isEnabled = !isLoading
            passwordEditText.isEnabled = !isLoading
            // Only disable register text view if loading, not based on other states
            goToRegisterTextView.isEnabled = !isLoading
        }

        authViewModel.authError.observe(this) { errorMessage ->
            errorMessage?.let {
                Log.e("LoginActivity", "Auth Error: \$it")
                errorTextView.text = it
                errorTextView.visibility = View.VISIBLE
                // Toast.makeText(this, it, Toast.LENGTH_LONG).show() // Alternative error display
            } ?: run {
                errorTextView.visibility = View.GONE
            }
        }
    }
}
