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
}