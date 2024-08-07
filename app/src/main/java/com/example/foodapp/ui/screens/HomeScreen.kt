package com.example.foodapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodapp.R
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entitiy.Yemekler
import com.example.foodapp.ui.viewmodel.AuthState
import com.example.foodapp.ui.viewmodel.AuthViewModel
import com.example.foodapp.ui.viewmodel.YemeklerViewModel

/*
@Composable
fun HomeScreen(yemeklerViewModel: YemeklerViewModel = hiltViewModel() , navController: NavController) {

    val yemeklerResponse by yemeklerViewModel.yemekler.collectAsState()


    when(yemeklerResponse){
        is ResourceState.Loading -> {
            Log.d("HOMESCREEN", "HomeScreen: Loading...")
        }
        is ResourceState.Success -> {
            val response = (yemeklerResponse as ResourceState.Success).data
            Log.d("HOMESCREEN", "HomeScreen: SUCCESS... success = ${response.success} | yemekeler.size = ${response.yemekler.size}")

            if(response.yemekler.isNotEmpty()){

                //
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                ){
                    Text(text = "arama kısmı gelecek")


                    Spacer(modifier = Modifier.height(16.dp))

                    YemekListesi(yemekler = response.yemekler,navController = navController)
                }


            }else{
                // TODO("EmptyStateComponent() buraya yemeklerin yuklenmedigi senaryo eklenecek")
            }
        }
        is ResourceState.Error -> {
            val error = (yemeklerResponse as ResourceState.Error)
            Log.d("HOMESCREEN", "HomeScreen: Error... $error")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Sepete Git
        IconButton(
            onClick = {
                navController.navigate("sepet")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(66.dp)
                .background(Color.White, shape = RoundedCornerShape(28.dp))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.basket),
                contentDescription = "Sepet",
                modifier = Modifier.size(30.dp),
                tint = Color.Black
            )
        }
    }


}


 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    yemeklerViewModel: YemeklerViewModel = hiltViewModel(),
    navController: NavController,
    authViewModel: AuthViewModel
) {


    // Firebase
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {

        // Kullanıcı Unauthanticaded durumunda ise login ekranına yönlendir
        when(authState.value){
          is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit

        }

    }



    // HomeScreen'de kullanılacak değişkenler
    val yemeklerResponse by yemeklerViewModel.yemekler.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var filteredYemekler by remember { mutableStateOf<List<Yemekler>>(emptyList()) }


    when (yemeklerResponse) {
        is ResourceState.Loading -> {
            Log.d("HOMESCREEN", "HomeScreen: Loading...")
        }

        is ResourceState.Success -> {
            val response = (yemeklerResponse as ResourceState.Success).data
            Log.d(
                "HOMESCREEN",
                "HomeScreen: SUCCESS... success = ${response.success} | yemekeler.size = ${response.yemekler.size}"
            )

            if (response.yemekler.isNotEmpty()) {
                filteredYemekler = if (searchText.isBlank()) {
                    response.yemekler
                } else {
                    response.yemekler.filter {
                        it.yemek_adi.contains(searchText, ignoreCase = true)
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 1.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                ) {

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp)
                    ){
                        Text(
                            text = "Food",
                            fontWeight = FontWeight.Medium,
                            fontSize = 40.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Arama Kısmı
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                        },
                        label = { Text("Search") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Arama"
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Yemek Listesi
                    Box(modifier = Modifier.size(400.dp, 620.dp)) {
                        // Yemek Listesi
                        YemekListesi(yemekler = filteredYemekler, navController = navController, authViewModel = authViewModel)

                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    // BottomAppBar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            //.background(Color.LightGray)
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center


                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Sepete Git
                            IconButton(
                                onClick = {
                                    navController.navigate("home")
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()

                            ) {

                                Icon(
                                    imageVector = Icons.Filled.Home,
                                    contentDescription = "",
                                    modifier = Modifier.size(100.dp),


                                )
                            }
                            // Sepete Git
                            IconButton(
                                onClick = {
                                    navController.navigate("sepet")
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.List,
                                    contentDescription = "",
                                    modifier = Modifier.size(100.dp),


                                )
                            }

                            // Sepete Git
                            IconButton(
                                onClick = {
                                    navController.navigate("account")
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "",
                                    modifier = Modifier.size(100.dp),


                                )
                            }
                        }// BottomAppBar Row
                    }

                }// Column

            } else {
                // TODO("EmptyStateComponent() buraya yemeklerin yuklenmedigi senaryo eklenecek")
            }
        }

        is ResourceState.Error -> {
            val error = (yemeklerResponse as ResourceState.Error)
            Log.d("HOMESCREEN", "HomeScreen: Error... $error")
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YemekKart(
    yemek: Yemekler,
    yemeklerViewModel: YemeklerViewModel = hiltViewModel(),
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val response by yemeklerViewModel.sepeteYemekEkle.collectAsState()

    var isAddingToCart by remember { mutableStateOf(false) }

    when (response) {
        is ResourceState.Loading -> {
            Log.d("Sepete Ekleme", "Sepete Ekleme: Loading...")
        }
        is ResourceState.Success -> {
            val response = (response as ResourceState.Success).data
            Log.d("Sepete Ekleme", "Sepete Ekleme: SUCCESS... success = ${response.success} | message = ${response.message}")
            isAddingToCart = false
        }
        is ResourceState.Error -> {
            val error = (response as ResourceState.Error)
            Log.d("Sepete Ekleme", "Sepete Ekleme: Error... $error")
            isAddingToCart = false
        }
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            navController.navigate("detay/${yemek.yemek_adi}/${yemek.yemek_fiyat}/${yemek.yemek_resim_adi}/${yemek.yemek_id}")
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}",
                contentDescription = yemek.yemek_adi,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = yemek.yemek_adi,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 150.dp, height = 30.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${yemek.yemek_fiyat} ₺",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .padding(start = 8.dp),
                    fontSize = 20.sp
                )
                IconButton(
                    onClick = {
                        isAddingToCart = true
                        yemeklerViewModel.sepeteYemekEkle(
                            yemek.yemek_adi,
                            yemek.yemek_resim_adi,
                            yemek.yemek_fiyat,
                            yemek_siparis_adet = 1,
                            kullanici_adi = authViewModel.getUserName()
                        )
                    },
                    modifier = Modifier.wrapContentSize()
                ) {
                    if (isAddingToCart) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cart_added_lottie))
                        LottieAnimation(
                            composition = composition,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                            //speed = 0.05f,
                            //iterations = LottieConstants.IterateForever
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.add_square),
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }


            }
        }
    }
}



@Composable
fun YemekListesi(yemekler: List<Yemekler>, navController: NavController,authViewModel: AuthViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            //.padding(8.dp)
            //.background(Color.LightGray)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))

    ) {
        items(yemekler.size) { yemek ->
            YemekKart(yemek = yemekler[yemek], navController = navController, authViewModel = authViewModel)
        }
    }
}


@Composable
fun yemekDetayScreen(yemekAdi: String, yemekFiyat: Int, yemekResimAdi: String, yemekId: Int) {
    // TODO: Yemek Detay Ekranı parametre olarak

}





