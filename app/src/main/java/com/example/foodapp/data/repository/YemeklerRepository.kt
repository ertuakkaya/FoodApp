package com.example.foodapp.data.repository

import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.ResourceStateSepeteEkle
import com.example.foodapp.data.datasource.YemeklerDataSource
import com.example.foodapp.data.entitiy.CRUDCevap
import com.example.foodapp.data.entitiy.SepetYemeklerCevap
import com.example.foodapp.data.entitiy.YemeklerCevap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class YemeklerRepository @Inject constructor(var yemeklerDataSource: YemeklerDataSource) {


    suspend fun tumYemekleriGetir(): Flow<ResourceState<YemeklerCevap>> {
        return flow {
            emit(ResourceState.Loading()) // başta loading göster

            val response = yemeklerDataSource.tumYemekleriGetir()

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                emit(ResourceState.Error("Yemekler getirilirken hata oluştu"))

            }
        }.catch { e ->
            emit(
                ResourceState.Error(
                    e?.localizedMessage ?: "Some error in tumYemekleriGetir() flow"
                )
            )
        }
    }

    suspend fun sepeteYemekEkle(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String = "ertugrul"
    ): Flow<ResourceStateSepeteEkle<CRUDCevap>> {
        return flow {
            emit(ResourceStateSepeteEkle.Loading())

            val response = yemeklerDataSource.sepeteYemekEkle(
                yemek_adi,
                yemek_resim_adi,
                yemek_fiyat,
                yemek_siparis_adet,
                kullanici_adi
            )

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceStateSepeteEkle.Success(response.body()!!))
            } else {
                emit(ResourceStateSepeteEkle.Error("Sepete yemek eklenirken hata oluştu"))

            }


        }.catch { e ->
            emit(ResourceStateSepeteEkle.Error(e?.localizedMessage ?: "Some error in sepeteYemekEkle flow"))

        }
    }





    /*

   suspend fun tumYemekleriGetir() : Flow<ResourceState<YemeklerCevap>>{
        yemeklerDataSource.tumYemekleriGetir()

    }




    suspend fun sepeteYemekEkle(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String = "ertugrul"
    ) : Flow<ResourceState<SepetYemeklerCevap>>{
        yemeklerDataSource.sepeteYemekEkle(
            yemek_adi,
            yemek_resim_adi,
            yemek_fiyat,
            yemek_siparis_adet,
            kullanici_adi
        )
    }


    suspend fun sepettenYemekSil(sepet_yemek_id: Int, kullanici_adi: String = "ertugrul") =
        yemeklerDataSource.sepettenYemekSil(sepet_yemek_id, kullanici_adi)


    suspend fun sepettekiYemekleriGetir(kullanici_adi: String = "ertugrul") =
        yemeklerDataSource.sepettekiYemekleriGetir(kullanici_adi)
    */

}
