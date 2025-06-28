package com.nipa.healthcaremobile.repository.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nipa.healthcaremobile.model.auth.User
import com.nipa.healthcaremobile.persistence.UserDao // Assuming UserDao is in this path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await // For Firebase tasks with coroutines
import kotlinx.coroutines.withContext

// Result sealed class for better error handling (optional for this step, can return User? or Boolean)
sealed class AuthResult {
    data class Success(val user: FirebaseUser? = null, val appUser: User? = null) : AuthResult()
    data class Error(val exception: Exception) : AuthResult()
}

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val userDao: UserDao
) {
    init {
        Log.d("AuthRepository", "AuthRepository initialized with FirebaseAuth and UserDao")
    }

    suspend fun registerUser(email: String, password: String, userType: String): AuthResult {
        return withContext(Dispatchers.IO) { // Perform network/db operations on IO dispatcher
            try {
                val authResultTask = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResultTask.user
                if (firebaseUser != null) {
                    val appUser = User(id = firebaseUser.uid, email = email, userType = userType)
                    userDao.insertUser(appUser)
                    Log.i("AuthRepository", "User registered: \${firebaseUser.uid}, saved to Room.")
                    AuthResult.Success(user = firebaseUser, appUser = appUser)
                } else {
                    Log.e("AuthRepository", "Firebase user is null after registration")
                    AuthResult.Error(Exception("Firebase user is null after registration"))
                }
            } catch (e: Exception) {
                Log.e("AuthRepository", "Error registering user", e)
                AuthResult.Error(e)
            }
        }
    }

    suspend fun loginUser(email: String, password: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val authResultTask = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResultTask.user
                if (firebaseUser != null) {
                    // Optionally fetch full appUser from Room here if needed immediately after login
                    // val appUser = userDao.getUserById(firebaseUser.uid)
                    Log.i("AuthRepository", "User logged in: \${firebaseUser.uid}")
                    AuthResult.Success(user = firebaseUser)
                } else {
                    Log.e("AuthRepository", "Firebase user is null after login")
                    AuthResult.Error(Exception("Firebase user is null after login"))
                }
            } catch (e: Exception) {
                Log.e("AuthRepository", "Error logging in user", e)
                AuthResult.Error(e)
            }
        }
    }

    suspend fun getCurrentUser(): User? { // Returns our app specific User object
        return withContext(Dispatchers.IO) {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                userDao.getUserById(firebaseUser.uid)
            } else {
                null
            }
        }
    }

    // Simple getter for current FirebaseUser if needed by ViewModel directly for some reason
    fun getCurrentFirebaseUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    suspend fun logoutUser() {
        // No need for withContext typically for signOut as it's a lightweight operation
        // but can be wrapped if there's any doubt or related cleanup.
        firebaseAuth.signOut()
        Log.i("AuthRepository", "User logged out")
        // Optionally: Clear any other local user-specific cache if necessary
    }
}
