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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodsViewModel @Inject constructor(private val foodsRepository: FoodsRepository) : ViewModel() {

    private val _foods : MutableStateFlow<ResourceState<FoodsResponse>> = MutableStateFlow(ResourceState.Loading())
    val foods : StateFlow<ResourceState<FoodsResponse>> = _foods


    private val _addFoodToCart : MutableStateFlow<ResourceState<CRUDResponse>> = MutableStateFlow(ResourceState.Loading())
    val addFoodToCart: StateFlow<ResourceState<CRUDResponse>> = _addFoodToCart



    init {
        getAllFoods()
    }

    private fun getAllFoods() {
        viewModelScope.launch (Dispatchers.IO){
            foodsRepository.getAllFoods().collectLatest {foodsResponse ->
                _foods.value = foodsResponse

            }
        }
    }

    fun addFoodToCart(food_name: String, food_image_name: String, food_price: Int, food_order_quantity: Int, user_name: String = "ertugrul"  ) {
        viewModelScope.launch (Dispatchers.IO){
            foodsRepository.addFoodToCart(food_name, food_image_name, food_price, food_order_quantity, user_name).collectLatest {crudResponse->
                _addFoodToCart.value = crudResponse

            }
        }
    }


    private val _cartFoods : MutableStateFlow<ResourceState<CartFoodsResponse>> = MutableStateFlow(ResourceState.Loading())
    val cartFoods : StateFlow<ResourceState<CartFoodsResponse>> = _cartFoods

    fun getCartFoods(user_name: String = "ertugrul") {
        viewModelScope.launch (Dispatchers.IO){
            foodsRepository.getCartFoods(user_name).collectLatest {cartFoodsResponse->
                 _cartFoods.value = cartFoodsResponse

            }
        }
    }

    private val _deleteFoodFromCart : MutableStateFlow<ResourceState<CRUDResponse>> = MutableStateFlow(ResourceState.Loading())
    val deleteFoodFromCart: StateFlow<ResourceState<CRUDResponse>> = _deleteFoodFromCart

    fun deleteFoodFromCart(cart_food_id: Int, user_name: String = "ertugrul") {
        viewModelScope.launch (Dispatchers.IO){
            foodsRepository.deleteFoodFromCart(cart_food_id, user_name).collectLatest {crudResponse->
                _deleteFoodFromCart.value = crudResponse
                getCartFoods() // refresh cart foods after deleting a food
            }
        }
    }

}