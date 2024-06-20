package com.example.foodapp.retrofit

import com.example.foodapp.data.entitiy.CRUDCevap
import com.example.foodapp.data.entitiy.SepetYemeklerCevap
import com.example.foodapp.data.entitiy.YemeklerCevap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface YemeklerDao {

    //  http://kasimadalan.pe.hu/yemekler/tumYemekleriGetir.php
    // Tüm yemekleri getir
    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun tumYemekleriGetir() : YemeklerCevap



    // Sepete yemek ekle
    //  http://kasimadalan.pe.hu/yemekler/sepeteYemekEkle.php
    @FormUrlEncoded
    @POST("yemekler/sepeteYemekEkle.php")
    suspend fun sepeteYemekEkle(
                                yemek_adi: String,
                                yemek_resim_adi: String,
                                yemek_fiyat: Int,
                                yemek_siparis_adet: Int,
                                kullanici_adi: String = "ertugrul"
                            ): CRUDCevap
    {
        TODO(
            "SEPETE YEMEK EKLERKEN yemek_adi ," +
                    " yemek_resim_adi ," +
                    " yemek_fiyatı bizde var." +
                    " sadece adet ve kullanici_adi kısmını yazılacak ve post ile gönderilecek."
        )
    }


    // Sepetten yemek
    // http://kasimadalan.pe.hu/yemekler/sepettenYemekSil.php
    @FormUrlEncoded
    @POST("yemekler/sepettenYemekSil.php")
    suspend fun sepettenYemekSil(sepet_yemek_id: Int,
                                 kullanici_adi: String = "ertugrul"): CRUDCevap {


        TODO(
            "SEPETTEN YEMEK SİLERKEN sepet_yemek_id bizde var." +
                    " sadece id kısmını yazılacak ve post ile gönderilecek."
        )
    }



    // Sepetteki yemekleri getir
    // http://kasimadalan.pe.hu/yemekler/sepettekiYemekleriGetir.php
    @FormUrlEncoded
    @POST("yemekler/sepettekiYemekleriGetir.php")
    suspend fun sepettekiYemekleriGetir(kullanici_adi: String = "ertugrul"): SepetYemeklerCevap





}