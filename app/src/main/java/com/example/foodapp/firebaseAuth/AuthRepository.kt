package com.example.foodapp.firebaseAuth

import com.example.foodapp.data.ResourceState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun loginUser(email: String, password: String): Flow<ResourceState<AuthResult>>
    fun registerUser(email: String, password: String): Flow<ResourceState<AuthResult>>

    fun googleSignIn(credential: AuthCredential): Flow<ResourceState<AuthResult>>




}