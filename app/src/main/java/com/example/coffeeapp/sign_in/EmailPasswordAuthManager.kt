package com.example.coffeeapp.sign_in

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class EmailPasswordAuthManager(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) {

    suspend fun registerUser(email: String, password: String, username: String): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("UID is null")
            Log.d("EmailPasswordAuthManager", "User registered with UID: $uid")

            try {
                val userMap = mapOf(
                    "uid" to uid,
                    "email" to email,
                    "username" to username
                )
                database.reference.child("users").child(uid).setValue(userMap).await()
                Log.d("EmailPasswordAuthManager", "User data saved to database")
            } catch (e: Exception) {
                Log.e("EmailPasswordAuthManager", "Failed to save user data", e)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("EmailPasswordAuthManager", "Registration failed", e)
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Log.d("EmailPasswordAuthManager", "User logged in successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("EmailPasswordAuthManager", "Login failed", e)
            Result.failure(e)
        }
    }
}