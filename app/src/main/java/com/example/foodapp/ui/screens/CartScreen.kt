package com.example.foodapp.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodapp.R
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entity.CartFood
import com.example.foodapp.data.entity.CartFoodsResponse
import com.example.foodapp.ui.components.SuccessPopup
import com.example.foodapp.ui.viewmodel.AuthState
import com.example.foodapp.ui.viewmodel.AuthViewModel
import com.example.foodapp.ui.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val cartFoodsResponse by cartViewModel.cartFoods.collectAsState()
    val userName = authViewModel.getUserName()
    val checkoutState by cartViewModel.checkoutState.collectAsState()
    var showSuccessPopup by remember { mutableStateOf(false) }

    LaunchedEffect(userName) {
        if (userName.isNotEmpty()) {
            cartViewModel.getCartFoods(userName)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            if (cartFoodsResponse is ResourceState.Success) {
                val response = (cartFoodsResponse as ResourceState.Success<CartFoodsResponse>).data
                if (response.cart_foods.isNotEmpty()) {
                    val aggregatedFoods = response.cart_foods.groupBy { it.food_name }
                        .map { (_, foodList) ->
                            foodList.first().copy(food_order_quantity = foodList.sumOf { it.food_order_quantity })
                        }
                    CartBottomBar(totalPrice = aggregatedFoods.sumOf { it.food_price * it.food_order_quantity }) {
                        if (userName.isNotEmpty()) {
                            cartViewModel.checkout(userName)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (cartFoodsResponse) {
                is ResourceState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ResourceState.Success -> {
                    val response = (cartFoodsResponse as ResourceState.Success<CartFoodsResponse>).data
                    if (response.cart_foods.isNotEmpty()) {
                        val aggregatedFoods = response.cart_foods.groupBy { it.food_name }
                            .map { (_, foodList) ->
                                foodList.first().copy(food_order_quantity = foodList.sumOf { it.food_order_quantity })
                            }
                        CartList(
                            foods = aggregatedFoods,
                            cartViewModel = cartViewModel,
                            authViewModel = authViewModel
                        )
                    } else {
                        EmptyCartView(navController = navController)
                    }
                }
                is ResourceState.Error -> {
                    EmptyCartView(navController = navController)
                }
            }
        }
    }

    // Handle Checkout State
    LaunchedEffect(checkoutState) {
        if (checkoutState is ResourceState.Success) {
            showSuccessPopup = true
            cartViewModel.resetCheckoutState() // Reset state to avoid re-triggering
        }
    }

    if (showSuccessPopup) {
        SuccessPopup(isVisible = showSuccessPopup, message = "Checkout successful!") {
            showSuccessPopup = false
            navController.navigate("home") {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }


    // Handle Auth State
    val authState by authViewModel.authState.collectAsState()
    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }
}

@Composable
fun CartBottomBar(totalPrice: Int, onCheckoutClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 8.dp) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Total Price", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "$totalPrice ₺",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = onCheckoutClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(50.dp)
            ) {
                Text(text = "Checkout", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun EmptyCartView(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_card))
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(250.dp),
            iterations = LottieConstants.IterateForever,
            reverseOnRepeat = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your cart is empty.",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Looks like you haven't added anything to your cart yet.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate("home") },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Start Shopping")
        }
    }
}

@Composable
fun CartList(
    foods: List<CartFood>,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(foods.size) { index ->
            CartCard(
                food = foods[index],
                cartViewModel = cartViewModel,
                authViewModel = authViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartCard(
    food: CartFood,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val userName = authViewModel.getUserName()

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "http://kasimadalan.pe.hu/yemekler/resimler/${food.food_image_name}",
                contentDescription = food.food_name,
                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = food.food_name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${food.food_price} ₺",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            if (userName.isNotEmpty()) {
                                // API does not support decreasing quantity, so we delete one instance
                                cartViewModel.deleteFoodFromCart(food.cart_food_id, userName)
                            }
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_remove),
                            contentDescription = "Decrease Quantity",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "${food.food_order_quantity}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = {
                            if (userName.isNotEmpty()) {
                                cartViewModel.addFoodToCart(
                                    food_name = food.food_name,
                                    food_image_name = food.food_image_name,
                                    food_price = food.food_price,
                                    food_order_quantity = 1, // Add one instance
                                    user_name = userName
                                )
                            }
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Increase Quantity",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            if(userName.isNotEmpty()){
                                // This will delete all items of this kind from the cart
                                cartViewModel.deleteFoodFromCart(food.cart_food_id, userName)
                            }
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete from cart",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                Text(
                    text = "${food.food_price * food.food_order_quantity} ₺",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
