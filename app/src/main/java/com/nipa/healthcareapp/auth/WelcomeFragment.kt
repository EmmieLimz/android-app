package com.nipa.healthcareapp.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nipa.healthcaremobile.R // Import R class
import com.nipa.healthcaremobile.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assuming buttonProvider is for "Register" and buttonClient is for "Login"
        // based on current fragment_welcome.xml and auth_nav_graph.xml
        // Consider renaming buttons in XML later for clarity (e.g., buttonGoToRegister, buttonGoToLogin)

        binding.buttonProvider.setOnClickListener { // "I'm a Provider" -> Register
            findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }

        binding.buttonClient.setOnClickListener { // "I'm a Client" -> Login
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
