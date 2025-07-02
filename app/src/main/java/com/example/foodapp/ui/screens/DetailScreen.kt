package com.example.foodapp.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodapp.data.entity.CartFood
import com.example.foodapp.ui.viewmodel.AuthViewModel
import com.example.foodapp.ui.viewmodel.FoodsViewModel

@Composable
fun DetailScreen(food: CartFood, foodsViewModel: FoodsViewModel = hiltViewModel() ,navController: NavController,authViewModel: AuthViewModel) {
    var foodId = food.cart_food_id
    var foodName = food.food_name
    var foodImageName = food.food_image_name
    var foodPrice = food.food_price
    var userName = authViewModel.getUserName()


    var quantity by remember {
        mutableStateOf(food.food_order_quantity)
    }

    val totalPrice = food.food_price * quantity

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
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = "Details",
            style = MaterialTheme.typography.titleLarge, // Or another appropriate style
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
        )

        Spacer(modifier = Modifier.height(66.dp))

        AsyncImage(
            model = "http://kasimadalan.pe.hu/yemekler/resimler/${food.food_image_name}",
            contentDescription = food.food_name,
            modifier = Modifier
                .size(300.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = food.food_name,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Price: ${food.food_price} ₺",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically)
        {

            Text(text = "Quantity: ")

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { if (quantity > 1) quantity-- }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "")
            }

            Spacer(modifier = Modifier.width(8.dp))
            
            Text(text = "$quantity",)

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { quantity++ }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Total: $totalPrice ₺",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(80.dp))

        Button(
            onClick = {
                foodsViewModel.addFoodToCart(
                    foodName,
                    foodImageName,
                    foodPrice,
                    quantity,
                    authViewModel.getUserName()
                )
            },
            modifier = Modifier
                .padding(end = 32.dp, start = 32.dp)
                .fillMaxWidth(),

            colors = ButtonDefaults.buttonColors(Color.DarkGray)
        ) {
            Text(text = "Add to Cart", color = Color.White)

        }
    }
}
