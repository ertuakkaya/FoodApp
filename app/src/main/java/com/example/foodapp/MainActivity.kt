package com.example.foodapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.foodapp.ui.theme.FoodAppTheme
import com.example.foodapp.ui.viewmodel.YemeklerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity  : ComponentActivity() {

    private val viewModel: YemeklerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    //HomeScreen()
                    //SepetScreen(navController = nav)
                    
                    val navController = rememberNavController()
                    
                    NavHost(navController = navController, startDestination ="home") {
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
                                ),
                                onAddToCart = { quantity ->
                                    viewModel.sepeteYemekEkle(
                                        yemekAdi,
                                        yemekResimAdi,
                                        yemekFiyat,
                                        quantity
                                    )
                                }
                            )
                        }

                    }

                }

            }
        }
    }
}

