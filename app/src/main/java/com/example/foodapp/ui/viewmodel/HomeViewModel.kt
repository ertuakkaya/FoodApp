package com.example.foodapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entity.CRUDResponse
import com.example.foodapp.data.entity.CartFoodsResponse
import com.example.foodapp.data.entity.FoodsResponse
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
class HomeViewModel @Inject constructor(private val foodsRepository: FoodsRepository) : ViewModel() {

    private val _foods = MutableStateFlow<ResourceState<FoodsResponse>>(ResourceState.Loading())
    val foods: StateFlow<ResourceState<FoodsResponse>> = _foods.asStateFlow()

    init {
        getAllFoods()
    }

    private fun getAllFoods() {
        viewModelScope.launch(Dispatchers.IO) {
            foodsRepository.getAllFoods().collectLatest { foodsResponse ->
                _foods.value = foodsResponse
            }
        }
    }
    
    fun refreshFoods() {
        getAllFoods()
    }

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
                // Refresh cart foods after successful deletion
                if (crudResponse is ResourceState.Success) {
                    getCartFoods(user_name)
                }
            }
        }
    }
    
    fun clearDeleteState() {
        _deleteFoodFromCart.value = ResourceState.Loading()
    }
}