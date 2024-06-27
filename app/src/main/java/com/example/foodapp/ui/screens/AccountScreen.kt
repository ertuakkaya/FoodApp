package com.example.foodapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen() {


    Scaffold(
        topBar = {
            TopAppBar(

                modifier = Modifier
                    .background(Brush.horizontalGradient(
                        colors = listOf(
                            Color.LightGray,
                            Color.Black
                        )
                    )),
                title = {
                    Text(
                        text = "Account",
                        textAlign = TextAlign.Center,
                        modifier = Modifier,
                        fontWeight = FontWeight.Medium


                    )


                },
                windowInsets = TopAppBarDefaults.windowInsets,
                navigationIcon = {
                    IconToggleButton(checked = true,
                        onCheckedChange = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                        
                    }
                },

            )

            Spacer(modifier = Modifier.height(40.dp))

        },// Top App Bar

    ) { innerPadding ->



        // Account Card, delete account , change password , logout button etc.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            // Account info Card
            Card(
                modifier = Modifier
                    .size(400.dp, 200.dp)
                    .padding(8.dp),
                border = BorderStroke(2.dp, Color.Gray)


            ){
                // avatar , name , email , phone number etc.
                Row (
                    modifier = Modifier
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,



                    //.background(Color.Black)


                ){
                    Column (
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)


                        )
                        Text(text ="Rana")
                    }


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text ="Rana Akkaya")
                        Text(text = "rana@gmail.com")
                    }
                }


            }

            Spacer(modifier = Modifier.height(32.dp))

            // Delete Account Button
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                ,
                content = {
                    Text(text = "Delete Account")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Change Password Button
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                ,
                content = {
                    Text(text = "Change Password")
                }
            )

            Spacer(modifier = Modifier.height(300.dp))

            // Logout Button
            ElevatedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.LightGray,
                                Color.Black
                            )
                        )
                    )
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),



                ){
                Text(text = "Logout")
            }




        }// Account Card, delete account , change password , logout button etc.


    }// Scaffold




}




@Composable
@Preview(showSystemUi = true)
fun AccountScreenPreview() {
    AccountScreen()
}