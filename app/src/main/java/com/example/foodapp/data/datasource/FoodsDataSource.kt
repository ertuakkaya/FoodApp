package com.example.foodapp.data.datasource

import com.example.foodapp.data.entity.CRUDResponse
import com.example.foodapp.data.entity.CartFoodsResponse
import com.example.foodapp.data.entity.FoodsResponse
import com.example.foodapp.retrofit.FoodsDao
import com.example.foodapp.ui.viewmodel.AuthViewModel
import retrofit2.Response
import javax.inject.Inject


class FoodsDataSource @Inject constructor(var foodsDao : FoodsDao) {

    val authViewModel = AuthViewModel()

    suspend fun getAllFoods() : Response<FoodsResponse>{
        return foodsDao.getAllFoods()
    }


    suspend fun addFoodToCart(
        food_name: String,
        food_image_name: String,
        food_price: Int,
        food_order_quantity: Int,
        user_name: String = "ertugrul"
    ): Response<CRUDResponse> {
        return foodsDao.addFoodToCart(
            food_name,
            food_image_name,
            food_price,
            food_order_quantity,
            user_name = authViewModel.getUserName()
        )
    }


    suspend fun getCartFoods(user_name: String = "ertugrul"): Response<CartFoodsResponse>{
        return foodsDao.getCartFoods(user_name = authViewModel.getUserName())
    }


    suspend fun deleteFoodFromCart(cart_food_id: Int,
                                 user_name: String = "ertugrul"): Response<CRUDResponse>{
        return foodsDao.deleteFoodFromCart(cart_food_id, user_name = authViewModel.getUserName())
    }
}