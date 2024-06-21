package com.example.foodapp.retrofit

import com.example.foodapp.data.entitiy.CRUDCevap
import com.example.foodapp.data.entitiy.SepetYemeklerCevap
import com.example.foodapp.data.entitiy.YemeklerCevap
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface YemeklerDao {

    //  http://kasimadalan.pe.hu/yemekler/tumYemekleriGetir.php
    // Tüm yemekleri getir
    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun tumYemekleriGetir() : Response<YemeklerCevap>



    // Sepete yemek ekle
    //  http://kasimadalan.pe.hu/yemekler/sepeteYemekEkle.php
    @FormUrlEncoded
    @POST("yemekler/sepeteYemekEkle.php")
    suspend fun sepeteYemekEkle(
                                @Field("yemek_adi") yemek_adi: String,
                                @Field("yemek_resim_adi") yemek_resim_adi: String,
                                @Field("yemek_fiyat") yemek_fiyat: Int,
                                @Field("yemek_siparis_adet") yemek_siparis_adet: Int,
                                @Field("kullanici_adi") kullanici_adi: String = "ertugrul"
    ): CRUDCevap {
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
    suspend fun sepettenYemekSil(
                                 @Field("sepet_yemek_id") sepet_yemek_id: Int,
                                 @Field("kullanici_adi") kullanici_adi: String = "ertugrul"): CRUDCevap {


        TODO(
            "SEPETTEN YEMEK SİLERKEN sepet_yemek_id bizde var." +
                    " sadece id kısmını yazılacak ve post ile gönderilecek."
        )
    }



    // Sepetteki yemekleri getir
    // http://kasimadalan.pe.hu/yemekler/sepettekiYemekleriGetir.php
    @FormUrlEncoded
    @POST("yemekler/sepettekiYemekleriGetir.php")
    suspend fun sepettekiYemekleriGetir(
        @Field("kullanici_adi") kullanici_adi: String = "ertugrul"): SepetYemeklerCevap





}