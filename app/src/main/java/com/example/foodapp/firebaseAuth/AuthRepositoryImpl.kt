package com.example.foodapp.firebaseAuth

import com.example.foodapp.data.ResourceState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth): AuthRepository {
    override fun loginUser(email: String, password: String): Flow<ResourceState<AuthResult>> {
        return flow {
            emit(ResourceState.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(ResourceState.Success(result))
        }.catch {
            emit(ResourceState.Error(it.message.toString()))
        }

    }

    override fun registerUser(email: String, password: String): Flow<ResourceState<AuthResult>> {
        return flow {
            emit(ResourceState.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(ResourceState.Success(result))
        }.catch {
            emit(ResourceState.Error(it.message.toString()))
        }
    }

    override fun googleSignIn(credential: AuthCredential): Flow<ResourceState<AuthResult>> {
        return flow {
            emit(ResourceState.Loading())
            val result = firebaseAuth.signInWithCredential(credential).await()
            emit(ResourceState.Success(result))
        }.catch {
            emit(ResourceState.Error(it.message.toString()))
        }
    }
}