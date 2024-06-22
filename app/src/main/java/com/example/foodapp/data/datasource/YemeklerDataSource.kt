package com.example.foodapp.data.datasource

import com.example.foodapp.data.entitiy.CRUDCevap
import com.example.foodapp.data.entitiy.SepetYemekler
import com.example.foodapp.data.entitiy.Yemekler
import com.example.foodapp.data.entitiy.YemeklerCevap
import com.example.foodapp.retrofit.YemeklerDao
import retrofit2.Response
import javax.inject.Inject


class YemeklerDataSource @Inject constructor(var yemeklerDao : YemeklerDao) {

    suspend fun tumYemekleriGetir() : Response<YemeklerCevap>{
        return yemeklerDao.tumYemekleriGetir()
    }


    suspend fun sepeteYemekEkle(
                                yemek_adi: String,
                                yemek_resim_adi: String,
                                yemek_fiyat: Int,
                                yemek_siparis_adet: Int,
                                kullanici_adi: String = "ertugrul"
    ): Response<CRUDCevap> {
        return yemeklerDao.sepeteYemekEkle(
            yemek_adi,
            yemek_resim_adi,
            yemek_fiyat,
            yemek_siparis_adet,
            kullanici_adi
        )
    }


    /*


     suspend fun tumYemekleriGetir() : List<Yemekler>{
        return yemeklerDao.tumYemekleriGetir().yemekler
    }






    suspend fun sepeteYemekEkle(
                                yemek_adi: String,
                                yemek_resim_adi: String,
                                yemek_fiyat: Int,
                                yemek_siparis_adet: Int,
                                kullanici_adi: String = "ertugrul"
    ){
        yemeklerDao.sepeteYemekEkle(
            yemek_adi,
            yemek_resim_adi,
            yemek_fiyat,
            yemek_siparis_adet,
            kullanici_adi
        )
    }

    suspend fun sepettenYemekSil(sepet_yemek_id: Int,
                                 kullanici_adi: String = "ertugrul"){
        yemeklerDao.sepettenYemekSil(sepet_yemek_id,kullanici_adi)
    }

    suspend fun sepettekiYemekleriGetir(kullanici_adi: String = "ertugrul"): List<SepetYemekler>{
        return yemeklerDao.sepettekiYemekleriGetir(kullanici_adi).sepet_yemekler
    }

    */


}