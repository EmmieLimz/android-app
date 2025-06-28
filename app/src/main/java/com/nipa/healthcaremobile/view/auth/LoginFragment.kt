package com.nipa.healthcaremobile.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nipa.healthcaremobile.R
import com.nipa.healthcaremobile.persistence.AppDatabase
import com.nipa.healthcaremobile.repository.auth.AuthRepository
import com.nipa.healthcaremobile.viewmodel.auth.AuthViewModel

class LoginFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var goToRegisterTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        initViews(view)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupViewModel() {
        // Temporary Dependency Instantiation
        val firebaseAuthInstance = FirebaseAuth.getInstance()
        val appDatabaseInstance = AppDatabase.getInstance(requireContext())
        val authRepositoryInstance = AuthRepository(firebaseAuthInstance, appDatabaseInstance.userDao())
        // You'll need to make AuthViewModelFactory accessible or create it here
        // val factory = AuthViewModelFactory(authRepositoryInstance)
        // authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
    }

    private fun initViews(view: View) {
        emailEditText = view.findViewById(R.id.et_login_email)
        passwordEditText = view.findViewById(R.id.et_login_password)
        loginButton = view.findViewById(R.id.btn_login)
        loadingProgressBar = view.findViewById(R.id.pb_login_loading)
        errorTextView = view.findViewById(R.id.tv_login_error)
        goToRegisterTextView = view.findViewById(R.id.tv_go_to_register)
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Add your validation logic here
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // authViewModel.login(email, password)
            }
        }

        goToRegisterTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun observeViewModel() {
        // Add your ViewModel observers here
    }
}