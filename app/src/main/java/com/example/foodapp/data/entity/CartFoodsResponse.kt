package com.example.foodapp.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CartFoodsResponse(
    @SerializedName("sepet_yemekler") var cart_foods : List<CartFood>,
    @SerializedName("success") var success :  Int
) : Serializable

data class CartFood(
    @SerializedName("sepet_yemek_id") var cart_food_id: Int,
    @SerializedName("yemek_adi") var food_name: String,
    @SerializedName("yemek_resim_adi") var food_image_name: String,
    @SerializedName("yemek_fiyat") var food_price: Int,
    @SerializedName("yemek_siparis_adet") var food_order_quantity: Int,
    @SerializedName("kullanici_adi") var user_name: String = ""
) : Serializable

