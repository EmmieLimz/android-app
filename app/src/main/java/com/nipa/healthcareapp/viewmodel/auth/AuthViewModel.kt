package com.nipa.healthcareapp.viewmodel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nipa.healthcareapp.model.auth.User
import com.nipa.healthcareapp.repository.auth.AuthRepository
import com.nipa.healthcareapp.repository.auth.AuthResult // Import the sealed class
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _authError = MutableLiveData<String?>()
    val authError: LiveData<String?> = _authError

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    // Call this method from the Fragment after navigation has occurred
    fun onRegistrationNavigationComplete() {
        _registrationSuccess.value = false // Or null
    }

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    // Call this method from the Fragment after navigation has occurred
    fun onLoginNavigationComplete() {
        _loginSuccess.value = false // Or null, depending on how you want to handle initial state
    }

    init {
        Log.d("AuthViewModel", "AuthViewModel initialized with AuthRepository")
        checkUserLoggedIn() // Check login status on init
    }

    fun checkUserLoggedIn() {
        viewModelScope.launch {
            // _isLoading.value = true // Decided against for initial check to prevent quick flicker
            val user = authRepository.getCurrentUser()
            _currentUser.value = user
            // _isLoading.value = false // Ensure loading is false after check. isLoading is primarily for active operations.
            Log.i("AuthViewModel", "Checked user login status. User: \${user?.email}")
        }
    }

    fun register(email: String, pass: String, name: String, userType: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            _registrationSuccess.value = false

            val result = authRepository.registerUser(email, pass, name, userType)
            when (result) {
                is AuthResult.Success -> {
                    Log.i("AuthViewModel", "Registration successful for \${result.appUser?.email}")
                    _registrationSuccess.value = true
                }
                is AuthResult.Error -> {
                    Log.w("AuthViewModel", "Registration failed: \${result.exception.message}")
                    _authError.value = result.exception.message ?: "Registration failed"
                }
            }
            _isLoading.value = false
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            _loginSuccess.value = false

            val result = authRepository.loginUser(email, pass)
            when (result) {
                is AuthResult.Success -> {
                    Log.i("AuthViewModel", "Login successful for \${result.firebaseUser?.email}")
                    // After successful Firebase login, fetch the app-specific User object from Room
                    val appUser = authRepository.getCurrentUser()
                    _currentUser.value = appUser // Update LiveData with the User from Room
                    _loginSuccess.value = true
                }
                is AuthResult.Error -> {
                    Log.w("AuthViewModel", "Login failed: \${result.exception.message}")
                    _authError.value = result.exception.message ?: "Login failed"
                }
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            //isLoading should be true during logout process as well
            _isLoading.value = true
            authRepository.logoutUser()
            _currentUser.value = null
            _loginSuccess.value = false
            _isLoading.value = false
            Log.i("AuthViewModel", "User logged out")
        }
    }

    fun clearAuthError() {
        _authError.value = null
    }
}
