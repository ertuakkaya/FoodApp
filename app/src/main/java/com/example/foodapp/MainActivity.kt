package com.example.foodapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.foodapp.ui.navigation.AppNavigationGraph
import com.example.foodapp.ui.theme.FoodAppTheme
import com.example.foodapp.ui.viewmodel.AuthViewModel
import com.example.foodapp.ui.viewmodel.FoodsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity ( ) : ComponentActivity() {

    private val viewModel: FoodsViewModel by viewModels()

    val authViewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    AppNavigationGraph(authViewModel = authViewModel,viewModel = viewModel)

                }

            }
        }
    }
}

