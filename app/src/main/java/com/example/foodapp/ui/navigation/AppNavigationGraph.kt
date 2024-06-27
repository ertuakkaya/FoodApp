package com.example.foodapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodapp.data.entitiy.SepetYemekler
import com.example.foodapp.ui.screens.AccountScreen
import com.example.foodapp.ui.screens.DetayScreen
import com.example.foodapp.ui.screens.HomeScreen
import com.example.foodapp.ui.screens.SepetScreen
import com.example.foodapp.ui.screens.login.LoginPage
import com.example.foodapp.ui.screens.signup.SignUpPage
import com.example.foodapp.ui.viewmodel.AuthViewModel
import com.example.foodapp.ui.viewmodel.YemeklerViewModel

@Composable
fun AppNavigationGraph (modifier: Modifier = Modifier,authViewModel: AuthViewModel,viewModel: YemeklerViewModel = hiltViewModel()){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {



        composable("login") {
            LoginPage(modifier = modifier, navController = navController,authViewModel = authViewModel)
            //TODO("BURAYA LOGIN SCREEN EKLENECEK")
        }

        composable("signup") {
           SignUpPage(modifier = modifier, navController = navController,authViewModel = authViewModel)
            //TODO("BURAYA SignUp SCREEN EKLENECEK")
        }

        composable("home") {
            HomeScreen(yemeklerViewModel = viewModel,navController = navController,authViewModel = authViewModel)
        }

        composable("sepet") {
            SepetScreen(viewModel,navController = navController,authViewModel = authViewModel)
            viewModel.sepettekiYemekleriGetir()
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
                ),navController = navController,authViewModel = authViewModel

            )
        }

        composable("account") {
            AccountScreen(authViewModel = authViewModel)

        }
    }

}