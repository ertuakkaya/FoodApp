package com.example.foodapp.ui.screens.login_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.ResourceState
import com.example.foodapp.firebaseAuth.AuthRepository

import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel(){

    val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    val _googleState = mutableStateOf(GoogleSignInState())
    val googleState: State<GoogleSignInState> = _googleState

    fun googleSignIn(credential: AuthCredential) = viewModelScope.launch {
        repository.googleSignIn(credential).collect { result ->
            when (result) {
                is ResourceState.Success -> {
                    _googleState.value = GoogleSignInState(success = result.data)
                }
                is ResourceState.Loading -> {
                    _googleState.value = GoogleSignInState(loading = true)
                }
                is ResourceState.Error -> {
                    _googleState.value = GoogleSignInState(error = result.error!!)
                }
            }


        }
    }


    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email, password).collect { result ->
            when (result) {
                is ResourceState.Success -> {
                    _signInState.send(SignInState(isSuccess = "Sign In Success "))
                }
                is ResourceState.Loading -> {
                    _signInState.send(SignInState(isLoading = true))
                }
                is ResourceState.Error -> {

                    _signInState.send(SignInState(isError = result.error!!))
                }
            }

        }
    }

}