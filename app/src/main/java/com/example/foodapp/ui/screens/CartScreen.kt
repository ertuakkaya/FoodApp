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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.TopAppBarDefaults




import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodapp.R
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entity.CartFood
import com.example.foodapp.data.entity.CartFoodsResponse
import com.example.foodapp.data.entity.CRUDResponse
import com.example.foodapp.ui.viewmodel.AuthState
import com.example.foodapp.ui.viewmodel.AuthViewModel
import com.example.foodapp.ui.viewmodel.HomeViewModel
import com.example.foodapp.ui.navigation.BottomNavigationBar
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val cartFoodsResponse by homeViewModel.cartFoods.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.getCartFoods()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cart",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                            //modifier = Modifier.size(40.dp)
                        )
                    }
                },
                modifier = Modifier.wrapContentHeight(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        when (cartFoodsResponse) {
            is ResourceState.Loading -> {
                Log.d("CartScreen", "CartScreen: Loading...")
            }

            is ResourceState.Success -> {
                val response = (cartFoodsResponse as ResourceState.Success<CartFoodsResponse>).data
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
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(paddingValues),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(16.dp))

                        // Sepet Listesi
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                                //.size(400.dp,700.dp),
                            contentAlignment = Alignment.TopCenter


                        ){
                                                        CartList(foods = aggregatedFoods, homeViewModel = homeViewModel, navController = navController, authViewModel = authViewModel)
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
                                color = MaterialTheme.colorScheme.onSurface
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
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

            }

            is ResourceState.Error -> {
                Log.d("CartScreen", "CartScreen: Error... ${(cartFoodsResponse as ResourceState.Error).error}")

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
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
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }


                }// Column App Bar, Sepet Listesi, Total Price and LogOut

            }
        }
    }

    // Firebase
    val authState by authViewModel.authState.collectAsState()
    LaunchedEffect(authState) {
        // Kullanıcı Unauthanticaded durumunda ise login ekranına yönlendir
        when (authState) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartList(
    foods: List<CartFood>,
    homeViewModel: HomeViewModel,
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
                //.padding(start = 16.dp, end = 16.dp,)

        ) {

            items(foods.size) { food ->
                val kullaniciAdi = authViewModel.getUserName()
                CartCard(food = foods[food], homeViewModel = homeViewModel, authViewModel = authViewModel)
            }
        }






}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartCard(food: CartFood, homeViewModel: HomeViewModel = hiltViewModel(),authViewModel: AuthViewModel ){

    // coroutine scope
    val coroutineScope = rememberCoroutineScope()


    val response by homeViewModel.cartFoods.collectAsState()

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

    val deleteFoodFromCartResponse by homeViewModel.deleteFoodFromCart.collectAsState()

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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
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
                    .size(80.dp) // Fixed size for the image
                    .clip(RoundedCornerShape(8.dp)) // Add rounded corners to the image
                    .clickable {
                        // TODO: YemekDetayScreen'e git
                        //TODO: (yemekDetayScreen(yemek.yemek_adi, yemek.yemek_fiyat, yemek.yemek_resim_adi, yemek.yemek_id))
                    },
                contentScale = ContentScale.Crop, // Crop to fill the bounds
                alignment = Alignment.Center
            )


            // Yemek Fiyatı ve Sepete Ekle Butonu
            Column(
                modifier = Modifier
                    .weight(1f) // Take available space
                    .fillMaxHeight()
                    .padding(start = 8.dp), // Add some padding to the left of the text
                verticalArrangement = Arrangement.Center, // Center content vertically
                horizontalAlignment = Alignment.Start // Align text to start
            ) {
                Text(
                    text = food.food_name,
                    fontSize = 18.sp, // Slightly smaller font size
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1, // Limit to one line
                    overflow = TextOverflow.Ellipsis // Add ellipsis if text overflows
                )
                Spacer(modifier = Modifier.height(4.dp)) // Small space between name and price
                Text(
                    text = "${food.food_price} ₺",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp, // Slightly smaller font size
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp)) // Small space between price and quantity
                Text(
                    text = "Quantity: ${food.food_order_quantity}",
                    fontSize = 14.sp, // Smaller font size for quantity
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 8.dp), // Add padding to the right
                verticalArrangement = Arrangement.SpaceAround, // Distribute space evenly
                horizontalAlignment = Alignment.End // Align content to the end
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            homeViewModel.deleteFoodFromCart(cart_food_id = food.cart_food_id, user_name = authViewModel.getUserName() )
                        }
                    },
                    modifier = Modifier
                        .size(36.dp) // Slightly larger icon button
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.garbage_bin),
                        contentDescription = "Delete from cart",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.error // Use error color for delete icon
                    )
                }
                Text(
                    text = "${food.food_price * food.food_order_quantity} ₺",
                    fontSize = 18.sp, // Larger font size for total price
                    fontWeight = FontWeight.Bold, // Bold for total price
                    color = MaterialTheme.colorScheme.onSurface
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