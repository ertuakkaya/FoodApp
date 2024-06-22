package com.example.foodapp.data.repository

import android.util.Log
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.datasource.YemeklerDataSource
import com.example.foodapp.data.entitiy.CRUDCevap
import com.example.foodapp.data.entitiy.SepetYemeklerCevap
import com.example.foodapp.data.entitiy.YemeklerCevap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class YemeklerRepository @Inject constructor(var yemeklerDataSource: YemeklerDataSource){


    suspend fun tumYemekleriGetir() : Flow<ResourceState<YemeklerCevap>>{
        return flow {
            emit(ResourceState.Loading()) // başta loading göster

            val response = yemeklerDataSource.tumYemekleriGetir()

            if(response.isSuccessful && response.body() != null){
                emit(ResourceState.Success(response.body()!!))
            }else{
                emit(ResourceState.Error("Yemekler getirilirken hata oluştu"))

            }
        }.catch { e->
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow"))
        }
    }


    suspend fun sepeteYemekEkle( yemek_adi: String,
                                 yemek_resim_adi: String,
                                 yemek_fiyat: Int,
                                 yemek_siparis_adet: Int,
                                 kullanici_adi: String = "ertugrul") : Flow<ResourceState<CRUDCevap>>{
        return flow {
            emit(ResourceState.Loading()) // başta loading göster

            val response = yemeklerDataSource.sepeteYemekEkle( yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)

            if(response.isSuccessful && response.body() != null){
                emit(ResourceState.Success(response.body()!!))
            }else{
                emit(ResourceState.Error("Yemekler getirilirken hata oluştu"))

            }
        }.catch { e->
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow"))
        }
    }

    suspend fun sepettekiYemekleriGetir(kullanici_adi: String = "ertugrul") : Flow<ResourceState<SepetYemeklerCevap>>{
        return flow {
            emit(ResourceState.Loading()) // başta loading göster

            val response = yemeklerDataSource.sepettekiYemekleriGetir(kullanici_adi)

            if(response.isSuccessful && response.body() != null){
                emit(ResourceState.Success(response.body()!!))

            }else{
                emit(ResourceState.Error("Sepetteki Yemekler getirilirken hata oluştu"))

            }
        }.catch { e->
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow"))
        }
    }


    suspend fun sepettenYemekSil(sepet_yemek_id: Int, kullanici_adi: String = "ertugrul") : Flow<ResourceState<CRUDCevap>>{
        return flow {
            emit(ResourceState.Loading()) // başta loading göster

            val response = yemeklerDataSource.sepettenYemekSil(sepet_yemek_id, kullanici_adi)

            if(response.isSuccessful && response.body() != null){
                emit(ResourceState.Success(response.body()!!))

            }else{
                emit(ResourceState.Error("Sepetteki Yemekler getirilirken hata oluştu"))

            }
        }.catch { e->
            emit(ResourceState.Error(e?.localizedMessage?:"Some error in flow"))
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