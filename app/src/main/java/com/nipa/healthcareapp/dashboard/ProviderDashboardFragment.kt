package com.nipa.healthcareapp.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nipa.healthcaremobile.R // Ensure R is imported
import com.nipa.healthcaremobile.databinding.FragmentProviderDashboardBinding

class ProviderDashboardFragment : Fragment() {

    private var _binding: FragmentProviderDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProviderDashboardBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogoutProvider.setOnClickListener {
            auth.signOut()
            // Ensure fragment is still added before navigating
            if (isAdded) {
                findNavController().navigate(R.id.action_provider_logout)
            }
        }

        // TODO: Implement other button click listeners (set availability, etc.)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
