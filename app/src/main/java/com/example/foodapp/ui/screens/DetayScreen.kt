package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.foodapp.data.entitiy.SepetYemekler
import com.example.foodapp.data.entitiy.Yemekler

@Composable
fun DetayScreen(yemek: SepetYemekler, onAddToCart: (Int) -> Unit) {
    var quantity = yemek.yemek_siparis_adet

    val totalPrice = yemek.yemek_fiyat * quantity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = yemek.yemek_adi,
            //style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Fiyat: ${yemek.yemek_fiyat} ₺",
            //style = MaterialTheme.typography.h6,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Adet: ",)
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { if (quantity > 1) quantity-- }) {
                Text(text = "-")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "$quantity",)
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { quantity++ }) {
                Text(text = "+")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Toplam Fiyat: $totalPrice TL",
            //style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAddToCart(quantity) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color.Red)
        ) {
            Text(text = "Sepete Ekle", color = Color.White)
        }
    }
}
