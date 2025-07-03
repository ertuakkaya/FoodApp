package com.example.foodapp.retrofit

import com.example.foodapp.data.entity.CRUDResponse
import com.example.foodapp.data.entity.CartFoodsResponse
import com.example.foodapp.data.entity.FoodsResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodsDao {

    //  http://kasimadalan.pe.hu/yemekler/tumYemekleriGetir.php
    // Get all foods
    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun getAllFoods() : Response<FoodsResponse>



    // Add food to cart
    //  http://kasimadalan.pe.hu/yemekler/sepeteYemekEkle.php
    @FormUrlEncoded
    @POST("yemekler/sepeteYemekEkle.php")
    suspend fun addFoodToCart(
                                @Field("yemek_adi") food_name: String,
                                @Field("yemek_resim_adi") food_image_name: String,
                                @Field("yemek_fiyat") food_price: Int,
                                @Field("yemek_siparis_adet") food_order_quantity: Int,
                                @Field("kullanici_adi") user_name: String
    ): Response<CRUDResponse>


    // Delete food from cart
    // http://kasimadalan.pe.hu/yemekler/sepettenYemekSil.php
    @FormUrlEncoded
    @POST("yemekler/sepettenYemekSil.php")
    suspend fun deleteFoodFromCart(
                                 @Field("sepet_yemek_id") cart_food_id: Int,
                                 @Field("kullanici_adi") user_name: String): Response<CRUDResponse>



    // Get cart foods
    // http://kasimadalan.pe.hu/yemekler/sepettekiYemekleriGetir.php
    @FormUrlEncoded
    @POST("yemekler/sepettekiYemekleriGetir.php")
    suspend fun getCartFoods(
        @Field("kullanici_adi") user_name: String): Response<CartFoodsResponse>

}