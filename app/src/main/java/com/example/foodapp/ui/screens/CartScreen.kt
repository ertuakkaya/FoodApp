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

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold




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
import com.example.foodapp.data.entity.CartFood
import com.example.foodapp.ui.viewmodel.AuthState
import com.example.foodapp.ui.viewmodel.AuthViewModel
import com.example.foodapp.ui.viewmodel.FoodsViewModel
import com.example.foodapp.ui.navigation.BottomNavigationBar
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    foodsViewModel: FoodsViewModel = hiltViewModel(),
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val cartFoodsResponse by foodsViewModel.cartFoods.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cart",
                        fontWeight = FontWeight.Medium,
                        //fontSize = 40.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("home")
                        },
                        modifier = Modifier
                            //.padding(10.dp)
                            //.clip(RoundedCornerShape(8.dp))
                            //.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            //modifier = Modifier.size(40.dp)
                        )
                    }
                },
                modifier = Modifier.wrapContentHeight(),
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        when (cartFoodsResponse) {
            is ResourceState.Loading -> {
                val loading = (cartFoodsResponse as ResourceState.Loading)
                Log.d("CartScreen", "CartScreen: Loading... $loading")
            }

            is ResourceState.Success -> {
                val response = (cartFoodsResponse as ResourceState.Success).data
                Log.d(
                    "CartScreen",
                    "CartScreen: SUCCESS... success = ${response.success} | foods.size = ${response.cart_foods.size}"
                )
                if (response.cart_foods.isNotEmpty()) {
                    val aggregatedFoods = response.cart_foods.groupBy { it.food_name }
                        .map { (foodName, foodList) ->
                            foodList.first()
                                .copy(food_order_quantity = foodList.sumOf { it.food_order_quantity })
                        }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(paddingValues),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(16.dp))

                        // Sepet Listesi
                        Box(
                            modifier = Modifier
                                .size(400.dp,700.dp),
                            contentAlignment = Alignment.TopCenter


                        ){
                            CartList(foods = aggregatedFoods, foodsViewModel = foodsViewModel, navController = navController, authViewModel = authViewModel)
                        }


                        // Total Price and LogOut
                        Spacer(modifier = Modifier.height(16.dp))

                        // Row Bottom Bar with Total Price and Sign Out Button
                        Row(
                            modifier = Modifier
                                .size(400.dp, 60.dp)
                                .padding(start = 10.dp, end = 10.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Total Price
                            Text(
                                text = "Total Price: ${aggregatedFoods.sumOf { it.food_price * it.food_order_quantity }} ₺",
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
                val error = (cartFoodsResponse as ResourceState.Error)
                Log.d("CartScreen", "CartScreen: Error...: $error")

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

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
fun CartList(
    foods: List<CartFood>,
    foodsViewModel: FoodsViewModel,
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

        ) {

            items(foods.size) { food ->
                val kullaniciAdi = authViewModel.getUserName()
                CartCard(food = foods[food], foodsViewModel = foodsViewModel, authViewModel = authViewModel)
            }
        }






}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartCard(food: CartFood, foodsViewModel: FoodsViewModel = hiltViewModel(),authViewModel: AuthViewModel ){

    // coroutine scope
    val coroutineScope = rememberCoroutineScope()


    val response by foodsViewModel.cartFoods.collectAsState()

    when (response) {
        is ResourceState.Loading -> {
            Log.d("AddToCart", "AddToCart: Loading...")
        }

        is ResourceState.Success -> {
            val response = (response as ResourceState.Success).data
            Log.d(
                "AddToCart",
                "AddToCart: SUCCESS... success = ${response.success} | message = ${response.cart_foods.size}"
            )
        }

        is ResourceState.Error -> {
            val error = (response as ResourceState.Error)
            Log.d("AddToCart", "AddToCart: Error... $error")
        }
    }

    val deleteFoodFromCartResponse by foodsViewModel.deleteFoodFromCart.collectAsState()

    when (deleteFoodFromCartResponse) {
        is ResourceState.Loading -> {
            Log.d("DeleteFromCart", "DeleteFromCart: Loading...")
        }

        is ResourceState.Success -> {
            val response = (deleteFoodFromCartResponse as ResourceState.Success).data
            Log.d(
                "DeleteFromCart",
                "DeleteFromCart: SUCCESS... success = ${response.success} | message = ${response.message}"
            )
        }

        is ResourceState.Error -> {
            val error = (deleteFoodFromCartResponse as ResourceState.Error)
            Log.d("DeleteFromCart", "DeleteFromCart: Error... $error")
        }
    }



    Card(
        shape = RoundedCornerShape(8.dp),
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
                model = "http://kasimadalan.pe.hu/yemekler/resimler/${food.food_image_name}",
                contentDescription = food.food_name,
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
                    text = food.food_name,
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.size(width = 150.dp, height = 30.dp),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,


                    )
                // Yemek Fiyatı
                Text(
                    text = "${food.food_price} ₺",
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
                    text = "Quantity: ${food.food_order_quantity}",
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
                            foodsViewModel.deleteFoodFromCart(cart_food_id = food.cart_food_id, user_name = authViewModel.getUserName() )
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
                    text = "${food.food_price * food.food_order_quantity} ₺",
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