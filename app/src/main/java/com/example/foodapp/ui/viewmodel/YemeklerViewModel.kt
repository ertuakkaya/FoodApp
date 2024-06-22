package com.example.foodapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.ResourceState
import com.example.foodapp.data.entitiy.CRUDCevap
import com.example.foodapp.data.entitiy.SepetYemeklerCevap
import com.example.foodapp.data.entitiy.YemeklerCevap
import com.example.foodapp.data.repository.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YemeklerViewModel @Inject constructor(private val yemeklerRepository: YemeklerRepository) : ViewModel() {

    /*
    private val _yemekler = MutableLiveData<YemeklerCevap?>()
    val yemekler: LiveData<YemeklerCevap?> = _yemekler

    private val _sepetYemekler = MutableLiveData<SepetYemeklerCevap?>()
    val sepetYemekler: LiveData<SepetYemeklerCevap?> = _sepetYemekler
    */

    private val _yemekler : MutableStateFlow<ResourceState<YemeklerCevap>> = MutableStateFlow(ResourceState.Loading())
    val yemekler : StateFlow<ResourceState<YemeklerCevap>> = _yemekler


    private val _sepeteYemekEkle : MutableStateFlow<ResourceState<CRUDCevap>> = MutableStateFlow(ResourceState.Loading())
    val sepeteYemekEkle: StateFlow<ResourceState<CRUDCevap>> = _sepeteYemekEkle

    /*
    private val _sepetYemekler : MutableStateFlow<ResourceState<SepetYemeklerCevap>> = MutableStateFlow(ResourceState.Loading())
    val sepetYemekler : StateFlow<ResourceState<SepetYemeklerCevap>> = _sepetYemekler
    */

    /*
    fun tumYemekleriGetir() = liveData(Dispatchers.IO) {
        emit(yemeklerRepository.tumYemekleriGetir())
    }

    fun sepeteYemekEkle(yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int) = liveData(Dispatchers.IO) {
        emit(yemeklerRepository.sepeteYemekEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet))
    }

    fun sepettenYemekSil(sepet_yemek_id: Int) = liveData(Dispatchers.IO) {
        emit(yemeklerRepository.sepettenYemekSil(sepet_yemek_id))
    }

    fun sepettekiYemekleriGetir() = liveData(Dispatchers.IO) {
        emit(yemeklerRepository.sepettekiYemekleriGetir())
    }
    */

    init {
        tumYemekleriGetir()
        sepettekiYemekleriGetir()
    }

    private fun tumYemekleriGetir() {
        viewModelScope.launch (Dispatchers.IO){
            yemeklerRepository.tumYemekleriGetir().collectLatest {yemeklerCevap ->
                _yemekler.value = yemeklerCevap

            }
        }
    }

    fun sepeteYemekEkle(yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int, kullanici_adi: String = "ertugrul"  ) {
        viewModelScope.launch (Dispatchers.IO){
            yemeklerRepository.sepeteYemekEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi).collectLatest {crudCevap->
                _sepeteYemekEkle.value = crudCevap

            }
        }
    }


    private val _sepetYemekler : MutableStateFlow<ResourceState<SepetYemeklerCevap>> = MutableStateFlow(ResourceState.Loading())
    val sepetYemekler : StateFlow<ResourceState<SepetYemeklerCevap>> = _sepetYemekler

    fun sepettekiYemekleriGetir(kullanici_adi: String = "ertugrul") {
        viewModelScope.launch (Dispatchers.IO){
            yemeklerRepository.sepettekiYemekleriGetir(kullanici_adi).collectLatest {sepetYemeklerCevap->
                 _sepetYemekler.value = sepetYemeklerCevap

            }
        }
    }

    private val _sepettenYemekSil : MutableStateFlow<ResourceState<CRUDCevap>> = MutableStateFlow(ResourceState.Loading())
    val sepettenYemekSil: StateFlow<ResourceState<CRUDCevap>> = _sepettenYemekSil

    fun sepettenYemekSil(sepet_yemek_id: Int, kullanici_adi: String = "ertugrul") {
        viewModelScope.launch (Dispatchers.IO){
            yemeklerRepository.sepettenYemekSil(sepet_yemek_id, kullanici_adi).collectLatest {crudCevap->
                _sepettenYemekSil.value = crudCevap

            }
        }
    }


    /*
    fun tumYemekleriGetir() {
        viewModelScope.launch {
            val response = yemeklerRepository.tumYemekleriGetir()
            _yemekler.postValue(response)
        }
    }

    fun sepeteYemekEkle(yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int) {
        viewModelScope.launch {
            yemeklerRepository.sepeteYemekEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet)
            sepettekiYemekleriGetir()
        }
    }

    fun sepettenYemekSil(sepet_yemek_id: Int) {
        viewModelScope.launch {
            yemeklerRepository.sepettenYemekSil(sepet_yemek_id)
            sepettekiYemekleriGetir()
        }
    }

    fun sepettekiYemekleriGetir() {
        viewModelScope.launch {
            val response = yemeklerRepository.sepettekiYemekleriGetir()
            _sepetYemekler.postValue(response)
        }
    }
    */
}