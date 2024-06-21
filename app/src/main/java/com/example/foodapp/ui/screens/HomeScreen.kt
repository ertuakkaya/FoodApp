package com.example.foodapp.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodapp.data.ResourceState
import com.example.foodapp.ui.viewmodel.YemeklerViewModel

@Composable
fun HomeScreen(yemeklerViewModel: YemeklerViewModel = hiltViewModel()) {


    val yemeklerResponse by yemeklerViewModel.yemekler.collectAsState()


   // TODO("when yapısı istenilen componentin icine konacak")

    when(yemeklerResponse){
        is ResourceState.Loading -> {
            Log.d("HOMESCREEN", "HomeScreen: Loading...")
        }
        is ResourceState.Success -> {

            val response = (yemeklerResponse as ResourceState.Success).data
            Log.d("HOMESCREEN", "HomeScreen: SUCCESS... success = ${response.success} | yemekeler.size = ${response.yemekler.size}")

            if(response.yemekler.isNotEmpty()){
               // TODO("buraya yemekler listesi eklenecek if else yapısı ile")
            }else{
               // TODO("EmptyStateComponent() buraya yemeklerin yuklenmedigi senaryo eklenecek")
            }

        }
        is ResourceState.Error -> {
            val error = (yemeklerResponse as ResourceState.Error)
            Log.d("HOMESCREEN", "HomeScreen: Error... $error")
        }
    }



}
