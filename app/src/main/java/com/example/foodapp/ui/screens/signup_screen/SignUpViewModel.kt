package com.example.foodapp.ui.screens.signup_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.ResourceState
import com.example.foodapp.firebaseAuth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel(){

    val _signUpState = Channel<SignUpState>()
    val signUpState = _signUpState.receiveAsFlow()

    fun signUpUser(email: String, password: String) = viewModelScope.launch {

        repository.registerUser(email, password).collect { result ->

            when(result){
                is ResourceState.Success -> {
                    _signUpState.send(SignUpState(isSuccess = "Sign in Success"))
                }
                is ResourceState.Loading -> {
                    _signUpState.send(SignUpState(isLoading = true))
                }
                is ResourceState.Error -> {
                    _signUpState.send(SignUpState(isError = result.error))
                }

            }
        }
    }

}