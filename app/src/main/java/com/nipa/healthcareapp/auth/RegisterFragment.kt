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
import com.nipa.healthcaremobile.R
import com.nipa.healthcaremobile.databinding.FragmentRegisterBinding
// User model is now used via AuthViewModel
import com.nipa.healthcareapp.persistence.AppDatabase // For AuthRepository
import com.nipa.healthcareapp.repository.auth.AuthRepository // For AuthViewModelFactory
import com.nipa.healthcareapp.viewmodel.auth.AuthViewModel
import com.nipa.healthcareapp.viewmodel.auth.AuthViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Simplified Dependency Instantiation for ViewModel
        val application = requireActivity().application
        val authRepository = AuthRepository(
            FirebaseAuth.getInstance(),
            AppDatabase.getInstance(application).userDao()
        )
        val factory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        binding.buttonRegister.setOnClickListener {
            handleRegistration()
        }

        binding.textViewGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        observeViewModel()
    }

    private fun handleRegistration() {
        val name = binding.editTextName.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        val selectedUserTypeId = binding.radioGroupUserType.checkedRadioButtonId
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedUserTypeId == -1) {
            Toast.makeText(context, "Please select user type", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }
        val userType = if (selectedUserTypeId == R.id.radioButtonClient) "client" else "provider"

        authViewModel.clearAuthError() // Clear previous errors
        authViewModel.register(email, password, name, userType)
    }

    private fun observeViewModel() {
        authViewModel.registrationSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, "Registration successful! Please login.", Toast.LENGTH_LONG).show()
                if(isAdded) findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                authViewModel.onRegistrationNavigationComplete() // Reset the event
            }
        }
        authViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonRegister.isEnabled = !isLoading
            // Disable other fields as well if needed
        }
        authViewModel.authError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, "Registration failed: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
