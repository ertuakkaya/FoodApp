package com.example.foodapp.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodapp.R
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entitiy.SepetYemekler
import com.example.foodapp.data.entitiy.Yemekler
import com.example.foodapp.ui.viewmodel.AuthState
import com.example.foodapp.ui.viewmodel.AuthViewModel
import com.example.foodapp.ui.viewmodel.YemeklerViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SepetScreen(
    yemeklerViewModel: YemeklerViewModel = hiltViewModel(),
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val sepetYemeklerResponse by yemeklerViewModel.sepetYemekler.collectAsState()

    when (sepetYemeklerResponse) {
        is ResourceState.Loading -> {
            val loading = (sepetYemeklerResponse as ResourceState.Loading)
            Log.d("SepetScreen", "SepetScreen: Loading... $loading")
        }

        is ResourceState.Success -> {
            val response = (sepetYemeklerResponse as ResourceState.Success).data
            Log.d(
                "SepetScreen",
                "SepetScreen: SUCCESS... success = ${response.success} | yemekler.size = ${response.sepet_yemekler.size}"
            )
            if (response.sepet_yemekler.isNotEmpty()) {
                val aggregatedYemekler = response.sepet_yemekler.groupBy { it.yemek_adi }
                    .map { (yemekAdi, yemekList) ->
                        yemekList.first()
                            .copy(yemek_siparis_adet = yemekList.sumOf { it.yemek_siparis_adet })
                    }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // App Bar
                    TopAppBar(
                        title = {
                            Text(
                                text = "Cart",
                                fontWeight = FontWeight.Medium,
                                fontSize = 40.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    navController.navigate("home")
                                },
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        },
                        modifier = Modifier.wrapContentHeight(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sepet Listesi
                    Box(
                        modifier = Modifier
                            .size(400.dp,700.dp),
                        contentAlignment = Alignment.TopCenter


                    ){
                        SepetListesi(yemekler = aggregatedYemekler, yemeklerViewModel = yemeklerViewModel, navController = navController, authViewModel = authViewModel)
                    }


                    // Total Price and LogOut
                    Spacer(modifier = Modifier.height(16.dp))

                    // Row Bottom Bar with Total Price and Sign Out Button
                    Row(
                        modifier = Modifier
                            .size(400.dp, 60.dp)
                            .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
                            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp)),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Total Price
                        Text(
                            text = "Total Price: ${aggregatedYemekler.sumOf { it.yemek_fiyat * it.yemek_siparis_adet }} ₺",
                            modifier = Modifier.weight(1f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                        )
                        val coroutineScope = rememberCoroutineScope()
//                        Box(
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            IconButton(
//                                onClick = {
//                                    coroutineScope.launch {
//                                        authViewModel.signOut()
//                                        navController.navigate("login")
//                                    }
//                                },
//                                modifier = Modifier
//                                    .align(Alignment.Center)
//                                    .padding(4.dp)
//                                    .fillMaxSize()
//                            ) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.sign_out_squre_fill),
//                                    contentDescription = "sign out",
//                                    modifier = Modifier.fillMaxSize()
//                                )
//                            }
//                        }
                    } // Row Bottom Bar with Total Price and Sign Out Button
                }// Column App Bar, Sepet Listesi, Total Price and LogOut

            } else {
                // Sepet boş olduğunda yapılacak işlemler
                Text(
                    text = "Sepetiniz boş",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }

        }

        is ResourceState.Error -> {
            val error = (sepetYemeklerResponse as ResourceState.Error)
            Log.d("SepetScreen", "SepetScreen: Error...: $error")

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Bar
                TopAppBar(
                    title = {
                        Text(
                            text = "Cart",
                            fontWeight = FontWeight.Medium,
                            fontSize = 40.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigate("home")
                            },
                            modifier = Modifier
                                .padding(10.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    },
                    modifier = Modifier.wrapContentHeight(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Sepet Listesi
                Box(
                    modifier = Modifier
                        .size(400.dp,700.dp),
                    contentAlignment = Alignment.Center


                ){
                    Column (
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){

                        // TODO: Sepet Boş olduğunda yapılacak işlemler
                        // lottie animation
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_card))
                        LottieAnimation(
                            composition = composition,
                            modifier = Modifier
                                .fillMaxWidth(),
                            iterations = LottieConstants.IterateForever,
                            reverseOnRepeat = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Your cart is empty.",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                }


            }// Column App Bar, Sepet Listesi, Total Price and LogOut

        }
    }

    // Firebase
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        // Kullanıcı Unauthanticaded durumunda ise login ekranına yönlendir
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SepetListesi(
    yemekler: List<SepetYemekler>,
    yemeklerViewModel: YemeklerViewModel,
    navController: NavController,
    authViewModel: AuthViewModel
) {





        Spacer(modifier = Modifier.height(8.dp))

        // Cart Items
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),

            modifier = Modifier

                //.fillMaxSize()
                .fillMaxWidth()
                //.height(1500.dp) ////
                .padding(start = 16.dp, end = 16.dp,)
                //.background(Brush.verticalGradient(listOf(Color.White, Color.LightGray)))
                //.background(Color.LightGray)
                .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))

        ) {

            items(yemekler.size) { yemek ->
                val kullaniciAdi = authViewModel.getUserName()
                SepetKart(yemek = yemekler[yemek], yemeklerViewModel = yemeklerViewModel, authViewModel = AuthViewModel())
            }
        }






}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SepetKart(yemek: SepetYemekler, yemeklerViewModel: YemeklerViewModel = hiltViewModel(),authViewModel: AuthViewModel ){

    // coroutine scope
    val coroutineScope = rememberCoroutineScope()


    val response by yemeklerViewModel.sepetYemekler.collectAsState()

    when (response) {
        is ResourceState.Loading -> {
            Log.d("Sepete Ekleme", "Sepete Ekleme: Loading...")
        }

        is ResourceState.Success -> {
            val response = (response as ResourceState.Success).data
            Log.d(
                "Sepete Ekleme",
                "Sepete Ekleme: SUCCESS... success = ${response.success} | message = ${response.sepet_yemekler.size}"
            )
        }

        is ResourceState.Error -> {
            val error = (response as ResourceState.Error)
            Log.d("Sepete Ekleme", "Sepete Ekleme: Error... $error")
        }
    }

    val sepetYemekSilResponse by yemeklerViewModel.sepettenYemekSil.collectAsState()

    when (sepetYemekSilResponse) {
        is ResourceState.Loading -> {
            Log.d("SepetSilme", "SepetSilme: Loading...")
        }

        is ResourceState.Success -> {
            val response = (sepetYemekSilResponse as ResourceState.Success).data
            Log.d(
                "SepetSilme",
                "SepetSilme: SUCCESS... success = ${response.success} | message = ${response.message}"
            )
        }

        is ResourceState.Error -> {
            val error = (sepetYemekSilResponse as ResourceState.Error)
            Log.d("SepetSilme", "SepetSilme: Error... $error")
        }
    }



    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Color.Gray),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
            .height(130.dp),
        //.wrapContentSize(),

        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {} // TODO: KART ICINDEN DETAYA GIDILEBILIR
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),

            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Yemek Resmi
            AsyncImage(
                model = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}",
                contentDescription = yemek.yemek_adi,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable {
                        // TODO: YemekDetayScreen'e git
                        //TODO: (yemekDetayScreen(yemek.yemek_adi, yemek.yemek_fiyat, yemek.yemek_resim_adi, yemek.yemek_id))

                    },
                //.fillMaxWidth(),
                contentScale = ContentScale.Fit,
                alignment = Alignment.CenterStart


            )


            // Yemek Fiyatı ve Sepete Ekle Butonu
            Column(
                modifier = Modifier
                    //.fillMaxSize(),
                    //.fillMaxWidth(),
                    .size(width = 150.dp, height = 100.dp),


                verticalArrangement = Arrangement.SpaceBetween,

                ) {
                // Yemek Adı
                Text(
                    text = yemek.yemek_adi,
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.size(width = 150.dp, height = 30.dp),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,


                    )
                // Yemek Fiyatı
                Text(
                    text = "${yemek.yemek_fiyat} ₺",
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.weight(1f),
                    //.align(Alignment.CenterVertically)
                    //.padding(start = 8.dp),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
                // Yemek Adedi
                Text(
                    text = "Quantity: ${yemek.yemek_siparis_adet}",
                    modifier = Modifier
                        .fillMaxWidth(),

                    //.padding(top = 4.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                // Sepet Sil Butonu
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            yemeklerViewModel.sepettenYemekSil(sepet_yemek_id = yemek.sepet_yemek_id, kullanici_adi = authViewModel.getUserName() )
                        }

                    },
                    modifier = Modifier
                        .size(30.dp)
                        //.padding(end = 8.dp)
                        .align(Alignment.CenterHorizontally),

                    ) {
                    Icon(
                        painter = painterResource(id = R.drawable.garbage_bin),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()


                    )
                }

                // Toplam Fiayt
                Text(
                    text = "${yemek.yemek_fiyat * yemek.yemek_siparis_adet} ₺",
                    modifier = Modifier
                        .fillMaxWidth(),


                    //.padding(top = 4.dp),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }

        }
    }
}


/*
@Composable
@Preview
fun PreviewSepetKart() {
    SepetKart( SepetYemekler(1, "Yemek Adı", "yemek_resim_adi", 10, 1))
}
*/



