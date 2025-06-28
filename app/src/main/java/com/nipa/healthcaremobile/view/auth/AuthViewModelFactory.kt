package com.nipa.healthcaremobile.view.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nipa.healthcaremobile.repository.auth.AuthRepository
import com.nipa.healthcaremobile.viewmodel.auth.AuthViewModel

/**
 * ViewModelFactory for AuthViewModel
 * Creates AuthViewModel instances with required dependencies
 */
class AuthViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class for AuthViewModelFactory: ${modelClass.name}")
    }
}