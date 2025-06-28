package com.nipa.healthcaremobile.view.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nipa.healthcaremobile.R
import com.nipa.healthcaremobile.persistence.AppDatabase
import com.nipa.healthcaremobile.repository.auth.AuthRepository
import com.nipa.healthcaremobile.viewmodel.auth.AuthViewModel
import com.nipa.healthcaremobile.view.auth.AuthViewModelFactory

class RegisterFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var goToLoginTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("RegisterFragment", "onViewCreated called")

        setupViewModel()
        initViews(view)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupViewModel() {
        // --- Temporary Dependency Instantiation ---
        val firebaseAuthInstance = FirebaseAuth.getInstance()
        // Use applicationContext to avoid memory leaks
        val appDatabaseInstance = AppDatabase.getInstance(requireActivity().applicationContext)
        val authRepositoryInstance = AuthRepository(firebaseAuthInstance, appDatabaseInstance.userDao())
        val factory = AuthViewModelFactory(authRepositoryInstance)
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        // --- End Temporary Dependency Instantiation ---
    }

    private fun initViews(view: View) {
        emailEditText = view.findViewById(R.id.et_register_email)
        passwordEditText = view.findViewById(R.id.et_register_password)
        confirmPasswordEditText = view.findViewById(R.id.et_register_confirm_password)
        registerButton = view.findViewById(R.id.btn_register)
        loadingProgressBar = view.findViewById(R.id.pb_register_loading)
        errorTextView = view.findViewById(R.id.tv_register_error)
        goToLoginTextView = view.findViewById(R.id.tv_go_to_login)
    }

    private fun setupClickListeners() {
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
                if(valid) confirmPasswordEditText.requestFocus()
                valid = false
            } else if (!confirmPassword.isEmpty() && password == confirmPassword) {
                confirmPasswordEditText.error = null // Clear error if they match
            }

            if (!valid) {
                return@setOnClickListener
            }

            Log.i("RegisterFragment", "Register button clicked for email: $email, type: $userType")
            authViewModel.clearAuthError() // Clear previous errors
            errorTextView.visibility = View.GONE // Hide error view explicitly
            authViewModel.register(email, password, userType)
        }

        goToLoginTextView.setOnClickListener {
            Log.i("RegisterFragment", "Go to Login clicked")
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun observeViewModel() {
        authViewModel.registrationSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess == true) {
                Log.i("RegisterFragment", "Registration successful, navigating to LoginFragment.")
                Toast.makeText(context, "Registration Successful! Please login.", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }

        authViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            registerButton.isEnabled = !isLoading
            emailEditText.isEnabled = !isLoading
            passwordEditText.isEnabled = !isLoading
            confirmPasswordEditText.isEnabled = !isLoading
            goToLoginTextView.isEnabled = !isLoading
        }

        authViewModel.authError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Log.e("RegisterFragment", "Auth Error: $it")
                errorTextView.text = it
                errorTextView.visibility = View.VISIBLE
            } ?: run {
                errorTextView.visibility = View.GONE
            }
        }
    }
}