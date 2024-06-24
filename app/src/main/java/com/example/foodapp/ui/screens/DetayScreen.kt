package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodapp.data.entitiy.SepetYemekler
import com.example.foodapp.data.entitiy.Yemekler
import com.example.foodapp.ui.viewmodel.YemeklerViewModel

//onAddToCart : (Int) -> Unit
@Composable
fun DetayScreen(yemek: SepetYemekler,yemeklerViewModel: YemeklerViewModel = hiltViewModel() ,navController: NavController) {
    //var quantity = 0
    var yemek_id = yemek.sepet_yemek_id
    var yemek_adi = yemek.yemek_adi
    var yemek_resim_adi = yemek.yemek_resim_adi
    var yemek_fiyat = yemek.yemek_fiyat
    var kullanici_adi = yemek.kullanici_adi


    var quantity by remember {
        mutableStateOf(yemek.yemek_siparis_adet)
    }




    val totalPrice = yemek.yemek_fiyat * quantity


    IconButton(
        onClick = { navController.navigate("home") }, // back to home
        modifier = Modifier
            .padding(20.dp)
            .wrapContentSize(Alignment.TopStart)
            .clip(RoundedCornerShape(8.dp)),




    ) {
        Icon(
            Icons.Filled.Close, contentDescription = "",
            modifier = Modifier.size(40.dp),
            tint = Color.Gray
        )

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(3.dp, Color.Gray, RoundedCornerShape(8.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {



        // Yemek Resmi
        AsyncImage(
            model = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}",
            contentDescription = yemek.yemek_adi,
            modifier = Modifier
                .border(3.dp, Color.Gray, RoundedCornerShape(8.dp))
                .size(300.dp),



            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Yemek Adı
        Text(
            text = yemek.yemek_adi,
            //style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Yemek Fiyatı
        Text(
            text = "Fiyat: ${yemek.yemek_fiyat} ₺",
            //style = MaterialTheme.typography.h6,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        //
        Row(
            verticalAlignment = Alignment.CenterVertically)
        {

            Text(text = "Adet: ")

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { if (quantity > 1) quantity-- }) {
                Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "")
            }

            Spacer(modifier = Modifier.width(8.dp))
            
            Text(text = "$quantity",)

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { quantity++ }) {
                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "")


            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Toplam Fiyat: $totalPrice TL",
            //style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(80.dp))


        // Sepete Ekle Butonu
        Button(
            onClick = {
                yemeklerViewModel.sepeteYemekEkle(
                    yemek_adi,
                    yemek_resim_adi,
                    yemek_fiyat,
                    quantity,
                    kullanici_adi
                )
            },
            modifier = Modifier
                .padding(end = 32.dp, start = 32.dp)
                .fillMaxWidth(),

            colors = ButtonDefaults.buttonColors(Color.DarkGray)
        ) {
            Text(text = "Sepete Ekle", color = Color.White)

        }
    }
}
