package com.example.foodapp.ui.screens

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodapp.ui.viewmodel.AuthState
import com.example.foodapp.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(authViewModel: AuthViewModel,navController: NavController) {


    var email by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    fun chechPassword(): Boolean {
        if (newPassword == confirmPassword ) {
            return true
        }
        return false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Top App Bar
        TopAppBar(
            title = {
                Text(text = "Account Settings", fontSize = 20.sp)
            },
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth(),
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.navigate("home")
                    }
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")

                }
            },
        )

        // Account Avatar Icon
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = ""
            , modifier = Modifier
                .size(250.dp)
                .padding(bottom = 16.dp)
        )


        Text(text = "Please write yout mail adress to change your password.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)

        )


        // Send Password Reset Email Button
        Button(
            onClick = {
                val email = authViewModel.getUserName()
                authViewModel.sendPasswordResetEmail(email)
            },
            enabled = if ( email == authViewModel.getUserEmail() ) true else false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 16.dp),

            ){
            Text(
                text= "Send Password Reset Email",
                modifier = Modifier


            )
        }



        Button(
            onClick = {
                authViewModel.signOut()
                navController.navigate("login")
            },
            enabled = if ( authViewModel.authState.value == AuthState.Authenticated ) true else false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 16.dp),

        ){
            Text(
                text= "Log Out",
                modifier = Modifier


            )
        }

    }









}



/*
@Composable
@Preview(showSystemUi = true)
@PreviewParameter(AuthViewModel::class)
fun AccountScreenPreview( authViewModel: AuthViewModel) {
    AccountScreen(authViewModel )
}

 */