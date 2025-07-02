package com.example.foodapp.data.repository

import android.util.Log

import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.datasource.FoodsDataSource
import com.example.foodapp.data.entity.CRUDResponse
import com.example.foodapp.data.entity.CartFoodsResponse
import com.example.foodapp.data.entity.FoodsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

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
                emit(ResourceState.Error("Error when getting foods"))

            }
        }.catch { e->
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow"))
        }
    }


    suspend fun addFoodToCart( food_name: String,
                                 food_image_name: String,
                                 food_price: Int,
                                 food_order_quantity: Int,
                                 user_name: String = "ertugrul") : Flow<ResourceState<CRUDResponse>>{
        return flow {
            emit(ResourceState.Loading()) // show loading at first

            val response = foodsDataSource.addFoodToCart( food_name, food_image_name, food_price, food_order_quantity, user_name)

            if(response.isSuccessful && response.body() != null){
                emit(ResourceState.Success(response.body()!!))
            }else{
                emit(ResourceState.Error("Error when adding food to cart"))

            }
        }.catch { e->
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow"))
        }
    }

    suspend fun getCartFoods(user_name: String = "ertugrul") : Flow<ResourceState<CartFoodsResponse>>{
        return flow {
            emit(ResourceState.Loading()) // show loading at first

            val response = foodsDataSource.getCartFoods(user_name)

            if(response.isSuccessful && response.body() != null){
                emit(ResourceState.Success(response.body()!!))

            }else{
                emit(ResourceState.Error("Error when getting cart foods"))

            }
        }.catch { e->
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow"))
        }
    }


    suspend fun deleteFoodFromCart(cart_food_id: Int, user_name: String = "ertugrul") : Flow<ResourceState<CRUDResponse>>{
        return flow {
            emit(ResourceState.Loading()) // show loading at first

            val response = foodsDataSource.deleteFoodFromCart(cart_food_id, user_name)

            if(response.isSuccessful && response.body() != null){
                emit(ResourceState.Success(response.body()!!))

            }else{
                emit(ResourceState.Error("Error when deleting food from cart"))

            }
        }.catch { e->
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow"))
        }
    }
}