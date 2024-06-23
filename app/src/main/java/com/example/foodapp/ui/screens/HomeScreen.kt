package com.example.foodapp.ui.screens

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entitiy.Yemekler
import com.example.foodapp.ui.viewmodel.YemeklerViewModel

@Composable
fun HomeScreen(yemeklerViewModel: YemeklerViewModel = hiltViewModel() , navController: NavController) {

    val yemeklerResponse by yemeklerViewModel.yemekler.collectAsState()


    when(yemeklerResponse){
        is ResourceState.Loading -> {
            Log.d("HOMESCREEN", "HomeScreen: Loading...")
        }
        is ResourceState.Success -> {
            val response = (yemeklerResponse as ResourceState.Success).data
            Log.d("HOMESCREEN", "HomeScreen: SUCCESS... success = ${response.success} | yemekeler.size = ${response.yemekler.size}")

            if(response.yemekler.isNotEmpty()){

                //
                YemekListesi(yemekler = response.yemekler)

            }else{
                // TODO("EmptyStateComponent() buraya yemeklerin yuklenmedigi senaryo eklenecek")
            }
        }
        is ResourceState.Error -> {
            val error = (yemeklerResponse as ResourceState.Error)
            Log.d("HOMESCREEN", "HomeScreen: Error... $error")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Sepete Git
        IconButton(
            onClick = {
                navController.navigate("sepet")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(66.dp)
                .background(Color.White, shape = RoundedCornerShape(28.dp))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.basket),
                contentDescription = "Sepet",
                modifier = Modifier.size(30.dp),
                tint = Color.Black
            )
        }
    }


}


@Composable
fun YemekKart(yemek: Yemekler,yemeklerViewModel: YemeklerViewModel = hiltViewModel()) { // TODO: viewmodel parametresi eklenecek

   val response by yemeklerViewModel.sepeteYemekEkle.collectAsState()

   when(response){

       is ResourceState.Loading -> {

           Log.d("Sepete Ekeleme", "Sepete Ekeleme: Loading...")

       }
       is ResourceState.Success -> {
           val response = (response as ResourceState.Success).data
           Log.d("Sepete Ekeleme", "Sepete Ekeleme: SUCCESS... success = ${response.success} | message = ${response.message}")

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
                        painter = painterResource(id = R.drawable.add_square),
                       contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                   )
               }


           }
       }
   }
}

/*
/// worked
@Composable
fun YemekListesi(yemekler: List<Yemekler>) {
   LazyColumn (
       modifier = Modifier
           .fillMaxSize(),
       verticalArrangement = Arrangement.spacedBy(8.dp)
   ){
       items(yemekler.size) { yemek ->
           YemekKart(yemek = yemekler[yemek])
       }
   }
}
*/
@Composable
fun YemekListesi(yemekler: List<Yemekler>) {
   LazyVerticalGrid (
       columns = GridCells.Fixed(2),
       modifier = Modifier
           .fillMaxSize()
           //.padding(8.dp)
           .background(Color.LightGray)

   ){
       items(yemekler.size) { yemek ->
           YemekKart(yemek = yemekler[yemek])
       }
   }
}


@Composable
fun yemekDetayScreen(yemekAdi: String, yemekFiyat: Int, yemekResimAdi: String, yemekId: Int) {
   // TODO: Yemek Detay Ekranı parametre olarak

}


@Composable
@Preview()
fun YemekKartPreview(){
YemekKart(yemek = Yemekler(1, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10))
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun YemekListesiPreview(){
   YemekListesi(
       yemekler = listOf(
           Yemekler(1, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(2, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(3, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(4, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(5, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(6, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(7, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(8, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(9, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(10, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(11, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(12, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10),
           Yemekler(13, "Yemek Adı", "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png", 10)))
}


