package uz.akmal.furortask.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import uz.akmal.furortask.model.ApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun getApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}