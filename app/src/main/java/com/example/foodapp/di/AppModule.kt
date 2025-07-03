package com.example.foodapp.di

import com.example.foodapp.data.datasource.FoodsDataSource
import com.example.foodapp.data.repository.FoodsRepository
import com.example.foodapp.domain.service.UserService
import com.example.foodapp.retrofit.ApiUtils
import com.example.foodapp.retrofit.FoodsDao
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
    fun provideFoodsDao() : FoodsDao{
        return ApiUtils.getFoodsDao()
    }

    @Provides
    @Singleton
    fun provideFoodsDataSource(foodsDao: FoodsDao, userService: UserService) : FoodsDataSource{
        return FoodsDataSource(foodsDao, userService)
    }

    @Provides
    @Singleton
    fun provideFoodsRepository(foodsDataSource: FoodsDataSource) : FoodsRepository {
        return FoodsRepository(foodsDataSource)
    }

}