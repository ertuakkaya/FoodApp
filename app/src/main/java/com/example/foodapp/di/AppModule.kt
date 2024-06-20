package com.example.foodapp.di

import com.example.foodapp.data.datasource.YemeklerDataSource
import com.example.foodapp.data.repository.YemeklerRepository
import com.example.foodapp.retrofit.ApiUtils
import com.example.foodapp.retrofit.YemeklerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideYemeklerDao() : YemeklerDao{
        return ApiUtils.getYemeklerDao()
    }

    @Provides
    @Singleton
    fun provideYemeklerDataSource(yemeklerDao: YemeklerDao) : YemeklerDataSource{
        return YemeklerDataSource(yemeklerDao)
    }

    @Provides
    @Singleton
    fun provideYemeklerRepository(yemeklerDataSource: YemeklerDataSource) : YemeklerRepository {
        return YemeklerRepository(yemeklerDataSource)
    }

}