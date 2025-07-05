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

/**
 * ViewModel for the Home screen that manages food listings and cart operations.
 * 
 * This ViewModel handles:
 * - Loading and displaying all available foods
 * - Managing shopping cart operations (view, add, remove items)
 * - Handling loading states and error conditions
 * - Providing reactive data streams to the UI
 * 
 * @property foodsRepository Repository for food-related data operations
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val foodsRepository: FoodsRepository) : ViewModel() {

    /**
     * StateFlow containing the current state of foods data.
     * Emits Loading, Success, or Error states based on API response.
     */
    private val _foods = MutableStateFlow<ResourceState<FoodsResponse>>(ResourceState.Loading())
    val foods: StateFlow<ResourceState<FoodsResponse>> = _foods.asStateFlow()

    init {
        getAllFoods()
    }

    /**
     * Fetches all available foods from the repository.
     * Automatically called during ViewModel initialization.
     */
    private fun getAllFoods() {
        viewModelScope.launch(Dispatchers.IO) {
            foodsRepository.getAllFoods().collectLatest { foodsResponse ->
                _foods.value = foodsResponse
            }
        }
    }
    
    /**
     * Refreshes the foods list by making a new API call.
     * Used for pull-to-refresh functionality.
     */
    fun refreshFoods() {
        getAllFoods()
    }

    /**
     * StateFlow containing the current state of cart foods data.
     * Emits Loading, Success, or Error states based on API response.
     */
    private val _cartFoods = MutableStateFlow<ResourceState<CartFoodsResponse>>(ResourceState.Loading())
    val cartFoods: StateFlow<ResourceState<CartFoodsResponse>> = _cartFoods.asStateFlow()

    /**
     * Fetches cart items for the specified user.
     * 
     * @param user_name Username to fetch cart items for (empty string for guest users)
     */
    fun getCartFoods(user_name: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            foodsRepository.getCartFoods(user_name).collectLatest { cartFoodsResponse ->
                _cartFoods.value = cartFoodsResponse
            }
        }
    }
    
    /**
     * Refreshes the cart items for the specified user.
     * Used for pull-to-refresh functionality on cart screen.
     * 
     * @param user_name Username to refresh cart items for
     */
    fun refreshCartFoods(user_name: String = "") {
        getCartFoods(user_name)
    }

    /**
     * StateFlow containing the current state of delete food from cart operation.
     * Emits Loading, Success, or Error states based on API response.
     */
    private val _deleteFoodFromCart = MutableStateFlow<ResourceState<CRUDResponse>>(ResourceState.Loading())
    val deleteFoodFromCart: StateFlow<ResourceState<CRUDResponse>> = _deleteFoodFromCart.asStateFlow()

    /**
     * Removes a food item from the user's cart.
     * Automatically refreshes the cart after successful deletion.
     * 
     * @param cart_food_id Unique identifier of the cart item to remove
     * @param user_name Username of the cart owner (empty string for guest users)
     */
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
    
    /**
     * Resets the delete operation state to Loading.
     * Used to clear previous delete operation results from the UI.
     */
    fun clearDeleteState() {
        _deleteFoodFromCart.value = ResourceState.Loading()
    }
}