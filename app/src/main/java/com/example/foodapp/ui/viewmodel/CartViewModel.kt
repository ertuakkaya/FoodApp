package com.example.foodapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entity.CRUDResponse
import com.example.foodapp.data.entity.CartFoodsResponse
import com.example.foodapp.data.repository.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val foodsRepository: FoodsRepository) : ViewModel() {

    private val _cartFoods = MutableStateFlow<ResourceState<CartFoodsResponse>>(ResourceState.Loading())
    val cartFoods: StateFlow<ResourceState<CartFoodsResponse>> = _cartFoods.asStateFlow()

    fun getCartFoods(user_name: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            foodsRepository.getCartFoods(user_name).collectLatest { cartFoodsResponse ->
                _cartFoods.value = cartFoodsResponse
            }
        }
    }

    fun refreshCartFoods(user_name: String = "") {
        getCartFoods(user_name)
    }

    private val _deleteFoodFromCart = MutableStateFlow<ResourceState<CRUDResponse>>(ResourceState.Loading())
    val deleteFoodFromCart: StateFlow<ResourceState<CRUDResponse>> = _deleteFoodFromCart.asStateFlow()

    fun deleteFoodFromCart(cart_food_id: Int, user_name: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            foodsRepository.deleteFoodFromCart(cart_food_id, user_name).collectLatest { crudResponse ->
                _deleteFoodFromCart.value = crudResponse
                if (crudResponse is ResourceState.Success) {
                    getCartFoods(user_name)
                }
            }
        }
    }

    fun clearDeleteState() {
        _deleteFoodFromCart.value = ResourceState.Loading()
    }

    private val _addFoodToCart = MutableStateFlow<ResourceState<CRUDResponse>>(ResourceState.Loading())
    val addFoodToCart: StateFlow<ResourceState<CRUDResponse>> = _addFoodToCart.asStateFlow()

    fun addFoodToCart(
        food_name: String,
        food_image_name: String,
        food_price: Int,
        food_order_quantity: Int,
        user_name: String = ""
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            foodsRepository.addFoodToCart(
                food_name,
                food_image_name,
                food_price,
                food_order_quantity,
                user_name
            ).collectLatest { crudResponse ->
                _addFoodToCart.value = crudResponse
                if (crudResponse is ResourceState.Success) {
                    getCartFoods(user_name)
                }
            }
        }
    }

    private val _checkoutState = MutableStateFlow<ResourceState<Unit>>(ResourceState.Loading())
    val checkoutState: StateFlow<ResourceState<Unit>> = _checkoutState.asStateFlow()

    fun checkout(user_name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _checkoutState.value = ResourceState.Loading()
            try {
                val cartResponse = cartFoods.value
                if (cartResponse is ResourceState.Success) {
                    cartResponse.data.cart_foods.forEach { cartFood ->
                        foodsRepository.deleteFoodFromCart(cartFood.cart_food_id, user_name).collectLatest { }
                    }
                    getCartFoods(user_name)
                    _checkoutState.value = ResourceState.Success(Unit)
                } else {
                    _checkoutState.value = ResourceState.Error("Cart is empty or not loaded.")
                }
            } catch (e: Exception) {
                _checkoutState.value = ResourceState.Error(e.message ?: "An unknown error occurred during checkout.")
            }
        }
    }

    fun resetCheckoutState() {
        _checkoutState.value = ResourceState.Loading()
    }
}