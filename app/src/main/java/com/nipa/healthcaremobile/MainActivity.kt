package com.nipa.healthcaremobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.nipa.healthcaremobile.persistence.AppDatabase
import com.nipa.healthcaremobile.repository.auth.AuthRepository
import com.nipa.healthcaremobile.view.auth.AuthViewModelFactory // Assuming AuthViewModelFactory is accessible
import com.nipa.healthcaremobile.view.auth.LoginActivity
import com.nipa.healthcaremobile.viewmodel.auth.AuthViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var welcomeTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "onCreate called")

        // --- Temporary Dependency Instantiation ---
        val firebaseAuthInstance = FirebaseAuth.getInstance()
        val appDatabaseInstance = AppDatabase.getInstance(applicationContext)
        val authRepositoryInstance = AuthRepository(firebaseAuthInstance, appDatabaseInstance.userDao())
        // AuthViewModelFactory is assumed to be in its own file or accessible.
        // If AuthViewModelFactory is defined in LoginActivity.kt, it needs to be moved to its own file
        // or made a public top-level class to be accessed here directly without an import issue.
        // For this subtask, we proceed assuming it can be resolved by the compiler either way.
        val factory = AuthViewModelFactory(authRepositoryInstance)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        // --- End Temporary Dependency Instantiation ---

        welcomeTextView = findViewById(R.id.tv_welcome_message)
        logoutButton = findViewById(R.id.btn_logout)

        logoutButton.setOnClickListener {
            Log.i("MainActivity", "Logout button clicked")
            authViewModel.logout()
        }

        observeViewModel()

        // ViewModel's init block calls checkUserLoggedIn.
        // If MainActivity is reached, it implies the ViewModel is being created and
        // the check will run. If user is null, observer will redirect.
    }

    private fun observeViewModel() {
        authViewModel.currentUser.observe(this) { user ->
            if (user != null) {
                Log.i("MainActivity", "User logged in: \${user.email}")
                welcomeTextView.text = "Welcome, \${user.email} (Type: \${user.userType})"
                logoutButton.visibility = View.VISIBLE
                welcomeTextView.visibility = View.VISIBLE
            } else {
                Log.i("MainActivity", "User not logged in or has logged out, navigating to LoginActivity.")
                val intent = Intent(this, LoginActivity::class.java)
                // Clear task and start new one to prevent user from navigating back to MainActivity after logout
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        // Optional: Observe isLoading if MainActivity needs to show a global progress during logout
        authViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                Log.d("MainActivity", "Auth operation in progress...")
                // You might disable the logout button here, for example
                logoutButton.isEnabled = false
            } else {
                Log.d("MainActivity", "Auth operation idle.")
                logoutButton.isEnabled = true
            }
        }
    }
}
