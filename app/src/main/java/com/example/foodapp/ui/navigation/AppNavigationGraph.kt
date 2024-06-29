package com.example.foodapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.foodapp.data.entitiy.SepetYemekler
import com.example.foodapp.ui.screens.AccountScreen
import com.example.foodapp.ui.screens.DetayScreen
import com.example.foodapp.ui.screens.HomeScreen
import com.example.foodapp.ui.screens.SepetScreen
import com.example.foodapp.ui.screens.login.LoginPage
import com.example.foodapp.ui.screens.signup.SignUpPage
import com.example.foodapp.ui.viewmodel.AuthViewModel
import com.example.foodapp.ui.viewmodel.YemeklerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavigationGraph (modifier: Modifier = Modifier,authViewModel: AuthViewModel,viewModel: YemeklerViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    val coroutineScope = rememberCoroutineScope()

    val isLoading = remember { mutableStateOf(true) }

    NavHost(navController = navController, startDestination = "login") {


        composable("login") {


            // Set isLoading to true every time we navigate to this screen
            LaunchedEffect(key1 = "login") {
                isLoading.value = true
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading.value) {
                    // lottie animation
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_lottie))
                    LottieAnimation(
                        composition = composition,
                        modifier = Modifier.fillMaxSize(),
                        iterations = LottieConstants.IterateForever,
                        reverseOnRepeat = true
                    )
                } else {
                    LoginPage(modifier = modifier, navController = navController, authViewModel = authViewModel)
                    //viewModel.sepettekiYemekleriGetir()
                }
            }

            LaunchedEffect(key1 = "login") {
                delay(1000) // Adjust this delay to match your loading time
                isLoading.value = false
            }

        }

        composable("signup") {

            // Set isLoading to true every time we navigate to this screen
            LaunchedEffect(key1 = "signup") {
                isLoading.value = true
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading.value) {
                    // lottie animation
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_lottie))
                    LottieAnimation(
                        composition = composition,
                        modifier = Modifier.fillMaxSize(),
                        iterations = LottieConstants.IterateForever,
                        reverseOnRepeat = true
                    )
                } else {
                    SignUpPage(modifier = modifier, navController = navController, authViewModel = authViewModel)
                }
            }

            LaunchedEffect(key1 = "signup") {
                delay(1000) // Adjust this delay to match your loading time
                isLoading.value = false
            }

        }

        composable("home") {
            HomeScreen(yemeklerViewModel = viewModel, navController = navController, authViewModel = authViewModel)
        }

        composable("sepet") {

            // Set isLoading to true every time we navigate to this screen
            LaunchedEffect(key1 = "sepet") {
                isLoading.value = true
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading.value) {
                    // lottie animation
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_lottie))
                    LottieAnimation(
                        composition = composition,
                        modifier = Modifier.fillMaxSize(),
                        iterations = LottieConstants.IterateForever,
                        reverseOnRepeat = true
                    )
                } else {
                    SepetScreen(viewModel, navController = navController, authViewModel = authViewModel)
                    viewModel.sepettekiYemekleriGetir()
                }
            }

            LaunchedEffect(key1 = "sepet") {
                delay(2000) // Adjust this delay to match your loading time
                isLoading.value = false
            }



        }

        composable(
            route = "detay/{yemekAdi}/{yemekFiyat}/{yemekResimAdi}/{yemekId}",
            arguments = listOf(
                navArgument("yemekAdi") { type = NavType.StringType },
                navArgument("yemekFiyat") { type = NavType.IntType },
                navArgument("yemekResimAdi") { type = NavType.StringType },
                navArgument("yemekId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val yemekAdi = backStackEntry.arguments?.getString("yemekAdi") ?: ""
            val yemekFiyat = backStackEntry.arguments?.getInt("yemekFiyat") ?: 0
            val yemekResimAdi = backStackEntry.arguments?.getString("yemekResimAdi") ?: ""
            val yemekId = backStackEntry.arguments?.getInt("yemekId") ?: 0
            DetayScreen(
                yemek = SepetYemekler(
                    sepet_yemek_id = yemekId,
                    yemek_adi = yemekAdi,
                    yemek_fiyat = yemekFiyat,
                    yemek_resim_adi = yemekResimAdi,
                    yemek_siparis_adet = 1
                ), navController = navController, authViewModel = authViewModel

            )
        }




        composable("account") {

            // Set isLoading to true every time we navigate to this screen
            LaunchedEffect(key1 = "account") {
                isLoading.value = true
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading.value) {
                    // lottie animation
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.auth_lottie))
                    LottieAnimation(
                        composition = composition,
                        modifier = Modifier.fillMaxSize(),
                        iterations = LottieConstants.IterateForever,
                        reverseOnRepeat = true
                    )
                } else {
                    AccountScreen(authViewModel = authViewModel, navController = navController)
                }
            }

            LaunchedEffect(key1 = "account") {
                delay(1000) // Adjust this delay to match your loading time
                isLoading.value = false
            }
        }

    }
}