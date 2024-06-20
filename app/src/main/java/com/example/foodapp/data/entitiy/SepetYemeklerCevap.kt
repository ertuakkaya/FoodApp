package com.example.foodapp.data.entitiy

import java.io.Serializable

data class SepetYemeklerCevap(var sepet_yemekler : List<SepetYemekler>,
                              var success :  Int ) : Serializable

data class SepetYemekler(
                            var sepet_yemek_id: Int,
                            var yemek_adi: String,
                            var yemek_resim_adi: String,
                            var yemek_fiyat: Int,
                            var yemek_siparis_adet: Int,
                            var kullanici_adi: String = "ertugrul") : Serializable

