package com.example.foodapp.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodapp.R
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entity.Food
import com.example.foodapp.ui.viewmodel.AuthState
import com.example.foodapp.ui.viewmodel.AuthViewModel
import com.example.foodapp.ui.viewmodel.HomeViewModel
import com.example.foodapp.ui.viewmodel.DetailViewModel
import com.example.foodapp.ui.navigation.BottomNavigationBar


import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
    authViewModel: AuthViewModel
) {
    Log.d("HOMESCREEN", "HomeScreen composable entered")

    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when(authState.value){
          is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    val foodsResponse by homeViewModel.foods.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var filteredFoods by remember { mutableStateOf<List<Food>>(emptyList()) }

    when (foodsResponse) {
        is ResourceState.Loading -> {
            Log.d("HOMESCREEN", "HomeScreen: Loading...")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ResourceState.Success -> {
            val response = (foodsResponse as ResourceState.Success).data
            Log.d(
                "HOMESCREEN",
                "HomeScreen: SUCCESS... success = ${response.success} | foods.size = ${response.foods?.size ?: 0}"
            )

            if (response.foods?.isNotEmpty() == true) {
                filteredFoods = if (searchText.isBlank()) {
                    response.foods ?: emptyList()
                } else {
                    (response.foods ?: emptyList()).filter {
                        it.food_name.contains(searchText, ignoreCase = true)
                    }
                }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Food",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 40.sp,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp)
                    ) {

                        Spacer(modifier = Modifier.height(6.dp))

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
                                    contentDescription = "Search"
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            FoodList(foods = filteredFoods, navController = navController, authViewModel = authViewModel)
                        }
                    }
                }

            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No foods found.", fontSize = 20.sp, color = Color.Gray)
                }
            }
        }

        is ResourceState.Error -> {
            val error = (foodsResponse as ResourceState.Error)
            Log.d("HOMESCREEN", "HomeScreen: Error... ${error.error}")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${error.error}", fontSize = 20.sp, color = Color.Red)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodCard(
    food: Food,
    detailViewModel: DetailViewModel = hiltViewModel(),
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val response by detailViewModel.addFoodToCart.collectAsState()

    var isAddingToCart by remember { mutableStateOf(false) }

    when (response) {
        is ResourceState.Loading -> {
            Log.d("AddToCart", "AddToCart: Loading...")
        }
        is ResourceState.Success -> {
            val response = (response as ResourceState.Success).data
            Log.d("AddToCart", "AddToCart: SUCCESS... success = ${response.success} | message = ${response.message}")
            isAddingToCart = false
        }
        is ResourceState.Error -> {
            val error = (response as ResourceState.Error)
            Log.d("AddToCart", "AddToCart: Error... $error")
            isAddingToCart = false
        }
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .wrapContentSize(),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            navController.navigate("detail/${food.food_name}/${food.food_price}/${food.food_image_name}/${food.food_id}")
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = "http://kasimadalan.pe.hu/yemekler/resimler/${food.food_image_name}",
                contentDescription = food.food_name,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = food.food_name,
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
                    text = "${food.food_price} ₺",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .padding(start = 8.dp),
                    fontSize = 20.sp
                )
                IconButton(
                    onClick = {
                        isAddingToCart = true
                        detailViewModel.addFoodToCart(
                            food.food_name,
                            food.food_image_name,
                            food.food_price,
                            food_order_quantity = 1,
                            user_name = authViewModel.getUserName()
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
fun FoodList(foods: List<Food>, navController: NavController,authViewModel: AuthViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(items = foods, key = { food -> food.food_id }) { food ->
            FoodCard(food = food, navController = navController, authViewModel = authViewModel)
        }
    }
}





