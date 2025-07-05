package com.example.foodapp.data.repository

import android.util.Log
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.datasource.FoodsDataSource
import com.example.foodapp.data.entity.CRUDResponse
import com.example.foodapp.data.entity.CartFoodsResponse
import com.example.foodapp.data.entity.FoodsResponse
import com.example.foodapp.data.error.ErrorType
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Repository class that handles food-related data operations.
 * 
 * This repository acts as a single source of truth for food data,
 * abstracting the data sources from the ViewModels and providing
 * a clean API for data operations.
 * 
 * Features:
 * - Centralized error handling with ResourceState wrapper
 * - Network error categorization and recovery
 * - Reactive data streams using Kotlin Flow
 * - Automatic data transformation and validation
 * 
 * @property foodsDataSource Data source for remote food API operations
 */
class FoodsRepository @Inject constructor(var foodsDataSource: FoodsDataSource){


    suspend fun getAllFoods() : Flow<ResourceState<FoodsResponse>>{
        return flow {
            emit(ResourceState.Loading()) // show loading at first

            val response = foodsDataSource.getAllFoods()

            if(response.isSuccessful && response.body() != null){
                Log.d("FoodsRepository", "Raw response body: ${response.body()}")
                emit(ResourceState.Success(response.body()!!))
            }else{
                Log.e("FoodsRepository", "Error response: ${response.errorBody()?.string()}")
                val errorType = when(response.code()) {
                    in 400..499 -> ErrorType.ApiError(response.code(), "Client error")
                    in 500..599 -> ErrorType.ServerError
                    else -> ErrorType.UnknownError
                }
                emit(ResourceState.Error("Error when getting foods", errorType))
            }
        }.catch { e->
            val errorType = when(e) {
                is IOException, is SocketTimeoutException -> ErrorType.NetworkError
                else -> ErrorType.UnknownError
            }
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow", errorType))
        }
    }


    suspend fun addFoodToCart( food_name: String,
                                 food_image_name: String,
                                 food_price: Int,
                                 food_order_quantity: Int,
                                 user_name: String = "") : Flow<ResourceState<CRUDResponse>>{
        return flow {
            emit(ResourceState.Loading()) // show loading at first

            val response = foodsDataSource.addFoodToCart( food_name, food_image_name, food_price, food_order_quantity, user_name)

            if(response.isSuccessful && response.body() != null){
                emit(ResourceState.Success(response.body()!!))
            }else{
                val errorType = when(response.code()) {
                    in 400..499 -> ErrorType.ApiError(response.code(), "Client error")
                    in 500..599 -> ErrorType.ServerError
                    else -> ErrorType.UnknownError
                }
                emit(ResourceState.Error("Error when adding food to cart", errorType))
            }
        }.catch { e->
            val errorType = when(e) {
                is IOException, is SocketTimeoutException -> ErrorType.NetworkError
                else -> ErrorType.UnknownError
            }
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow", errorType))
        }
    }

    suspend fun getCartFoods(user_name: String = "") : Flow<ResourceState<CartFoodsResponse>>{
        return flow {
            emit(ResourceState.Loading()) // show loading at first

            val response = foodsDataSource.getCartFoods(user_name)

            if(response.isSuccessful && response.body() != null){
                emit(ResourceState.Success(response.body()!!))
            }else{
                val errorType = when(response.code()) {
                    in 400..499 -> ErrorType.ApiError(response.code(), "Client error")
                    in 500..599 -> ErrorType.ServerError
                    else -> ErrorType.UnknownError
                }
                emit(ResourceState.Error("Error when getting cart foods", errorType))
            }
        }.catch { e->
            val errorType = when(e) {
                is IOException, is SocketTimeoutException -> ErrorType.NetworkError
                else -> ErrorType.UnknownError
            }
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow", errorType))
        }
    }


    suspend fun deleteFoodFromCart(cart_food_id: Int, user_name: String = "") : Flow<ResourceState<CRUDResponse>>{
        return flow {
            emit(ResourceState.Loading()) // show loading at first

            val response = foodsDataSource.deleteFoodFromCart(cart_food_id, user_name)

            if(response.isSuccessful && response.body() != null){
                emit(ResourceState.Success(response.body()!!))
            }else{
                val errorType = when(response.code()) {
                    in 400..499 -> ErrorType.ApiError(response.code(), "Client error")
                    in 500..599 -> ErrorType.ServerError
                    else -> ErrorType.UnknownError
                }
                emit(ResourceState.Error("Error when deleting food from cart", errorType))
            }
        }.catch { e->
            val errorType = when(e) {
                is IOException, is SocketTimeoutException -> ErrorType.NetworkError
                else -> ErrorType.UnknownError
            }
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow", errorType))
        }
    }
}