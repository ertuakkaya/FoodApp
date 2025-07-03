package com.example.foodapp.data.datasource

import com.example.foodapp.data.entity.CRUDResponse
import com.example.foodapp.data.entity.CartFoodsResponse
import com.example.foodapp.data.entity.FoodsResponse
import com.example.foodapp.domain.service.UserService
import com.example.foodapp.retrofit.FoodsDao
import retrofit2.Response
import javax.inject.Inject


class FoodsDataSource @Inject constructor(
    private val foodsDao: FoodsDao,
    private val userService: UserService
) {

    suspend fun getAllFoods() : Response<FoodsResponse>{
        return foodsDao.getAllFoods()
    }


    suspend fun addFoodToCart(
        food_name: String,
        food_image_name: String,
        food_price: Int,
        food_order_quantity: Int,
        user_name: String = ""
    ): Response<CRUDResponse> {
        val currentUserName = if (user_name.isNotEmpty()) user_name else userService.getCurrentUserName()
        return foodsDao.addFoodToCart(
            food_name,
            food_image_name,
            food_price,
            food_order_quantity,
            user_name = currentUserName
        )
    }


    suspend fun getCartFoods(user_name: String = ""): Response<CartFoodsResponse>{
        val currentUserName = if (user_name.isNotEmpty()) user_name else userService.getCurrentUserName()
        return foodsDao.getCartFoods(user_name = currentUserName)
    }


    suspend fun deleteFoodFromCart(cart_food_id: Int,
                                 user_name: String = ""): Response<CRUDResponse>{
        val currentUserName = if (user_name.isNotEmpty()) user_name else userService.getCurrentUserName()
        return foodsDao.deleteFoodFromCart(cart_food_id, user_name = currentUserName)
    }
}