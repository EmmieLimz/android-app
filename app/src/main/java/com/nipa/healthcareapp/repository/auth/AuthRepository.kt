package com.nipa.healthcareapp.repository.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nipa.healthcareapp.model.auth.User
import com.nipa.healthcareapp.persistence.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// Sealed class to handle success and error results
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

    suspend fun registerUser(email: String, password: String, name: String, userType: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user
                if (firebaseUser != null) {
                    val appUser = User(
                        id = firebaseUser.uid,
                        name = name,
                        email = email,
                        userType = userType
                    )
                    userDao.insertUser(appUser)
                    Log.i("AuthRepository", "User registered: ${firebaseUser.uid}, saved to Room.")
                    AuthResult.Success(user = firebaseUser, appUser = appUser)
                } else {
                    Log.e("AuthRepository", "Firebase user is null after registration")
                    AuthResult.Error(Exception("Firebase user is null after registration"))
                }
            } catch (e: Exception) {
                Log.e("AuthRepository", "Error registering user", e)
                return@withContext AuthResult.Error(e)
            }
        }
    }

    suspend fun loginUser(email: String, password: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user
                if (firebaseUser != null) {
                    Log.i("AuthRepository", "User logged in: ${firebaseUser.uid}")
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

    suspend fun getCurrentUser(): User? {
        return withContext(Dispatchers.IO) {
            val firebaseUser = firebaseAuth.currentUser
            firebaseUser?.let {
                userDao.getUserById(it.uid)
            }
        }
    }

    fun getCurrentFirebaseUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    suspend fun logoutUser() {
        firebaseAuth.signOut()
        Log.i("AuthRepository", "User logged out")
        // Optionally clear cached user data if needed
    }
}
