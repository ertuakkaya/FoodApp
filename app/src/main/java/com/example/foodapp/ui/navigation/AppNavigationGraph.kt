package com.example.foodapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodapp.R
import com.example.foodapp.data.entity.CartFood
import com.example.foodapp.ui.screens.AccountScreen
import com.example.foodapp.ui.screens.DetailScreen
import com.example.foodapp.ui.screens.HomeScreen
import com.example.foodapp.ui.screens.CartScreen
import com.example.foodapp.ui.screens.login.LoginPage
import com.example.foodapp.ui.screens.signup.SignUpPage
import com.example.foodapp.ui.viewmodel.AuthViewModel
import com.example.foodapp.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavigationGraph (modifier: Modifier = Modifier) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(key1 = Unit) {
                coroutineScope.launch {
                    delay(1000) // Simulate a loading process
                    if (authViewModel.isUserLoggedIn()) {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_lottie)).value
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.fillMaxSize(),
                    iterations = LottieConstants.IterateForever,
                    reverseOnRepeat = true
                )
            }
        }

        composable("login") {
            LoginPage(modifier = modifier, navController = navController)
        }

        composable("signup") {
            SignUpPage(modifier = modifier, navController = navController)
        }

        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("cart") {
            CartScreen(navController = navController)
        }

        composable(
            route = "detail/{foodName}/{foodPrice}/{foodImageName}/{foodId}",
            arguments = listOf(
                navArgument("foodName") { type = NavType.StringType },
                navArgument("foodPrice") { type = NavType.IntType },
                navArgument("foodImageName") { type = NavType.StringType },
                navArgument("foodId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val foodName = backStackEntry.arguments?.getString("foodName") ?: ""
            val foodPrice = backStackEntry.arguments?.getInt("foodPrice") ?: 0
            val foodImageName = backStackEntry.arguments?.getString("foodImageName") ?: ""
            val foodId = backStackEntry.arguments?.getInt("foodId") ?: 0
            DetailScreen(
                food = CartFood(
                    cart_food_id = foodId,
                    food_name = foodName,
                    food_price = foodPrice,
                    food_image_name = foodImageName,
                    food_order_quantity = 1
                ), navController = navController

            )
        }

        composable("account") {
            AccountScreen(navController = navController)
        }

    }
}