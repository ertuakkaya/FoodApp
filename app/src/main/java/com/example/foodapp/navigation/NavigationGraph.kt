package com.example.foodapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodapp.data.entitiy.SepetYemekler
import com.example.foodapp.ui.screens.DetayScreen
import com.example.foodapp.ui.screens.HomeScreen
import com.example.foodapp.ui.screens.SepetScreen
import com.example.foodapp.ui.screens.login_screen.SignInScreen
import com.example.foodapp.ui.viewmodel.YemeklerViewModel

@Composable
fun NavigationGraph(viewModel: YemeklerViewModel = hiltViewModel() ){

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination ="signInScreen"
    ){
        composable("signInScreen"){
            SignInScreen()
        }

        composable("home") {
            HomeScreen(viewModel,navController = navController)
        }
        composable("sepet") {
            SepetScreen(viewModel,navController = navController)
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
                ),navController = navController

            )
        }

    }


}