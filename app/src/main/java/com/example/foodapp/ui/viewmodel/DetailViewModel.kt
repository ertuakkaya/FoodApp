package com.example.foodapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entity.CRUDResponse
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
class DetailViewModel @Inject constructor(private val foodsRepository: FoodsRepository) : ViewModel() {

    private val _addFoodToCart = MutableStateFlow<ResourceState<CRUDResponse>>(ResourceState.Loading())
    val addFoodToCart: StateFlow<ResourceState<CRUDResponse>> = _addFoodToCart.asStateFlow()

    fun addFoodToCart(food_name: String, food_image_name: String, food_price: Int, food_order_quantity: Int, user_name: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            foodsRepository.addFoodToCart(food_name, food_image_name, food_price, food_order_quantity, user_name).collectLatest { crudResponse ->
                _addFoodToCart.value = crudResponse
            }
        }
    }
    
    fun clearAddToCartState() {
        _addFoodToCart.value = ResourceState.Loading()
    }
}
