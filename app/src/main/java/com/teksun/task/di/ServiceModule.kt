package com.teksun.task.di

import com.teksun.task.network.AuthRepoImpl
import com.teksun.task.network.AuthRepository
import com.teksun.task.network.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Singleton
    @Provides
    fun providerService(retrofit: Retrofit):AuthService{
        return retrofit.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(repo: AuthRepoImpl):AuthRepository{
        return repo
    }


}