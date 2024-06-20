package com.example.foodapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigationGraph (){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME_SCREEN) {

        composable(Routes.HOME_SCREEN) {
            //HomeScreen()
            TODO("BURAYA HOME SCREEN EKLENECEK")
        }
    }

}