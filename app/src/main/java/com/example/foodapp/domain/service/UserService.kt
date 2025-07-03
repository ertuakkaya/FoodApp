package com.example.foodapp.domain.service

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor() {
    
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    fun getCurrentUserName(): String {
        return auth.currentUser?.email ?: "guest"
    }
    
    fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: ""
    }
    
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
    
    fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
    }
}