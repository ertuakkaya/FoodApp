package com.example.foodapp.data.entitiy

import java.io.Serializable

data class YemeklerCevap(var yemekler : List<Yemekler>,
                         var success :  Int ) : Serializable

data class Yemekler(
                    var yemek_id: Int,
                    var yemek_adi: String,
                    var yemek_resim_adi: String,
                    var yemek_fiyat: Int) : Serializable