package com.nipa.healthcareapp.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth // Still needed for AuthRepository instantiation
import com.nipa.healthcaremobile.R  // Fixed import!
import com.nipa.healthcaremobile.databinding.FragmentLoginBinding  // Also fix this import!
import com.nipa.healthcareapp.persistence.AppDatabase // For AuthRepository
import com.nipa.healthcareapp.repository.auth.AuthRepository // For AuthViewModelFactory
import com.nipa.healthcareapp.viewmodel.auth.AuthViewModel
import com.nipa.healthcareapp.viewmodel.auth.AuthViewModelFactory
// User model is now observed via AuthViewModel.currentUser

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireActivity().application
        val authRepository = AuthRepository(
            FirebaseAuth.getInstance(),
            AppDatabase.getInstance(application).userDao()
        )
        val factory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        binding.buttonLogin.setOnClickListener {
            handleLogin()
        }

        binding.textViewGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        observeViewModel()
    }

    private fun handleLogin() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        authViewModel.clearAuthError() // Clear previous errors
        authViewModel.login(email, password)
    }

    private fun observeViewModel() {
        authViewModel.loginSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val user = authViewModel.currentUser.value
                user?.userType?.let { userType ->
                    Log.d("LoginFragment", "Login successful, currentUser available. UserType: $userType. Navigating.")
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    if (isAdded) {
                        when (userType) {
                            "client" -> findNavController().navigate(R.id.action_global_to_client_nav_graph)
                            "provider" -> findNavController().navigate(R.id.action_global_to_provider_nav_graph)
                            else -> {
                                Log.e("LoginFragment", "Unknown user type: $userType")
                                Toast.makeText(context, "Login failed: Unknown user type.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    authViewModel.onLoginNavigationComplete() // Reset the event
                } ?: run {
                    // This case should ideally not happen if loginSuccess is true because AuthViewModel sets currentUser first.
                    // But as a safeguard:
                    if (isSuccess) { // Only show error if loginSuccess was true but user is null
                        Log.e("LoginFragment", "Login reported success, but currentUser is null.")
                        Toast.makeText(context, "Login error: User data not available.", Toast.LENGTH_SHORT).show()
                        authViewModel.onLoginNavigationComplete() // Reset event anyway
                    }
                }
            }
        }
        authViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonLogin.isEnabled = !isLoading
        }
        authViewModel.authError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, "Login failed: $it", Toast.LENGTH_LONG).show()
            }
        }
        // Observer for currentUser to handle navigation will be added in the next step.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}