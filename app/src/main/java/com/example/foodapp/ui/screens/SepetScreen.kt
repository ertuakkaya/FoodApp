package com.example.foodapp.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entitiy.SepetYemekler
import com.example.foodapp.data.entitiy.Yemekler
import com.example.foodapp.ui.viewmodel.YemeklerViewModel


@Composable
fun SepetScreen(yemeklerViewModel: YemeklerViewModel = hiltViewModel()) {
    val sepetYemeklerResponse by yemeklerViewModel.sepetYemekler.collectAsState()


    when(sepetYemeklerResponse){
        is ResourceState.Loading -> {

            val loading = (sepetYemeklerResponse as ResourceState.Loading)
            Log.d("SepetScreen", "SepetScreen: Loading... $loading")
        }
        is ResourceState.Success -> {
            val response = (sepetYemeklerResponse as ResourceState.Success).data
            Log.d("SepetScreen", "SepetScreen: SUCCESS... success = ${response.success} | yemekeler.size = ${response.sepet_yemekler.size}")

            if(response.sepet_yemekler.isNotEmpty()){

                //
                SepetListesi(yemekler = response.sepet_yemekler)

            }else{
                // TODO("EmptyStateComponent() buraya yemeklerin yuklenmedigi senaryo eklenecek")
            }
        }
        is ResourceState.Error -> {
            val error = (sepetYemeklerResponse as ResourceState.Error)
            Log.d("SepetScreen", "SepetScreen: Error... $error")
        }
    }
}

@Composable
fun SepetListesi(yemekler: List<SepetYemekler>) {
    LazyVerticalGrid (
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            //.padding(8.dp)
            .background(Color.LightGray)

    ){
        items(yemekler.size) { yemek ->
            SepetKart(yemek = yemekler[yemek])
        }
    }
}

@Composable
fun SepetKart(yemek: SepetYemekler,yemeklerViewModel: YemeklerViewModel = hiltViewModel()) { // TODO: viewmodel parametresi eklenecek

    val response by yemeklerViewModel.sepetYemekler.collectAsState()

    when(response){

        is ResourceState.Loading -> {

            Log.d("Sepete Ekeleme", "Sepete Ekeleme: Loading...")

        }
        is ResourceState.Success -> {
            val response = (response as ResourceState.Success).data
            Log.d("Sepete Ekeleme", "Sepete Ekeleme: SUCCESS... success = ${response.success} | message = ${response.sepet_yemekler.size}")

        }
        is ResourceState.Error -> {
            val error = (response as ResourceState.Error)
            Log.d("Sepete Ekeleme", "Sepete Ekeleme: Error... $error")
        }
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(),
        elevation =  CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Yemek Resmi
            AsyncImage(
                model = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}",
                contentDescription = yemek.yemek_adi,
                modifier = Modifier
                    .height(150.dp)
                    .clickable {
                        // TODO: YemekDetayScreen'e git
                        //TODO: (yemekDetayScreen(yemek.yemek_adi,yemek.yemek_fiyat,yemek.yemek_resim_adi,yemek.yemek_id))


                    }
                    .fillMaxWidth(),

                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Yemek Adı
            Text(
                text = yemek.yemek_adi,
                modifier = Modifier
                    //.fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .size(width = 150.dp, height = 30.dp),

                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold



            )
            // Yemek Fiyatı ve Sepete Ekle Butonu
            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween

            ){
                // Yemek Fiyatı
                Text(
                    text = "${yemek.yemek_fiyat} TL",
                    color = Color.Red,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        //.border(1.dp, Color.Red, RectangleShape)
                        .padding(start = 8.dp),
                    fontSize = 20.sp

                )

                // Sepete Ekle Butonu
                IconButton(
                    onClick = {
                        // TODO: Sepete Ekle api çağrısı yapılacak
                        // TODO: Sepete ekleme animasyonunu getir
                        // TODO: viewmodelden sepete ekle fonksiyonu çağrılacak ////

                        yemeklerViewModel.sepeteYemekEkle(yemek.yemek_adi, yemek.yemek_resim_adi, yemek.yemek_fiyat, 1)



                    },
                    modifier = Modifier
                        .wrapContentSize()
                    //.size(25.dp),


                )
                {
                    Icon(
                        painter = painterResource(id = R.drawable.basket),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }


            }
        }
    }
}