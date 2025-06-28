package com.nipa.healthcaremobile.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nipa.healthcaremobile.R

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // User type selection buttons
        val providerButton = view.findViewById<Button>(R.id.buttonProvider)
        val clientButton = view.findViewById<Button>(R.id.buttonClient)

        // Navigation buttons
        val loginButton = view.findViewById<Button>(R.id.btn_go_to_login)
        val registerButton = view.findViewById<Button>(R.id.btn_go_to_register)

        // Handle user type selection
        providerButton.setOnClickListener {
            // Store user type preference or navigate to provider-specific flow
            Toast.makeText(context, "Provider selected", Toast.LENGTH_SHORT).show()
            // You can store this selection in SharedPreferences or pass as argument
        }

        clientButton.setOnClickListener {
            // Store user type preference or navigate to client-specific flow
            Toast.makeText(context, "Client selected", Toast.LENGTH_SHORT).show()
            // You can store this selection in SharedPreferences or pass as argument
        }

        // Handle navigation to login/register
        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }

        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }
    }
}